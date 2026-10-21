package com.zode.web.fsrs.dto;

/**
 * FSRS v5 算法参数
 * 默认权重为FSRS官方训练的通用最优参数
 */
public class FsrsParameters {
    /** FSRS v5 默认权重数组 (共19个参数) */
    private static final double[] DEFAULT_WEIGHTS = {
            0.4072,  // w0: Again 初始稳定性
            1.1829,  // w1: Hard 初始稳定性
            2.9743,  // w2: Good 初始稳定性
            5.0231,  // w3: Easy 初始稳定性
            4.9312,  // w4: 难度基准值
            0.9447,  // w5: 难度指数因子
            0.8617,  // w6: 难度变化率
            0.0100,  // w7: 均值回归权重
            1.4908,  // w8: 稳定性增长基础乘数
            0.1448,  // w9: 稳定性增长幂次因子
            0.9436,  // w10: Hard 评分惩罚系数
            2.1827,  // w11: Easy 评分奖励系数
            0.0511,  // w12: 失败后稳定性衰减基准
            0.3408,  // w13: 失败后稳定性衰减难度因子
            1.2613,  // w14: 失败后稳定性衰减幂次
            0.2926,  // w15: 失败后难度增量
            2.6125,  // w16: 失败后难度上限
            0.5000,  // w17: 短期稳定性基数
            0.2000   // w18: 短期稳定性评分偏移
    };

    /** 目标保留率 (默认90%) */
    private double requestRetention = 0.9;

    /** 权重数组 */
    private double[] weights;

    /** 学习阶段毕业阈值（天）：间隔超过该值则从学习阶段进入长期复习 */
    private double graduationInterval = 1.0;

    // ========== 夜间睡眠配置（仅跨天场景，无午休） ==========
    /** 是否开启睡眠时间调整 */
    private boolean enableSleepAdjust = true;
    /** 夜间睡眠开始时间-小时（24小时制，默认23点） */
    private int sleepStartHour = 22;
    /** 夜间睡眠开始时间-分钟 */
    private int sleepStartMinute = 0;
    /** 睡眠结束时间-小时（次日早晨，默认7点） */
    private int sleepEndHour = 7;
    /** 睡眠结束时间-分钟 */
    private int sleepEndMinute = 0;

    // ========== 防堆积随机模糊配置 ==========
    /** 是否开启随机偏移（避免卡片集中在同一时间点） */
    private boolean enableFuzz = true;
    /** 随机偏移最大分钟数，默认120分钟（2小时） */
    private int fuzzMaxMinutes = 60;

    public FsrsParameters() {
        this.weights = DEFAULT_WEIGHTS.clone();
    }

    // 自定义参数构造
    public FsrsParameters(double[] weights, double requestRetention, double graduationInterval) {
        this.weights = weights.clone();
        this.requestRetention = requestRetention;
        this.graduationInterval = graduationInterval;
    }

    // getter
    public double[] getWeights() { return weights; }
    public double getRequestRetention() { return requestRetention; }
    public double getGraduationInterval() { return graduationInterval; }

    public boolean isEnableSleepAdjust() { return enableSleepAdjust; }
    public void setEnableSleepAdjust(boolean enableSleepAdjust) { this.enableSleepAdjust = enableSleepAdjust; }
    public int getSleepStartHour() { return sleepStartHour; }
    public void setSleepStartHour(int sleepStartHour) { this.sleepStartHour = sleepStartHour; }
    public int getSleepStartMinute() { return sleepStartMinute; }
    public void setSleepStartMinute(int sleepStartMinute) { this.sleepStartMinute = sleepStartMinute; }
    public int getSleepEndHour() { return sleepEndHour; }
    public void setSleepEndHour(int sleepEndHour) { this.sleepEndHour = sleepEndHour; }
    public int getSleepEndMinute() { return sleepEndMinute; }
    public void setSleepEndMinute(int sleepEndMinute) { this.sleepEndMinute = sleepEndMinute; }
    public boolean isEnableFuzz() { return enableFuzz; }
    public void setEnableFuzz(boolean enableFuzz) { this.enableFuzz = enableFuzz; }
    public int getFuzzMaxMinutes() { return fuzzMaxMinutes; }
    public void setFuzzMaxMinutes(int fuzzMaxMinutes) { this.fuzzMaxMinutes = fuzzMaxMinutes; }
}
