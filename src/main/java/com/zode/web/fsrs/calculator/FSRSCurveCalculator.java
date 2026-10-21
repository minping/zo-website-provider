package com.zode.web.fsrs.calculator;

import com.zode.web.fsrs.dto.ReviewRecord;
import com.zode.web.fsrs.dto.Series;

import java.util.*;
import java.util.List;

/**
 * @author zhoump
 * @date 2026/10/21
 * @purpose 基于FSRS算法的记忆留存率曲线 计算器
 */
public class FSRSCurveCalculator {

    // ==================== FSRS核心常数（官方标准） ====================
    private static final double FACTOR = 19.0 / 81.0; // ≈0.2345679，由90%留存率定义推导
    private static final double DECAY = -0.5;         // 遗忘曲线形状参数，FSRS标准值


    // ==================== 核心算法方法 ====================

    /**
     * FSRS标准遗忘曲线：计算距离上次复习t天后的记忆留存率
     * @param t 距离上次复习的天数
     * @param S 当前记忆稳定性
     * @return 留存率（0~1，即回忆概率）
     */
    public static double forgettingCurve(double t, double S) {
        return Math.pow(1 + FACTOR * t / S, DECAY);
    }

    /**
     * 生成均匀的时间采样点（对应numpy.linspace）
     */
    private static double[] generateTimePoints(int totalDays, int points) {
        double[] time = new double[points];
        double step = (double) totalDays / (points - 1);
        for (int i = 0; i < points; i++) {
            time[i] = i * step;
        }
        return time;
    }

    /**
     * 计算无任何复习时的自然遗忘曲线
     */
    public static double[] calculateNoReviewCurve(int totalDays, double initialS, int points) {
        double[] time = generateTimePoints(totalDays, points);
        double[] retention = new double[points];
        for (int i = 0; i < points; i++) {
            retention[i] = forgettingCurve(time[i], initialS);
        }
        return retention;
    }

    /**
     * 根据复习记录计算动态留存率曲线
     * @param reviewRecords 复习记录列表
     * @param totalDays 总观察天数
     * @param initialS 初始记忆稳定性
     * @param points 曲线采样点数
     * @return 对应每个时间点的留存率数组
     */
    public static double[] calculateRetentionCurve(List<ReviewRecord> reviewRecords,
                                                   int totalDays,
                                                   double initialS,
                                                   int points) {
        // 按复习天数升序排序
        List<ReviewRecord> sortedRecords = new ArrayList<>(reviewRecords);
        sortedRecords.sort(Comparator.comparingInt(ReviewRecord::getDay));

        double[] time = generateTimePoints(totalDays, points);
        double[] retention = new double[points];

        double currentS = initialS;   // 当前记忆稳定性
        int lastReviewDay = 0;        // 上一次复习的天数
        int reviewIndex = 0;          // 已处理的复习记录下标

        for (int i = 0; i < points; i++) {
            double currentDay = time[i];

            // 检查是否经过新的复习点，经过则更新记忆稳定性
            while (reviewIndex < sortedRecords.size()
                    && currentDay >= sortedRecords.get(reviewIndex).getDay()) {
                ReviewRecord record = sortedRecords.get(reviewIndex);
                // 简化版稳定性更新逻辑（演示用，完整FSRS含更精细的DSR联动更新）
                switch (record.getRating()) {
                    case 1: currentS *= 0.5; break;   // 忘记，稳定性减半
                    case 2: currentS *= 1.8; break;   // 困难，小幅提升
                    case 3: currentS *= 2.8; break;   // 良好，中幅提升
                    case 4: currentS *= 4.2; break;   // 简单，大幅提升
                    default: break;
                }
                lastReviewDay = record.getDay();
                reviewIndex++;
            }

            // 计算当前时间点的留存率
            double deltaT = currentDay - lastReviewDay;
            retention[i] = forgettingCurve(deltaT, currentS);
        }
        return retention;
    }

    public static Map<String,List<Series>> parseLine(List<ReviewRecord> reviewRecords){
        Map<String,List<Series>> line = new HashMap<>();
        // ================= 可自定义参数 =================
        int totalDays = 30;          // 总观察天数
        double initialS = 1.0;       // 初始记忆稳定性（天）
        int samplePoints = 100;     // 曲线采样点数（越高越平滑）

        // ================= 计算曲线数据 =================
        double[] timePoints = generateTimePoints(totalDays, samplePoints);
        double[] noReviewData = calculateNoReviewCurve(totalDays, initialS, samplePoints);
        double[] withReviewData = calculateRetentionCurve(reviewRecords, totalDays, initialS, samplePoints);

        // 无复习曲线
        List<Series> noReviewSeries = new ArrayList<>(timePoints.length);
        for (int i = 0; i < samplePoints; i++) {
            noReviewSeries.add(new Series(timePoints[i], noReviewData[i]));
        }
        line.put("noReviewSeries",noReviewSeries);
        // 带复习曲线
        List<Series> withReviewSeries = new ArrayList<>(timePoints.length);
        for (int i = 0; i < samplePoints; i++) {
            withReviewSeries.add(new Series(timePoints[i], withReviewData[i]));
        }
        line.put("withReviewSeries",withReviewSeries);
        return line;
    }
}
