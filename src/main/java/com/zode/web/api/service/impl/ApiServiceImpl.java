package com.zode.web.api.service.impl;

import com.common.base.BaseService;
import com.common.util.DateKit;
import com.common.util.Result;
import com.common.util.StrKit;
import com.common.util.TextValue;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.zode.web.api.dto.ApiSaveDto;
import com.zode.web.api.service.ApiService;
import com.zode.web.article.dto.ApiParamDto;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author zhoump
 * @date 2026/5/31
 * @purpose
 */
@Service
public class ApiServiceImpl extends BaseService implements ApiService {

    private final String SQL_KEY = "website.web.api.sql.";

    /**
     * 保存
     *
     * @param apiSaveDto
     * @return
     */
    @Override
    public Result saveApi(ApiSaveDto apiSaveDto) {
        Record api = new Record();
        api.set("name",apiSaveDto.getName());
        api.set("description",apiSaveDto.getDescription());
        api.set("endpoint",apiSaveDto.getEndpoint());
        api.set("method",apiSaveDto.getMethod());
        api.set("tag_value",apiSaveDto.getTagValue());
        api.set("tag_text",apiSaveDto.getTagText());
        api.set("is_free",apiSaveDto.getIsFree());
        api.set("response_format",apiSaveDto.getResponseFormat());
        api.set("request_example",apiSaveDto.getRequestExample());
        api.set("response_example",apiSaveDto.getResponseExample());
        api.set("status",apiSaveDto.getStatus());
        api.set("deleted","0");
        api.set("response_time",apiSaveDto.getResponseTime());
        String apiId;
        if (StrKit.isBlank(apiSaveDto.getId())){
            // 新增
            apiId = StrKit.uuid();
            api.set("zo_website_api_id",apiId);
            api.set("created_time", DateKit.getNowTime());
            api.set("updated_time", DateKit.getNowTime());
            Db.save("zo_website_api",api);
        }else{
            // 更新
            apiId = apiSaveDto.getId();
            api.set("zo_website_api_id",apiId);
            api.set("updated_time", DateKit.getNowTime());
            Db.update("zo_website_api","zo_website_api_id",api);
        }

        List<ApiParamDto> paramList = apiSaveDto.getParamList();
        if (!paramList.isEmpty()){
            List<Record> updateList = paramList.stream().filter(p -> StrKit.isNotBlank(p.getId()))
                    .map(p -> p.parseRecord(apiId))
                    .collect(Collectors.toList());
            List<Record> saveList = paramList.stream().filter(p -> StrKit.isBlank(p.getId()))
                    .map(p -> p.parseRecord(apiId))
                    .collect(Collectors.toList());
            if (!updateList.isEmpty()){
                Db.batchUpdate("zo_website_api_param","zo_website_api_param_id",updateList,10);
            }
            if (!saveList.isEmpty()){
                Db.batchSave("zo_website_api_param",saveList,10);
            }
        }
        return success();
    }

    /**
     * 删除
     *
     * @param id
     * @return
     */
    @Override
    public Result deleteApi(String id) {
        Record api = new Record();
        api.set("zo_website_api_id",id);
        api.set("deleted","1");
        boolean update = Db.update("zo_website_api", "zo_website_api_id", api);
        return update ? success() : error().setMessage("删除失败");
    }

    /**
     * 查询api列表
     *
     * @return
     */
    @Override
    public Result queryApiList() {
        List<Record> list = Db.find(Db.getSqlPara(SQL_KEY + "queryApiList"));
        return success().setData(list.stream().map(Record::getColumns).collect(Collectors.toList()));
    }

    /**
     * 查询api数据
     *
     * @param id
     * @return
     */
    @Override
    public Result queryApiById(String id) {
        Record api = Db.findById("zo_website_api", "zo_website_api_id", id);
        List<Record> list = Db.find(Db.getSqlPara(SQL_KEY + "queryApiParamList", Kv.by("apiId",id)));
        api.set("paramList",list.stream().map(Record::getColumns).collect(Collectors.toList()));
        return success().setData(api.getColumns());
    }

    /**
     * 保存标签
     *
     * @param id
     * @param name
     * @param color
     * @return
     */
    @Override
    public Result saveApiTag(String id, String name, String color) {
        Record tag = new Record();
        tag.set("tag_name",name);
        tag.set("tag_color",color);
        tag.set("deleted","0");
        if (StrKit.isBlank(id)){
            // 新增
            tag.set("zo_website_api_tag_id",StrKit.uuid());
            tag.set("created_time",DateKit.getNowTime());
            tag.set("updated_time",DateKit.getNowTime());
            Db.save("zo_website_api_tag",tag);
        }else{
            // 更新
            tag.set("zo_website_api_tag_id",id);
            tag.set("updated_time",DateKit.getNowTime());
            Db.update("zo_website_api_tag","zo_website_api_tag_id",tag);
        }
        return success();
    }

    /**
     * 删除标签
     *
     * @param id
     * @return
     */
    @Override
    public Result deleteApiTag(String id) {
        Record api = new Record();
        api.set("zo_website_api_tag_id",id);
        api.set("deleted","1");
        boolean update = Db.update("zo_website_api_tag", "zo_website_api_tag_id", api);
        return update ? success() : error().setMessage("删除失败");
    }

    /**
     * 查询标签
     *
     * @return
     */
    @Override
    public Result queryApiTagList() {
        List<Record> list = Db.find(Db.getSqlPara(SQL_KEY + "queryApiTagList"));
        return success().setData(list.stream().map(Record::getColumns).collect(Collectors.toList()));
    }

    /**
     * 首页查询精选api
     *
     * @return
     */
    @Override
    public Result querySomeApiList() {
        List<Record> tagList = Db.find(Db.getSqlPara(SQL_KEY + "queryApiTagList"));
        Map<String, Record> tagMap = tagList.stream().collect(Collectors.toMap(tag -> tag.getStr("tag_id"), Function.identity()));
        List<Record> list = Db.find(Db.getSqlPara(SQL_KEY + "querySomeApiList"));
        list.forEach(api ->{
            api.set("tags", this.getTagList(api.getStr("tag_value"),tagMap));
        });
        return success().setData(list.stream().map(Record::getColumns).collect(Collectors.toList()));
    }

    private List<Map<String,Object>> getTagList(String tagValue,  Map<String, Record> tagMap) {
        if (StrKit.isBlank(tagValue)) return Collections.emptyList();
        List<Map<String,Object>> result = new LinkedList<>();
        String[] tagArr = tagValue.split("\\^");
        for (String id : tagArr) {
            Record tag = tagMap.get(id);
            result.add(tag.getColumns());
        }
        return result;
    }

    /**
     * 查询api
     *
     * @param searchValue
     * @param tag
     * @return
     */
    @Override
    public Result queryAllApiList(String searchValue, String tag) {
        Kv kv = Kv.create();
        if (StrKit.isNotBlank(searchValue)){
            kv.set("searchValue",searchValue);
        }
        if (StrKit.isNotBlank(tag)){
            kv.set("tag",tag);
        }
        List<Record> tagList = Db.find(Db.getSqlPara(SQL_KEY + "queryApiTagList"));
        Map<String, Record> tagMap = tagList.stream().collect(Collectors.toMap(t -> t.getStr("tag_id"), Function.identity()));
        List<Record> list = Db.find(Db.getSqlPara(SQL_KEY + "querySomeApiList",kv));
        list.forEach(api ->{
            api.set("tags", this.getTagList(api.getStr("tag_value"),tagMap));
        });
        return success().setData(list.stream().map(Record::getColumns).collect(Collectors.toList()));
    }
}
