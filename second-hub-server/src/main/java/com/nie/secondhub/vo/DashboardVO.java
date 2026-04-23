package com.nie.secondhub.vo;

import lombok.Builder;
import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
@Builder
public class DashboardVO {
    private Long userCount;
    private Long goodsCount;
    private Long pendingGoodsCount;
    private Long orderCount;
    private Long reportCount;
    private List<Map<String, Object>> categoryDistribution;
    private List<Map<String, Object>> orderStatusDistribution;
    private List<Map<String, Object>> userStatusDistribution;
}
