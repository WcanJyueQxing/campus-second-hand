const { request } = require('../../utils/request')

Page({
  data: {
    orderId: '',
    order: {},
    stars: [false, false, false, false, false],
    scoreText: '',
    scoreDesc: ['很差', '一般', '满意', '很好', '非常满意'],
    content: '',
    hasScore: false
  },

  onLoad(options) {
    const { orderId } = options
    if (!orderId) {
      wx.showToast({ title: '参数错误', icon: 'none' })
      setTimeout(() => {
        wx.navigateBack({ delta: 1 })
      }, 1000)
      return
    }
    this.setData({ orderId })
    this.loadOrderDetail()
  },

  loadOrderDetail() {
    request({
      url: `/api/user/orders/${this.data.orderId}`
    }).then((data) => {
      if (data) {
        this.setData({ order: data })
      }
    }).catch((err) => {
      console.error('加载订单详情失败:', err)
      wx.showToast({ title: '加载失败，请重试', icon: 'none' })
    })
  },

  selectStar(e) {
    const index = e.currentTarget.dataset.index
    const stars = this.data.stars.map((_, i) => i <= index)
    this.setData({
      stars: stars,
      scoreText: this.data.scoreDesc[index],
      hasScore: true
    })
  },

  bindContent(e) {
    this.setData({ content: e.detail.value })
  },

  submitComment() {
    const score = this.data.stars.filter(item => item).length
    if (!score) {
      wx.showToast({ title: '请选择评分', icon: 'none' })
      return
    }
    if (!this.data.content.trim()) {
      wx.showToast({ title: '请输入评价内容', icon: 'none' })
      return
    }

    wx.showLoading({ title: '提交中...' })
    request({
      url: '/api/user/comments',
      method: 'POST',
      data: {
        orderId: this.data.orderId,
        goodsId: this.data.order.goodsId,
        rating: score,
        content: this.data.content.trim()
      }
    }).then(() => {
      wx.showToast({
        title: '评价成功',
        icon: 'success'
      })
      setTimeout(() => {
        wx.navigateBack({ delta: 1 })
      }, 1500)
    }).catch((err) => {
      console.error('提交评价失败:', err)
      wx.showToast({ title: '提交失败，请重试', icon: 'none' })
    }).finally(() => {
      wx.hideLoading()
    })
  },

  navigateBack() {
    wx.navigateBack({ delta: 1 })
  }
})