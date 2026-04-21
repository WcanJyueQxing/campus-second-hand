# Second Hub Client - 微信小程序

## 项目简介

`second-hub-client` 是校园二手交易平台的微信小程序用户端，基于原生小程序框架开发，提供商品浏览、发布、收藏、订单交易等功能。

## 技术栈

| 类别 | 技术 |
|------|------|
| 框架 | 原生微信小程序 |
| 微信 SDK | 3.0.2 |
| 本地存储 | wx.getStorageSync |
| HTTP | wx.request |
| 登录 | 微信一键登录 |

## 项目结构

```
second-hub-client/
├── pages/
│   ├── index/           # 首页
│   ├── home/           # 发现页（商品列表）
│   ├── goods-detail/   # 商品详情
│   ├── goods-publish/  # 发布商品
│   ├── favorites/      # 我的收藏
│   ├── orders/         # 我的订单
│   ├── mine/          # 个人中心
│   ├── login/         # 登录页
│   └── logs/          # 日志页
├── custom-tab-bar/    # 自定义底部导航
├── utils/
│   ├── request.js     # 请求封装
│   └── util.js        # 工具函数
├── app.js             # 应用入口
├── app.json           # 应用配置
├── app.wxss           # 全局样式
└── project.config.json # 项目配置
```

## 页面说明

### 1. 首页 (index)
- 小程序入口页面
- 欢迎页面
- 跳转链接

### 2. 发现页 (home)
- 商品分类筛选
- 商品列表展示
- 下拉刷新
- 上拉加载更多
- 商品搜索

### 3. 商品详情 (goods-detail)
- 商品图片轮播
- 商品信息展示（价格、描述）
- 卖家信息
- 收藏按钮
- 留言区域
- 下单按钮

### 4. 发布商品 (goods-publish)
- 商品信息填写（标题、描述、价格）
- 分类选择
- 图片上传
- 发布/提交审核

### 5. 我的收藏 (favorites)
- 收藏商品列表
- 取消收藏
- 查看商品详情

### 6. 我的订单 (orders)
- 订单状态筛选（全部/待支付/待确认/已完成）
- 订单列表
- 订单操作（支付/确认/取消）

### 7. 个人中心 (mine)
- 用户信息展示
- 微信一键登录
- 我的发布
- 退出登录

### 8. 登录页 (login)
- 微信授权登录
- 用户信息绑定

## 功能模块

### 用户模块
- 微信一键登录（自动注册）
- 个人信息查看
- 退出登录

### 商品模块
- 商品列表浏览
- 商品详情查看
- 商品搜索
- 分类筛选
- 发布商品
- 商品下架

### 收藏模块
- 添加收藏
- 取消收藏
- 收藏列表

### 留言模块
- 留言列表
- 发布留言

### 订单模块
- 创建订单
- 模拟支付
- 确认收货
- 取消订单
- 订单列表

### 举报模块
- 举报商品

## 底部导航

自定义底部导航栏，包含：

- **首页** (index)
- **发现** (home) - 商品列表
- **发布** (goods-publish)
- **订单** (orders)
- **我的** (mine)

## API 对接

### 请求封装

位于 `utils/request.js`，使用微信原生 `wx.request`：

```javascript
const BASE_URL = 'http://127.0.0.1:8080'

// 自动携带 Token
// 统一错误处理
```

### 认证机制

登录成功后，Token 存储在本地：

```javascript
wx.setStorageSync('token', token)
wx.setStorageSync('userInfo', userInfo)
```

### 登录流程

1. 调用微信 `wx.login()` 获取 code
2. 将 code 发送到后端
3. 后端调用微信接口验证
4. 返回自定义登录态 Token
5. 存储 Token 到本地

## 启动步骤

### 前置条件

1. **安装微信开发者工具**
2. **后端服务已启动** (http://127.0.0.1:8080)

### 导入项目

1. 打开微信开发者工具
2. 点击"导入项目"
3. 选择 `second-hub-client` 目录
4. AppID 填写你的小程序 AppID（或测试号）

### 配置服务器域名

在微信开发者工具中：

1. 点击右上角"详情"
2. 选择"本地配置"
3. 勾选"不校验合法域名、web-view（业务域名）、TLS 版本以及 HTTPS 证书"

### 启动项目

点击"编译"按钮，预览效果。

## 开发注意事项

### 1. 本地调试

开发阶段需要在微信开发者工具中：

- 勾选"不校验合法域名"（仅开发阶段）
- 使用手机预览时需配合内网穿透

### 2. 内网穿透

微信小程序无法直接访问 `127.0.0.1`，需要使用内网穿透：

```bash
# 使用 ngrok
ngrok http 8080

# 或使用 cpolar
cpolar http 8080
```

然后修改 `utils/request.js` 中的 `BASE_URL` 为穿透后的地址。

### 3. 用户体验

- 图片加载使用占位图
- 请求使用 loading 提示
- 错误信息友好提示

### 4. 数据结构

#### 商品信息
```javascript
{
  id: 1,
  title: '商品标题',
  price: 99.00,
  description: '商品描述',
  coverImage: 'https://...',
  images: ['https://...'],
  status: 'APPROVED',
  user: {
    id: 1,
    nickname: '卖家'
  },
  category: {
    id: 1,
    name: '手机数码'
  }
}
```

#### 订单信息
```javascript
{
  id: 1,
  orderNo: 'ORDER20260421001',
  goods: {...},
  amount: 99.00,
  orderStatus: 'PENDING_PAYMENT',
  payStatus: 'UNPAID',
  buyerConfirmed: false,
  sellerConfirmed: false
}
```

## 页面生命周期

| 页面 | 关键生命周期 |
|------|-------------|
| home | onShow - 每次显示刷新数据 |
| goods-detail | onShow - 刷新收藏状态 |
| orders | onShow - 刷新订单状态 |
| mine | onShow - 检查登录状态 |

## 状态管理

使用微信本地存储管理状态：

| Key | 说明 |
|-----|------|
| token | 登录凭证 |
| userInfo | 用户信息 |
| cartCount | 购物车数量（预留） |

## 注意事项

1. **用户隐私**：需在小程序隐私政策中说明用户信息使用
2. **支付功能**：当前为模拟支付，生产环境需对接微信支付
3. **图片上传**：需后端文件存储服务正常运行

## 默认测试账号

微信登录会自动创建测试用户，无需手动注册。

## 联系方式

如有问题，请查阅项目文档或联系开发团队。
