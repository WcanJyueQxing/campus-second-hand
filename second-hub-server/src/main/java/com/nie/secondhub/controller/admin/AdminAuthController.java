package com.nie.secondhub.controller.admin;

import com.nie.secondhub.common.response.ApiResponse;
import com.nie.secondhub.dto.admin.AdminLoginRequest;
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
 * 管理员认证控制器
 * 提供管理员登录接口
 */
@Validated
@RestController
@RequestMapping("/api/admin/auth")
public class AdminAuthController {

    @Resource
    private AuthService authService;

    /**
     * 管理员登录
     * 
     * @param request 登录请求（包含用户名、密码、验证码）
     * @return 登录结果（包含用户信息和Token）
     */
    @PostMapping("/login")
    public ApiResponse<LoginVO> login(@Valid @RequestBody AdminLoginRequest request) {
        return ApiResponse.success(authService.adminLogin(request));
    }
}