package com.zode.web.article.service;

import com.common.util.Result;
import com.jfinal.plugin.activerecord.Record;
import com.zode.web.article.dto.ArticleVo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author zhoump
 * @date 2026/5/17
 * @purpose
 */
public interface ArticleService {

    /**
     * 查询全部标签
     * @return
     */
    List<Record> queryAllTags();

    /**
     * 添加标签
     * @param tagName
     * @param tagColor
     * @return
     */
    boolean insertTag(String tagName,String tagColor);

    /**
     * 删除标签
     * @param tagId
     * @return
     */
    boolean deleteTag(String tagId);

    /**
     * 新增/更新 文章
     * @param articleVo
     * @return
     */
    boolean insertArticle(ArticleVo articleVo);

    /**
     * 文章列表
     * @param searchValue
     * @param tag
     * @param status
     * @return
     */
    List<Record> queryArticle(String searchValue, String tag, String status);

    /**
     * 文章详情
     * @param id
     * @return
     */
    Record queryArticleDetail(String id);

    /**
     * 文章 删除/发布/下架
     * @param id
     * @param type
     */
    void dealArticle(String id, String type);

    /**
     * 查看最新文章
     * @return
     */
    List<Record> queryIndexArticle();

    /**
     * 仪表盘文章统计
     * @return
     */
    Record queryArticleStatics();

    /**
     * 浏览文章
     * @param id
     */
    void viewArticle(String id);

    /**
     * 点赞文章
     * @param id
     */
    void likeArticle(String id);

    /**
     * 获取推荐文章
     * @param id
     * @return
     */
    List<Record> queryRecommendArticle(String id);

    /**
     * 技术标签统计
     * @return
     */
    List<Record> queryAllTagStatics();

    /**
     * 上传图片
     * @param file
     * @return
     */
    Result uploadImage(MultipartFile file);
}
