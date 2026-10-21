package com.zode.web.fsrs.calculator;

import com.zode.web.fsrs.dto.CardState;
import com.zode.web.fsrs.dto.FsrsParameters;
import com.zode.web.fsrs.dto.Rating;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.concurrent.ThreadLocalRandom;

/**
 * FSRS v5 核心算法计算器
 * 实现记忆状态更新、间隔计算、短期学习逻辑
 */
public class FsrsCalculator {
    private final FsrsParameters parameters;
    private final double[] w;

    public FsrsCalculator() {
        this(new FsrsParameters());
    }

    public FsrsCalculator(FsrsParameters parameters) {
        this.parameters = parameters;
        this.w = parameters.getWeights();
    }

    /**
     * 初始化新卡片的记忆状态（第一次复习）
     */
    public CardState initCard(Rating rating, LocalDateTime now) {
        CardState card = new CardState();
        int r = rating.getValue();

        double initStability = Math.max(w[r - 1], 0.1);
        double initDifficulty = initDifficulty(rating);

        card.setStability(initStability);
        card.setDifficulty(initDifficulty);
        card.setLastReview(now);
        card.setReviewCount(1);

        // 先算原始时间，再做睡眠调整
        double interval = calculateInterval(initStability);
        LocalDateTime rawNextReview = now.plusMinutes((long) (interval * 24 * 60));
        card.setNextReview(adjustForSleepTime(rawNextReview)); // 替换原逻辑

        card.setLearningPhase(calculateInterval(initStability) < parameters.getGraduationInterval());
        return card;
    }
    /**
     * 初始化新卡片难度（修正版：正确符号逻辑）
     */
    private double initDifficulty(Rating rating) {
        int r = rating.getValue();
        double initD = w[4] - w[5] * (r - 3);
        return clamp(initD, 1.0, 10.0);
    }

    /**
     * 处理复习，更新卡片状态
     */
    public CardState review(CardState card, Rating rating, LocalDateTime now) {
        // 计算距离上次复习的天数
        double deltaDays = calculateDeltaDays(card.getLastReview(), now);

        // 计算当前可提取性 R
        double retrievability = calculateRetrievability(deltaDays, card.getStability());

        // 更新难度
        double newDifficulty = updateDifficulty(card.getDifficulty(), rating);

        // 更新稳定性
        double newStability;
        if (deltaDays < 1.0 && card.isLearningPhase()) {
            // 短期学习阶段：使用短期稳定性公式
            newStability = updateShortTermStability(card.getStability(), rating);
        } else {
            // 长期复习阶段
            if (rating == Rating.AGAIN) {
                newStability = updateStabilityAfterFailure(card.getStability(), newDifficulty, retrievability);
            } else {
                newStability = updateStabilityAfterSuccess(card.getStability(), newDifficulty, retrievability, rating);
            }
        }

        // 钳位稳定性最小值
        newStability = Math.max(newStability, 0.1);

        // 计算下次间隔
        double nextInterval = calculateInterval(newStability);

        // 更新卡片状态
        card.setDifficulty(newDifficulty);
        card.setStability(newStability);
        card.setLastReview(now);
        card.setReviewCount(card.getReviewCount() + 1);
        // 先算原始时间，再做睡眠调整
        LocalDateTime rawNextReview = now.plusMinutes((long) (nextInterval * 24 * 60));
        card.setNextReview(adjustForSleepTime(rawNextReview)); // 替换原逻辑

        // 学习阶段毕业判定
        if (card.isLearningPhase() && nextInterval >= parameters.getGraduationInterval()) {
            card.setLearningPhase(false);
        }

        return card;
    }

    /**
     * 睡眠时间调整 + 随机模糊（仅夜间跨天睡眠，无午休）
     * 1. 若复习时间落在夜间睡眠区间内，顺延到睡眠结束时间
     * 2. 开启模糊后叠加随机分钟偏移，避免卡片堆积
     * @param rawNextReview 原始算法计算的下次复习时间
     * @return 调整后的复习时间
     */
    private LocalDateTime adjustForSleepTime(LocalDateTime rawNextReview) {
        LocalDateTime result = rawNextReview;

        // 1. 睡眠时间调整（仅跨天夜间睡眠）
        if (parameters.isEnableSleepAdjust()) {
            int startMinute = parameters.getSleepStartHour() * 60 + parameters.getSleepStartMinute();
            int endMinute = parameters.getSleepEndHour() * 60 + parameters.getSleepEndMinute();
            int currentMinute = result.getHour() * 60 + result.getMinute();

            // 跨天睡眠判断：当前时间 >= 入睡点 或 < 起床点
            boolean inSleepPeriod = currentMinute >= startMinute || currentMinute < endMinute;

            if (inSleepPeriod) {
                LocalTime sleepEnd = LocalTime.of(parameters.getSleepEndHour(), parameters.getSleepEndMinute());
                // 前半夜命中 → 次日起床；后半夜命中 → 当天起床
                if (currentMinute >= startMinute) {
                    result = result.toLocalDate().plusDays(1).atTime(sleepEnd);
                } else {
                    result = result.toLocalDate().atTime(sleepEnd);
                }
            }
        }

        // 2. 随机模糊偏移（全时段防堆积）
        if (parameters.isEnableFuzz() && parameters.getFuzzMaxMinutes() > 0) {
            int randomMinutes = ThreadLocalRandom.current().nextInt(parameters.getFuzzMaxMinutes() + 1);
            result = result.plusMinutes(randomMinutes);
        }

        return result;
    }

    /**
     * 计算两次复习间隔的天数
     */
    private double calculateDeltaDays(LocalDateTime lastReview, LocalDateTime now) {
        Duration duration = Duration.between(lastReview, now);
        return duration.toMinutes() / (24.0 * 60.0);
    }

    /**
     * 计算可提取性 R (遗忘曲线: 幂律模型)
     * R(t,S) = 1 / (1 + t/(9*S))
     */
    private double calculateRetrievability(double deltaDays, double stability) {
        if (deltaDays <= 0) return 1.0;
        return 1.0 / (1.0 + deltaDays / (9.0 * stability));
    }

    /**
     * 更新难度 D（修正版：含线性阻尼、正确变化方向）
     */
    private double updateDifficulty(double currentD, Rating rating) {
        int r = rating.getValue();
        // 1. 计算基础难度变化量
        double deltaD = -w[6] * (r - 3);

        // 2. 线性阻尼：难度越高，变化幅度越小
        double dampedDelta = deltaD * (10.0 - currentD) / 9.0;

        // 3. 原始更新后的值
        double newD = currentD + dampedDelta;

        // 4. 均值回归：向基准难度收敛
        double baseD = w[4];
        newD = w[7] * baseD + (1 - w[7]) * newD;

        // 5. 钳位到 [1, 10] 合法区间
        return clamp(newD, 1.0, 10.0);
    }

    /**
     * 短期稳定性更新（学习阶段内）
     * S' = S * e^(w17 * (rating - 3 + w18))
     */
    private double updateShortTermStability(double currentS, Rating rating) {
        int r = rating.getValue();
        return currentS * Math.exp(w[17] * (r - 3 + w[18]));
    }

    /**
     * 成功复习后的稳定性更新
     */
    private double updateStabilityAfterSuccess(double currentS, double newD, double retrievability, Rating rating) {
        double hardPenalty = rating == Rating.HARD ? w[10] : 1.0;
        double easyBonus = rating == Rating.EASY ? w[11] : 1.0;

        double factor = w[8] * Math.pow(newD, -w[9]);
        double growth = Math.exp(w[9] * (1 - retrievability)) - 1;

        return currentS * (1 + factor * growth * hardPenalty * easyBonus);
    }

    /**
     * 失败复习后的稳定性更新
     */
    private double updateStabilityAfterFailure(double currentS, double newD, double retrievability) {
        double factor = w[12] * Math.pow(newD, -w[13]);
        double decay = Math.pow(1 - retrievability, w[14]);
        return currentS * factor * decay;
    }

    /**
     * 根据稳定性计算下次复习间隔（天）
     * 由目标保留率反推间隔时间
     */
    private double calculateInterval(double stability) {
        double r = parameters.getRequestRetention();
        // 由 R = 1/(1 + t/(9S)) 推导 t = 9S * (1/r - 1)
        return 9.0 * stability * (1.0 / r - 1.0);
    }

    /**
     * 数值钳位
     */
    private double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }
}