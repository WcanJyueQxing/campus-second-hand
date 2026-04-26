package com.nie.secondhub.service;

import com.nie.secondhub.util.Result;

import java.util.List;

public interface GoodsFavoriteService {
    Result toggleFavorite(Long userId, Long goodsId);
    Result getFavoriteGoodsList(Long userId);
    // 单条删除
    Result deleteFavorite(Long userId, Long goodsId);
    // 批量删除
    Result batchDeleteFavorites(Long userId, List<Long> goodsIds);
}