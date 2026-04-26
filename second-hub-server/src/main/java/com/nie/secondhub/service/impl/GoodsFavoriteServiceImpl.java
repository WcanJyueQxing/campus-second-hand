package com.nie.secondhub.service.impl;

import com.nie.secondhub.entity.Goods;
import com.nie.secondhub.entity.GoodsFavorite;
import com.nie.secondhub.mapper.GoodsFavoriteMapper;
import com.nie.secondhub.mapper.GoodsMapper;
import com.nie.secondhub.service.GoodsFavoriteService;
import com.nie.secondhub.util.Result;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class GoodsFavoriteServiceImpl implements GoodsFavoriteService {

    @Resource
    private GoodsFavoriteMapper goodsFavoriteMapper;

    @Resource
    private GoodsMapper goodsMapper;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result toggleFavorite(Long userId, Long goodsId) {
        System.out.println("【收藏操作】用户：" + userId + "，商品：" + goodsId);

        GoodsFavorite favorite = goodsFavoriteMapper.selectByUserAndGoods(userId, goodsId);
        System.out.println("【收藏操作】查询结果：" + favorite);

        if (favorite == null) {
            System.out.println("【收藏操作】从未收藏 → 新增收藏");
            GoodsFavorite newFav = new GoodsFavorite();
            newFav.setUserId(userId);
            newFav.setGoodsId(goodsId);
            newFav.setIsDeleted(0);
            newFav.setCreatedAt(LocalDateTime.now());
            newFav.setUpdatedAt(LocalDateTime.now());
            goodsFavoriteMapper.insert(newFav);

            goodsMapper.addFavoriteCount(goodsId);
            return Result.success("收藏成功");
        } else {
            System.out.println("【收藏操作】已存在 → 切换状态");
            int newStatus = favorite.getIsDeleted() == 0 ? 1 : 0;
            String now = LocalDateTime.now().format(DATE_FORMATTER);

            int updateRows = goodsFavoriteMapper.updateFavoriteStatus(favorite.getId(), newStatus, now);
            System.out.println("【收藏操作】更新行数：" + updateRows);

            if (newStatus == 0) {
                goodsMapper.addFavoriteCount(goodsId);
                return Result.success("收藏成功");
            } else {
                goodsMapper.reduceFavoriteCount(goodsId);
                return Result.success("取消收藏成功");
            }
        }
    }

    @Override
    public Result getFavoriteGoodsList(Long userId) {
        System.out.println("【收藏列表】查询用户：" + userId);
        List<Goods> list = goodsFavoriteMapper.selectFavoriteGoods(userId);
        System.out.println("【收藏列表】查到数量：" + (list == null ? 0 : list.size()));
        return Result.success("获取成功", list);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result deleteFavorite(Long userId, Long goodsId) {
        // 查询收藏记录
        GoodsFavorite favorite = goodsFavoriteMapper.selectByUserAndGoods(userId, goodsId);
        if (favorite == null || favorite.getIsDeleted() == 1) {
            return Result.error("该商品未收藏");
        }

        // 标记为删除
        favorite.setIsDeleted(1);
        favorite.setUpdatedAt(LocalDateTime.now());
        goodsFavoriteMapper.updateById(favorite);

        // 减少商品收藏数
        goodsMapper.reduceFavoriteCount(goodsId);
        return Result.success("取消收藏成功");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result batchDeleteFavorites(Long userId, List<Long> goodsIds) {
        int successCount = 0;
        for (Long goodsId : goodsIds) {
            GoodsFavorite favorite = goodsFavoriteMapper.selectByUserAndGoods(userId, goodsId);
            if (favorite != null && favorite.getIsDeleted() == 0) {
                favorite.setIsDeleted(1);
                favorite.setUpdatedAt(LocalDateTime.now());
                goodsFavoriteMapper.updateById(favorite);
                goodsMapper.reduceFavoriteCount(goodsId);
                successCount++;
            }
        }
        return Result.success("批量取消收藏成功，共处理" + successCount + "件商品");
    }
}