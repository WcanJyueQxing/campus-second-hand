package com.nie.secondhub.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("history_record")
public class HistoryRecord {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private Long goodsId;
    private String title;
    private Double price;
    private String images;
    private LocalDateTime viewTime;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}