package com.zode.web.login.enums;

import com.zo.platform.starter.exception.ZoException;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author zhoump
 * @date 2026/11/1
 * @purpose
 */
@Getter
@AllArgsConstructor
public enum ErrorCode {
    /**
     * 账号密码错误
     */
    USER_LOGIN_MISS(20511, "账号或密码错误");
    /**
     * 状态码
     */
    private Integer state;
    /**
     * 状态信息
     */
    private String message;

    public void doThrow() {
        throw ZoException.create(this.state,this.message);
    }

}
