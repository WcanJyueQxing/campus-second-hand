package com.nie.secondhub.controller.user;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.nie.secondhub.common.context.LoginUserHolder;
import com.nie.secondhub.common.response.ApiResponse;
import com.nie.secondhub.dto.user.UserUpdateRequest;
import com.nie.secondhub.entity.User;
import com.nie.secondhub.entity.UserProfile;
import com.nie.secondhub.mapper.UserMapper;
import com.nie.secondhub.mapper.UserProfileMapper;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Validated
@RestController
@RequestMapping("/api/user/info")
public class UserInfoController {

    @Resource
    private UserMapper userMapper;

    @Resource
    private UserProfileMapper userProfileMapper;

    @GetMapping
    public ApiResponse<UserProfile> getUserInfo() {
        Long userId = LoginUserHolder.requireUserId();
        LambdaQueryWrapper<UserProfile> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserProfile::getUserId, userId);
        UserProfile profile = userProfileMapper.selectOne(wrapper);
        if (profile == null) {
            profile = new UserProfile();
            profile.setUserId(userId);
            profile.setGender(0);
            userProfileMapper.insert(profile);
        }
        return ApiResponse.success(profile);
    }

    @PutMapping
    public ApiResponse<Void> updateUserInfo(@Validated @RequestBody UserUpdateRequest request) {
        Long userId = LoginUserHolder.requireUserId();

        LambdaQueryWrapper<UserProfile> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserProfile::getUserId, userId);
        UserProfile profile = userProfileMapper.selectOne(wrapper);

        if (profile == null) {
            profile = new UserProfile();
            profile.setUserId(userId);
            profile.setCreatedAt(LocalDateTime.now());
            profile.setUpdatedAt(LocalDateTime.now());
            profile.setGender(0);
            userProfileMapper.insert(profile);
        }

        if (request.getNickname() != null) {
            profile.setNickname(request.getNickname());
        }
        if (request.getAvatarUrl() != null) {
            profile.setAvatarUrl(request.getAvatarUrl());
        }
        if (request.getBio() != null) {
            profile.setBio(request.getBio());
        }
        if (request.getGender() != null) {
            profile.setGender(request.getGender());
        }
        if (request.getBirthday() != null) {
            profile.setBirthday(request.getBirthday());
        }
        if (request.getInterests() != null) {
            profile.setInterests(request.getInterests());
        }
        if (request.getSchool() != null) {
            profile.setSchool(request.getSchool());
        }

        profile.setUpdatedAt(LocalDateTime.now());
        userProfileMapper.updateById(profile);

        return ApiResponse.success(null);
    }
}