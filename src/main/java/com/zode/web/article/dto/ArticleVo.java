package com.zode.web.article.dto;

import lombok.Data;

/**
 * @author zhoump
 * @date 2026/5/18
 * @purpose
 */
@Data
public class ArticleVo {

    private String id;
    private String title;
    private String summary;
    private String content;
    private String authorName;
    private String tagValue;
    private String tagText;
    private String coverGradient;
    private String statusValue;
    private String statusText;
    private String recommendReadTime;
}
