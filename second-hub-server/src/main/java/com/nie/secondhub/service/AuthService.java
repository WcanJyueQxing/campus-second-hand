package com.nie.secondhub.service;

import com.nie.secondhub.dto.admin.AdminLoginRequest;
import com.nie.secondhub.dto.user.AccountLoginRequest;
import com.nie.secondhub.dto.user.WxLoginRequest;
import com.nie.secondhub.vo.LoginVO;

import com.nie.secondhub.dto.user.RegisterRequest;

public interface AuthService {
    LoginVO wxLogin(WxLoginRequest request);

    LoginVO accountLogin(AccountLoginRequest request);

    LoginVO adminLogin(AdminLoginRequest request);

    void register(RegisterRequest request);
}
