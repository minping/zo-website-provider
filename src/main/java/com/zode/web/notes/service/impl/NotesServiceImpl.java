package com.zode.web.notes.service.impl;

import com.common.base.BaseService;
import com.common.util.DateKit;
import com.common.util.Result;
import com.common.util.StrKit;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.zode.web.article.dto.ApiParamDto;
import com.zode.web.notes.dto.ChapterSaveVo;
import com.zode.web.notes.dto.NoteSaveVo;
import com.zode.web.notes.service.NotesService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author zhoump
 * @date 2026/8/15
 * @purpose
 */
@Service
public class NotesServiceImpl extends BaseService implements NotesService {

    private final String SQL_KEY = "web.notes.sql.";

    /**
     * 保存笔记
     *
     * @param noteSaveVo
     * @return
     */
    @Override
    public Result saveNote(NoteSaveVo noteSaveVo) {
        Record note = new Record();
        note.set("name",noteSaveVo.getName());
        note.set("author",noteSaveVo.getAuthor());
        note.set("preface",noteSaveVo.getPreface());
        note.set("status_value",noteSaveVo.getStatusValue());
        note.set("status_text","0".equals(noteSaveVo.getStatusValue()) ? "未发布": "已发布" );
        note.set("deleted","0");
        String noteId;
        if (StrKit.isBlank(noteSaveVo.getId())){
            // 新增
            noteId = StrKit.uuid();
            note.set("zo_website_note_id",noteId);
            note.set("created_time", DateKit.getNowTime());
            note.set("updated_time", DateKit.getNowTime());
            Db.save("zo_website_note",note);
        }else{
            // 更新
            noteId = noteSaveVo.getId();
            note.set("zo_website_note_id",noteId);
            note.set("updated_time", DateKit.getNowTime());
            Db.update("zo_website_note","zo_website_note_id",note);
        }
        return success();
    }

    /**
     * 更新发布状态
     *
     * @param id
     * @param status
     * @return
     */
    @Override
    public Result updateStatus(String id, String status) {
        if (StrKit.isBlank(id) || StrKit.isBlank(status)){
            return error().setMessage("参数错误");
        }
        Record note = new Record();
        note.set("zo_website_note_id",id);
        note.set("status_value",status);
        note.set("status_text","0".equals(status) ? "未发布": "已发布");
        Db.update("zo_website_note","zo_website_note_id",note);
        return success();
    }

    /**
     * 查询笔记详情
     *
     * @param id
     * @return
     */
    @Override
    public Result queryNoteDetail(String id) {
        Record note =  Db.findById("zo_website_note", "zo_website_note_id", id);
        note.set("id",note.getStr("zo_website_note_id"));
        return success().setData(note.getColumns());
    }

    /**
     * 删除笔记
     *
     * @param id
     * @return
     */
    @Override
    public Result deleteNote(String id) {
        if (StrKit.isBlank(id)){
            return error().setMessage("参数错误");
        }
        Record note = new Record();
        note.set("zo_website_note_id",id);
        note.set("deleted","1");
        Db.update("zo_website_note","zo_website_note_id",note);
        return success();
    }

    /**
     * 查询笔记列表
     *
     * @return
     */
    @Override
    public Result queryNoteList() {
        List<Record> noteList = Db.find(Db.getSqlPara(SQL_KEY + "queryNoteList"));
        List<Map<String, Object>> result = noteList.stream().map(r -> r.getColumns()).collect(Collectors.toList());
        return success().setData(result);
    }

    /**
     * 查询已发布笔记
     *
     * @return
     */
    @Override
    public Result queryNoteListPublished() {
        List<Record> noteList = Db.find(Db.getSqlPara(SQL_KEY + "queryNoteListPublished"));
        List<Map<String, Object>> result = noteList.stream().map(r -> r.getColumns()).collect(Collectors.toList());
        return success().setData(result);
    }

    /**
     * 保存章节
     *
     * @param chapterSaveVo
     * @return
     */
    @Override
    public Result saveChapter(ChapterSaveVo chapterSaveVo) {
        Record chapter = new Record();
        chapter.set("title",chapterSaveVo.getTitle());
        chapter.set("orders",chapterSaveVo.getOrders());
        chapter.set("zo_website_note_id",chapterSaveVo.getNoteId());
        chapter.set("deleted","0");
        if (StrKit.isNotBlank(chapterSaveVo.getContent())){
            chapter.set("content",chapterSaveVo.getContent());
        }
        String chapterId;
        if (StrKit.isBlank(chapterSaveVo.getId())){
            // 新增
            chapterId = StrKit.uuid();
            chapter.set("zo_website_note_chapter_id",chapterId);
            chapter.set("created_time", DateKit.getNowTime());
            chapter.set("updated_time", DateKit.getNowTime());
            Db.save("zo_website_note_chapter",chapter);
        }else{
            // 更新
            chapterId = chapterSaveVo.getId();
            chapter.set("zo_website_note_chapter_id",chapterId);
            chapter.set("updated_time", DateKit.getNowTime());
            Db.update("zo_website_note_chapter","zo_website_note_chapter_id",chapter);
        }
        return success();
    }

    /**
     * 笔记id
     *
     * @param noteId
     * @return
     */
    @Override
    public Result queryChapterList(String noteId) {
        List<Record> chapterList = Db.find(Db.getSqlPara(SQL_KEY + "queryChapterList", Kv.by("noteId",noteId)));
        List<Map<String, Object>> result = chapterList.stream().map(r -> r.getColumns()).collect(Collectors.toList());
        return success().setData(result);
    }

    /**
     * 查询章节详情
     *
     * @param id
     * @return
     */
    @Override
    public Result queryChapter(String id) {
        Record chapter = Db.findFirst(Db.getSqlPara(SQL_KEY + "queryChapter", Kv.by("id", id)));
        return success().setData(chapter.getColumns());
    }

    /**
     * 删除章节
     *
     * @param id
     * @return
     */
    @Override
    public Result deleteChapter(String id) {
        if (StrKit.isBlank(id)){
            return error().setMessage("参数错误");
        }
        Record note = new Record();
        note.set("zo_website_note_chapter_id",id);
        note.set("deleted","1");
        Db.update("zo_website_note_chapter","zo_website_note_chapter_id",note);
        return success();
    }
}
