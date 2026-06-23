#namespace("website.web.api.sql")

    #sql("queryApiList")
        SELECT
            api.zo_website_api_id AS id,
            api.name,
            api.description,
            api.endpoint,
            api.method,
            api.tag_value,api.tag_text,
            api.is_free,
            api.response_format,
            api.request_example,
            api.like_count,
            api.response_time
        FROM zo_website_api api
        WHERE api.deleted = '0'
        ORDER BY api.created_time DESC
    #end

    #sql("queryApiParamList")
        SELECT
            *
        FROM zo_website_api_param
        WHERE zo_website_api_id = #para(apiId)
        AND deleted = '0'
    #end

    #sql("queryApiTagList")
        select
            zo_website_api_tag_id as tag_id,
            tag_name,tag_color
        from zo_website_api_tag
        where deleted = '0'
        order by sort_order asc,created_time desc
    #end

    #sql("querySomeApiList")
        SELECT
            api.zo_website_api_id AS id,
            api.name,
            api.description,
            api.endpoint,
            api.method,
            api.tag_value,api.tag_text,
            api.is_free,
            api.response_format,
            api.request_example,
            api.like_count,
            api.response_time
        FROM zo_website_api api
        WHERE api.deleted = '0'
        and api.status = '1'
        #if(searchValue)
        and instr(api.name ,#para(searchValue)) > 0
        #end
        #if(tag)
         and instr(api.tag_value,#para(tag)) > 0
        #end
        ORDER BY api.created_time DESC
    #end

#end