/**
 * 订单页面
 * 展示订单列表，支持买家/卖家视角切换和订单操作
 */
const { request } = require('../../utils/request')

Page({
  /**
   * 页面数据
   * @property {string} asRole - 角色视角（buyer-买家，seller-卖家）
   * @property {Array} list - 订单列表
   * @property {number} pageNo - 当前页码
   * @property {number} pageSize - 每页大小
   * @property {boolean} hasMore - 是否有更多数据
   * @property {boolean} loading - 加载状态
   */
  data: {
    asRole: 'buyer',
    list: [],
    pageNo: 1,
    pageSize: 20,
    hasMore: true,
    loading: false
  },

  /**
   * 页面加载时触发
   * 解析跳转参数，确定显示哪种订单列表
   * @param {Object} options - URL参数
   */
  onLoad(options) {
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
        app.globalData.orderTabType = null
        return
      }
      app.globalData.orderTabType = null
    } else if (options.type) {
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

  /**
   * 页面显示时触发
   * 初始化TabBar状态，重新加载订单列表
   */
  onShow() {
    const tabBar = this.getTabBar && this.getTabBar()
    if (tabBar) {
      tabBar.setData({ selected: 2 })
    }
    this.setData({ pageNo: 1, hasMore: true, list: [] })
    this.loadOrders()
  },

  /**
   * 下拉刷新时触发
   * @param {Function} callback - 刷新完成回调
   */
  onPullDownRefresh() {
    this.setData({ pageNo: 1, hasMore: true, list: [] })
    this.loadOrders(() => {
      wx.stopPullDownRefresh()
    })
  },

  /**
   * 切换角色视角
   * @param {Object} e - 事件对象
   */
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

  /**
   * 加载订单列表
   * @param {Function} callback - 加载完成回调
   */
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

  /**
   * 加载待评价订单
   */
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

  /**
   * 加载更多订单
   */
  loadMore() {
    if (!this.data.hasMore || this.data.loading) return
    this.loadOrders()
  },

  /**
   * 跳转到订单详情页
   * @param {Object} e - 事件对象
   */
  toOrderDetail(e) {
    const orderId = e.currentTarget.dataset.id
    wx.navigateTo({ url: `/pages/order-detail/order-detail?id=${orderId}` })
  },

  /**
   * 格式化日期
   * @param {string} dateString - 日期字符串
   * @returns {string} 格式化后的日期
   */
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

  /**
   * 获取订单状态文本
   * @param {string} status - 状态值
   * @returns {string} 状态文本
   */
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

  /**
   * 获取订单状态样式类
   * @param {string} status - 状态值
   * @returns {string} 样式类名
   */
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

  /**
   * 获取支付状态文本
   * @param {string} status - 状态值
   * @returns {string} 状态文本
   */
  getPayStatusText(status) {
    const text = String(status || '')
    if (text === '0' || text.toUpperCase() === 'UNPAID') return '未支付'
    if (text === '1' || text.toUpperCase() === 'PAID') return '已支付'
    if (text === '2' || text.toUpperCase() === 'REFUNDED') return '已退款'
    return text || '未知'
  },

  /**
   * 获取支付状态样式类
   * @param {string} status - 状态值
   * @returns {string} 样式类名
   */
  getPayStatusClass(status) {
    const text = String(status || '').toUpperCase()
    if (text === '0' || text === 'UNPAID') return 'tag--warning'
    if (text === '1' || text === 'PAID') return 'tag--success'
    if (text === '2' || text === 'REFUNDED') return 'tag--danger'
    return 'tag--info'
  },

  /**
   * 执行订单操作
   * @param {Object} e - 事件对象
   */
  doAction(e) {
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
  },

  /**
   * 返回首页
   */
  navigateBack() {
    wx.switchTab({ url: '/pages/home/home' })
  }
})