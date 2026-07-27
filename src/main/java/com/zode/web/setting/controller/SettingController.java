package com.zode.web.setting.controller;

import com.common.util.Result;
import com.zode.web.setting.service.SettingService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author zhoump
 * @date 2026/7/26
 * @purpose
 */
@RestController
@RequestMapping("/setting")
public class SettingController {

    @Resource
    private SettingService settingService;

    @GetMapping("/getSetting")
    public Result getSetting(){
        return settingService.getSetting();
    }

    @PostMapping("/switchTheme")
    public Result switchTheme(String theme){
        return settingService.switchTheme(theme);
    }

}
