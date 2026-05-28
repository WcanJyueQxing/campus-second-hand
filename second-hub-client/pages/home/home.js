/**
 * 首页
 * 展示商品列表、分类导航和搜索功能
 */
const { request } = require('../../utils/request')

Page({
  /**
   * 页面数据
   * @property {string} keyword - 搜索关键词
   * @property {Array} categories - 分类列表
   * @property {Array} goodsList - 商品列表
   * @property {number} pageNo - 当前页码
   * @property {number} pageSize - 每页大小
   * @property {boolean} loading - 加载状态
   * @property {boolean} hasMore - 是否有更多数据
   */
  data: {
    keyword: '',
    categories: [],
    goodsList: [],
    pageNo: 1,
    pageSize: 10,
    loading: false,
    hasMore: true
  },

  /**
   * 页面显示时触发
   * 初始化TabBar状态，加载分类和商品列表
   */
  onShow() {
    const tabBar = this.getTabBar && this.getTabBar()
    if (tabBar) {
      tabBar.setData({ selected: 0 })
    }
    this.loadCategories()
    this.resetAndLoad()
  },

  /**
   * 下拉刷新时触发
   */
  onPullDownRefresh() {
    this.resetAndLoad().finally(() => wx.stopPullDownRefresh())
  },

  /**
   * 上拉触底时触发
   */
  onReachBottom() {
    if (!this.data.loading && this.data.hasMore) {
      this.loadGoods()
    }
  },

  /**
   * 加载分类列表
   */
  loadCategories() {
    request({ url: '/api/user/public/categories' }).then((data) => {
      this.setData({ categories: data || [] })
    })
  },

  /**
   * 重置分页并加载第一页数据
   * @returns {Promise}
   */
  resetAndLoad() {
    this.setData({ goodsList: [], pageNo: 1, hasMore: true })
    return this.loadGoods()
  },

  /**
   * 加载商品列表
   */
  loadGoods() {
    this.setData({ loading: true })
    return request({
      url: '/api/user/goods/list',
      data: {
        keyword: this.data.keyword,
        pageNo: this.data.pageNo,
        pageSize: this.data.pageSize,
        sortBy: 'newest'
      }
    }).then((page) => {
      const list = [...this.data.goodsList, ...(page.records || [])]
      const hasMore = list.length < (page.total || 0)
      this.setData({
        goodsList: list,
        pageNo: this.data.pageNo + 1,
        hasMore
      })
    }).finally(() => {
      this.setData({ loading: false })
    })
  },

  /**
   * 搜索关键词输入事件
   * @param {Object} e - 事件对象
   */
  onKeywordInput(e) {
    this.setData({ keyword: e.detail.value })
  },

  /**
   * 执行搜索
   */
  onSearch() {
    this.resetAndLoad()
  },

  /**
   * 跳转到商品详情页
   * @param {Object} e - 事件对象
   */
  toDetail(e) {
    const { id } = e.currentTarget.dataset
    wx.navigateTo({ url: `/pages/goods-detail/goods-detail?id=${id}` })
  }
})