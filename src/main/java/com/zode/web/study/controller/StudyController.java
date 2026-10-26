package com.zode.web.study.controller;

import com.common.util.Result;
import com.zode.web.study.dto.StudyDto;
import com.zode.web.notes.dto.StudyRecordDto;
import com.zode.web.study.service.StudyService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author zhoump
 * @date 2026/10/24
 * @purpose
 */
@RestController
@RequestMapping("/index/study")
public class StudyController {

    @Resource
    StudyService studyService;


    @PostMapping("/saveStudyRecord")
    public Result saveStudyRecord(@RequestBody StudyRecordDto studyRecordDto){
        return studyService.saveStudyRecord(studyRecordDto);
    }

    @GetMapping("/queryStudyRecord")
    public Result queryStudyRecord(String relationId){
        return studyService.queryStudyRecord(relationId);
    }

    @PostMapping("/saveStudyBan")
    public Result saveStudyBan(@RequestBody StudyDto studyDto){
        return studyService.saveStudyBan(studyDto);
    }

    @PostMapping("/saveStudyLike")
    public Result saveStudyLike(@RequestBody StudyDto studyDto){
        return studyService.saveStudyLike(studyDto);
    }

    @GetMapping("/queryStudyDomain")
    public Result queryStudyDomain(){
        return studyService.queryStudyDomain();
    }

    @GetMapping("/queryStudyDomainNoticeList")
    public Result queryStudyDomainNoticeList(String type){
        return studyService.queryStudyDomainNoticeList(type);
    }

}

