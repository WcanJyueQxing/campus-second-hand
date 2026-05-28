/**
 * 收藏页面
 * 展示用户收藏的商品列表
 */
const { request } = require('../../utils/request')

Page({
  /**
   * 页面数据
   * @property {Array} list - 收藏商品列表
   * @property {boolean} loading - 加载状态
   */
  data: {
    list: [],
    loading: false
  },

  /**
   * 页面加载时触发
   */
  onLoad() {
    this.loadFavorites()
  },

  /**
   * 页面显示时触发
   */
  onShow() {
    this.loadFavorites()
  },

  /**
   * 加载收藏列表
   */
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

  /**
   * 跳转到商品详情页
   * @param {Object} e - 事件对象
   */
  goDetail(e) {
    const goodsId = e.currentTarget.dataset.id
    wx.navigateTo({
      url: `/pages/goods-detail/goods-detail?id=${goodsId}`
    })
  },

  /**
   * 返回上一页
   */
  goBack() {
    wx.navigateBack()
  }
})