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
            end_time
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
            end_time
        from zo_website_author_project
        where deleted = '0'
        and zo_website_author_project_id = #para(id)
        order by start_time desc
    #end

#end