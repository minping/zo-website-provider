#namespace("web.notes.sql")

    #sql("queryNoteList")
        SELECT zo_website_note_id AS id,name,author,DATE_FORMAT(created_time,'%Y年%m月%d日') AS created_time,DATE_FORMAT(updated_time,'%Y年%m月%d日') AS updated_time,preface,status_value,status_text
        FROM zo_website_note
        WHERE deleted = '0'
          #if(publish == '1')
          AND status_value = '1'
          #end
        ORDER BY status_value DESC,updated_time DESC
    #end

    #sql("queryChapterList")
        select zo_website_note_chapter_id as id,zo_website_note_id as note_id,title,orders,ifnull(complete,'0') as complete,
               tree_pid,tree_global_id,tree_level,text_char_count,code_line_count,complete
        from zo_website_note_chapter
        where deleted = '0'
         #if(noteId)
         and zo_website_note_id = #para(noteId)
         #end
        order by tree_level asc,orders+0 asc
    #end

    #sql("queryChapter")
        SELECT nc.zo_website_note_chapter_id AS id,nc.title,nc.orders,nc.content,
               CONCAT(p.title,': ',nc.title) AS full_title,n.author,n.name,
               DATE_FORMAT(sr.next_review,'%m月%d日 %h:%i') AS next_review,
               CASE WHEN b.relation_id IS NULL THEN '0' ELSE '1' END AS is_ban,
               CASE WHEN a.relation_id IS NULL THEN '0' ELSE '1' END AS is_like
        FROM zo_website_note_chapter nc
                 INNER JOIN zo_website_note n ON nc.zo_website_note_id = n.zo_website_note_id
                 INNER JOIN zo_website_note_chapter p ON nc.tree_pid = p.zo_website_note_chapter_id AND p.deleted = '0'
                 LEFT JOIN (
            SELECT r.relation_id,r.next_review
            FROM zo_website_study_record r
                     INNER JOIN (
                SELECT relation_id, MAX(next_review) AS next_review
                FROM zo_website_study_record
                WHERE deleted = '0'
                GROUP BY relation_id
            ) m ON m.relation_id = r.relation_id AND r.next_review = m.next_review
        ) sr ON sr.relation_id = nc.zo_website_note_chapter_id
                 LEFT JOIN zo_website_study_ban b ON b.relation_id = nc.zo_website_note_chapter_id AND b.deleted = '0'
                 LEFT JOIN zo_website_study_like a ON a.relation_id = nc.zo_website_note_chapter_id AND a.deleted = '0'
        WHERE nc.deleted = '0'
          AND nc.zo_website_note_chapter_id = #para(id)
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

    #sql("queryNoteDay")
        SELECT  DATE_FORMAT(nc.created_time,'%Y-%m-%d') AS day,COUNT(1) AS num,any_value(DATE_FORMAT(nc.created_time,'%Y-%m')) AS month
        FROM zo_website_note_chapter nc
         INNER JOIN zo_website_note n ON nc.zo_website_note_id = n.zo_website_note_id AND n.deleted = '0'
        WHERE nc.deleted = '0'
          AND nc.tree_level = '2'
          AND nc.content IS NOT NULL
        GROUP BY DATE_FORMAT(nc.created_time,'%Y-%m-%d')
    #end

    #sql("queryReviewDay")
        SELECT  DATE_FORMAT(nc.created_time,'%Y-%m-%d') AS day,COUNT(1) AS num,any_value(DATE_FORMAT(nc.created_time,'%Y-%m')) AS month
        FROM zo_website_study_record nc
        WHERE nc.deleted = '0'
        #if(month)
        and DATE_FORMAT(nc.created_time,'%Y-%m') = #para(month)
        #end
        GROUP BY DATE_FORMAT(nc.created_time,'%Y-%m-%d')
        ORDER BY DATE_FORMAT(nc.created_time,'%Y-%m-%d') DESC
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