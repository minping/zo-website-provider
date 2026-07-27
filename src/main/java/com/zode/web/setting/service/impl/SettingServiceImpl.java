package com.zode.web.setting.service.impl;

import com.common.base.BaseService;
import com.common.util.Result;
import com.common.util.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.zode.web.setting.dto.SetDto;
import com.zode.web.setting.service.SettingService;
import org.springframework.stereotype.Service;

/**
 * @author zhoump
 * @date 2026/7/26
 * @purpose
 */
@Service
public class SettingServiceImpl extends BaseService implements SettingService {

    private final String SQL_KEY = "website.web.setting.sql.";

    /**
     * 获取系统配置
     *
     * @return
     */
    @Override
    public Result getSetting() {
        Record first = Db.findFirst(Db.getSqlPara(SQL_KEY + "getSetting"));
        SetDto setDto = new SetDto();
        if (first == null){
            Record setting = new Record();
            setting.set("zo_website_setting_id", StrKit.uuid());
            setting.set("app", "website");
            setting.set("theme", "light");
            setting.set("user_id", "light");
            Db.save("zo_website_setting",setting);
            setDto.parse(setting);
        }else{
            setDto.parse(first);
        }
        return success().setData(setDto);
    }

    /**
     * 切换系统主题
     *
     * @param theme
     * @return
     */
    @Override
    public Result switchTheme(String theme) {
        Record first = Db.findFirst(Db.getSqlPara(SQL_KEY + "getSetting"));
        if (first == null){
            Record setting = new Record();
            setting.set("zo_website_setting_id", StrKit.uuid());
            setting.set("app", "website");
            setting.set("theme", theme);
            setting.set("user_id", "");
            Db.save("zo_website_setting",setting);
        }else{
            first.set("theme", theme);
            Db.update("zo_website_setting","zo_website_setting_id",first);
        }
        return success();
    }
}
