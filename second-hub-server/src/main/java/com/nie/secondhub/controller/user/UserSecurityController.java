package com.nie.secondhub.controller.user;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.nie.secondhub.common.context.LoginUserHolder;
import com.nie.secondhub.common.exception.BizException;
import com.nie.secondhub.common.response.ApiResponse;
import com.nie.secondhub.entity.User;
import com.nie.secondhub.mapper.UserMapper;
import com.nie.secondhub.util.Md5Util;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/api/user")
public class UserSecurityController {

    private static final String CAPTCHA_CODE_KEY = "captcha_code_";
    private static final String SESSION_TOKEN_KEY = "session:token:";

    @Resource
    private UserMapper userMapper;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Data
    public static class PasswordChangeRequest {
        @NotBlank(message = "原密码不能为空")
        private String oldPassword;
        @NotBlank(message = "新密码不能为空")
        private String newPassword;
    }

    @Data
    public static class PhoneChangeRequest {
        @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
        private String phone;
        @NotBlank(message = "验证码不能为空")
        private String captchaCode;
        @NotBlank(message = "验证码ID不能为空")
        private String captchaUuid;
    }

    @PutMapping("/password")
    public ApiResponse<Void> changePassword(@Valid @RequestBody PasswordChangeRequest request) {
        Long userId = LoginUserHolder.requireUserId();

        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BizException(404, "用户不存在");
        }

        String oldPasswordMd5 = Md5Util.md5(request.getOldPassword());
        if (!oldPasswordMd5.equals(user.getPassword())) {
            throw new BizException(400, "原密码错误");
        }

        String newPasswordMd5 = Md5Util.md5(request.getNewPassword());
        user.setPassword(newPasswordMd5);
        userMapper.updateById(user);

        return ApiResponse.success(null);
    }

    @PutMapping("/phone")
    public ApiResponse<Void> changePhone(@Valid @RequestBody PhoneChangeRequest request) {
        Long userId = LoginUserHolder.requireUserId();

        String phone = request.getPhone();
        String captchaCode = request.getCaptchaCode();
        String captchaUuid = request.getCaptchaUuid();

        String verifyKey = CAPTCHA_CODE_KEY + captchaUuid;
        String cachedCode = stringRedisTemplate.opsForValue().get(verifyKey);
        if (cachedCode == null) {
            throw new BizException(400, "验证码已过期，请重新获取");
        }
        if (!cachedCode.equalsIgnoreCase(captchaCode.trim())) {
            throw new BizException(400, "验证码错误");
        }
        stringRedisTemplate.delete(verifyKey);

        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BizException(404, "用户不存在");
        }

        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getPhone, phone);
        Long count = userMapper.selectCount(wrapper);
        if (count > 0) {
            throw new BizException(400, "该手机号已被其他账号绑定");
        }

        user.setPhone(phone);
        userMapper.updateById(user);

        return ApiResponse.success(null);
    }

    @PostMapping("/auth/logout")
    public ApiResponse<Void> logout() {
        Long userId = LoginUserHolder.requireUserId();
        String tokenKey = SESSION_TOKEN_KEY + userId;
        stringRedisTemplate.delete(tokenKey);
        return ApiResponse.success(null);
    }
}