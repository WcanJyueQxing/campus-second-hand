const { request } = require('../../utils/request')

Page({
  data: {
    orderId: null,
    orderDetail: {},
    orderStatusText: '',
    orderStatusClass: '',
    payStatusText: '',
    formattedCreateTime: '',
    statusDesc: '',
    isBuyer: false,
    isSeller: false,
    showActions: true
  },

  onLoad(options) {
    if (options.id) {
      this.setData({ orderId: options.id })
      this.loadOrderDetail()
    }
  },

  loadOrderDetail() {
    if (!this.data.orderId) return

    wx.showLoading({ title: '加载中...' })
    request({
      url: `/api/user/orders/${this.data.orderId}`
    }).then((data) => {
      const orderDetail = data || {}
      const userInfo = wx.getStorageSync('userInfo') || {}
      const currentUserId = userInfo.id
      
      this.setData({
        orderDetail,
        orderStatusText: this.getOrderStatusText(orderDetail.orderStatus),
        orderStatusClass: this.getOrderStatusClass(orderDetail.orderStatus),
        payStatusText: this.getPayStatusText(orderDetail.payStatus),
        formattedCreateTime: this.formatDate(orderDetail.createdAt),
        statusDesc: this.getStatusDesc(orderDetail.orderStatus),
        isBuyer: currentUserId === orderDetail.buyerId,
        isSeller: currentUserId === orderDetail.sellerId,
        showActions: this.shouldShowActions(orderDetail.orderStatus, currentUserId, orderDetail.buyerId, orderDetail.sellerId)
      })
    }).finally(() => {
      wx.hideLoading()
    })
  },

  navigateBack() {
    wx.navigateBack({ delta: 1 })
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

  getStatusDesc(status) {
    const text = String(status || '').toUpperCase()
    if (text === 'PENDING_PAYMENT') return '请尽快完成支付'
    if (text === 'PAID') return '卖家正在处理订单'
    if (text === 'SELLER_CONFIRMED') return '卖家已确认，等待买家确认'
    if (text === 'BUYER_CONFIRMED') return '买家已确认，等待卖家确认'
    if (text === 'COMPLETED') return '交易已完成'
    if (text === 'CANCELLED') return '订单已取消'
    return ''
  },

  shouldShowActions(orderStatus, currentUserId, buyerId, sellerId) {
    if (!currentUserId) return false
    
    const status = String(orderStatus || '').toUpperCase()
    const isBuyer = currentUserId === buyerId
    const isSeller = currentUserId === sellerId
    
    if (status === 'COMPLETED' || status === 'CANCELLED') return false
    
    return isBuyer || isSeller
  },

  payOrder() {
    wx.showLoading({ title: '处理中...' })
    request({
      url: `/api/user/orders/${this.data.orderId}/pay`,
      method: 'POST'
    }).then(() => {
      wx.showToast({ title: '支付成功', icon: 'success' })
      setTimeout(() => {
        this.loadOrderDetail()
      }, 1000)
    }).finally(() => {
      wx.hideLoading()
    })
  },

  cancelOrder() {
    wx.showModal({
      title: '确认取消',
      content: '确定要取消该订单吗？',
      success: (res) => {
        if (res.confirm) {
          wx.showLoading({ title: '处理中...' })
          request({
            url: `/api/user/orders/${this.data.orderId}/cancel`,
            method: 'POST'
          }).then(() => {
            wx.showToast({ title: '订单已取消', icon: 'success' })
            setTimeout(() => {
              this.loadOrderDetail()
            }, 1000)
          }).finally(() => {
            wx.hideLoading()
          })
        }
      }
    })
  },

  confirmShipment() {
    wx.showModal({
      title: '确认发货',
      content: '确定要确认发货吗？',
      success: (res) => {
        if (res.confirm) {
          wx.showLoading({ title: '处理中...' })
          request({
            url: `/api/user/orders/${this.data.orderId}/seller-confirm`,
            method: 'POST'
          }).then(() => {
            wx.showToast({ title: '已确认发货', icon: 'success' })
            setTimeout(() => {
              this.loadOrderDetail()
            }, 1000)
          }).finally(() => {
            wx.hideLoading()
          })
        }
      }
    })
  },

  confirmReceipt() {
    wx.showModal({
      title: '确认收货',
      content: '确定已收到商品吗？',
      success: (res) => {
        if (res.confirm) {
          wx.showLoading({ title: '处理中...' })
          request({
            url: `/api/user/orders/${this.data.orderId}/buyer-confirm`,
            method: 'POST'
          }).then(() => {
            wx.showToast({ title: '已确认收货', icon: 'success' })
            setTimeout(() => {
              this.loadOrderDetail()
            }, 1000)
          }).finally(() => {
            wx.hideLoading()
          })
        }
      }
    })
  },

  confirmCompletion() {
    wx.showModal({
      title: '确认完成',
      content: '确定要完成该订单吗？',
      success: (res) => {
        if (res.confirm) {
          wx.showLoading({ title: '处理中...' })
          const apiUrl = this.data.isBuyer 
            ? `/api/user/orders/${this.data.orderId}/buyer-confirm` 
            : `/api/user/orders/${this.data.orderId}/seller-confirm`
          
          request({
            url: apiUrl,
            method: 'POST'
          }).then(() => {
            wx.showToast({ title: '订单已完成', icon: 'success' })
            setTimeout(() => {
              this.loadOrderDetail()
            }, 1000)
          }).finally(() => {
            wx.hideLoading()
          })
        }
      }
    })
  }
})