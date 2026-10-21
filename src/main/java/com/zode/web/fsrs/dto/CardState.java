package com.zode.web.fsrs.dto;

import java.time.LocalDateTime;

/**
 * 卡片记忆状态
 * 存储FSRS核心状态变量与调度时间
 */
public class CardState {
    /** 难度 D: 1~10，数值越难记忆难度越高 */
    private double difficulty;

    /** 稳定性 S: 记忆稳定性，单位天，代表保留率降到90%所需的时间 */
    private double stability;

    /** 上次复习时间 */
    private LocalDateTime lastReview;

    /** 下次复习时间 */
    private LocalDateTime nextReview;

    /** 是否处于短期学习阶段 */
    private boolean learningPhase;

    /** 累计复习次数 */
    private int reviewCount;

    public CardState() {
        this.learningPhase = true; // 新卡片默认进入学习阶段
        this.reviewCount = 0;
    }

    // getter & setter
    public double getDifficulty() { return difficulty; }
    public void setDifficulty(double difficulty) { this.difficulty = difficulty; }
    public double getStability() { return stability; }
    public void setStability(double stability) { this.stability = stability; }
    public LocalDateTime getLastReview() { return lastReview; }
    public void setLastReview(LocalDateTime lastReview) { this.lastReview = lastReview; }
    public LocalDateTime getNextReview() { return nextReview; }
    public void setNextReview(LocalDateTime nextReview) { this.nextReview = nextReview; }
    public boolean isLearningPhase() { return learningPhase; }
    public void setLearningPhase(boolean learningPhase) { this.learningPhase = learningPhase; }
    public int getReviewCount() { return reviewCount; }
    public void setReviewCount(int reviewCount) { this.reviewCount = reviewCount; }
}
