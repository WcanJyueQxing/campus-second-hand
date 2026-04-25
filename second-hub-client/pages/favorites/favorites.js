const { request } = require('../../utils/request')

Page({
  data: {
    list: []
  },

  onShow() {
    console.log('加载收藏列表...')
    const userInfo = wx.getStorageSync('userInfo') || {}
    const token = wx.getStorageSync('token')
    console.log('当前用户:', userInfo)
    
    // 未登录用户：弹出提示框
    if (!token || !userInfo.id) {
      wx.showModal({
        title: '提示',
        content: '请先登录或注册',
        cancelText: '取消',
        confirmText: '去登录',
        success: (res) => {
          if (res.confirm) {
            wx.navigateTo({ url: '/pages/login/login' })
          }
        }
      })
      this.setData({ list: [] })
      return
    }
    
    // 已登录用户：加载收藏列表
    request({ url: '/api/user/favorites', data: { pageNo: 1, pageSize: 50 } }).then((data) => {
      console.log('收藏数据:', data)
      this.setData({ list: data.records || [] })
    }).catch((err) => {
      console.error('加载收藏失败:', err)
    })
  },

  toDetail(e) {
    wx.navigateTo({ url: `/pages/goods-detail/goods-detail?id=${e.currentTarget.dataset.id}` })
  }
})
