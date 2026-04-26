Page({
  data: {
    list: [],
    isEditMode: false, // 是否处于编辑模式
    isAllSelected: false, // 是否全选
    selectedCount: 0 // 选中数量
  },

  onLoad() {
    this.loadFavorites()
  },

  onShow() {
    this.loadFavorites()
  },

  // 加载收藏列表
  loadFavorites() {
    const token = wx.getStorageSync('token')
    wx.request({
      url: 'http://localhost:8080/api/user/favorites',
      method: 'GET',
      header: { token },
      success: (res) => {
        if (res.data && (res.data.code === 0 || res.data.code === 200) && res.data.data) {
          const list = res.data.data.map(item => ({
            ...item,
            selected: false // 新增选中状态
          }))
          this.setData({ list, isEditMode: false, isAllSelected: false, selectedCount: 0 })
        }
      }
    })
  },

  // 切换编辑模式
  toggleEditMode() {
    this.setData({
      isEditMode: !this.data.isEditMode,
      isAllSelected: false,
      selectedCount: 0,
      list: this.data.list.map(item => ({ ...item, selected: false }))
    })
  },

  // 切换单条选中状态
  toggleSelect(e) {
    const id = e.currentTarget.dataset.id
    const list = this.data.list.map(item => {
      if (item.id === id) {
        return { ...item, selected: !item.selected }
      }
      return item
    })
    const selectedCount = list.filter(item => item.selected).length
    const isAllSelected = selectedCount === list.length && list.length > 0
    this.setData({ list, selectedCount, isAllSelected })
  },

  // 全选/取消全选
  toggleSelectAll() {
    const isAllSelected = !this.data.isAllSelected
    const list = this.data.list.map(item => ({ ...item, selected: isAllSelected }))
    const selectedCount = isAllSelected ? list.length : 0
    this.setData({ list, isAllSelected, selectedCount })
  },

  // 单条删除
  deleteSingle(e) {
    const id = e.currentTarget.dataset.id
    wx.showModal({
      title: '提示',
      content: '确定要取消收藏该商品吗？',
      success: (res) => {
        if (res.confirm) {
          const token = wx.getStorageSync('token')
          wx.request({
            url: `http://localhost:8080/api/user/favorites/${id}`,
            method: 'DELETE',
            header: { token },
            success: () => {
              wx.showToast({ title: '取消收藏成功' })
              this.loadFavorites()
            }
          })
        }
      }
    })
  },

  // 批量删除（修复版）
  batchDelete() {
    const selectedIds = this.data.list.filter(item => item.selected).map(item => item.id)
    if (selectedIds.length === 0) {
      wx.showToast({ title: '请选择要删除的商品', icon: 'none' })
      return
    }

    wx.showModal({
      title: '提示',
      content: `确定要取消收藏选中的${selectedIds.length}件商品吗？`,
      success: (res) => {
        if (res.confirm) {
          const token = wx.getStorageSync('token')
          wx.request({
            url: 'http://localhost:8080/api/user/favorites/batch',
            method: 'DELETE',
            header: {
              token: token,
              'Content-Type': 'application/json'
            },
            data: selectedIds,
            success: (res) => {
              if (res.data && res.data.code === 200) {
                wx.showToast({ title: '批量取消收藏成功' })
                this.setData({
                  isEditMode: false,
                  isAllSelected: false,
                  selectedCount: 0
                })
                this.loadFavorites()
              } else {
                wx.showToast({ title: res.data.message || '删除失败', icon: 'none' })
              }
            },
            fail: () => {
              wx.showToast({ title: '网络请求失败', icon: 'none' })
            }
          })
        }
      }
    })
  },

  // 返回上一页
  goBack() {
    wx.navigateBack()
  },

  // 跳转到商品详情
  goDetail(e) {
    const goodsId = e.currentTarget.dataset.id
    wx.navigateTo({
      url: `/pages/goods-detail/goods-detail?id=${goodsId}`
    })
  }
})