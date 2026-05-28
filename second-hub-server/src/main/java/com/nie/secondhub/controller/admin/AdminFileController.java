package com.nie.secondhub.controller.admin;

import com.nie.secondhub.common.response.ApiResponse;
import com.nie.secondhub.service.FileStorageService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * 文件管理控制器
 * 提供文件上传接口
 */
@RestController
@RequestMapping("/api/admin/files")
public class AdminFileController {

    @Resource
    private FileStorageService fileStorageService;

    /**
     * 上传文件
     * 
     * @param file 上传的文件
     * @return 包含文件URL的结果
     */
    @PostMapping("/upload")
    public ApiResponse<Map<String, String>> upload(@RequestPart("file") MultipartFile file) {
        return ApiResponse.success(Map.of("url", fileStorageService.upload(file)));
    }
}