package com.nie.secondhub.dto.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AccountLoginRequest {

    @NotBlank(message = "账号不能为空")
    private String account;

    @NotBlank(message = "密码不能为空")
    private String password;
}
