Page({
  // 跳转到编辑资料页面
  toEditProfile() {
    wx.navigateTo({ url: '/pages/profile-edit/profile-edit' })
  },

  // 跳转到地址管理页面
  toAddressManagement() {
    wx.navigateTo({ url: '/pages/address/address' })
  },

  // 跳转到账号与安全页面
  toAccountSecurity() {
    wx.navigateTo({ url: '/pages/account-security/account-security' })
  },

  // 跳转到隐私设置页面
  toPrivacySettings() {
    wx.navigateTo({ url: '/pages/privacy-settings/privacy-settings' })
  },

  // 跳转到关于我们页面
  toAboutUs() {
    wx.navigateTo({ url: '/pages/about-us/about-us' })
  },

  // 跳转到意见反馈页面
  toFeedback() {
    wx.navigateTo({ url: '/pages/feedback/feedback' })
  },

  // 退出登录
  handleLogout() {
    wx.showModal({
      title: '确认退出',
      content: '确定要退出登录吗？',
      success: (res) => {
        if (res.confirm) {
          // 清除本地存储的token和用户信息
          wx.removeStorageSync('token')
          wx.removeStorageSync('userInfo')
          // 跳转到登录页面
          wx.redirectTo({ url: '/pages/login/login' })
        }
      }
    })
  }
})