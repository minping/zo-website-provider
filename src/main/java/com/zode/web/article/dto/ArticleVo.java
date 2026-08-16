package com.zode.web.article.dto;

import com.common.util.StrKit;
import com.jfinal.plugin.activerecord.exception.ZoException;
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
    private String files;

    public void check(){
        if (StrKit.isBlank(title) || StrKit.isBlank(content) || StrKit.isBlank(summary)){
            throw ZoException.create(20001,"缺少必填参数");
        }
    }
}
