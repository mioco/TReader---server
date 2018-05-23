package com.treader.demo.dto;

import lombok.Data;

import java.util.List;

@Data
public class SubscriptionDTO {
    private String url;

    private String tempItem1;
    private String tempItem2;
    private List<String> keywords;
}
