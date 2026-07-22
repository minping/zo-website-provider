package com.zode.web.author.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author zhoump
 * @date 2026/7/19
 * @purpose
 */
@Data
public class AuthorProductDto {

    private String id;
    private String name;
    private String img;
    private String description;
    private String playUrl;
    private String githubUrl;
    private String tags;
}
