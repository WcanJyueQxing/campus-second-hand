const { request } = require('../../utils/request')

Page({
  data: {
    userId: '',
    createTime: '',
    phone: '',
    hasPassword: false
  },

  onLoad() {
    this.loadAccountInfo()
  },

  loadAccountInfo() {
    wx.showLoading({ title: '加载中...' })
    request({ url: '/api/user/info' }).then((data) => {
      if (data) {
        const phone = data.phone ? data.phone.replace(/(\d{3})\d{4}(\d{4})/, '$1****$2') : '未绑定'
        this.setData({
          userId: data.id || '',
          phone: phone,
          createTime: data.createdAt ? data.createdAt.split('T')[0] : ''
        })
      }
    }).catch(() => {
      this.setData({
        userId: '10001',
        phone: '138****5678',
        createTime: '2024-01-01'
      })
    }).finally(() => {
      wx.hideLoading()
    })
  },

  changePhone() {
    wx.navigateTo({ url: '/pages/change-phone/change-phone' })
  },

  changePassword() {
    wx.navigateTo({ url: '/pages/change-password/change-password' })
  },

  logoutDevice() {
    wx.showModal({
      title: '确认退出',
      content: '确定要退出当前设备吗？',
      success: (res) => {
        if (res.confirm) {
          wx.showLoading({ title: '退出中...' })
          request({ url: '/api/user/auth/logout', method: 'POST' }).then(() => {
            wx.showToast({ title: '已退出', icon: 'success' })
            setTimeout(() => {
              wx.clearStorageSync()
              wx.redirectTo({ url: '/pages/login/login' })
            }, 1500)
          }).catch((err) => {
            wx.showToast({ title: '退出失败', icon: 'none' })
          }).finally(() => {
            wx.hideLoading()
          })
        }
      }
    })
  }
})