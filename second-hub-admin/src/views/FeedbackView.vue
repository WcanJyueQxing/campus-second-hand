<template>
  <el-card>
    <template #header>
      <div class="flex items-center justify-between">
        <span>意见反馈</span>
        <div class="flex items-center gap-4">
          <el-select v-model="searchForm.status" placeholder="状态筛选" class="w-32" @change="load">
            <el-option label="全部" value="" />
            <el-option label="待处理" value="PENDING" />
            <el-option label="已回复" value="REPLIED" />
          </el-select>
          <el-select v-model="searchForm.type" placeholder="类型筛选" class="w-32" @change="load">
            <el-option label="全部" value="" />
            <el-option label="功能建议" value="FEATURE" />
            <el-option label="Bug反馈" value="BUG" />
            <el-option label="商品问题" value="GOODS" />
            <el-option label="交易纠纷" value="DISPUTE" />
            <el-option label="其他" value="OTHER" />
          </el-select>
        </div>
      </div>
    </template>
    
    <el-table :data="list" border>
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="userNickname" label="用户" width="120" />
      <el-table-column prop="type" label="类型" width="100" />
      <el-table-column prop="content" label="反馈内容" />
      <el-table-column prop="contact" label="联系方式" width="150" />
      <el-table-column prop="status" label="状态" width="100">
        <template #default="scope">
          <el-tag :type="getStatusType(scope.row.statusCode)">
            {{ scope.row.status }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createdAt" label="创建时间" width="180" />
      <el-table-column label="操作" width="180">
        <template #default="scope">
          <el-button 
            v-if="scope.row.statusCode === 'PENDING'" 
            size="small" 
            type="primary" 
            @click="handleFeedback(scope.row)"
          >
            处理
          </el-button>
          <el-button size="small" type="danger" @click="deleteFeedback(scope.row)">
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    
    <el-pagination
      v-model:current-page="pagination.page"
      v-model:page-size="pagination.size"
      :total="pagination.total"
      :page-sizes="[10, 20, 50, 100]"
      layout="total, sizes, prev, pager, next, jumper"
      @size-change="load"
      @current-change="load"
    />
  </el-card>
</template>

<script setup>
import { onMounted, ref, reactive } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '../utils/request'

/**
 * 意见反馈视图组件
 * 提供反馈列表展示、筛选、处理和删除功能
 */
const list = ref([])

/**
 * 搜索表单数据
 * @property {string} status - 状态筛选（空字符串表示全部）
 * @property {string} type - 类型筛选（空字符串表示全部）
 */
const searchForm = reactive({
  status: '',
  type: ''
})

/**
 * 分页数据
 * @property {number} page - 当前页码
 * @property {number} size - 每页大小
 * @property {number} total - 总记录数
 */
const pagination = reactive({
  page: 1,
  size: 10,
  total: 0
})

/**
 * 加载反馈列表
 */
const load = async () => {
  const params = {
    page: pagination.page,
    size: pagination.size
  }
  if (searchForm.status) params.status = searchForm.status
  if (searchForm.type) params.type = searchForm.type
  
  const res = await request.get('/api/admin/feedback/list', { params })
  list.value = res.records || []
  pagination.total = res.total || 0
}

/**
 * 处理反馈
 * @param {Object} row - 反馈数据
 */
const handleFeedback = async (row) => {
  await request.put(`/api/admin/feedback/${row.id}/handle`, { handlerId: 1 })
  ElMessage.success('处理成功')
  load()
}

/**
 * 删除反馈
 * @param {Object} row - 反馈数据
 */
const deleteFeedback = async (row) => {
  await ElMessageBox.confirm('确定删除该反馈吗？', '提示').catch(() => { return })
  await request.delete(`/api/admin/feedback/${row.id}`)
  ElMessage.success('删除成功')
  load()
}

/**
 * 获取状态对应的标签类型
 * @param {string} status - 状态码
 * @returns {string} 标签类型
 */
const getStatusType = (status) => {
  const statusMap = {
    PENDING: 'warning',
    REPLIED: 'success'
  }
  return statusMap[status] || 'info'
}

// 组件挂载时加载数据
onMounted(load)
</script>