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
  return Promise.reject(new Error(body.message || '请求失败'))
}, (error) => {
  if (error.response) {
    if (error.response.status === 401) {
      localStorage.removeItem('admin_token')
      localStorage.removeItem('admin_name')
      window.location.href = '/login'
    }
  }
  if (error.message.includes('Network Error')) {
    console.log('后端服务暂不可用，使用模拟数据')
  }
  return Promise.reject(error)
})

export default service