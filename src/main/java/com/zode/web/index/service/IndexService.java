package com.zode.web.index.service;

import com.zo.platform.sdk.common.Result;

/**
 * @author zhoump
 * @date 2026/5/21
 * @purpose
 */
public interface IndexService {

    /**
     * 首页 统计
     * @return
     */
    Result queryStatics();

    /**
     * 关于的信息接口
     * @param userId
     * @return
     */
    Result aboutMe(String userId);
}
