package com.nie.secondhub.controller.user;

import com.nie.secondhub.common.context.LoginUserHolder;
import com.nie.secondhub.common.response.ApiResponse;
import com.nie.secondhub.common.response.PageResponse;
import com.nie.secondhub.dto.user.CommentCreateRequest;
import com.nie.secondhub.dto.user.ReportCreateRequest;
import com.nie.secondhub.service.InteractionService;
import com.nie.secondhub.vo.CommentVO;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 用户互动控制器
 * 提供评论、举报等互动功能接口
 */
@Validated
@RestController
@RequestMapping("/api/user")
public class UserInteractionController {

    @Resource
    private InteractionService interactionService;

    /**
     * 添加评论
     * 
     * @param request 评论创建请求（包含商品ID、评论内容等）
     * @return 操作结果
     */
    @PostMapping("/comments")
    public ApiResponse<Void> addComment(@Valid @RequestBody CommentCreateRequest request) {
        interactionService.addComment(LoginUserHolder.requireUserId(), request);
        return ApiResponse.success(null);
    }

    /**
     * 查询商品评论列表
     * 
     * @param goodsId 商品ID
     * @param pageNo 页码（从1开始）
     * @param pageSize 每页大小
     * @return 分页评论列表
     */
    @GetMapping("/comments/{goodsId}")
    public ApiResponse<PageResponse<CommentVO>> comments(@PathVariable Long goodsId,
                                                         @RequestParam(defaultValue = "1") @Min(1) Long pageNo,
                                                         @RequestParam(defaultValue = "10") @Min(1) Long pageSize) {
        return ApiResponse.success(interactionService.commentPage(goodsId, pageNo, pageSize));
    }

    /**
     * 举报商品
     * 
     * @param request 举报创建请求（包含商品ID、举报类型、举报内容等）
     * @return 操作结果
     */
    @PostMapping("/reports")
    public ApiResponse<Void> report(@Valid @RequestBody ReportCreateRequest request) {
        interactionService.report(LoginUserHolder.requireUserId(), request);
        return ApiResponse.success(null);
    }
}