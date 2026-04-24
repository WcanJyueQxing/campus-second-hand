const { request } = require('../../utils/request')

Page({
  data: {
    currentTab: 'account',
    account: '',
    password: '',
    nickname: '',
    remember: false,
    loading: false,
    captchaUuid: '',
    captchaBase64: '',
    captchaCode: '',
    registerForm: {
      username: '',
      password: '',
      confirmPassword: '',
      captchaCode: ''
    }
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
    if (options.tab) {
      this.setData({ currentTab: options.tab })
    }
    this.refreshCaptcha()
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

  // 生成随机姓名
  generateRandomName() {
    request({
      url: '/api/user/random-name/generate',
      method: 'GET',
      data: {
        gender: 'random'
      }
    }).then((name) => {
      this.setData({ nickname: name })
      wx.showToast({
        title: '生成成功',
        icon: 'success'
      })
    }).catch((err) => {
      wx.showToast({
        title: '生成失败，请重试',
        icon: 'none'
      })
    })
  },

  // 注册用户名输入
  onRegisterUsernameInput(e) {
    this.setData({
      'registerForm.username': e.detail.value
    })
  },

  // 注册密码输入
  onRegisterPasswordInput(e) {
    this.setData({
      'registerForm.password': e.detail.value
    })
  },

  // 注册确认密码输入
  onRegisterConfirmPasswordInput(e) {
    this.setData({
      'registerForm.confirmPassword': e.detail.value
    })
  },

  // 注册验证码输入
  onRegisterCaptchaInput(e) {
    this.setData({
      'registerForm.captchaCode': e.detail.value
    })
  },

  // 生成注册表单的随机姓名
  generateRandomRegisterName() {
    request({
      url: '/api/user/random-name/generate',
      method: 'GET',
      data: {
        gender: 'random'
      }
    }).then((name) => {
      this.setData({ 'registerForm.username': name })
      wx.showToast({
        title: '生成成功',
        icon: 'success'
      })
    }).catch((err) => {
      wx.showToast({
        title: '生成失败，请重试',
        icon: 'none'
      })
    })
  },

  // 注册
  register() {
    const { username, password, confirmPassword, captchaCode } = this.data.registerForm

    if (!username || !username.trim()) {
      wx.showToast({ title: '请输入用户名', icon: 'none' })
      return
    }

    if (!password || password.length < 6) {
      wx.showToast({ title: '密码长度至少6位', icon: 'none' })
      return
    }

    if (password !== confirmPassword) {
      wx.showToast({ title: '两次密码输入不一致', icon: 'none' })
      return
    }

    if (!captchaCode || !captchaCode.trim()) {
      wx.showToast({ title: '请输入验证码', icon: 'none' })
      return
    }

    if (this.data.loading) {
      return
    }

    this.setData({ loading: true })

    request({
      url: '/api/user/auth/register',
      method: 'POST',
      data: {
        username: username.trim(),
        password: password.trim(),
        captchaCode: captchaCode.trim(),
        captchaUuid: this.data.captchaUuid
      }
    }).then((data) => {
      wx.showToast({ title: '注册成功', icon: 'success' })
      // 注册成功后切换到登录标签
      this.setData({ currentTab: 'account' })
      // 清空注册表单
      this.setData({
        registerForm: {
          username: '',
          password: '',
          confirmPassword: '',
          captchaCode: ''
        }
      })
    }).catch((err) => {
      if (err && err.code === 400) {
        this.refreshCaptcha()
        this.setData({ 'registerForm.captchaCode': '' })
      }
      const msg = (err && (err.message || err.msg)) || '注册失败'
      wx.showToast({ title: msg, icon: 'none' })
    }).finally(() => {
      this.setData({ loading: false })
    })
  },

  onRememberChange(e) {
    const value = e.detail.value
    this.setData({ remember: value.length > 0 })
  },

  onCaptchaInput(e) {
    this.setData({ captchaCode: e.detail.value })
  },

  refreshCaptcha() {
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
    })
  },

  accountLogin() {
    const { account, password, remember, captchaCode, captchaUuid } = this.data

    if (!account || !account.trim()) {
      wx.showToast({ title: '请输入用户名', icon: 'none' })
      return
    }

    if (!password || !password.trim()) {
      wx.showToast({ title: '请输入密码', icon: 'none' })
      return
    }

    if (!captchaCode || !captchaCode.trim()) {
      wx.showToast({ title: '请输入验证码', icon: 'none' })
      return
    }

    if (this.data.loading) {
      return
    }

    this.setData({ loading: true })

    request({
      url: '/api/user/auth/account-login',
      method: 'POST',
      data: {
        account: account.trim(),
        password: password.trim(),
        captchaCode: captchaCode.trim(),
        captchaUuid: captchaUuid
      }
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
      if (err && err.code === 400) {
        this.refreshCaptcha()
        this.setData({ captchaCode: '' })
      }
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
