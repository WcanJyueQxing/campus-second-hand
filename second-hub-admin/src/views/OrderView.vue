<template>
  <el-card>
    <template #header>订单监管</template>
    
    <div v-if="previewVisible" class="image-preview-mask" @click="closePreview">
      <img :src="previewImageSrc" alt="预览图" class="preview-image" />
    </div>
    
    <el-table :data="list" border>
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="orderNo" label="订单号" width="220" />
      <el-table-column label="商品图片" width="100">
        <template #default="scope">
          <img 
            v-if="scope.row.goodsCover"
            :src="scope.row.goodsCover" 
            alt="商品图片" 
            class="cover-image"
            @click="previewImage(scope.row.goodsCover)"
          />
          <div v-else class="no-image">无图片</div>
        </template>
      </el-table-column>
      <el-table-column prop="goodsTitle" label="商品" />
      <el-table-column prop="amount" label="金额" width="100" />
      <el-table-column prop="orderStatus" label="订单状态" width="160">
        <template #default="scope">
          <el-tag :type="getOrderStatusType(scope.row.orderStatus)" size="small">
            {{ scope.row.orderStatus }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="payStatus" label="支付状态" width="120">
        <template #default="scope">
          <el-tag :type="getPayStatusType(scope.row.payStatus)" size="small">
            {{ scope.row.payStatus }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="120">
        <template #default="scope">
          <el-button type="danger" size="small" @click="cancel(scope.row.id)">取消订单</el-button>
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
const previewVisible = ref(false)
const previewImageSrc = ref('')

const getOrderStatusType = (status) => {
  const text = String(status || '')
  if (text.includes('完成')) return 'success'
  if (text.includes('待支付') || text.includes('已支付') || text.includes('确认')) return 'warning'
  if (text.includes('取消') || text.includes('关闭')) return 'danger'
  return 'default'
}

const getPayStatusType = (status) => {
  const text = String(status || '')
  if (text.includes('已支付')) return 'success'
  if (text.includes('未支付')) return 'warning'
  return 'default'
}

const load = async () => {
  const page = await request.get('/api/admin/orders', { params: { pageNo: 1, pageSize: 100 } })
  list.value = page.records || []
}

const cancel = async (id) => {
  await request.post(`/api/admin/orders/${id}/cancel`)
  ElMessage.success('已取消')
  load()
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

.no-image {
  width: 60px;
  height: 60px;
  border: 1px solid #e4e7ed;
  border-radius: 4px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #909399;
  font-size: 12px;
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
