package com.zode.web.index.service.impl;

import com.common.base.BaseService;
import com.common.util.Result;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.zode.web.index.service.IndexService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

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
}
