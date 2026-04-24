const { request } = require('../../utils/request')

Page({
  data: {
    notifications: {
      order: true,
      goods: true,
      system: true,
      pushEnabled: true,
      sound: true,
      vibrate: true
    }
  },

  onLoad() {
    this.loadNotificationSettings()
  },

  loadNotificationSettings() {
    wx.showLoading({ title: '加载中...' })
    request({ url: '/api/user/notification' }).then((data) => {
      if (data) {
        this.setData({ notifications: data })
      }
    }).catch(() => {
      // 使用默认值
    }).finally(() => {
      wx.hideLoading()
    })
  },

  onSwitchChange(e) {
    const key = e.currentTarget.dataset.key
    const value = e.detail.value
    const notifications = this.data.notifications
    notifications[key] = value

    this.setData({ notifications })

    request({
      url: '/api/user/notification',
      method: 'PUT',
      data: notifications
    }).then(() => {
      wx.showToast({
        title: '设置已保存',
        icon: 'success'
      })
    }).catch(() => {
      wx.showToast({ title: '保存失败', icon: 'none' })
      this.loadNotificationSettings()
    })
  }
})