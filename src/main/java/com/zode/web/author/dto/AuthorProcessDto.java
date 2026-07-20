package com.zode.web.author.dto;

import lombok.Data;

import java.util.Date;

/**
 * @author zhoump
 * @date 2026/7/19
 * @purpose
 */
@Data
public class AuthorProcessDto {

    private String id;
    private String time;
    private String event;
    private String deleted;
}
