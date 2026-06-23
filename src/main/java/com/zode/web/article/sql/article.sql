#namespace("website.web.article")

    #sql("queryAllTags")
        select zo_website_article_tag_id as tag_id,name as tag_name,color
        from zo_website_article_tag where deleted = '0' order by sort_order
    #end

    #sql("queryAllTagStatics")
        SELECT t.name AS tag_name,t.zo_website_article_tag_id AS tag_id,
               SUM(IFNULL(like_count,0)) AS like_count,
               SUM(IFNULL(view_count,0)) AS view_count,
               COUNT(a.zo_website_article_id) AS num,
               t.color
        FROM zo_website_article_tag t
                 LEFT JOIN zo_website_article a ON t.zo_website_article_tag_id = a.tag_value AND  a.deleted = '0' AND a.status_value = '1'
        WHERE t.deleted = '0'
        GROUP BY t.name,t.zo_website_article_tag_id
    #end

    #sql("queryArticle")
        select zo_website_article_id,title,content,tag_value,tag_text,author_name,date_format(create_time,'%Y-%m-%d %H:%i') create_time,
               date_format(publish_time,'%Y-%m-%d %H:%i') as publish_time,recommend_read_time,cover_gradient,summary,ifnull(like_count,0) as like_count,
               ifnull(view_count,0) as view_count,status_value,status_text,sort_order
        from zo_website_article
         where deleted = '0'
        #if(searchValue) and instr(title,#para(searchValue)) > 0 #end
        #if(tag) and tag_value = #para(tag) #end
        #if(status) and status_value = #para(status) #end
         order by status_value asc,create_time desc
    #end

    #sql("queryRecommendArticle")
        select zo_website_article_id,title,content,tag_value,tag_text,author_name,date_format(create_time,'%Y-%m-%d %H:%i') create_time,
               date_format(publish_time,'%Y-%m-%d %H:%i') as publish_time,date_format(publish_time,'%Y-%m-%d') as publish_day,
               recommend_read_time,cover_gradient,summary,ifnull(like_count,0) as like_count,
               ifnull(view_count,0) as view_count,status_value,status_text,sort_order
        from zo_website_article
        where deleted = '0'
        and tag_value = #para(tag)
        and zo_website_article_id != #para(id)
        and status_value = '1'
        order by ifnull(like_count,0)+ifnull(view_count,0) desc,sort_order asc,create_time desc
    #end

    #sql("queryIndexArticle")
        select zo_website_article_id,title,content,tag_value,tag_text,author_name,date_format(create_time,'%Y-%m-%d') create_time,
               date_format(publish_time,'%Y-%m-%d') as publish_time,recommend_read_time,cover_gradient,summary,ifnull(like_count,0) as like_count,
               ifnull(view_count,0) as view_count,status_value,status_text,sort_order
        from zo_website_article
        where deleted = '0'
        and status_value = '1'
        order by is_top desc,sort_order asc,create_time desc
        limit 0,8
    #end

    #sql("queryArticleById")
        select zo_website_article_id,
               title,
               content,
               tag_value,
               tag_text,
               author_name,
               date_format(create_time,'%Y-%m-%d') create_time,
               date_format(publish_time,'%Y-%m-%d') as publish_time,
               recommend_read_time,
               cover_gradient,
               summary,
               ifnull(like_count,0) as like_count,
               ifnull(view_count,0) as view_count,
               a.status_value,a.status_text,
               a.sort_order,
               t.color
        from zo_website_article a
        inner join zo_website_article_tag t on t.zo_website_article_tag_id = a.tag_value
        where zo_website_article_id = #para(id)
    #end

    #sql("queryArticleStatics")
        SELECT
            COUNT(zo_website_article_id) AS num,
            SUM(CASE WHEN status_value = '1' THEN 1 ELSE 0 END) AS publish_num,
            SUM(CASE WHEN status_value = '0' THEN 1 ELSE 0 END) AS un_publish_num,
            SUM(IFNULL(like_count,0)) AS like_count,
            SUM(IFNULL(view_count,0)) AS view_count
        FROM zo_website_article
        WHERE deleted = '0'
    #end

    #sql("viewArticle")
        UPDATE zo_website_article set  view_count  = view_count + 1 where zo_website_article_id =  #para(id)
    #end

    #sql("likeArticle")
         UPDATE zo_website_article set  like_count  = like_count + 1 where zo_website_article_id =  #para(id)
    #end

#end