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
    const token = wx.getStorageSync('token')

    wx.request({
      url: 'http://localhost:8080/api/user/favorites',
      method: 'GET',
      header: { token: token },
      success: (res) => {
        console.log('【收藏列表】接口返回:', res.data)

        // 修复：后端返回 code: 0
        if (res.data && (res.data.code === 0 || res.data.code === 200) && res.data.data) {
          const list = res.data.data
          console.log('【收藏列表】数据长度:', list.length)
          this.setData({
            list: list,
            loading: false
          })
        } else {
          console.log('【收藏列表】无数据或响应异常')
          this.setData({
            list: [],
            loading: false
          })
        }
      },
      fail: (err) => {
        console.error('加载收藏失败', err)
        this.setData({
          list: [],
          loading: false
        })
      }
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