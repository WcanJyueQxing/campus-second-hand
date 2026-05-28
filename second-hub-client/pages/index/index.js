/**
 * 微信小程序默认首页
 * 展示用户信息获取入口和欢迎界面
 */

// 默认头像URL
const defaultAvatarUrl = 'https://mmbiz.qpic.cn/mmbiz/icTdbqWNOwNRna42FI242Lcia07jQodd2FJGIYQfG0LAJGFxM4FbnQP6yfMxBgJ0F3YRqJCJ1aPAK2dQagdusBZg/0'

Page({
  /**
   * 页面数据
   * @property {string} motto - 欢迎语
   * @property {Object} userInfo - 用户信息
   * @property {string} userInfo.avatarUrl - 用户头像URL
   * @property {string} userInfo.nickName - 用户昵称
   * @property {boolean} hasUserInfo - 是否已获取用户信息
   * @property {boolean} canIUseGetUserProfile - 是否支持getUserProfile接口
   * @property {boolean} canIUseNicknameComp - 是否支持昵称组件
   */
  data: {
    motto: 'Hello World',
    userInfo: {
      avatarUrl: defaultAvatarUrl,
      nickName: '',
    },
    hasUserInfo: false,
    canIUseGetUserProfile: wx.canIUse('getUserProfile'),
    canIUseNicknameComp: wx.canIUse('input.type.nickname'),
  },

  /**
   * 跳转到日志页面
   */
  bindViewTap() {
    wx.navigateTo({ url: '../logs/logs' })
  },

  /**
   * 选择头像事件
   * @param {Object} e - 事件对象
   */
  onChooseAvatar(e) {
    const { avatarUrl } = e.detail
    const { nickName } = this.data.userInfo
    this.setData({
      "userInfo.avatarUrl": avatarUrl,
      hasUserInfo: nickName && avatarUrl && avatarUrl !== defaultAvatarUrl,
    })
  },

  /**
   * 昵称输入事件
   * @param {Object} e - 事件对象
   */
  onInputChange(e) {
    const nickName = e.detail.value
    const { avatarUrl } = this.data.userInfo
    this.setData({
      "userInfo.nickName": nickName,
      hasUserInfo: nickName && avatarUrl && avatarUrl !== defaultAvatarUrl,
    })
  },

  /**
   * 获取用户信息
   * 推荐使用wx.getUserProfile获取用户信息，每次调用均需用户确认
   * @param {Object} e - 事件对象
   */
  getUserProfile(e) {
    wx.getUserProfile({
      desc: '展示用户信息',
      success: (res) => {
        console.log(res)
        this.setData({
          userInfo: res.userInfo,
          hasUserInfo: true
        })
      }
    })
  },
})