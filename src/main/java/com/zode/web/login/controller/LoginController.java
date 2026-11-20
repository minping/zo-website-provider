package com.zode.web.login.controller;

import com.zo.platform.sdk.common.Result;
import com.zo.platform.starter.session.AuthIgnore;
import com.zode.web.login.dto.LoginParam;
import com.zode.web.login.service.LoginService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author zhoump
 * @date 2026/10/31
 * @purpose
 */
@RequestMapping("/user/login")
@RestController
public class LoginController {

    @Resource
    private LoginService loginService;

    @PostMapping("userLogin")
    @AuthIgnore
    public Result userLogin(@RequestBody LoginParam loginParam){
        return loginService.userLogin(loginParam);
    }

    @PostMapping("userLogout")
    public Result userLogout(){
        return loginService.userLogout();
    }


}
