# 数据库脚本说明文档

> Second Hub 校园二手交易平台数据库设计说明

## 📖 概述

本目录包含校园二手交易平台的数据库初始化脚本，用于创建和初始化项目所需的数据库表结构和初始数据。

## 📁 文件结构

```
sql/
├── db_second_hub.sql            # 完整数据库初始化脚本
└── README.md                    # 数据库设计说明文档
```

## 🚀 数据库初始化

### 1. 创建数据库

```sql
CREATE DATABASE second_hub 
DEFAULT CHARACTER SET utf8mb4 
COLLATE utf8mb4_unicode_ci;
```

### 2. 使用数据库

```sql
USE second_hub;
```

### 3. 执行初始化脚本

```bash
mysql -u username -p second_hub < db_second_hub.sql
```

## 📊 数据表结构

### 数据关系图

```
用户相关
├── user (用户表)
├── user_profile (用户资料表)
└── admin_user (管理员表)

商品相关
├── category (商品分类表)
├── goods (商品表)
├── goods_image (商品图片表)
├── goods_comment (商品评论表)
├── goods_favorite (商品收藏表)
├── goods_report (商品举报表)
└── goods_audit (商品审核记录表)

订单相关
└── trade_order (交易订单表)

其他
├── address (收货地址表)
├── history_record (浏览历史表)
├── feedback (意见反馈表)
└── notice (系统公告表)
```

### 核心数据表说明

#### 1. 用户表 (`user`)

| 字段名 | 类型 | 约束 | 说明 |
|--------|------|------|------|
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | 用户ID |
| phone | VARCHAR(11) | UNIQUE, NOT NULL | 用户手机号 |
| password | VARCHAR(32) | NOT NULL | MD5加密后的密码 |
| nickname | VARCHAR(50) | NOT NULL | 用户昵称 |
| avatar_url | VARCHAR(255) | - | 头像URL |
| status | TINYINT | DEFAULT 1 | 用户状态：0-禁用，1-正常 |
| role | TINYINT | DEFAULT 0 | 用户角色：0-普通用户，1-管理员 |
| created_at | DATETIME | NOT NULL | 创建时间 |
| updated_at | DATETIME | NOT NULL | 更新时间 |

#### 2. 用户资料表 (`user_profile`)

| 字段名 | 类型 | 约束 | 说明 |
|--------|------|------|------|
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | 主键ID |
| user_id | BIGINT | FOREIGN KEY, UNIQUE | 用户ID |
| real_name | VARCHAR(50) | - | 真实姓名 |
| student_id | VARCHAR(20) | - | 学号 |
| gender | TINYINT | DEFAULT 0 | 性别：0-未知，1-男，2-女 |
| department | VARCHAR(100) | - | 院系专业 |
| dormitory | VARCHAR(50) | - | 宿舍地址 |
| signature | VARCHAR(200) | - | 个性签名 |
| created_at | DATETIME | NOT NULL | 创建时间 |
| updated_at | DATETIME | NOT NULL | 更新时间 |

#### 3. 管理员表 (`admin_user`)

| 字段名 | 类型 | 约束 | 说明 |
|--------|------|------|------|
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | 管理员ID |
| username | VARCHAR(50) | UNIQUE, NOT NULL | 管理员用户名 |
| password | VARCHAR(32) | NOT NULL | MD5加密后的密码 |
| nickname | VARCHAR(50) | NOT NULL | 管理员昵称 |
| role | TINYINT | DEFAULT 1 | 角色类型：1-超级管理员，2-普通管理员 |
| status | TINYINT | DEFAULT 1 | 状态：0-禁用，1-正常 |
| created_at | DATETIME | NOT NULL | 创建时间 |
| updated_at | DATETIME | NOT NULL | 更新时间 |

#### 4. 商品分类表 (`category`)

| 字段名 | 类型 | 约束 | 说明 |
|--------|------|------|------|
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | 分类ID |
| name | VARCHAR(50) | UNIQUE, NOT NULL | 分类名称 |
| icon | VARCHAR(255) | - | 分类图标URL |
| sort_order | INT | DEFAULT 0 | 排序顺序 |
| status | TINYINT | DEFAULT 1 | 状态：0-禁用，1-启用 |
| created_at | DATETIME | NOT NULL | 创建时间 |
| updated_at | DATETIME | NOT NULL | 更新时间 |

#### 5. 商品表 (`goods`)

| 字段名 | 类型 | 约束 | 说明 |
|--------|------|------|------|
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | 商品ID |
| user_id | BIGINT | FOREIGN KEY | 发布者ID |
| category_id | BIGINT | FOREIGN KEY | 分类ID |
| title | VARCHAR(200) | NOT NULL | 商品标题 |
| description | TEXT | - | 商品描述 |
| price | DECIMAL(10,2) | NOT NULL | 商品价格 |
| original_price | DECIMAL(10,2) | - | 原价 |
| quantity | INT | DEFAULT 1 | 商品数量 |
| status | TINYINT | DEFAULT 0 | 状态：0-待审核，1-已通过，2-已驳回，3-已下架 |
| cover_image | VARCHAR(255) | - | 封面图片URL |
| views | INT | DEFAULT 0 | 浏览次数 |
| sold_count | INT | DEFAULT 0 | 售出数量 |
| created_at | DATETIME | NOT NULL | 创建时间 |
| updated_at | DATETIME | NOT NULL | 更新时间 |
| deleted_at | DATETIME | - | 删除时间（软删除） |

#### 6. 商品图片表 (`goods_image`)

| 字段名 | 类型 | 约束 | 说明 |
|--------|------|------|------|
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | 图片ID |
| goods_id | BIGINT | FOREIGN KEY | 商品ID |
| image_url | VARCHAR(255) | NOT NULL | 图片URL |
| sort_order | INT | DEFAULT 0 | 排序顺序 |
| created_at | DATETIME | NOT NULL | 创建时间 |

#### 7. 商品评论表 (`goods_comment`)

| 字段名 | 类型 | 约束 | 说明 |
|--------|------|------|------|
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | 评论ID |
| goods_id | BIGINT | FOREIGN KEY | 商品ID |
| user_id | BIGINT | FOREIGN KEY | 评论者ID |
| content | VARCHAR(500) | NOT NULL | 评论内容 |
| rating | TINYINT | DEFAULT 5 | 评分（1-5星） |
| created_at | DATETIME | NOT NULL | 创建时间 |
| deleted_at | DATETIME | - | 删除时间（软删除） |

#### 8. 商品收藏表 (`goods_favorite`)

| 字段名 | 类型 | 约束 | 说明 |
|--------|------|------|------|
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | 收藏ID |
| user_id | BIGINT | FOREIGN KEY | 用户ID |
| goods_id | BIGINT | FOREIGN KEY | 商品ID |
| is_deleted | TINYINT | DEFAULT 0 | 是否删除：0-正常，1-已删除 |
| created_at | DATETIME | NOT NULL | 创建时间 |
| updated_at | DATETIME | NOT NULL | 更新时间 |

#### 9. 商品举报表 (`goods_report`)

| 字段名 | 类型 | 约束 | 说明 |
|--------|------|------|------|
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | 举报ID |
| goods_id | BIGINT | FOREIGN KEY | 商品ID |
| user_id | BIGINT | FOREIGN KEY | 举报者ID |
| reason | VARCHAR(500) | NOT NULL | 举报理由 |
| status | TINYINT | DEFAULT 0 | 状态：0-待处理，1-已处理 |
| created_at | DATETIME | NOT NULL | 创建时间 |
| handled_at | DATETIME | - | 处理时间 |

#### 10. 商品审核记录表 (`goods_audit`)

| 字段名 | 类型 | 约束 | 说明 |
|--------|------|------|------|
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | 审核记录ID |
| goods_id | BIGINT | FOREIGN KEY | 商品ID |
| admin_id | BIGINT | FOREIGN KEY | 审核管理员ID |
| status | TINYINT | NOT NULL | 审核结果：1-通过，2-驳回 |
| remark | VARCHAR(500) | - | 审核备注/驳回理由 |
| created_at | DATETIME | NOT NULL | 创建时间 |

#### 11. 交易订单表 (`trade_order`)

| 字段名 | 类型 | 约束 | 说明 |
|--------|------|------|------|
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | 订单ID |
| order_no | VARCHAR(32) | UNIQUE, NOT NULL | 订单编号 |
| goods_id | BIGINT | FOREIGN KEY | 商品ID |
| buyer_id | BIGINT | FOREIGN KEY | 买家ID |
| seller_id | BIGINT | FOREIGN KEY | 卖家ID |
| price | DECIMAL(10,2) | NOT NULL | 成交价格 |
| quantity | INT | DEFAULT 1 | 购买数量 |
| total_amount | DECIMAL(10,2) | NOT NULL | 订单总额 |
| status | TINYINT | DEFAULT 0 | 订单状态：0-待支付，1-已支付，2-卖家已确认，3-买家已确认，4-已完成，5-已取消 |
| pay_status | TINYINT | DEFAULT 0 | 支付状态：0-未支付，1-已支付 |
| address_id | BIGINT | FOREIGN KEY | 收货地址ID |
| remark | VARCHAR(200) | - | 订单备注 |
| created_at | DATETIME | NOT NULL | 创建时间 |
| paid_at | DATETIME | - | 支付时间 |
| seller_confirmed_at | DATETIME | - | 卖家确认时间 |
| buyer_confirmed_at | DATETIME | - | 买家确认时间 |
| canceled_at | DATETIME | - | 取消时间 |

#### 12. 收货地址表 (`address`)

| 字段名 | 类型 | 约束 | 说明 |
|--------|------|------|------|
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | 地址ID |
| user_id | BIGINT | FOREIGN KEY | 用户ID |
| name | VARCHAR(50) | NOT NULL | 收货人姓名 |
| phone | VARCHAR(11) | NOT NULL | 联系电话 |
| province | VARCHAR(50) | NOT NULL | 省份 |
| city | VARCHAR(50) | NOT NULL | 城市 |
| district | VARCHAR(50) | NOT NULL | 区县 |
| detail | VARCHAR(200) | NOT NULL | 详细地址 |
| is_default | TINYINT | DEFAULT 0 | 是否默认：0-否，1-是 |
| created_at | DATETIME | NOT NULL | 创建时间 |
| updated_at | DATETIME | NOT NULL | 更新时间 |

#### 13. 浏览历史表 (`history_record`)

| 字段名 | 类型 | 约束 | 说明 |
|--------|------|------|------|
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | 记录ID |
| user_id | BIGINT | FOREIGN KEY | 用户ID |
| goods_id | BIGINT | FOREIGN KEY | 商品ID |
| visited_at | DATETIME | NOT NULL | 访问时间 |

#### 14. 意见反馈表 (`feedback`)

| 字段名 | 类型 | 约束 | 说明 |
|--------|------|------|------|
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | 反馈ID |
| user_id | BIGINT | FOREIGN KEY | 用户ID |
| type | TINYINT | NOT NULL | 反馈类型：1-功能建议，2-问题反馈，3-其他 |
| content | TEXT | NOT NULL | 反馈内容 |
| contact | VARCHAR(50) | - | 联系方式 |
| status | TINYINT | DEFAULT 0 | 状态：0-待处理，1-已处理，2-已回复 |
| reply | TEXT | - | 回复内容 |
| created_at | DATETIME | NOT NULL | 创建时间 |
| handled_at | DATETIME | - | 处理时间 |

#### 15. 系统公告表 (`notice`)

| 字段名 | 类型 | 约束 | 说明 |
|--------|------|------|------|
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | 公告ID |
| title | VARCHAR(100) | NOT NULL | 公告标题 |
| content | TEXT | NOT NULL | 公告内容 |
| is_top | TINYINT | DEFAULT 0 | 是否置顶：0-否，1-是 |
| status | TINYINT | DEFAULT 1 | 状态：0-禁用，1-启用 |
| created_at | DATETIME | NOT NULL | 创建时间 |
| updated_at | DATETIME | NOT NULL | 更新时间 |

## 🔤 数据字典

### 商品状态 (`goods.status`)

| 值 | 描述 | 说明 |
|----|------|------|
| 0 | 待审核 | 用户发布后等待审核 |
| 1 | 已通过 | 审核通过，商品上架 |
| 2 | 已驳回 | 审核不通过，商品下架 |
| 3 | 已下架 | 商品主动下架 |

### 订单状态 (`trade_order.status`)

| 值 | 描述 | 说明 |
|----|------|------|
| 0 | 待支付 | 订单创建，等待支付 |
| 1 | 已支付 | 买家已支付，等待卖家确认 |
| 2 | 卖家已确认 | 卖家确认收到款项 |
| 3 | 买家已确认 | 买家确认收到商品 |
| 4 | 已完成 | 交易完成 |
| 5 | 已取消 | 订单取消 |

### 支付状态 (`trade_order.pay_status`)

| 值 | 描述 | 说明 |
|----|------|------|
| 0 | 未支付 | 订单未支付 |
| 1 | 已支付 | 订单已支付 |

### 用户状态 (`user.status`)

| 值 | 描述 | 说明 |
|----|------|------|
| 0 | 禁用 | 用户被禁用 |
| 1 | 正常 | 用户正常 |

### 反馈类型 (`feedback.type`)

| 值 | 描述 | 说明 |
|----|------|------|
| 1 | 功能建议 | 用户提出功能建议 |
| 2 | 问题反馈 | 用户反馈问题 |
| 3 | 其他 | 其他类型 |

### 反馈状态 (`feedback.status`)

| 值 | 描述 | 说明 |
|----|------|------|
| 0 | 待处理 | 等待处理 |
| 1 | 已处理 | 已处理但未回复 |
| 2 | 已回复 | 已处理并回复用户 |

## 🔗 表关系图

```
user 1:1 user_profile    -- 用户与用户资料：一对一
user 1:N goods           -- 用户与商品：一对多
user 1:N trade_order(buyer) -- 用户与订单(买家)：一对多
user 1:N trade_order(seller) -- 用户与订单(卖家)：一对多
user 1:N address         -- 用户与地址：一对多
user 1:N goods_comment   -- 用户与评论：一对多
user 1:N goods_favorite  -- 用户与收藏：一对多
user 1:N goods_report    -- 用户与举报：一对多
user 1:N history_record  -- 用户与浏览历史：一对多
user 1:N feedback        -- 用户与反馈：一对多

category 1:N goods       -- 分类与商品：一对多

goods 1:N goods_image    -- 商品与图片：一对多
goods 1:N goods_comment  -- 商品与评论：一对多
goods 1:N goods_favorite -- 商品与收藏：一对多
goods 1:N goods_report   -- 商品与举报：一对多
goods 1:N trade_order    -- 商品与订单：一对多
goods 1:N goods_audit    -- 商品与审核记录：一对多

admin_user 1:N goods_audit -- 管理员与审核记录：一对多
admin_user 1:N feedback(reply) -- 管理员与反馈回复：一对多

trade_order 1:1 address  -- 订单与地址：一对一
```

## 📝 索引设计

### 单字段索引

| 表名 | 字段 | 说明 |
|------|------|------|
| user | phone | 用户登录查询 |
| user | nickname | 用户昵称搜索 |
| goods | category_id | 分类筛选 |
| goods | status | 状态筛选 |
| goods | user_id | 用户商品列表 |
| trade_order | order_no | 订单号查询 |
| trade_order | buyer_id | 买家订单列表 |
| trade_order | seller_id | 卖家订单列表 |
| trade_order | status | 订单状态筛选 |

### 复合索引

| 表名 | 字段组合 | 说明 |
|------|----------|------|
| goods | status, created_at | 按状态和时间排序 |
| goods_favorite | user_id, is_deleted | 用户收藏列表 |
| goods_report | status, created_at | 举报列表排序 |
| feedback | status, created_at | 反馈列表排序 |

## 🗃️ 数据库配置建议

### 开发环境

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/second_hub?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai
    username: root
    password: root
    driver-class-name: com.mysql.cj.jdbc.Driver
```

### 生产环境

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/second_hub?useUnicode=true&characterEncoding=utf8&useSSL=true&serverTimezone=Asia/Shanghai
    username: second_hub_user
    password: your_strong_password
    driver-class-name: com.mysql.cj.jdbc.Driver
```
