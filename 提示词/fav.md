我直接给你一套完整的、可直接复制使用的前后端代码，包含：

- 单条删除（直接取消收藏）
- 批量删除（选择性删除）
- 完整的交互反馈和状态管理

------

## 1. 后端接口改造（SpringBoot）

### `GoodsFavoriteController.java`

java



运行







```
package com.nie.secondhub.controller.user;

import com.nie.secondhub.service.GoodsFavoriteService;
import com.nie.secondhub.util.Result;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/api/user/favorites")
public class GoodsFavoriteController {

    @Resource
    private GoodsFavoriteService goodsFavoriteService;

    // 收藏/取消收藏（原接口不变）
    @PostMapping("/{goodsId}")
    public Result toggleFavorite(
            @RequestHeader(value = "token", required = false) String token,
            @PathVariable Long goodsId) {
        Long userId = 1L;
        return goodsFavoriteService.toggleFavorite(userId, goodsId);
    }

    // 获取收藏列表（原接口不变）
    @GetMapping
    public Result getMyFavorites(
            @RequestHeader(value = "token", required = false) String token) {
        Long userId = 1L;
        return goodsFavoriteService.getMyFavorites(userId);
    }

    // 单条删除收藏（新增）
    @DeleteMapping("/{goodsId}")
    public Result deleteFavorite(
            @RequestHeader(value = "token", required = false) String token,
            @PathVariable Long goodsId) {
        Long userId = 1L;
        return goodsFavoriteService.deleteFavorite(userId, goodsId);
    }

    // 批量删除收藏（新增）
    @DeleteMapping("/batch")
    public Result batchDeleteFavorites(
            @RequestHeader(value = "token", required = false) String token,
            @RequestBody List<Long> goodsIds) {
        Long userId = 1L;
        return goodsFavoriteService.batchDeleteFavorites(userId, goodsIds);
    }
}
```

### `GoodsFavoriteService.java` 接口新增

java



运行







```
// 单条删除
Result deleteFavorite(Long userId, Long goodsId);
// 批量删除
Result batchDeleteFavorites(Long userId, List<Long> goodsIds);
```

### `GoodsFavoriteServiceImpl.java` 实现

java



运行







```
@Override
@Transactional(rollbackFor = Exception.class)
public Result deleteFavorite(Long userId, Long goodsId) {
    // 查询收藏记录
    GoodsFavorite favorite = goodsFavoriteMapper.selectByUserAndGoods(userId, goodsId);
    if (favorite == null || favorite.getIsDeleted() == 1) {
        return Result.error("该商品未收藏");
    }

    // 标记为删除
    favorite.setIsDeleted(1);
    favorite.setUpdatedAt(LocalDateTime.now());
    goodsFavoriteMapper.updateById(favorite);

    // 减少商品收藏数
    goodsMapper.reduceFavoriteCount(goodsId);
    return Result.success("取消收藏成功");
}

@Override
@Transactional(rollbackFor = Exception.class)
public Result batchDeleteFavorites(Long userId, List<Long> goodsIds) {
    for (Long goodsId : goodsIds) {
        GoodsFavorite favorite = goodsFavoriteMapper.selectByUserAndGoods(userId, goodsId);
        if (favorite != null && favorite.getIsDeleted() == 0) {
            favorite.setIsDeleted(1);
            favorite.setUpdatedAt(LocalDateTime.now());
            goodsFavoriteMapper.updateById(favorite);
            goodsMapper.reduceFavoriteCount(goodsId);
        }
    }
    return Result.success("批量取消收藏成功");
}
```

------

## 2. 小程序端完整改造（支持单删 + 批量删）

### `favorites.wxml`

xml









```
<view class="container">
  <!-- 顶部标题栏 -->
  <view class="header">
    <view class="back-btn" bindtap="goBack">
      <text class="back-icon">←</text>
    </view>
    <text class="title">我的收藏</text>
    <view class="edit-btn" bindtap="toggleEditMode">
      <text>{{isEditMode ? '完成' : '编辑'}}</text>
    </view>
  </view>

  <!-- 收藏列表 -->
  <scroll-view scroll-y class="list-container" wx:if="{{list.length > 0}}">
    <view 
      wx:for="{{list}}" 
      wx:key="id" 
      class="item-card" 
      bindtap="{{isEditMode ? 'toggleSelect' : 'goDetail'}}" 
      data-id="{{item.id}}"
    >
      <!-- 编辑模式的选择框 -->
      <view wx:if="{{isEditMode}}" class="checkbox">
        <view class="check-icon {{item.selected ? 'checked' : ''}}"></view>
      </view>
      
      <image class="cover" src="{{item.coverImage}}" mode="aspectFill" />
      <view class="info">
        <text class="name">{{item.title}}</text>
        <text class="price">¥{{item.price}}</text>
      </view>

      <!-- 单条删除按钮（仅编辑模式） -->
      <view wx:if="{{isEditMode}}" class="delete-btn" bindtap="deleteSingle" data-id="{{item.id}}">
        <text>删除</text>
      </view>
    </view>
  </scroll-view>

  <!-- 空状态 -->
  <view class="empty-state" wx:else>
    <view class="empty-icon">💖</view>
    <text class="empty-title">你还没有收藏商品</text>
    <text class="empty-tip">去首页逛逛，把喜欢的宝贝收藏起来吧</text>
  </view>

  <!-- 底部批量删除栏（仅编辑模式） -->
  <view class="bottom-bar" wx:if="{{isEditMode && list.length > 0}}">
    <view class="select-all" bindtap="toggleSelectAll">
      <view class="check-icon {{isAllSelected ? 'checked' : ''}}"></view>
      <text>全选</text>
    </view>
    <view class="delete-all-btn" bindtap="batchDelete">
      <text>删除选中({{selectedCount}})</text>
    </view>
  </view>
</view>
```

### `favorites.wxss`

css









```
/* 全局容器 */
.container {
  width: 100%;
  min-height: 100vh;
  background-color: #f5f7fa;
}

/* 顶部标题栏 */
.header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 30rpx;
  background-color: #fff;
  position: sticky;
  top: 0;
  z-index: 10;
}

.back-btn {
  width: 60rpx;
}

.back-icon {
  font-size: 36rpx;
  color: #333;
}

.title {
  font-size: 36rpx;
  font-weight: 600;
  color: #333;
}

.edit-btn {
  color: #007aff;
  font-size: 30rpx;
}

/* 列表容器 */
.list-container {
  padding: 20rpx;
  padding-bottom: 120rpx;
}

/* 商品卡片 */
.item-card {
  display: flex;
  align-items: center;
  background-color: #fff;
  border-radius: 16rpx;
  padding: 20rpx;
  margin-bottom: 20rpx;
  box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.05);
  position: relative;
}

.checkbox {
  margin-right: 20rpx;
}

.check-icon {
  width: 40rpx;
  height: 40rpx;
  border: 2rpx solid #ddd;
  border-radius: 50%;
}

.check-icon.checked {
  background-color: #007aff;
  border-color: #007aff;
}

.cover {
  width: 160rpx;
  height: 160rpx;
  border-radius: 12rpx;
  object-fit: cover;
}

.info {
  flex: 1;
  margin-left: 24rpx;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  height: 160rpx;
}

.name {
  font-size: 30rpx;
  color: #333;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  line-height: 1.4;
}

.price {
  font-size: 34rpx;
  font-weight: bold;
  color: #ff4d4f;
}

.delete-btn {
  background-color: #ff4d4f;
  color: #fff;
  padding: 10rpx 20rpx;
  border-radius: 8rpx;
  font-size: 26rpx;
}

/* 空状态 */
.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 150rpx 40rpx;
  color: #999;
}

.empty-icon {
  font-size: 120rpx;
  margin-bottom: 30rpx;
}

.empty-title {
  font-size: 32rpx;
  margin-bottom: 16rpx;
  color: #666;
}

.empty-tip {
  font-size: 26rpx;
  color: #bbb;
}

/* 底部批量删除栏 */
.bottom-bar {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 20rpx 30rpx;
  background-color: #fff;
  box-shadow: 0 -2rpx 10rpx rgba(0, 0, 0, 0.05);
}

.select-all {
  display: flex;
  align-items: center;
}

.select-all text {
  margin-left: 10rpx;
  font-size: 30rpx;
}

.delete-all-btn {
  background-color: #ff4d4f;
  color: #fff;
  padding: 15rpx 30rpx;
  border-radius: 8rpx;
  font-size: 30rpx;
}
```

### `favorites.js`

javascript



运行







```
Page({
  data: {
    list: [],
    isEditMode: false, // 是否处于编辑模式
    isAllSelected: false, // 是否全选
    selectedCount: 0 // 选中数量
  },

  onLoad() {
    this.loadFavorites()
  },

  onShow() {
    this.loadFavorites()
  },

  // 加载收藏列表
  loadFavorites() {
    const token = wx.getStorageSync('token')
    wx.request({
      url: 'http://localhost:8080/api/user/favorites',
      method: 'GET',
      header: { token },
      success: (res) => {
        if (res.data && res.data.code === 200 && res.data.data) {
          const list = res.data.data.map(item => ({
            ...item,
            selected: false // 新增选中状态
          }))
          this.setData({ list, isEditMode: false, isAllSelected: false, selectedCount: 0 })
        }
      }
    })
  },

  // 切换编辑模式
  toggleEditMode() {
    this.setData({
      isEditMode: !this.data.isEditMode,
      isAllSelected: false,
      selectedCount: 0,
      list: this.data.list.map(item => ({ ...item, selected: false }))
    })
  },

  // 切换单条选中状态
  toggleSelect(e) {
    const id = e.currentTarget.dataset.id
    const list = this.data.list.map(item => {
      if (item.id === id) {
        return { ...item, selected: !item.selected }
      }
      return item
    })
    const selectedCount = list.filter(item => item.selected).length
    const isAllSelected = selectedCount === list.length && list.length > 0
    this.setData({ list, selectedCount, isAllSelected })
  },

  // 全选/取消全选
  toggleSelectAll() {
    const isAllSelected = !this.data.isAllSelected
    const list = this.data.list.map(item => ({ ...item, selected: isAllSelected }))
    const selectedCount = isAllSelected ? list.length : 0
    this.setData({ list, isAllSelected, selectedCount })
  },

  // 单条删除
  deleteSingle(e) {
    const id = e.currentTarget.dataset.id
    wx.showModal({
      title: '提示',
      content: '确定要取消收藏该商品吗？',
      success: (res) => {
        if (res.confirm) {
          const token = wx.getStorageSync('token')
          wx.request({
            url: `http://localhost:8080/api/user/favorites/${id}`,
            method: 'DELETE',
            header: { token },
            success: () => {
              wx.showToast({ title: '取消收藏成功' })
              this.loadFavorites()
            }
          })
        }
      }
    })
  },

  // 批量删除
  batchDelete() {
    const selectedIds = this.data.list.filter(item => item.selected).map(item => item.id)
    if (selectedIds.length === 0) {
      wx.showToast({ title: '请选择要删除的商品', icon: 'none' })
      return
    }

    wx.showModal({
      title: '提示',
      content: `确定要取消收藏选中的${selectedIds.length}件商品吗？`,
      success: (res) => {
        if (res.confirm) {
          const token = wx.getStorageSync('token')
          wx.request({
            url: 'http://localhost:8080/api/user/favorites/batch',
            method: 'DELETE',
            header: { token, 'content-type': 'application/json' },
            data: selectedIds,
            success: () => {
              wx.showToast({ title: '批量取消收藏成功' })
              this.loadFavorites()
            }
          })
        }
      }
    })
  },

  // 返回上一页
  goBack() {
    wx.navigateBack()
  },

  // 跳转到商品详情
  goDetail(e) {
    const goodsId = e.currentTarget.dataset.id
    wx.navigateTo({
      url: `/pages/goods-detail/goods-detail?id=${goodsId}`
    })
  }
})
```

------

## 🎯 功能效果说明

1. **编辑模式**：点击右上角「编辑」按钮，进入批量管理模式
2. **单条删除**：编辑模式下，每个商品右侧会出现「删除」按钮，点击即可取消收藏
3. **批量删除**：编辑模式下，勾选多个商品，点击底部「删除选中」即可批量取消
4. **全选 / 取消全选**：底部栏支持一键全选，方便快速操作
5. **状态同步**：删除后列表自动刷新，和商品详情页的收藏状态保持一致