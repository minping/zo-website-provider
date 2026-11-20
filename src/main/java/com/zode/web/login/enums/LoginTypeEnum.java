package com.zode.web.login.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author zhoump
 * @date 2026/10/31
 * @purpose
 */
@Getter
@AllArgsConstructor
public enum LoginTypeEnum {

    ACCOUNT_LOGIN("account","账号登录"),
    WECHAT_LOGIN("open_id", "微信用户ID登录"),
    SMS("sms","短信快捷登录");

    private String code;
    private String desc;
}