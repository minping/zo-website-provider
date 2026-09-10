package com.zode.web.notes.service.impl;

import com.common.base.BaseService;
import com.common.util.DateKit;
import com.common.util.Result;
import com.common.util.StrKit;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.TreeInfo;
import com.zode.web.article.dto.ApiParamDto;
import com.zode.web.notes.dto.ChapterSaveVo;
import com.zode.web.notes.dto.NoteSaveVo;
import com.zode.web.notes.dto.StudyBanDto;
import com.zode.web.notes.dto.StudyRecordDto;
import com.zode.web.notes.service.NotesService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author zhoump
 * @date 2026/8/15
 * @purpose
 */
@Service
public class NotesServiceImpl extends BaseService implements NotesService {

    private final String SQL_KEY = "web.notes.sql.";

    private static final Random RANDOM = new Random();

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
        return success().setData(note);
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
        List<Record> chapterList = Db.find(Db.getSqlPara(SQL_KEY + "queryChapterList"));
        Map<String, List<Record>> chapterMap = chapterList.stream().collect(Collectors.groupingBy(c -> c.getStr("note_id")));
        noteList.forEach(note -> {
            List<Record> cList = chapterMap.getOrDefault(note.getStr("id"), new LinkedList<>());
            if (cList.isEmpty()){
                note.set("process",0);
            }else{
                long complete = cList.stream().filter(c -> "1".equals(c.getStr("complete"))).count();
                BigDecimal process = new BigDecimal(complete).multiply(new BigDecimal(100)).divide(new BigDecimal(cList.size()), 2, RoundingMode.HALF_UP);
                note.set("process",process.doubleValue());
            }
        });
        return success().setData(noteList);
    }

    /**
     * 查询已发布笔记
     *
     * @return
     */
    @Override
    public Result queryNoteListPublished() {
        List<Record> noteList = Db.find(Db.getSqlPara(SQL_KEY + "queryNoteList",Kv.by("publish","1")));
        List<Record> chapterList = Db.find(Db.getSqlPara(SQL_KEY + "queryChapterList"));
        Map<String, List<Record>> chapterMap = chapterList.stream().collect(Collectors.groupingBy(c -> c.getStr("note_id")));
        noteList.forEach(note -> {
            List<Record> cList = chapterMap.getOrDefault(note.getStr("id"), new LinkedList<>());
            if (cList.isEmpty()){
                note.set("process",0);
                note.set("chapter_num",0);
                note.set("section_num",0);
            }else{
                long complete = cList.stream().filter(c -> "1".equals(c.getStr("complete"))).count();
                BigDecimal process = new BigDecimal(complete).multiply(new BigDecimal(100)).divide(new BigDecimal(cList.size()), 2, RoundingMode.HALF_UP);
                note.set("process",process.doubleValue());
                note.set("chapter_num",cList.stream().filter(c -> "1".equals(c.getStr("tree_level"))).count());
                note.set("section_num",cList.stream().filter(c -> "2".equals(c.getStr("tree_level"))).count());
            }
        });
        return success().setData(noteList);
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
        String chapterId = StrKit.isBlank(chapterSaveVo.getId()) ? StrKit.uuid() : chapterSaveVo.getId();
        chapter.set("zo_website_note_chapter_id",chapterId);
        chapter.set("updated_time", DateKit.getNowTime());

        // 处理树结构
        if (StrKit.isNotBlank(chapterSaveVo.getPid())){
            Record pInfo = Db.findById("zo_website_note_chapter", "zo_website_note_chapter_id", chapterSaveVo.getPid());
            chapter.set("tree_pid",pInfo.getStr("zo_website_note_chapter_id"));
            chapter.set("tree_global_id",pInfo.getStr("zo_website_note_chapter_id")+"."+chapterId);
            chapter.set("tree_level",Integer.parseInt(pInfo.getStr("tree_level"))+1);
        }else{
            chapter.set("tree_pid",null);
            chapter.set("tree_global_id",chapterId);
            chapter.set("tree_level",1);
        }
        if (StrKit.isBlank(chapterSaveVo.getId())){
            // 新增
            chapter.set("created_time", DateKit.getNowTime());
            chapter.set("complete","0");
            Db.save("zo_website_note_chapter",chapter);
        }else{
            // 更新
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
        List<Record> chapterList = Db.findTree(new TreeInfo("id", "tree_pid"), Db.getSqlPara(SQL_KEY + "queryChapterList", Kv.by("noteId", noteId)));
        chapterList.forEach(c -> {
            if (c.get("children") != null){
                List<Record> cl = c.get("children");
                List<Map<String, Object>> clm = cl.stream().map(r -> r.getColumns()).collect(Collectors.toList());
                c.set("children",clm);
            }
        });
        return success().setData(chapterList);
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
        return success().setData(chapter);
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

    /**
     * 完成本章节
     *
     * @param id
     * @return
     */
    @Override
    public Result completeChapter(String id) {
        if (StrKit.isBlank(id)){
            return error().setMessage("参数错误");
        }
        Record note = new Record();
        note.set("zo_website_note_chapter_id",id);
        note.set("complete","1");
        Db.update("zo_website_note_chapter","zo_website_note_chapter_id",note);
        return success();
    }

    /**
     * 学习打卡
     *
     * @param studyRecordDto
     * @return
     */
    @Override
    public Result saveStudyRecord(StudyRecordDto studyRecordDto) {
        Record save = new Record();
        save.set("zo_website_study_record_id",StrKit.uuid());
        save.set("created_time",new Date());
        save.set("relation_id",studyRecordDto.getRelationId());
        save.set("relation_type_text",studyRecordDto.getRelationTypeText());
        save.set("relation_type_value",studyRecordDto.getRelationTypeValue());
        save.set("timing",studyRecordDto.getTiming());
        Db.save("zo_website_study_record",save);
        return success();
    }

    /**
     * 查询历史学习打卡记录
     *
     * @param relationId
     * @return
     */
    @Override
    public Result queryStudyRecord(String relationId) {
        Record result = new Record();
        List<Record> recordList = Db.find(Db.getSqlPara(SQL_KEY + "queryStudyRecord", Kv.by("relationId", relationId)));
        result.set("history",recordList.stream().map(Record::getColumns).collect(Collectors.toList()));
        List<Record> totalList = Db.find(Db.getSqlPara(SQL_KEY + "queryStudyRecord"));
        String today = DateKit.formatDate2Str(new Date(), DateKit.YYYY_MM_DD);
        long todayCount = totalList.stream().filter(r -> StrKit.equals(r.getStr("day"), today)).count();
        result.set("todayCount",todayCount);
        result.set("totalCount",totalList.size());
        long totalTiming = totalList.stream().map(r->Long.parseLong(r.getStr("timing"))).reduce(0L,Long::sum);
        result.set("totalTiming",secondToHms(totalTiming));
        Optional<Record> optional = recordList.stream().filter(r -> StrKit.equals(r.getStr("day"), today)).findAny();
        result.set("totalStatus",optional.isPresent());
        return success().setData(result);
    }

    /**
     * 将总秒数转为 时分秒字符串 HH:mm:ss
     * @param totalSeconds 总秒数
     * @return HH:mm:ss
     */
    public static String secondToHms(long totalSeconds) {
        if (totalSeconds < 0) {
            return "00:00:00";
        }
        long hours = totalSeconds / 3600;
        long remain = totalSeconds % 3600;
        long minutes = remain / 60;
        long seconds = remain % 60;
        // 格式化，不足两位补0
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    /**
     * 随机获取一篇笔记小节
     *
     * @return
     */
    @Override
    public Result getRandomContent() {
        String today = DateKit.formatDate2Str(new Date(), DateKit.YYYY_MM_DD);
        List<Record> list = Db.find(Db.getSqlPara(SQL_KEY + "getTodayNoStudy", Kv.by("today", today)));
        int index = randomInt(list.size()-1);
        return success().setData(list.get(index));
    }

    /**
     * 获取 0 ~ max 的随机整数，闭区间 [0, max]
     * @param max 最大值（包含）
     * @return 随机int
     */
    public int randomInt(int max) {
        if (max < 0) {
            throw new IllegalArgumentException("max不能小于0");
        }
        // nextInt(n) 返回 [0, n)，所以要+1才能包含max
        return RANDOM.nextInt(max + 1);
    }

    /**
     * 斩杀不需要复习的知识
     *
     * @param studyBanDto
     * @return
     */
    @Override
    public Result saveStudyBan(StudyBanDto studyBanDto) {
        Record save = new Record();
        save.set("zo_website_study_ban_id",StrKit.uuid());
        save.set("created_time",new Date());
        save.set("relation_id",studyBanDto.getRelationId());
        save.set("relation_type_text",studyBanDto.getRelationTypeText());
        save.set("relation_type_value",studyBanDto.getRelationTypeValue());
        save.set("deleted","0");
        Db.save("zo_website_study_ban",save);
        return success();
    }
}
