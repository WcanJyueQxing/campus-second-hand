# Second Hub - 校园二手交易平台微信小程序

> 校园二手交易平台的微信小程序客户端，为在校学生提供便捷的闲置物品交易服务。

## 📖 项目简介

Second Hub 是一款基于微信小程序的校园二手交易平台，用户可以轻松发布闲置物品、浏览商品、收藏商品、下单购买、管理订单等，助力校园内绿色消费和资源循环利用。

### 核心功能

- 🛍️ **商品交易**：发布、浏览、购买二手商品
- ❤️ **收藏管理**：收藏心仪商品，便捷管理
- 📦 **订单管理**：完整的订单流程，清晰的订单状态
- 👤 **个人中心**：个人信息管理、订单查看
- 🔍 **商品搜索**：关键词搜索、分类筛选
- 📝 **互动评论**：商品评论、查看历史

---

## 🛠 技术栈

### 框架与平台

| 技术 | 版本 | 说明 |
|------|------|------|
| 微信小程序 | 原生框架 | 微信小程序开发 |
| 微信基础库 | 3.0.2 | 小程序基础 API |
| ES6+ | - | JavaScript 语法支持 |

### UI 组件

| 技术 | 说明 |
|------|------|
| WeUI | 微信原生样式库 |
| 自定义 TabBar | 自定义底部导航栏 |

### 网络与工具

| 技术 | 说明 |
|------|------|
| wx.request | 微信网络请求 |
| wx.uploadFile | 文件上传 |
| localStorage | 本地数据存储 |

### 后端对接

| 技术 | 说明 |
|------|------|
| RESTful API | 后端接口风格 |
| JWT Token | 身份认证 |

---

## 📁 项目结构

```
second-hub-client/
├── app.js                          # 小程序入口文件
├── app.json                        # 小程序全局配置
├── app.wxss                        # 小程序全局样式
├── project.config.json              # 项目配置文件
├── sitemap.json                     # 站点地图配置
├── .editorconfig                    # 编辑器配置
├── .eslintrc.js                    # ESLint 配置
│
├── custom-tab-bar/                # 自定义底部导航栏
│   ├── index.js
│   └── index.json
│
├── images/                         # 图片资源
│   ├── default-avatar.svg         # 默认头像
│   └── page.js                    # 页面占位图
│
├── pages/                          # 小程序页面
│   ├── index/                    # 默认首页
│   │   ├── index.js
│   │   ├── index.json
│   │   ├── index.wxml
│   │   └── index.wxss
│   │
│   ├── home/                     # 首页（商品列表）
│   │   ├── home.js
│   │   ├── home.json
│   │   ├── home.wxml
│   │   └── home.wxss
│   │
│   ├── goods-publish/            # 商品发布/编辑
│   │   ├── goods-publish.js
│   │   ├── goods-publish.json
│   │   ├── goods-publish.wxml
│   │   └── goods-publish.wxss
│   │
│   ├── goods-detail/             # 商品详情
│   │   ├── goods-detail.js
│   │   ├── goods-detail.json
│   │   ├── goods-detail.wxml
│   │   └── goods-detail.wxss
│   │
│   ├── orders/                   # 订单列表
│   │   ├── orders.js
│   │   ├── orders.json
│   │   ├── orders.wxml
│   │   └── orders.wxss
│   │
│   ├── order-detail/             # 订单详情
│   │   ├── order-detail.js
│   │   ├── order-detail.json
│   │   ├── order-detail.wxml
│   │   └── order-detail.wxss
│   │
│   ├── my-goods/                 # 我的商品
│   │   ├── my-goods.js
│   │   ├── my-goods.json
│   │   ├── my-goods.wxml
│   │   └── my-goods.wxss
│   │
│   ├── my-sold/                  # 我卖出的
│   │   ├── my-sold.js
│   │   ├── my-sold.json
│   │   ├── my-sold.wxml
│   │   └── my-sold.wxss
│   │
│   ├── my-bought/                # 我买到的
│   │   ├── my-bought.js
│   │   ├── my-bought.json
│   │   ├── my-bought.wxml
│   │   └── my-bought.wxss
│   │
│   ├── my-reviews/               # 待评价
│   │   ├── my-reviews.js
│   │   ├── my-reviews.json
│   │   ├── my-reviews.wxml
│   │   └── my-reviews.wxss
│   │
│   ├── comment/                  # 商品评论
│   │   ├── comment.js
│   │   ├── comment.json
│   │   ├── comment.wxml
│   │   └── comment.wxss
│   │
│   ├── favorites/                # 收藏列表
│   │   ├── favorites.js
│   │   ├── favorites.json
│   │   ├── favorites.wxml
│   │   └── favorites.wxss
│   │
│   ├── history/                  # 浏览历史
│   │   ├── history.js
│   │   ├── history.json
│   │   ├── history.wxml
│   │   └── history.wxss
│   │
│   ├── login/                    # 登录页面
│   │   ├── login.js
│   │   ├── login.json
│   │   ├── login.wxml
│   │   └── login.wxss
│   │
│   ├── mine/                     # 个人中心
│   │   ├── mine.js
│   │   ├── mine.json
│   │   ├── mine.wxml
│   │   └── mine.wxss
│   │
│   ├── profile-edit/              # 编辑个人信息
│   │   ├── profile-edit.js
│   │   ├── profile-edit.json
│   │   ├── profile-edit.wxml
│   │   └── profile-edit.wxss
│   │
│   ├── address/                  # 地址列表
│   │   ├── address.js
│   │   ├── address.json
│   │   ├── address.wxml
│   │   └── address.wxss
│   │
│   ├── address-edit/             # 地址编辑
│   │   ├── address-edit.js
│   │   ├── address-edit.json
│   │   ├── address-edit.wxml
│   │   └── address-edit.wxss
│   │
│   ├── settings/                 # 设置页面
│   │   ├── settings.js
│   │   ├── settings.json
│   │   ├── settings.wxml
│   │   └── settings.wxss
│   │
│   ├── privacy-settings/          # 隐私设置
│   │   ├── privacy-settings.js
│   │   ├── privacy-settings.json
│   │   ├── privacy-settings.wxml
│   │   └── privacy-settings.wxss
│   │
│   ├── notification-settings/    # 通知设置
│   │   ├── notification-settings.js
│   │   ├── notification-settings.json
│   │   ├── notification-settings.wxml
│   │   └── notification-settings.wxss
│   │
│   ├── feedback/                # 意见反馈
│   │   ├── feedback.js
│   │   ├── feedback.json
│   │   ├── feedback.wxml
│   │   └── feedback.wxss
│   │
│   ├── account-security/          # 账户安全
│   │   ├── account-security.js
│   │   ├── account-security.json
│   │   ├── account-security.wxml
│   │   └── account-security.wxss
│   │
│   ├── change-password/         # 修改密码
│   │   ├── change-password.js
│   │   ├── change-password.json
│   │   ├── change-password.wxml
│   │   └── change-password.wxss
│   │
│   ├── change-phone/             # 修改手机号
│   │   ├── change-phone.js
│   │   ├── change-phone.json
│   │   ├── change-phone.wxml
│   │   └── change-phone.wxss
│   │
│   ├── about-us/                 # 关于我们
│   │   ├── about-us.js
│   │   ├── about-us.json
│   │   ├── about-us.wxml
│   │   └── about-us.wxss
│   │
│   ├── random-name/              # 随机姓名
│   │   ├── random-name.js
│   │   ├── random-name.json
│   │   ├── random-name.wxml
│   │   └── random-name.wxss
│   │
│   └── logs/                     # 日志页面
│       ├── logs.js
│       ├── logs.json
│       ├── logs.wxml
│       └── logs.wxss
│
└── utils/                         # 工具函数
    ├── request.js                # 网络请求封装
    └── util.js                   # 通用工具函数
```

---

## 🚀 快速开始

### 环境要求

- 微信开发者工具 >= 1.06.0
- 微信小程序 AppID

### 1. 获取项目

```bash
# 克隆项目
git clone <repository-url>

# 进入项目目录
cd second-hub-client
```

### 2. 配置项目

1. 打开微信开发者工具
2. 导入项目目录 `second-hub-client`
3. 填写小程序 AppID
4. 配置后端接口地址

### 3. 修改接口地址

编辑 `utils/request.js` 文件，修改后端服务地址：

```javascript
const BASE_URL = 'http://127.0.0.1:8080'  // 修改为实际后端地址
```

### 4. 启动开发

在微信开发者工具中，点击"编译"按钮启动项目。

---

## 📱 页面功能

### 底部导航栏

| 页面 | 图标 | 说明 |
|------|------|------|
| 首页 | 首页图标 | 商品列表、搜索、分类 |
| 发布 | 加号图标 | 发布/编辑商品 |
| 订单 | 订单图标 | 订单管理 |
| 我的 | 我的图标 | 个人中心 |

### 主要页面

#### 1. 首页 (`/pages/home/home`)

商品展示首页：

| 功能 | 说明 |
|------|------|
| 商品列表 | 分页展示商品，支持下拉刷新 |
| 分类导航 | 顶部分类标签，点击筛选 |
| 搜索功能 | 关键词搜索商品 |
| 商品卡片 | 显示封面、价格、标题 |

#### 2. 商品详情 (`/pages/goods-detail/goods-detail`)

商品详细信息页：

| 功能 | 说明 |
|------|------|
| 图片轮播 | 商品图片展示 |
| 基本信息 | 标题、价格、发布时间 |
| 商品描述 | 详细商品说明 |
| 卖家信息 | 卖家昵称、头像 |
| 收藏功能 | 切换收藏状态 |
| 评论列表 | 查看商品评论 |
| 发表评论 | 对商品进行评论 |
| 立即购买 | 创建订单购买 |

#### 3. 商品发布 (`/pages/goods-publish/goods-publish`)

发布/编辑商品页：

| 功能 | 说明 |
|------|------|
| 分类选择 | 选择商品分类 |
| 商品信息 | 标题、描述、价格 |
| 商品数量 | 设置库存数量 |
| 图片上传 | 支持多图上传（最多6张） |
| 表单验证 | 必填项校验 |
| 编辑模式 | 支持编辑已有商品 |

#### 4. 订单管理 (`/pages/orders/orders`)

订单列表页：

| 功能 | 说明 |
|------|------|
| 角色切换 | 买家视角/卖家视角 |
| 订单列表 | 分页展示订单 |
| 状态筛选 | 按订单状态筛选 |
| 订单卡片 | 显示商品、金额、状态 |
| 订单操作 | 支付/确认/取消 |

#### 5. 收藏列表 (`/pages/favorites/favorites`)

收藏商品管理：

| 功能 | 说明 |
|------|------|
| 收藏列表 | 展示收藏的商品 |
| 取消收藏 | 取消已收藏商品 |
| 跳转详情 | 点击跳转商品详情 |

#### 6. 浏览历史 (`/pages/history/history`)

历史浏览记录：

| 功能 | 说明 |
|------|------|
| 历史列表 | 按日期分组显示 |
| 删除记录 | 删除单条记录 |
| 清空历史 | 一键清空所有 |
| 跳转详情 | 点击跳转商品详情 |

#### 7. 登录页面 (`/pages/login/login`)

用户登录注册：

| 功能 | 说明 |
|------|------|
| 账号登录 | 用户名+密码+验证码 |
| 微信登录 | 一键微信授权登录 |
| 用户注册 | 新用户注册账号 |
| 随机昵称 | 自动生成用户名 |

#### 8. 个人中心 (`/pages/mine/mine`)

用户个人信息：

| 功能 | 说明 |
|------|------|
| 用户信息 | 头像、昵称展示 |
| 统计数字 | 发布/卖出/购买数量 |
| 功能入口 | 跳转各功能页面 |
| 设置入口 | 跳转到设置页面 |

### 其他页面

| 页面 | 说明 |
|------|------|
| 订单详情 | 查看订单详细信息 |
| 我的商品 | 管理已发布的商品 |
| 我卖出的 | 卖家视角订单列表 |
| 我买到的 | 买家视角订单列表 |
| 待评价 | 待评价订单列表 |
| 商品评论 | 评论列表和发表 |
| 地址管理 | 收货地址增删改查 |
| 编辑资料 | 修改个人信息 |
| 账户安全 | 修改密码、手机号 |
| 隐私设置 | 隐私相关设置 |
| 通知设置 | 通知偏好设置 |
| 意见反馈 | 提交问题建议 |
| 关于我们 | 应用相关信息 |

---

## 🔧 配置文件

### app.json 全局配置

```json
{
  "pages": [...],
  "window": {
    "navigationBarTitleText": "Second Hub",
    "navigationBarBackgroundColor": "#ffffff",
    "backgroundColor": "#f4f7fc"
  },
  "tabBar": {
    "custom": true,
    "color": "#8a94a6",
    "selectedColor": "#2f7df6",
    "backgroundColor": "#ffffff",
    "list": [...]
  }
}
```

### project.config.json 项目配置

```json
{
  "compileType": "miniprogram",
  "libVersion": "3.0.2",
  "appid": "your-appid"
}
```

---

## 📡 接口对接

### 后端地址

默认后端地址：`http://127.0.0.1:8080`

### 请求封装

项目使用封装的网络请求工具，位于 `utils/request.js`：

```javascript
const { request, uploadFile } = require('../../utils/request')

// GET请求
request({
  url: '/api/user/goods/list',
  method: 'GET',
  data: { pageNo: 1, pageSize: 10 }
})

// POST请求
request({
  url: '/api/user/goods',
  method: 'POST',
  data: { title: '商品标题', price: 99.00 }
})

// 文件上传
uploadFile(filePath).then(url => {
  console.log('上传成功:', url)
})
```

### 接口文档

详细接口文档请参考：[小程序接口文档](../小程序接口文档.md)

---

## 💾 本地存储

| 键名 | 说明 | 示例 |
|------|------|------|
| `token` | 用户登录Token | JWT Token字符串 |
| `userInfo` | 用户信息对象 | `{ id, nickname, avatarUrl, phone }` |
| `goods_publish_edit_goods_id` | 编辑商品ID | 商品ID数字 |

---

## 🎨 样式规范

### 全局样式

| 样式 | 说明 |
|------|------|
| 主题色 | `#2f7df6` 蓝色 |
| 文字色 | `#303133` 深色 |
| 辅助色 | `#909399` 灰色 |
| 背景色 | `#f4f7fc` 浅灰 |

### 组件样式

- 使用 `<style scoped>` 保证样式隔离
- 使用 WeUI 组件库保持一致性

---

## 📋 开发指南

### 新增页面

1. 在 `pages/` 目录下创建页面文件夹
2. 创建 `.js`、`.json`、`.wxml`、`.wxss` 四个文件
3. 在 `app.json` 的 `pages` 数组中添加页面路径
4. 注册到 TabBar 或路由

### 添加接口

1. 编辑 `utils/request.js`
2. 或在页面中直接调用 `request()` 方法

### 样式编写

```wxss
/* page.wxss */
.page-container {
  padding: 20rpx;
  background-color: #f4f7fc;
}

.page-title {
  font-size: 32rpx;
  font-weight: bold;
  color: #303133;
}
```

## 
