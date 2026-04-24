const { request } = require('../../utils/request')

Page({
  data: {
    phone: '',
    captchaCode: '',
    captchaUuid: '',
    captchaBase64: ''
  },

  onLoad() {
    this.refreshCaptcha()
  },

  bindInput(e) {
    const field = e.currentTarget.dataset.field
    this.setData({
      [field]: e.detail.value
    })
  },

  refreshCaptcha() {
    wx.showLoading({ title: '加载中...' })
    request({
      url: '/api/user/captcha/generate',
      method: 'GET'
    }).then((data) => {
      this.setData({
        captchaUuid: data.uuid,
        captchaBase64: data.captcha
      })
    }).catch(() => {
      wx.showToast({ title: '验证码加载失败', icon: 'none' })
    }).finally(() => {
      wx.hideLoading()
    })
  },

  submitForm() {
    const { phone, captchaCode, captchaUuid } = this.data

    if (!phone) {
      wx.showToast({ title: '请输入手机号', icon: 'none' })
      return
    }

    const phoneRegex = /^1[3-9]\d{9}$/
    if (!phoneRegex.test(phone)) {
      wx.showToast({ title: '请输入正确的手机号', icon: 'none' })
      return
    }

    if (!captchaCode) {
      wx.showToast({ title: '请输入图形验证码', icon: 'none' })
      return
    }

    wx.showLoading({ title: '绑定中...' })
    request({
      url: '/api/user/phone',
      method: 'PUT',
      data: {
        phone: phone,
        captchaCode: captchaCode,
        captchaUuid: captchaUuid
      }
    }).then(() => {
      wx.showToast({ title: '手机号绑定成功', icon: 'success' })
      setTimeout(() => {
        wx.navigateBack()
      }, 1500)
    }).catch((err) => {
      this.refreshCaptcha()
      this.setData({ captchaCode: '' })
      wx.showToast({ title: err.message || '绑定失败', icon: 'none' })
    }).finally(() => {
      wx.hideLoading()
    })
  }
})