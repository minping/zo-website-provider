package com.zode.web.login.dto;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zhoump
 * @date 2026/10/31
 * @purpose
 */
@Data
public class UserDetail extends User{

    private String password;
    private String sexText;
    private String sexValue;

    public Map<String,Object> parseMap(){
        Map<String,Object> map = new HashMap<>();
        map.put("zo_platform_user_id",this.getUserId());
        map.put("name",this.getName());
        map.put("sex_text",this.getSexText());
        map.put("sex_value",this.getSexValue());
        map.put("phone",this.getPhone());
        map.put("login_name",this.getLoginName());
        map.put("password",this.getPassword());
        map.put("email",this.getEmail());
        map.put("wx_open_id",this.getOpenId());
        return map;
    }
}
