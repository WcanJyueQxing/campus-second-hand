/**
 * 微信小程序入口文件
 * 定义全局数据和应用生命周期钩子
 */
App({
  /**
   * 全局数据
   * @property {string} baseUrl - 后端API基础地址
   * @property {Object} userInfo - 用户信息对象
   * @property {string} orderTabType - 订单页面跳转类型
   * @property {string} token - 用户登录令牌
   */
  globalData: {
    baseUrl: 'http://127.0.0.1:8080',  // 后端服务地址
    userInfo: null,                     // 用户信息
    orderTabType: null                  // 订单页面跳转类型标记
  },

  /**
   * 应用启动时触发
   * 初始化登录状态，从本地存储读取token
   */
  onLaunch() {
    const token = wx.getStorageSync('token')
    if (token) {
      this.globalData.token = token
    }
  }
})