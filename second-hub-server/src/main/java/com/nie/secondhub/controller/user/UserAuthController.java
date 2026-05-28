package com.nie.secondhub.controller.user;

import com.nie.secondhub.common.response.ApiResponse;
import com.nie.secondhub.dto.user.AccountLoginRequest;
import com.nie.secondhub.dto.user.RegisterRequest;
import com.nie.secondhub.dto.user.WxLoginRequest;
import com.nie.secondhub.service.AuthService;
import com.nie.secondhub.vo.LoginVO;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户认证控制器
 * 提供用户登录、注册等接口
 */
@Validated
@RestController
@RequestMapping("/api/user/auth")
public class UserAuthController {

    @Resource
    private AuthService authService;

    /**
     * 微信登录
     * 
     * @param request 微信登录请求（包含code、昵称、头像）
     * @return 登录结果（包含用户信息和Token）
     */
    @PostMapping("/wx-login")
    public ApiResponse<LoginVO> wxLogin(@Valid @RequestBody WxLoginRequest request) {
        return ApiResponse.success(authService.wxLogin(request));
    }

    /**
     * 账号密码登录
     * 
     * @param request 账号登录请求（包含账号、密码、验证码）
     * @return 登录结果（包含用户信息和Token）
     */
    @PostMapping("/account-login")
    public ApiResponse<LoginVO> accountLogin(@Valid @RequestBody AccountLoginRequest request) {
        return ApiResponse.success(authService.accountLogin(request));
    }

    /**
     * 用户注册
     * 
     * @param request 注册请求（包含用户名、手机号、密码、验证码）
     * @return 操作结果
     */
    @PostMapping("/register")
    public ApiResponse<Void> register(@Valid @RequestBody RegisterRequest request) {
        authService.register(request);
        return ApiResponse.success(null);
    }
}