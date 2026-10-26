package com.zode.web.study.service;

import com.common.util.Result;
import com.zode.web.study.dto.StudyDto;
import com.zode.web.notes.dto.StudyRecordDto;

/**
 * @author zhoump
 * @date 2026/10/24
 * @purpose
 */
public interface StudyService {

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
     * 取消关注
     * @param studyDto
     * @return
     */
    Result saveStudyBan(StudyDto studyDto);

    /**
     * 重点关注
     * @param studyDto
     * @return
     */
    Result saveStudyLike(StudyDto studyDto);

    /**
     * 获取学习域数据
     * @return
     */
    Result queryStudyDomain();

    /**
     * 获取列表
     * @param type 列表类型
     * @return
     */
    Result queryStudyDomainNoticeList(String type);
}
