package com.zode.web.notes.dto;

import lombok.Data;

import java.util.List;

@Data
public class Line {
    String lineTime;
    String lineMonth;
    List<Content> contentList;
}
