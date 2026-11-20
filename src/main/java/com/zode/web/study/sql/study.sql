#namespace("web.study.sql")


    #sql("queryStudyRecord")
        select date_format(created_time,'%Y-%m-%d %H:%i') as study_time,date_format(created_time,'%Y-%m-%d') as day,timing,rating_value
        from zo_website_study_record
        where deleted = '0'
          and relation_id = #para(relationId)
        order by created_time asc
    #end

    #sql("queryLastStudyRecord")
     SELECT * FROM zo_website_study_record WHERE deleted = '0' AND relation_id = #para(relationId) ORDER BY created_time DESC
    #end

    #sql("queryTodayStudy")
        SELECT relation_id FROM zo_website_study_record
        WHERE DATE_FORMAT(created_time,'%Y-%m-%d') = DATE_FORMAT(NOW(),'%Y-%m-%d')
        GROUP BY relation_id
    #end

    #sql("queryTodayNeedStudy")
        SELECT relation_id, MAX(next_review) AS next_review
        FROM zo_website_study_record
        WHERE deleted = '0'
        GROUP BY relation_id
        HAVING NOW() >= MAX(next_review)
    #end

    #sql("queryHaveStudy")
        SELECT *,DATEDIFF(m.created_time,m.init_time) AS day_diff
        FROM zo_website_study_record r
        INNER JOIN (
            SELECT relation_id, MAX(created_time) AS created_time,MIN(created_time) AS init_time
            FROM zo_website_study_record
            WHERE deleted = '0'
            GROUP BY relation_id
        ) m ON m.relation_id = r.relation_id AND r.created_time = m.created_time
    #end

    #sql("queryAgainOrEasy")
        SELECT *
        FROM zo_website_study_record r
        INNER JOIN (
            SELECT relation_id, MIN(created_time) AS created_time
            FROM zo_website_study_record
            WHERE deleted = '0'
            GROUP BY relation_id
        ) m ON m.relation_id = r.relation_id AND r.created_time = m.created_time
        WHERE rating_value IN('1','4')
    #end

    #sql("queryNotesListByIds")
        SELECT c.zo_website_note_chapter_id AS id,CONCAT(p.title,': ',c.title) AS title,n.author,n.name,DATE_FORMAT(sr.next_review,'%m月%d日 %h:%i') as next_review
        FROM zo_website_note_chapter c
         INNER JOIN zo_website_note n ON c.zo_website_note_id = n.zo_website_note_id
         INNER JOIN zo_website_note_chapter p ON c.tree_pid = p.zo_website_note_chapter_id AND p.deleted = '0'
         LEFT JOIN (
            SELECT r.relation_id,r.next_review
            FROM zo_website_study_record r
             INNER JOIN (
                SELECT relation_id, MAX(next_review) AS next_review
                FROM zo_website_study_record
                WHERE deleted = '0'
                GROUP BY relation_id
            ) m ON m.relation_id = r.relation_id AND r.next_review = m.next_review
        ) sr on sr.relation_id = c.zo_website_note_chapter_id
        WHERE c.zo_website_note_chapter_id IN (
            #for(id:ids)
            #(for.index == 0 ? "" : ",")  #para(id)
            #end
        )
        order by next_review asc
    #end

    #sql("queryCancelFlow")
        SELECT relation_id
        FROM zo_website_study_ban
        WHERE deleted = '0'
        GROUP BY relation_id
    #end

    #sql("queryFlow")
        SELECT relation_id
        FROM zo_website_study_like
        WHERE deleted = '0'
        GROUP BY relation_id
    #end

    #sql("queryWeekStudy")
        SELECT DATE_FORMAT(created_time,'%Y-%m-%d') as dayStr,COUNT(relation_id) AS num
        FROM zo_website_study_record r
        WHERE deleted = '0'
        AND created_time BETWEEN #para(start) AND #para(end)
        GROUP BY DATE_FORMAT(created_time,'%Y-%m-%d')
    #end
#end