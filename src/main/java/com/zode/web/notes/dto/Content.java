package com.zode.web.notes.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Content{
    private String id;
    private String title;
    private String complete;
}
