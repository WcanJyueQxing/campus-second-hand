<template>
  <el-container style="height: 100vh">
    <el-aside width="220px" style="background: #1f2d3d; color: #fff">
      <div class="logo">Second Hub Admin</div>
      <el-menu
        :default-active="$route.path"
        background-color="#1f2d3d"
        text-color="#d3dce6"
        active-text-color="#409eff"
        router
      >
        <el-menu-item index="/dashboard">
          <el-icon><DataAnalysis /></el-icon>
          <span>数据统计</span>
        </el-menu-item>
        <el-menu-item index="/goods-audit">
          <el-icon><Goods /></el-icon>
          <span>商品审核</span>
        </el-menu-item>
        <el-menu-item index="/categories">
          <el-icon><Grid /></el-icon>
          <span>分类管理</span>
        </el-menu-item>
        <el-menu-item index="/users">
          <el-icon><User /></el-icon>
          <span>用户管理</span>
        </el-menu-item>
        <el-menu-item index="/reports">
          <el-icon><Warning /></el-icon>
          <span>举报处理</span>
        </el-menu-item>
        <el-menu-item index="/notices">
          <el-icon><Bell /></el-icon>
          <span>公告管理</span>
        </el-menu-item>
        <el-menu-item index="/orders">
          <el-icon><List /></el-icon>
          <span>订单监管</span>
        </el-menu-item>
      </el-menu>
    </el-aside>

    <el-container>
      <el-header class="header">
        <span>欢迎，{{ authStore.adminName }}</span>
        <el-button type="danger" plain size="small" @click="logout">退出</el-button>
      </el-header>
      <el-main>
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { useRouter } from 'vue-router'
import { useAuthStore } from '../stores/auth'

const router = useRouter()
const authStore = useAuthStore()

const logout = () => {
  authStore.logout()
  router.push('/login')
}
</script>

<style scoped>
.logo {
  height: 56px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 18px;
  font-weight: 700;
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
}

.header {
  background: #fff;
  display: flex;
  justify-content: space-between;
  align-items: center;
  border-bottom: 1px solid #ebeef5;
}
</style>
