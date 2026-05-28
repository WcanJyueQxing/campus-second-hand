/**
 * 登录页面
 * 提供账号密码登录、微信登录和用户注册功能
 */
const { request } = require('../../utils/request')

Page({
  /**
   * 页面数据
   * @property {string} currentTab - 当前标签页（account-登录，register-注册）
   * @property {string} account - 登录账号
   * @property {string} password - 登录密码
   * @property {string} nickname - 微信登录昵称
   * @property {boolean} remember - 是否记住密码
   * @property {boolean} loading - 加载状态
   * @property {string} captchaUuid - 验证码UUID
   * @property {string} captchaBase64 - 验证码图片Base64
   * @property {string} captchaCode - 用户输入的验证码
   * @property {Object} registerForm - 注册表单
   */
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
      phone: '',
      password: '',
      confirmPassword: '',
      captchaCode: ''
    }
  },

  /**
   * 页面加载时触发
   * 初始化表单数据和验证码
   * @param {Object} options - URL参数
   */
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

  /**
   * 切换标签页
   * @param {Object} e - 事件对象
   */
  switchTab(e) {
    const tab = e.currentTarget.dataset.tab
    this.setData({ currentTab: tab })
  },

  /**
   * 账号输入事件
   * @param {Object} e - 事件对象
   */
  onAccountInput(e) {
    this.setData({ account: e.detail.value })
  },

  /**
   * 密码输入事件
   * @param {Object} e - 事件对象
   */
  onPasswordInput(e) {
    this.setData({ password: e.detail.value })
  },

  /**
   * 昵称输入事件
   * @param {Object} e - 事件对象
   */
  onNicknameInput(e) {
    this.setData({ nickname: e.detail.value })
  },

  /**
   * 生成随机姓名
   * 用于自动生成昵称
   */
  generateRandomName() {
    request({
      url: '/api/user/random-name/generate',
      method: 'GET',
      data: { gender: 'random' }
    }).then((name) => {
      this.setData({ nickname: name })
      wx.showToast({ title: '生成成功', icon: 'success' })
    }).catch(() => {
      wx.showToast({ title: '生成失败，请重试', icon: 'none' })
    })
  },

  /**
   * 注册用户名输入事件
   * @param {Object} e - 事件对象
   */
  onRegisterUsernameInput(e) {
    this.setData({ 'registerForm.username': e.detail.value })
  },

  /**
   * 注册手机号输入事件
   * @param {Object} e - 事件对象
   */
  onRegisterPhoneInput(e) {
    this.setData({ 'registerForm.phone': e.detail.value })
  },

  /**
   * 注册密码输入事件
   * @param {Object} e - 事件对象
   */
  onRegisterPasswordInput(e) {
    this.setData({ 'registerForm.password': e.detail.value })
  },

  /**
   * 注册确认密码输入事件
   * @param {Object} e - 事件对象
   */
  onRegisterConfirmPasswordInput(e) {
    this.setData({ 'registerForm.confirmPassword': e.detail.value })
  },

  /**
   * 注册验证码输入事件
   * @param {Object} e - 事件对象
   */
  onRegisterCaptchaInput(e) {
    this.setData({ 'registerForm.captchaCode': e.detail.value })
  },

  /**
   * 为注册表单生成随机姓名
   */
  generateRandomRegisterName() {
    request({
      url: '/api/user/random-name/generate',
      method: 'GET',
      data: { gender: 'random' }
    }).then((name) => {
      this.setData({ 'registerForm.username': name })
      wx.showToast({ title: '生成成功', icon: 'success' })
    }).catch(() => {
      wx.showToast({ title: '生成失败，请重试', icon: 'none' })
    })
  },

  /**
   * 用户注册
   * 验证表单后调用注册接口
   */
  register() {
    const { username, phone, password, confirmPassword, captchaCode } = this.data.registerForm

    if (!username || !username.trim()) {
      wx.showToast({ title: '请输入用户名', icon: 'none' })
      return
    }
    if (!phone || !phone.trim()) {
      wx.showToast({ title: '请输入手机号', icon: 'none' })
      return
    }
    const phoneReg = /^1[3-9]\d{9}$/
    if (!phoneReg.test(phone.trim())) {
      wx.showToast({ title: '手机号格式不正确', icon: 'none' })
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
        phone: phone.trim(),
        password: password.trim(),
        captchaCode: captchaCode.trim(),
        captchaUuid: this.data.captchaUuid
      }
    }).then(() => {
      wx.showToast({ title: '注册成功', icon: 'success' })
      this.setData({ currentTab: 'account' })
      this.setData({
        registerForm: {
          username: '',
          phone: '',
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

  /**
   * 记住密码开关事件
   * @param {Object} e - 事件对象
   */
  onRememberChange(e) {
    const value = e.detail.value
    this.setData({ remember: value.length > 0 })
  },

  /**
   * 验证码输入事件
   * @param {Object} e - 事件对象
   */
  onCaptchaInput(e) {
    this.setData({ captchaCode: e.detail.value })
  },

  /**
   * 刷新验证码
   */
  refreshCaptcha() {
    request({ url: '/api/user/captcha/generate', method: 'GET' })
      .then((data) => {
        this.setData({
          captchaUuid: data.uuid,
          captchaBase64: data.captcha
        })
      })
      .catch(() => {
        wx.showToast({ title: '验证码加载失败', icon: 'none' })
      })
  },

  /**
   * 账号密码登录
   */
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

  /**
   * 微信登录
   */
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