# Second Hub Server - 校园二手交易平台后端服务

> 校园二手交易平台的 Spring Boot 后端服务，提供 RESTful API 接口。

## 📖 项目简介

Second Hub Server 是校园二手交易平台的后端服务系统，基于 Spring Boot 3.2 构建，为微信小程序和管理后台提供统一的 API 接口服务。

### 核心特性

- 🏗️ **分层架构**：Controller → Service → Mapper 分层设计，职责清晰
- 🔐 **安全认证**：JWT Token 身份认证，支持用户和管理员双重体系
- 📊 **数据可视化**：提供统计接口，支持数据分析
- 🖼️ **文件存储**：支持本地文件存储和云存储扩展
- 📱 **微信支持**：集成微信登录能力
- ⚡ **性能优化**：集成 Redis 缓存，提升响应速度
- 📝 **接口文档**：清晰的 RESTful API 设计

---

## 🛠 技术栈

### 核心框架

| 技术 | 版本 | 说明 |
|------|------|------|
| Spring Boot | 3.2.x | 后端核心框架 |
| MyBatis-Plus | 3.5.x | ORM 框架，简化数据库操作 |
| MySQL | 8.0+ | 关系型数据库 |
| Redis | 6.0+ | 缓存数据库 |

### 安全认证

| 技术 | 说明 |
|------|------|
| JWT | JSON Web Token 身份认证 |
| MD5 | 密码加密存储 |

### 工具库

| 技术 | 版本 | 说明 |
|------|------|------|
| Lombok | 1.18.x | 简化 Java 代码 |
|hutool | 5.x | Java 工具类库 |

### 开发工具

| 技术 | 说明 |
|------|------|
| Druid | 数据库连接池 |
| Kaptcha | 图形验证码生成 |

---

## 📁 项目结构

```
second-hub-server/
├── src/main/java/com/nie/secondhub/
│   ├── SecondHubServerApplication.java     # 应用启动类
│   │
│   ├── config/                           # 配置类
│   │   ├── CaptchaConfig.java           # 验证码配置
│   │   ├── DruidConfig.java             # Druid连接池配置
│   │   ├── JwtProperties.java           # JWT属性配置
│   │   ├── KaptchaTextCreator.java       # 验证码文本生成器
│   │   ├── MybatisPlusConfig.java       # MyBatis-Plus配置
│   │   ├── OpenApiConfig.java           # 开放接口配置
│   │   ├── StorageProperties.java       # 存储配置
│   │   ├── WebMvcConfig.java            # Web MVC配置
│   │   └── WechatProperties.java        # 微信配置
│   │
│   ├── common/                          # 通用组件
│   │   ├── context/                    # 上下文
│   │   │   ├── LoginUser.java          # 登录用户信息
│   │   │   └── LoginUserHolder.java    # 用户上下文持有器
│   │   ├── enums/                      # 枚举类
│   │   │   ├── FeedbackStatus.java     # 反馈状态枚举
│   │   │   ├── FeedbackType.java       # 反馈类型枚举
│   │   │   ├── GoodsStatus.java        # 商品状态枚举
│   │   │   ├── OrderStatus.java        # 订单状态枚举
│   │   │   ├── PayStatus.java          # 支付状态枚举
│   │   │   └── RoleType.java           # 角色类型枚举
│   │   ├── exception/                   # 异常处理
│   │   │   ├── BizException.java       # 业务异常
│   │   │   └── GlobalExceptionHandler.java  # 全局异常处理器
│   │   └── response/                   # 响应封装
│   │       ├── ApiResponse.java        # 统一响应格式
│   │       └── PageResponse.java       # 分页响应格式
│   │
│   ├── controller/                      # 控制器层
│   │   ├── admin/                      # 管理员接口
│   │   │   ├── AdminAuthController.java      # 管理员认证
│   │   │   ├── AdminCategoryController.java  # 分类管理
│   │   │   ├── AdminDashboardController.java # 仪表盘统计
│   │   │   ├── AdminFeedbackController.java  # 反馈管理
│   │   │   ├── AdminFileController.java      # 文件管理
│   │   │   ├── AdminGoodsController.java     # 商品管理
│   │   │   ├── AdminNoticeController.java    # 公告管理
│   │   │   ├── AdminOrderController.java    # 订单管理
│   │   │   ├── AdminReportController.java    # 举报管理
│   │   │   └── AdminUserController.java      # 用户管理
│   │   │
│   │   └── user/                       # 用户端接口
│   │       ├── AddressController.java        # 地址管理
│   │       ├── CaptchaController.java        # 验证码
│   │       ├── FeedbackController.java      # 意见反馈
│   │       ├── GoodsFavoriteController.java  # 收藏管理
│   │       ├── HistoryRecordController.java # 浏览历史
│   │       ├── NotificationController.java  # 通知设置
│   │       ├── OrderController.java         # 订单操作
│   │       ├── RandomNameController.java    # 随机姓名
│   │       ├── UserAuthController.java       # 用户认证
│   │       ├── UserFileController.java      # 文件上传
│   │       ├── UserGoodsController.java     # 商品管理
│   │       ├── UserInfoController.java      # 用户信息
│   │       ├── UserInteractionController.java # 互动功能
│   │       ├── UserOrderController.java      # 订单管理
│   │       ├── UserOrderStatsController.java # 订单统计
│   │       ├── UserPrivacyController.java    # 隐私设置
│   │       ├── UserPublicController.java     # 公共接口
│   │       └── UserSecurityController.java   # 账户安全
│   │
│   ├── dto/                            # 数据传输对象
│   │   ├── PageQuery.java             # 分页查询基类
│   │   ├── admin/                     # 管理员DTO
│   │   │   ├── AdminLoginRequest.java
│   │   │   ├── CategorySaveRequest.java
│   │   │   ├── FeedbackHandleRequest.java
│   │   │   ├── GoodsAuditRequest.java
│   │   │   ├── NoticeSaveRequest.java
│   │   │   ├── ReportHandleRequest.java
│   │   │   └── UserStatusUpdateRequest.java
│   │   ├── req/                       # 请求DTO
│   │   │   └── CommentCreateRequest.java
│   │   └── user/                      # 用户DTO
│   │       ├── AccountLoginRequest.java
│   │       ├── CommentCreateRequest.java
│   │       ├── GoodsQueryRequest.java
│   │       ├── GoodsSaveRequest.java
│   │       ├── HistoryRecordRequest.java
│   │       ├── OrderCreateRequest.java
│   │       ├── RegisterRequest.java
│   │       ├── ReportCreateRequest.java
│   │       ├── UserUpdateRequest.java
│   │       └── WxLoginRequest.java
│   │
│   ├── entity/                         # 数据库实体
│   │   ├── Address.java               # 收货地址
│   │   ├── AdminUser.java             # 管理员
│   │   ├── Category.java              # 商品分类
│   │   ├── Feedback.java              # 用户反馈
│   │   ├── Goods.java                 # 商品
│   │   ├── GoodsAudit.java            # 商品审核记录
│   │   ├── GoodsComment.java          # 商品评论
│   │   ├── GoodsFavorite.java         # 商品收藏
│   │   ├── GoodsImage.java            # 商品图片
│   │   ├── GoodsReport.java           # 商品举报
│   │   ├── HistoryRecord.java         # 浏览历史
│   │   ├── Notice.java                # 系统公告
│   │   ├── TradeOrder.java            # 交易订单
│   │   ├── User.java                  # 用户
│   │   └── UserProfile.java           # 用户资料
│   │
│   ├── mapper/                        # 数据访问层
│   │   ├── AddressMapper.java
│   │   ├── AdminUserMapper.java
│   │   ├── CategoryMapper.java
│   │   ├── FeedbackMapper.java
│   │   ├── GoodsAuditMapper.java
│   │   ├── GoodsCommentMapper.java
│   │   ├── GoodsFavoriteMapper.java
│   │   ├── GoodsImageMapper.java
│   │   ├── GoodsMapper.java
│   │   ├── GoodsReportMapper.java
│   │   ├── HistoryRecordMapper.java
│   │   ├── NoticeMapper.java
│   │   ├── TradeOrderMapper.java
│   │   ├── UserMapper.java
│   │   └── UserProfileMapper.java
│   │
│   ├── security/                       # 安全认证
│   │   ├── AuthInterceptor.java        # 认证拦截器
│   │   └── JwtTokenUtil.java           # JWT工具类
│   │
│   ├── service/                        # 业务逻辑层
│   │   ├── AddressService.java
│   │   ├── AdminOpsService.java
│   │   ├── AuthService.java
│   │   ├── CommentService.java
│   │   ├── FeedbackService.java
│   │   ├── FileStorageService.java
│   │   ├── GoodsFavoriteService.java
│   │   ├── GoodsService.java
│   │   ├── HistoryRecordService.java
│   │   ├── InteractionService.java
│   │   ├── OrderService.java
│   │   ├── impl/                      # 服务实现
│   │   │   ├── AddressServiceImpl.java
│   │   │   ├── AdminOpsServiceImpl.java
│   │   │   ├── AuthServiceImpl.java
│   │   │   ├── CommentServiceImpl.java
│   │   │   ├── FeedbackServiceImpl.java
│   │   │   ├── GoodsFavoriteServiceImpl.java
│   │   │   ├── GoodsServiceImpl.java
│   │   │   ├── HistoryRecordServiceImpl.java
│   │   │   ├── InteractionServiceImpl.java
│   │   │   ├── LocalFileStorageServiceImpl.java
│   │   │   └── OrderServiceImpl.java
│   │   └── support/
│   │       └── WechatAuthClient.java   # 微信认证客户端
│   │
│   ├── util/                           # 工具类
│   │   ├── Md5Util.java               # MD5加密工具
│   │   ├── PageUtil.java              # 分页工具
│   │   └── Result.java                # 统一响应工具
│   │
│   ├── utils/                          # 工具类
│   │   └── NameInitializer.java       # 姓名初始化工具
│   │
│   └── vo/                             # 视图对象
│       ├── CommentVO.java
│       ├── DashboardVO.java
│       ├── FeedbackVO.java
│       ├── GoodsDetailVO.java
│       ├── GoodsVO.java
│       ├── HistoryRecordVO.java
│       ├── LoginVO.java
│       └── OrderVO.java
│
├── src/main/resources/
│   ├── application.yml                 # 应用配置文件
│   └── mapper/                        # MyBatis XML映射文件
│       ├── FeedbackMapper.xml
│       ├── GoodsFavoriteMapper.xml
│       ├── GoodsMapper.xml
│       └── HistoryRecordMapper.xml
│
├── src/test/java/                     # 测试类
├── uploads/                           # 上传文件存储目录
├── pom.xml                           # Maven项目配置
└── README.md                         # 项目说明文档
```

---

## 🚀 快速开始

### 环境要求

- JDK 17+
- Maven 3.6+
- MySQL 8.0+
- Redis 6.0+（可选，用于缓存）

### 1. 导入数据库

```bash
# 登录MySQL
mysql -u root -p

# 创建数据库
CREATE DATABASE second_hub DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

# 导入数据库脚本
source sql/db_second_hub.sql
```

### 2. 修改配置

编辑 `src/main/resources/application.yml`：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/second_hub?useUnicode=true&characterEncoding=utf8
    username: your_username
    password: your_password

  redis:
    host: localhost
    port: 6379

server:
  port: 8080
```

### 3. 编译运行

```bash
# 编译项目
mvn clean package -DskipTests

# 运行项目
mvn spring-boot:run

# 或直接运行jar包
java -jar target/second-hub-server-0.0.1-SNAPSHOT.jar
```

### 4. 验证服务

访问 `http://localhost:8080/api/user/captcha/generate` 验证服务是否启动成功。

---

## ⚙️ 配置说明

### 主要配置项

| 配置项 | 说明 | 默认值 |
|--------|------|--------|
| `server.port` | 服务端口 | 8080 |
| `spring.datasource.url` | 数据库连接地址 | - |
| `spring.datasource.username` | 数据库用户名 | - |
| `spring.datasource.password` | 数据库密码 | - |
| `spring.redis.host` | Redis主机地址 | localhost |
| `spring.redis.port` | Redis端口 | 6379 |
| `jwt.secret` | JWT加密密钥 | - |
| `jwt.expiration` | Token过期时间 | 86400000ms |
| `storage.local.path` | 本地存储路径 | ./uploads |

### 管理员账号

首次启动后，系统会初始化默认管理员账号：

| 字段 | 值 |
|------|-----|
| 用户名 | admin |
| 密码 | admin123 |

> ⚠️ 生产环境请务必修改默认密码！

---

## 📊 数据库设计

### 主要数据表

| 表名 | 说明 |
|------|------|
| `user` | 用户信息表 |
| `user_profile` | 用户资料表 |
| `admin_user` | 管理员表 |
| `goods` | 商品表 |
| `goods_image` | 商品图片表 |
| `trade_order` | 交易订单表 |
| `goods_favorite` | 商品收藏表 |
| `goods_comment` | 商品评论表 |
| `goods_report` | 商品举报表 |
| `goods_audit` | 商品审核记录表 |
| `category` | 商品分类表 |
| `notice` | 系统公告表 |
| `address` | 收货地址表 |
| `history_record` | 浏览历史表 |
| `feedback` | 意见反馈表 |

详细表结构请参考：`src/main/resources/sql/db_second_hub.sql`

---

## 🔌 API 接口概览

### 用户端接口 (`/api/user/*`)

| 模块 | 路径 | 说明 |
|------|------|------|
| 认证 | `/api/user/auth/*` | 登录、注册、微信登录 |
| 商品 | `/api/user/goods/*` | 商品列表、详情、发布、编辑 |
| 订单 | `/api/user/orders/*` | 创建订单、支付、确认 |
| 收藏 | `/api/user/favorites/*` | 收藏/取消收藏 |
| 评论 | `/api/user/comments/*` | 评论管理 |
| 用户 | `/api/user/info` | 用户信息管理 |
| 地址 | `/api/address/*` | 收货地址管理 |
| 历史 | `/api/user/history` | 浏览历史 |
| 反馈 | `/api/feedback` | 意见反馈 |
| 公共 | `/api/user/public/*` | 分类、公告等公开信息 |

### 管理端接口 (`/api/admin/*`)

| 模块 | 路径 | 说明 |
|------|------|------|
| 认证 | `/api/admin/auth/*` | 管理员登录 |
| 仪表盘 | `/api/admin/dashboard/*` | 数据统计 |
| 商品 | `/api/admin/goods/*` | 商品审核、管理 |
| 用户 | `/api/admin/users/*` | 用户管理 |
| 订单 | `/api/admin/orders/*` | 订单监管 |
| 分类 | `/api/admin/categories/*` | 分类管理 |
| 公告 | `/api/admin/notices/*` | 公告管理 |
| 举报 | `/api/admin/reports/*` | 举报处理 |
| 反馈 | `/api/admin/feedback/*` | 反馈管理 |

详细接口文档请参考：[API接口文档](../API接口文档.md)

---

## 🏗️ 核心模块

### 1. 认证模块 (Auth)

- **用户认证**：账号密码登录、微信登录
- **管理员认证**：独立的Admin Token体系
- **JWT Token**：统一的身份认证机制

### 2. 商品模块 (Goods)

- **商品发布**：支持多图上传，自动生成封面
- **商品审核**：待审核 → 通过/拒绝 → 上架
- **商品状态**：审核中、已通过、已驳回、已下架

### 3. 订单模块 (Order)

- **订单流程**：创建 → 支付 → 卖家确认 → 买家确认 → 完成
- **状态管理**：待支付、已支付、卖家已确认、买家已确认、已完成、已取消
- **退款处理**：支持退款状态管理

### 4. 收藏模块 (Favorite)

- **Toggle模式**：已收藏则取消，未收藏则添加
- **列表查询**：分页展示用户收藏商品

### 5. 统计模块 (Dashboard)

- **数据概览**：用户数、商品数、订单数等
- **趋势分析**：按日期统计新增数量
- **分布统计**：订单状态、用户状态分布

---

## 📝 开发指南

### 项目分层

```
Controller层：接收请求，参数校验，调用Service
    ↓
Service层：业务逻辑处理，事务管理
    ↓
Mapper层：数据库操作，SQL编写
```

### 新增接口流程

1. **创建 Entity**：在 `entity` 包下创建实体类
2. **创建 Mapper**：在 `mapper` 包下创建 Mapper 接口
3. **创建 Service**：在 `service` 包下创建 Service 接口和实现
4. **创建 Controller**：在 `controller` 包下创建 Controller
5. **创建 VO/DTO**：根据需要创建视图或传输对象

### 代码规范

1. 所有实体类使用 Lombok 注解
2. Service 层添加 `@Service` 注解
3. Controller 层添加 `@RestController` 注解
4. 使用统一响应格式 `Result.success()` / `Result.error()`

### 统一响应格式

```java
// 成功响应
return Result.success(data);
return Result.success("操作成功", data);

// 失败响应
return Result.error("操作失败");
return Result.error(500, "服务器内部错误");
```

---

## 🔒 安全说明

1. **密码加密**：用户密码使用 MD5 加密存储
2. **Token认证**：使用 JWT 进行身份认证
3. **参数校验**：使用 `@Valid` 注解进行参数校验
4. **SQL注入防护**：使用 MyBatis-Plus 防止 SQL 注入
5. **XSS防护**：对用户输入进行过滤处理

---

## 📈 性能优化

1. **数据库连接池**：使用 Druid 连接池
2. **Redis缓存**：对热点数据进行缓存
3. **分页查询**：大量数据使用分页查询
4. **索引优化**：为常用查询字段添加索引

## 
