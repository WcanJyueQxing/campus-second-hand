Page({ 
  data: { 
    list: [], 
    isEditMode: false, 
    isAllSelected: false, 
    selectedCount: 0 
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
        if (res.data?.code === 200 && res.data.data) { 
          this.setData({ 
            list: res.data.data.map(item => ({ ...item, selected: false })), 
            isEditMode: false, 
            isAllSelected: false, 
            selectedCount: 0 
          }) 
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
      list: this.data.list.map(i => ({ ...i, selected: false })) 
    }) 
  }, 

  // 选中单个 
  toggleSelect(e) { 
    const id = e.currentTarget.dataset.id 
    const list = this.data.list.map(item => 
      item.id === id ? { ...item, selected: !item.selected } : item 
    ) 
    const selectedCount = list.filter(i => i.selected).length 
    const isAllSelected = selectedCount === list.length 
    this.setData({ list, selectedCount, isAllSelected }) 
  }, 

  // 全选 
  toggleSelectAll() { 
    const isAll = !this.data.isAllSelected 
    this.setData({ 
      isAllSelected: isAll, 
      selectedCount: isAll ? this.data.list.length : 0, 
      list: this.data.list.map(item => ({ ...item, selected: isAll })) 
    }) 
  }, 

  // --------------------- 
  // ✅ 【关键】真正可用的批量删除 
  // --------------------- 
  batchDelete() { 
    const selected = this.data.list.filter(item => item.selected) 
    if (selected.length === 0) { 
      wx.showToast({ title: '请选择商品', icon: 'none' }) 
      return 
    } 

    wx.showModal({ 
      title: '确认删除', 
      content: `确定删除 ${selected.length} 个收藏？`, 
      success: (res) => { 
        if (!res.confirm) return 

        let success = 0 
        selected.forEach(item => { 
          this._deleteOne(item.id, () => { 
            success++ 
            if (success === selected.length) { 
              wx.showToast({ title: '批量删除成功' }) 
              this.loadFavorites() 
            } 
          }) 
        }) 
      } 
    }) 
  }, 

  // 单条删除（内部用） 
  _deleteOne(goodsId, callback) { 
    const token = wx.getStorageSync('token') 
    wx.request({ 
      url: `http://localhost:8080/api/user/favorites/${goodsId}`, 
      method: 'DELETE', 
      header: { token }, 
      success: callback 
    }) 
  }, 

  // 单个删除按钮 
  deleteSingle(e) { 
    const id = e.currentTarget.dataset.id 
    wx.showModal({ 
      title: '确认删除', 
      success: (res) => { 
        if (res.confirm) { 
          this._deleteOne(id, () => { 
            wx.showToast({ title: '删除成功' }) 
            this.loadFavorites() 
          }) 
        } 
      } 
    }) 
  }, 

  goBack() { 
    wx.navigateBack() 
  }, 

  goDetail(e) { 
    wx.navigateTo({ 
      url: '/pages/goods-detail/goods-detail?id=' + e.currentTarget.dataset.id 
    }) 
  } 
 })