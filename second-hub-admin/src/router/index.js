import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore } from '../stores/auth'

/**
 * 路由配置
 * 定义管理员后台的页面路由
 */
const routes = [
  // 登录页 - 无需权限
  { path: '/login', name: 'login', component: () => import('../views/LoginView.vue') },
  
  // 主布局 - 需要登录权限
  {
    path: '/',
    component: () => import('../layout/AdminLayout.vue'),
    redirect: '/dashboard',
    children: [
      { path: 'dashboard', component: () => import('../views/DashboardView.vue') },      // 仪表盘首页
      { path: 'goods-audit', component: () => import('../views/GoodsAuditView.vue') },  // 商品审核
      { path: 'goods-manage', component: () => import('../views/GoodsManageView.vue') },// 商品管理
      { path: 'categories', component: () => import('../views/CategoryView.vue') },      // 分类管理
      { path: 'users', component: () => import('../views/UserView.vue') },              // 用户管理
      { path: 'reports', component: () => import('../views/ReportView.vue') },          // 举报处理
      { path: 'notices', component: () => import('../views/NoticeView.vue') },          // 公告管理
      { path: 'orders', component: () => import('../views/OrderView.vue') },            // 订单监管
      { path: 'feedback', component: () => import('../views/FeedbackView.vue') }        // 意见反馈
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

/**
 * 路由前置守卫
 * 实现登录状态检查和页面权限控制
 */
router.beforeEach((to) => {
  const authStore = useAuthStore()
  
  // 未登录且访问非登录页，跳转到登录页
  if (to.path !== '/login' && !authStore.token) {
    return '/login'
  }
  
  // 已登录且访问登录页，跳转到首页
  if (to.path === '/login' && authStore.token) {
    return '/dashboard'
  }
  
  return true
})

export default router