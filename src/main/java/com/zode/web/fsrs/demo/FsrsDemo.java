package com.zode.web.fsrs.demo;

import com.zode.web.fsrs.dto.CardState;
import com.zode.web.fsrs.dto.Rating;
import com.zode.web.fsrs.service.FsrsService;

import java.time.format.DateTimeFormatter;

public class FsrsDemo {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static void main(String[] args) {
        FsrsService fsrsService = new FsrsService();

        System.out.println("=== 1. 新卡片首次学习（Good评分） ===");
        CardState card = fsrsService.firstLearn(Rating.HARD);
        printCardState(card);

        System.out.println("\n=== 2. 10分钟后复习（Hard评分）- 短期学习阶段 ===");
        // 模拟10分钟后复习
        card.setLastReview(card.getLastReview().plusMinutes(10));
        card = fsrsService.reviewCard(card, Rating.GOOD);
        printCardState(card);

        System.out.println("\n=== 3. 30分钟后复习（Good评分）- 学习阶段毕业 ===");
        card.setLastReview(card.getLastReview().plusMinutes(30));
        card = fsrsService.reviewCard(card, Rating.GOOD);
        printCardState(card);

        System.out.println("\n=== 4. 1天后复习（Easy评分）- 长期复习阶段 ===");
        card.setLastReview(card.getLastReview().plusDays(1));
        card = fsrsService.reviewCard(card, Rating.EASY);
        printCardState(card);

        System.out.println("\n=== 5. 5天后复习（Again评分）- 忘记了 ===");
        card.setLastReview(card.getLastReview().plusDays(5));
        card = fsrsService.reviewCard(card, Rating.GOOD);
        printCardState(card);
    }

    private static void printCardState(CardState card) {
        System.out.printf("难度 D: %.2f%n", card.getDifficulty());
        System.out.printf("稳定性 S: %.2f 天%n", card.getStability());
        System.out.printf("上次复习: %s%n", card.getLastReview().format(FORMATTER));
        System.out.printf("下次复习: %s%n", card.getNextReview().format(FORMATTER));
        System.out.printf("是否学习阶段: %s%n", card.isLearningPhase() ? "是" : "否");
        System.out.printf("复习次数: %d%n", card.getReviewCount());
    }
}
