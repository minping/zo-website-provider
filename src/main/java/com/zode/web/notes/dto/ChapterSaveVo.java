package com.zode.web.notes.dto;

import lombok.Data;

/**
 * @author zhoump
 * @date 2026/8/15
 * @purpose
 */
@Data
public class ChapterSaveVo {

    private String id;
    private String noteId;
    private String title;
    private String orders;
    private String content;

}
