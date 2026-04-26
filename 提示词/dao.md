## 可直接复制的完整代码（和你截图结构一致）

### 1. WXML 结构

xml









```
<!-- pages/order-bought/order-bought.wxml -->
<view class="container">
  <!-- 顶部导航 -->
  <view class="nav-bar">
    <view class="back-btn" bindtap="goBack">
      <image src="/images/back.png" class="icon" />
    </view>
    <text class="title">我买到的</text>
  </view>

  <!-- 订单列表 -->
  <scroll-view class="order-list" scroll-y>
    <view wx:for="{{orderList}}" wx:key="id" class="order-card">
      <!-- 订单头部：用户信息+状态 -->
      <view class="card-header">
        <view class="user-info">
          <image src="{{item.buyerAvatar}}" class="avatar" />
          <text class="username">{{item.buyerName}}</text>
        </view>
        <text class="status-tag completed">{{item.statusText}}</text>
      </view>

      <!-- 商品信息 -->
      <view class="goods-info">
        <image src="{{item.goodsCover}}" class="goods-img" />
        <view class="goods-detail">
          <text class="goods-name">{{item.goodsTitle}}</text>
          <view class="price-time">
            <text class="price">¥{{item.goodsPrice}}</text>
            <text class="order-time">{{item.orderTime}}</text>
          </view>
        </view>
      </view>

      <!-- 交易状态 -->
      <view class="card-footer">
        <text class="trade-status completed">{{item.tradeStatusText}}</text>
      </view>
    </view>

    <!-- 空状态 -->
    <view wx:if="{{orderList.length === 0}}" class="empty-state">
      <text class="empty-text">还没有订单</text>
      <text class="empty-desc">成交后会在这里查看订单状态</text>
    </view>

    <!-- 没有更多了 -->
    <view wx:if="{{orderList.length > 0}}" class="no-more">
      <text>没有更多了</text>
    </view>
  </scroll-view>
</view>
```

### 2. WXSS 样式

css









```
/* pages/order-bought/order-bought.wxss */
.container {
  background-color: #f5f5f5;
  min-height: 100vh;
}

.nav-bar {
  display: flex;
  align-items: center;
  background: #fff;
  padding: 20rpx 30rpx;
  position: sticky;
  top: 0;
  z-index: 99;
}

.back-btn {
  width: 40rpx;
  height: 40rpx;
  margin-right: 20rpx;
}

.icon {
  width: 100%;
  height: 100%;
}

.title {
  font-size: 34rpx;
  font-weight: 500;
  color: #333;
}

.order-list {
  padding: 20rpx;
}

.order-card {
  background: #fff;
  border-radius: 16rpx;
  padding: 30rpx;
  margin-bottom: 20rpx;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20rpx;
}

.user-info {
  display: flex;
  align-items: center;
}

.avatar {
  width: 60rpx;
  height: 60rpx;
  border-radius: 50%;
  margin-right: 16rpx;
}

.username {
  font-size: 28rpx;
  color: #333;
}

.status-tag {
  padding: 8rpx 16rpx;
  border-radius: 20rpx;
  font-size: 24rpx;
}

.status-tag.completed {
  background-color: #e8f5e9;
  color: #4caf50;
}

.goods-info {
  display: flex;
  align-items: center;
  margin-bottom: 20rpx;
}

.goods-img {
  width: 120rpx;
  height: 120rpx;
  border-radius: 12rpx;
  margin-right: 20rpx;
}

.goods-detail {
  flex: 1;
}

.goods-name {
  font-size: 30rpx;
  color: #333;
  margin-bottom: 16rpx;
  display: block;
}

.price-time {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.price {
  font-size: 32rpx;
  color: #ff4444;
  font-weight: 500;
}

.order-time {
  font-size: 24rpx;
  color: #999;
}

.card-footer {
  text-align: right;
  padding-top: 16rpx;
  border-top: 1rpx solid #f0f0f0;
}

.trade-status {
  font-size: 28rpx;
}

.trade-status.completed {
  color: #4caf50;
}

.empty-state {
  text-align: center;
  padding: 100rpx 0;
}

.empty-text {
  display: block;
  font-size: 32rpx;
  color: #666;
  margin-bottom: 16rpx;
}

.empty-desc {
  font-size: 26rpx;
  color: #999;
}

.no-more {
  text-align: center;
  padding: 30rpx 0;
  font-size: 26rpx;
  color: #999;
}
```

### 3. JS 逻辑

javascript



运行







```
// pages/order-bought/order-bought.js
const { request } = require('../../utils/request')

Page({
  data: {
    orderList: []
  },

  onShow() {
    this.getBoughtOrders()
  },

  // 获取我买到的订单列表
  getBoughtOrders() {
    wx.showLoading({ title: '加载中...' })
    const userId = wx.getStorageSync('userId') || ''
    request({
      url: '/api/order/bought',
      data: { userId }
    }).then(res => {
      if (res.code === 200) {
        this.setData({
          orderList: res.data
        })
      }
    }).finally(() => {
      wx.hideLoading()
    })
  },

  // 返回上一页
  goBack() {
    wx.navigateBack()
  },

  // 下拉刷新
  onPullDownRefresh() {
    this.getBoughtOrders().then(() => {
      wx.stopPullDownRefresh()
    })
  }
})
```

------

需要我把「待评价」页面也按同样的风格，给你写好完整的代码吗？