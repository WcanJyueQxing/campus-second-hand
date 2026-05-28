package com.nie.secondhub.controller.user;

import com.nie.secondhub.common.context.LoginUserHolder;
import com.nie.secondhub.common.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.Data;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 通知设置控制器
 * 提供通知偏好设置的查询和修改接口
 */
@Validated
@RestController
@RequestMapping("/api/user/notification")
public class NotificationController {

    /**
     * 通知设置类
     */
    @Data
    public static class NotificationSettings {
        /** 订单通知 */
        private Boolean order;
        /** 商品通知 */
        private Boolean goods;
        /** 系统通知 */
        private Boolean system;
        /** 推送开关 */
        private Boolean pushEnabled;
        /** 声音开关 */
        private Boolean sound;
        /** 震动开关 */
        private Boolean vibrate;
    }

    /**
     * 获取用户设置映射（模拟存储）
     * 
     * @return 用户设置映射
     */
    private Map<Long, NotificationSettings> getUserSettingsMap() {
        return new HashMap<>();
    }

    /**
     * 获取当前用户的通知设置
     * 
     * @return 通知设置
     */
    @GetMapping
    public ApiResponse<NotificationSettings> getSettings() {
        Long userId = LoginUserHolder.requireUserId();
        Map<Long, NotificationSettings> settingsMap = getUserSettingsMap();
        NotificationSettings settings = settingsMap.getOrDefault(userId, getDefaultSettings());
        return ApiResponse.success(settings);
    }

    /**
     * 更新通知设置
     * 
     * @param settings 通知设置
     * @return 操作结果
     */
    @PutMapping
    public ApiResponse<Void> updateSettings(@RequestBody @Valid NotificationSettings settings) {
        Long userId = LoginUserHolder.requireUserId();
        Map<Long, NotificationSettings> settingsMap = getUserSettingsMap();
        settingsMap.put(userId, settings);
        return ApiResponse.success(null);
    }

    /**
     * 获取默认通知设置
     * 
     * @return 默认设置（所有通知均开启）
     */
    private NotificationSettings getDefaultSettings() {
        NotificationSettings settings = new NotificationSettings();
        settings.setOrder(true);
        settings.setGoods(true);
        settings.setSystem(true);
        settings.setPushEnabled(true);
        settings.setSound(true);
        settings.setVibrate(true);
        return settings;
    }
}