package com.zode.web.article.dto;

import com.common.util.StrKit;
import com.jfinal.plugin.activerecord.Record;
import lombok.Data;

/**
 * @author zhoump
 * @date 2026/6/20
 * @purpose
 */
@Data
public class ApiParamDto {
    private String id;
    private String para;
    private String desc;
    private String type;
    private String method;
    private String required;
    private String defaultValue;
    private String deleted;

    public Record parseRecord(String apiId){
        Record record = new Record();
        if (StrKit.isBlank(this.id)){
            record.set("zo_website_api_param_id",StrKit.uuid());
        }else{
            record.set("zo_website_api_param_id",this.id);
        }
        record.set("zo_website_api_id",apiId);
        record.set("para",this.para);
        record.set("desc",this.desc);
        record.set("type",this.type);
        record.set("method",this.method);
        record.set("required",this.required);
        record.set("default_value",this.defaultValue);
        record.set("deleted",this.deleted);
        return record;
    }
}
