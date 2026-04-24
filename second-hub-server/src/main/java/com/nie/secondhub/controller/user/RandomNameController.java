package com.nie.secondhub.controller.user;

import com.nie.secondhub.common.response.ApiResponse;
import com.nie.secondhub.utils.NameInitializer;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 随机姓名生成控制器
 */
@RestController
@RequestMapping("/api/user/random-name")
@Tag(name = "随机姓名生成", description = "随机姓名生成相关接口")
public class RandomNameController {

    @Resource
    private NameInitializer nameInitializer;

    /**
     * 生成单个随机姓名
     * @param gender 性别：male/female/random
     * @return 随机姓名
     */
    @Operation(summary = "生成单个随机姓名", description = "根据性别生成随机姓名")
    @GetMapping("/generate")
    public ApiResponse<String> generateRandomName(
            @RequestParam(value = "gender", defaultValue = "random") String gender) {
        String name = nameInitializer.generateName(gender);
        return ApiResponse.success(name);
    }

    /**
     * 批量生成随机姓名
     * @param gender 性别：male/female/random
     * @param count 生成数量，默认5个
     * @return 随机姓名列表
     */
    @Operation(summary = "批量生成随机姓名", description = "根据性别批量生成随机姓名")
    @GetMapping("/generate-batch")
    public ApiResponse<List<String>> generateBatchRandomNames(
            @RequestParam(value = "gender", defaultValue = "random") String gender,
            @RequestParam(value = "count", defaultValue = "5") Integer count) {
        // 限制生成数量，最多20个
        count = Math.min(count, 20);
        List<String> names = nameInitializer.generateNames(gender, count);
        return ApiResponse.success(names);
    }

    /**
     * 初始化姓名库
     * @return 初始化结果
     */
    @Operation(summary = "初始化姓名库", description = "重新初始化姓名库到Redis")
    @GetMapping("/init")
    public ApiResponse<String> initNameLibrary() {
        nameInitializer.initNameLibrary();
        return ApiResponse.success("姓名库初始化成功");
    }
}