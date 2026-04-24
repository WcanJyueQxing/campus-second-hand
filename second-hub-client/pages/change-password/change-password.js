const { request } = require('../../utils/request')

Page({
  data: {
    oldPassword: '',
    newPassword: '',
    confirmPassword: '',
    showOldPassword: false,
    showNewPassword: false,
    showConfirmPassword: false
  },

  bindInput(e) {
    const field = e.currentTarget.dataset.field
    this.setData({
      [field]: e.detail.value
    })
  },

  toggleShowPassword(e) {
    const field = e.currentTarget.dataset.field
    this.setData({
      [field]: !this.data[field]
    })
  },

  submitForm() {
    const { oldPassword, newPassword, confirmPassword } = this.data

    if (!oldPassword) {
      wx.showToast({ title: '请输入原密码', icon: 'none' })
      return
    }

    if (!newPassword) {
      wx.showToast({ title: '请输入新密码', icon: 'none' })
      return
    }

    if (newPassword.length < 6) {
      wx.showToast({ title: '新密码至少6位', icon: 'none' })
      return
    }

    if (!confirmPassword) {
      wx.showToast({ title: '请确认新密码', icon: 'none' })
      return
    }

    if (newPassword !== confirmPassword) {
      wx.showToast({ title: '两次密码不一致', icon: 'none' })
      return
    }

    wx.showLoading({ title: '修改中...' })
    request({
      url: '/api/user/password',
      method: 'PUT',
      data: {
        oldPassword: oldPassword,
        newPassword: newPassword
      }
    }).then(() => {
      wx.showToast({ title: '密码修改成功', icon: 'success' })
      setTimeout(() => {
        wx.navigateBack()
      }, 1500)
    }).catch((err) => {
      wx.showToast({ title: err.message || '修改失败', icon: 'none' })
    }).finally(() => {
      wx.hideLoading()
    })
  }
})