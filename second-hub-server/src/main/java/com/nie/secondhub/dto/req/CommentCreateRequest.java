package com.nie.secondhub.dto.req;

import lombok.Data;

@Data
public class CommentCreateRequest {

    private Long orderId;
    private Long goodsId;
    private Integer rating;
    private String content;

}