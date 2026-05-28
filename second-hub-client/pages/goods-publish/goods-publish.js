/**
 * 商品发布页
 * 提供商品发布和编辑功能，支持图片上传、表单验证
 */
const { request, uploadFile } = require('../../utils/request')

const MAX_IMAGES = 6                    // 最大图片数量
const MAX_FILE_SIZE_MB = 5              // 单文件最大大小(MB)
const MAX_FILE_SIZE_BYTES = MAX_FILE_SIZE_MB * 1024 * 1024  // 单文件最大大小(字节)
const EDIT_GOODS_ID_KEY = 'goods_publish_edit_goods_id'     // 编辑商品ID存储键名

Page({
  /**
   * 页面数据
   * @property {string} mode - 模式（create-新建，edit-编辑）
   * @property {number} editingGoodsId - 正在编辑的商品ID
   * @property {string} pageTitle - 页面标题
   * @property {string} submitText - 提交按钮文本
   * @property {Object} form - 表单数据
   * @property {Array} categories - 分类列表
   * @property {number} categoryIndex - 当前选中的分类索引
   * @property {boolean} submitting - 提交中
   * @property {string} maxFileSizeText - 文件大小限制文本
   * @property {number} maxImages - 最大图片数量
   * @property {string} quantityError - 数量错误提示
   */
  data: {
    mode: 'create',
    editingGoodsId: null,
    pageTitle: '发布闲置',
    submitText: '提交发布',
    form: {
      categoryId: null,
      title: '',
      description: '',
      price: '',
      commentCount: '',
      coverImage: '',
      images: []
    },
    categories: [],
    categoryIndex: 0,
    submitting: false,
    maxFileSizeText: `${MAX_FILE_SIZE_MB}MB`,
    maxImages: MAX_IMAGES,
    quantityError: ''
  },

  /**
   * 页面显示时触发
   * 初始化TabBar状态，检查登录，加载分类，尝试进入编辑模式
   */
  onShow() {
    const tabBar = this.getTabBar && this.getTabBar()
    if (tabBar) {
      tabBar.setData({ selected: 1 })
    }

    const token = wx.getStorageSync('token')
    if (!token) {
      wx.showToast({ title: '请先登录', icon: 'none' })
      setTimeout(() => {
        wx.navigateTo({ url: '/pages/login/login' })
      }, 1000)
      return
    }

    const pendingId = Number(wx.getStorageSync(EDIT_GOODS_ID_KEY) || 0)
    if (!pendingId && this.data.mode === 'edit') {
      this.resetToCreateMode()
    }

    this.loadCategories().then(() => {
      this.tryEnterEditMode()
    })
  },

  /**
   * 加载分类列表
   * @returns {Promise}
   */
  loadCategories() {
    return request({ url: '/api/user/public/categories' }).then((data) => {
      const categories = data || []
      const currentCategoryId = this.data.form.categoryId
      let categoryIndex = 0
      if (currentCategoryId) {
        const matched = categories.findIndex((item) => item.id === currentCategoryId)
        categoryIndex = matched >= 0 ? matched : 0
      }
      const categoryId = categories[categoryIndex] ? categories[categoryIndex].id : null
      this.setData({
        categories,
        categoryIndex,
        'form.categoryId': currentCategoryId || categoryId
      })
    })
  },

  /**
   * 尝试进入编辑模式
   */
  tryEnterEditMode() {
    const pendingId = Number(wx.getStorageSync(EDIT_GOODS_ID_KEY) || 0)
    if (!pendingId) {
      return
    }

    wx.removeStorageSync(EDIT_GOODS_ID_KEY)
    request({ url: `/api/user/goods/${pendingId}` }).then((detail) => {
      const images = this.normalizeImages(detail)
      const categories = this.data.categories || []
      const categoryIndex = categories.findIndex((item) => item.id === detail.categoryId)
      this.setData({
        mode: 'edit',
        editingGoodsId: pendingId,
        pageTitle: '编辑商品',
        submitText: '保存修改',
        categoryIndex: categoryIndex >= 0 ? categoryIndex : 0,
        form: {
          categoryId: detail.categoryId || (categories[0] ? categories[0].id : null),
          title: detail.title || '',
          description: detail.description || '',
          price: detail.price === undefined || detail.price === null ? '' : String(detail.price),
          commentCount: detail.commentCount === undefined || detail.commentCount === null ? '1' : String(detail.commentCount),
          coverImage: detail.coverImage || images[0] || '',
          images
        }
      })
    })
  },

  /**
   * 标准化图片数组
   * @param {Object} detail - 商品详情
   * @returns {Array} 图片URL数组
   */
  normalizeImages(detail) {
    const list = Array.isArray(detail && detail.images) ? detail.images : []
    const urls = list.filter((item) => typeof item === 'string' && item.trim()).map((item) => item.trim())
    if (urls.length > 0) {
      return this.uniqueUrls(urls).slice(0, MAX_IMAGES)
    }
    if (detail && detail.coverImage) {
      return [detail.coverImage]
    }
    return []
  },

  /**
   * 表单输入事件
   * @param {Object} e - 事件对象
   */
  onInput(e) {
    const { field } = e.currentTarget.dataset
    this.setData({ [`form.${field}`]: e.detail.value })
    if (field === 'commentCount') {
      this.setData({ quantityError: '' })
    }
  },

  /**
   * 数量输入框失去焦点事件
   * 验证数量是否为大于0的整数
   * @param {Object} e - 事件对象
   */
  onQuantityBlur(e) {
    const value = e.detail.value
    const numValue = parseInt(value, 10)
    if (!value || isNaN(numValue) || numValue <= 0 || numValue !== parseFloat(value)) {
      this.setData({
        'form.commentCount': '',
        quantityError: '请输入大于0的整数'
      })
    } else {
      this.setData({ quantityError: '' })
    }
  },

  /**
   * 分类选择事件
   * @param {Object} e - 事件对象
   */
  onCategoryChange(e) {
    const index = Number(e.detail.value)
    const category = this.data.categories[index]
    if (category) {
      this.setData({
        'form.categoryId': category.id,
        categoryIndex: index
      })
    }
  },

  /**
   * 选择图片
   */
  chooseImage() {
    const existing = this.data.form.images || []
    const remaining = MAX_IMAGES - existing.length
    if (remaining <= 0) {
      wx.showToast({ title: `最多上传 ${MAX_IMAGES} 张`, icon: 'none' })
      return
    }

    wx.chooseImage({
      count: remaining,
      success: async (res) => {
        const tempFiles = res.tempFiles || []
        const oversize = tempFiles.find((file) => Number(file.size || 0) > MAX_FILE_SIZE_BYTES)
        if (oversize) {
          wx.showToast({ title: `单个文件不能超过${MAX_FILE_SIZE_MB}MB`, icon: 'none' })
          return
        }

        if (existing.length + tempFiles.length > MAX_IMAGES) {
          wx.showToast({ title: `最多上传 ${MAX_IMAGES} 张`, icon: 'none' })
          return
        }

        wx.showLoading({ title: '上传中...' })
        try {
          const uploaded = []
          for (const path of res.tempFilePaths || []) {
            const url = await uploadFile(path)
            uploaded.push(url)
          }
          const merged = this.uniqueUrls([...(this.data.form.images || []), ...uploaded]).slice(0, MAX_IMAGES)
          this.setData({
            'form.images': merged,
            'form.coverImage': merged[0] || ''
          })
        } catch (e) {
          // uploadFile已处理错误提示，此处仅捕获避免未处理异常
        } finally {
          wx.hideLoading()
        }
      }
    })
  },

  /**
   * 删除图片
   * @param {Object} e - 事件对象
   */
  removeImage(e) {
    const index = Number(e.currentTarget.dataset.index)
    const current = this.data.form.images || []
    if (index < 0 || index >= current.length) {
      return
    }
    const next = current.filter((_, i) => i !== index)
    this.setData({
      'form.images': next,
      'form.coverImage': next[0] || ''
    })
  },

  /**
   * 去重URL列表
   * @param {Array} list - URL列表
   * @returns {Array} 去重后的列表
   */
  uniqueUrls(list) {
    const result = []
    const seen = new Set()
    for (const item of list || []) {
      if (typeof item !== 'string') {
        continue
      }
      const value = item.trim()
      if (!value || seen.has(value)) {
        continue
      }
      seen.add(value)
      result.push(value)
    }
    return result
  },

  /**
   * 提交表单
   */
  submit() {
    if (this.data.submitting) {
      return
    }

    const f = this.data.form
    if (!f.title || !f.description || !f.price || !f.coverImage || !f.categoryId) {
      wx.showToast({ title: '请完善商品信息', icon: 'none' })
      return
    }

    const payload = {
      categoryId: f.categoryId,
      title: f.title,
      description: f.description,
      price: Number(f.price),
      commentCount: Number(f.commentCount) || 1,
      coverImage: f.coverImage,
      images: f.images
    }

    const isEdit = this.data.mode === 'edit' && this.data.editingGoodsId
    const submitReq = request({
      url: isEdit ? `/api/user/goods/${this.data.editingGoodsId}` : '/api/user/goods',
      method: isEdit ? 'PUT' : 'POST',
      data: payload
    })

    this.setData({ submitting: true })
    submitReq.then(() => {
      wx.showToast({ title: isEdit ? '保存成功' : '发布成功，待审核', icon: 'none' })
      this.resetToCreateMode()
    }).finally(() => {
      this.setData({ submitting: false })
    })
  },

  /**
   * 重置为创建模式
   */
  resetToCreateMode() {
    const categoryId = this.data.categories[0] ? this.data.categories[0].id : null
    wx.removeStorageSync(EDIT_GOODS_ID_KEY)
    this.setData({
      mode: 'create',
      editingGoodsId: null,
      pageTitle: '发布闲置',
      submitText: '提交发布',
      form: {
        categoryId,
        title: '',
        description: '',
        price: '',
        commentCount: '',
        coverImage: '',
        images: []
      },
      categoryIndex: 0
    })
  }
})