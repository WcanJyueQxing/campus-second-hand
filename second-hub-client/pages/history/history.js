const { request } = require('../../utils/request')

Page({
  data: {
    historyList: [],
    groupedHistory: []
  },

  onLoad() {
    this.loadHistory()
  },

  onShow() {
    this.loadHistory()
  },

  // 加载历史浏览记录
  loadHistory() {
    wx.showLoading({ title: '加载中...' })
    request({ url: '/api/user/history' }).then((data) => {
      const historyList = data || []
      // 按浏览时间倒序排序
      historyList.sort((a, b) => new Date(b.viewTime) - new Date(a.viewTime))
      // 按日期分组
      const groupedHistory = this.groupByDate(historyList)
      this.setData({ historyList, groupedHistory })
    }).finally(() => {
      wx.hideLoading()
    })
  },

  // 按日期分组
  groupByDate(historyList) {
    const groups = {}
    
    historyList.forEach(item => {
      const date = this.formatDate(item.viewTime)
      if (!groups[date]) {
        groups[date] = []
      }
      groups[date].push(item)
    })
    
    // 转换为数组并按日期倒序排序
    return Object.keys(groups).map(date => ({
      date,
      items: groups[date]
    })).sort((a, b) => {
      // 按日期倒序排序
      return new Date(b.date) - new Date(a.date)
    })
  },

  // 格式化日期
  formatDate(dateString) {
    const date = new Date(dateString)
    const year = date.getFullYear()
    const month = String(date.getMonth() + 1).padStart(2, '0')
    const day = String(date.getDate()).padStart(2, '0')
    return `${year}-${month}-${day}`
  },

  // 跳转到商品详情
  toGoodsDetail(e) {
    const goodsId = e.currentTarget.dataset.id
    wx.navigateTo({
      url: `/pages/goods-detail/goods-detail?id=${goodsId}`,
      success: (res) => {
        // 可以在这里添加浏览记录的更新逻辑
      }
    })
  },

  // 删除单条历史记录
  deleteHistory(e) {
    const { goodsId } = e.currentTarget.dataset
    wx.showLoading({ title: '删除中...' })
    request({ url: `/api/user/history/${goodsId}`, method: 'DELETE' }).then(() => {
      this.loadHistory()
      wx.showToast({ title: '删除成功', icon: 'success' })
    }).finally(() => {
      wx.hideLoading()
    })
  },

  // 清空历史记录
  clearHistory() {
    wx.showModal({
      title: '确认清空',
      content: '确定要清空所有历史浏览记录吗？',
      success: (res) => {
        if (res.confirm) {
          wx.showLoading({ title: '清空ing...' })
          request({ url: '/api/user/history', method: 'DELETE' }).then(() => {
            this.loadHistory()
            wx.showToast({ title: '清空成功', icon: 'success' })
          }).finally(() => {
            wx.hideLoading()
          })
        }
      }
    })
  },

  // 跳转到首页
  toHome() {
    wx.switchTab({ url: '/pages/home/home' })
  }
})