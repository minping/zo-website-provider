package com.zode.web.admin.controller;

import com.common.util.Result;
import com.jfinal.plugin.activerecord.Record;
import com.zode.web.article.service.ArticleService;
import com.zode.web.notes.service.NotesService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author zhoump
 * @date 2026/9/12
 * @purpose
 */
@RestController
@RequestMapping("/admin/dashboard")
public class DashboardController {

    @Resource
    private ArticleService articleService;

    @Resource
    private NotesService notesService;

    @GetMapping("/queryArticleStatics")
    public Result queryArticleStatics(){
        Record record = articleService.queryArticleStatics();
        record.set("days",articleService.queryArticleDayStatics());
        return Result.me().success(record);
    }

    @GetMapping("/queryNoteStatics")
    public Result queryNoteStatics(String month){
        return notesService.queryNoteStatics(month);
    }

}
