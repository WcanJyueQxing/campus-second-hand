const { request } = require('../../utils/request')

Page({
  data: {
    phone: ''
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
          phone: phone
        })
      }
    }).catch(() => {
      this.setData({
        phone: '138****5678'
      })
    }).finally(() => {
      wx.hideLoading()
    })
  },

  toChangePhone() {
    wx.navigateTo({ url: '/pages/change-phone/change-phone' })
  },

  toChangePassword() {
    wx.navigateTo({ url: '/pages/change-password/change-password' })
  }
})