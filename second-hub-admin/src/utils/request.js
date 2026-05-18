import axios from 'axios'

const service = axios.create({
  baseURL: 'http://127.0.0.1:8080',
  timeout: 15000
})

service.interceptors.request.use((config) => {
  const token = localStorage.getItem('admin_token')

  const whiteList = [
    '/api/admin/auth/login',
    '/api/user/captcha/generate'
  ]
  const isWhite = whiteList.some(item => config.url.includes(item))

  if (token && !isWhite) {
    config.headers.Authorization = `Bearer ${token}`
  }
  return config
})

service.interceptors.response.use((res) => {
  const body = res.data || {}
  if (body.code === 0) {
    return body.data
  }
  if (body.code === 401 || body.message?.includes('令牌') || body.message?.includes('登录')) {
    localStorage.removeItem('admin_token')
    localStorage.removeItem('admin_name')
    setTimeout(() => {
      window.location.href = '/login'
    }, 100)
  }
  return Promise.reject(new Error(body.message || '请求失败'))
}, (error) => {
  if (error.response) {
    if (error.response.status === 401) {
      localStorage.removeItem('admin_token')
      localStorage.removeItem('admin_name')
      setTimeout(() => {
        window.location.href = '/login'
      }, 100)
    }
  }
  if (error.message?.includes('令牌无效') || error.message?.includes('已过期') || error.message?.includes('未登录')) {
    localStorage.removeItem('admin_token')
    localStorage.removeItem('admin_name')
    setTimeout(() => {
      window.location.href = '/login'
    }, 100)
  }
  if (error.message.includes('Network Error')) {
    console.log('后端服务暂不可用，使用模拟数据')
  }
  return Promise.reject(error)
})

export default service