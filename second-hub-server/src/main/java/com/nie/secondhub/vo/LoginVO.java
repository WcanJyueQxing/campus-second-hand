package com.nie.secondhub.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginVO {
    private Long id;
    private String nickname;
    private String role;
    private String token;
    private String phone;
    
    public LoginVO(Long id, String nickname, String role, String token) {
        this.id = id;
        this.nickname = nickname;
        this.role = role;
        this.token = token;
    }
}