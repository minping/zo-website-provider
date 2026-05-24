package com.zode.web.index.controller;

import com.common.base.BaseController;
import com.common.util.Result;
import com.zode.web.index.service.IndexService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author zhoump
 * @date 2026/5/21
 * @purpose
 */
@RestController
@RequestMapping("/index")
public class IndexController extends BaseController {

    @Resource
    IndexService indexService;

    @GetMapping("/queryStatics")
    public Result queryStatics(){
        return indexService.queryStatics();
    }

}
