const { request } = require('../../utils/request')

Page({
  data: {
    list: [],
    loading: false
  },

  onLoad() {
    this.loadFavorites()
  },

  onShow() {
    this.loadFavorites()
  },

  loadFavorites() {
    this.setData({ loading: true })

    request({
      url: '/api/user/favorites',
      method: 'GET'
    }).then((data) => {
      console.log('【收藏列表】接口返回:', data)
      this.setData({
        list: data || [],
        loading: false
      })
    }).catch((err) => {
      console.error('加载收藏失败', err)
      this.setData({
        list: [],
        loading: false
      })
    })
  },

  goDetail(e) {
    const goodsId = e.currentTarget.dataset.id
    wx.navigateTo({
      url: `/pages/goods-detail/goods-detail?id=${goodsId}`
    })
  },

  goBack() {
    wx.navigateBack()
  }
})