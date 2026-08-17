package com.zode.web.notes.controller;

import com.common.base.BaseController;
import com.common.util.Result;
import com.zode.web.api.dto.ApiSaveDto;
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
@RequestMapping("/admin/notes")
public class NotesAdminController extends BaseController {

    @Resource
    private NotesService notesService;

    @PostMapping("/saveNote")
    public Result saveNote(@RequestBody NoteSaveVo noteSaveVo){
        return notesService.saveNote(noteSaveVo);
    }

    @GetMapping("/updateStatus")
    public Result updateStatus(String id,String status){
        return notesService.updateStatus(id,status);
    }

    @GetMapping("/queryNoteDetail")
    public Result queryNoteDetail(String id){
        return notesService.queryNoteDetail(id);
    }

    @GetMapping("/deleteNote")
    public Result deleteNote(String id){
        return notesService.deleteNote(id);
    }

    @GetMapping("/queryNoteList")
    public Result queryNoteList(){
        return notesService.queryNoteList();
    }

    @PostMapping("/saveChapter")
    public Result saveChapter(@RequestBody ChapterSaveVo chapterSaveVo){
        return notesService.saveChapter(chapterSaveVo);
    }

    @GetMapping("/queryChapter")
    public Result queryChapter(String id){
        return notesService.queryChapter(id);
    }

    @GetMapping("/queryChapterList")
    public Result queryChapterList(String noteId){
        return notesService.queryChapterList(noteId);
    }

    @GetMapping("/deleteChapter")
    public Result deleteChapter(String id){
        return notesService.deleteChapter(id);
    }

    @GetMapping("/completeChapter")
    public Result completeChapter(String id){
        return notesService.completeChapter(id);
    }

}
