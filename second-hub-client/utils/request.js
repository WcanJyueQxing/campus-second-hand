const BASE_URL = 'http://127.0.0.1:8080'

function request({ url, method = 'GET', data = {}, header = {} }) {
  const token = wx.getStorageSync('token')

  // 白名单：登录、注册、验证码、随机姓名 不携带 token，避免 401
  const whiteList = [
    '/api/user/auth/account-login',
    '/api/user/auth/wx-login',
    '/api/user/auth/register',
    '/api/user/captcha/generate',
    '/api/user/random-name/generate',
    '/api/user/random-name/generate-batch',
    '/api/user/random-name/init'
  ]
  const isWhite = whiteList.some(item => url.includes(item))

  return new Promise((resolve, reject) => {
    wx.request({
      url: `${BASE_URL}${url}`,
      method,
      data,
      header: {
        'content-type': 'application/json',
        // 白名单接口不携带 token
        ...(!isWhite && token ? { Authorization: `Bearer ${token}` } : {}),
        ...header
      },
      success(res) {
        const body = res.data || {}
        if (body.code === 0 || body.code === 200) {
          resolve(body.data)
          return
        }
        const msg = body.message || (body.code === 401 ? '登录已过期，请重新登录' : '请求失败')
        if (body.code === 401) {
          wx.removeStorageSync('token')
          wx.removeStorageSync('userInfo')
        }
        wx.showToast({ title: msg, icon: 'none' })
        reject(body)
      },
      fail(err) {
        wx.showToast({ title: '网络错误', icon: 'none' })
        reject(err)
      }
    })
  })
}

function uploadFile(filePath) {
  const token = wx.getStorageSync('token')
  return new Promise((resolve, reject) => {
    wx.uploadFile({
      url: `${BASE_URL}/api/user/files/upload`,
      filePath,
      name: 'file',
      header: token ? { Authorization: `Bearer ${token}` } : {},
      success(res) {
        const body = JSON.parse(res.data)
        if (body.code === 0) {
          resolve(body.data.url)
          return
        }
        wx.showToast({ title: body.message || '上传失败', icon: 'none' })
        reject(body)
      },
      fail(err) {
        wx.showToast({ title: '上传失败', icon: 'none' })
        reject(err)
      }
    })
  })
}

module.exports = {
  request,
  uploadFile
}
