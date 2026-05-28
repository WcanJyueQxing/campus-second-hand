package com.nie.secondhub.service;

import com.nie.secondhub.util.Result;

import java.util.List;

/**
 * 商品收藏服务接口
 * 提供商品收藏相关功能
 */
public interface GoodsFavoriteService {

    /**
     * 切换商品收藏状态
     * 
     * @param userId 用户ID
     * @param goodsId 商品ID
     * @return 操作结果
     */
    Result toggleFavorite(Long userId, Long goodsId);

    /**
     * 获取用户收藏商品列表
     * 
     * @param userId 用户ID
     * @return 收藏商品列表
     */
    Result getFavoriteGoodsList(Long userId);
}