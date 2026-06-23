package com.zode.web.api.controller;

import com.common.base.BaseController;
import com.common.util.Result;
import com.zode.web.api.dto.ApiSaveDto;
import com.zode.web.api.service.ApiService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author zhoump
 * @date 2026/5/31
 * @purpose
 */
@RestController
@RequestMapping("/admin/api")
public class ApiAdminController extends BaseController {

    @Resource
    private ApiService apiService;

    @PostMapping("/saveApi")
    public Result saveApi(@RequestBody ApiSaveDto apiSaveDto){
        return apiService.saveApi(apiSaveDto);
    }

    @GetMapping("/deleteApi")
    public Result deleteApi(String id){
        return apiService.deleteApi(id);
    }

    @GetMapping("/queryApiList")
    public Result queryApiList(){
        return apiService.queryApiList();
    }

    @GetMapping("/queryApiById")
    public Result queryApiById(String id){
        return apiService.queryApiById(id);
    }

    @PostMapping("/saveApiTag")
    public Result saveApiTag(String id,String name,String color){
        return apiService.saveApiTag(id,name,color);
    }

    @GetMapping("/deleteApiTag")
    public Result deleteApiTag(String id){
        return apiService.deleteApiTag(id);
    }

    @GetMapping("/queryApiTagList")
    public Result queryApiTagList(){
        return apiService.queryApiTagList();
    }

}
