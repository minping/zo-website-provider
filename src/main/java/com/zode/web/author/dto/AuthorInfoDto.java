package com.zode.web.author.dto;

import lombok.Data;

import java.util.List;

/**
 * @author zhoump
 * @date 2026/7/19
 * @purpose
 */
@Data
public class AuthorInfoDto {

    private String id;
    private String name;
    private String post;
    private String email;
    private String github;
    private String self;
    private String avatar;
    private String frontend;
    private String backend;
    private String tools;
    private String devops;
    private String workTime;
    // 历程
    List<AuthorProcessDto> processDtoList;
}
