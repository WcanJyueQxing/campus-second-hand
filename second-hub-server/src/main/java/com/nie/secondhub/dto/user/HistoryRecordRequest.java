package com.nie.secondhub.dto.user;

import lombok.Data;

@Data
public class HistoryRecordRequest {
    private Long goodsId;
    private String title;
    private Double price;
    private String[] images;
}