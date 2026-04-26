const { request } = require('../../utils/request')

Page({
  data: {
    asRole: 'buyer',
    list: [],
    pageNo: 1,
    pageSize: 20,
    hasMore: true,
    loading: false
  },

  onLoad(options) {
    // 优先从globalData读取类型参数（来自switchTab跳转）
    const app = getApp()
    const globalData = app.globalData || {}
    
    if (globalData.orderTabType) {
      if (globalData.orderTabType === 'sold') {
        this.setData({ asRole: 'seller' })
      } else if (globalData.orderTabType === 'bought') {
        this.setData({ asRole: 'buyer' })
      } else if (globalData.orderTabType === 'pending_review') {
        this.setData({ asRole: 'buyer' })
        this.loadPendingReviews()
        // 清除globalData中的类型，避免影响下次跳转
        app.globalData.orderTabType = null
        return
      }
      // 清除globalData中的类型，避免影响下次跳转
      app.globalData.orderTabType = null
    } else if (options.type) {
      // 备用：从URL参数读取（来自navigateTo跳转，非Tab页面）
      if (options.type === 'sold') {
        this.setData({ asRole: 'seller' })
      } else if (options.type === 'bought') {
        this.setData({ asRole: 'buyer' })
      } else if (options.type === 'pending_review') {
        this.setData({ asRole: 'buyer' })
        this.loadPendingReviews()
        return
      }
    }
  },

  onShow() {
    this.setData({ pageNo: 1, hasMore: true, list: [] })
    this.loadOrders()
  },

  onPullDownRefresh() {
    this.setData({ pageNo: 1, hasMore: true, list: [] })
    this.loadOrders(() => {
      wx.stopPullDownRefresh()
    })
  },

  switchRole(e) {
    const role = e.currentTarget.dataset.role
    if (role === this.data.asRole) {
      return
    }
    this.setData({ 
      asRole: role, 
      pageNo: 1, 
      hasMore: true, 
      list: [] 
    })
    this.loadOrders()
  },

  loadOrders(callback) {
    if (this.data.loading || !this.data.hasMore) {
      callback && callback()
      return
    }

    this.setData({ loading: true })

    request({
      url: '/api/user/orders/my',
      data: { 
        asRole: this.data.asRole, 
        pageNo: this.data.pageNo, 
        pageSize: this.data.pageSize 
      }
    }).then((data) => {
      const records = data.records || []
      const processedList = records.map((item) => ({
        ...item,
        _orderStatusText: this.getOrderStatusText(item.orderStatus),
        _orderStatusClass: this.getOrderStatusClass(item.orderStatus),
        _payStatusText: this.getPayStatusText(item.payStatus),
        _payStatusClass: this.getPayStatusClass(item.payStatus),
        createdAt: this.formatDate(item.createdAt)
      }))

      const newList = this.data.pageNo === 1 ? processedList : [...this.data.list, ...processedList]
      this.setData({
        list: newList,
        hasMore: processedList.length >= this.data.pageSize,
        pageNo: this.data.pageNo + 1
      })
    }).finally(() => {
      this.setData({ loading: false })
      callback && callback()
    })
  },

  loadPendingReviews() {
    if (this.data.loading) return

    this.setData({ loading: true })

    request({
      url: '/api/user/orders/my',
      data: { asRole: 'buyer', pageNo: 1, pageSize: 20 }
    }).then((data) => {
      const records = data.records || []
      const list = records
        .filter(item => item.orderStatus === 'COMPLETED' && !item.hasComment)
        .map((item) => ({
          ...item,
          _orderStatusText: this.getOrderStatusText(item.orderStatus),
          _orderStatusClass: this.getOrderStatusClass(item.orderStatus),
          _payStatusText: this.getPayStatusText(item.payStatus),
          _payStatusClass: this.getPayStatusClass(item.payStatus),
          createdAt: this.formatDate(item.createdAt)
        }))
      this.setData({ list })
    }).finally(() => {
      this.setData({ loading: false })
    })
  },

  loadMore() {
    if (!this.data.hasMore || this.data.loading) return
    this.loadOrders()
  },

  toOrderDetail(e) {
    const orderId = e.currentTarget.dataset.id
    wx.navigateTo({ 
      url: `/pages/order-detail/order-detail?id=${orderId}` 
    })
  },

  formatDate(dateString) {
    if (!dateString) return ''
    const date = new Date(dateString)
    const year = date.getFullYear()
    const month = String(date.getMonth() + 1).padStart(2, '0')
    const day = String(date.getDate()).padStart(2, '0')
    const hours = String(date.getHours()).padStart(2, '0')
    const minutes = String(date.getMinutes()).padStart(2, '0')
    return `${year}-${month}-${day} ${hours}:${minutes}`
  },

  getOrderStatusText(status) {
    const text = String(status || '')
    if (text === '1' || text.toUpperCase() === 'PENDING') return '待处理'
    if (text === '2' || text.toUpperCase() === 'PROCESSING') return '处理中'
    if (text === '3' || text.toUpperCase() === 'FINISHED') return '已完成'
    if (text === '4' || text.toUpperCase() === 'CANCELLED') return '已取消'
    if (text.toUpperCase() === 'PENDING_PAYMENT') return '待支付'
    if (text.toUpperCase() === 'PAID') return '已支付'
    if (text.toUpperCase() === 'SELLER_CONFIRMED') return '卖家已确认'
    if (text.toUpperCase() === 'BUYER_CONFIRMED') return '买家已确认'
    if (text.toUpperCase() === 'COMPLETED') return '已完成'
    if (text.toUpperCase() === 'CANCELLED') return '已取消'
    return text || '未知'
  },

  getOrderStatusClass(status) {
    const text = String(status || '').toUpperCase()
    if (text === '1' || text === 'PENDING') return 'tag--warning'
    if (text === '2' || text === 'PROCESSING') return 'tag--info'
    if (text === '3' || text === 'FINISHED') return 'tag--success'
    if (text === '4' || text === 'CANCELLED') return 'tag--danger'
    if (text === 'PENDING_PAYMENT') return 'tag--warning'
    if (text === 'PAID') return 'tag--info'
    if (text === 'SELLER_CONFIRMED') return 'tag--info'
    if (text === 'BUYER_CONFIRMED') return 'tag--info'
    if (text === 'COMPLETED') return 'tag--success'
    if (text === 'CANCELLED') return 'tag--danger'
    return 'tag--info'
  },

  getPayStatusText(status) {
    const text = String(status || '')
    if (text === '0' || text.toUpperCase() === 'UNPAID') return '未支付'
    if (text === '1' || text.toUpperCase() === 'PAID') return '已支付'
    if (text === '2' || text.toUpperCase() === 'REFUNDED') return '已退款'
    return text || '未知'
  },

  getPayStatusClass(status) {
    const text = String(status || '').toUpperCase()
    if (text === '0' || text === 'UNPAID') return 'tag--warning'
    if (text === '1' || text === 'PAID') return 'tag--success'
    if (text === '2' || text === 'REFUNDED') return 'tag--danger'
    return 'tag--info'
  },

  doAction(e) {
    // 移除stopPropagation，微信小程序中可能不需要或有兼容性问题
    
    const { id, action } = e.currentTarget.dataset
    const apiMap = {
      pay: `/api/user/orders/${id}/pay`,
      buyerConfirm: `/api/user/orders/${id}/buyer-confirm`,
      sellerConfirm: `/api/user/orders/${id}/seller-confirm`,
      cancel: `/api/user/orders/${id}/cancel`
    }
    
    wx.showLoading({ title: '处理中...' })
    request({ url: apiMap[action], method: 'POST' }).then(() => {
      wx.showToast({ title: '操作成功', icon: 'none' })
      this.setData({ pageNo: 1, hasMore: true, list: [] })
      this.loadOrders()
    }).finally(() => {
      wx.hideLoading()
    })
  }
})