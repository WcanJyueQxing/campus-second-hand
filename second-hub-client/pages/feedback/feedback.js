/**
 * 意见反馈页面
 * 提供用户提交意见反馈的功能
 */
const { request } = require('../../utils/request')

Page({
  /**
   * 页面数据
   * @property {Array} types - 反馈类型列表
   * @property {number} selectedType - 选中的反馈类型
   * @property {string} content - 反馈内容
   * @property {string} contact - 联系方式（自动填充用户手机号）
   */
  data: {
    types: [
      { id: 1, name: '功能建议' },
      { id: 2, name: 'Bug反馈' },
      { id: 3, name: '商品问题' },
      { id: 4, name: '交易纠纷' },
      { id: 5, name: '其他' }
    ],
    selectedType: 1,
    content: '',
    contact: ''
  },

  /**
   * 页面加载时触发
   * 自动填充登录用户的手机号作为联系方式
   */
  onLoad() {
    const userInfo = wx.getStorageSync('userInfo') || {}
    if (userInfo.phone) {
      this.setData({ contact: userInfo.phone })
    }
  },

  /**
   * 选择反馈类型
   * @param {Object} e - 事件对象
   */
  selectType(e) {
    const id = e.currentTarget.dataset.id
    this.setData({ selectedType: id })
  },

  /**
   * 反馈内容输入事件
   * @param {Object} e - 事件对象
   */
  onContentInput(e) {
    this.setData({ content: e.detail.value })
  },

  /**
   * 联系方式输入事件
   * @param {Object} e - 事件对象
   */
  onContactInput(e) {
    this.setData({ contact: e.detail.value })
  },

  /**
   * 提交反馈
   */
  async submitFeedback() {
    const { content, selectedType, contact } = this.data

    if (!content) {
      wx.showToast({ title: '请输入反馈内容', icon: 'none' })
      return
    }

    wx.showLoading({ title: '提交中...' })

    try {
      await request({
        url: '/api/feedback',
        method: 'POST',
        data: {
          type: selectedType,
          content: content,
          contact: contact
        }
      })

      wx.hideLoading()
      wx.showToast({ title: '提交成功', icon: 'success' })
      setTimeout(() => {
        wx.navigateBack()
      }, 1000)
    } catch (err) {
      wx.hideLoading()
      console.error('反馈提交失败', err)
      wx.showToast({ title: '提交失败，请重试', icon: 'none' })
    }
  }
})