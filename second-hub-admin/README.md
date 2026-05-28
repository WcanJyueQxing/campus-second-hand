# Second Hub Admin - 校园二手交易平台管理后台

> 校园二手交易平台的管理员后台系统，提供全面的运营管理能力。

## 📖 项目简介

Second Hub Admin 是校园二手交易平台的 Web 管理后台，为平台管理员提供商品审核、用户管理、订单监管、数据统计等功能，助力平台高效运营。

### 核心功能

- 📊 **数据仪表盘**：实时展示用户、商品、订单等核心数据
- ✅ **商品审核**：审核用户发布的商品，保障平台内容质量
- 👥 **用户管理**：管理平台用户，禁用/启用账号
- 🛒 **订单监管**：监管平台交易订单，支持强制取消
- 📢 **内容管理**：发布系统公告，管理商品分类
- 🔔 **反馈处理**：处理用户意见反馈和举报信息

---

## 🛠 技术栈

| 技术 | 版本 | 说明 |
|------|------|------|
| Vue | 3.5.x | 前端框架 |
| Vue Router | 4.5.x | 路由管理 |
| Pinia | 3.0.x | 状态管理 |
| Element Plus | 2.9.x | UI 组件库 |
| ECharts | 6.0.x | 数据可视化图表 |
| Vue-ECharts | 8.0.x | ECharts Vue 封装 |
| Axios | 1.9.x | HTTP 请求库 |
| Vite | 5.0.x | 构建工具 |

---

## 📁 项目结构

```
second-hub-admin/
├── public/                         # 静态资源
│   └── favicon.ico                 # 网站图标
├── src/                           # 源代码
│   ├── main.js                    # 应用入口
│   ├── App.vue                    # 根组件
│   ├── assets/                    # 资源文件
│   │   ├── base.css               # 基础样式
│   │   ├── logo.svg               # Logo
│   │   └── main.css               # 主样式
│   ├── components/                # 公共组件
│   │   ├── ChartComponent.vue     # 图表组件
│   │   ├── HelloWorld.vue         # 欢迎组件
│   │   ├── TheWelcome.vue         # 欢迎界面
│   │   ├── WelcomeItem.vue        # 欢迎条目
│   │   └── icons/                # 图标组件
│   ├── layout/                    # 布局组件
│   │   └── AdminLayout.vue       # 后台主布局
│   ├── router/                    # 路由配置
│   │   └── index.js              # 路由定义
│   ├── stores/                    # 状态管理
│   │   └── auth.js               # 认证状态
│   ├── styles/                    # 样式文件
│   │   └── global.css            # 全局样式
│   ├── utils/                     # 工具函数
│   │   └── request.js            # HTTP 请求封装
│   └── views/                     # 页面视图
│       ├── LoginView.vue          # 登录页面
│       ├── DashboardView.vue      # 仪表盘首页
│       ├── GoodsAuditView.vue     # 商品审核
│       ├── GoodsManageView.vue    # 商品管理
│       ├── UserView.vue           # 用户管理
│       ├── CategoryView.vue       # 分类管理
│       ├── OrderView.vue          # 订单监管
│       ├── ReportView.vue         # 举报处理
│       ├── NoticeView.vue         # 公告管理
│       └── FeedbackView.vue       # 意见反馈
├── index.html                     # HTML 入口
├── package.json                   # 项目配置
├── vite.config.js                 # Vite 配置
├── jsconfig.json                  # JS 配置
└── README.md                      # 项目说明文档
```

---

## 🚀 快速开始

### 环境要求

- Node.js >= 16.x
- npm >= 8.x

### 安装依赖

```bash
npm install
```

### 开发模式

```bash
npm run dev
```

启动后访问 `http://localhost:5173`

### 生产构建

```bash
npm run build
```

构建产物将输出到 `dist` 目录。

### 预览构建结果

```bash
npm run preview
```

---

## 📱 页面功能

### 1. 登录页 (`/login`)

管理员登录入口，支持：
- 账号密码登录
- 图形验证码验证
- 登录状态持久化

**默认管理员账号**：
- 用户名：`admin`
- 密码：`admin123`

### 2. 仪表盘 (`/dashboard`)

系统数据概览和可视化分析：

| 功能 | 说明 |
|------|------|
| 统计卡片 | 用户总数、商品总数、待审核商品、订单总数、待处理举报 |
| 趋势图表 | 用户/商品/订单趋势，支持近7天/全部切换 |
| 分类分布 | 商品分类饼图展示 |
| 订单状态 | 订单各状态分布玫瑰图 |
| 用户状态 | 用户正常/禁用柱状图 |
| 快捷跳转 | 点击卡片跳转到对应管理页面 |

### 3. 商品审核 (`/goods-audit`)

待审核商品管理：

| 功能 | 说明 |
|------|------|
| 待审核列表 | 展示待审核商品信息 |
| 审核通过 | 批准商品上架销售 |
| 审核拒绝 | 拒绝商品并填写原因 |
| 商品下架 | 直接下架违规商品 |

### 4. 商品管理 (`/goods-manage`)

全部商品管理：

| 功能 | 说明 |
|------|------|
| 商品列表 | 展示所有商品，支持筛选 |
| 图片预览 | 点击图片放大查看 |
| 商品下架 | 强制下架商品 |
| 商品删除 | 删除违规商品 |

### 5. 用户管理 (`/users`)

平台用户管理：

| 功能 | 说明 |
|------|------|
| 用户列表 | 展示所有用户信息 |
| 头像显示 | 显示用户头像，支持默认头像 |
| 用户禁用 | 禁用违规用户 |
| 用户启用 | 恢复被禁用用户 |

### 6. 订单监管 (`/orders`)

平台订单监管：

| 功能 | 说明 |
|------|------|
| 订单列表 | 展示所有订单，支持状态筛选 |
| 图片预览 | 点击商品图片放大查看 |
| 强制取消 | 管理员强制取消订单 |

### 7. 分类管理 (`/categories`)

商品分类管理：

| 功能 | 说明 |
|------|------|
| 分类列表 | 展示所有商品分类 |
| 添加分类 | 创建新商品分类 |
| 修改分类 | 编辑分类信息 |
| 删除分类 | 删除不需要的分类 |

### 8. 公告管理 (`/notices`)

系统公告管理：

| 功能 | 说明 |
|------|------|
| 公告列表 | 展示所有公告 |
| 发布公告 | 创建新系统公告 |
| 删除公告 | 删除过期公告 |

### 9. 举报处理 (`/reports`)

用户举报处理：

| 功能 | 说明 |
|------|------|
| 举报列表 | 展示用户举报信息 |
| 处理举报 | 审核并处理举报内容 |

### 10. 意见反馈 (`/feedback`)

用户反馈管理：

| 功能 | 说明 |
|------|------|
| 反馈列表 | 展示用户反馈，支持状态筛选 |
| 处理反馈 | 回复用户反馈 |
| 删除反馈 | 删除已处理的反馈 |

---

## 🔌 接口对接

### 后端地址配置

请求库默认后端地址：`http://127.0.0.1:8080`

如需修改，编辑 `src/utils/request.js` 文件：

```javascript
const service = axios.create({
  baseURL: 'http://127.0.0.1:8080',  // 修改此处
  timeout: 15000
})
```

### 认证说明

- 登录接口无需认证
- 其他接口需要在请求头中携带 Token
- Token 存储在浏览器 `localStorage` 中
- Token 过期或无效时自动跳转到登录页

### 接口文档

详细接口文档请参考：[管理后台接口文档](../管理后台接口文档.md)

---

## 📋 路由配置

| 路由路径 | 页面组件 | 功能描述 |
|---------|---------|---------|
| `/login` | LoginView.vue | 管理员登录页 |
| `/dashboard` | DashboardView.vue | 仪表盘首页 |
| `/goods-audit` | GoodsAuditView.vue | 商品审核页 |
| `/goods-manage` | GoodsManageView.vue | 商品管理页 |
| `/users` | UserView.vue | 用户管理页 |
| `/categories` | CategoryView.vue | 分类管理页 |
| `/orders` | OrderView.vue | 订单监管页 |
| `/reports` | ReportView.vue | 举报处理页 |
| `/notices` | NoticeView.vue | 公告管理页 |
| `/feedback` | FeedbackView.vue | 意见反馈页 |

### 路由守卫

系统实现了路由守卫功能：
- 未登录访问非登录页 → 重定向到登录页
- 已登录访问登录页 → 重定向到仪表盘
- Token 失效 → 自动清除并跳转登录页

---

## 🎨 样式说明

### 全局样式

全局样式文件位于 `src/styles/global.css`，包含：
- CSS 变量定义
- 重置样式
- 通用工具类

### 组件样式

组件采用 Vue 3 `<style scoped>` 方式编写，保证样式隔离。

---

## 🔧 开发指南

### 添加新页面

1. 在 `src/views/` 目录创建 Vue 组件
2. 在 `src/router/index.js` 中添加路由配置
3. 在侧边栏 `AdminLayout.vue` 中添加菜单项

### 添加新接口

1. 在 `src/utils/request.js` 中按需添加请求方法
2. 或在组件中直接调用 `request.get()` / `request.post()` 等方法

### 状态管理

使用 Pinia 进行状态管理：
- `src/stores/auth.js`：管理认证状态（Token、用户信息）

