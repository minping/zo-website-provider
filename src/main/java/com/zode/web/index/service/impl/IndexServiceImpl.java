package com.zode.web.index.service.impl;

import com.common.base.BaseService;
import com.common.util.Result;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.zode.web.index.service.IndexService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author zhoump
 * @date 2026/5/21
 * @purpose
 */
@Service
public class IndexServiceImpl extends BaseService implements IndexService {

    public static final String SQL_KEY = "website.web.index.";

    /**
     * 首页 统计
     *
     * @return
     */
    @Override
    public Result queryStatics() {
        Record statics = Db.findFirst(Db.getSqlPara(SQL_KEY + "queryArticleStatics"));
        LocalDate startDate = LocalDate.parse("2026-05-01");
        LocalDate endDate = LocalDate.now();
        long between = ChronoUnit.DAYS.between(startDate, endDate);
        statics.set("days",between);
        return success().setData(statics.getColumns());
    }

    /**
     * 关于的信息接口
     *
     * @param userId
     * @return
     */
    @Override
    public Result aboutMe(String userId) {
        Record authInfo = Db.findFirst(Db.getSqlPara("website.web.auth.sql.queryInfoById", Kv.by("id", userId)));
        if (authInfo == null) {
            return Result.me().success().setData(new HashMap<>());
        }
        List<Record> processList = Db.find(Db.getSqlPara("website.web.auth.sql.queryProcessById", Kv.by("id", authInfo.getStr("id"))));
        Map<String, Object> data = new HashMap<>();
        authInfo.set("work_time",calculateWorkExperience(authInfo.getStr("work_time"))+"年");
        data.put("info", authInfo.getColumns());
        List<Map<String, Object>> pList = processList.stream().map(Record::getColumns).collect(Collectors.toList());
        data.put("processList", pList);
        List<Record> projectList = Db.find(Db.getSqlPara("website.web.auth.sql.queryProjectList", Kv.by("authId", userId)));
        data.put("projectList", projectList.stream().map(r -> r.getColumns()).collect(Collectors.toList()));
        List<Record> productList = Db.find(Db.getSqlPara("website.web.auth.sql.queryProductList", Kv.by("authId", userId)));
        data.put("productList", productList.stream().map(r -> r.getColumns()).collect(Collectors.toList()));
        return Result.me().success().setData(data);
    }

    /**
     * 计算工作年限（精确到小数）
     * @param startYearMonth 入职年月，格式：yyyy-MM
     * @return 工作年限（例如：3.5表示3年6个月）
     */
    public double calculateWorkExperience(String startYearMonth) {
        DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM");
        YearMonth start = YearMonth.parse(startYearMonth, FORMATTER);
        YearMonth current = YearMonth.now();

        long months = ChronoUnit.MONTHS.between(start, current);
        return months < 0 ? 0.0 : Math.round(months / 12.0 * 10.0) / 10.0;
    }


}
