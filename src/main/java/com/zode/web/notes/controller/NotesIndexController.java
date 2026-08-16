package com.zode.web.notes.controller;

import com.common.base.BaseController;
import com.common.util.Result;
import com.zode.web.notes.dto.ChapterSaveVo;
import com.zode.web.notes.dto.NoteSaveVo;
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
}
