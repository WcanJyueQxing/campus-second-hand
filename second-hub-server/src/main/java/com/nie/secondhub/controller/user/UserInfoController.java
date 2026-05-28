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

/**
 * 用户信息控制器
 * 提供用户信息查询和修改接口
 */
@Validated
@RestController
@RequestMapping("/api/user/info")
public class UserInfoController {

    @Resource
    private UserMapper userMapper;

    @Resource
    private UserProfileMapper userProfileMapper;

    /**
     * 获取当前用户信息
     * 
     * @return 用户信息（包含用户基本信息和档案信息）
     */
    @GetMapping
    public ApiResponse<User> getUserInfo() {
        Long userId = LoginUserHolder.requireUserId();
        
        User user = userMapper.selectById(userId);
        if (user == null) {
            return ApiResponse.fail(404, "用户不存在");
        }
        
        LambdaQueryWrapper<UserProfile> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserProfile::getUserId, userId);
        UserProfile profile = userProfileMapper.selectOne(wrapper);
        
        if (profile != null && profile.getNickname() != null && !profile.getNickname().isBlank()) {
            user.setNickname(profile.getNickname());
        }
        if (profile != null && profile.getAvatarUrl() != null && !profile.getAvatarUrl().isBlank()) {
            user.setAvatarUrl(profile.getAvatarUrl());
        }
        
        return ApiResponse.success(user);
    }

    /**
     * 更新用户信息
     * 
     * @param request 用户更新请求（包含昵称、头像、签名等）
     * @return 操作结果
     */
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