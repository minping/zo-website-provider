package com.zode.web.setting.dto;

import com.jfinal.plugin.activerecord.Record;
import lombok.Data;

/**
 * @author zhoump
 * @date 2026/7/26
 * @purpose
 */
@Data
public class SetDto {

    private String theme;

    public void parse(Record data){
        this.setTheme(data.getStr("theme"));
    }
}
