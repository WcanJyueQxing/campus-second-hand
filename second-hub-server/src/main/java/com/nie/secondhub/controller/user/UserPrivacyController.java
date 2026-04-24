package com.nie.secondhub.controller.user;

import com.nie.secondhub.common.context.LoginUserHolder;
import com.nie.secondhub.common.response.ApiResponse;
import com.nie.secondhub.entity.User;
import com.nie.secondhub.mapper.UserMapper;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserPrivacyController {

    @Resource
    private UserMapper userMapper;

    @GetMapping("/privacy")
    public ApiResponse<Map<String, Object>> getPrivacySettings() {
        Long userId = LoginUserHolder.requireUserId();

        User user = userMapper.selectById(userId);
        if (user == null) {
            return ApiResponse.fail(404, "用户不存在");
        }

        Map<String, Object> data = new HashMap<>();
        data.put("phone", user.getPhone());
        data.put("showFavorites", true);
        data.put("showGoods", true);
        data.put("showHistory", false);

        return ApiResponse.success(data);
    }

    @PutMapping("/privacy")
    public ApiResponse<Void> updatePrivacySettings(@RequestBody Map<String, Object> settings) {
        Long userId = LoginUserHolder.requireUserId();

        User user = userMapper.selectById(userId);
        if (user == null) {
            return ApiResponse.fail(404, "用户不存在");
        }

        return ApiResponse.success(null);
    }
}