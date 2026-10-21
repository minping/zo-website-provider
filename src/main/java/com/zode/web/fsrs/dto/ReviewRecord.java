package com.zode.web.fsrs.dto;

import lombok.Data;

/**
 * @author zhoump
 * @date 2026/10/21
 * @purpose 复习记录模型
 */
@Data
public class ReviewRecord {
    private final int day;       // 第几天复习
    private final int rating;    // 复习评分：1=忘记, 2=困难, 3=良好, 4=简单

    public ReviewRecord(int day, int rating) {
        this.day = day;
        this.rating = rating;
    }
}
