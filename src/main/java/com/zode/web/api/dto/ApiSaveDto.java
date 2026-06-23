package com.zode.web.api.dto;

import com.zode.web.article.dto.ApiParamDto;
import lombok.Data;

import java.util.List;

/**
 * @author zhoump
 * @date 2026/6/20
 * @purpose
 */
@Data
public class ApiSaveDto {

    private String id;
    private String name;
    private String description;
    private String endpoint;
    private String method;
    private String tagValue;
    private String tagText;
    // 是否免费：1-免费，0-付费
    private String isFree;
    private String responseFormat;
    private String requestExample;
    private String responseExample;
    private String status;
    private String responseTime;

    private List<ApiParamDto> paramList;
}
