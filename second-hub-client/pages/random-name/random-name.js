const { request } = require('../../utils/request')

Page({
  data: {
    gender: 'random', // random, male, female
    names: [],
    loading: false
  },

  onLoad(options) {
    // 页面加载时初始化
  },

  // 选择性别
  selectGender(e) {
    const gender = e.currentTarget.dataset.gender
    this.setData({ gender })
  },

  // 生成单个姓名
  generateSingle() {
    this.setData({ loading: true })
    
    request({
      url: '/api/user/random-name/generate',
      method: 'GET',
      data: {
        gender: this.data.gender
      }
    }).then((name) => {
      this.setData({
        names: [name],
        loading: false
      })
    }).catch((err) => {
      wx.showToast({
        title: '生成失败，请重试',
        icon: 'none'
      })
      this.setData({ loading: false })
    })
  },

  // 批量生成5个姓名
  generateBatch() {
    this.setData({ loading: true })
    
    request({
      url: '/api/user/random-name/generate-batch',
      method: 'GET',
      data: {
        gender: this.data.gender,
        count: 5
      }
    }).then((names) => {
      this.setData({
        names: names,
        loading: false
      })
    }).catch((err) => {
      wx.showToast({
        title: '生成失败，请重试',
        icon: 'none'
      })
      this.setData({ loading: false })
    })
  },

  // 复制姓名
  copyName(e) {
    const name = e.currentTarget.dataset.name
    wx.setClipboardData({
      data: name,
      success: () => {
        wx.showToast({
          title: '复制成功',
          icon: 'success'
        })
      }
    })
  }
})