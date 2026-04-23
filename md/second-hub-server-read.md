# Second Hub Server - 后端服务

## 项目简介

`second-hub-server` 是校园二手交易平台的后端服务，基于 Spring Boot 3.2.12 构建，提供 RESTful API 接口，支持用户端和管理端业务。

## 技术栈

| 类别 | 技术 |
|------|------|
| 框架 | Spring Boot 3.2.12 |
| ORM | MyBatis-Plus 3.5.6 |
| 数据库连接池 | Druid 1.2.23 |
| 数据库 | MySQL 8.0 |
| 缓存 | Redis |
| 安全 | JWT (jjwt 0.12.6) |
| 文档 | Knife4j 4.5.0 |
| Java 版本 | 17+ |

## 项目结构

```
second-hub-server/
├── src/main/java/com/nie/secondhub/
│   ├── common/           # 公共组件
│   │   ├── context/      # 登录上下文
│   │   ├── enums/        # 枚举定义
│   │   ├── exception/     # 异常处理
│   │   └── response/     # 统一响应
│   ├── config/           # 配置类
│   ├── controller/       # 控制器
│   │   ├── admin/        # 管理端接口
│   │   └── user/         # 用户端接口
│   ├── dto/              # 数据传输对象
│   ├── entity/           # 实体类
│   ├── mapper/           # 数据访问层
│   ├── security/         # 安全相关
│   ├── service/          # 业务逻辑
│   └── util/             # 工具类
└── src/main/resources/
    ├── sql/              # 数据库脚本
    └── application.yml   # 应用配置
```

## 接口域划分

| 接口前缀 | 说明 | 认证 |
|---------|------|------|
| `/api/user/public/**` | 公开接口 | 无需认证 |
| `/api/user/**` | 用户私有接口 | 需要 JWT Token |
| `/api/admin/**` | 管理端接口 | 需要 JWT Token |

## 核心功能模块

### 用户端 (User)

- **认证模块** - 微信登录
- **商品模块** - 发布、编辑、删除、上架下架、列表、详情
- **收藏模块** - 收藏/取消收藏、收藏列表
- **留言模块** - 发布留言、留言列表
- **订单模块** - 创建订单、支付、确认收货、取消
- **举报模块** - 举报商品

### 管理端 (Admin)

- **认证模块** - 管理员登录
- **商品审核** - 待审核列表、审核通过/驳回
- **用户管理** - 用户列表、禁用/启用
- **分类管理** - 分类增删改
- **订单管理** - 订单列表、订单详情
- **举报管理** - 举报列表、处理举报
- **公告管理** - 公告增删改
- **数据统计** - 仪表盘数据

## 数据库配置

```yaml
spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://127.0.0.1:3306/db_second_hub
    username: root
    password: 123456
```

## Redis 配置

```yaml
spring:
  data:
    redis:
      host: 127.0.0.1
      port: 6379
      database: 0
      password: 123456
```

## 启动步骤

### 前置条件

1. **安装 JDK 17+**
2. **安装 MySQL 8.0+**
3. **安装 Redis**
4. **安装 Maven 3.9+**

### 启动步骤

1. **创建数据库**
```sql
CREATE DATABASE db_second_hub DEFAULT CHARACTER SET utf8mb4;
```

2. **执行初始化脚本**
```bash
mysql -u root -p db_second_hub < src/main/resources/sql/db_second_hub.sql
```

3. **配置数据库连接**
修改 `src/main/resources/application.yml` 中的数据库连接信息。

4. **启动服务**
```bash
cd second-hub-server
mvn spring-boot:run
```

或直接运行主类 `SecondHubServerApplication`

5. **访问接口文档**
- Swagger UI: http://127.0.0.1:8080/swagger-ui.html
- Knife4j: http://127.0.0.1:8080/doc.html

## 默认账号

| 角色 | 用户名 | 密码 |
|------|--------|------|
| 超级管理员 | admin | 123456 |
| 测试用户A | 13800000001 | 123456 |
| 测试用户B | 13800000002 | 123456 |

## Druid 监控

启动后访问 http://127.0.0.1:8080/druid/

- 用户名: admin
- 密码: 123456

## API 统一响应格式

```json
{
  "code": 0,
  "message": "success",
  "data": {},
  "timestamp": "2026-04-21T10:00:00"
}
```

| code | 说明 |
|------|------|
| 0 | 成功 |
| 401 | 未授权/认证失败 |
| 403 | 禁止访问 |
| 500 | 服务器内部错误 |

## 商品状态流转

```
DRAFT(草稿) -> PENDING(待审核) -> APPROVED(已上架)
                     ↓
               REJECTED(已驳回) -> PENDING(重新提交)
                                       
APPROVED -> OFFLINE(已下架) -> PENDING(重新上架)
    ↓
SOLD(已售出)
```

## 订单状态流转

```
PENDING_PAYMENT(待支付) -> PAID(已支付) -> SELLER_CONFIRMED(卖家已确认) -> COMPLETED(已完成)
                            ↓
                      BUYER_CONFIRMED(买家已确认)
                             ↓
                         COMPLETED(已完成)

PENDING_PAYMENT -> CANCELLED(已取消)
```

## 联系方式

如有问题，请查阅项目文档或联系开发团队。
