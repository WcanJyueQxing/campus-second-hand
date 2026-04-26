package com.nie.secondhub.controller.user;

import com.nie.secondhub.service.GoodsFavoriteService;
import com.nie.secondhub.util.Result;
import org.springframework.web.bind.annotation.*;
import jakarta.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/api/user/favorites")
public class GoodsFavoriteController {

    @Resource
    private GoodsFavoriteService goodsFavoriteService;

    // 收藏/取消收藏（原接口不变）
    @PostMapping("/{goodsId}")
    public Result toggleFavorite(
            @RequestHeader(value = "token", required = false) String token,
            @PathVariable Long goodsId) {
        Long userId = 1L;
        return goodsFavoriteService.toggleFavorite(userId, goodsId);
    }

    // 获取收藏列表（原接口不变）
    @GetMapping
    public Result getMyFavorites(
            @RequestHeader(value = "token", required = false) String token) {
        Long userId = 1L;
        return goodsFavoriteService.getFavoriteGoodsList(userId);
    }

    // 单条删除收藏（新增）
    @DeleteMapping("/{goodsId}")
    public Result deleteFavorite(
            @RequestHeader(value = "token", required = false) String token,
            @PathVariable Long goodsId) {
        Long userId = 1L;
        return goodsFavoriteService.deleteFavorite(userId, goodsId);
    }

    // 批量删除收藏（新增）
    @DeleteMapping("/batch")
    public Result batchDeleteFavorites(
            @RequestHeader(value = "token", required = false) String token,
            @RequestBody List<Long> goodsIds) {
        Long userId = 1L;
        return goodsFavoriteService.batchDeleteFavorites(userId, goodsIds);
    }
}