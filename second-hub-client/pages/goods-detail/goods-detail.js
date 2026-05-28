/**
 * 商品详情页
 * 展示商品详情、评论列表，提供收藏、评论、下单等功能
 */
const { request } = require('../../utils/request')

// 编辑商品ID的本地存储键名
const EDIT_GOODS_ID_KEY = 'goods_publish_edit_goods_id'

Page({
  /**
   * 页面数据
   * @property {number} id - 商品ID
   * @property {Object} detail - 商品详情
   * @property {string} detailStatusText - 状态文本
   * @property {string} detailStatusClass - 状态样式类
   * @property {Array} comments - 评论列表
   * @property {string} commentText - 评论输入内容
   * @property {boolean} isOwner - 是否为商品所有者
   * @property {boolean} favoriting - 收藏操作中
   */
  data: {
    id: null,
    detail: {},
    detailStatusText: '',
    detailStatusClass: 'tag--info',
    comments: [],
    commentText: '',
    isOwner: false,
    favoriting: false
  },

  /**
   * 页面加载时触发
   * @param {Object} options - URL参数
   */
  onLoad(options) {
    this.setData({ id: Number(options.id) })
    this.loadDetail()
    this.loadComments()
  },

  /**
   * 加载商品详情
   */
  loadDetail() {
    request({ url: `/api/user/goods/${this.data.id}` }).then((data) => {
      const detail = data || {}
      const userInfo = wx.getStorageSync('userInfo') || {}
      const currentUserId = Number(userInfo.id || 0)
      this.setData({
        detail,
        detailStatusText: this.getStatusText(detail.status),
        detailStatusClass: this.getStatusClass(detail.status),
        isOwner: currentUserId > 0 && Number(detail.userId || 0) === currentUserId
      })
      this.addToHistory(detail)
    })
  },

  /**
   * 添加浏览记录
   * @param {Object} goods - 商品信息
   */
  addToHistory(goods) {
    if (!goods || !goods.id) return

    const historyData = {
      goodsId: goods.id,
      title: goods.title,
      price: goods.price,
      images: goods.images || []
    }
    request({ url: '/api/user/history', method: 'POST', data: historyData })
      .then(() => { console.log('历史记录添加成功') })
      .catch(() => { console.log('历史记录添加失败') })
  },

  /**
   * 获取状态文本
   * @param {string} status - 状态值
   * @returns {string} 状态文本
   */
  getStatusText(status) {
    const text = String(status || '').toUpperCase()
    if (text === 'APPROVED' || text === 'ON_SALE' || text === '1') return '在售中'
    if (text === 'PENDING' || text === '2') return '审核中'
    if (text === 'OFFLINE' || text === '3') return '已下架'
    if (text === 'REJECTED') return '已驳回'
    return '处理中'
  },

  /**
   * 获取状态样式类
   * @param {string} status - 状态值
   * @returns {string} 样式类名
   */
  getStatusClass(status) {
    const text = String(status || '').toUpperCase()
    if (text === 'APPROVED' || text === 'ON_SALE' || text === '1') return 'tag--success'
    if (text === 'PENDING' || text === '2') return 'tag--warning'
    if (text === 'OFFLINE' || text === '3' || text === 'REJECTED') return 'tag--danger'
    return 'tag--info'
  },

  /**
   * 加载评论列表
   */
  loadComments() {
    request({
      url: `/api/user/comments/${this.data.id}`,
      data: { pageNo: 1, pageSize: 20 }
    }).then((data) => {
      this.setData({ comments: data.records || [] })
    })
  },

  /**
   * 跳转到编辑页面
   */
  toEdit() {
    if (!this.data.isOwner) {
      return
    }
    wx.setStorageSync(EDIT_GOODS_ID_KEY, this.data.id)
    wx.switchTab({ url: '/pages/goods-publish/goods-publish' })
  },

  /**
   * 切换收藏状态
   */
  toggleFavorite() {
    const token = wx.getStorageSync('token')
    if (!token) {
      wx.showToast({ title: '请先登录', icon: 'none' })
      return
    }
    if (this.data.favoriting) return
    this.setData({ favoriting: true })

    request({
      url: `/api/user/favorites/${this.data.id}`,
      method: 'POST'
    }).then(() => {
      wx.showToast({ title: '操作成功' })
      this.loadDetail()
    }).catch((err) => {
      wx.showToast({ title: err?.message || '操作失败', icon: 'none' })
    }).finally(() => {
      this.setData({ favoriting: false })
    })
  },

  /**
   * 评论输入事件
   * @param {Object} e - 事件对象
   */
  onCommentInput(e) {
    this.setData({ commentText: e.detail.value })
  },

  /**
   * 提交评论
   */
  submitComment() {
    if (!this.data.commentText.trim()) {
      return
    }
    request({
      url: '/api/user/comments',
      method: 'POST',
      data: {
        goodsId: this.data.id,
        content: this.data.commentText
      }
    }).then(() => {
      this.setData({ commentText: '' })
      this.loadComments()
      this.loadDetail()
    })
  },

  /**
   * 创建订单
   */
  createOrder() {
    request({
      url: '/api/user/orders',
      method: 'POST',
      data: {
        goodsId: this.data.id,
        amount: this.data.detail.price,
        note: ''
      }
    }).then(() => {
      wx.showToast({ title: '下单成功', icon: 'none' })
      setTimeout(() => {
        wx.switchTab({ url: '/pages/orders/orders' })
      }, 600)
    })
  }
})