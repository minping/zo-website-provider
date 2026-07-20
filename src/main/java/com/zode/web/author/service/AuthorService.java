package com.zode.web.author.service;

import com.common.util.Result;
import com.zode.web.author.dto.AuthorInfoDto;
import com.zode.web.author.dto.AuthorProjectDto;

/**
 * @author zhoump
 * @date 2026/7/19
 * @purpose
 */
public interface AuthorService {

    /**
     * 保存更新作者信息
     * @param authorInfoDto
     * @return
     */
    Result saveInfo(AuthorInfoDto authorInfoDto);

    /**
     * 查询作者信息
     * @param authId
     * @return
     */
    Result queryInfo(String authId);

    /**
     * 保存项目信息
     * @param projectDto
     * @return
     */
    Result saveProjectInfo(AuthorProjectDto projectDto);

    /**
     * 查询项目列表
     * @param authId
     * @return
     */
    Result queryProjectList(String authId);

    /**
     * 查询项目信息
     * @param id
     * @return
     */
    Result queryProjectInfo(String id);
}
