const { request } = require('../../utils/request')

Page({
  data: {
    currentTab: 'account',
    account: '',
    password: '',
    nickname: '',
    remember: false,
    loading: false
  },

  onLoad(options) {
    const rememberAccount = wx.getStorageSync('rememberAccount')
    if (rememberAccount) {
      this.setData({
        account: rememberAccount.account || '',
        password: rememberAccount.password || '',
        remember: rememberAccount.remember || false
      })
    }
  },

  switchTab(e) {
    const tab = e.currentTarget.dataset.tab
    this.setData({ currentTab: tab })
  },

  onAccountInput(e) {
    this.setData({ account: e.detail.value })
  },

  onPasswordInput(e) {
    this.setData({ password: e.detail.value })
  },

  onNicknameInput(e) {
    this.setData({ nickname: e.detail.value })
  },

  onRememberChange(e) {
    const value = e.detail.value
    this.setData({ remember: value.length > 0 })
  },

  accountLogin() {
    const { account, password, remember } = this.data

    if (!account || !account.trim()) {
      wx.showToast({ title: '请输入用户名', icon: 'none' })
      return
    }

    if (!password || !password.trim()) {
      wx.showToast({ title: '请输入密码', icon: 'none' })
      return
    }

    if (this.data.loading) {
      return
    }

    this.setData({ loading: true })

    request({
      url: '/api/user/auth/account-login',
      method: 'POST',
      data: { account: account.trim(), password: password.trim() }
    }).then((data) => {
      if (remember) {
        wx.setStorageSync('rememberAccount', { account, password, remember: true })
      } else {
        wx.removeStorageSync('rememberAccount')
      }

      wx.setStorageSync('token', data.token)
      wx.setStorageSync('userInfo', data)
      wx.showToast({ title: '登录成功', icon: 'none' })

      setTimeout(() => {
        wx.switchTab({ url: '/pages/home/home' })
      }, 500)
    }).catch((err) => {
      const msg = (err && (err.message || err.msg)) || '登录失败'
      wx.showToast({ title: msg, icon: 'none' })
    }).finally(() => {
      this.setData({ loading: false })
    })
  },

  wechatLogin() {
    if (this.data.loading) {
      return
    }

    this.setData({ loading: true })

    wx.login({
      success: (res) => {
        if (!res.code) {
          wx.showToast({ title: '获取 code 失败', icon: 'none' })
          this.setData({ loading: false })
          return
        }

        request({
          url: '/api/user/auth/wx-login',
          method: 'POST',
          data: {
            code: res.code,
            nickname: this.data.nickname,
            avatarUrl: ''
          }
        }).then((data) => {
          wx.setStorageSync('token', data.token)
          wx.setStorageSync('userInfo', data)
          wx.showToast({ title: '登录成功', icon: 'none' })
          setTimeout(() => {
            wx.switchTab({ url: '/pages/home/home' })
          }, 500)
        }).catch((err) => {
          const msg = (err && (err.message || err.msg)) || '登录失败'
          wx.showToast({ title: msg, icon: 'none' })
        }).finally(() => {
          this.setData({ loading: false })
        })
      },
      fail: () => {
        wx.showToast({ title: '微信登录调用失败', icon: 'none' })
        this.setData({ loading: false })
      }
    })
  }
})
