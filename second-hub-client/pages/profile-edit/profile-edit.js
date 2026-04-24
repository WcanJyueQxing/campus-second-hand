const { request, uploadFile } = require('../../utils/request')

Page({
  data: {
    avatarUrl: '',
    nickname: '',
    bio: '',
    gender: null,
    genderText: '',
    birthday: '',
    school: '',
    // 日期选择器
    showDatePicker: false,
    pickerIndex: [0, 0, 0],
    years: [],
    months: [],
    days: []
  },

  onLoad() {
    this.initDatePicker()
    this.loadUserInfo()
  },

  onShow() {
    this.loadUserInfo()
  },

  // 1. 初始化日期选择器数据
  initDatePicker() {
    const startYear = 1990
    const endYear = 2010
    const years = []
    for (let i = startYear; i <= endYear; i++) {
      years.push(i)
    }
    const months = Array.from({ length: 12 }, (_, i) => i + 1)
    const days = Array.from({ length: 31 }, (_, i) => i + 1)

    this.setData({ years, months, days })
  },

  // 2. 解析初始生日，定位到选择器的正确位置
  parseInitialDate() {
    const { birthday, years, months, days } = this.data
    if (!birthday) return

    const [year, month, day] = birthday.split('-').map(Number)
    const yearIndex = years.indexOf(year)
    const monthIndex = month - 1
    const dayIndex = day - 1

    this.setData({
      pickerIndex: [yearIndex, monthIndex, dayIndex]
    })
  },

  loadUserInfo() {
    wx.showLoading({ title: '加载中...' })
    request({ url: '/api/user/info' }).then((data) => {
      if (data) {
        const genderText = this.getGenderText(data.gender)
        this.setData({
          avatarUrl: data.avatarUrl || '',
          nickname: data.nickname || '',
          bio: data.bio || '',
          gender: data.gender,
          genderText: genderText,
          birthday: data.birthday || '',
          school: data.school || ''
        }, () => {
          this.parseInitialDate()
        })
      }
    }).finally(() => {
      wx.hideLoading()
    })
  },

  getGenderText(gender) {
    if (gender === 1) return '男'
    if (gender === 2) return '女'
    if (gender === 0) return '保密'
    return ''
  },

  chooseAvatar() {
    wx.chooseMedia({
      count: 1,
      mediaType: ['image'],
      sourceType: ['album', 'camera'],
      success: (res) => {
        const tempFilePath = res.tempFiles[0].tempFilePath
        wx.showLoading({ title: '上传中...' })
        uploadFile(tempFilePath).then((url) => {
          this.setData({ avatarUrl: url })
          // 不在这里调用saveProfile，避免loading冲突
          const { nickname, bio, gender, birthday, school } = this.data
          const profileData = {
            nickname,
            bio,
            gender,
            birthday,
            school,
            avatarUrl: url
          }
          request({ url: '/api/user/info', method: 'PUT', data: profileData }).then(() => {
            wx.showToast({ title: '保存成功', icon: 'success' })
          }).finally(() => {
            wx.hideLoading()
          })
        }).catch(() => {
          wx.hideLoading()
        })
      }
    })
  },

  editNickname() {
    wx.showModal({
      title: '修改昵称',
      editable: true,
      placeholderText: '请输入昵称',
      content: this.data.nickname,
      success: (res) => {
        if (res.confirm && res.content) {
          this.setData({ nickname: res.content }, () => {
            this.saveProfile()
          })
        }
      }
    })
  },

  editBio() {
    wx.showModal({
      title: '编辑简介',
      editable: true,
      placeholderText: '请输入个人简介',
      content: this.data.bio,
      success: (res) => {
        if (res.confirm && res.content) {
          this.setData({ bio: res.content }, () => {
            this.saveProfile()
          })
        }
      }
    })
  },

  chooseGender() {
    wx.showActionSheet({
      itemList: ['男', '女', '保密'],
      success: (res) => {
        const genderMap = { 0: 1, 1: 2, 2: 0 }
        const gender = genderMap[res.tapIndex]
        const genderText = this.getGenderText(gender)
        this.setData({ gender, genderText }, () => {
          this.saveProfile()
        })
      }
    })
  },

  // 3. 显示日期选择器
  showDatePicker() {
    this.setData({ showDatePicker: true })
  },

  // 4. 隐藏日期选择器
  hideDatePicker() {
    this.setData({ showDatePicker: false })
  },

  // 5. 日期选择变化时，更新显示的生日
  onDateChange(e) {
    const [yearIdx, monthIdx, dayIdx] = e.detail.value
    const { years, months, days } = this.data

    const year = years[yearIdx]
    const month = months[monthIdx]
    const day = days[dayIdx]

    // 格式化为 YYYY-MM-DD，和后端交互格式统一
    const newBirthday = `${year}-${String(month).padStart(2, '0')}-${String(day).padStart(2, '0')}`

    this.setData({ 
      pickerIndex: e.detail.value, 
      birthday: newBirthday 
    })
  },

  chooseSchool() {
    wx.showModal({
      title: '院校名称',
      editable: true,
      placeholderText: '请输入院校名称',
      content: this.data.school,
      success: (res) => {
        if (res.confirm && res.content) {
          this.setData({ school: res.content })
        }
      }
    })
  },

  // 6. 保存修改时，提交生日到后端
  saveProfile() {
    const { nickname, bio, gender, birthday, school } = this.data
    const profileData = {
      nickname,
      bio,
      gender,
      birthday,
      school
    }
    wx.showLoading({ title: '保存中...' })
    request({ url: '/api/user/info', method: 'PUT', data: profileData }).then(() => {
      wx.showToast({ title: '保存成功', icon: 'success' })
      this.hideDatePicker()
      // 保存成功后返回上一个页面
      setTimeout(() => {
        wx.navigateBack()
      }, 1000)
    }).finally(() => {
      wx.hideLoading()
    })
  }
})