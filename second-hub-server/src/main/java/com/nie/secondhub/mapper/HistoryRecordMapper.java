package com.nie.secondhub.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nie.secondhub.entity.HistoryRecord;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface HistoryRecordMapper extends BaseMapper<HistoryRecord> {
    List<HistoryRecord> selectByUserIdOrderByViewTimeDesc(@Param("userId") Long userId, @Param("limit") Integer limit);
    void deleteByUserIdAndGoodsId(@Param("userId") Long userId, @Param("goodsId") Long goodsId);
    void deleteByUserId(@Param("userId") Long userId);
}