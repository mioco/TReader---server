package com.treader.demo.dto;

import com.treader.demo.model.Tag;
import lombok.Data;

import java.util.List;

@Data
public class UrlTagDTO {
    private Integer id;
    private String url;
    private String tempItem;
    private List<Tag> tagList;
}
