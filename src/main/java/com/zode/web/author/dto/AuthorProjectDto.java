package com.zode.web.author.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author zhoump
 * @date 2026/7/19
 * @purpose
 */
@Data
public class AuthorProjectDto {

    private String id;
    private String authorId;
    private String name;
    private String role;
    private String description;
    private String tech;
    private String image;
    @JsonProperty("start_time")
    private String startTime;
    @JsonProperty("end_time")
    private String endTime;
}
