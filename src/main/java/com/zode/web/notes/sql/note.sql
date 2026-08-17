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
        select zo_website_note_chapter_id as id,zo_website_note_id as note_id,title,orders,content,ifnull(complete,'0') as complete
        from zo_website_note_chapter
        where deleted = '0'
         #if(noteId)
         and zo_website_note_id = #para(noteId)
         #end
        order by orders asc
    #end

    #sql("queryChapter")
        select zo_website_note_chapter_id as id,title,orders,content
        from zo_website_note_chapter
        where deleted = '0'
          and zo_website_note_chapter_id = #para(id)
    #end

#end