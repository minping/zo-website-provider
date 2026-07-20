package com.zode.web.author.controller;

import com.common.base.BaseController;
import com.common.util.Result;
import com.zode.web.author.dto.AuthorInfoDto;
import com.zode.web.author.dto.AuthorProjectDto;
import com.zode.web.author.service.AuthorService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author zhoump
 * @date 2026/7/19
 * @purpose
 */
@RestController
@RequestMapping("/auth")
public class AuthorController extends BaseController {

    @Resource
    AuthorService authorService;

    @PostMapping("/saveInfo")
    public Result saveInfo(@RequestBody AuthorInfoDto authorInfoDto){
        return authorService.saveInfo(authorInfoDto);
    }

    @GetMapping("/queryInfo")
    public Result queryInfo(String authId){
        return authorService.queryInfo(authId);
    }

    @PostMapping("/saveProjectInfo")
    public Result saveProjectInfo(@RequestBody AuthorProjectDto projectDto){
        return authorService.saveProjectInfo(projectDto);
    }

    @GetMapping("/queryProjectList")
    public Result queryProjectList(String authId){
        return authorService.queryProjectList(authId);
    }

    @GetMapping("/queryProjectInfo")
    public Result queryProjectInfo(String id){
        return authorService.queryProjectInfo(id);
    }
}
