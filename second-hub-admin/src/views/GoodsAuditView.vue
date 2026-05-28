<template>
  <el-card>
    <template #header>商品审核</template>
    
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
      <el-table-column prop="createdAt" label="发布时间" width="180" />
      <el-table-column label="操作" width="260">
        <template #default="scope">
          <el-button type="success" size="small" @click="audit(scope.row, true)">通过</el-button>
          <el-button type="warning" size="small" @click="audit(scope.row, false)">拒绝</el-button>
          <el-button type="danger" size="small" @click="offline(scope.row)">下架</el-button>
        </template>
      </el-table-column>
    </el-table>
  </el-card>
</template>

<script setup>
import { ElMessage, ElMessageBox } from 'element-plus'
import { onMounted, ref } from 'vue'
import request from '../utils/request'

/**
 * 商品审核视图组件
 * 提供待审核商品列表展示、图片预览和审核操作功能
 */
const list = ref([])
const previewVisible = ref(false)
const previewImageSrc = ref('')

/**
 * 加载待审核商品列表
 */
const load = async () => {
  const page = await request.get('/api/admin/goods/pending', { params: { pageNo: 1, pageSize: 50 } })
  list.value = page.records || []
}

/**
 * 审核商品
 * @param {Object} row - 商品数据
 * @param {boolean} approved - 是否通过审核
 */
const audit = async (row, approved) => {
  let reason = ''
  
  // 如果拒绝审核，需要输入拒绝原因
  if (!approved) {
    reason = await ElMessageBox.prompt('请输入拒绝原因', '审核拒绝').then((res) => res.value).catch(() => null)
    if (reason === null) {
      return
    }
  }
  
  // 调用审核接口
  await request.post(`/api/admin/goods/${row.id}/audit`, { approved, reason })
  ElMessage.success('操作成功')
  load()
}

/**
 * 下架商品
 * @param {Object} row - 商品数据
 */
const offline = async (row) => {
  await request.post(`/api/admin/goods/${row.id}/offline`)
  ElMessage.success('已下架')
  load()
}

/**
 * 预览商品图片
 * @param {string} src - 图片URL
 */
const previewImage = (src) => {
  previewImageSrc.value = src
  previewVisible.value = true
  document.body.style.overflow = 'hidden'
}

/**
 * 关闭图片预览
 */
const closePreview = () => {
  previewVisible.value = false
  document.body.style.overflow = ''
}

// 组件挂载时加载数据
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