<template>
  <el-card>
    <template #header>分类管理</template>
    <div style="margin-bottom: 16px; display: flex; gap: 8px">
      <el-input v-model="form.name" placeholder="分类名" style="width: 200px" />
      <el-input-number v-model="form.sort" :min="0" />
      <el-select v-model="form.status" style="width: 120px">
        <el-option :value="1" label="启用" />
        <el-option :value="0" label="停用" />
      </el-select>
      <el-button type="primary" @click="save">新增</el-button>
    </div>

    <el-table :data="list" border>
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="name" label="分类名" />
      <el-table-column prop="sort" label="排序" width="100" />
      <el-table-column prop="status" label="状态" width="100">
        <template #default="scope">{{ scope.row.status === 1 ? '启用' : '停用' }}</template>
      </el-table-column>
      <el-table-column label="操作" width="180">
        <template #default="scope">
          <el-button size="small" type="danger" @click="remove(scope.row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
  </el-card>
</template>

<script setup>
import { onMounted, reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import request from '../utils/request'

/**
 * 分类管理视图组件
 * 提供商品分类的新增、删除和列表展示功能
 */
const list = ref([])

/**
 * 分类表单数据
 * @property {string} name - 分类名称
 * @property {number} sort - 排序值
 * @property {number} status - 状态（1-启用，0-停用）
 */
const form = reactive({ name: '', sort: 0, status: 1 })

/**
 * 加载分类列表
 */
const load = async () => {
  list.value = await request.get('/api/admin/categories')
}

/**
 * 保存分类
 */
const save = async () => {
  if (!form.name) return
  await request.post('/api/admin/categories', form)
  form.name = ''
  ElMessage.success('保存成功')
  load()
}

/**
 * 删除分类
 * @param {number} id - 分类ID
 */
const remove = async (id) => {
  await request.delete(`/api/admin/categories/${id}`)
  ElMessage.success('删除成功')
  load()
}

// 组件挂载时加载数据
onMounted(load)
</script>