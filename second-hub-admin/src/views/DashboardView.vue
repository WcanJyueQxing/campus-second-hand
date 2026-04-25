<template>
  <div class="dashboard" v-loading="loading">
    <el-row :gutter="16" class="stat-cards">
      <el-col :xs="12" :sm="8" :md="4" v-for="(item, index) in cards" :key="item.label">
        <el-card class="stat-card" :class="'stat-card-' + (index + 1)" @click="handleCardClick(item.label)">
          <div class="stat-icon">
            <el-icon :size="32"><component :is="item.icon" /></el-icon>
          </div>
          <div class="stat-content">
            <div class="stat-value">{{ item.value }}</div>
            <div class="stat-label">{{ item.label }}</div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="16" class="chart-row">
      <el-col :xs="24" :sm="24" :md="14">
        <el-card class="chart-card">
          <template #header>
            <div class="card-header">
              <span>{{ trendTitle }}</span>
              <div style="display: flex; gap: 12px; align-items: center;">
                <el-radio-group v-model="trendType" size="small">
                  <el-radio-button value="user">用户</el-radio-button>
                  <el-radio-button value="goods">商品</el-radio-button>
                  <el-radio-button value="order">订单</el-radio-button>
                </el-radio-group>
                <el-radio-group v-model="timeRange" size="small">
                  <el-radio-button value="all">全部</el-radio-button>
                  <el-radio-button value="7days">近7天</el-radio-button>
                </el-radio-group>
              </div>
            </div>
          </template>
          <v-chart :option="trendChartOption" style="height: 320px" />
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="24" :md="10">
        <el-card class="chart-card">
          <template #header>
            <span>商品分类分布</span>
          </template>
          <v-chart :option="categoryChartOption" style="height: 320px" />
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="16" class="chart-row">
      <el-col :xs="24" :sm="24" :md="12">
        <el-card class="chart-card">
          <template #header>
            <span>订单状态分布</span>
          </template>
          <v-chart :option="orderStatusChartOption" style="height: 280px" />
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="24" :md="12">
        <el-card class="chart-card">
          <template #header>
            <span>用户状态分布</span>
          </template>
          <v-chart :option="userStatusChartOption" style="height: 280px" />
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { computed, onMounted, ref, watch } from 'vue'
import { User, Goods, Document, Money, Warning, Check, Clock } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import { useRouter } from 'vue-router'
import request from '../utils/request'

const router = useRouter()

const loading = ref(true)
const overview = ref({})
const trend = ref([])
const trendType = ref('user')
const timeRange = ref('7days')

const trendTitle = computed(() => {
  return timeRange.value === '7days' ? '近7日趋势' : '全部趋势'
})

const cards = computed(() => [
  { label: '用户总数', value: overview.value.userCount ?? 0, icon: User },
  { label: '商品总数', value: overview.value.goodsCount ?? 0, icon: Goods },
  { label: '待审核商品', value: overview.value.pendingGoodsCount ?? 0, icon: Clock },
  { label: '订单总数', value: overview.value.orderCount ?? 0, icon: Document },
  { label: '待处理举报', value: overview.value.reportCount ?? 0, icon: Warning }
])

const trendChartOption = computed(() => {
  const colors = {
    user: '#409eff',
    goods: '#67c23a',
    order: '#e6a23c'
  }
  const labels = {
    user: '新增用户',
    goods: '新增商品',
    order: '新增订单'
  }
  const dataKey = `${trendType.value}Count`

  return {
    tooltip: {
      trigger: 'axis',
      backgroundColor: 'rgba(255,255,255,0.95)',
      borderColor: '#e4e7ed',
      textStyle: { color: '#303133' }
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      top: '10%',
      outerBounds: {
        left: 0,
        right: 0,
        top: 0,
        bottom: 0
      }
    },
    xAxis: {
      type: 'category',
      boundaryGap: false,
      data: trend.value.map(item => item.date),
      axisLine: { lineStyle: { color: '#e4e7ed' } },
      axisLabel: { color: '#606266' }
    },
    yAxis: {
      type: 'value',
      axisLine: { show: false },
      splitLine: { lineStyle: { color: '#f0f0f0' } },
      axisLabel: { color: '#606266' }
    },
    series: [
      {
        name: labels[trendType.value],
        type: 'line',
        smooth: true,
        symbol: 'circle',
        symbolSize: 8,
        data: trend.value.map(item => item[dataKey]),
        itemStyle: { color: colors[trendType.value] },
        areaStyle: {
          color: {
            type: 'linear',
            x: 0, y: 0, x2: 0, y2: 1,
            colorStops: [
              { offset: 0, color: colors[trendType.value] + '40' },
              { offset: 1, color: colors[trendType.value] + '05' }
            ]
          }
        },
        lineStyle: { width: 3 },
        emphasis: {
          itemStyle: { borderWidth: 2, borderColor: '#fff', shadowBlur: 10 }
        }
      }
    ]
  }
})

const categoryChartOption = computed(() => {
  const colors = ['#5470c6', '#91cc75', '#fac858', '#ee6666', '#73c0de', '#3ba272', '#fc8452', '#9a60b4', '#ea7ccc']
  const data = (overview.value.categoryDistribution || []).map((item, index) => ({
    name: item.categoryName,
    value: item.goodsCount,
    itemStyle: { color: colors[index % colors.length] }
  }))

  return {
    tooltip: {
      trigger: 'item',
      backgroundColor: 'rgba(255,255,255,0.95)',
      borderColor: '#e4e7ed',
      textStyle: { color: '#303133' },
      formatter: '{b}: {c} ({d}%)'
    },
    legend: {
      orient: 'vertical',
      right: '5%',
      top: 'center',
      textStyle: { color: '#606266' }
    },
    series: [
      {
        name: '商品分类',
        type: 'pie',
        radius: ['45%', '70%'],
        center: ['35%', '50%'],
        avoidLabelOverlap: true,
        itemStyle: {
          borderRadius: 8,
          borderColor: '#fff',
          borderWidth: 2
        },
        label: {
          show: true,
          position: 'outside',
          formatter: '{b}\n{c}',
          color: '#606266'
        },
        labelLine: { length: 10, length2: 5 },
        data: data.length > 0 ? data : [{ name: '暂无数据', value: 0 }]
      }
    ]
  }
})

const orderStatusChartOption = computed(() => {
  const statusMap = {
    PENDING_PAYMENT: { name: '待支付', color: '#f56c6c' },
    PAID: { name: '已支付', color: '#409eff' },
    SHIPPED: { name: '已发货', color: '#e6a23c' },
    COMPLETED: { name: '已完成', color: '#67c23a' },
    CANCELLED: { name: '已取消', color: '#909399' }
  }

  const orderStats = overview.value.orderStatusDistribution || []
  const data = Object.entries(statusMap).map(([key, val]) => {
    const found = orderStats.find(s => s.status === key)
    return { name: val.name, value: found ? found.count : 0, itemStyle: { color: val.color } }
  })

  return {
    tooltip: {
      trigger: 'item',
      backgroundColor: 'rgba(255,255,255,0.95)',
      borderColor: '#e4e7ed',
      textStyle: { color: '#303133' },
      formatter: '{b}: {c}'
    },
    legend: {
      orient: 'horizontal',
      bottom: '0',
      textStyle: { color: '#606266' }
    },
    series: [
      {
        name: '订单状态',
        type: 'pie',
        radius: ['35%', '60%'],
        center: ['50%', '45%'],
        roseType: 'radius',
        itemStyle: {
          borderRadius: 6,
          borderColor: '#fff',
          borderWidth: 2
        },
        label: { show: true, formatter: '{b}: {c}', color: '#606266' },
        data: data.length > 0 ? data : [{ name: '暂无数据', value: 0 }]
      }
    ]
  }
})

const userStatusChartOption = computed(() => {
  const userStats = overview.value.userStatusDistribution || []
  const normalCount = userStats.find(s => s.status === 1)?.count || 0
  const disabledCount = userStats.find(s => s.status === 0)?.count || 0

  return {
    tooltip: {
      trigger: 'axis',
      backgroundColor: 'rgba(255,255,255,0.95)',
      borderColor: '#e4e7ed',
      textStyle: { color: '#303133' },
      axisPointer: { type: 'shadow' }
    },
    legend: {
      data: ['用户数量'],
      bottom: '0',
      textStyle: { color: '#606266' }
    },
    grid: { left: '3%', right: '4%', bottom: '15%', top: '10%', outerBounds: { left: 0, right: 0, top: 0, bottom: 0 } },
    xAxis: {
      type: 'category',
      data: ['正常用户', '禁用用户'],
      axisLine: { lineStyle: { color: '#e4e7ed' } },
      axisLabel: { color: '#606266' }
    },
    yAxis: {
      type: 'value',
      axisLine: { show: false },
      splitLine: { lineStyle: { color: '#f0f0f0' } },
      axisLabel: { color: '#606266' }
    },
    series: [
      {
        name: '用户数量',
        type: 'bar',
        barWidth: '40%',
        data: [
          { value: normalCount, itemStyle: { color: '#67c23a' }, label: { show: true, position: 'top' } },
          { value: disabledCount, itemStyle: { color: '#f56c6c' }, label: { show: true, position: 'top' } }
        ],
        itemStyle: {
          borderRadius: [8, 8, 0, 0]
        },
        emphasis: {
          itemStyle: { shadowBlur: 10, shadowOffsetX: 0, shadowColor: 'rgba(0,0,0,0.2)' }
        }
      }
    ]
  }
})

const load = async () => {
  loading.value = true
  try {
    const [overviewData, trendData] = await Promise.all([
      request.get('/api/admin/dashboard/overview'),
      request.get('/api/admin/dashboard/trend', { params: { timeRange: timeRange.value } })
    ])
    overview.value = overviewData || {}
    trend.value = trendData || []
  } catch (error) {
    console.error('加载数据失败:', error)
    ElMessage.error(error.message || '加载数据失败，请检查后端服务是否启动')
    overview.value = {}
    trend.value = []
  } finally {
    loading.value = false
  }
}

onMounted(load)

watch([timeRange, trendType], () => {
  load()
}, { immediate: false })

const handleCardClick = (label) => {
  switch (label) {
    case '用户总数':
      router.push('/users')
      break
    case '商品总数':
      router.push('/goods-audit')
      break
    case '待审核商品':
      router.push('/goods-audit')
      break
    case '订单总数':
      router.push('/orders')
      break
    case '待处理举报':
      router.push('/reports')
      break
  }
}
</script>

<style scoped>
.dashboard {
  padding: 0;
}

.stat-cards {
  margin-bottom: 16px;
}

.stat-card {
  margin-bottom: 12px;
  border-radius: 12px;
  transition: all 0.3s;
}

.stat-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.1);
  cursor: pointer;
}

.stat-card :deep(.el-card__body) {
  display: flex;
  align-items: center;
  padding: 16px;
}

.stat-icon {
  width: 56px;
  height: 56px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 16px;
}

.stat-card-1 .stat-icon { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: #fff; }
.stat-card-2 .stat-icon { background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%); color: #fff; }
.stat-card-3 .stat-icon { background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%); color: #fff; }
.stat-card-4 .stat-icon { background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%); color: #fff; }
.stat-card-5 .stat-icon { background: linear-gradient(135deg, #fa709a 0%, #fee140 100%); color: #fff; }

.stat-content {
  flex: 1;
}

.stat-value {
  font-size: 28px;
  font-weight: 700;
  color: #303133;
  line-height: 1.2;
}

.stat-label {
  font-size: 13px;
  color: #909399;
  margin-top: 4px;
}

.chart-row {
  margin-bottom: 16px;
}

.chart-card {
  border-radius: 12px;
}

.chart-card :deep(.el-card__header) {
  padding: 14px 20px;
  border-bottom: 1px solid #f0f0f0;
  font-weight: 600;
  color: #303133;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.card-header span {
  font-weight: 600;
  color: #303133;
}
</style>