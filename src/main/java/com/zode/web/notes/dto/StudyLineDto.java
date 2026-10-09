package com.zode.web.notes.dto;

import lombok.Data;

import java.util.List;

/**
 * @author zhoump
 * @date 2026/9/23
 * @purpose
 */
@Data
public class StudyLineDto {

    private String name;
    private String startTime;
    private String endTime;
    /**
     * 时间线
     */
    List<Line> lineList;
}

