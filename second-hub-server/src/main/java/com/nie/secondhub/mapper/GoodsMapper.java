package com.nie.secondhub.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nie.secondhub.entity.Goods;
import org.apache.ibatis.annotations.Param;

public interface GoodsMapper extends BaseMapper<Goods> {
    void updateFavoriteCount(Long goodsId, int delta);
    void addFavoriteCount(@Param("goodsId") Long goodsId);
    void reduceFavoriteCount(@Param("goodsId") Long goodsId);
}
