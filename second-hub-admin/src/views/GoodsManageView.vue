<template>
  <el-card>
    <template #header>商品管理</template>
    
    <!-- 图片预览遮罩层 -->
    <div v-if="previewVisible" class="image-preview-mask" @click="closePreview">
      <img :src="previewImageSrc" alt="预览图" class="preview-image" />
    </div>
    <el-table :data="list" border>
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column label="封面图" width="100">
        <template #default="scope">
          <img :src="scope.row.coverImage" alt="封面图" class="cover-image" @click="previewImage(scope.row.coverImage)" />
        </template>
      </el-table-column>
      <el-table-column prop="title" label="标题" />
      <el-table-column prop="price" label="价格" width="120" />
      <el-table-column prop="status" label="状态" width="100">
        <template #default="scope">
          <el-tag :type="getStatusType(scope.row.status)" size="small">
            {{ scope.row.status }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="favoriteCount" label="收藏数" width="100" />
      <el-table-column prop="createdAt" label="发布时间" width="180" />
      <el-table-column label="操作" width="200">
        <template #default="scope">
          <el-button type="warning" size="small" @click="offline(scope.row)">下架</el-button>
          <el-button type="danger" size="small" @click="deleteGoods(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
  </el-card>
</template>

<script setup>
import { ElMessage, ElMessageBox } from 'element-plus'
import { onMounted, ref } from 'vue'
import request from '../utils/request'

const list = ref([])
const previewVisible = ref(false)
const previewImageSrc = ref('')

const load = async () => {
  const page = await request.get('/api/admin/goods/list', { params: { pageNo: 1, pageSize: 50 } })
  list.value = page.records || []
}

const getStatusType = (status) => {
  const text = String(status || '')
  if (text.includes('上架') || text.includes('通过')) return 'success'
  if (text.includes('下架') || text.includes('草稿') || text.includes('售出')) return 'info'
  if (text.includes('审核')) return 'warning'
  if (text.includes('拒绝') || text.includes('驳回')) return 'danger'
  return 'default'
}

const offline = async (row) => {
  await request.post(`/api/admin/goods/${row.id}/offline`)
  ElMessage.success('已下架')
  load()
}

const deleteGoods = async (row) => {
  await ElMessageBox.confirm('确定要删除该商品吗？', '删除确认').then(async () => {
    await request.delete(`/api/admin/goods/${row.id}`)
    ElMessage.success('删除成功')
    load()
  }).catch(() => {})
}

const previewImage = (src) => {
  previewImageSrc.value = src
  previewVisible.value = true
  document.body.style.overflow = 'hidden'
}

const closePreview = () => {
  previewVisible.value = false
  document.body.style.overflow = ''
}

onMounted(load)
</script>

<style scoped>
.cover-image {
  width: 60px;
  height: 60px;
  object-fit: cover;
  border-radius: 4px;
  cursor: pointer;
}

.image-preview-mask {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.7);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 9999;
  cursor: pointer;
}

.preview-image {
  max-width: 90%;
  max-height: 90%;
  object-fit: contain;
  border-radius: 8px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.3);
}

</style>