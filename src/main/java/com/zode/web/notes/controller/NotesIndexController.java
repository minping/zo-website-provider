package com.zode.web.notes.controller;

import com.common.base.BaseController;
import com.zo.platform.sdk.common.Result;
import com.zo.platform.starter.session.AuthIgnore;
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
@AuthIgnore
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


    @GetMapping("/getRandomContent")
    public Result getRandomContent(String noteId){
        return notesService.getRandomContent(noteId);
    }

    @GetMapping("/batchCountMd")
    public Result batchCountMd(){
        return notesService.batchCountMd();
    }

    @GetMapping("/getNoteLine")
    public Result getNoteLine(String noteId){
        return notesService.getNoteLine(noteId);
    }

    @GetMapping("/queryThinkingData")
    public Result queryThinkingData(){
        return notesService.queryThinkingData();
    }

}
