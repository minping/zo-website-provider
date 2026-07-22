#namespace("website.web.auth.sql")

    #sql("queryInfoById")
        select
            zo_website_author_id as id,
            name,
            post,
            email,
            github,
            self,
            avatar,
            frontend,
            backend,
            tools,
            devops,
            work_time
            from zo_website_author
    #end

    #sql("queryProcessById")
        select
            zo_website_author_process_id as id,
            zo_website_author_id as authorId,
            date_format(time,'%Y-%m-%d') as time,
            event,
            deleted
        from zo_website_author_process
        where zo_website_author_id = #para(id)
        and deleted = '0'
        order by time
    #end

    #sql("queryProjectList")
        select
            zo_website_author_project_id as id,
            zo_website_author_id as author_id,
            name,
            role,
            description,
            tech,
            image,
            start_time,
            end_time,
            finish_status,
            user_num
        from zo_website_author_project
        where deleted = '0'
        order by start_time desc
    #end

    #sql("queryProjectById")
        select
            zo_website_author_project_id as id,
            zo_website_author_id as author_id,
            name,
            role,
            description,
            tech,
            image,
            start_time,
            end_time,
            finish_status,
            user_num
        from zo_website_author_project
        where deleted = '0'
        and zo_website_author_project_id = #para(id)
        order by start_time desc
    #end

    #sql("queryProductList")
        select
            zo_website_author_product_id as id,
            zo_website_author_id as author_id,
            name,
            img,
            description,
            play_url,
            github_url,
            tags
        from zo_website_author_product
        where deleted = '0'
        order by zo_website_author_product_id desc
    #end

    #sql("queryProductById")
        select
            zo_website_author_product_id as id,
            zo_website_author_id as author_id,
            name,
            img,
            description,
            play_url,
            github_url,
            tags
        from zo_website_author_product
        where deleted = '0'
        and zo_website_author_product_id = #para(id)
    #end

#end