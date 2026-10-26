package com.zode.web.study.service.impl;

import com.common.base.BaseService;
import com.common.util.Result;
import com.common.util.StrKit;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.zode.web.fsrs.calculator.FSRSCurveCalculator;
import com.zode.web.fsrs.dto.CardState;
import com.zode.web.fsrs.dto.Rating;
import com.zode.web.fsrs.dto.ReviewRecord;
import com.zode.web.fsrs.dto.Series;
import com.zode.web.fsrs.service.FsrsService;
import com.zode.web.study.dto.StudyDto;
import com.zode.web.notes.dto.StudyRecordDto;
import com.zode.web.study.service.StudyService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author zhoump
 * @date 2026/10/24
 * @purpose
 */
@Service
public class StudyServiceImpl extends BaseService implements StudyService {

    private final static String SQL_KEY = "web.study.sql.";

    @Resource
    FsrsService fsrsService;


    /**
     * 学习打卡
     *
     * @param studyRecordDto
     * @return
     */
    @Override
    public Result saveStudyRecord(StudyRecordDto studyRecordDto) {
        CardState cardState;
        int reviewCount;
        Rating rating = Rating.of(Integer.parseInt(studyRecordDto.getRatingValue()));
        if (studyRecordDto.isFirst()){
            // 首次学习
            cardState = fsrsService.firstLearn(rating);
            reviewCount = 1;
        }else{
            // 多次复习 寻找上次学习fsrs卡片数据
            Record lastRecord = Db.findFirst(Db.getSqlPara(SQL_KEY + "queryLastStudyRecord", Kv.by("relationId", studyRecordDto.getRelationId())));
            CardState card = CardState.Of(lastRecord);
            cardState = fsrsService.reviewCard(card,rating);
            reviewCount = Integer.parseInt(lastRecord.getStr("review_count")) + 1;
        }
        Record save = new Record();
        save.set("zo_website_study_record_id",StrKit.uuid());
        save.set("created_time",new Date());
        save.set("relation_id",studyRecordDto.getRelationId());
        save.set("relation_type_text",studyRecordDto.getRelationTypeText());
        save.set("relation_type_value",studyRecordDto.getRelationTypeValue());
        save.set("timing",studyRecordDto.getTiming());
        save.set("deleted","0");
        // fsrs卡片模型持久化
        save.set("rating_value",studyRecordDto.getRatingValue());
        save.set("rating_text",studyRecordDto.getRatingText());
        save.set("difficulty",String.format("%.4f", cardState.getDifficulty()));
        save.set("stability",String.format("%.4f",cardState.getStability()));
        save.set("next_review",String.valueOf(cardState.getNextReview()));
        save.set("learning_phase",String.valueOf(cardState.isLearningPhase()));
        save.set("review_count",String.valueOf(reviewCount));
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
        if (!recordList.isEmpty()){
            // 起点
            String firstDay = recordList.get(0).getStr("day");
            List<ReviewRecord> reviewRecordList = recordList.stream().map(r -> {
                        int days = countPastDay(r.getStr("day"), firstDay);
                        return new ReviewRecord(days, Integer.parseInt(r.getStr("rating_value")));
                    }).distinct()
                    .collect(Collectors.toList());
            Map<String, List<Series>> line = FSRSCurveCalculator.parseLine(reviewRecordList);
            result.set("line",line);
        }
        // 推荐下次复习时间：取该小节最新一条学习记录里的 next_review（FSRS 调度结果）
        Record lastRecord = Db.findFirst(Db.getSqlPara(SQL_KEY + "queryLastStudyRecord", Kv.by("relationId", relationId)));
        String nextReviewTime = "";
        if (lastRecord != null && lastRecord.get("next_review") != null) {
            // LocalDateTime 字符串形如 2026-10-21T10:30:00.123，规整为 yyyy-MM-dd HH:mm
            String raw = lastRecord.getStr("next_review").replace("T", " ");
            nextReviewTime = raw.length() > 16 ? raw.substring(0, 16) : raw;
        }
        result.set("nextReviewTime",nextReviewTime);
        long totalTiming = recordList.stream().map(r->Long.parseLong(r.getStr("timing"))).reduce(0L,Long::sum);
        result.set("totalTiming",secondToHms(totalTiming));
        // 累计学习次数：该小节的历史学习记录条数
        result.set("totalCount",recordList.size());
        result.set("isFirst",recordList.isEmpty());
        return success().setData(result);
    }

    /**
     * 计算天数：day1 >= day2，格式 yyyy-MM-dd
     * day1 == day2 返回 1
     * 例：day2=2026-10-20, day1=2026-10-20 → 1
     *     day2=2026-10-20, day1=2026-10-21 → 2
     */
    public int countPastDay(String day1, String day2) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate d1 = LocalDate.parse(day1, formatter);
        LocalDate d2 = LocalDate.parse(day2, formatter);
        // 校验，按你前提 day1 >= day2，这里加个保护
        if (d1.isBefore(d2)) {
            throw new IllegalArgumentException("day1 不能小于 day2");
        }
        // ChronoUnit.between 得到间隔天数（不含起始当天），+1 满足同天返回1
        long days = java.time.temporal.ChronoUnit.DAYS.between(d2, d1) + 1;
        return (int) days;
    }

    /**
     * 将总秒数转为 时分秒字符串 HH:mm:ss
     * @param totalSeconds 总秒数
     * @return HH:mm:ss
     */
    public String secondToHms(long totalSeconds) {
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
     * 取消关注
     *
     * @param studyDto
     * @return
     */
    @Override
    public Result saveStudyBan(StudyDto studyDto) {
        if ("1".equals(studyDto.getFollowType())){
            Record save = new Record();
            save.set("zo_website_study_ban_id", StrKit.uuid());
            save.set("created_time",new Date());
            save.set("relation_id", studyDto.getRelationId());
            save.set("relation_type_text", studyDto.getRelationTypeText());
            save.set("relation_type_value", studyDto.getRelationTypeValue());
            save.set("deleted","0");
            Db.save("zo_website_study_ban",save);
        }else{
            Db.deleteById("zo_website_study_ban","relation_id",studyDto.getRelationId());
        }
        return success();
    }

    /**
     * 重点关注
     *
     * @param studyDto
     * @return
     */
    @Override
    public Result saveStudyLike(StudyDto studyDto) {
        if ("1".equals(studyDto.getFollowType())){
            Record save = new Record();
            save.set("zo_website_study_like_id", StrKit.uuid());
            save.set("created_time",new Date());
            save.set("relation_id", studyDto.getRelationId());
            save.set("relation_type_text", studyDto.getRelationTypeText());
            save.set("relation_type_value", studyDto.getRelationTypeValue());
            save.set("deleted","0");
            Db.save("zo_website_study_like",save);
        }else{
            Db.deleteById("zo_website_study_like","relation_id",studyDto.getRelationId());
        }
        return success();
    }

    /**
     * 获取学习域数据
     *
     * @return
     */
    @Override
    public Result queryStudyDomain() {
        Record result = new Record();
        // 今日学习
        List<Record> todayList = Db.find(Db.getSqlPara(SQL_KEY + "queryTodayStudy"));
        result.set("todayStudyCount",todayList.size());
        // 今日需复习
        List<Record> needList = Db.find(Db.getSqlPara(SQL_KEY + "queryTodayNeedStudy"));
        result.set("todayNeedStudyCount",needList.size());
        // 所有学习过的记录
        List<Record> list = Db.find(Db.getSqlPara(SQL_KEY + "queryHaveStudy"));
        List<String> finishList = list.stream()
                .filter(r -> fsrsService.isMemoryFirm(CardState.Of(r)) && Integer.parseInt(r.getStr("day_diff")) > 30)
                .map(r->r.getStr("zo_website_study_record_id"))
                .collect(Collectors.toList());
        // 记忆构建
        long ing = list.size()-finishList.size();
        result.set("ing",ing);
        // 记忆牢固
        long finish = finishList.size();
        result.set("finish",finish);

        List<Record> aeList = Db.find(Db.getSqlPara(SQL_KEY + "queryAgainOrEasy"));
        // 难标记
        long hard = aeList.stream().filter(r -> "1".equals(r.getStr("rating_value"))).count();
        result.set("hard",hard);
        // 易标记
        long easy = aeList.stream().filter(r -> "4".equals(r.getStr("rating_value"))).count();
        result.set("easy",easy);
        // 重点关注
        List<Record> flowList = Db.find(Db.getSqlPara(SQL_KEY + "queryFlow"));
        result.set("flow",flowList.size());
        // 取消关注
        List<Record> cancelFlowList = Db.find(Db.getSqlPara(SQL_KEY + "queryCancelFlow"));
        result.set("cancelFlow",cancelFlowList.size());
        return success().setData(result);
    }

    /**
     * 获取列表
     *
     * @param type 列表类型
     * @return
     */
    @Override
    public Result queryStudyDomainNoticeList(String type) {
        List<String> relationId = new ArrayList<>();
        switch (type){
            case "0":
                // 今日学习
                List<Record> todayList = Db.find(Db.getSqlPara(SQL_KEY + "queryTodayStudy"));
                relationId = todayList.stream().map(r->r.getStr("relation_id")).collect(Collectors.toList());
                break;
            case "1":
                // 今日需复习
                List<Record> needList = Db.find(Db.getSqlPara(SQL_KEY + "queryTodayNeedStudy"));
                relationId = needList.stream().map(r->r.getStr("relation_id")).collect(Collectors.toList());
                break;
            case "2":
            case "3":
                List<Record> list = Db.find(Db.getSqlPara(SQL_KEY + "queryHaveStudy"));
                List<String> finishList  = list.stream()
                        .filter(r -> fsrsService.isMemoryFirm(CardState.Of(r)) && Integer.parseInt(r.getStr("day_diff")) > 30)
                        .map(r->r.getStr("relation_id"))
                        .collect(Collectors.toList());
                if ("3".equals(type)){
                    relationId = finishList;
                }else{
                    relationId = list.stream().filter(r->!finishList.contains(r.getStr("relation_id")))
                            .map(r->r.getStr("relation_id"))
                            .collect(Collectors.toList());
                }
                break;
            case "4":
            case "5":
                List<Record> aeList = Db.find(Db.getSqlPara(SQL_KEY + "queryAgainOrEasy"));
                if ("4".equals(type)) {
                    // 难标记
                    relationId = aeList.stream().filter(r -> "1".equals(r.getStr("rating_value")))
                            .map(r -> r.getStr("relation_id"))
                            .collect(Collectors.toList());
                }else {
                    // 易标记
                    relationId = aeList.stream().filter(r -> "4".equals(r.getStr("rating_value")))
                            .map(r -> r.getStr("relation_id"))
                            .collect(Collectors.toList());
                }
                break;
            case "6":
                List<Record> flowList = Db.find(Db.getSqlPara(SQL_KEY + "queryFlow"));
                relationId = flowList.stream()
                        .map(r -> r.getStr("relation_id"))
                        .collect(Collectors.toList());
                break;
            case "7":
                List<Record> cancelFlowList = Db.find(Db.getSqlPara(SQL_KEY + "queryCancelFlow"));
                relationId = cancelFlowList.stream()
                        .map(r -> r.getStr("relation_id"))
                        .collect(Collectors.toList());
                break;
        }
        if (relationId.isEmpty()){
            return success().setData(new ArrayList<>());
        }
        List<Record> list = Db.find(Db.getSqlPara(SQL_KEY + "queryNotesListByIds", Kv.by("ids", relationId)));
        return success().setData(list);
    }
}
