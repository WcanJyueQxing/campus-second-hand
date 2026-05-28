/**
 * 我的页面
 * 展示用户信息、统计数据和功能入口
 */
const { request } = require('../../utils/request')

Page({
  /**
   * 页面数据
   * @property {boolean} isLoggedIn - 是否已登录
   * @property {string} token - 用户令牌
   * @property {string} avatarUrl - 用户头像URL
   * @property {string} nickname - 用户昵称
   * @property {number} publishCount - 发布商品数量
   * @property {number} soldCount - 卖出商品数量
   * @property {number} boughtCount - 购买商品数量
   * @property {number} reviewCount - 待评价数量
   */
  data: {
    isLoggedIn: false,
    token: '',
    avatarUrl: '',
    nickname: '',
    publishCount: 0,
    soldCount: 0,
    boughtCount: 0,
    reviewCount: 0
  },

  /**
   * 页面显示时触发
   * 初始化TabBar状态，检查登录状态
   */
  onShow() {
    const tabBar = this.getTabBar && this.getTabBar()
    if (tabBar) {
      tabBar.setData({ selected: 3 })
    }
    this.checkLoginStatus()
  },

  /**
   * 检查登录状态
   */
  checkLoginStatus() {
    const token = wx.getStorageSync('token') || ''
    const userInfo = wx.getStorageSync('userInfo') || {}
    const isLoggedIn = !!token

    if (isLoggedIn) {
      this.setData({
        isLoggedIn: true,
        token,
        avatarUrl: userInfo.avatarUrl || '',
        nickname: userInfo.nickname || ''
      })
      this.loadUserProfile()
      this.loadTradeCounts()
    } else {
      this.setData({
        isLoggedIn: false,
        token: '',
        avatarUrl: '',
        nickname: '',
        publishCount: 0,
        soldCount: 0,
        boughtCount: 0,
        reviewCount: 0
      })
    }
  },

  /**
   * 加载用户信息
   */
  loadUserProfile() {
    request({ url: '/api/user/info' }).then((data) => {
      if (data) {
        this.setData({
          avatarUrl: data.avatarUrl || '',
          nickname: data.nickname || ''
        })
        const userInfo = wx.getStorageSync('userInfo') || {}
        userInfo.avatarUrl = data.avatarUrl
        userInfo.nickname = data.nickname
        wx.setStorageSync('userInfo', userInfo)
      }
    }).catch(() => {})
  },

  /**
   * 加载交易统计数据
   */
  loadTradeCounts() {
    request({ url: '/api/user/goods/my' }).then((data) => {
      if (data && data.records) {
        this.setData({ publishCount: data.records.length || 0 })
      }
    }).catch(() => {})

    request({ url: '/api/user/orders/stats' }).then((data) => {
      if (data) {
        this.setData({
          soldCount: data.soldCount || 0,
          boughtCount: data.boughtCount || 0,
          reviewCount: data.pendingReviewCount || 0
        })
      }
    }).catch(() => {})
  },

  /**
   * 跳转到登录页面
   */
  toLogin() {
    wx.navigateTo({ url: '/pages/login/login' })
  },

  /**
   * 跳转到个人资料编辑页面
   */
  toProfileEdit() {
    if (!this.data.isLoggedIn) {
      this.toLogin()
      return
    }
    wx.navigateTo({ url: '/pages/profile-edit/profile-edit' })
  },

  /**
   * 跳转到我的商品页面
   */
  toMyGoods() {
    if (!this.data.isLoggedIn) {
      this.toLogin()
      return
    }
    wx.navigateTo({ url: '/pages/my-goods/my-goods' })
  },

  /**
   * 跳转到发布页面
   */
  toPublish() {
    if (!this.data.isLoggedIn) {
      this.toLogin()
      return
    }
    wx.navigateTo({ url: '/pages/my-goods/my-goods' })
  },

  /**
   * 跳转到卖出订单页面
   */
  toSoldOrders() {
    if (!this.data.isLoggedIn) {
      this.toLogin()
      return
    }
    wx.navigateTo({ url: '/pages/my-sold/my-sold' })
  },

  /**
   * 跳转到购买订单页面
   */
  toBoughtOrders() {
    if (!this.data.isLoggedIn) {
      this.toLogin()
      return
    }
    wx.navigateTo({ url: '/pages/my-bought/my-bought' })
  },

  /**
   * 跳转到待评价页面
   */
  toPendingReviews() {
    if (!this.data.isLoggedIn) {
      this.toLogin()
      return
    }
    wx.navigateTo({ url: '/pages/my-reviews/my-reviews' })
  },

  /**
   * 跳转到收藏页面
   */
  toFavorites() {
    if (!this.data.isLoggedIn) {
      this.toLogin()
      return
    }
    wx.navigateTo({ url: '/pages/favorites/favorites' })
  },

  /**
   * 跳转到浏览历史页面
   */
  toHistory() {
    if (!this.data.isLoggedIn) {
      this.toLogin()
      return
    }
    wx.navigateTo({ url: '/pages/history/history' })
  },

  /**
   * 跳转到设置页面
   */
  toSettings() {
    if (!this.data.isLoggedIn) {
      this.toLogin()
      return
    }
    wx.navigateTo({ url: '/pages/settings/settings' })
  },

  /**
   * 退出登录
   */
  handleLogout() {
    wx.showModal({
      title: '确认退出',
      content: '确定要退出登录吗？',
      success: (res) => {
        if (res.confirm) {
          wx.removeStorageSync('token')
          wx.removeStorageSync('userInfo')
          this.setData({
            isLoggedIn: false,
            token: '',
            avatarUrl: '',
            nickname: '',
            publishCount: 0,
            soldCount: 0,
            boughtCount: 0,
            reviewCount: 0
          })
          wx.showToast({ title: '已退出登录', icon: 'success' })
        }
      }
    })
  }
})