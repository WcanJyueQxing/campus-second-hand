# 校园二手交易平台 - 管理后台

## 项目简介

`second-hub-admin` 是校园二手交易平台的管理后台系统，基于 Vue 3 + Element Plus 构建，为平台管理员提供商品审核、用户管理、订单监管、数据统计等功能。

## 技术栈

| 技术 | 版本 | 说明 |
|------|------|------|
| Vue | 3.5.x | 渐进式JavaScript框架 |
| Vite | 5.x | 下一代前端构建工具 |
| Element Plus | 2.9.x | Vue 3 UI 组件库 |
| Pinia | 3.x | 状态管理库 |
| Vue Router | 4.x | Vue官方路由管理 |
| ECharts | 6.x | 数据可视化图表库 |
| axios | 1.9.x | HTTP请求库 |

## 功能模块

### 1. 登录认证
- 管理员账号密码登录
- JWT Token 身份验证
- 登录状态持久化

### 2. 数据统计仪表盘
- **统计卡片**：用户总数、商品总数、待审核商品、订单总数、待处理举报
- **近7日趋势图**：支持切换查看用户/商品/订单新增趋势
- **商品分类分布**：饼图展示各分类商品数量占比
- **订单状态分布**：玫瑰图展示各状态订单数量
- **用户状态分布**：柱状图展示正常/禁用用户对比

### 3. 商品审核
- 待审核商品列表展示
- 审核通过操作
- 审核拒绝操作（需填写拒绝原因）
- 商品下架操作

### 4. 分类管理
- 商品分类列表
- 新增分类（名称、排序、状态）
- 删除分类

### 5. 用户管理
- 用户列表展示（昵称、手机号、状态）
- 用户账号禁用
- 用户账号启用

### 6. 订单监管
- 所有订单列表展示
- 订单状态筛选
- 订单取消操作

### 7. 举报处理
- 待处理举报列表
- 举报通过处理
- 举报驳回处理

### 8. 公告管理
- 公告列表展示
- 发布新公告（标题、内容、封面）
- 删除公告

## 快速开始

### 环境要求
- Node.js >= 16.x
- npm >= 8.x

### 安装依赖
```bash
npm install
```

### 开发模式启动
```bash
npm run dev
```

### 生产环境构建
```bash
npm run build
```

### 预览构建结果
```bash
npm run preview
```

## 接口文档

### 通用说明

**认证方式**：所有接口（除登录外）需要在请求头中携带 JWT Token：
```
Authorization: Bearer <token>
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

### 认证接口 `/api/admin/auth`

#### 管理员登录

**请求**
```http
POST /api/admin/auth/login
Content-Type: application/json
```

**请求参数**
| 参数名 | 类型 | 必填 | 说明 | 示例 |
|--------|------|------|------|------|
| username | string | 是 | 管理员用户名 | "admin" |
| password | string | 是 | 密码（MD5加密传输） | "e10adc3949ba59abbe56e057f20f883e" |

**请求示例**
```json
{
  "username": "admin",
  "password": "123456"
}
```

**响应参数**
| 参数名 | 类型 | 说明 |
|--------|------|------|
| code | int | 响应状态码，0表示成功 |
| message | string | 响应消息 |
| data | object | 登录信息对象 |
| data.id | long | 管理员ID |
| data.nickname | string | 管理员昵称 |
| data.role | string | 角色标识，固定为"ADMIN" |
| data.token | string | JWT认证令牌 |

**响应示例**
```json
{
  "code": 0,
  "data": {
    "id": 1,
    "nickname": "系统管理员",
    "role": "ADMIN",
    "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsInJvbGUiOiJBRE1JTiIsImlhdCI6MTcxMzQ4MzIzNCwiZXhwIjoxNzEzNTY5NjM0fQ.abc123..."
  },
  "message": "操作成功"
}
```

---

#### 退出登录

**请求**
```http
POST /api/admin/auth/logout
Authorization: Bearer <token>
```

**请求参数**：无

**响应参数**
| 参数名 | 类型 | 说明 |
|--------|------|------|
| code | int | 响应状态码 |
| message | string | 操作结果消息 |

**响应示例**
```json
{
  "code": 0,
  "message": "退出成功"
}
```

---

### 数据统计接口 `/api/admin/dashboard`

#### 获取仪表盘概览

**请求**
```http
GET /api/admin/dashboard/overview
Authorization: Bearer <token>
```

**请求参数**：无

**响应参数**
| 参数名 | 类型 | 说明 |
|--------|------|------|
| code | int | 响应状态码 |
| data | object | 统计数据对象 |
| data.userCount | long | 用户总数 |
| data.goodsCount | long | 商品总数 |
| data.pendingGoodsCount | long | 待审核商品数量 |
| data.orderCount | long | 订单总数 |
| data.reportCount | long | 待处理举报数量 |
| data.categoryDistribution | array | 商品分类分布列表 |
| data.orderStatusDistribution | array | 订单状态分布列表 |
| data.userStatusDistribution | array | 用户状态分布列表 |

**categoryDistribution 数组元素**
| 参数名 | 类型 | 说明 |
|--------|------|------|
| categoryName | string | 分类名称 |
| goodsCount | long | 该分类下的商品数量 |

**orderStatusDistribution 数组元素**
| 参数名 | 类型 | 说明 |
|--------|------|------|
| status | string | 订单状态枚举值 |
| count | long | 该状态的订单数量 |

**userStatusDistribution 数组元素**
| 参数名 | 类型 | 说明 |
|--------|------|------|
| status | int | 用户状态，1-正常，0-禁用 |
| count | long | 该状态的用户数量 |

**响应示例**
```json
{
  "code": 0,
  "data": {
    "userCount": 100,
    "goodsCount": 500,
    "pendingGoodsCount": 10,
    "orderCount": 200,
    "reportCount": 5,
    "categoryDistribution": [
      { "categoryName": "手机数码", "goodsCount": 120 },
      { "categoryName": "电脑办公", "goodsCount": 80 }
    ],
    "orderStatusDistribution": [
      { "status": "PENDING_PAYMENT", "count": 10 },
      { "status": "PAID", "count": 50 },
      { "status": "SHIPPED", "count": 30 },
      { "status": "COMPLETED", "count": 100 },
      { "status": "CANCELLED", "count": 10 }
    ],
    "userStatusDistribution": [
      { "status": 1, "count": 95 },
      { "status": 0, "count": 5 }
    ]
  }
}
```

---

#### 获取近7日趋势

**请求**
```http
GET /api/admin/dashboard/trend
Authorization: Bearer <token>
```

**请求参数**：无

**响应参数**
| 参数名 | 类型 | 说明 |
|--------|------|------|
| code | int | 响应状态码 |
| data | array | 近7日趋势数据列表 |

**data 数组元素**
| 参数名 | 类型 | 说明 |
|--------|------|------|
| date | string | 日期，格式：yyyy-MM-dd |
| userCount | int | 当日新增用户数 |
| goodsCount | int | 当日新增商品数 |
| orderCount | int | 当日新增订单数 |

**响应示例**
```json
{
  "code": 0,
  "data": [
    { "date": "2026-04-17", "userCount": 5, "goodsCount": 20, "orderCount": 8 },
    { "date": "2026-04-18", "userCount": 3, "goodsCount": 15, "orderCount": 6 },
    { "date": "2026-04-19", "userCount": 7, "goodsCount": 25, "orderCount": 12 },
    { "date": "2026-04-20", "userCount": 4, "goodsCount": 18, "orderCount": 9 },
    { "date": "2026-04-21", "userCount": 6, "goodsCount": 22, "orderCount": 10 },
    { "date": "2026-04-22", "userCount": 8, "goodsCount": 30, "orderCount": 15 },
    { "date": "2026-04-23", "userCount": 5, "goodsCount": 20, "orderCount": 8 }
  ]
}
```

---

### 分类管理接口 `/api/admin/categories`

#### 获取分类列表

**请求**
```http
GET /api/admin/categories
Authorization: Bearer <token>
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
    { "id": 2, "name": "电脑办公", "sort": 90, "status": 1 },
    { "id": 3, "name": "图书教材", "sort": 80, "status": 1 }
  ]
}
```

---

#### 新增/编辑分类

**请求**
```http
POST /api/admin/categories
Content-Type: application/json
Authorization: Bearer <token>
```

**请求参数**
| 参数名 | 类型 | 必填 | 说明 | 示例 |
|--------|------|------|------|------|
| name | string | 是 | 分类名称 | "手机数码" |
| sort | int | 否 | 排序值，默认0 | 100 |
| status | int | 否 | 状态，默认1（启用） | 1 |

**请求示例**
```json
{
  "name": "手机数码",
  "sort": 100,
  "status": 1
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
  "message": "保存成功"
}
```

---

#### 删除分类

**请求**
```http
DELETE /api/admin/categories/{id}
Authorization: Bearer <token>
```

**路径参数**
| 参数名 | 类型 | 必填 | 说明 | 示例 |
|--------|------|------|------|------|
| id | long | 是 | 分类ID | 1 |

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

### 商品审核接口 `/api/admin/goods`

#### 获取待审核商品

**请求**
```http
GET /api/admin/goods/pending?pageNo=1&pageSize=50
Authorization: Bearer <token>
```

**Query参数**
| 参数名 | 类型 | 必填 | 说明 | 默认值 |
|--------|------|------|------|--------|
| pageNo | int | 否 | 页码 | 1 |
| pageSize | int | 否 | 每页条数 | 50 |

**响应参数**
| 参数名 | 类型 | 说明 |
|--------|------|------|
| code | int | 响应状态码 |
| data | object | 分页数据对象 |
| data.records | array | 商品列表 |
| data.total | long | 总记录数 |
| data.size | long | 每页条数 |
| data.current | long | 当前页码 |

**data.records 数组元素**
| 参数名 | 类型 | 说明 |
|--------|------|------|
| id | long | 商品ID |
| title | string | 商品标题 |
| description | string | 商品描述 |
| price | decimal | 商品价格 |
| images | array | 商品图片URL列表 |
| categoryId | long | 分类ID |
| categoryName | string | 分类名称 |
| userId | long | 发布用户ID |
| nickname | string | 发布者昵称 |
| status | string | 商品状态，PENDING-待审核 |
| createdAt | string | 发布时间，格式：yyyy-MM-dd HH:mm:ss |

**响应示例**
```json
{
  "code": 0,
  "data": {
    "records": [
      {
        "id": 1,
        "title": "iPhone 14 Pro 256G",
        "description": "99新无划痕，国行正品",
        "price": 6999.00,
        "images": ["https://example.com/img1.jpg", "https://example.com/img2.jpg"],
        "categoryId": 1,
        "categoryName": "手机数码",
        "userId": 5,
        "nickname": "卖家小王",
        "status": "PENDING",
        "createdAt": "2026-04-20 10:00:00"
      }
    ],
    "total": 10,
    "size": 50,
    "current": 1
  }
}
```

---

#### 审核商品

**请求**
```http
POST /api/admin/goods/{id}/audit
Content-Type: application/json
Authorization: Bearer <token>
```

**路径参数**
| 参数名 | 类型 | 必填 | 说明 | 示例 |
|--------|------|------|------|------|
| id | long | 是 | 商品ID | 1 |

**请求参数**
| 参数名 | 类型 | 必填 | 说明 | 示例 |
|--------|------|------|------|------|
| approved | boolean | 是 | 审核结果，true-通过，false-拒绝 | true |
| reason | string | 否 | 拒绝原因，approved为false时必填 | "商品信息不符合规范" |

**请求示例（通过）**
```json
{
  "approved": true,
  "reason": ""
}
```

**请求示例（拒绝）**
```json
{
  "approved": false,
  "reason": "商品信息虚假，图片与描述不符"
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
  "message": "操作成功"
}
```

---

#### 下架商品

**请求**
```http
POST /api/admin/goods/{id}/offline
Authorization: Bearer <token>
```

**路径参数**
| 参数名 | 类型 | 必填 | 说明 | 示例 |
|--------|------|------|------|------|
| id | long | 是 | 商品ID | 1 |

**请求参数**：无

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

### 用户管理接口 `/api/admin/users`

#### 获取用户列表

**请求**
```http
GET /api/admin/users?pageNo=1&pageSize=100
Authorization: Bearer <token>
```

**Query参数**
| 参数名 | 类型 | 必填 | 说明 | 默认值 |
|--------|------|------|------|--------|
| pageNo | int | 否 | 页码 | 1 |
| pageSize | int | 否 | 每页条数 | 100 |

**响应参数**
| 参数名 | 类型 | 说明 |
|--------|------|------|
| code | int | 响应状态码 |
| data | object | 分页数据对象 |
| data.records | array | 用户列表 |
| data.total | long | 总记录数 |
| data.size | long | 每页条数 |
| data.current | long | 当前页码 |

**data.records 数组元素**
| 参数名 | 类型 | 说明 |
|--------|------|------|
| id | long | 用户ID |
| nickname | string | 用户昵称 |
| phone | string | 手机号 |
| avatarUrl | string | 头像URL |
| status | int | 状态，1-正常，0-禁用 |
| createdAt | string | 注册时间，格式：yyyy-MM-dd HH:mm:ss |

**响应示例**
```json
{
  "code": 0,
  "data": {
    "records": [
      {
        "id": 1,
        "nickname": "测试用户A",
        "phone": "13800000001",
        "avatarUrl": "https://example.com/avatar.jpg",
        "status": 1,
        "createdAt": "2026-04-20 10:00:00"
      },
      {
        "id": 2,
        "nickname": "测试用户B",
        "phone": "13800000002",
        "avatarUrl": "",
        "status": 0,
        "createdAt": "2026-04-21 11:00:00"
      }
    ],
    "total": 50,
    "size": 100,
    "current": 1
  }
}
```

---

#### 更新用户状态

**请求**
```http
POST /api/admin/users/{id}/status
Content-Type: application/json
Authorization: Bearer <token>
```

**路径参数**
| 参数名 | 类型 | 必填 | 说明 | 示例 |
|--------|------|------|------|------|
| id | long | 是 | 用户ID | 1 |

**请求参数**
| 参数名 | 类型 | 必填 | 说明 | 示例 |
|--------|------|------|------|------|
| status | int | 是 | 状态，0-禁用，1-启用 | 0 |

**请求示例**
```json
{ "status": 0 }
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
  "message": "状态已更新"
}
```

---

### 订单监管接口 `/api/admin/orders`

#### 获取所有订单

**请求**
```http
GET /api/admin/orders?pageNo=1&pageSize=100
Authorization: Bearer <token>
```

**Query参数**
| 参数名 | 类型 | 必填 | 说明 | 默认值 |
|--------|------|------|------|--------|
| pageNo | int | 否 | 页码 | 1 |
| pageSize | int | 否 | 每页条数 | 100 |

**响应参数**
| 参数名 | 类型 | 说明 |
|--------|------|------|
| code | int | 响应状态码 |
| data | object | 分页数据对象 |
| data.records | array | 订单列表 |
| data.total | long | 总记录数 |
| data.size | long | 每页条数 |
| data.current | long | 当前页码 |

**data.records 数组元素**
| 参数名 | 类型 | 说明 |
|--------|------|------|
| id | long | 订单ID |
| orderNo | string | 订单编号 |
| goodsId | long | 商品ID |
| goodsTitle | string | 商品标题 |
| goodsImage | string | 商品图片URL |
| price | decimal | 商品单价 |
| amount | decimal | 订单金额 |
| orderStatus | string | 订单状态 |
| payStatus | string | 支付状态 |
| message | string | 买家留言 |
| buyerId | long | 买家用户ID |
| buyerNickname | string | 买家昵称 |
| sellerId | long | 卖家用户ID |
| sellerNickname | string | 卖家昵称 |
| createdAt | string | 下单时间，格式：yyyy-MM-dd HH:mm:ss |

**orderStatus 订单状态枚举值**
| 值 | 说明 |
|------|------|
| PENDING_PAYMENT | 待支付 |
| PAID | 已支付 |
| SHIPPED | 已发货 |
| COMPLETED | 已完成 |
| CANCELLED | 已取消 |

**payStatus 支付状态枚举值**
| 值 | 说明 |
|------|------|
| UNPAID | 未支付 |
| PAID | 已支付 |
| REFUNDED | 已退款 |

**响应示例**
```json
{
  "code": 0,
  "data": {
    "records": [
      {
        "id": 1,
        "orderNo": "ORD202604201234567890",
        "goodsId": 10,
        "goodsTitle": "iPhone 14 Pro",
        "goodsImage": "https://example.com/goods.jpg",
        "price": 6999.00,
        "amount": 6999.00,
        "orderStatus": "PENDING_PAYMENT",
        "payStatus": "UNPAID",
        "message": "尽快发货",
        "buyerId": 5,
        "buyerNickname": "买家小李",
        "sellerId": 3,
        "sellerNickname": "卖家小王",
        "createdAt": "2026-04-20 10:00:00"
      }
    ],
    "total": 100,
    "size": 100,
    "current": 1
  }
}
```

---

#### 取消订单

**请求**
```http
POST /api/admin/orders/{id}/cancel
Authorization: Bearer <token>
```

**路径参数**
| 参数名 | 类型 | 必填 | 说明 | 示例 |
|--------|------|------|------|------|
| id | long | 是 | 订单ID | 1 |

**请求参数**：无

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

### 举报处理接口 `/api/admin/reports`

#### 获取待处理举报

**请求**
```http
GET /api/admin/reports?pageNo=1&pageSize=50
Authorization: Bearer <token>
```

**Query参数**
| 参数名 | 类型 | 必填 | 说明 | 默认值 |
|--------|------|------|------|--------|
| pageNo | int | 否 | 页码 | 1 |
| pageSize | int | 否 | 每页条数 | 50 |

**响应参数**
| 参数名 | 类型 | 说明 |
|--------|------|------|
| code | int | 响应状态码 |
| data | object | 分页数据对象 |
| data.records | array | 举报列表 |
| data.total | long | 总记录数 |
| data.size | long | 每页条数 |
| data.current | long | 当前页码 |

**data.records 数组元素**
| 参数名 | 类型 | 说明 |
|--------|------|------|
| id | long | 举报ID |
| goodsId | long | 被举报商品ID |
| goodsTitle | string | 被举报商品标题 |
| reporterId | long | 举报人ID |
| reporterNickname | string | 举报人昵称 |
| reason | string | 举报原因 |
| status | string | 处理状态，PENDING-待处理，HANDLED-已处理 |
| handleResult | string | 处理结果 |
| createdAt | string | 举报时间，格式：yyyy-MM-dd HH:mm:ss |

**响应示例**
```json
{
  "code": 0,
  "data": {
    "records": [
      {
        "id": 1,
        "goodsId": 10,
        "goodsTitle": "iPhone 14 Pro",
        "reporterId": 5,
        "reporterNickname": "举报人",
        "reason": "商品信息虚假",
        "status": "PENDING",
        "handleResult": "",
        "createdAt": "2026-04-20 10:00:00"
      }
    ],
    "total": 5,
    "size": 50,
    "current": 1
  }
}
```

---

#### 处理举报

**请求**
```http
POST /api/admin/reports/{id}/handle
Content-Type: application/json
Authorization: Bearer <token>
```

**路径参数**
| 参数名 | 类型 | 必填 | 说明 | 示例 |
|--------|------|------|------|------|
| id | long | 是 | 举报ID | 1 |

**请求参数**
| 参数名 | 类型 | 必填 | 说明 | 示例 |
|--------|------|------|------|------|
| handled | boolean | 是 | 处理结果，true-举报成立，false-举报驳回 | true |
| result | string | 是 | 处理说明 | "举报成立，商品已下架" |

**请求示例（举报成立）**
```json
{
  "handled": true,
  "result": "举报成立，商品已下架"
}
```

**请求示例（举报驳回）**
```json
{
  "handled": false,
  "result": "举报不成立，经核实商品信息真实"
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
  "message": "处理成功"
}
```

---

### 公告管理接口 `/api/admin/notices`

#### 获取公告列表

**请求**
```http
GET /api/admin/notices?pageNo=1&pageSize=100
Authorization: Bearer <token>
```

**Query参数**
| 参数名 | 类型 | 必填 | 说明 | 默认值 |
|--------|------|------|------|--------|
| pageNo | int | 否 | 页码 | 1 |
| pageSize | int | 否 | 每页条数 | 100 |

**响应参数**
| 参数名 | 类型 | 说明 |
|--------|------|------|
| code | int | 响应状态码 |
| data | object | 分页数据对象 |
| data.records | array | 公告列表 |
| data.total | long | 总记录数 |
| data.size | long | 每页条数 |
| data.current | long | 当前页码 |

**data.records 数组元素**
| 参数名 | 类型 | 说明 |
|--------|------|------|
| id | long | 公告ID |
| title | string | 公告标题 |
| content | string | 公告内容 |
| coverUrl | string | 封面图片URL |
| status | int | 状态，1-发布，0-下线 |
| publishedAt | string | 发布时间，格式：yyyy-MM-dd HH:mm:ss |
| createdAt | string | 创建时间 |

**响应示例**
```json
{
  "code": 0,
  "data": {
    "records": [
      {
        "id": 1,
        "title": "平台服务升级通知",
        "content": "尊敬的用户，平台将于本周日凌晨进行服务升级...",
        "coverUrl": "https://example.com/notice.jpg",
        "status": 1,
        "publishedAt": "2026-04-20 10:00:00",
        "createdAt": "2026-04-20 09:00:00"
      }
    ],
    "total": 10,
    "size": 100,
    "current": 1
  }
}
```

---

#### 发布公告

**请求**
```http
POST /api/admin/notices
Content-Type: application/json
Authorization: Bearer <token>
```

**请求参数**
| 参数名 | 类型 | 必填 | 说明 | 示例 |
|--------|------|------|------|------|
| title | string | 是 | 公告标题 | "平台服务升级通知" |
| content | string | 是 | 公告内容 | "尊敬的用户..." |
| coverUrl | string | 否 | 封面图片URL | "https://..." |
| status | int | 否 | 状态，默认1（发布） | 1 |

**请求示例**
```json
{
  "title": "平台服务升级通知",
  "content": "尊敬的用户，平台将于本周日凌晨进行服务升级，届时部分功能将暂停使用。",
  "coverUrl": "https://example.com/notice.jpg",
  "status": 1
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
  "message": "保存成功"
}
```

---

#### 删除公告

**请求**
```http
DELETE /api/admin/notices/{id}
Authorization: Bearer <token>
```

**路径参数**
| 参数名 | 类型 | 必填 | 说明 | 示例 |
|--------|------|------|------|------|
| id | long | 是 | 公告ID | 1 |

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

## 项目结构

```
second-hub-admin/
├── public/
│   └── favicon.ico          # 网站图标
├── src/
│   ├── assets/
│   │   ├── base.css         # 基础样式
│   │   ├── logo.svg         # Logo
│   │   └── main.css         # 主样式
│   ├── components/          # 公共组件
│   │   ├── ChartComponent.vue   # ECharts图表组件
│   │   ├── icons/           # 图标组件
│   │   └── ...
│   ├── layout/
│   │   └── AdminLayout.vue  # 管理后台布局
│   ├── router/
│   │   └── index.js          # 路由配置
│   ├── stores/
│   │   └── auth.js          # 认证状态管理
│   ├── styles/
│   │   └── global.css       # 全局样式
│   ├── utils/
│   │   └── request.js       # axios请求封装
│   ├── views/               # 页面视图
│   │   ├── DashboardView.vue    # 数据统计
│   │   ├── GoodsAuditView.vue   # 商品审核
│   │   ├── CategoryView.vue     # 分类管理
│   │   ├── UserView.vue         # 用户管理
│   │   ├── OrderView.vue        # 订单监管
│   │   ├── ReportView.vue       # 举报处理
│   │   ├── NoticeView.vue       # 公告管理
│   │   └── LoginView.vue        # 登录页
│   ├── App.vue              # 根组件
│   └── main.js              # 入口文件
├── index.html               # HTML模板
├── package.json             # 项目配置
├── vite.config.js           # Vite配置
└── README.md                # 项目文档
```

## 默认管理员账号

| 账号 | 密码 |
|------|------|
| admin | 123456 |

## 开发指南

### 添加新页面
1. 在 `src/views/` 下创建 Vue 组件
2. 在 `src/router/index.js` 中添加路由
3. 在 `src/layout/AdminLayout.vue` 中添加菜单项

### 添加图表
项目已集成 ECharts，可直接使用：
```vue
<template>
  <v-chart :option="chartOption" style="height: 350px" />
</template>

<script setup>
import { computed } from 'vue'

const chartOption = computed(() => ({
  tooltip: { trigger: 'axis' },
  xAxis: { type: 'category', data: ['Mon', 'Tue', 'Wed'] },
  yAxis: { type: 'value' },
  series: [{ data: [120, 200, 150], type: 'line' }]
}))
</script>
```

## 许可证

MIT License
