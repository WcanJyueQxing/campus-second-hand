const { request } = require('../../utils/request')

Page({
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

  selectType(e) {
    const id = e.currentTarget.dataset.id
    this.setData({ selectedType: id })
  },

  onContentInput(e) {
    this.setData({ content: e.detail.value })
  },

  onContactInput(e) {
    this.setData({ contact: e.detail.value })
  },

  submitFeedback() {
    const { content, selectedType, contact } = this.data

    if (!content) {
      wx.showToast({ title: '请输入反馈内容', icon: 'none' })
      return
    }

    wx.showLoading({ title: '提交中...' })
    request({
      url: '/api/feedback',
      method: 'POST',
      data: {
        type: selectedType,
        content: content,
        contact: contact
      }
    }).then(() => {
      wx.showToast({ title: '提交成功', icon: 'success' })
      setTimeout(() => {
        wx.navigateBack()
      }, 1000)
    }).catch(() => {
      wx.showToast({ title: '提交失败，请重试', icon: 'none' })
    }).finally(() => {
      wx.hideLoading()
    })
  }
})