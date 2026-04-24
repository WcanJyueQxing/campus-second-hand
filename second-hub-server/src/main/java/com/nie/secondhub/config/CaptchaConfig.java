package com.nie.secondhub.config;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

import static com.google.code.kaptcha.Constants.*;

/**
 * 验证码配置类
 * 配置 kaptcha 验证码生成器的各种参数
 *
 * @author nie
 */
@Configuration
public class CaptchaConfig {

    /**
     * 字符型验证码生成器
     * 生成随机字母数字组合的验证码图片
     */
    @Bean(name = "captchaProducer")
    public DefaultKaptcha getKaptchaBean() {
        DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
        Properties properties = new Properties();

        // 是否有边框，默认为true，可以设置为yes或no
        properties.setProperty(KAPTCHA_BORDER, "yes");

        // 验证码文本字符颜色，默认为Color.BLACK
        properties.setProperty(KAPTCHA_TEXTPRODUCER_FONT_COLOR, "black");

        // 验证码图片宽度，默认为200
        properties.setProperty(KAPTCHA_IMAGE_WIDTH, "160");

        // 验证码图片高度，默认为50
        properties.setProperty(KAPTCHA_IMAGE_HEIGHT, "60");

        // 验证码文本字符大小，默认为40
        properties.setProperty(KAPTCHA_TEXTPRODUCER_FONT_SIZE, "38");

        // 验证码Session的KEY，用于存储验证码文本
        properties.setProperty(KAPTCHA_SESSION_CONFIG_KEY, "kaptchaCode");

        // 验证码文本字符长度，默认为5
        properties.setProperty(KAPTCHA_TEXTPRODUCER_CHAR_LENGTH, "4");

        // 验证码文本字体样式，默认为new Font("Arial", 1, fontSize), new Font("Courier", 1, fontSize)
        properties.setProperty(KAPTCHA_TEXTPRODUCER_FONT_NAMES, "Arial,Courier");

        // 图片样式：水纹com.google.code.kaptcha.impl.WaterRipple、鱼眼com.google.code.kaptcha.impl.FishEyeGimpy、阴影com.google.code.kaptcha.impl.ShadowGimpy
        properties.setProperty(KAPTCHA_OBSCURIFICATOR_IMPL, "com.google.code.kaptcha.impl.ShadowGimpy");

        Config config = new Config(properties);
        defaultKaptcha.setConfig(config);
        return defaultKaptcha;
    }

    /**
     * 数学计算型验证码生成器
     * 生成简单的数学计算题（如：1+2=? 或 6-3=?）
     */
    @Bean(name = "captchaProducerMath")
    public DefaultKaptcha getKaptchaBeanMath() {
        DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
        Properties properties = new Properties();

        // 是否有边框，默认为true
        properties.setProperty(KAPTCHA_BORDER, "yes");

        // 边框颜色
        properties.setProperty(KAPTCHA_BORDER_COLOR, "105,179,90");

        // 验证码文本字符颜色，默认为Color.BLACK
        properties.setProperty(KAPTCHA_TEXTPRODUCER_FONT_COLOR, "blue");

        // 验证码图片宽度，默认为200
        properties.setProperty(KAPTCHA_IMAGE_WIDTH, "160");

        // 验证码图片高度，默认为50
        properties.setProperty(KAPTCHA_IMAGE_HEIGHT, "60");

        // 验证码文本字符大小，默认为40
        properties.setProperty(KAPTCHA_TEXTPRODUCER_FONT_SIZE, "35");

        // 验证码Session的KEY
        properties.setProperty(KAPTCHA_SESSION_CONFIG_KEY, "kaptchaCodeMath");

        // 验证码文本生成器，使用自定义的数学表达式生成器
        properties.setProperty(KAPTCHA_TEXTPRODUCER_IMPL, "com.nie.secondhub.config.KaptchaTextCreator");

        // 验证码文本字符间距，默认为2
        properties.setProperty(KAPTCHA_TEXTPRODUCER_CHAR_SPACE, "3");

        // 验证码文本字符长度，默认为5
        properties.setProperty(KAPTCHA_TEXTPRODUCER_CHAR_LENGTH, "6");

        // 验证码文本字体样式
        properties.setProperty(KAPTCHA_TEXTPRODUCER_FONT_NAMES, "Arial,Courier");

        // 验证码噪点颜色，默认为Color.BLACK
        properties.setProperty(KAPTCHA_NOISE_COLOR, "white");

        // 干扰实现类，设置为无干扰
        properties.setProperty(KAPTCHA_NOISE_IMPL, "com.google.code.kaptcha.impl.NoNoise");

        // 图片样式
        properties.setProperty(KAPTCHA_OBSCURIFICATOR_IMPL, "com.google.code.kaptcha.impl.ShadowGimpy");

        Config config = new Config(properties);
        defaultKaptcha.setConfig(config);
        return defaultKaptcha;
    }
}
