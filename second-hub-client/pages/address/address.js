const { request } = require('../../utils/request')

Page({
  data: {
    addresses: []
  },

  onLoad() {
    this.loadAddresses()
  },

  onShow() {
    this.loadAddresses()
  },

  // 加载地址列表
  loadAddresses() {
    wx.showLoading({ title: '加载中...' })
    request({ url: '/api/address/list' }).then((data) => {
      if (data && data.length > 0) {
        // 转换 isDefault 为布尔值
        data.forEach(item => {
          item.isDefault = item.isDefault === 1
        })
      }
      this.setData({ addresses: data || [] })
    }).finally(() => {
      wx.hideLoading()
    })
  },

  // 新增地址
  addAddress() {
    wx.navigateTo({ url: '/pages/address-edit/address-edit' })
  },

  // 编辑地址
  editAddress(e) {
    const id = e.currentTarget.dataset.id
    wx.navigateTo({ url: `/pages/address-edit/address-edit?id=${id}` })
  },

  // 删除地址
  deleteAddress(e) {
    const id = e.currentTarget.dataset.id
    wx.showModal({
      title: '确认删除',
      content: '确定要删除此地址吗？',
      success: (res) => {
        if (res.confirm) {
          wx.showLoading({ title: '删除中...' })
          request({ url: `/api/address/${id}`, method: 'DELETE' }).then(() => {
            wx.showToast({ title: '删除成功', icon: 'success' })
            this.loadAddresses()
          }).finally(() => {
            wx.hideLoading()
          })
        }
      }
    })
  },

  // 设为默认
  setDefault(e) {
    const id = e.currentTarget.dataset.id
    wx.showLoading({ title: '设置中...' })
    request({
      url: `/api/address/${id}/default`,
      method: 'PUT'
    }).then(() => {
      wx.showToast({ title: '设置成功', icon: 'success' })
      this.loadAddresses()
    }).finally(() => {
      wx.hideLoading()
    })
  }
})