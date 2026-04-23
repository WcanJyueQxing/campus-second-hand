# 校园二手交易平台 - 后端服务

## 项目简介

`second-hub-server` 是校园二手交易平台的后端服务系统，基于 Spring Boot 3.2 构建，提供用户认证、商品管理、订单交易、互动社交等核心功能，支持微信小程序客户端和管理后台的API调用。

## 技术栈

| 技术 | 版本 | 说明 |
|------|------|------|
| Spring Boot | 3.2.x | 核心框架 |
| MyBatis-Plus | 3.5.x | ORM框架 |
| Druid | 1.2.x | 数据库连接池 |
| JWT | 0.12.x | 身份认证 |
| Redis | 3.x | 缓存（可选） |
| Lombok | 1.18.x | 简化代码 |
| Maven | 3.8+ | 项目构建 |

## 项目结构

```
second-hub-server/
├── src/main/java/com/nie/secondhub/
│   ├── common/                    # 公共模块
│   │   ├── context/              # 上下文（登录用户）
│   │   ├── enums/                # 枚举类
│   │   ├── exception/            # 异常处理
│   │   └── response/             # 统一响应
│   ├── config/                    # 配置类
│   │   ├── DruidConfig.java      # Druid连接池配置
│   │   ├── JwtProperties.java    # JWT配置
│   │   ├── MybatisPlusConfig.java # MyBatis-Plus配置
│   │   ├── OpenApiConfig.java    # 开放接口配置
│   │   ├── StorageProperties.java # 文件存储配置
│   │   └── WebMvcConfig.java     # Web配置（CORS等）
│   ├── controller/               # 控制器层
│   │   ├── admin/                # 管理后台接口
│   │   └── user/                # 用户端接口
│   ├── dto/                      # 数据传输对象
│   ├── entity/                  # 数据库实体
│   ├── mapper/                  # MyBatis Mapper
│   ├── security/                # 安全认证
│   ├── service/                 # 业务逻辑层
│   ├── util/                    # 工具类
│   └── vo/                      # 视图对象
├── src/main/resources/
│   ├── application.yml          # 主配置文件
│   └── sql/
│       └── db_second_hub.sql    # 数据库脚本
└── pom.xml                      # Maven配置
```

## 核心功能

### 1. 认证模块
- 微信小程序登录
- 账号密码登录
- 管理员登录
- JWT Token认证

### 2. 用户模块
- 用户注册与登录
- 用户信息管理

### 3. 商品模块
- 商品发布（需审核）
- 商品编辑与删除
- 商品上下架
- 商品浏览与搜索

### 4. 订单模块
- 创建订单
- 支付订单（模拟）
- 卖家发货确认
- 买家收货确认
- 取消订单

### 5. 互动模块
- 商品收藏/取消收藏
- 商品评论
- 商品举报

### 6. 管理后台
- 数据统计
- 商品审核
- 分类管理
- 用户管理
- 订单监管
- 举报处理
- 公告管理

---

## 接口文档

### 通用说明

**基础URL**: `http://localhost:8080`

**认证方式**: 需要认证的接口需要在请求头中携带 JWT Token：
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

## 用户端 API

### 认证接口 `/api/user/auth`

#### 微信登录

**请求**
```http
POST /api/user/auth/wx-login
Content-Type: application/json
```

**请求参数**
| 参数名 | 类型 | 必填 | 说明 | 示例 |
|--------|------|------|------|------|
| code | string | 是 | 微信授权code，通过wx.login()获取 | "0611A..."

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
  "data": {
    "id": 1,
    "nickname": "用户昵称",
    "avatarUrl": "https://example.com/avatar.jpg",
    "token": "eyJhbGciOiJIUzI1NiJ9..."
  }
}
```

---

#### 发送验证码

**请求**
```http
POST /api/user/auth/send-code
Content-Type: application/json
```

**请求参数**
| 参数名 | 类型 | 必填 | 说明 | 示例 |
|--------|------|------|------|------|
| phone | string | 是 | 手机号 | "13800000001" |

**请求示例**
```json
{
  "phone": "13800000001"
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
  "message": "发送成功"
}
```

---

#### 注册账号

**请求**
```http
POST /api/user/auth/register
Content-Type: application/json
```

**请求参数**
| 参数名 | 类型 | 必填 | 说明 | 示例 |
|--------|------|------|------|------|
| phone | string | 是 | 手机号 | "13800000001" |
| password | string | 是 | 密码 | "123456" |
| code | string | 是 | 短信验证码 | "123456" |

**请求示例**
```json
{
  "phone": "13800000001",
  "password": "123456",
  "code": "123456"
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
  "message": "注册成功"
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
| sort | int | 排序值 |
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

#### 获取公告列表

**请求**
```http
GET /api/user/public/notices?pageNo=1&pageSize=10
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
| data.records | array | 公告列表 |
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
        "title": "平台服务升级",
        "content": "尊敬的用户...",
        "coverUrl": "https://example.com/notice.jpg",
        "status": 1,
        "publishedAt": "2026-04-20 10:00:00"
      }
    ],
    "total": 10,
    "pageNo": 1,
    "pageSize": 10
  }
}
```

---

### 商品接口 `/api/user/goods`

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

#### 商品列表（搜索）

**请求**
```http
GET /api/user/goods/list?pageNo=1&pageSize=10&keyword=iPhone&categoryId=1&sortBy=newest
Authorization: Bearer <token>
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

#### 商品详情

**请求**
```http
GET /api/user/goods/{goodsId}
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

## 管理后台 API

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
| password | string | 是 | 密码 | "123456" |

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
| code | int | 响应状态码 |
| data | object | 管理员信息 |
| data.id | long | 管理员ID |
| data.nickname | string | 管理员昵称 |
| data.role | string | 角色标识 |
| data.token | string | JWT令牌 |

**响应示例**
```json
{
  "code": 0,
  "data": {
    "id": 1,
    "nickname": "系统管理员",
    "role": "ADMIN",
    "token": "eyJhbGciOiJIUzI1NiJ9..."
  }
}
```

---

### 数据统计 `/api/admin/dashboard`

#### 概览数据

**请求**
```http
GET /api/admin/dashboard/overview
Authorization: Bearer <token>
```

**响应参数**
| 参数名 | 类型 | 说明 |
|--------|------|------|
| code | int | 响应状态码 |
| data | object | 统计数据对象 |
| data.userCount | long | 用户总数 |
| data.goodsCount | long | 商品总数 |
| data.pendingGoodsCount | long | 待审核商品数 |
| data.orderCount | long | 订单总数 |
| data.reportCount | long | 待处理举报数 |
| data.categoryDistribution | array | 分类分布 |
| data.orderStatusDistribution | array | 订单状态分布 |
| data.userStatusDistribution | array | 用户状态分布 |

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
      { "categoryName": "手机数码", "goodsCount": 120 }
    ],
    "orderStatusDistribution": [
      { "status": "PENDING_PAYMENT", "count": 10 }
    ],
    "userStatusDistribution": [
      { "status": 1, "count": 95 }
    ]
  }
}
```

---

#### 近7日趋势

**请求**
```http
GET /api/admin/dashboard/trend
Authorization: Bearer <token>
```

**响应参数**
| 参数名 | 类型 | 说明 |
|--------|------|------|
| code | int | 响应状态码 |
| data | array | 趋势数据列表 |

**data 数组元素**
| 参数名 | 类型 | 说明 |
|--------|------|------|
| date | string | 日期 |
| userCount | int | 新增用户数 |
| goodsCount | int | 新增商品数 |
| orderCount | int | 新增订单数 |

**响应示例**
```json
{
  "code": 0,
  "data": [
    { "date": "2026-04-17", "userCount": 5, "goodsCount": 20, "orderCount": 8 }
  ]
}
```

---

### 分类管理 `/api/admin/categories`

#### 分类列表

**请求**
```http
GET /api/admin/categories
Authorization: Bearer <token>
```

**响应参数**
| 参数名 | 类型 | 说明 |
|--------|------|------|
| code | int | 响应状态码 |
| data | array | 分类列表 |

**响应示例**
```json
{
  "code": 0,
  "data": [
    { "id": 1, "name": "手机数码", "sort": 100, "status": 1 }
  ]
}
```

---

#### 新增分类

**请求**
```http
POST /api/admin/categories
Authorization: Bearer <token>
Content-Type: application/json
```

**请求参数**
| 参数名 | 类型 | 必填 | 说明 | 示例 |
|--------|------|------|------|------|
| name | string | 是 | 分类名称 | "手机数码" |
| sort | int | 否 | 排序值 | 100 |
| status | int | 否 | 状态 | 1 |

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

**响应示例**
```json
{
  "code": 0,
  "message": "删除成功"
}
```

---

### 商品审核 `/api/admin/goods`

#### 待审核商品

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

**响应示例**
```json
{
  "code": 0,
  "data": {
    "records": [...],
    "total": 10,
    "pageNo": 1,
    "pageSize": 50
  }
}
```

---

#### 审核商品

**请求**
```http
POST /api/admin/goods/{id}/audit
Authorization: Bearer <token>
Content-Type: application/json
```

**请求参数**
| 参数名 | 类型 | 必填 | 说明 | 示例 |
|--------|------|------|------|------|
| approved | boolean | 是 | 审核结果 | true |
| reason | string | 否 | 拒绝原因 | "信息不符" |

**响应示例**
```json
{
  "code": 0,
  "message": "操作成功"
}
```

---

### 用户管理 `/api/admin/users`

#### 用户列表

**请求**
```http
GET /api/admin/users?pageNo=1&pageSize=100
Authorization: Bearer <token>
```

**响应参数**
| 参数名 | 类型 | 说明 |
|--------|------|------|
| code | int | 响应状态码 |
| data | object | 分页数据对象 |

**响应示例**
```json
{
  "code": 0,
  "data": {
    "records": [...],
    "total": 50,
    "pageNo": 1,
    "pageSize": 100
  }
}
```

---

#### 更新用户状态

**请求**
```http
POST /api/admin/users/{id}/status
Authorization: Bearer <token>
Content-Type: application/json
```

**请求参数**
| 参数名 | 类型 | 必填 | 说明 | 示例 |
|--------|------|------|------|------|
| status | int | 是 | 状态 | 0 |

**响应示例**
```json
{
  "code": 0,
  "message": "状态已更新"
}
```

---

### 订单监管 `/api/admin/orders`

#### 订单列表

**请求**
```http
GET /api/admin/orders?pageNo=1&pageSize=100
Authorization: Bearer <token>
```

**响应参数**
| 参数名 | 类型 | 说明 |
|--------|------|------|
| code | int | 响应状态码 |
| data | object | 分页数据对象 |

**响应示例**
```json
{
  "code": 0,
  "data": {
    "records": [...],
    "total": 100,
    "pageNo": 1,
    "pageSize": 100
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

**响应示例**
```json
{
  "code": 0,
  "message": "订单已取消"
}
```

---

### 举报处理 `/api/admin/reports`

#### 举报列表

**请求**
```http
GET /api/admin/reports?pageNo=1&pageSize=50
Authorization: Bearer <token>
```

**响应参数**
| 参数名 | 类型 | 说明 |
|--------|------|------|
| code | int | 响应状态码 |
| data | object | 分页数据对象 |

**响应示例**
```json
{
  "code": 0,
  "data": {
    "records": [...],
    "total": 5,
    "pageNo": 1,
    "pageSize": 50
  }
}
```

---

#### 处理举报

**请求**
```http
POST /api/admin/reports/{id}/handle
Authorization: Bearer <token>
Content-Type: application/json
```

**请求参数**
| 参数名 | 类型 | 必填 | 说明 | 示例 |
|--------|------|------|------|------|
| handled | boolean | 是 | 处理结果 | true |
| result | string | 是 | 处理说明 | "举报成立" |

**响应示例**
```json
{
  "code": 0,
  "message": "处理成功"
}
```

---

### 公告管理 `/api/admin/notices`

#### 公告列表

**请求**
```http
GET /api/admin/notices?pageNo=1&pageSize=100
Authorization: Bearer <token>
```

**响应参数**
| 参数名 | 类型 | 说明 |
|--------|------|------|
| code | int | 响应状态码 |
| data | object | 分页数据对象 |

**响应示例**
```json
{
  "code": 0,
  "data": {
    "records": [...],
    "total": 10,
    "pageNo": 1,
    "pageSize": 100
  }
}
```

---

#### 发布公告

**请求**
```http
POST /api/admin/notices
Authorization: Bearer <token>
Content-Type: application/json
```

**请求参数**
| 参数名 | 类型 | 必填 | 说明 | 示例 |
|--------|------|------|------|------|
| title | string | 是 | 公告标题 | "服务升级" |
| content | string | 是 | 公告内容 | "尊敬的用户..." |
| coverUrl | string | 否 | 封面URL | "https://..." |
| status | int | 否 | 状态 | 1 |

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

**响应示例**
```json
{
  "code": 0,
  "message": "删除成功"
}
```

---

## 数据模型

### 商品状态枚举
| 状态 | 说明 |
|------|------|
| PENDING | 待审核 |
| APPROVED | 已通过 |
| REJECTED | 已拒绝 |
| OFFLINE | 已下架 |

### 订单状态枚举
| 状态 | 说明 |
|------|------|
| PENDING_PAYMENT | 待支付 |
| PAID | 已支付 |
| SHIPPED | 已发货 |
| COMPLETED | 已完成 |
| CANCELLED | 已取消 |

### 支付状态枚举
| 状态 | 说明 |
|------|------|
| UNPAID | 未支付 |
| PAID | 已支付 |
| REFUNDED | 已退款 |

---

## 快速开始

### 环境要求
- JDK 17+
- MySQL 8.0+
- Maven 3.8+

### 配置文件

修改 `src/main/resources/application.yml`：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/second_hub
    username: root
    password: your_password
```

### 构建与运行

```bash
mvn clean package -DskipTests
java -jar target/second-hub-server-0.0.1-SNAPSHOT.jar
```

---

## 默认账号

| 角色 | 账号 | 密码 |
|------|------|------|
| 管理员 | admin | 123456 |
| 用户 | 13800000001 | 123456 |

---

## 许可证

MIT License
