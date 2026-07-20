package com.zode.web.index.service.impl;

import com.common.base.BaseService;
import com.common.util.Result;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.zode.web.index.service.IndexService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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
        data.put("info", authInfo.getColumns());
        List<Map<String, Object>> pList = processList.stream().map(Record::getColumns).collect(Collectors.toList());
        data.put("processList", pList);
        List<Record> projectList = Db.find(Db.getSqlPara("website.web.auth.sql.queryProjectList", Kv.by("authId", userId)));
        List<Map<String, Object>> dataList = projectList.stream().map(r -> r.getColumns()).collect(Collectors.toList());
        data.put("projectList", dataList);
        return Result.me().success().setData(data);
    }
}
