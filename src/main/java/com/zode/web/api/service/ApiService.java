package com.zode.web.api.service;

import com.common.util.Result;
import com.zode.web.api.dto.ApiSaveDto;

/**
 * @author zhoump
 * @date 2026/5/31
 * @purpose
 */
public interface ApiService {

    /**
     * 保存
     * @param apiSaveDto
     * @return
     */
    Result saveApi(ApiSaveDto apiSaveDto);

    /**
     * 删除
     * @param id
     * @return
     */
    Result deleteApi(String id);

    /**
     * 查询api列表
     * @return
     */
    Result queryApiList();

    /**
     * 查询api数据
     * @param id
     * @return
     */
    Result queryApiById(String id);

    /**
     * 保存标签
     * @param id
     * @param name
     * @param color
     * @return
     */
    Result saveApiTag(String id,String name, String color);

    /**
     * 删除标签
     * @param id
     * @return
     */
    Result deleteApiTag(String id);

    /**
     * 查询标签
     * @return
     */
    Result queryApiTagList();

    /**
     * 首页查询精选api
     * @return
     */
    Result querySomeApiList();

    /**
     * 查询api
     * @param searchValue
     * @param tag
     * @return
     */
    Result queryAllApiList(String searchValue, String tag);
}
