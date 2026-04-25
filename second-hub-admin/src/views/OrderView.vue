<template>
  <el-card>
    <template #header>订单监管</template>
    <el-table :data="list" border>
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="orderNo" label="订单号" width="220" />
      <el-table-column label="商品图片" width="100">
        <template #default="scope">
          <el-image
            v-if="scope.row.goodsCover"
            :src="scope.row.goodsCover"
            fit="cover"
            :preview-src-list="[scope.row.goodsCover]"
            :preview-teleported="false"
            style="width: 60px; height: 60px; border-radius: 4px;"
          />
          <div v-else style="width: 60px; height: 60px; border: 1px solid #e4e7ed; border-radius: 4px; display: flex; align-items: center; justify-content: center; color: #909399;">
            无图片
          </div>
        </template>
      </el-table-column>
      <el-table-column prop="goodsTitle" label="商品" />
      <el-table-column prop="amount" label="金额" width="100" />
      <el-table-column prop="orderStatus" label="订单状态" width="160" />
      <el-table-column prop="payStatus" label="支付状态" width="120" />
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

const load = async () => {
  const page = await request.get('/api/admin/orders', { params: { pageNo: 1, pageSize: 100 } })
  list.value = page.records || []
}

const cancel = async (id) => {
  await request.post(`/api/admin/orders/${id}/cancel`)
  ElMessage.success('已取消')
  load()
}

onMounted(load)
</script>
