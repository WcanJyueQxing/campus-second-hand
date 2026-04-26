package com.nie.secondhub.service;

import com.nie.secondhub.util.Result;

import java.util.List;

public interface GoodsFavoriteService {
    Result toggleFavorite(Long userId, Long goodsId);
    Result getFavoriteGoodsList(Long userId);
}