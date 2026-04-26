package com.nie.secondhub.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentVO {

    private Long id;
    private Long orderId;
    private Long goodsId;
    private Long userId;
    private Integer rating;
    private String content;
    private LocalDateTime createdAt;
    private String nickname;
    private String avatarUrl;

    public CommentVO() {
    }

}