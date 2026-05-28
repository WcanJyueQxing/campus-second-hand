import axios from 'axios'

/**
 * 创建 axios 实例
 * 配置基础请求参数
 */
const service = axios.create({
  baseURL: 'http://127.0.0.1:8080',  // 后端服务地址
  timeout: 15000                      // 请求超时时间
})

/**
 * 请求拦截器
 * 在请求发送前添加 token 认证
 */
service.interceptors.request.use((config) => {
  // 从 localStorage 获取管理员 token
  const token = localStorage.getItem('admin_token')

  // 白名单 - 无需 token 的接口
  const whiteList = [
    '/api/admin/auth/login',     // 登录接口
    '/api/user/captcha/generate' // 验证码接口
  ]
  
  // 判断是否在白名单中
  const isWhite = whiteList.some(item => config.url.includes(item))

  // 如果有 token 且不在白名单，添加 Authorization 头
  if (token && !isWhite) {
    config.headers.Authorization = `Bearer ${token}`
  }
  
  return config
})

/**
 * 响应拦截器
 * 处理响应数据和错误
 */
service.interceptors.response.use(
  (res) => {
    const body = res.data || {}
    
    // 成功响应（code = 0）
    if (body.code === 0) {
      return body.data
    }
    
    // 未登录或 token 失效（code = 401）
    if (body.code === 401 || body.message?.includes('令牌') || body.message?.includes('登录')) {
      localStorage.removeItem('admin_token')
      localStorage.removeItem('admin_name')
      setTimeout(() => {
        window.location.href = '/login'
      }, 100)
    }
    
    // 其他错误，抛出异常
    return Promise.reject(new Error(body.message || '请求失败'))
  },
  (error) => {
    // HTTP 401 错误处理
    if (error.response && error.response.status === 401) {
      localStorage.removeItem('admin_token')
      localStorage.removeItem('admin_name')
      setTimeout(() => {
        window.location.href = '/login'
      }, 100)
    }
    
    // token 相关错误处理
    if (error.message?.includes('令牌无效') || error.message?.includes('已过期') || error.message?.includes('未登录')) {
      localStorage.removeItem('admin_token')
      localStorage.removeItem('admin_name')
      setTimeout(() => {
        window.location.href = '/login'
      }, 100)
    }
    
    // 网络错误处理
    if (error.message.includes('Network Error')) {
      console.log('后端服务暂不可用，使用模拟数据')
    }
    
    return Promise.reject(error)
  }
)

export default service