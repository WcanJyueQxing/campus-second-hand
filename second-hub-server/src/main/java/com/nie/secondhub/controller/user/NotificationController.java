package com.nie.secondhub.controller.user;

import com.nie.secondhub.common.context.LoginUserHolder;
import com.nie.secondhub.common.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.Data;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Validated
@RestController
@RequestMapping("/api/user/notification")
public class NotificationController {

    @Data
    public static class NotificationSettings {
        private Boolean order;
        private Boolean goods;
        private Boolean system;
        private Boolean pushEnabled;
        private Boolean sound;
        private Boolean vibrate;
    }

    private Map<Long, NotificationSettings> getUserSettingsMap() {
        return new HashMap<>();
    }

    @GetMapping
    public ApiResponse<NotificationSettings> getSettings() {
        Long userId = LoginUserHolder.requireUserId();
        Map<Long, NotificationSettings> settingsMap = getUserSettingsMap();
        NotificationSettings settings = settingsMap.getOrDefault(userId, getDefaultSettings());
        return ApiResponse.success(settings);
    }

    @PutMapping
    public ApiResponse<Void> updateSettings(@RequestBody @Valid NotificationSettings settings) {
        Long userId = LoginUserHolder.requireUserId();
        Map<Long, NotificationSettings> settingsMap = getUserSettingsMap();
        settingsMap.put(userId, settings);
        return ApiResponse.success(null);
    }

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