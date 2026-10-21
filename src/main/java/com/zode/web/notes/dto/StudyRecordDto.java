package com.zode.web.notes.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author zhoump
 * @date 2026/9/7
 * @purpose
 */
@Data
public class StudyRecordDto {

    /**
     * 是否首次学习（前端字段名为 isFirst）
     * boolean + is 前缀时 Lombok 生成的 setter 为 setFirst，Jackson 推断的属性名会是 first，
     * 导致前端传来的 isFirst 被当作未知字段忽略而恒为 false，故显式声明 JSON 属性名。
     */
    @JsonProperty("isFirst")
    private boolean isFirst;
    private String relationId;
    private String timing;
    private String relationTypeText;
    private String relationTypeValue;
    private String ratingValue;
    private String ratingText;
}
