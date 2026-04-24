package com.nie.secondhub.dto.user;

import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserUpdateRequest {
    @Size(min = 1, max = 20, message = "昵称长度必须在1-20之间")
    private String nickname;
    private String avatarUrl;
    private String bio;
    private Integer gender;
    private LocalDate birthday;
    private String interests;
    private String school;
}