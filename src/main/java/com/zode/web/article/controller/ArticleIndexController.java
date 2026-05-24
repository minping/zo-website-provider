package com.zode.web.article.controller;

import com.common.base.BaseController;
import com.common.util.Result;
import com.jfinal.plugin.activerecord.Record;
import com.zode.web.article.service.ArticleService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author zhoump
 * @date 2026/5/18
 * @purpose
 */
@RestController
@RequestMapping("/index/article")
public class ArticleIndexController extends BaseController {

    @Resource
    private ArticleService articleService;

    @GetMapping("/queryAllTags")
    public Result queryAllTags(){
        List<Record> list = articleService.queryAllTags();
        List<Map<String, Object>> res = list.stream().map(r -> r.getColumns()).collect(Collectors.toList());
        return Result.me().success().setData(res);
    }

    @GetMapping("/queryIndexArticle")
    public Result queryIndexArticle(){
        List<Record> list = articleService.queryIndexArticle();
        List<Map<String, Object>> res = list.stream().map(r -> r.getColumns()).collect(Collectors.toList());
        return Result.me().success().setData(res);
    }

    @GetMapping("/queryArticle")
    public Result queryArticle(String searchValue,String tag,String status){
        List<Record> list = articleService.queryArticle(searchValue, tag, status);
        List<Map<String, Object>> res = list.stream().map(r -> r.getColumns()).collect(Collectors.toList());
        return Result.me().success(res);
    }

    @GetMapping("/queryRecommendArticle")
    public Result queryRecommendArticle(String id){
        List<Record> list = articleService.queryRecommendArticle(id);
        List<Map<String, Object>> res = list.stream().map(r -> r.getColumns()).collect(Collectors.toList());
        return Result.me().success(res);
    }

    @GetMapping("/queryArticleDetail")
    public Result queryArticleDetail(String id){
        Record detail = articleService.queryArticleDetail(id);
        return Result.me().success(detail.getColumns());
    }

    @GetMapping("/viewArticle")
    public Result viewArticle(String id){
        articleService.viewArticle(id);
        return Result.me().success();
    }

    @GetMapping("/likeArticle")
    public Result likeArticle(String id){
        articleService.likeArticle(id);
        return Result.me().success();
    }
}
