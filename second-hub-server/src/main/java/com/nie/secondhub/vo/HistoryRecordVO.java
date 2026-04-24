package com.nie.secondhub.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class HistoryRecordVO {
    private Long id;
    private Long goodsId;
    private String title;
    private Double price;
    private String[] images;
    private LocalDateTime viewTime;
}