package com.nie.secondhub.service;

import com.nie.secondhub.dto.req.CommentCreateRequest;
import com.nie.secondhub.vo.CommentVO;
import com.nie.secondhub.common.response.PageResponse;

public interface CommentService {

    /**
     * 提交评价
     */
    void createComment(Long userId, CommentCreateRequest req);

    /**
     * 获取商品评价列表
     */
    PageResponse<CommentVO> getGoodsComments(Long goodsId, Long pageNo, Long pageSize);

    /**
     * 获取我的评价列表
     */
    PageResponse<CommentVO> getMyComments(Long userId, Long pageNo, Long pageSize);

    /**
     * 删除评价
     */
    void deleteComment(Long userId, Long commentId);

}