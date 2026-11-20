package com.zode.web.login.service;

import com.zo.platform.sdk.common.Result;
import com.zode.web.login.dto.LoginParam;
import com.zode.web.login.dto.UserDetail;

/**
 * @author zhoump
 * @date 2026/10/31
 * @purpose
 */
public interface LoginService {

    /**
     * 注册用户
     * @return
     */
    Result saveUserInfo(UserDetail userDetail);

    /**
     * 用户登录
     * @param loginParam
     * @return
     */
    Result userLogin(LoginParam loginParam);

    /**
     * 用户登出
     * @return
     */
    Result userLogout();

}
