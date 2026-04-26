package com.nie.secondhub.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nie.secondhub.dto.req.CommentCreateRequest;
import com.nie.secondhub.entity.GoodsComment;
import com.nie.secondhub.entity.TradeOrder;
import com.nie.secondhub.mapper.GoodsCommentMapper;
import com.nie.secondhub.mapper.TradeOrderMapper;
import com.nie.secondhub.service.CommentService;
import com.nie.secondhub.common.exception.BizException;
import com.nie.secondhub.vo.CommentVO;
import com.nie.secondhub.common.response.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final GoodsCommentMapper goodsCommentMapper;
    private final TradeOrderMapper tradeOrderMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createComment(Long userId, CommentCreateRequest req) {
        // 检查订单是否存在
        TradeOrder order = tradeOrderMapper.selectById(req.getOrderId());
        if (order == null) {
            throw new BizException("订单不存在");
        }

        // 检查是否是买家
        if (!order.getBuyerId().equals(userId)) {
            throw new BizException("只有买家可以评价");
        }

        // 检查订单状态
        if (!"COMPLETED".equals(order.getOrderStatus())) {
            throw new BizException("订单未完成，无法评价");
        }

        // 检查是否已经评价过
        GoodsComment existing = goodsCommentMapper.selectOne(new LambdaQueryWrapper<GoodsComment>()
                .eq(GoodsComment::getGoodsId, req.getGoodsId())
                .eq(GoodsComment::getUserId, userId));
        if (existing != null) {
            throw new BizException("已经评价过了");
        }

        // 创建评价
        GoodsComment comment = new GoodsComment();
        comment.setGoodsId(req.getGoodsId());
        comment.setUserId(userId);
        comment.setContent(req.getContent());
        comment.setCreatedAt(LocalDateTime.now());
        comment.setUpdatedAt(LocalDateTime.now());
        goodsCommentMapper.insert(comment);
    }

    @Override
    public PageResponse<CommentVO> getGoodsComments(Long goodsId, Long pageNo, Long pageSize) {
        Page<GoodsComment> page = new Page<>(pageNo, pageSize);
        IPage<GoodsComment> commentPage = goodsCommentMapper.selectPage(page, new LambdaQueryWrapper<GoodsComment>()
                .eq(GoodsComment::getGoodsId, goodsId)
                .orderByDesc(GoodsComment::getCreatedAt));

        return PageResponse.<CommentVO>builder()
                .total(commentPage.getTotal())
                .pageNo(commentPage.getCurrent())
                .pageSize(commentPage.getSize())
                .records(commentPage.getRecords().stream()
                        .map(this::convertToVO)
                        .toList())
                .build();
    }

    @Override
    public PageResponse<CommentVO> getMyComments(Long userId, Long pageNo, Long pageSize) {
        Page<GoodsComment> page = new Page<>(pageNo, pageSize);
        IPage<GoodsComment> commentPage = goodsCommentMapper.selectPage(page, new LambdaQueryWrapper<GoodsComment>()
                .eq(GoodsComment::getUserId, userId)
                .orderByDesc(GoodsComment::getCreatedAt));

        return PageResponse.<CommentVO>builder()
                .total(commentPage.getTotal())
                .pageNo(commentPage.getCurrent())
                .pageSize(commentPage.getSize())
                .records(commentPage.getRecords().stream()
                        .map(this::convertToVO)
                        .toList())
                .build();
    }

    @Override
    public void deleteComment(Long userId, Long commentId) {
        GoodsComment comment = goodsCommentMapper.selectById(commentId);
        if (comment == null) {
            throw new BizException("评价不存在");
        }

        if (!comment.getUserId().equals(userId)) {
            throw new BizException("只能删除自己的评价");
        }

        goodsCommentMapper.deleteById(commentId);
    }

    private CommentVO convertToVO(GoodsComment comment) {
        CommentVO vo = new CommentVO();
        vo.setId(comment.getId());
        vo.setGoodsId(comment.getGoodsId());
        vo.setUserId(comment.getUserId());
        vo.setContent(comment.getContent());
        vo.setCreatedAt(comment.getCreatedAt());
        return vo;
    }

}