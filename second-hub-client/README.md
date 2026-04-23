# 校园二手交易平台 - 微信小程序客户端

## 项目简介

`second-hub-client` 是校园二手交易平台的微信小程序客户端，基于原生小程序框架开发，为在校师生提供商品浏览、发布、交易、互动等核心功能。

## 技术栈

| 技术 | 说明 |
|------|------|
| 微信小程序 | 原生框架 |
| JavaScript | 核心语言 |
| WXSS | 样式语言 |
| WXML | 模板语言 |
| Vant Weapp | UI组件库（可选） |

## 项目结构

```
second-hub-client/
├── pages/                          # 页面目录
│   ├── index/                      # 首页
│   │   ├── index.js               # 首页逻辑
│   │   └── index.json             # 首页配置
│   ├── home/                       # 商品首页
│   │   ├── home.js                # 商品列表逻辑
│   │   └── home.json              # 首页配置
│   ├── login/                      # 登录页
│   │   ├── login.js               # 登录逻辑
│   │   ├── login.wxml             # 登录模板
│   │   └── login.wxss             # 登录样式
│   ├── goods-detail/               # 商品详情
│   │   ├── goods-detail.js        # 商品详情逻辑
│   │   └── goods-detail.json      # 商品详情配置
│   ├── goods-publish/              # 商品发布
│   │   ├── goods-publish.js        # 发布逻辑
│   │   └── goods-publish.json      # 发布配置
│   ├── my-goods/                   # 我的发布
│   │   ├── my-goods.js            # 我的发布逻辑
│   │   └── my-goods.json          # 我的发布配置
│   ├── orders/                     # 订单中心
│   │   ├── orders.js              # 订单逻辑
│   │   └── orders.json            # 订单配置
│   ├── favorites/                  # 我的收藏
│   │   ├── favorites.js           # 收藏逻辑
│   │   └── favorites.json         # 收藏配置
│   ├── mine/                       # 个人中心
│   │   ├── mine.js               # 个人中心逻辑
│   │   └── mine.json             # 个人中心配置
│   └── logs/                       # 日志页
│       ├── logs.js                # 日志逻辑
│       └── logs.json              # 日志配置
├── utils/                          # 工具目录
│   └── request.js                  # 请求封装
├── app.js                          # 应用入口
├── app.json                        # 应用配置
├── app.wxss                        # 全局样式
├── project.config.json             # 项目配置
└── sitemap.json                    #  sitemap配置
```

## 页面功能详解

### 1. 首页 (index)

**文件**: `pages/index/index.js`

**功能**:
- 应用启动后的欢迎页面
- 可作为启动引导页

---

### 2. 商品首页 (home)

**文件**: `pages/home/home.js`, `home.wxml`, `home.wxss`

**功能**:
- 顶部分类导航栏（横向滚动）
- 搜索框（关键词搜索商品）
- 商品列表（瀑布流/列表展示）
- 下拉刷新
- 上拉加载更多（分页）
- 点击商品跳转详情

**主要API**:
| 接口 | 方法 | 说明 |
|------|------|------|
| `/api/user/public/categories` | GET | 获取分类列表 |
| `/api/user/goods/list` | GET | 获取商品列表 |

---

### 3. 登录页 (login)

**文件**: `pages/login/login.js`, `login.wxml`, `login.wxss`

**功能**:
- Tab切换：账号登录 / 微信登录
- 账号登录：
  - 输入用户名/密码
  - 记住密码功能（localStorage存储）
  - 调用账号登录API
- 微信一键登录：
  - 调用 `wx.login()` 获取code
  - 发送code到后台验证
- 登录成功后跳转首页

**主要API**:
| 接口 | 方法 | 说明 |
|------|------|------|
| `/api/user/auth/account-login` | POST | 账号密码登录 |
| `/api/user/auth/wx-login` | POST | 微信授权登录 |

**记住密码逻辑**:
```javascript
// 登录成功后保存
wx.setStorageSync('rememberAccount', { account, password, remember: true })

// 页面加载时读取
const rememberAccount = wx.getStorageSync('rememberAccount')
```

---

### 4. 商品详情 (goods-detail)

**文件**: `pages/goods-detail/goods-detail.js`, `goods-detail.wxml`, `goods-detail.wxss`

**功能**:
- 商品图片轮播展示
- 商品信息展示（标题、价格、描述、卖家信息）
- 商品状态标签（在售/审核中/已下架/已驳回）
- 收藏/取消收藏
- 评论列表展示
- 发布评论
- 查看卖家信息
- 判断是否为卖家本人（隐藏购买按钮）

**主要API**:
| 接口 | 方法 | 说明 |
|------|------|------|
| `/api/user/goods/{id}` | GET | 获取商品详情 |
| `/api/user/comments/{goodsId}` | GET | 获取评论列表 |
| `/api/user/favorites/{goodsId}` | POST | 收藏商品 |
| `/api/user/favorites/{goodsId}` | DELETE | 取消收藏 |
| `/api/user/comments` | POST | 发布评论 |

**状态判断逻辑**:
```javascript
isOwner: currentUserId > 0 && Number(detail.userId || 0) === currentUserId
```

---

### 5. 商品发布 (goods-publish)

**文件**: `pages/goods-publish/goods-publish.js`

**功能**:
- 选择分类
- 填写商品信息（标题、描述、价格）
- 上传商品图片
- 表单验证
- 发布商品
- 编辑已发布的商品

**主要API**:
| 接口 | 方法 | 说明 |
|------|------|------|
| `/api/user/goods` | POST | 发布商品 |
| `/api/user/goods/{id}` | PUT | 编辑商品 |
| `/api/user/files/upload` | POST | 上传图片 |

---

### 6. 我的发布 (my-goods)

**文件**: `pages/my-goods/my-goods.js`

**功能**:
- 查看我发布的商品列表
- 商品状态筛选（全部/待审核/已通过/已下架）
- 上架/下架操作
- 编辑商品
- 删除商品

**主要API**:
| 接口 | 方法 | 说明 |
|------|------|------|
| `/api/user/goods/my` | GET | 获取我的发布 |
| `/api/user/goods/{id}` | DELETE | 删除商品 |
| `/api/user/goods/{id}/offline` | POST | 下架商品 |
| `/api/user/goods/{id}/online` | POST | 上架商品 |

---

### 7. 订单中心 (orders)

**文件**: `pages/orders/orders.js`

**功能**:
- Tab切换：买到的 / 卖出的
- 订单列表展示
- 订单状态筛选
- 订单操作：
  - 买家：支付订单、确认收货、取消订单
  - 卖家：确认发货

**主要API**:
| 接口 | 方法 | 说明 |
|------|------|------|
| `/api/user/orders/my` | GET | 获取我的订单 |
| `/api/user/orders/{id}` | GET | 订单详情 |
| `/api/user/orders/{id}/pay` | POST | 支付订单 |
| `/api/user/orders/{id}/cancel` | POST | 取消订单 |
| `/api/user/orders/{id}/seller-confirm` | POST | 卖家发货确认 |
| `/api/user/orders/{id}/buyer-confirm` | POST | 买家收货确认 |

---

### 8. 我的收藏 (favorites)

**文件**: `pages/favorites/favorites.js`

**功能**:
- 收藏商品列表展示
- 取消收藏
- 点击商品跳转详情

**主要API**:
| 接口 | 方法 | 说明 |
|------|------|------|
| `/api/user/favorites` | GET | 获取收藏列表 |
| `/api/user/favorites/{goodsId}` | DELETE | 取消收藏 |

---

### 9. 个人中心 (mine)

**文件**: `pages/mine/mine.js`

**功能**:
- 用户信息展示（头像、昵称）
- 功能入口导航：
  - 我的发布
  - 我的订单（买到的）
  - 卖出的订单
  - 我的收藏
- 退出登录

---

### 10. 日志页 (logs)

**文件**: `pages/logs/logs.js`

**功能**:
- 微信小程序日志页面
- 用于调试和问题排查

---

## 工具模块

### 请求封装 (utils/request.js)

**功能**:
- 封装 `wx.request()`
- 统一添加 `Authorization` 请求头
- 统一处理响应格式
- 错误处理

**核心代码**:
```javascript
const { request } = require('../../utils/request')

// 携带Token的请求
request({
  url: '/api/user/goods/list',
  data: { pageNo: 1, pageSize: 10 }
}).then((data) => {
  // 处理数据
})
```

**请求头配置**:
```javascript
header: {
  'Content-Type': 'application/json',
  'Authorization': token ? `Bearer ${token}` : ''
}
```

---

## 全局配置

### app.json

```json
{
  "pages": [
    "pages/index/index",
    "pages/home/home",
    "pages/login/login",
    "pages/goods-detail/goods-detail",
    "pages/goods-publish/goods-publish",
    "pages/my-goods/my-goods",
    "pages/orders/orders",
    "pages/favorites/favorites",
    "pages/mine/mine"
  ],
  "window": {
    "backgroundTextStyle": "light",
    "navigationBarBackgroundColor": "#fff",
    "navigationBarTitleText": "校园二手交易",
    "navigationBarTextStyle": "black"
  },
  "tabBar": {
    "color": "#999",
    "selectedColor": "#07c160",
    "backgroundColor": "#fff",
    "list": [
      { "pagePath": "pages/home/home", "text": "首页", "iconPath": "...", "selectedIconPath": "..." },
      { "pagePath": "pages/goods-publish/goods-publish", "text": "发布", "iconPath": "...", "selectedIconPath": "..." },
      { "pagePath": "pages/orders/orders", "text": "订单", "iconPath": "...", "selectedIconPath": "..." },
      { "pagePath": "pages/mine/mine", "text": "我的", "iconPath": "...", "selectedIconPath": "..." }
    ]
  }
}
```

---

## 页面跳转方式

| 方式 | 适用场景 |
|------|---------|
| `wx.switchTab()` | 跳转TabBar页面（需在app.json注册） |
| `wx.navigateTo()` | 跳转普通页面（保留当前页） |
| `wx.redirectTo()` | 跳转普通页面（关闭当前页） |
| `wx.navigateBack()` | 返回上一页 |

---

## 数据存储

| Key | 说明 |
|-----|------|
| `token` | JWT认证令牌 |
| `userInfo` | 用户信息对象 |
| `rememberAccount` | 记住的账号密码 |

---

## 接口文档

### 通用说明

**基础URL**: `http://localhost:8080`

**认证方式**: 需要认证的接口需要在请求头中携带 JWT Token：
```javascript
header: {
  'Authorization': token ? `Bearer ${token}` : ''
}
```

**通用响应格式**：
```json
{
  "code": 0,
  "message": "操作成功",
  "data": {}
}
```

**状态码说明**：
| code | 说明 |
|------|------|
| 0 | 成功 |
| 401 | 未授权/认证失败 |
| 403 | 无权限访问 |
| 500 | 服务器内部错误 |

---

### 认证接口 `/api/user/auth`

#### 账号登录

**请求**
```http
POST /api/user/auth/account-login
Content-Type: application/json
```

**请求参数**
| 参数名 | 类型 | 必填 | 说明 | 示例 |
|--------|------|------|------|------|
| account | string | 是 | 账号（手机号/用户名） | "13800000001" |
| password | string | 是 | 密码 | "123456" |

**请求示例**
```json
{
  "account": "13800000001",
  "password": "123456"
}
```

**响应参数**
| 参数名 | 类型 | 说明 |
|--------|------|------|
| code | int | 响应状态码，0表示成功 |
| message | string | 响应消息 |
| data | object | 用户信息对象 |
| data.id | long | 用户ID |
| data.nickname | string | 用户昵称 |
| data.avatarUrl | string | 用户头像URL |
| data.token | string | JWT认证令牌 |

**响应示例**
```json
{
  "code": 0,
  "message": "操作成功",
  "data": {
    "id": 1,
    "nickname": "用户昵称",
    "avatarUrl": "https://example.com/avatar.jpg",
    "token": "eyJhbGciOiJIUzI1NiJ9..."
  }
}
```

---

#### 微信登录

**请求**
```http
POST /api/user/auth/wx-login
Content-Type: application/json
```

**请求参数**
| 参数名 | 类型 | 必填 | 说明 | 示例 |
|--------|------|------|------|------|
| code | string | 是 | 微信授权code，通过wx.login()获取 | "0611A..." |

**请求示例**
```json
{
  "code": "0611A11wa2..."
}
```

**响应参数**
| 参数名 | 类型 | 说明 |
|--------|------|------|
| code | int | 响应状态码 |
| message | string | 响应消息 |
| data | object | 用户信息对象 |
| data.id | long | 用户ID |
| data.nickname | string | 用户昵称 |
| data.avatarUrl | string | 用户头像URL |
| data.token | string | JWT认证令牌 |

**响应示例**
```json
{
  "code": 0,
  "message": "操作成功",
  "data": {
    "id": 1,
    "nickname": "微信用户",
    "avatarUrl": "https://thirdwx.qlogo.cn/...",
    "token": "eyJhbGciOiJIUzI1NiJ9..."
  }
}
```

---

### 公开接口 `/api/user/public`

#### 获取分类列表

**请求**
```http
GET /api/user/public/categories
```

**请求参数**：无

**响应参数**
| 参数名 | 类型 | 说明 |
|--------|------|------|
| code | int | 响应状态码 |
| data | array | 分类列表 |

**data 数组元素**
| 参数名 | 类型 | 说明 |
|--------|------|------|
| id | long | 分类ID |
| name | string | 分类名称 |
| sort | int | 排序值，数值越大越靠前 |
| status | int | 状态，1-启用，0-停用 |

**响应示例**
```json
{
  "code": 0,
  "data": [
    { "id": 1, "name": "手机数码", "sort": 100, "status": 1 },
    { "id": 2, "name": "电脑办公", "sort": 90, "status": 1 }
  ]
}
```

---

### 商品接口 `/api/user/goods`

#### 商品列表（搜索）

**请求**
```http
GET /api/user/goods/list?pageNo=1&pageSize=10&keyword=iPhone&categoryId=1&sortBy=newest
```

**Query参数**
| 参数名 | 类型 | 必填 | 说明 | 默认值 |
|--------|------|------|------|--------|
| pageNo | int | 否 | 页码 | 1 |
| pageSize | int | 否 | 每页条数 | 10 |
| keyword | string | 否 | 搜索关键词 | - |
| categoryId | long | 否 | 分类ID | - |
| sortBy | string | 否 | 排序方式：newest(最新)/price_asc(价格升序)/price_desc(价格降序) | newest |

**响应参数**
| 参数名 | 类型 | 说明 |
|--------|------|------|
| code | int | 响应状态码 |
| data | object | 分页数据对象 |
| data.records | array | 商品列表 |
| data.total | long | 总记录数 |
| data.pageNo | long | 当前页码 |
| data.pageSize | long | 每页条数 |

**data.records 数组元素**
| 参数名 | 类型 | 说明 |
|--------|------|------|
| id | long | 商品ID |
| title | string | 商品标题 |
| price | decimal | 商品价格 |
| coverImage | string | 封面图片URL |
| categoryId | long | 分类ID |
| categoryName | string | 分类名称 |
| status | string | 商品状态 |
| viewCount | int | 浏览次数 |
| favoriteCount | int | 收藏次数 |
| createdAt | string | 发布时间 |

**响应示例**
```json
{
  "code": 0,
  "data": {
    "records": [
      {
        "id": 1,
        "title": "iPhone 14 Pro",
        "price": 6999.00,
        "coverImage": "https://example.com/goods.jpg",
        "categoryId": 1,
        "categoryName": "手机数码",
        "status": "APPROVED",
        "viewCount": 100,
        "favoriteCount": 10,
        "createdAt": "2026-04-20 10:00:00"
      }
    ],
    "total": 50,
    "pageNo": 1,
    "pageSize": 10
  }
}
```

---

#### 商品详情

**请求**
```http
GET /api/user/goods/{goodsId}
```

**路径参数**
| 参数名 | 类型 | 必填 | 说明 | 示例 |
|--------|------|------|------|------|
| goodsId | long | 是 | 商品ID | 1 |

**响应参数**
| 参数名 | 类型 | 说明 |
|--------|------|------|
| code | int | 响应状态码 |
| data | object | 商品详情对象 |
| data.id | long | 商品ID |
| data.title | string | 商品标题 |
| data.description | string | 商品描述 |
| data.price | decimal | 商品价格 |
| data.images | array | 商品图片列表 |
| data.categoryId | long | 分类ID |
| data.categoryName | string | 分类名称 |
| data.userId | long | 卖家用户ID |
| data.nickname | string | 卖家昵称 |
| data.avatarUrl | string | 卖家头像 |
| data.status | string | 商品状态 |
| data.viewCount | int | 浏览次数 |
| data.favoriteCount | int | 收藏次数 |
| data.isFavorite | boolean | 当前用户是否收藏 |
| data.createdAt | string | 发布时间 |

**响应示例**
```json
{
  "code": 0,
  "data": {
    "id": 1,
    "title": "iPhone 14 Pro",
    "description": "99新无划痕",
    "price": 6999.00,
    "images": ["url1", "url2"],
    "categoryId": 1,
    "categoryName": "手机数码",
    "userId": 5,
    "nickname": "卖家昵称",
    "avatarUrl": "https://...",
    "status": "APPROVED",
    "viewCount": 100,
    "favoriteCount": 10,
    "isFavorite": false,
    "createdAt": "2026-04-20T10:00:00"
  }
}
```

---

#### 发布商品

**请求**
```http
POST /api/user/goods
Authorization: Bearer <token>
Content-Type: application/json
```

**请求参数**
| 参数名 | 类型 | 必填 | 说明 | 示例 |
|--------|------|------|------|------|
| title | string | 是 | 商品标题 | "iPhone 14 Pro" |
| description | string | 是 | 商品描述 | "99新无划痕" |
| price | decimal | 是 | 商品价格 | 6999.00 |
| categoryId | long | 是 | 分类ID | 1 |
| images | array | 是 | 商品图片URL列表 | ["url1","url2"] |

**请求示例**
```json
{
  "title": "iPhone 14 Pro 256G",
  "description": "99新无划痕，国行正品，支持当面验货",
  "price": 6999.00,
  "categoryId": 1,
  "images": ["https://example.com/img1.jpg", "https://example.com/img2.jpg"]
}
```

**响应参数**
| 参数名 | 类型 | 说明 |
|--------|------|------|
| code | int | 响应状态码 |
| message | string | 响应消息 |
| data | long | 新增商品ID |

**响应示例**
```json
{
  "code": 0,
  "message": "发布成功",
  "data": 1
}
```

---

#### 编辑商品

**请求**
```http
PUT /api/user/goods/{goodsId}
Authorization: Bearer <token>
Content-Type: application/json
```

**路径参数**
| 参数名 | 类型 | 必填 | 说明 | 示例 |
|--------|------|------|------|------|
| goodsId | long | 是 | 商品ID | 1 |

**请求参数**
| 参数名 | 类型 | 必填 | 说明 | 示例 |
|--------|------|------|------|------|
| title | string | 是 | 商品标题 | "iPhone 14 Pro Max" |
| description | string | 是 | 商品描述 | "全新未拆封" |
| price | decimal | 是 | 商品价格 | 7999.00 |
| categoryId | long | 是 | 分类ID | 1 |
| images | array | 是 | 商品图片URL列表 | ["url1","url2"] |

**响应参数**
| 参数名 | 类型 | 说明 |
|--------|------|------|
| code | int | 响应状态码 |
| message | string | 操作结果消息 |

**响应示例**
```json
{
  "code": 0,
  "message": "操作成功"
}
```

---

#### 删除商品

**请求**
```http
DELETE /api/user/goods/{goodsId}
Authorization: Bearer <token>
```

**路径参数**
| 参数名 | 类型 | 必填 | 说明 | 示例 |
|--------|------|------|------|------|
| goodsId | long | 是 | 商品ID | 1 |

**响应参数**
| 参数名 | 类型 | 说明 |
|--------|------|------|
| code | int | 响应状态码 |
| message | string | 操作结果消息 |

**响应示例**
```json
{
  "code": 0,
  "message": "删除成功"
}
```

---

#### 下架商品

**请求**
```http
POST /api/user/goods/{goodsId}/offline
Authorization: Bearer <token>
```

**路径参数**
| 参数名 | 类型 | 必填 | 说明 | 示例 |
|--------|------|------|------|------|
| goodsId | long | 是 | 商品ID | 1 |

**响应参数**
| 参数名 | 类型 | 说明 |
|--------|------|------|
| code | int | 响应状态码 |
| message | string | 操作结果消息 |

**响应示例**
```json
{
  "code": 0,
  "message": "下架成功"
}
```

---

#### 上架商品

**请求**
```http
POST /api/user/goods/{goodsId}/online
Authorization: Bearer <token>
```

**路径参数**
| 参数名 | 类型 | 必填 | 说明 | 示例 |
|--------|------|------|------|------|
| goodsId | long | 是 | 商品ID | 1 |

**响应参数**
| 参数名 | 类型 | 说明 |
|--------|------|------|
| code | int | 响应状态码 |
| message | string | 操作结果消息 |

**响应示例**
```json
{
  "code": 0,
  "message": "上架成功"
}
```

---

#### 我的发布

**请求**
```http
GET /api/user/goods/my?pageNo=1&pageSize=10
Authorization: Bearer <token>
```

**Query参数**
| 参数名 | 类型 | 必填 | 说明 | 默认值 |
|--------|------|------|------|--------|
| pageNo | int | 否 | 页码 | 1 |
| pageSize | int | 否 | 每页条数 | 10 |

**响应参数**
| 参数名 | 类型 | 说明 |
|--------|------|------|
| code | int | 响应状态码 |
| data | object | 分页数据对象 |
| data.records | array | 商品列表 |
| data.total | long | 总记录数 |
| data.pageNo | long | 当前页码 |
| data.pageSize | long | 每页条数 |

**响应示例**
```json
{
  "code": 0,
  "data": {
    "records": [...],
    "total": 5,
    "pageNo": 1,
    "pageSize": 10
  }
}
```

---

### 订单接口 `/api/user/orders`

#### 创建订单

**请求**
```http
POST /api/user/orders
Authorization: Bearer <token>
Content-Type: application/json
```

**请求参数**
| 参数名 | 类型 | 必填 | 说明 | 示例 |
|--------|------|------|------|------|
| goodsId | long | 是 | 商品ID | 1 |
| message | string | 否 | 买家留言 | "尽快发货" |

**请求示例**
```json
{
  "goodsId": 1,
  "message": "尽快发货"
}
```

**响应参数**
| 参数名 | 类型 | 说明 |
|--------|------|------|
| code | int | 响应状态码 |
| data | long | 订单ID |

**响应示例**
```json
{
  "code": 0,
  "data": 1
}
```

---

#### 我的订单

**请求**
```http
GET /api/user/orders/my?asRole=buyer&pageNo=1&pageSize=10
Authorization: Bearer <token>
```

**Query参数**
| 参数名 | 类型 | 必填 | 说明 | 默认值 |
|--------|------|------|------|--------|
| asRole | string | 否 | 视角：buyer(买家)/seller(卖家) | buyer |
| pageNo | int | 否 | 页码 | 1 |
| pageSize | int | 否 | 每页条数 | 10 |

**响应参数**
| 参数名 | 类型 | 说明 |
|--------|------|------|
| code | int | 响应状态码 |
| data | object | 分页数据对象 |
| data.records | array | 订单列表 |
| data.total | long | 总记录数 |
| data.pageNo | long | 当前页码 |
| data.pageSize | long | 每页条数 |

**响应示例**
```json
{
  "code": 0,
  "data": {
    "records": [...],
    "total": 20,
    "pageNo": 1,
    "pageSize": 10
  }
}
```

---

#### 订单详情

**请求**
```http
GET /api/user/orders/{orderId}
Authorization: Bearer <token>
```

**路径参数**
| 参数名 | 类型 | 必填 | 说明 | 示例 |
|--------|------|------|------|------|
| orderId | long | 是 | 订单ID | 1 |

**响应参数**
| 参数名 | 类型 | 说明 |
|--------|------|------|
| code | int | 响应状态码 |
| data | object | 订单详情对象 |
| data.id | long | 订单ID |
| data.orderNo | string | 订单编号 |
| data.goodsId | long | 商品ID |
| data.goodsTitle | string | 商品标题 |
| data.goodsImage | string | 商品图片 |
| data.price | decimal | 商品单价 |
| data.amount | decimal | 订单金额 |
| data.orderStatus | string | 订单状态 |
| data.payStatus | string | 支付状态 |
| data.message | string | 买家留言 |
| data.sellerId | long | 卖家ID |
| data.sellerNickname | string | 卖家昵称 |
| data.buyerId | long | 买家ID |
| data.buyerNickname | string | 买家昵称 |
| data.createdAt | string | 下单时间 |

**orderStatus 订单状态枚举值**
| 值 | 说明 |
|------|------|
| PENDING_PAYMENT | 待支付 |
| PAID | 已支付 |
| SHIPPED | 已发货 |
| COMPLETED | 已完成 |
| CANCELLED | 已取消 |

**响应示例**
```json
{
  "code": 0,
  "data": {
    "id": 1,
    "orderNo": "ORD202604201234567890",
    "goodsId": 1,
    "goodsTitle": "iPhone 14 Pro",
    "goodsImage": "https://...",
    "price": 6999.00,
    "amount": 6999.00,
    "orderStatus": "PAID",
    "payStatus": "PAID",
    "message": "尽快发货",
    "sellerId": 3,
    "sellerNickname": "卖家",
    "buyerId": 5,
    "buyerNickname": "买家",
    "createdAt": "2026-04-20 10:00:00"
  }
}
```

---

#### 支付订单

**请求**
```http
POST /api/user/orders/{orderId}/pay
Authorization: Bearer <token>
```

**路径参数**
| 参数名 | 类型 | 必填 | 说明 | 示例 |
|--------|------|------|------|------|
| orderId | long | 是 | 订单ID | 1 |

**响应参数**
| 参数名 | 类型 | 说明 |
|--------|------|------|
| code | int | 响应状态码 |
| message | string | 操作结果消息 |

**响应示例**
```json
{
  "code": 0,
  "message": "支付成功"
}
```

---

#### 卖家发货确认

**请求**
```http
POST /api/user/orders/{orderId}/seller-confirm
Authorization: Bearer <token>
```

**路径参数**
| 参数名 | 类型 | 必填 | 说明 | 示例 |
|--------|------|------|------|------|
| orderId | long | 是 | 订单ID | 1 |

**响应参数**
| 参数名 | 类型 | 说明 |
|--------|------|------|
| code | int | 响应状态码 |
| message | string | 操作结果消息 |

**响应示例**
```json
{
  "code": 0,
  "message": "已确认发货"
}
```

---

#### 买家收货确认

**请求**
```http
POST /api/user/orders/{orderId}/buyer-confirm
Authorization: Bearer <token>
```

**路径参数**
| 参数名 | 类型 | 必填 | 说明 | 示例 |
|--------|------|------|------|------|
| orderId | long | 是 | 订单ID | 1 |

**响应参数**
| 参数名 | 类型 | 说明 |
|--------|------|------|
| code | int | 响应状态码 |
| message | string | 操作结果消息 |

**响应示例**
```json
{
  "code": 0,
  "message": "已确认收货"
}
```

---

#### 取消订单

**请求**
```http
POST /api/user/orders/{orderId}/cancel
Authorization: Bearer <token>
```

**路径参数**
| 参数名 | 类型 | 必填 | 说明 | 示例 |
|--------|------|------|------|------|
| orderId | long | 是 | 订单ID | 1 |

**响应参数**
| 参数名 | 类型 | 说明 |
|--------|------|------|
| code | int | 响应状态码 |
| message | string | 操作结果消息 |

**响应示例**
```json
{
  "code": 0,
  "message": "订单已取消"
}
```

---

### 互动接口 `/api/user`

#### 收藏商品

**请求**
```http
POST /api/user/favorites/{goodsId}
Authorization: Bearer <token>
```

**路径参数**
| 参数名 | 类型 | 必填 | 说明 | 示例 |
|--------|------|------|------|------|
| goodsId | long | 是 | 商品ID | 1 |

**响应参数**
| 参数名 | 类型 | 说明 |
|--------|------|------|
| code | int | 响应状态码 |
| message | string | 操作结果消息 |

**响应示例**
```json
{
  "code": 0,
  "message": "收藏成功"
}
```

---

#### 取消收藏

**请求**
```http
DELETE /api/user/favorites/{goodsId}
Authorization: Bearer <token>
```

**路径参数**
| 参数名 | 类型 | 必填 | 说明 | 示例 |
|--------|------|------|------|------|
| goodsId | long | 是 | 商品ID | 1 |

**响应参数**
| 参数名 | 类型 | 说明 |
|--------|------|------|
| code | int | 响应状态码 |
| message | string | 操作结果消息 |

**响应示例**
```json
{
  "code": 0,
  "message": "已取消收藏"
}
```

---

#### 我的收藏

**请求**
```http
GET /api/user/favorites?pageNo=1&pageSize=10
Authorization: Bearer <token>
```

**Query参数**
| 参数名 | 类型 | 必填 | 说明 | 默认值 |
|--------|------|------|------|--------|
| pageNo | int | 否 | 页码 | 1 |
| pageSize | int | 否 | 每页条数 | 10 |

**响应参数**
| 参数名 | 类型 | 说明 |
|--------|------|------|
| code | int | 响应状态码 |
| data | object | 分页数据对象 |
| data.records | array | 收藏商品列表 |
| data.total | long | 总记录数 |
| data.pageNo | long | 当前页码 |
| data.pageSize | long | 每页条数 |

**响应示例**
```json
{
  "code": 0,
  "data": {
    "records": [
      {
        "id": 1,
        "title": "iPhone 14 Pro",
        "price": 6999.00,
        "coverImage": "https://..."
      }
    ],
    "total": 5,
    "pageNo": 1,
    "pageSize": 10
  }
}
```

---

#### 添加评论

**请求**
```http
POST /api/user/comments
Authorization: Bearer <token>
Content-Type: application/json
```

**请求参数**
| 参数名 | 类型 | 必填 | 说明 | 示例 |
|--------|------|------|------|------|
| goodsId | long | 是 | 商品ID | 1 |
| content | string | 是 | 评论内容 | "商品很好，卖家服务周到" |

**请求示例**
```json
{
  "goodsId": 1,
  "content": "商品很好，卖家服务周到"
}
```

**响应参数**
| 参数名 | 类型 | 说明 |
|--------|------|------|
| code | int | 响应状态码 |
| message | string | 操作结果消息 |

**响应示例**
```json
{
  "code": 0,
  "message": "评论成功"
}
```

---

#### 商品评论列表

**请求**
```http
GET /api/user/comments/{goodsId}?pageNo=1&pageSize=10
```

**路径参数**
| 参数名 | 类型 | 必填 | 说明 | 示例 |
|--------|------|------|------|------|
| goodsId | long | 是 | 商品ID | 1 |

**Query参数**
| 参数名 | 类型 | 必填 | 说明 | 默认值 |
|--------|------|------|------|--------|
| pageNo | int | 否 | 页码 | 1 |
| pageSize | int | 否 | 每页条数 | 10 |

**响应参数**
| 参数名 | 类型 | 说明 |
|--------|------|------|
| code | int | 响应状态码 |
| data | object | 分页数据对象 |

**data.records 数组元素**
| 参数名 | 类型 | 说明 |
|--------|------|------|
| id | long | 评论ID |
| userId | long | 评论用户ID |
| nickname | string | 评论用户昵称 |
| avatarUrl | string | 评论用户头像 |
| content | string | 评论内容 |
| createdAt | string | 评论时间 |

**响应示例**
```json
{
  "code": 0,
  "data": {
    "records": [
      {
        "id": 1,
        "userId": 5,
        "nickname": "评论者",
        "avatarUrl": "https://...",
        "content": "商品很好",
        "createdAt": "2026-04-20 10:00:00"
      }
    ],
    "total": 10,
    "pageNo": 1,
    "pageSize": 10
  }
}
```

---

#### 举报商品

**请求**
```http
POST /api/user/reports
Authorization: Bearer <token>
Content-Type: application/json
```

**请求参数**
| 参数名 | 类型 | 必填 | 说明 | 示例 |
|--------|------|------|------|------|
| goodsId | long | 是 | 被举报商品ID | 1 |
| reason | string | 是 | 举报原因 | "商品信息虚假" |

**请求示例**
```json
{
  "goodsId": 1,
  "reason": "商品信息虚假"
}
```

**响应参数**
| 参数名 | 类型 | 说明 |
|--------|------|------|
| code | int | 响应状态码 |
| message | string | 操作结果消息 |

**响应示例**
```json
{
  "code": 0,
  "message": "举报成功"
}
```

---

### 文件上传 `/api/user/files`

#### 上传图片

**请求**
```http
POST /api/user/files/upload
Authorization: Bearer <token>
Content-Type: multipart/form-data
```

**表单字段**
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| file | file | 是 | 图片文件 |

**响应参数**
| 参数名 | 类型 | 说明 |
|--------|------|------|
| code | int | 响应状态码 |
| data | string | 文件访问URL |

**响应示例**
```json
{
  "code": 0,
  "data": "https://your-domain.com/uploads/xxx.jpg"
}
```

---

## 快速开始

### 环境要求
- 微信开发者工具 1.0+
- 微信小程序AppID

### 安装依赖
```bash
# 在小程序项目目录执行
npm install
```

### 开发调试
1. 打开微信开发者工具
2. 导入项目目录
3. 设置项目AppID
4. 编译运行

### 构建发布
1. 执行 `npm run build`（如有）
2. 在微信开发者工具中点击"上传"
3. 在微信公众平台提交审核

---

## 许可证

MIT License
