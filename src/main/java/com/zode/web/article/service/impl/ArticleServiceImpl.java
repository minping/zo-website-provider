package com.zode.web.article.service.impl;

import com.common.base.BaseService;
import com.common.util.DateKit;
import com.common.util.StrKit;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.zode.web.article.dto.ArticleVo;
import com.zode.web.article.service.ArticleService;
import com.zode.web.article.util.ColorGenerator;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * @author zhoump
 * @date 2026/5/17
 * @purpose
 */
@Service
public class ArticleServiceImpl extends BaseService implements ArticleService {

    public static final String SQL_KEY = "website.web.article.";

    @Override
    public List<Record> queryAllTags() {
        return Db.find(Db.getSqlPara(SQL_KEY + "queryAllTags"));
    }

    @Override
    public boolean insertTag(String tagName,String tagColor) {
        Record save = new Record();
        save.set("zo_website_article_tag_id", StrKit.uuid());
        save.set("name",tagName);
        save.set("color", StrKit.isBlank(tagColor) ? ColorGenerator.generateRandomHex() : tagColor);
        save.set("deleted",0);
        save.set("sort_order",0);

        return Db.save("zo_website_article_tag", "zo_website_article_tag_id", save);
    }

    @Override
    public boolean deleteTag(String tagId) {
        Record update = new Record();
        update.set("zo_website_article_tag_id", tagId);
        update.set("deleted", 1);
        return Db.update("zo_website_article_tag", "zo_website_article_tag_id", update);
    }

    @Override
    public boolean insertArticle(ArticleVo articleVo) {
        Record save = new Record();
        String id = articleVo.getId();
        boolean isAdd = false;
        if (StrKit.isBlank(id)){
            id = StrKit.uuid();
            isAdd = true;
        }
        save.set("zo_website_article_id",id);
        save.set("title",articleVo.getTitle());
        save.set("content",articleVo.getContent());
        save.set("summary",articleVo.getSummary());
        save.set("tag_value",articleVo.getTagValue());
        save.set("tag_text",articleVo.getTagText());
        save.set("author_name",articleVo.getAuthorName());
        if (isAdd){
            save.set("create_time", DateKit.getNowTime());
        }
        save.set("update_time", DateKit.getNowTime());
        if ("1".equals(articleVo.getStatusValue())){
            // 发布
            save.set("publish_time", DateKit.getNowTime());
        }
        save.set("recommend_read_time", articleVo.getRecommendReadTime());
        save.set("cover_gradient", articleVo.getCoverGradient());
        save.set("deleted", 0);
        if (isAdd){
            return Db.save("zo_website_article",save);
        }else{
            return Db.update("zo_website_article","zo_website_article_id",save);
        }
    }

    @Override
    public List<Record> queryArticle(String searchValue, String tag, String status) {
        Kv kv = Kv.create();
        if (StrKit.isNotBlank(searchValue)){
            kv.set("searchValue",searchValue);
        }
        if (StrKit.isNotBlank(tag)){
            kv.set("tag",tag);
        }
        if (StrKit.isNotBlank(status)){
            kv.set("status",status);
        }
        List<Record> articleList = Db.find(Db.getSqlPara(SQL_KEY + "queryArticle", kv));
        return articleList;
    }

    /**
     * 获取推荐文章
     *
     * @param id
     * @return
     */
    @Override
    public List<Record> queryRecommendArticle(String id) {
        Record article = Db.findFirst(Db.getSqlPara(SQL_KEY + "queryArticleById", Kv.by("id",id)));
        // 获取标签
        String tagValue = article.getStr("tag_value");
        List<Record> articleList = Db.find(Db.getSqlPara(SQL_KEY + "queryRecommendArticle", Kv.by("tag",tagValue).set("id",id)));
        return articleList;
    }

    @Override
    public Record queryArticleDetail(String id) {
        Record article = Db.findFirst(Db.getSqlPara(SQL_KEY + "queryArticleById", Kv.by("id",id)));
        return article;
    }

    @Override
    public void dealArticle(String id, String type) {
        Record update = new Record();
        update.set("zo_website_article_id",id);
        if ("delete".equals(type)){
            update.set("deleted",1);
        }else if ("publish".equals(type)){
            update.set("status_value","1");
            update.set("status_text","已发布");
            update.set("publish_time",DateKit.getNowTime());
        }else if ("unpublish".equals(type)){
            update.set("status_value","0");
            update.set("status_text","草稿");
            update.set("publish_time",null);
        }
        Db.update("zo_website_article","zo_website_article_id",update);
    }

    /**
     * 查看最新文章
     *
     * @return
     */
    @Override
    public List<Record> queryIndexArticle() {
        List<Record> articleList = Db.find(Db.getSqlPara(SQL_KEY + "queryIndexArticle"));
        return articleList;
    }

    @Override
    public Record queryArticleStatics() {

        return Db.findFirst(Db.getSqlPara(SQL_KEY + "queryArticleStatics"));
    }

    /**
     * 浏览文章
     *
     * @param id
     */
    @Override
    public void viewArticle(String id) {
        Db.update(Db.getSqlPara(SQL_KEY+"viewArticle",Kv.by("id",id)));
    }

    /**
     * 点赞文章
     *
     * @param id
     */
    @Override
    public void likeArticle(String id) {
        Db.update(Db.getSqlPara(SQL_KEY+"likeArticle",Kv.by("id",id)));
    }
}
