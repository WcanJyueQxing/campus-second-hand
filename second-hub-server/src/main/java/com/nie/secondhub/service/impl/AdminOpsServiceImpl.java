package com.nie.secondhub.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nie.secondhub.common.exception.BizException;
import com.nie.secondhub.common.response.PageResponse;
import com.nie.secondhub.dto.admin.CategorySaveRequest;
import com.nie.secondhub.dto.admin.NoticeSaveRequest;
import com.nie.secondhub.entity.Category;
import com.nie.secondhub.entity.Goods;
import com.nie.secondhub.entity.GoodsReport;
import com.nie.secondhub.entity.Notice;
import com.nie.secondhub.entity.TradeOrder;
import com.nie.secondhub.entity.User;
import com.nie.secondhub.mapper.CategoryMapper;
import com.nie.secondhub.mapper.GoodsMapper;
import com.nie.secondhub.mapper.GoodsReportMapper;
import com.nie.secondhub.mapper.NoticeMapper;
import com.nie.secondhub.mapper.TradeOrderMapper;
import com.nie.secondhub.mapper.UserMapper;
import com.nie.secondhub.service.AdminOpsService;
import com.nie.secondhub.vo.DashboardVO;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class AdminOpsServiceImpl implements AdminOpsService {

    private static final String DASHBOARD_CACHE_KEY = "dashboard:overview";

    @Resource
    private CategoryMapper categoryMapper;
    @Resource
    private UserMapper userMapper;
    @Resource
    private NoticeMapper noticeMapper;
    @Resource
    private GoodsMapper goodsMapper;
    @Resource
    private TradeOrderMapper tradeOrderMapper;
    @Resource
    private GoodsReportMapper goodsReportMapper;
    @Resource(name = "stringRedisTemplate")
    private StringRedisTemplate redisTemplate;
    @Resource
    private ObjectMapper objectMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long saveCategory(Long id, CategorySaveRequest request) {
        LocalDateTime now = LocalDateTime.now();
        Category category;
        if (id == null) {
            category = new Category();
            category.setCreatedAt(now);
        } else {
            category = categoryMapper.selectById(id);
            if (category == null) {
                throw new BizException("分类不存在");
            }
        }
        category.setName(request.getName());
        category.setSort(request.getSort());
        category.setStatus(request.getStatus());
        category.setUpdatedAt(now);
        if (id == null) {
            categoryMapper.insert(category);
        } else {
            categoryMapper.updateById(category);
        }
        return category.getId();
    }

    @Override
    public void deleteCategory(Long id) {
        categoryMapper.deleteById(id);
    }

    @Override
    public List<?> listCategory() {
        return categoryMapper.selectList(new LambdaQueryWrapper<Category>()
                .orderByDesc(Category::getSort).orderByAsc(Category::getId));
    }

    @Override
    public PageResponse<?> userPage(Long pageNo, Long pageSize, String keyword) {
        Page<User> page = new Page<>(pageNo, pageSize);
        Page<User> userPage = userMapper.selectPage(page, new LambdaQueryWrapper<User>()
                .like(keyword != null && !keyword.isBlank(), User::getNickname, keyword)
                .orderByDesc(User::getCreatedAt));
        return PageResponse.<User>builder()
                .total(userPage.getTotal())
                .pageNo(userPage.getCurrent())
                .pageSize(userPage.getSize())
                .records(userPage.getRecords())
                .build();
    }

    @Override
    public void updateUserStatus(Long userId, Integer status) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BizException("用户不存在");
        }
        user.setStatus(status);
        user.setUpdatedAt(LocalDateTime.now());
        userMapper.updateById(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long saveNotice(Long adminId, Long id, NoticeSaveRequest request) {
        Notice notice;
        LocalDateTime now = LocalDateTime.now();
        if (id == null) {
            notice = new Notice();
            notice.setCreatedAt(now);
        } else {
            notice = noticeMapper.selectById(id);
            if (notice == null) {
                throw new BizException("公告不存在");
            }
        }
        notice.setTitle(request.getTitle());
        notice.setContent(request.getContent());
        notice.setCoverUrl(request.getCoverUrl());
        notice.setStatus(request.getStatus());
        notice.setPublishAdminId(adminId);
        notice.setPublishedAt(now);
        notice.setUpdatedAt(now);
        if (id == null) {
            noticeMapper.insert(notice);
        } else {
            noticeMapper.updateById(notice);
        }
        return notice.getId();
    }

    @Override
    public void deleteNotice(Long id) {
        noticeMapper.deleteById(id);
    }

    @Override
    public PageResponse<?> noticePage(Long pageNo, Long pageSize) {
        Page<Notice> page = new Page<>(pageNo, pageSize);
        Page<Notice> noticePage = noticeMapper.selectPage(page, new LambdaQueryWrapper<Notice>()
                .orderByDesc(Notice::getPublishedAt));
        return PageResponse.<Notice>builder()
                .total(noticePage.getTotal())
                .pageNo(noticePage.getCurrent())
                .pageSize(noticePage.getSize())
                .records(noticePage.getRecords())
                .build();
    }

    @Override
    public DashboardVO dashboardOverview() {
        try {
            String cached = redisTemplate.opsForValue().get(DASHBOARD_CACHE_KEY);
            if (cached != null && !cached.isBlank()) {
                try {
                    return objectMapper.readValue(cached, DashboardVO.class);
                } catch (JsonProcessingException ignored) {
                }
            }
        } catch (Exception ignored) {
            // Redis 不可用时跳过缓存，直接查询数据库
        }

        List<Map<String, Object>> categoryDistribution = new ArrayList<>();
        List<Category> categories = categoryMapper.selectList(null);
        for (Category cat : categories) {
            Long count = goodsMapper.selectCount(new LambdaQueryWrapper<Goods>().eq(Goods::getCategoryId, cat.getId()));
            Map<String, Object> item = new HashMap<>();
            item.put("categoryName", cat.getName());
            item.put("goodsCount", count);
            categoryDistribution.add(item);
        }

        List<Map<String, Object>> orderStatusDistribution = new ArrayList<>();
        String[] orderStatuses = {"PENDING_PAYMENT", "PAID", "SHIPPED", "COMPLETED", "CANCELLED"};
        for (String status : orderStatuses) {
            Long count = tradeOrderMapper.selectCount(new LambdaQueryWrapper<TradeOrder>().eq(TradeOrder::getOrderStatus, status));
            Map<String, Object> item = new HashMap<>();
            item.put("status", status);
            item.put("count", count);
            orderStatusDistribution.add(item);
        }

        List<Map<String, Object>> userStatusDistribution = new ArrayList<>();
        Long normalCount = userMapper.selectCount(new LambdaQueryWrapper<User>().eq(User::getStatus, 1));
        Long disabledCount = userMapper.selectCount(new LambdaQueryWrapper<User>().eq(User::getStatus, 0));
        Map<String, Object> normalItem = new HashMap<>();
        normalItem.put("status", 1);
        normalItem.put("count", normalCount);
        userStatusDistribution.add(normalItem);
        Map<String, Object> disabledItem = new HashMap<>();
        disabledItem.put("status", 0);
        disabledItem.put("count", disabledCount);
        userStatusDistribution.add(disabledItem);

        DashboardVO dashboardVO = DashboardVO.builder()
                .userCount(userMapper.selectCount(null))
                .goodsCount(goodsMapper.selectCount(null))
                .pendingGoodsCount(goodsMapper.selectCount(new LambdaQueryWrapper<Goods>().eq(Goods::getStatus, "PENDING")))
                .orderCount(tradeOrderMapper.selectCount(null))
                .reportCount(goodsReportMapper.selectCount(new LambdaQueryWrapper<GoodsReport>().eq(GoodsReport::getStatus, "PENDING")))
                .categoryDistribution(categoryDistribution)
                .orderStatusDistribution(orderStatusDistribution)
                .userStatusDistribution(userStatusDistribution)
                .build();
        try {
            redisTemplate.opsForValue().set(DASHBOARD_CACHE_KEY, objectMapper.writeValueAsString(dashboardVO), 30, TimeUnit.SECONDS);
        } catch (Exception ignored) {
            // Redis 不可用时忽略缓存错误
        }
        return dashboardVO;
    }

    @Override
    public List<Map<String, Object>> userGoodsOrderTrend(String timeRange) {
        LocalDate today = LocalDate.now();
        LocalDateTime startDate;
        
        if ("all".equals(timeRange)) {
            // 全部数据：从系统开始时间（使用一个很早的日期）
            startDate = LocalDateTime.of(2020, 1, 1, 0, 0, 0);
        } else {
            // 默认近7天
            startDate = today.minusDays(6).atStartOfDay();
        }
        
        List<User> users = userMapper.selectList(new LambdaQueryWrapper<User>().ge(User::getCreatedAt, startDate));
        List<Goods> goodsList = goodsMapper.selectList(new LambdaQueryWrapper<Goods>().ge(Goods::getCreatedAt, startDate));
        List<TradeOrder> orderList = tradeOrderMapper.selectList(new LambdaQueryWrapper<TradeOrder>().ge(TradeOrder::getCreatedAt, startDate));

        Map<LocalDate, Long> userMap = users.stream().collect(Collectors.groupingBy(u -> u.getCreatedAt().toLocalDate(), Collectors.counting()));
        Map<LocalDate, Long> goodsMap = goodsList.stream().collect(Collectors.groupingBy(g -> g.getCreatedAt().toLocalDate(), Collectors.counting()));
        Map<LocalDate, Long> orderMap = orderList.stream().collect(Collectors.groupingBy(o -> o.getCreatedAt().toLocalDate(), Collectors.counting()));

        List<Map<String, Object>> result = new ArrayList<>();
        
        if ("all".equals(timeRange)) {
            // 全部数据：按实际日期排序
            List<LocalDate> dates = new ArrayList<>(userMap.keySet());
            dates.addAll(goodsMap.keySet());
            dates.addAll(orderMap.keySet());
            dates = dates.stream().distinct().sorted().collect(Collectors.toList());
            
            for (LocalDate date : dates) {
                Map<String, Object> row = new HashMap<>();
                row.put("date", date.toString());
                row.put("userCount", userMap.getOrDefault(date, 0L));
                row.put("goodsCount", goodsMap.getOrDefault(date, 0L));
                row.put("orderCount", orderMap.getOrDefault(date, 0L));
                result.add(row);
            }
        } else {
            // 近7天：固定7天范围
            for (int i = 6; i >= 0; i--) {
                LocalDate date = today.minusDays(i);
                Map<String, Object> row = new HashMap<>();
                row.put("date", date.toString());
                row.put("userCount", userMap.getOrDefault(date, 0L));
                row.put("goodsCount", goodsMap.getOrDefault(date, 0L));
                row.put("orderCount", orderMap.getOrDefault(date, 0L));
                result.add(row);
            }
        }
        
        return result;
    }
}
