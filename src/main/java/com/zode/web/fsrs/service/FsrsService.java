package com.zode.web.fsrs.service;

import com.zode.web.fsrs.calculator.FsrsCalculator;
import com.zode.web.fsrs.dto.CardState;
import com.zode.web.fsrs.dto.FsrsParameters;
import com.zode.web.fsrs.dto.Rating;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * FSRS 服务层封装
 * 提供面向业务的复习调度接口
 */
@Service
public class FsrsService {
    private final FsrsCalculator calculator;

    public FsrsService() {
        this.calculator = new FsrsCalculator();
    }

    public FsrsService(FsrsParameters parameters) {
        this.calculator = new FsrsCalculator(parameters);
    }

    /**
     * 新卡片第一次学习
     */
    public CardState firstLearn(Rating rating) {
        return calculator.initCard(rating, LocalDateTime.now());
    }

    /**
     * 复习卡片
     */
    public CardState reviewCard(CardState card, Rating rating) {
        return calculator.review(card, rating, LocalDateTime.now());
    }

    /**
     * 判断卡片是否到期需要复习
     */
    public boolean isDue(CardState card) {
        return LocalDateTime.now().isAfter(card.getNextReview());
    }
}