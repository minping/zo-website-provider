package com.zode.web.setting.service;

import com.common.util.Result;

/**
 * @author zhoump
 * @date 2026/7/26
 * @purpose
 */
public interface SettingService {

    /**
     * 获取系统配置
     * @return
     */
    Result getSetting();

    /**
     * 切换系统主题
     * @param theme
     * @return
     */
    Result switchTheme(String theme);

}
