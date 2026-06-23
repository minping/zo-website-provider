package com.zode.web.api.controller;

import com.common.util.Result;
import com.jfinal.plugin.activerecord.Record;
import com.zode.web.api.service.ApiService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author zhoump
 * @date 2026/5/31
 * @purpose
 */
@RestController
@RequestMapping("/index/api")
public class ApiIndexController {

    @Resource
    private ApiService apiService;

    @GetMapping("/querySomeApiList")
    public Result querySomeApiList(){
        return apiService.querySomeApiList();
    }

    @GetMapping("/queryApiTagList")
    public Result queryApiTagList(){
        return apiService.queryApiTagList();
    }

    @GetMapping("/queryAllApiList")
    public Result queryAllApiList(String searchValue,String tag){
        return apiService.queryAllApiList(searchValue,tag);
    }

    @GetMapping("/queryApiById")
    public Result queryApiById(String id){
        return apiService.queryApiById(id);
    }
}
