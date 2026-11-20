package com.zode.web.login.dto;

import com.zo.platform.sdk.common.kit.StrKit;
import com.zode.web.login.enums.LoginTypeEnum;
import lombok.Data;

import java.io.Serializable;

/**
 * @author zhoump
 * @date 2026/10/31
 * @purpose
 */
@Data
public class LoginParam implements Serializable {

    /**
     * 登录名
     */
    private String loginName;
    /**
     * 密码
     */
    private String password;
    /**
     * 微信openId
     */
    private String openId;
    /**
     * 短信验证码
     */
    private String smsCode;
    /**
     * 登录应用AppKey
     */
    private String appKey;


    /**
     * 登录类型
     */
    private String loginType = LoginTypeEnum.ACCOUNT_LOGIN.getCode();

    public boolean verifyParam(){
        if (LoginTypeEnum.ACCOUNT_LOGIN.getCode().equals(this.loginType)){
            return StrKit.isNotBlank(this.loginName) && StrKit.isNotBlank(this.password);
        } else if (LoginTypeEnum.WECHAT_LOGIN.getCode().equals(this.loginType)) {
            return StrKit.isNotBlank(this.openId);
        }else if (LoginTypeEnum.SMS.getCode().equals(this.loginType)) {
            return StrKit.isNotBlank(this.smsCode);
        }else{
            return false;
        }
    }
}
