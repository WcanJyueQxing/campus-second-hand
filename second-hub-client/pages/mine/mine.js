const { request } = require('../../utils/request')

Page({
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

  onShow() {
    const tabBar = this.getTabBar && this.getTabBar()
    if (tabBar) {
      tabBar.setData({ selected: 3 })
    }
    this.checkLoginStatus()
  },

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
    }).catch(() => {
    })
  },

  loadTradeCounts() {
    request({ url: '/api/user/goods/my' }).then((data) => {
      if (data && data.list) {
        this.setData({ publishCount: data.list.length || 0 })
      }
    }).catch(() => {
    })

    request({ url: '/api/user/orders/my?asRole=seller' }).then((data) => {
      if (data && data.list) {
        this.setData({ soldCount: data.list.length || 0 })
      }
    }).catch(() => {
    })

    request({ url: '/api/user/orders/my?asRole=buyer' }).then((data) => {
      if (data && data.list) {
        this.setData({ boughtCount: data.list.length || 0 })
      }
    }).catch(() => {
    })
  },

  toLogin() {
    wx.navigateTo({ url: '/pages/login/login' })
  },

  toProfileEdit() {
    if (!this.data.isLoggedIn) {
      this.toLogin()
      return
    }
    wx.navigateTo({ url: '/pages/profile-edit/profile-edit' })
  },

  toMyGoods() {
    if (!this.data.isLoggedIn) {
      this.toLogin()
      return
    }
    wx.navigateTo({ url: '/pages/my-goods/my-goods' })
  },

  toSoldOrders() {
    if (!this.data.isLoggedIn) {
      this.toLogin()
      return
    }
    wx.navigateTo({ url: '/pages/orders/orders?type=sold' })
  },

  toBoughtOrders() {
    if (!this.data.isLoggedIn) {
      this.toLogin()
      return
    }
    wx.navigateTo({ url: '/pages/orders/orders?type=bought' })
  },

  toPendingReviews() {
    if (!this.data.isLoggedIn) {
      this.toLogin()
      return
    }
    wx.navigateTo({ url: '/pages/orders/orders?type=pending_review' })
  },

  toFavorites() {
    if (!this.data.isLoggedIn) {
      this.toLogin()
      return
    }
    wx.navigateTo({ url: '/pages/favorites/favorites' })
  },

  toHistory() {
    if (!this.data.isLoggedIn) {
      this.toLogin()
      return
    }
    wx.navigateTo({ url: '/pages/history/history' })
  },

  toSettings() {
    if (!this.data.isLoggedIn) {
      this.toLogin()
      return
    }
    wx.navigateTo({ url: '/pages/settings/settings' })
  },

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