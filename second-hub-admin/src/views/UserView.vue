<template>
  <el-card>
    <template #header>用户管理</template>
    <el-table :data="list" border>
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column label="头像" width="100">
        <template #default="scope">
          <img 
            :src="scope.row.avatarUrl || getDefaultAvatar(scope.row.nickname)" 
            alt="头像" 
            class="avatar-image"
            @error="handleAvatarError($event)"
          />
        </template>
      </el-table-column>
      <el-table-column prop="nickname" label="昵称" />
      <el-table-column prop="phone" label="手机号" />
      <el-table-column prop="status" label="状态" width="120">
        <template #default="scope">{{ scope.row.status === 1 ? '正常' : '禁用' }}</template>
      </el-table-column>
      <el-table-column label="操作" width="180">
        <template #default="scope">
          <el-button size="small" type="warning" @click="setStatus(scope.row, 0)">禁用</el-button>
          <el-button size="small" type="success" @click="setStatus(scope.row, 1)">启用</el-button>
        </template>
      </el-table-column>
    </el-table>
  </el-card>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { ElMessage } from 'element-plus'
import request from '../utils/request'

const list = ref([])

const load = async () => {
  const page = await request.get('/api/admin/users', { params: { pageNo: 1, pageSize: 100 } })
  list.value = page.records || []
}

const setStatus = async (row, status) => {
  await request.post(`/api/admin/users/${row.id}/status`, { status })
  ElMessage.success('状态已更新')
  load()
}

const getDefaultAvatar = (nickname) => {
  if (!nickname) {
    return 'data:image/svg+xml,%3Csvg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 100 100"%3E%3Ccircle cx="50" cy="50" r="45" fill="%23e8e8e8"/%3E%3Ctext x="50" y="55" text-anchor="middle" font-size="30" fill="%23999"%3E%E4%BA%BA%3C/text%3E%3C/svg%3E'
  }
  const colors = ['#67c23a', '#409eff', '#f56c6c', '#e6a23c', '#909399', '#b37feb', '#6f7ad3', '#39c5cf']
  const index = nickname.charCodeAt(0) % colors.length
  const color = colors[index]
  const char = nickname.charAt(0)
  return `data:image/svg+xml,%3Csvg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 100 100"%3E%3Ccircle cx="50" cy="50" r="45" fill="${encodeURIComponent(color)}"/%3E%3Ctext x="50" y="55" text-anchor="middle" font-size="30" fill="white"%3E${encodeURIComponent(char)}%3C/text%3E%3C/svg%3E`
}

const handleAvatarError = (event) => {
  const target = event.target
  target.src = 'data:image/svg+xml,%3Csvg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 100 100"%3E%3Ccircle cx="50" cy="50" r="45" fill="%23e8e8e8"/%3E%3Ctext x="50" y="55" text-anchor="middle" font-size="30" fill="%23999"%3E%E4%BA%BA%3C/text%3E%3C/svg%3E'
}

onMounted(load)
</script>

<style scoped>
.avatar-image {
  width: 60px;
  height: 60px;
  object-fit: cover;
  border-radius: 50%;
}
</style>
