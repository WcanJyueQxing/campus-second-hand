package com.nie.secondhub.service;

import com.nie.secondhub.dto.admin.AdminLoginRequest;
import com.nie.secondhub.dto.user.AccountLoginRequest;
import com.nie.secondhub.dto.user.WxLoginRequest;
import com.nie.secondhub.vo.LoginVO;

import com.nie.secondhub.dto.user.RegisterRequest;

/**
 * 认证服务接口
 * 提供用户登录、注册等认证功能
 */
public interface AuthService {

    /**
     * 微信登录
     * 
     * @param request 微信登录请求
     * @return 登录结果
     */
    LoginVO wxLogin(WxLoginRequest request);

    /**
     * 账号密码登录
     * 
     * @param request 账号登录请求
     * @return 登录结果
     */
    LoginVO accountLogin(AccountLoginRequest request);

    /**
     * 管理员登录
     * 
     * @param request 管理员登录请求
     * @return 登录结果
     */
    LoginVO adminLogin(AdminLoginRequest request);

    /**
     * 用户注册
     * 
     * @param request 注册请求
     */
    void register(RegisterRequest request);
}