package com.zode.web.notes.dto;

import lombok.Data;

/**
 * @author zhoump
 * @date 2026/8/15
 * @purpose
 */
@Data
public class NoteSaveVo {

    private String id;
    private String name;
    private String author;
    private String preface;
    private String statusValue;

}
