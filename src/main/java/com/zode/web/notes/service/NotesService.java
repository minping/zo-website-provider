package com.zode.web.notes.service;

import com.common.util.Result;
import com.zode.web.notes.dto.ChapterSaveVo;
import com.zode.web.notes.dto.NoteSaveVo;
import com.zode.web.notes.dto.StudyBanDto;
import com.zode.web.notes.dto.StudyRecordDto;

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

    /**
     * 完成本章节
     * @param id
     * @return
     */
    Result completeChapter(String id);

    /**
     * 学习打卡
     * @param studyRecordDto
     * @return
     */
    Result saveStudyRecord(StudyRecordDto studyRecordDto);

    /**
     * 查询历史学习打卡记录
     * @param relationId
     * @return
     */
    Result queryStudyRecord(String relationId);

    /**
     * 随机获取一篇笔记小节
     * @return
     */
    Result getRandomContent(String noteId);

    /**
     * 斩杀不需要复习的知识
     * @param studyBanDto
     * @return
     */
    Result saveStudyBan(StudyBanDto studyBanDto);

    /**
     * 控制面板统计相关
     *
     * @return
     */
    Result queryNoteStatics(String month);

    /**
     * 批量计算笔记
     * @return
     */
    Result batchCountMd();

    /**
     * 获取笔记时间线
     * @param noteId
     * @return
     */
    Result getNoteLine(String noteId);
}
