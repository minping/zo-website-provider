#namespace("website.web.index")

    #sql("queryArticleStatics")
        SELECT COUNT(zo_website_article_id) AS num,
               SUM(like_count) AS like_count,
               SUM(view_count) AS view_count
        FROM zo_website_article a
        WHERE deleted = '0' AND status_value = '1'
    #end

#end