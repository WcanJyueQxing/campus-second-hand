# Second Hub Admin - 管理后台

## 项目简介

`second-hub-admin` 是校园二手交易平台的管理后台，基于 Vue 3 + Element Plus 构建，提供商品审核、用户管理、订单管理等运营功能。

## 技术栈

| 类别 | 技术 |
|------|------|
| 框架 | Vue 3.5.31 |
| UI 组件库 | Element Plus 2.9.10 |
| 状态管理 | Pinia 3.0.3 |
| 路由 | Vue Router 4.5.1 |
| HTTP 客户端 | Axios 1.9.0 |
| 图表 | Apache ECharts 5.x + vue-echarts |
| 构建工具 | Vite 5.0 |
| Node 版本 | 16+ |

## 项目结构

```
second-hub-admin/
├── src/
│   ├── assets/          # 静态资源
│   ├── components/      # 公共组件
│   │   └── ChartComponent.vue  # 图表组件
│   ├── layout/          # 布局组件
│   │   └── AdminLayout.vue     # 管理后台布局
│   ├── router/          # 路由配置
│   │   └── index.js
│   ├── stores/          # 状态管理
│   │   └── auth.js      # 认证状态
│   ├── styles/          # 全局样式
│   ├── utils/           # 工具函数
│   │   └── request.js   # Axios 封装
│   ├── views/           # 页面组件
│   │   ├── LoginView.vue        # 登录页
│   │   ├── DashboardView.vue    # 仪表盘
│   │   ├── GoodsAuditView.vue   # 商品审核
│   │   ├── CategoryView.vue     # 分类管理
│   │   ├── UserView.vue         # 用户管理
│   │   ├── OrderView.vue        # 订单管理
│   │   ├── ReportView.vue       # 举报管理
│   │   └── NoticeView.vue       # 公告管理
│   ├── App.vue
│   └── main.js
├── public/
├── package.json
└── vite.config.js
```

## 功能模块

### 1. 登录认证
- 管理员用户名密码登录
- JWT Token 认证
- 自动登录状态保持

### 2. 仪表盘 (Dashboard)
- 统计数据卡片（用户总数、商品总数、待审核、订单总数、待处理举报）
- 近7日趋势图（折线图 - ECharts）
- 商品分类分布（饼图 - ECharts）

### 3. 商品审核
- 待审核商品列表
- 商品详情查看
- 审核通过/驳回操作
- 驳回原因填写

### 4. 分类管理
- 商品分类列表
- 新增/编辑/删除分类
- 分类排序

### 5. 用户管理
- 用户列表
- 用户状态查看
- 禁用/启用用户

### 6. 订单管理
- 订单列表
- 订单状态筛选
- 订单详情查看

### 7. 举报管理
- 举报列表
- 举报详情查看
- 处理举报（通过/驳回）
- 处理结果填写

### 8. 公告管理
- 公告列表
- 发布/编辑/删除公告
- 公告状态管理

## 页面预览

### 登录页
- 简洁的卡片式登录表单
- 用户名/密码输入
- 登录按钮

### 仪表盘
- 顶部统计数据卡片（5个指标）
- 中部趋势图（7日数据）
- 右侧分类饼图

### 列表页通用特性
- Element Plus Table 表格
- 分页组件
- 搜索筛选
- 操作按钮

## API 对接

### 请求封装

项目使用 Axios 封装请求，位于 `src/utils/request.js`：

```javascript
const BASE_URL = 'http://127.0.0.1:8080'

// 自动携带 Token
// 统一错误处理
// 返回 data.data 部分
```

### 认证机制

登录成功后，Token 存储在 `localStorage`：

```javascript
localStorage.setItem('admin_token', token)
localStorage.setItem('admin_nickname', nickname)
```

请求时自动在 Header 携带：

```javascript
Authorization: Bearer <token>
```

## 启动步骤

### 前置条件

1. **安装 Node.js 16+**
2. **后端服务已启动** (http://127.0.0.1:8080)

### 安装依赖

```bash
cd second-hub-admin
npm install
```

### 启动开发服务器

```bash
npm run dev
```

访问 http://localhost:5173

### 构建生产版本

```bash
npm run build
```

## 目录规范

### Views 页面规范

每个页面文件应包含：

```vue
<template>
  <el-card>
    <template #header>页面标题</template>
    <!-- 页面内容 -->
  </el-card>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import request from '../utils/request'

// 响应式数据
const data = ref([])

// 加载数据
const load = async () => {
  data.value = await request.get('/api/admin/xxx/list')
}

// 生命周期
onMounted(load)
</script>
```

### 组件规范

公共组件放在 `src/components/` 目录，使用 PascalCase 命名。

### 状态管理

使用 Pinia 管理全局状态，当前有：

- `stores/auth.js` - 认证状态（Token、用户信息）

## ECharts 图表使用

### 全局注册

在 `main.js` 中已注册 `v-chart` 全局组件：

```javascript
import VChart from 'vue-echarts'
app.component('v-chart', VChart)
```

### 使用示例

```vue
<v-chart :option="chartOption" style="height: 300px" />
```

```javascript
const chartOption = {
  tooltip: { trigger: 'axis' },
  xAxis: { type: 'category', data: ['Mon', 'Tue'] },
  yAxis: { type: 'value' },
  series: [{ data: [120, 200], type: 'line' }]
}
```

## 注意事项

1. **跨域问题**：开发环境下 Vite 已配置代理，生产环境需后端配置 CORS
2. **Token 过期**：Token 过期后需重新登录
3. **错误处理**：统一通过 `request.js` 处理错误提示

## 默认账号

| 用户名 | 密码 |
|--------|------|
| admin | 123456 |

## 联系方式

如有问题，请查阅项目文档或联系开发团队。
