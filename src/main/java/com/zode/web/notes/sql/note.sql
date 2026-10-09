#namespace("web.notes.sql")

    #sql("queryNoteList")
        SELECT zo_website_note_id AS id,name,author,DATE_FORMAT(created_time,'%Y年%m月%d日') AS created_time,DATE_FORMAT(updated_time,'%Y年%m月%d日') AS updated_time,preface,status_value,status_text
        FROM zo_website_note
        WHERE deleted = '0'
          #if(publish == '1')
          AND status_value = '1'
          #end
        ORDER BY created_time DESC
    #end

    #sql("queryChapterList")
        select zo_website_note_chapter_id as id,zo_website_note_id as note_id,title,orders,ifnull(complete,'0') as complete,
               tree_pid,tree_global_id,tree_level,text_char_count,code_line_count
        from zo_website_note_chapter
        where deleted = '0'
         #if(noteId)
         and zo_website_note_id = #para(noteId)
         #end
        order by tree_level asc,orders+0 asc
    #end

    #sql("queryChapter")
        select zo_website_note_chapter_id as id,title,orders,content
        from zo_website_note_chapter
        where deleted = '0'
          and zo_website_note_chapter_id = #para(id)
    #end

    #sql("queryStudyRecord")
        select date_format(created_time,'%Y-%m-%d %H:%i') as study_time,date_format(created_time,'%Y-%m-%d') as day,timing from zo_website_study_record
        where 1=1
          #if(relationId)
           and relation_id = #para(relationId)
          #end
        order by created_time desc
    #end

    #sql("getTodayNoStudy")
        SELECT c.zo_website_note_chapter_id AS id
        FROM zo_website_note_chapter c
         INNER JOIN zo_website_note n ON c.zo_website_note_id = n.zo_website_note_id
         INNER JOIN zo_website_note_chapter p ON c.tree_pid = p.zo_website_note_chapter_id AND p.deleted = '0'
        WHERE c.tree_level = '2'
          AND c.deleted = '0'
          AND c.content IS NOT NULL
          AND c.complete = '1'
          AND c.zo_website_note_chapter_id NOT IN (
            SELECT relation_id FROM zo_website_study_record WHERE DATE_FORMAT(created_time,'%Y-%m-%d') = #para(today) AND relation_type_value = '2'
            UNION
            SELECT relation_id FROM zo_website_study_ban WHERE deleted = '0'
        )
         #if(noteId)
          AND c.zo_website_note_id = #para(noteId)
        #end
    #end

    #sql("getTodayNoStudyById")
        SELECT c.zo_website_note_chapter_id AS id,CONCAT(p.title,': ',c.title) AS title,c.content,n.author,n.name
        FROM zo_website_note_chapter c
         INNER JOIN zo_website_note n ON c.zo_website_note_id = n.zo_website_note_id
         INNER JOIN zo_website_note_chapter p ON c.tree_pid = p.zo_website_note_chapter_id AND p.deleted = '0'
        WHERE  c.zo_website_note_chapter_id = #para(id)
    #end


    #sql("queryNoteDay")
        SELECT  DATE_FORMAT(nc.created_time,'%Y-%m-%d') AS day,COUNT(1) AS num,any_value(DATE_FORMAT(nc.created_time,'%Y-%m')) AS month
        FROM zo_website_note_chapter nc
                 INNER JOIN zo_website_note n ON nc.zo_website_note_id = n.zo_website_note_id AND n.deleted = '0'
        WHERE nc.deleted = '0'
          AND nc.tree_level = '2'
          AND nc.content IS NOT NULL
        GROUP BY DATE_FORMAT(nc.created_time,'%Y-%m-%d')
    #end

    #sql("querySonChapter")
        select * from zo_website_note_chapter where tree_pid = #para(pid) and deleted = '0'
    #end

    #sql("queryNoteLine")
        SELECT c.zo_website_note_chapter_id AS id,CONCAT(p.orders,'.',c.orders,' ',c.title) AS title,
               DATE_FORMAT(c.created_time,'%Y年%m月%d日') AS line_time,c.complete,c.created_time,
               DATE_FORMAT(c.created_time,'%m月') AS line_month
        FROM zo_website_note_chapter c
         INNER JOIN zo_website_note n ON c.zo_website_note_id = n.zo_website_note_id
         INNER JOIN zo_website_note_chapter p ON c.tree_pid = p.zo_website_note_chapter_id AND p.deleted = '0'
        WHERE c.tree_level = '2'
          AND c.deleted = '0'
          AND n.zo_website_note_id = #para(noteId)
        ORDER BY c.created_time ASC
    #end
#end