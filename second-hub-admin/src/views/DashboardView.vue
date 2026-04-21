<template>
  <div>
    <el-row :gutter="16">
      <el-col :span="4" v-for="item in cards" :key="item.label">
        <el-card>
          <div>{{ item.label }}</div>
          <h2>{{ item.value }}</h2>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="16" style="margin-top: 16px">
      <el-col :span="16">
        <el-card>
          <template #header>近7日趋势</template>
          <v-chart :option="trendChartOption" style="height: 350px" />
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card>
          <template #header>商品分类分布</template>
          <v-chart :option="categoryChartOption" style="height: 350px" />
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { computed, onMounted, ref } from 'vue'
import request from '../utils/request'

const overview = ref({})
const trend = ref([])

const cards = computed(() => [
  { label: '用户总数', value: overview.value.userCount || 0 },
  { label: '商品总数', value: overview.value.goodsCount || 0 },
  { label: '待审核商品', value: overview.value.pendingGoodsCount || 0 },
  { label: '订单总数', value: overview.value.orderCount || 0 },
  { label: '待处理举报', value: overview.value.reportCount || 0 }
])

const trendChartOption = computed(() => ({
  tooltip: {
    trigger: 'axis'
  },
  legend: {
    data: ['新增用户', '新增商品', '新增订单']
  },
  grid: {
    left: '3%',
    right: '4%',
    bottom: '3%',
    containLabel: true
  },
  xAxis: {
    type: 'category',
    boundaryGap: false,
    data: trend.value.map(item => item.date)
  },
  yAxis: {
    type: 'value'
  },
  series: [
    {
      name: '新增用户',
      type: 'line',
      smooth: true,
      data: trend.value.map(item => item.userCount),
      itemStyle: { color: '#409eff' }
    },
    {
      name: '新增商品',
      type: 'line',
      smooth: true,
      data: trend.value.map(item => item.goodsCount),
      itemStyle: { color: '#67c23a' }
    },
    {
      name: '新增订单',
      type: 'line',
      smooth: true,
      data: trend.value.map(item => item.orderCount),
      itemStyle: { color: '#e6a23c' }
    }
  ]
}))

const categoryChartOption = computed(() => ({
  tooltip: {
    trigger: 'item',
    formatter: '{b}: {c} ({d}%)'
  },
  legend: {
    orient: 'vertical',
    left: 'left'
  },
  series: [
    {
      name: '商品分类',
      type: 'pie',
      radius: ['40%', '70%'],
      avoidLabelOverlap: false,
      itemStyle: {
        borderRadius: 10,
        borderColor: '#fff',
        borderWidth: 2
      },
      label: {
        show: true,
        formatter: '{b}: {c}'
      },
      data: overview.value.categoryDistribution || []
    }
  ]
}))

const load = async () => {
  overview.value = await request.get('/api/admin/dashboard/overview')
  trend.value = await request.get('/api/admin/dashboard/trend')
}

onMounted(load)
</script>
