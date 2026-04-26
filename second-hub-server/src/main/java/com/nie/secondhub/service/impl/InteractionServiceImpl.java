package com.nie.secondhub.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nie.secondhub.common.exception.BizException;
import com.nie.secondhub.common.response.ApiResponse;
import com.nie.secondhub.common.response.PageResponse;
import com.nie.secondhub.dto.admin.ReportHandleRequest;
import com.nie.secondhub.dto.user.CommentCreateRequest;
import com.nie.secondhub.dto.user.ReportCreateRequest;
import com.nie.secondhub.entity.Goods;
import com.nie.secondhub.entity.GoodsComment;
import com.nie.secondhub.entity.GoodsFavorite;
import com.nie.secondhub.entity.GoodsReport;
import com.nie.secondhub.entity.User;
import com.nie.secondhub.mapper.GoodsCommentMapper;
import com.nie.secondhub.mapper.GoodsFavoriteMapper;
import com.nie.secondhub.mapper.GoodsMapper;
import com.nie.secondhub.mapper.GoodsReportMapper;
import com.nie.secondhub.mapper.UserMapper;
import com.nie.secondhub.service.InteractionService;
import com.nie.secondhub.vo.CommentVO;
import com.nie.secondhub.vo.GoodsVO;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.concurrent.TimeUnit;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class InteractionServiceImpl implements InteractionService {

    @Resource
    private GoodsFavoriteMapper goodsFavoriteMapper;
    @Resource
    private GoodsMapper goodsMapper;
    @Resource
    private GoodsCommentMapper goodsCommentMapper;
    @Resource
    private UserMapper userMapper;
    @Resource
    private GoodsReportMapper goodsReportMapper;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    private String getFavoriteCacheKey(Long userId, Long goodsId) {
        return "favorite:" + userId + ":" + goodsId;
    }

    private String getFavoritesListCacheKey(Long userId) {
        return "favorites:list:" + userId;
    }

    private String getFavoriteLockKey(Long userId, Long goodsId) {
        return "lock:favorite:" + userId + ":" + goodsId;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void favorite(Long userId, Long goodsId) {
        // 分布式锁防止并发操作
        String lockKey = getFavoriteLockKey(userId, goodsId);
        Boolean acquired = redisTemplate.opsForValue().setIfAbsent(lockKey, "locked", 3, TimeUnit.SECONDS);
        if (Boolean.FALSE.equals(acquired)) {
            throw new BizException("操作过于频繁，请稍后再试");
        }

        try {
            // 检查缓存
            String cacheKey = getFavoriteCacheKey(userId, goodsId);
            if (Boolean.TRUE.equals(redisTemplate.hasKey(cacheKey))) {
                return; // 已经收藏，直接返回
            }

            Goods goods = goodsMapper.selectById(goodsId);
            if (goods == null) {
                throw new BizException("商品不存在");
            }
            // 查询所有状态的收藏记录
            GoodsFavorite favorite = goodsFavoriteMapper.selectByUserAndGoodsAll(userId, goodsId);

            if (favorite == null) {
                // 从未收藏 → 新增
                GoodsFavorite newFavorite = new GoodsFavorite();
                newFavorite.setUserId(userId);
                newFavorite.setGoodsId(goodsId);
                newFavorite.setIsDeleted(0);
                newFavorite.setCreatedAt(LocalDateTime.now());
                newFavorite.setUpdatedAt(LocalDateTime.now());
                goodsFavoriteMapper.insert(newFavorite);
                goodsMapper.updateFavoriteCount(goodsId, 1);
            } else {
                // 已存在但被删除 → 恢复
                if (favorite.getIsDeleted() == 1) {
                    favorite.setIsDeleted(0);
                    favorite.setUpdatedAt(LocalDateTime.now());
                    goodsFavoriteMapper.updateById(favorite);
                    goodsMapper.updateFavoriteCount(goodsId, 1);
                }
            }

            // 更新缓存
            redisTemplate.opsForValue().set(cacheKey, true, 1, TimeUnit.HOURS);
            // 清除收藏列表缓存
            redisTemplate.delete(getFavoritesListCacheKey(userId));
        } finally {
            // 释放锁
            redisTemplate.delete(lockKey);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void unfavorite(Long userId, Long goodsId) {
        // 分布式锁防止并发操作
        String lockKey = getFavoriteLockKey(userId, goodsId);
        Boolean acquired = redisTemplate.opsForValue().setIfAbsent(lockKey, "locked", 3, TimeUnit.SECONDS);
        if (Boolean.FALSE.equals(acquired)) {
            throw new BizException("操作过于频繁，请稍后再试");
        }

        try {
            // 检查缓存
            String cacheKey = getFavoriteCacheKey(userId, goodsId);
            if (!Boolean.TRUE.equals(redisTemplate.hasKey(cacheKey))) {
                return; // 未收藏，直接返回
            }

            // 查询未删除的收藏记录
            GoodsFavorite favorite = goodsFavoriteMapper.selectByUserAndGoods(userId, goodsId);
            if (favorite == null) {
                // 清除缓存
                redisTemplate.delete(cacheKey);
                return;
            }
            // 标记为已删除
            favorite.setIsDeleted(1);
            favorite.setUpdatedAt(LocalDateTime.now());
            goodsFavoriteMapper.updateById(favorite);
            goodsMapper.updateFavoriteCount(goodsId, -1);

            // 更新缓存
            redisTemplate.delete(cacheKey);
            // 清除收藏列表缓存
            redisTemplate.delete(getFavoritesListCacheKey(userId));
        } finally {
            // 释放锁
            redisTemplate.delete(lockKey);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResponse<Void> toggleFavorite(Long userId, Long goodsId) {
        // 分布式锁防止并发操作
        String lockKey = getFavoriteLockKey(userId, goodsId);
        Boolean acquired = null;
        try {
            acquired = redisTemplate.opsForValue().setIfAbsent(lockKey, "locked", 3, TimeUnit.SECONDS);
        } catch (Exception e) {
            // Redis连接失败，不使用锁
        }
        if (Boolean.FALSE.equals(acquired)) {
            throw new BizException("操作过于频繁，请稍后再试");
        }

        try {
            Goods goods = goodsMapper.selectById(goodsId);
            if (goods == null) {
                throw new BizException("商品不存在");
            }
            // 1. 查询用户是否收藏过该商品（不管是否删除）
            GoodsFavorite favorite = goodsFavoriteMapper.selectByUserAndGoodsAll(userId, goodsId);

            if (favorite == null) {
                // ======================================
                // 情况1：从未收藏 → 新增收藏
                // ======================================
                GoodsFavorite newFavorite = new GoodsFavorite();
                newFavorite.setUserId(userId);
                newFavorite.setGoodsId(goodsId);
                newFavorite.setIsDeleted(0); // 正常
                newFavorite.setCreatedAt(LocalDateTime.now());
                newFavorite.setUpdatedAt(LocalDateTime.now());
                goodsFavoriteMapper.insert(newFavorite);

                // 收藏数 +1
                goodsMapper.addFavoriteCount(goodsId);
                
                // 更新缓存
                try {
                    redisTemplate.opsForValue().set(getFavoriteCacheKey(userId, goodsId), true, 1, TimeUnit.HOURS);
                    // 清除收藏列表缓存
                    redisTemplate.delete(getFavoritesListCacheKey(userId));
                    // 清除商品详情缓存
                    redisTemplate.delete("goods:detail:" + goodsId);
                } catch (Exception e) {
                    // Redis操作失败，不影响主流程
                }
                
                return ApiResponse.success("收藏成功", null);

            } else {
                // ======================================
                // 情况2：已存在 → 切换状态
                // ======================================
                int nowDeleted = favorite.getIsDeleted();
                int newDeleted = nowDeleted == 0 ? 1 : 0;

                favorite.setIsDeleted(newDeleted);
                favorite.setUpdatedAt(LocalDateTime.now());
                goodsFavoriteMapper.updateById(favorite);

                // 收藏数 正确 +1 / -1
                if (newDeleted == 0) {
                    goodsMapper.addFavoriteCount(goodsId);
                    // 更新缓存
                    try {
                        redisTemplate.opsForValue().set(getFavoriteCacheKey(userId, goodsId), true, 1, TimeUnit.HOURS);
                    } catch (Exception e) {
                        // Redis操作失败，不影响主流程
                    }
                } else {
                    goodsMapper.reduceFavoriteCount(goodsId);
                    // 更新缓存
                    try {
                        redisTemplate.delete(getFavoriteCacheKey(userId, goodsId));
                    } catch (Exception e) {
                        // Redis操作失败，不影响主流程
                    }
                }
                
                // 清除收藏列表缓存
                try {
                    redisTemplate.delete(getFavoritesListCacheKey(userId));
                    // 清除商品详情缓存
                    redisTemplate.delete("goods:detail:" + goodsId);
                } catch (Exception e) {
                    // Redis操作失败，不影响主流程
                }

                return ApiResponse.success(newDeleted == 0 ? "收藏成功" : "取消收藏成功", null);
            }
        } finally {
            // 释放锁
            try {
                redisTemplate.delete(lockKey);
            } catch (Exception e) {
                // Redis操作失败，不影响主流程
            }
        }
    }

    @Override
    public PageResponse<GoodsVO> myFavorites(Long userId, Long pageNo, Long pageSize) {
        // 检查缓存
        String cacheKey = getFavoritesListCacheKey(userId) + ":" + pageNo + ":" + pageSize;
        try {
            PageResponse<GoodsVO> cachedResult = (PageResponse<GoodsVO>) redisTemplate.opsForValue().get(cacheKey);
            if (cachedResult != null) {
                return cachedResult;
            }
        } catch (Exception e) {
            // 缓存读取失败，继续执行数据库查询
        }

        Page<GoodsFavorite> page = new Page<>(pageNo, pageSize);
        Page<GoodsFavorite> favoritePage = goodsFavoriteMapper.selectPage(page, new LambdaQueryWrapper<GoodsFavorite>()
                .eq(GoodsFavorite::getUserId, userId)
                .eq(GoodsFavorite::getIsDeleted, 0)
                .orderByDesc(GoodsFavorite::getCreatedAt));

        List<Long> goodsIds = favoritePage.getRecords().stream().map(GoodsFavorite::getGoodsId).toList();
        Map<Long, Goods> goodsMap = goodsIds.isEmpty() ? new HashMap<>() : goodsMapper.selectBatchIds(goodsIds).stream()
                .collect(Collectors.toMap(Goods::getId, g -> g, (a, b) -> a));

        List<GoodsVO> records = goodsIds.stream().map(goodsMap::get).filter(g -> g != null).map(g -> {
            GoodsVO vo = new GoodsVO();
            vo.setId(g.getId());
            vo.setUserId(g.getUserId());
            vo.setCategoryId(g.getCategoryId());
            vo.setTitle(g.getTitle());
            vo.setDescription(g.getDescription());
            vo.setPrice(g.getPrice());
            vo.setCoverImage(g.getCoverImage());
            vo.setStatus(g.getStatus());
            vo.setFavoriteCount(g.getFavoriteCount());
            vo.setCommentCount(g.getCommentCount());
            vo.setViewCount(g.getViewCount());
            vo.setCreatedAt(g.getCreatedAt());
            return vo;
        }).toList();

        PageResponse<GoodsVO> result = PageResponse.<GoodsVO>builder()
                .total(favoritePage.getTotal())
                .pageNo(favoritePage.getCurrent())
                .pageSize(favoritePage.getSize())
                .records(records)
                .build();

        // 更新缓存
        try {
            redisTemplate.opsForValue().set(cacheKey, result, 30, TimeUnit.MINUTES);
        } catch (Exception e) {
            // 缓存写入失败，不影响正常返回
        }

        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addComment(Long userId, CommentCreateRequest request) {
        Goods goods = goodsMapper.selectById(request.getGoodsId());
        if (goods == null) {
            throw new BizException("商品不存在");
        }
        GoodsComment comment = new GoodsComment();
        comment.setGoodsId(request.getGoodsId());
        comment.setUserId(userId);
        comment.setContent(request.getContent());
        comment.setCreatedAt(LocalDateTime.now());
        comment.setUpdatedAt(LocalDateTime.now());
        goodsCommentMapper.insert(comment);

        goods.setCommentCount(goods.getCommentCount() + 1);
        goods.setUpdatedAt(LocalDateTime.now());
        goodsMapper.updateById(goods);
    }

    @Override
    public PageResponse<CommentVO> commentPage(Long goodsId, Long pageNo, Long pageSize) {
        Page<GoodsComment> page = new Page<>(pageNo, pageSize);
        Page<GoodsComment> commentPage = goodsCommentMapper.selectPage(page,
                new LambdaQueryWrapper<GoodsComment>()
                        .eq(GoodsComment::getGoodsId, goodsId)
                        .orderByDesc(GoodsComment::getCreatedAt));
        List<Long> userIds = commentPage.getRecords().stream().map(GoodsComment::getUserId).distinct().toList();
        Map<Long, User> userMap = userIds.isEmpty() ? new HashMap<>() : userMapper.selectBatchIds(userIds)
                .stream().collect(Collectors.toMap(User::getId, u -> u, (a, b) -> a));

        List<CommentVO> records = commentPage.getRecords().stream().map(comment -> {
            CommentVO vo = new CommentVO();
            vo.setId(comment.getId());
            vo.setGoodsId(comment.getGoodsId());
            vo.setUserId(comment.getUserId());
            User user = userMap.get(comment.getUserId());
            if (user != null) {
                vo.setNickname(user.getNickname());
                vo.setAvatarUrl(user.getAvatarUrl());
            }
            vo.setContent(comment.getContent());
            vo.setCreatedAt(comment.getCreatedAt());
            return vo;
        }).toList();

        return PageResponse.<CommentVO>builder()
                .total(commentPage.getTotal())
                .pageNo(commentPage.getCurrent())
                .pageSize(commentPage.getSize())
                .records(records)
                .build();
    }

    @Override
    public void report(Long userId, ReportCreateRequest request) {
        Goods goods = goodsMapper.selectById(request.getGoodsId());
        if (goods == null) {
            throw new BizException("商品不存在");
        }
        GoodsReport report = new GoodsReport();
        report.setGoodsId(request.getGoodsId());
        report.setReporterId(userId);
        report.setReason(request.getReason());
        report.setContent(request.getContent());
        report.setStatus("PENDING");
        report.setCreatedAt(LocalDateTime.now());
        report.setUpdatedAt(LocalDateTime.now());
        goodsReportMapper.insert(report);
    }

    @Override
    public PageResponse<?> reportPage(Long pageNo, Long pageSize, String status) {
        Page<GoodsReport> page = new Page<>(pageNo, pageSize);
        Page<GoodsReport> reportPage = goodsReportMapper.selectPage(page, new LambdaQueryWrapper<GoodsReport>()
                .eq(status != null && !status.isBlank(), GoodsReport::getStatus, status)
                .orderByDesc(GoodsReport::getCreatedAt));

        return PageResponse.<GoodsReport>builder()
                .total(reportPage.getTotal())
                .pageNo(reportPage.getCurrent())
                .pageSize(reportPage.getSize())
                .records(reportPage.getRecords())
                .build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void handleReport(Long adminId, Long reportId, ReportHandleRequest request) {
        GoodsReport report = goodsReportMapper.selectById(reportId);
        if (report == null) {
            throw new BizException("举报不存在");
        }
        report.setStatus("PROCESSED");
        report.setHandlerId(adminId);
        report.setHandleResult(request.getHandleResult());
        report.setHandledAt(LocalDateTime.now());
        report.setUpdatedAt(LocalDateTime.now());
        goodsReportMapper.updateById(report);
    }
}
