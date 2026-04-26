package com.nie.secondhub.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nie.secondhub.entity.GoodsFavorite;
import org.apache.ibatis.annotations.Param;

import com.nie.secondhub.entity.Goods;

import java.util.List;

public interface GoodsFavoriteMapper extends BaseMapper<GoodsFavorite> {
    GoodsFavorite selectByUserAndGoodsAll(@Param("userId") Long userId, @Param("goodsId") Long goodsId);
    GoodsFavorite selectByUserAndGoods(@Param("userId") Long userId, @Param("goodsId") Long goodsId);
    List<Goods> selectFavoriteGoods(@Param("userId") Long userId);

    int updateFavoriteStatus(@Param("id") Long id, @Param("isDeleted") Integer isDeleted, @Param("updatedAt") String updatedAt);
}