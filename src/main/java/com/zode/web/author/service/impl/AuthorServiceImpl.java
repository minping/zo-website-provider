package com.zode.web.author.service.impl;

import com.common.util.DateKit;
import com.common.util.Result;
import com.common.util.StrKit;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.zode.web.author.dto.AuthorInfoDto;
import com.zode.web.author.dto.AuthorProcessDto;
import com.zode.web.author.dto.AuthorProjectDto;
import com.zode.web.author.service.AuthorService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author zhoump
 * @date 2026/7/19
 * @purpose
 */
@Service
public class AuthorServiceImpl implements AuthorService {

    private final static String SQL_KEY = "website.web.auth.sql.";

    /**
     * 保存更新作者信息
     *
     * @param authorInfoDto
     * @return
     */
    @Override
    public Result saveInfo(AuthorInfoDto authorInfoDto) {
        boolean isAdd = false;
        if (StrKit.isBlank(authorInfoDto.getId())){
            isAdd = true;
        }
        String id = isAdd ? StrKit.uuid() : authorInfoDto.getId();
        Record record = new Record();
        record.set("zo_website_author_id",id);
        record.set("deleted",0);
        record.set("name",authorInfoDto.getName());
        record.set("post",authorInfoDto.getPost());
        record.set("email",authorInfoDto.getEmail());
        record.set("github",authorInfoDto.getGithub());
        record.set("self",authorInfoDto.getSelf());
        record.set("avatar",authorInfoDto.getAvatar());
        record.set("frontend",authorInfoDto.getFrontend());
        record.set("backend",authorInfoDto.getBackend());
        record.set("tools",authorInfoDto.getTools());
        record.set("devops",authorInfoDto.getDevops());
        record.set("work_time",authorInfoDto.getWorkTime());
        if (isAdd){
            Db.save("zo_website_author","zo_website_author_id",record);
        }else{
            Db.update("zo_website_author","zo_website_author_id",record);
        }
        List<AuthorProcessDto> processDtoList = authorInfoDto.getProcessDtoList();
        processDtoList.forEach(p -> {
            Record pr = new Record();
            pr.set("zo_website_author_id",id);
            pr.set("time", DateKit.formatStr2Date(p.getTime(),DateKit.YYYY_MM_DD));
            pr.set("event",p.getEvent());
            pr.set("deleted",p.getDeleted());
            if (StrKit.isBlank(p.getId())){
                pr.set("zo_website_author_process_id",StrKit.uuid());
                Db.save("zo_website_author_process","zo_website_author_process_id",pr);
            }else{
                pr.set("zo_website_author_process_id",p.getId());
                Db.update("zo_website_author_process","zo_website_author_process_id",pr);
            }
        });
        return Result.me().success();
    }

    /**
     * 查询作者信息
     *
     * @param authId
     * @return
     */
    @Override
    public Result queryInfo(String authId) {
        Record authInfo = Db.findFirst(Db.getSqlPara(SQL_KEY + "queryInfoById", Kv.by("id", authId)));
        if (authInfo == null){
            return Result.me().success().setData(new HashMap<>());
        }
        List<Record> processList = Db.find(Db.getSqlPara(SQL_KEY + "queryProcessById", Kv.by("id", authInfo.getStr("id"))));
        Map<String, Object> columns = authInfo.getColumns();
        List<Map<String, Object>> pList = processList.stream().map(Record::getColumns).collect(Collectors.toList());
        columns.put("processList",pList);
        return Result.me().success().setData(columns);
    }

    /**
     * 保存项目信息
     *
     * @param projectDto
     * @return
     */
    @Override
    public Result saveProjectInfo(AuthorProjectDto projectDto) {
        Record authInfo = Db.findFirst(Db.getSqlPara(SQL_KEY + "queryInfoById", Kv.by("id", "")));
        if (authInfo == null){
            return Result.me().error().setMessage("未找到作者");
        }
        projectDto.setAuthorId(authInfo.getStr("id"));
        boolean isAdd = false;
        if (StrKit.isBlank(projectDto.getId())){
            isAdd = true;
        }
        Record r = new Record();
        r.set("zo_website_author_project_id",isAdd ? StrKit.uuid() : projectDto.getId());
        r.set("zo_website_author_id",projectDto.getAuthorId());
        r.set("name",projectDto.getName());
        r.set("role",projectDto.getRole());
        r.set("description",projectDto.getDescription());
        r.set("tech",projectDto.getTech());
        r.set("image",projectDto.getImage());
        r.set("start_time",projectDto.getStartTime());
        r.set("end_time",projectDto.getEndTime());
        if (isAdd){
            Db.save("zo_website_author_project",r);
        }else{
            Db.update("zo_website_author_project","zo_website_author_project_id",r);
        }
        return Result.me().success();
    }

    /**
     * 查询项目列表
     *
     * @param authId
     * @return
     */
    @Override
    public Result queryProjectList(String authId) {
        List<Record> projectList = Db.find(Db.getSqlPara(SQL_KEY + "queryProjectList", Kv.by("authId", authId)));
        List<Map<String, Object>> dataList = projectList.stream().map(r -> r.getColumns()).collect(Collectors.toList());
        return Result.me().success().setData(dataList);
    }

    /**
     * 查询项目信息
     *
     * @param id
     * @return
     */
    @Override
    public Result queryProjectInfo(String id) {
        Record projectInfo = Db.findFirst(Db.getSqlPara(SQL_KEY + "queryProjectById", Kv.by("id", id)));
        return Result.me().setData(projectInfo.getColumns());
    }
}
