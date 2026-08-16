package com.zode.web.notes.service;

import com.common.util.Result;
import com.zode.web.notes.dto.ChapterSaveVo;
import com.zode.web.notes.dto.NoteSaveVo;

/**
 * @author zhoump
 * @date 2026/8/15
 * @purpose
 */
public interface NotesService {

    /**
     * 保存笔记
     * @param noteSaveVo
     * @return
     */
    Result saveNote(NoteSaveVo noteSaveVo);

    /**
     * 更新发布状态
     * @param status
     * @return
     */
    Result updateStatus(String id, String status);

    /**
     * 查询笔记详情
     * @param id
     * @return
     */
    Result queryNoteDetail(String id);

    /**
     * 删除笔记
     * @param id
     * @return
     */
    Result deleteNote(String id);

    /**
     * 查询笔记列表
     * @return
     */
    Result queryNoteList();

    /**
     * 查询已发布笔记
     * @return
     */
    Result queryNoteListPublished();

    /**
     * 保存章节
     * @param chapterSaveVo
     * @return
     */
    Result saveChapter(ChapterSaveVo chapterSaveVo);

    /**
     * 笔记id
     * @param noteId
     * @return
     */
    Result queryChapterList(String noteId);

    /**
     * 查询章节详情
     * @param id
     * @return
     */
    Result queryChapter(String id);

    /**
     * 删除章节
     * @param id
     * @return
     */
    Result deleteChapter(String id);
}
