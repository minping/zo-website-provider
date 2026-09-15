package com.zode.web.admin.controller;

import com.common.base.BaseController;
import com.common.util.Result;
import com.jfinal.plugin.activerecord.Record;
import com.zode.web.article.dto.ArticleVo;
import com.zode.web.article.service.ArticleService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author zhoump
 * @date 2026/5/17
 * @purpose
 */
@RestController
@RequestMapping("/admin/article")
public class ArticleAdminController extends BaseController {


    @Resource
    private ArticleService articleService;

    @GetMapping("/queryAllTags")
    public Result queryAllTags(){
        List<Record> list = articleService.queryAllTags();
        return Result.me().success().setData(list);
    }


    @PostMapping("/insertTag")
    public Result insertTag(String tagName,String tagColor){
        return articleService.insertTag(tagName,tagColor) ? Result.me().success() : Result.me().error();
    }

    @GetMapping("/deleteTag")
    public Result deleteTag(String tagId){
        return articleService.deleteTag(tagId) ? Result.me().success() : Result.me().error();
    }

    @PostMapping("/insertArticle")
    public Result insertArticle(@RequestBody ArticleVo articleVo){
        articleVo.check();
        return articleService.insertArticle(articleVo) ? Result.me().success() : Result.me().error();
    }

    @GetMapping("/queryArticle")
    public Result queryArticle(String searchValue,String tag,String status){
        List<Record> list = articleService.queryArticle(searchValue, tag, status);
        return Result.me().success(list);
    }

    @GetMapping("/queryArticleDetail")
    public Result queryArticleDetail(String id){
        Record detail = articleService.queryArticleDetail(id);
        return Result.me().success(detail);
    }

    @GetMapping("/dealArticle")
    public Result dealArticle(String id,String type){
        articleService.dealArticle(id,type);
        return Result.me().success();
    }

    @PostMapping("/uploadImage")
    public Result uploadImage(@RequestParam("file") MultipartFile file){
        return articleService.uploadImage(file);
    }

}
