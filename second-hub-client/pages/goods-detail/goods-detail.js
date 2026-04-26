const { request } = require('../../utils/request')

const EDIT_GOODS_ID_KEY = 'goods_publish_edit_goods_id'

Page({
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

  onLoad(options) {
    this.setData({ id: Number(options.id) })
    this.loadDetail()
    this.loadComments()
  },

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

  addToHistory(goods) {
    if (!goods || !goods.id) return

    const historyData = {
      goodsId: goods.id,
      title: goods.title,
      price: goods.price,
      images: goods.images || []
    }
    request({ url: '/api/user/history', method: 'POST', data: historyData }).then(() => {
      console.log('历史记录添加成功')
    }).catch(() => {
      console.log('历史记录添加失败')
    })
  },

  getStatusText(status) {
    const text = String(status || '').toUpperCase()
    if (text === 'APPROVED' || text === 'ON_SALE' || text === '1') return '在售中'
    if (text === 'PENDING' || text === '2') return '审核中'
    if (text === 'OFFLINE' || text === '3') return '已下架'
    if (text === 'REJECTED') return '已驳回'
    return '处理中'
  },

  getStatusClass(status) {
    const text = String(status || '').toUpperCase()
    if (text === 'APPROVED' || text === 'ON_SALE' || text === '1') return 'tag--success'
    if (text === 'PENDING' || text === '2') return 'tag--warning'
    if (text === 'OFFLINE' || text === '3' || text === 'REJECTED') return 'tag--danger'
    return 'tag--info'
  },

  loadComments() {
    request({
      url: `/api/user/comments/${this.data.id}`,
      data: { pageNo: 1, pageSize: 20 }
    }).then((data) => {
      this.setData({ comments: data.records || [] })
    })
  },

  toEdit() {
    if (!this.data.isOwner) {
      return
    }
    wx.setStorageSync(EDIT_GOODS_ID_KEY, this.data.id)
    wx.switchTab({ url: '/pages/goods-publish/goods-publish' })
  },

  toggleFavorite() {
    const token = wx.getStorageSync('token')
    if (!token) {
      wx.showToast({ title: '请先登录', icon: 'none' })
      return
    }

    if (this.data.favoriting) return
    this.setData({ favoriting: true })

    wx.request({
      url: 'http://localhost:8080/api/user/favorites/' + this.data.id,
      method: 'POST',
      header: { token: token },
      success: (res) => {
        console.log('【收藏操作】响应：', res.data)
        if (res.data && (res.data.code === 0 || res.data.code === 200)) {
          wx.showToast({ title: res.data.message || '操作成功' })
          setTimeout(() => {
            this.loadDetail()
          }, 500)
        } else {
          wx.showToast({ title: res.data.message || '操作失败', icon: 'none' })
        }
      },
      fail: (err) => {
        console.error('【收藏操作】失败：', err)
        wx.showToast({ title: '网络异常', icon: 'none' })
      },
      complete: () => {
        this.setData({ favoriting: false })
      }
    })
  },

  onCommentInput(e) {
    this.setData({ commentText: e.detail.value })
  },

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