const { request } = require('../../utils/request')

Page({
  data: {
    addressId: null,
    formData: {
      name: '',
      phone: '',
      province: '',
      city: '',
      district: '',
      detailAddress: '',
      isDefault: false
    }
  },

  onLoad(options) {
    const id = options.id
    if (id) {
      this.setData({ addressId: id })
      this.loadAddress(id)
    }
  },

  // 加载地址详情
  loadAddress(id) {
    wx.showLoading({ title: '加载中...' })
    request({ url: `/api/address/${id}` }).then((data) => {
      if (data) {
        // 转换 isDefault 为布尔值
        data.isDefault = data.isDefault === 1
        this.setData({ formData: data })
      }
    }).finally(() => {
      wx.hideLoading()
    })
  },

  // 输入框绑定
  bindInput(e) {
    const field = e.currentTarget.dataset.field
    const value = e.detail.value
    this.setData({
      [`formData.${field}`]: value
    })
  },

  // 开关变化
  bindSwitchChange(e) {
    this.setData({
      'formData.isDefault': e.detail.value
    })
  },

  // 提交表单
  submitForm() {
    const { formData } = this.data

    // 收货人姓名验证
    if (!formData.name || formData.name.trim() === '') {
      wx.showToast({ title: '请输入收货人姓名', icon: 'none' })
      return
    }
    if (formData.name.trim().length < 2) {
      wx.showToast({ title: '收货人姓名至少2个字符', icon: 'none' })
      return
    }

    // 联系电话验证
    if (!formData.phone) {
      wx.showToast({ title: '请输入联系电话', icon: 'none' })
      return
    }
    // 电话号码格式验证（支持手机和座机）
    const mobileRegex = /^1[3-9]\d{9}$/
    const landlineRegex = /^(0\d{2,3}-?)?[1-9]\d{7,8}$/
    if (!mobileRegex.test(formData.phone) && !landlineRegex.test(formData.phone)) {
      wx.showToast({ title: '请输入正确的电话号码', icon: 'none' })
      return
    }

    // 省份验证
    if (!formData.province || formData.province.trim() === '') {
      wx.showToast({ title: '请输入省份', icon: 'none' })
      return
    }
    const provinceRegex = /^[\u4e00-\u9fa5]{2,10}$/
    if (!provinceRegex.test(formData.province.trim())) {
      wx.showToast({ title: '省份格式不正确', icon: 'none' })
      return
    }

    // 城市验证
    if (!formData.city || formData.city.trim() === '') {
      wx.showToast({ title: '请输入城市', icon: 'none' })
      return
    }
    const cityRegex = /^[\u4e00-\u9fa5]{2,10}$/
    if (!cityRegex.test(formData.city.trim())) {
      wx.showToast({ title: '城市格式不正确', icon: 'none' })
      return
    }

    // 区县验证
    if (!formData.district || formData.district.trim() === '') {
      wx.showToast({ title: '请输入区县', icon: 'none' })
      return
    }
    const districtRegex = /^[\u4e00-\u9fa5]{2,10}$/
    if (!districtRegex.test(formData.district.trim())) {
      wx.showToast({ title: '区县格式不正确', icon: 'none' })
      return
    }

    // 详细地址验证
    if (!formData.detailAddress || formData.detailAddress.trim() === '') {
      wx.showToast({ title: '请输入详细地址', icon: 'none' })
      return
    }
    if (formData.detailAddress.trim().length < 5) {
      wx.showToast({ title: '详细地址至少5个字符', icon: 'none' })
      return
    }

    // 提交数据
    wx.showLoading({ title: '保存中...' })
    const url = this.data.addressId
      ? `/api/address/${this.data.addressId}`
      : '/api/address'
    const method = this.data.addressId ? 'PUT' : 'POST'

    // 转换 isDefault 为整数
    const submitData = {
      ...formData,
      isDefault: formData.isDefault ? 1 : 0
    }

    request({ url, method, data: submitData }).then(() => {
      wx.showToast({ title: '保存成功', icon: 'success' })
      setTimeout(() => {
        wx.navigateBack()
      }, 1000)
    }).finally(() => {
      wx.hideLoading()
    })
  }
})