package com.nie.secondhub.controller.user;

import com.nie.secondhub.common.context.LoginUserHolder;
import com.nie.secondhub.common.enums.FeedbackType;
import com.nie.secondhub.entity.Feedback;
import com.nie.secondhub.service.FeedbackService;
import com.nie.secondhub.util.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 用户反馈控制器
 */
@RestController
@RequestMapping("/api/feedback")
@RequiredArgsConstructor
public class FeedbackController {

    private final FeedbackService feedbackService;

    /**
     * 用户提交反馈
     * @param request 反馈请求，包含 type(类型), content(内容), contact(联系方式)
     * @return 是否成功
     */
    @PostMapping
    public Result submitFeedback(@RequestBody Map<String, Object> request) {
        // 获取用户ID，如果未登录则为null
        Long userId = null;
        try {
            userId = LoginUserHolder.requireUserId();
        } catch (Exception e) {
            // 用户未登录，允许匿名提交
        }
        
        // 获取类型，小程序端可能传 Integer 或 String
        Object typeObj = request.get("type");
        String type = typeObj != null ? String.valueOf(typeObj) : null;
        
        String content = (String) request.get("content");
        String contact = (String) request.get("contact");
        
        // 转换类型：小程序端传的是数字ID，需要转换为枚举值
        String typeCode = convertType(type);
        
        Feedback feedback = new Feedback();
        feedback.setUserId(userId);
        feedback.setType(typeCode);
        feedback.setContent(content);
        feedback.setContact(contact);
        feedback.setStatus("PENDING");
        
        // 手动设置创建和更新时间，避免自动填充失效
        LocalDateTime now = LocalDateTime.now();
        feedback.setCreatedAt(now);
        feedback.setUpdatedAt(now);
        
        feedbackService.save(feedback);
        
        return Result.success("提交成功");
    }
    
    /**
     * 转换小程序端的类型ID为枚举值
     */
    private String convertType(String type) {
        if (type == null) {
            return FeedbackType.OTHER.name();
        }
        
        // 小程序端类型ID映射：
        // 1-功能建议(FEATURE), 2-Bug反馈(BUG), 3-商品问题(GOODS), 4-交易纠纷(DISPUTE), 5-其他(OTHER)
        switch (type) {
            case "1":
                return FeedbackType.FEATURE.name();
            case "2":
                return FeedbackType.BUG.name();
            case "3":
                return FeedbackType.GOODS.name();
            case "4":
                return FeedbackType.DISPUTE.name();
            case "5":
            default:
                return FeedbackType.OTHER.name();
        }
    }
}