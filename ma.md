# 验证码功能实现文档

## 1. 功能概述

验证码功能用于防止恶意攻击和自动化操作，提高系统安全性。本实现支持两种验证码类型：
- **字符型验证码**：随机生成字母数字组合
- **数学计算型验证码**：生成简单的数学计算题

## 2. 技术依赖

| 依赖项 | 版本/说明 | 用途 |
|--------|----------|------|
| Java | 17+ | 开发语言 |
| Spring Boot | 3.0+ | 基础框架 |
| Redis | 5.0+ | 存储验证码 |
| kaptcha | pro.fessional:kaptcha | 验证码生成库 |
| Spring Web | - | 提供 HTTP 接口 |

## 3. 依赖配置

在 `pom.xml` 文件中添加以下依赖：

```xml
<!-- 验证码 -->
<dependency>
    <groupId>pro.fessional</groupId>
    <artifactId>kaptcha</artifactId>
    <version>2.3.3</version>
    <exclusions>
        <exclusion>
            <artifactId>servlet-api</artifactId>
            <groupId>javax.servlet</groupId>
        </exclusion>
    </exclusions>
</dependency>

<!-- Redis 缓存 -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>

<!-- Spring Web -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
```

## 4. 核心配置文件

### 4.1 验证码配置类

创建 `CaptchaConfig.java` 文件：

```java
package com.example.config;

import java.util.Properties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import static com.google.code.kaptcha.Constants.*;

/**
 * 验证码配置
 */
@Configuration
public class CaptchaConfig {
    @Bean(name = "captchaProducer")
    public DefaultKaptcha getKaptchaBean() {
        DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
        Properties properties = new Properties();
        // 是否有边框 默认为true 我们可以自己设置yes，no
        properties.setProperty(KAPTCHA_BORDER, "yes");
        // 验证码文本字符颜色 默认为Color.BLACK
        properties.setProperty(KAPTCHA_TEXTPRODUCER_FONT_COLOR, "black");
        // 验证码图片宽度 默认为200
        properties.setProperty(KAPTCHA_IMAGE_WIDTH, "160");
        // 验证码图片高度 默认为50
        properties.setProperty(KAPTCHA_IMAGE_HEIGHT, "60");
        // 验证码文本字符大小 默认为40
        properties.setProperty(KAPTCHA_TEXTPRODUCER_FONT_SIZE, "38");
        // KAPTCHA_SESSION_KEY
        properties.setProperty(KAPTCHA_SESSION_CONFIG_KEY, "kaptchaCode");
        // 验证码文本字符长度 默认为5
        properties.setProperty(KAPTCHA_TEXTPRODUCER_CHAR_LENGTH, "4");
        // 验证码文本字体样式 默认为new Font("Arial", 1, fontSize), new Font("Courier", 1, fontSize)
        properties.setProperty(KAPTCHA_TEXTPRODUCER_FONT_NAMES, "Arial,Courier");
        // 图片样式 水纹com.google.code.kaptcha.impl.WaterRipple 鱼眼com.google.code.kaptcha.impl.FishEyeGimpy 阴影com.google.code.kaptcha.impl.ShadowGimpy
        properties.setProperty(KAPTCHA_OBSCURIFICATOR_IMPL, "com.google.code.kaptcha.impl.ShadowGimpy");
        Config config = new Config(properties);
        defaultKaptcha.setConfig(config);
        return defaultKaptcha;
    }

    @Bean(name = "captchaProducerMath")
    public DefaultKaptcha getKaptchaBeanMath() {
        DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
        Properties properties = new Properties();
        // 是否有边框 默认为true 我们可以自己设置yes，no
        properties.setProperty(KAPTCHA_BORDER, "yes");
        // 边框颜色 默认为Color.BLACK
        properties.setProperty(KAPTCHA_BORDER_COLOR, "105,179,90");
        // 验证码文本字符颜色 默认为Color.BLACK
        properties.setProperty(KAPTCHA_TEXTPRODUCER_FONT_COLOR, "blue");
        // 验证码图片宽度 默认为200
        properties.setProperty(KAPTCHA_IMAGE_WIDTH, "160");
        // 验证码图片高度 默认为50
        properties.setProperty(KAPTCHA_IMAGE_HEIGHT, "60");
        // 验证码文本字符大小 默认为40
        properties.setProperty(KAPTCHA_TEXTPRODUCER_FONT_SIZE, "35");
        // KAPTCHA_SESSION_KEY
        properties.setProperty(KAPTCHA_SESSION_CONFIG_KEY, "kaptchaCodeMath");
        // 验证码文本生成器
        properties.setProperty(KAPTCHA_TEXTPRODUCER_IMPL, "com.example.config.KaptchaTextCreator");
        // 验证码文本字符间距 默认为2
        properties.setProperty(KAPTCHA_TEXTPRODUCER_CHAR_SPACE, "3");
        // 验证码文本字符长度 默认为5
        properties.setProperty(KAPTCHA_TEXTPRODUCER_CHAR_LENGTH, "6");
        // 验证码文本字体样式 默认为new Font("Arial", 1, fontSize), new Font("Courier", 1, fontSize)
        properties.setProperty(KAPTCHA_TEXTPRODUCER_FONT_NAMES, "Arial,Courier");
        // 验证码噪点颜色 默认为Color.BLACK
        properties.setProperty(KAPTCHA_NOISE_COLOR, "white");
        // 干扰实现类
        properties.setProperty(KAPTCHA_NOISE_IMPL, "com.google.code.kaptcha.impl.NoNoise");
        // 图片样式 水纹com.google.code.kaptcha.impl.WaterRipple 鱼眼com.google.code.kaptcha.impl.FishEyeGimpy 阴影com.google.code.kaptcha.impl.ShadowGimpy
        properties.setProperty(KAPTCHA_OBSCURIFICATOR_IMPL, "com.google.code.kaptcha.impl.ShadowGimpy");
        Config config = new Config(properties);
        defaultKaptcha.setConfig(config);
        return defaultKaptcha;
    }
}
```

### 4.2 数学验证码文本生成器

创建 `KaptchaTextCreator.java` 文件：

```java
package com.example.config;

import java.util.Random;
import com.google.code.kaptcha.text.impl.DefaultTextCreator;

/**
 * 验证码文本生成器
 */
public class KaptchaTextCreator extends DefaultTextCreator {
    private static final String[] CNUMBERS = "0,1,2,3,4,5,6,7,8,9,10".split(",");

    @Override
    public String getText() {
        Integer result = 0;
        Random random = new Random();
        int x = random.nextInt(10);
        int y = random.nextInt(10);
        StringBuilder suChinese = new StringBuilder();
        int randomoperands = random.nextInt(3);
        if (randomoperands == 0) {
            result = x * y;
            suChinese.append(CNUMBERS[x]);
            suChinese.append("*");
            suChinese.append(CNUMBERS[y]);
        }
        else if (randomoperands == 1) {
            if ((x != 0) && y % x == 0) {
                result = y / x;
                suChinese.append(CNUMBERS[y]);
                suChinese.append("/");
                suChinese.append(CNUMBERS[x]);
            }
            else {
                result = x + y;
                suChinese.append(CNUMBERS[x]);
                suChinese.append("+");
                suChinese.append(CNUMBERS[y]);
            }
        }
        else {
            if (x >= y) {
                result = x - y;
                suChinese.append(CNUMBERS[x]);
                suChinese.append("-");
                suChinese.append(CNUMBERS[y]);
            }
            else {
                result = y - x;
                suChinese.append(CNUMBERS[y]);
                suChinese.append("-");
                suChinese.append(CNUMBERS[x]);
            }
        }
        suChinese.append("=?@" + result);
        return suChinese.toString();
    }
}
```

### 4.3 验证码控制器

创建 `CaptchaController.java` 文件：

```java
package com.example.controller;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FastByteArrayOutputStream;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.google.code.kaptcha.Producer;
import com.example.common.config.AppConfig;
import com.example.common.constant.CacheConstants;
import com.example.common.constant.Constants;
import com.example.common.core.domain.AjaxResult;
import com.example.common.core.redis.RedisCache;
import com.example.common.utils.sign.Base64;
import com.example.common.utils.uuid.IdUtils;
import com.example.system.service.ISysConfigService;

/**
 * 验证码操作处理
 */
@RestController
public class CaptchaController {
    @Autowired
    private Producer captchaProducer;

    @Autowired
    private Producer captchaProducerMath;

    @Autowired
    private RedisCache redisCache;
    
    @Autowired
    private ISysConfigService configService;
    
    /**
     * 生成验证码
     */
    @GetMapping("/captchaImage")
    public AjaxResult getCode(HttpServletResponse response) throws IOException {
        AjaxResult ajax = AjaxResult.success();
        boolean captchaEnabled = configService.selectCaptchaEnabled();
        ajax.put("captchaEnabled", captchaEnabled);
        if (!captchaEnabled) {
            return ajax;
        }

        // 保存验证码信息
        String uuid = IdUtils.simpleUUID();
        String verifyKey = CacheConstants.CAPTCHA_CODE_KEY + uuid;

        String capStr = null, code = null;
        BufferedImage image = null;

        // 生成验证码
        String captchaType = AppConfig.getCaptchaType();
        if ("math".equals(captchaType)) {
            String capText = captchaProducerMath.createText();
            capStr = capText.substring(0, capText.lastIndexOf("@"));
            code = capText.substring(capText.lastIndexOf("@") + 1);
            image = captchaProducerMath.createImage(capStr);
        }
        else if ("char".equals(captchaType)) {
            capStr = code = captchaProducer.createText();
            image = captchaProducer.createImage(capStr);
        }

        redisCache.setCacheObject(verifyKey, code, Constants.CAPTCHA_EXPIRATION, TimeUnit.MINUTES);
        // 转换流信息写出
        FastByteArrayOutputStream os = new FastByteArrayOutputStream();
        try {
            ImageIO.write(image, "jpg", os);
        }
        catch (IOException e) {
            return AjaxResult.error(e.getMessage());
        }

        ajax.put("uuid", uuid);
        ajax.put("img", Base64.encode(os.toByteArray()));
        return ajax;
    }
}
```

## 5. 辅助类和配置

### 5.1 常量类

创建 `Constants.java` 文件：

```java
package com.example.common.constant;

/**
 * 通用常量信息
 */
public class Constants {
    // 验证码有效期（分钟）
    public static final Integer CAPTCHA_EXPIRATION = 2;
}
```

创建 `CacheConstants.java` 文件：

```java
package com.example.common.constant;

/**
 * 缓存常量
 */
public class CacheConstants {
    // 验证码缓存键
    public static final String CAPTCHA_CODE_KEY = "captcha_code_";
}
```

### 5.2 应用配置类

创建 `AppConfig.java` 文件：

```java
package com.example.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 应用配置
 */
@Component
public class AppConfig {
    // 验证码类型
    @Value("${app.captcha.type:char}")
    private String captchaType;
    
    public static String getCaptchaType() {
        return instance.captchaType;
    }
    
    private static AppConfig instance;
    
    public AppConfig() {
        instance = this;
    }
}
```

### 5.3 Redis 缓存工具类

创建 `RedisCache.java` 文件：

```java
package com.example.common.core.redis;

import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * Redis 缓存工具类
 */
@Component
public class RedisCache {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    
    /**
     * 缓存基本的对象
     */
    public <T> void setCacheObject(final String key, final T value, final long timeout, final TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, value, timeout, timeUnit);
    }
    
    /**
     * 获取缓存的基本对象
     */
    public <T> T getCacheObject(final String key) {
        return (T) redisTemplate.opsForValue().get(key);
    }
    
    /**
     * 删除缓存
     */
    public boolean deleteObject(final String key) {
        return redisTemplate.delete(key);
    }
}
```

### 5.4 工具类

创建 `IdUtils.java` 文件：

```java
package com.example.common.utils.uuid;

import java.util.UUID;

/**
 * ID工具类
 */
public class IdUtils {
    /**
     * 获取随机UUID
     */
    public static String simpleUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
```

创建 `Base64.java` 文件：

```java
package com.example.common.utils.sign;

import java.util.Base64;

/**
 * Base64工具类
 */
public class Base64 {
    /**
     * Base64编码
     */
    public static String encode(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }
    
    /**
     * Base64解码
     */
    public static byte[] decode(String str) {
        return Base64.getDecoder().decode(str);
    }
}
```

### 5.5 响应结果类

创建 `AjaxResult.java` 文件：

```java
package com.example.common.core.domain;

import java.util.HashMap;
import java.util.Map;

/**
 * 响应结果
 */
public class AjaxResult extends HashMap<String, Object> {
    private static final long serialVersionUID = 1L;
    
    public static AjaxResult success() {
        AjaxResult result = new AjaxResult();
        result.put("code", 200);
        result.put("msg", "操作成功");
        return result;
    }
    
    public static AjaxResult error(String msg) {
        AjaxResult result = new AjaxResult();
        result.put("code", 500);
        result.put("msg", msg);
        return result;
    }
    
    @Override
    public AjaxResult put(String key, Object value) {
        super.put(key, value);
        return this;
    }
}
```

### 5.6 配置文件

在 `application.yml` 中添加配置：

```yaml
# 应用配置
app:
  captcha:
    type: char  # char: 字符验证码, math: 数学验证码

# Redis配置
spring:
  redis:
    host: localhost
    port: 6379
    password: 
    database: 0

# 系统配置
system:
  captcha:
    enabled: true  # 是否启用验证码
```

## 6. 前端实现

### 6.1 验证码组件

```html
<template>
  <div class="captcha-container">
    <el-input
      v-model="loginForm.code"
      placeholder="验证码"
      style="width: 63%"
      prefix-icon="el-icon-lock"
      @keyup.enter="handleLogin"
    >
    </el-input>
    <div class="captcha-image" @click="getCode">
      <img :src="captchaImg" alt="验证码" />
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      loginForm: {
        code: '',
        uuid: ''
      },
      captchaImg: '',
      captchaEnabled: true
    }
  },
  created() {
    this.getCode()
  },
  methods: {
    getCode() {
      getCodeImg().then(res => {
        this.captchaEnabled = res.captchaEnabled
        if (this.captchaEnabled) {
          this.captchaImg = 'data:image/gif;base64,' + res.img
          this.loginForm.uuid = res.uuid
        }
      })
    },
    handleLogin() {
      // 登录逻辑
    }
  }
}
</script>

<style scoped>
.captcha-container {
  display: flex;
  align-items: center;
}
.captcha-image {
  width: 35%;
  height: 40px;
  margin-left: 2%;
  cursor: pointer;
}
.captcha-image img {
  width: 100%;
  height: 100%;
  border-radius: 4px;
}
</style>
```

### 6.2 API 调用

```javascript
import request from '@/utils/request'

export function getCodeImg() {
  return request({
    url: '/captchaImage',
    method: 'get'
  })
}

export function login(data) {
  return request({
    url: '/login',
    method: 'post',
    data
  })
}
```

## 7. 验证码验证

在登录接口中添加验证码验证：

```java
@PostMapping("/login")
public AjaxResult login(@RequestBody LoginBody loginBody) {
    // 验证码校验
    if (configService.selectCaptchaEnabled()) {
        validateCaptcha(loginBody.getCode(), loginBody.getUuid());
    }
    // 其他登录逻辑
}

/**
 * 验证码校验
 */
private void validateCaptcha(String code, String uuid) {
    String verifyKey = CacheConstants.CAPTCHA_CODE_KEY + uuid;
    String captcha = redisCache.getCacheObject(verifyKey);
    redisCache.deleteObject(verifyKey);
    
    if (captcha == null) {
        throw new CaptchaExpireException();
    }
    if (!code.equalsIgnoreCase(captcha)) {
        throw new CaptchaException();
    }
}
```

## 8. 异常类

创建验证码相关异常类：

```java
package com.example.common.exception.user;

/**
 * 验证码异常
 */
public class CaptchaException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    
    public CaptchaException() {
        super("验证码错误");
    }
    
    public CaptchaException(String message) {
        super(message);
    }
}

/**
 * 验证码过期异常
 */
public class CaptchaExpireException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    
    public CaptchaExpireException() {
        super("验证码已过期");
    }
    
    public CaptchaExpireException(String message) {
        super(message);
    }
}
```

## 9. 部署和测试

### 9.1 环境要求

- JDK 17+
- Redis 5.0+
- Maven 3.6+

### 9.2 部署步骤

1. 克隆代码到本地
2. 配置 Redis 连接
3. 构建项目：`mvn clean package -DskipTests`
4. 运行应用：`java -jar target/your-app.jar`
5. 访问 `http://localhost:8080/captchaImage` 测试验证码生成

### 9.3 测试验证

1. 访问验证码接口，检查返回结果
2. 验证验证码图片是否正确显示
3. 测试验证码验证功能
4. 验证验证码过期时间是否生效

## 10. 性能优化

1. **缓存优化**：使用 Redis 存储验证码，设置合理的过期时间
2. **图片优化**：调整验证码图片大小和复杂度，平衡安全性和性能
3. **并发优化**：使用 Redis 的原子操作确保验证码的唯一性和一致性
4. **异常处理**：合理处理验证码生成和验证过程中的异常

## 11. 安全建议

1. **验证码类型**：建议生产环境使用数学验证码，增加破解难度
2. **过期时间**：验证码过期时间不宜过长，建议 2-5 分钟
3. **图片干扰**：使用适当的图片干扰，增加识别难度
4. **访问频率限制**：对验证码接口进行访问频率限制，防止恶意请求
5. **Redis 安全**：确保 Redis 服务安全，设置密码和访问控制

## 12. 总结

本实现提供了一个完整的验证码功能，包括：

- 支持字符型和数学计算型两种验证码
- 使用 Redis 存储验证码，确保安全性和一致性
- 提供完整的前端和后端实现
- 易于集成到其他项目中

通过本实现，您可以在任何 Spring Boot 项目中快速集成验证码功能，提高系统的安全性。