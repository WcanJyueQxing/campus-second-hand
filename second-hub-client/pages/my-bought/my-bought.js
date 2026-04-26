const { request } = require('../../utils/request')

Page({
  data: {
    list: [],
    pageNo: 1,
    pageSize: 10,
    hasMore: true,
    loading: false,
    isLoggedIn: false
  },

  onLoad() {
    this.checkLoginAndLoad()
  },

  onShow() {
    this.checkLoginAndLoad()
  },

  checkLoginAndLoad() {
    const token = wx.getStorageSync('token')
    if (!token) {
      wx.showModal({
        title: '提示',
        content: '请先登录后再查看订单',
        confirmText: '去登录',
        success: (res) => {
          if (res.confirm) {
            wx.navigateTo({ url: '/pages/login/login' })
          } else {
            wx.navigateBack({ delta: 1 })
          }
        }
      })
      return
    }
    this.setData({ isLoggedIn: true })
    this.setData({ pageNo: 1, hasMore: true, list: [] })
    this.loadOrders()
  },

  onPullDownRefresh() {
    const token = wx.getStorageSync('token')
    if (!token) {
      wx.stopPullDownRefresh()
      return
    }
    this.setData({ pageNo: 1, hasMore: true, list: [] })
    this.loadOrders(() => {
      wx.stopPullDownRefresh()
    })
  },

  loadOrders(callback) {
    if (this.data.loading) {
      callback && callback()
      return
    }

    this.setData({ loading: true })

    request({
      url: '/api/order/bought',
      data: {
        pageNo: this.data.pageNo,
        pageSize: this.data.pageSize
      }
    }).then((data) => {
      const records = data.records || []
      const processedList = records.map((item) => ({
        ...item,
        _statusText: this.getStatusText(item.orderStatus),
        _statusClass: this.getStatusClass(item.orderStatus)
      }))

      const newList = this.data.pageNo === 1 ? processedList : [...this.data.list, ...processedList]
      this.setData({
        list: newList,
        hasMore: records.length >= this.data.pageSize,
        pageNo: this.data.pageNo + 1
      })
    }).catch((err) => {
      console.error('加载订单失败:', err)
      if (err.code === 401) {
        wx.showModal({
          title: '提示',
          content: '登录已过期，请重新登录',
          confirmText: '去登录',
          success: (res) => {
            if (res.confirm) {
              wx.navigateTo({ url: '/pages/login/login' })
            }
          }
        })
      } else {
        wx.showToast({
          title: '加载失败，请重试',
          icon: 'none'
        })
      }
    }).finally(() => {
      this.setData({ loading: false })
      callback && callback()
    })
  },

  loadMore() {
    if (!this.data.hasMore || this.data.loading) return
    this.loadOrders()
  },

  navigateBack() {
    wx.navigateBack({ delta: 1 })
  },

  toOrderDetail(e) {
    const orderId = e.currentTarget.dataset.id
    wx.navigateTo({
      url: `/pages/order-detail/order-detail?id=${orderId}`
    })
  },

  preventBubble() {
  },

  doAction(e) {
    const { id, action } = e.currentTarget.dataset
    const actionText = {
      pay: '支付',
      buyerConfirm: '确认收货',
      cancel: '取消订单',
      comment: '评价'
    }

    if (action === 'cancel') {
      wx.showModal({
        title: '确认取消',
        content: '确定要取消该订单吗？',
        success: (res) => {
          if (res.confirm) {
            this.executeAction(id, action)
          }
        }
      })
    } else if (action === 'pay' || action === 'buyerConfirm') {
      wx.showModal({
        title: '确认操作',
        content: '确定要' + actionText[action] + '吗？',
        success: (res) => {
          if (res.confirm) {
            this.executeAction(id, action)
          }
        }
      })
    } else if (action === 'comment') {
      wx.navigateTo({
        url: `/pages/comment/comment?orderId=${id}`
      })
    } else {
      this.executeAction(id, action)
    }
  },

  executeAction(orderId, action) {
    const apiMap = {
      pay: `/api/user/orders/${orderId}/pay`,
      buyerConfirm: `/api/user/orders/${orderId}/buyer-confirm`,
      cancel: `/api/user/orders/${orderId}/cancel`
    }

    wx.showLoading({ title: '处理中...' })
    request({
      url: apiMap[action],
      method: 'POST'
    }).then(() => {
      wx.showToast({
        title: '操作成功',
        icon: 'success'
      })
      this.setData({ pageNo: 1, hasMore: true, list: [] })
      this.loadOrders()
    }).catch((err) => {
      console.error('操作失败:', err)
      wx.showToast({
        title: '操作失败，请重试',
        icon: 'none'
      })
    }).finally(() => {
      wx.hideLoading()
    })
  },

  getStatusText(status) {
    const text = String(status || '').toUpperCase()
    if (text === 'PENDING_PAYMENT') return '待支付'
    if (text === 'PAID') return '已支付'
    if (text === 'SELLER_CONFIRMED') return '待收货'
    if (text === 'BUYER_CONFIRMED') return '待确认'
    if (text === 'COMPLETED') return '已完成'
    if (text === 'CANCELLED') return '已取消'
    return text || '未知'
  },

  getStatusClass(status) {
    const text = String(status || '').toUpperCase()
    if (text === 'PENDING_PAYMENT') return 'warning'
    if (text === 'PAID') return 'info'
    if (text === 'SELLER_CONFIRMED') return 'info'
    if (text === 'BUYER_CONFIRMED') return 'info'
    if (text === 'COMPLETED') return 'success'
    if (text === 'CANCELLED') return 'danger'
    return 'info'
  }
})