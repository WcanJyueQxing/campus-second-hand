<template>
  <el-card>
    <template #header>订单监管</template>
    
    <!-- 搜索栏 -->
    <div class="search-bar">
      <el-input 
        v-model="keyword" 
        placeholder="请输入订单号搜索" 
        class="search-input"
        @keyup.enter="load"
        @input="handleSearch"
      >
        <template #append>
          <el-button @click="load" icon="Search">搜索</el-button>
        </template>
      </el-input>
    </div>
    
    <!-- 图片预览遮罩层 -->
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

/**
 * 订单监管视图组件
 * 提供订单列表展示、搜索、商品图片预览和订单取消功能
 */
const list = ref([])
const previewVisible = ref(false)
const previewImageSrc = ref('')
const keyword = ref('')
let searchTimer = null

/**
 * 获取订单状态对应的标签类型
 * @param {string} status - 订单状态
 * @returns {string} 标签类型
 */
const getOrderStatusType = (status) => {
  const text = String(status || '')
  if (text.includes('完成')) return 'success'
  if (text.includes('待支付') || text.includes('已支付') || text.includes('确认')) return 'warning'
  if (text.includes('取消') || text.includes('关闭')) return 'danger'
  return 'default'
}

/**
 * 获取支付状态对应的标签类型
 * @param {string} status - 支付状态
 * @returns {string} 标签类型
 */
const getPayStatusType = (status) => {
  const text = String(status || '')
  if (text.includes('已支付')) return 'success'
  if (text.includes('未支付')) return 'warning'
  return 'default'
}

/**
 * 加载订单列表（支持搜索）
 */
const load = async () => {
  const page = await request.get('/api/admin/orders', { params: { pageNo: 1, pageSize: 100, keyword: keyword.value } })
  list.value = page.records || []
}

/**
 * 输入变化时触发搜索（带防抖）
 */
const handleSearch = () => {
  if (searchTimer) {
    clearTimeout(searchTimer)
  }
  searchTimer = setTimeout(() => {
    load()
  }, 300)
}

/**
 * 取消订单
 * @param {number} id - 订单ID
 */
const cancel = async (id) => {
  await request.post(`/api/admin/orders/${id}/cancel`)
  ElMessage.success('已取消')
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
.search-bar {
  margin-bottom: 16px;
}

.search-input {
  width: 300px;
}

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