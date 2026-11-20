package com.zode.web.login.dto;

import lombok.Data;

/**
 * @author zhoump
 * @date 2026/10/31
 * @purpose
 */
@Data
public class LoginResult {

    private User user;

    private String authorizationToken;

    public static LoginResult build(User user,String token){
        LoginResult loginResult = new LoginResult();
        loginResult.setAuthorizationToken(token);
        loginResult.setUser(user);
        return loginResult;
    }

}
