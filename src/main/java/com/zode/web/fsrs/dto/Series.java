package com.zode.web.fsrs.dto;

import lombok.Data;

/**
 * @author zhoump
 * @date 2026/10/21
 * @purpose
 */
@Data
public class Series {
    private double x;
    private double y;

    public Series(double x,double y){
        this.x = x;
        this.y = y;
    }
}
