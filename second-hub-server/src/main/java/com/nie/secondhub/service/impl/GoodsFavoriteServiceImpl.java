package com.nie.secondhub.service.impl;

import com.nie.secondhub.entity.Goods;
import com.nie.secondhub.entity.GoodsFavorite;
import com.nie.secondhub.mapper.GoodsFavoriteMapper;
import com.nie.secondhub.mapper.GoodsMapper;
import com.nie.secondhub.service.GoodsFavoriteService;
import com.nie.secondhub.util.Result;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.RedisTemplate;
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

    @Resource
    private RedisTemplate<Object, Object> redisTemplate;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private String getGoodsDetailCacheKey(Long goodsId) {
        return "goods:detail:" + goodsId;
    }

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
        } else {
            System.out.println("【收藏操作】已存在 → 切换状态");
            int newStatus = favorite.getIsDeleted() == 0 ? 1 : 0;
            String now = LocalDateTime.now().format(DATE_FORMATTER);

            int updateRows = goodsFavoriteMapper.updateFavoriteStatus(favorite.getId(), newStatus, now);
            System.out.println("【收藏操作】更新行数：" + updateRows);

            if (newStatus == 0) {
                goodsMapper.addFavoriteCount(goodsId);
            } else {
                goodsMapper.reduceFavoriteCount(goodsId);
            }
        }

        // 清除商品详情缓存，确保下次获取时能看到最新的收藏状态
        try {
            redisTemplate.delete(getGoodsDetailCacheKey(goodsId));
            System.out.println("【收藏操作】已清除商品详情缓存：" + goodsId);
        } catch (Exception e) {
            System.out.println("【收藏操作】清除缓存失败：" + e.getMessage());
        }

        return Result.success("操作成功");
    }

    @Override
    public Result getFavoriteGoodsList(Long userId) {
        System.out.println("【收藏列表】查询用户：" + userId);
        List<Goods> list = goodsFavoriteMapper.selectFavoriteGoods(userId);
        System.out.println("【收藏列表】查到数量：" + (list == null ? 0 : list.size()));
        return Result.success("获取成功", list);
    }
}