package com.nie.secondhub.controller.user;

import com.nie.secondhub.service.GoodsFavoriteService;
import com.nie.secondhub.util.Result;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;

@RestController
@RequestMapping("/api/user/favorites")
public class GoodsFavoriteController {

    @Resource
    private GoodsFavoriteService goodsFavoriteService;

    @PostMapping("/{goodsId}")
    public Result toggleFavorite(
            @RequestHeader(value = "token", required = false) String token,
            @PathVariable Long goodsId
    ) {
        try {
            Long userId = 1L;
            return goodsFavoriteService.toggleFavorite(userId, goodsId);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("操作失败");
        }
    }

    @GetMapping
    public Result getMyFavorites(
            @RequestHeader(value = "token", required = false) String token
    ) {
        try {
            Long userId = 1L;
            return goodsFavoriteService.getFavoriteGoodsList(userId);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("获取收藏失败");
        }
    }
}