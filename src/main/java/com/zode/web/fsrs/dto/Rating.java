package com.zode.web.fsrs.dto;

/**
 * FSRS 复习评分
 * 1-again(忘记) 2-hard(困难) 3-good(记住) 4-easy(简单)
 */
public enum Rating {
    AGAIN(1),
    HARD(2),
    GOOD(3),
    EASY(4);

    private final int value;

    Rating(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static Rating of(int value) {
        for (Rating rating : values()) {
            if (rating.value == value) {
                return rating;
            }
        }
        throw new IllegalArgumentException("Invalid rating value: " + value);
    }
}
