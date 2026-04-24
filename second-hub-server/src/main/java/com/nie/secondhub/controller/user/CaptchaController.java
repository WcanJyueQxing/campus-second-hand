package com.nie.secondhub.controller.user;

import com.google.code.kaptcha.Producer;
import com.nie.secondhub.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.FastByteArrayOutputStream;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 验证码控制器
 * 生成图形验证码，防止暴力登录攻击
 *
 * @author nie
 */
@Tag(name = "验证码接口", description = "用于登录时的图形验证码验证")
@RestController
@RequestMapping("/api/user/captcha")
public class CaptchaController {

    /**
     * 验证码缓存前缀
     */
    private static final String CAPTCHA_CODE_KEY = "captcha_code_";

    /**
     * 验证码有效期（分钟）
     */
    private static final long CAPTCHA_EXPIRATION = 5;

    /**
     * 字符型验证码生成器
     */
    @Resource(name = "captchaProducer")
    private Producer captchaProducer;

    /**
     * 数学计算型验证码生成器
     */
    @Resource(name = "captchaProducerMath")
    private Producer captchaProducerMath;

    /**
     * Redis 操作模板
     */
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 生成验证码
     *
     * @param type 验证码类型：char-字符型，math-数学计算型，默认为char
     * @return 包含uuid和验证码图片(base64编码)的响应
     */
    @Operation(summary = "获取验证码", description = "生成图形验证码并返回验证码图片和UUID")
    @GetMapping("/generate")
    public ApiResponse<Map<String, Object>> getCaptchaImage(
            @RequestParam(value = "type", defaultValue = "char") String type) {

        Map<String, Object> data = new HashMap<>();

        // 生成唯一标识
        String uuid = UUID.randomUUID().toString().replace("-", "");
        String verifyKey = CAPTCHA_CODE_KEY + uuid;

        BufferedImage image;
        String capStr;
        String code;

        try {
            // 根据类型生成不同的验证码
            if ("math".equals(type)) {
                // 数学计算型验证码
                String capText = captchaProducerMath.createText();
                // 格式：算术表达式@答案，如 "3+5=?@8"
                capStr = capText.substring(0, capText.lastIndexOf("@"));
                code = capText.substring(capText.lastIndexOf("@") + 1);
                image = captchaProducerMath.createImage(capStr);
            } else {
                // 字符型验证码（默认）
                capStr = code = captchaProducer.createText();
                image = captchaProducer.createImage(capStr);
            }

            // 将验证码存储到Redis，设置过期时间
            try {
                stringRedisTemplate.opsForValue().set(verifyKey, code, CAPTCHA_EXPIRATION, TimeUnit.MINUTES);
            } catch (Exception e) {
                // Redis 连接失败，仍然返回验证码图片，但不存储到 Redis
                System.err.println("Redis 连接失败: " + e.getMessage());
                e.printStackTrace();
            }

            // 将验证码图片转换为Base64编码
            FastByteArrayOutputStream outputStream = new FastByteArrayOutputStream();
            ImageIO.write(image, "png", outputStream);
            String imgStr = java.util.Base64.getEncoder().encodeToString(outputStream.toByteArray());
            String imgBase64 = "data:image/png;base64," + imgStr;

            data.put("uuid", uuid);
            data.put("captcha", imgBase64);
            data.put("codeLength", capStr.length());

            return ApiResponse.success(data);

        } catch (IOException e) {
            return ApiResponse.fail(500, "验证码生成失败");
        }
    }

    /**
     * 验证验证码是否正确（可选接口，供前端单独验证）
     *
     * @param uuid 验证码唯一标识
     * @param code 用户输入的验证码
     * @return 验证结果
     */
    @Operation(summary = "验证验证码", description = "校验用户输入的验证码是否正确")
    @GetMapping("/verify")
    public ApiResponse<Map<String, Object>> verifyCaptcha(
            @RequestParam("uuid") String uuid,
            @RequestParam("code") String code) {

        Map<String, Object> data = new HashMap<>();

        if (uuid == null || uuid.isBlank() || code == null || code.isBlank()) {
            data.put("success", false);
            data.put("message", "参数不能为空");
            return ApiResponse.success(data);
        }

        try {
            String verifyKey = CAPTCHA_CODE_KEY + uuid;
            String cachedCode = stringRedisTemplate.opsForValue().get(verifyKey);

            if (cachedCode == null) {
                data.put("success", false);
                data.put("message", "验证码已过期，请重新获取");
                return ApiResponse.success(data);
            }

            // 忽略大小写比较
            boolean match = cachedCode.equalsIgnoreCase(code.trim());
            if (match) {
                // 验证成功后删除验证码
                stringRedisTemplate.delete(verifyKey);
                data.put("success", true);
                data.put("message", "验证成功");
            } else {
                data.put("success", false);
                data.put("message", "验证码错误");
            }
        } catch (Exception e) {
            data.put("success", false);
            data.put("message", "验证码服务异常，请重试");
        }

        return ApiResponse.success(data);
    }
}
