# 基于你的数据库表结构，实现「商品收藏功能」（Spring Boot \+ 微信小程序）

我会完全基于你现有的 `goods\_favorite` 表，给你一套**可直接落地**的前后端实现方案。

---

## 一、核心表结构确认

你的数据库中，`goods\_favorite` 表就是为收藏功能设计的：

```sql
CREATE TABLE `goods_favorite` (
  `id` bigint PRIMARY KEY AUTO_INCREMENT,
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `goods_id` bigint NOT NULL COMMENT '商品ID',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '收藏时间',
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_deleted` tinyint DEFAULT 0 COMMENT '逻辑删除标记'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```

**核心设计：**`\(user\_id, goods\_id\)` 唯一约束，保证同一用户不会重复收藏同一商品。

---

## 二、Spring Boot 后端实现

### 1\. 实体类 `GoodsFavorite\.java`

```java
@Data
@TableName("goods_favorite")
public class GoodsFavorite {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private Long goodsId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer isDeleted;
}
```

### 2\. Mapper 接口与 SQL

`GoodsFavoriteMapper\.java`

```java
@Mapper
public interface GoodsFavoriteMapper extends BaseMapper<GoodsFavorite> {
    // 查询用户是否已收藏该商品
    GoodsFavorite selectByUserAndGoods(@Param("userId") Long userId, @Param("goodsId") Long goodsId);
    // 查询用户的收藏列表
    List<GoodsFavorite> selectListByUserId(@Param("userId") Long userId);
}
```

`GoodsFavoriteMapper\.xml`

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.your.mapper.GoodsFavoriteMapper">
    <select id="selectByUserAndGoods" resultType="com.your.entity.GoodsFavorite">
        SELECT * FROM goods_favorite
        WHERE user_id = #{userId} AND goods_id = #{goodsId} AND is_deleted = 0
    </select>
    <select id="selectListByUserId" resultType="com.your.entity.GoodsFavorite">
        SELECT * FROM goods_favorite
        WHERE user_id = #{userId} AND is_deleted = 0
        ORDER BY created_at DESC
    </select>
</mapper>
```

### 3\. Service 层核心逻辑

`GoodsFavoriteService\.java`

```java
public interface GoodsFavoriteService {
    // 收藏/取消收藏（核心方法）
    Result toggleFavorite(Long userId, Long goodsId);
    // 判断是否已收藏
    boolean isFavorite(Long userId, Long goodsId);
    // 获取用户收藏列表
    Result getFavoriteList(Long userId);
}
```

`GoodsFavoriteServiceImpl\.java`

```java
@Service
public class GoodsFavoriteServiceImpl implements GoodsFavoriteService {
    @Autowired
    private GoodsFavoriteMapper favoriteMapper;
    @Autowired
    private GoodsMapper goodsMapper;

    @Override
    public Result toggleFavorite(Long userId, Long goodsId) {
        // 1. 校验商品是否存在
        Goods goods = goodsMapper.selectById(goodsId);
        if (goods == null || goods.getIsDeleted() == 1) {
            return Result.fail("商品不存在或已下架");
        }

        // 2. 查询是否已收藏
        GoodsFavorite favorite = favoriteMapper.selectByUserAndGoods(userId, goodsId);
        if (favorite == null) {
            // 未收藏：新增记录
            GoodsFavorite newFavorite = new GoodsFavorite();
            newFavorite.setUserId(userId);
            newFavorite.setGoodsId(goodsId);
            newFavorite.setCreatedAt(LocalDateTime.now());
            favoriteMapper.insert(newFavorite);
            // 同时更新商品收藏数（你的goods表有favorite_count字段）
            goodsMapper.updateFavoriteCount(goodsId, 1);
            return Result.success("收藏成功");
        } else {
            // 已收藏：逻辑删除
            favorite.setIsDeleted(1);
            favorite.setUpdatedAt(LocalDateTime.now());
            favoriteMapper.updateById(favorite);
            // 更新商品收藏数
            goodsMapper.updateFavoriteCount(goodsId, -1);
            return Result.success("取消收藏成功");
        }
    }

    @Override
    public boolean isFavorite(Long userId, Long goodsId) {
        return favoriteMapper.selectByUserAndGoods(userId, goodsId) != null;
    }

    @Override
    public Result getFavoriteList(Long userId) {
        List<GoodsFavorite> favorites = favoriteMapper.selectListByUserId(userId);
        // 封装成商品详情VO返回
        List<GoodsFavoriteVO> vos = favorites.stream()
                .map(fav -> {
                    Goods goods = goodsMapper.selectById(fav.getGoodsId());
                    if (goods == null || goods.getIsDeleted() == 1) return null;
                    GoodsFavoriteVO vo = new GoodsFavoriteVO();
                    BeanUtils.copyProperties(goods, vo);
                    vo.setFavoriteId(fav.getId());
                    return vo;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        return Result.success(vos);
    }
}
```

> 补充：`goodsMapper` 中更新收藏数的 SQL
> 
> ```xml
> <update id="updateFavoriteCount">
>   UPDATE goods SET favorite_count = favorite_count + #{delta} WHERE id = #{goodsId}
> </update>
> ```
> 
> 

### 4\. Controller 接口

`GoodsFavoriteController\.java`

```java
@RestController
@RequestMapping("/api/favorite")
public class GoodsFavoriteController {
    @Autowired
    private GoodsFavoriteService favoriteService;

    // 收藏/取消收藏
    @PostMapping("/toggle")
    public Result toggleFavorite(
            @RequestParam Long userId,
            @RequestParam Long goodsId) {
        return favoriteService.toggleFavorite(userId, goodsId);
    }

    // 判断是否已收藏
    @GetMapping("/check")
    public Result<Boolean> checkFavorite(
            @RequestParam Long userId,
            @RequestParam Long goodsId) {
        boolean isFavorite = favoriteService.isFavorite(userId, goodsId);
        return Result.success(isFavorite);
    }

    // 获取用户收藏列表
    @GetMapping("/list")
    public Result<List<GoodsFavoriteVO>> getFavoriteList(@RequestParam Long userId) {
        return favoriteService.getFavoriteList(userId);
    }
}
```

---

## 三、微信小程序前端实现

### 1\. 商品详情页：收藏按钮

**WXML**

```xml
<!-- 商品详情页底部收藏按钮 -->
<view class="action-bar">
  <view class="favorite-btn {{isFavorited ? 'active' : ''}}" bindtap="toggleFavorite">
    <image src="{{isFavorited ? '/images/heart-red.png' : '/images/heart-gray.png'}}" class="icon" />
    <text>{{isFavorited ? '已收藏' : '收藏'}}</text>
  </view>
</view>
```

**WXSS**

```css
.action-bar {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  height: 100rpx;
  background: #fff;
  display: flex;
  align-items: center;
  justify-content: center;
  border-top: 1rpx solid #eee;
}
.favorite-btn {
  display: flex;
  align-items: center;
  color: #666;
}
.favorite-btn.active {
  color: #ff4444;
}
.icon {
  width: 40rpx;
  height: 40rpx;
  margin-right: 8rpx;
}
```

**JS 逻辑**

```javascript
Page({
  data: {
    goodsId: 0,
    userId: 1, // 从登录态获取
    isFavorited: false
  },

  onLoad(options) {
    this.setData({ goodsId: options.goodsId })
    this.checkFavoriteStatus()
  },

  // 1. 进入页面判断是否已收藏
  checkFavoriteStatus() {
    const { userId, goodsId } = this.data
    wx.request({
      url: 'https://your-domain.com/api/favorite/check',
      data: { userId, goodsId },
      success: (res) => {
        this.setData({ isFavorited: res.data.data })
      }
    })
  },

  // 2. 点击切换收藏状态
  toggleFavorite() {
    const { userId, goodsId, isFavorited } = this.data
    wx.request({
      url: 'https://your-domain.com/api/favorite/toggle',
      method: 'POST',
      data: { userId, goodsId },
      success: (res) => {
        if (res.data.code === 200) {
          this.setData({ isFavorited: !isFavorited })
          wx.showToast({ title: res.data.msg, icon: 'success' })
        } else {
          wx.showToast({ title: res.data.msg, icon: 'none' })
        }
      }
    })
  }
})
```

### 2\. 「我的收藏」列表页

**WXML**

```xml
<view class="favorite-list">
  <view wx:for="{{favoriteList}}" wx:key="id" class="favorite-item">
    <image src="{{item.coverImage}}" class="cover" />
    <view class="info">
      <text class="title">{{item.title}}</text>
      <text class="price">¥{{item.price}}</text>
    </view>
  </view>
  <view wx:if="{{favoriteList.length === 0}}" class="empty">暂无收藏商品</view>
</view>
```

**JS 逻辑**

```javascript
Page({
  data: {
    userId: 1,
    favoriteList: []
  },

  onShow() {
    this.getFavoriteList()
  },

  getFavoriteList() {
    wx.request({
      url: 'https://your-domain.com/api/favorite/list',
      data: { userId: this.data.userId },
      success: (res) => {
        this.setData({ favoriteList: res.data.data })
      }
    })
  }
})
```

---

## 四、功能扩展与优化

1. **防重复提交**：给 `toggleFavorite` 接口加防抖，避免用户快速点击导致重复请求

2. **事务控制**：收藏 / 取消收藏时，`goods\_favorite` 表操作和 `goods\.favorite\_count` 更新要放在同一个事务里，防止数据不一致

3. **缓存优化**：可使用 Redis 缓存用户收藏列表，减少数据库查询压力

4. **收藏数展示**：在商品列表页直接展示 `goods\.favorite\_count`，无需额外查询收藏状态

---

需要我帮你把「收藏列表页」的完整前后端代码，整理成一份可直接复制的版本吗？

> （注：文档部分内容可能由 AI 生成）
