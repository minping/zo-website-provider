package com.zode.web.notes.controller;

import com.common.base.BaseController;
import com.common.util.Result;
import com.zode.web.notes.dto.ChapterSaveVo;
import com.zode.web.notes.dto.NoteSaveVo;
import com.zode.web.notes.dto.StudyBanDto;
import com.zode.web.notes.dto.StudyRecordDto;
import com.zode.web.notes.service.NotesService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author zhoump
 * @date 2026/8/15
 * @purpose 笔记后台维护模块
 */
@RestController
@RequestMapping("/index/notes")
public class NotesIndexController extends BaseController {

    @Resource
    private NotesService notesService;

    @GetMapping("/queryNoteDetail")
    public Result queryNoteDetail(String id){
        return notesService.queryNoteDetail(id);
    }

    @GetMapping("/queryNoteListPublished")
    public Result queryNoteListPublished(){
        return notesService.queryNoteListPublished();
    }

    @GetMapping("/queryChapter")
    public Result queryChapter(String id){
        return notesService.queryChapter(id);
    }

    @GetMapping("/queryChapterList")
    public Result queryChapterList(String noteId){
        return notesService.queryChapterList(noteId);
    }

    @PostMapping("/saveStudyRecord")
    public Result saveStudyRecord(@RequestBody StudyRecordDto studyRecordDto){
        return notesService.saveStudyRecord(studyRecordDto);
    }

    @GetMapping("/queryStudyRecord")
    public Result queryStudyRecord(String relationId){
        return notesService.queryStudyRecord(relationId);
    }

    @GetMapping("/getRandomContent")
    public Result getRandomContent(String noteId){
        return notesService.getRandomContent(noteId);
    }

    @PostMapping("/saveStudyBan")
    public Result saveStudyBan(@RequestBody StudyBanDto studyBanDto){
        return notesService.saveStudyBan(studyBanDto);
    }

    @GetMapping("/batchCountMd")
    public Result batchCountMd(){
        return notesService.batchCountMd();
    }

    @GetMapping("/getNoteLine")
    public Result getNoteLine(String noteId){
        return notesService.getNoteLine(noteId);
    }
}
