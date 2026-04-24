# 验证码功能实现说明

## 1. 技术选型

本项目采用 **kaptcha** 作为验证码生成库，原因如下：

| 方案 | 优点 | 缺点 |
|------|------|------|
| EasyCaptcha | 使用简单 | JDK 17+ 需要额外添加 nashorn 引擎，且存在 engine is null 问题 |
| **kaptcha** | 成熟稳定，配置灵活，无需额外引擎 | 配置相对复杂 |

## 2. 后端实现

### 2.1 依赖配置 (pom.xml)

```xml
<!-- 验证码生成工具：kaptcha -->
<dependency>
    <groupId>com.github.whvcse</groupId>
    <artifactId>kaptcha</artifactId>
    <version>2.3.3</version>
    <exclusions>
        <exclusion>
            <artifactId>servlet-api</artifactId>
            <groupId>javax.servlet</groupId>
        </exclusion>
    </exclusions>
</dependency>
```

### 2.2 核心文件

| 文件 | 路径 | 说明 |
|------|------|------|
| CaptchaConfig | `com.nie.secondhub.config.CaptchaConfig` | kaptcha 配置类，定义字符型/数学型两种验证码生成器 |
| KaptchaTextCreator | `com.nie.secondhub.config.KaptchaTextCreator` | 数学验证码文本生成器，生成算术表达式 |
| CaptchaController | `com.nie.secondhub.controller.user.CaptchaController` | 验证码接口，提供生成和验证功能 |

### 2.3 验证码类型

#### 字符型验证码
- 随机生成 4 位字母数字组合
- 如：`A3B7`、`9K2M`

#### 数学计算型验证码
- 生成简单算术计算题
- 如：`3+5=?`、`8-2=?`
- 答案存储在 Redis，格式为 `算术表达式@答案`

### 2.4 接口说明

#### 生成验证码
```
GET /api/user/captcha/generate
GET /api/user/captcha/generate?type=char  (字符型)
GET /api/user/captcha/generate?type=math  (数学计算型)
```

响应：
```json
{
  "uuid": "唯一标识符",
  "captcha": "data:image/png;base64,xxx",
  "codeLength": 4
}
```

#### 验证验证码
```
GET /api/user/captcha/verify?uuid=xxx&code=用户输入的验证码
```

### 2.5 Redis 存储

- Key 格式：`captcha_code_{uuid}`
- 过期时间：5 分钟
- 验证成功后自动删除

## 3. 前端实现

### 3.1 小程序页面

| 文件 | 说明 |
|------|------|
| login.wxml | 验证码输入框和图片显示 |
| login.js | 验证码加载和刷新逻辑 |
| login.wxss | 验证码样式 |

### 3.2 登录流程

1. 页面加载时自动调用 `/api/user/captcha/generate` 获取验证码
2. 用户输入账号、密码和验证码
3. 点击登录，携带 `captchaCode` 和 `captchaUuid` 提交
4. 后端校验验证码，成功后继续登录逻辑

## 4. 拦截器配置

验证码接口已在 `AuthInterceptor` 中放行：

```java
|| path.startsWith("/api/user/captcha/")
```

## 5. 测试步骤

1. **重启 Redis**：
   ```powershell
   cd F:\Redis\Redis-x64-3.2.100
   redis-server
   ```

2. **重启后端服务**：
   ```bash
   cd second-hub-server
   mvn clean compile
   mvn spring-boot:run
   ```

3. **测试验证码接口**：
   浏览器访问：`http://localhost:8080/api/user/captcha/generate`

4. **小程序测试**：
   - 清缓存
   - 重新编译
   - 打开登录页

## 6. 常见问题

### Q: 验证码不显示？
- 检查 Redis 是否运行
- 检查后端日志是否有错误
- 确认拦截器已放行 `/api/user/captcha/`

### Q: 验证码总是错误？
- 验证码有效期 5 分钟，超时需重新获取
- 验证码只能使用一次，验证后自动删除
- 忽略大小写比较

### Q: Redis 连接失败？
- 确保 Redis 服务已启动
- 检查 `application.yml` 中的 Redis 配置
