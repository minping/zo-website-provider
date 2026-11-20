package com.zode.web.login.dto;

import com.jfinal.plugin.activerecord.Record;
import lombok.Data;

import java.util.Map;

/**
 * @author zhoump
 * @date 2026/10/31
 * @purpose
 */
@Data
public class User {
    private String userId;
    private String name;
    private String phone;
    private String openId;
    private String email;
    private String loginName;
    private String avatar;

    public static User fromRecord(Record r){
        User user = new User();
        user.setUserId(r.getStr("zo_platform_user_id"));
        user.setLoginName(r.getStr("login_name"));
        user.setPhone(r.getStr("phone"));
        user.setName(r.getStr("name"));
        user.setEmail(r.getStr("email"));
        user.setOpenId(r.getStr("open_id"));
        user.setAvatar(r.getStr("avatar"));
        return user;
    }
}
