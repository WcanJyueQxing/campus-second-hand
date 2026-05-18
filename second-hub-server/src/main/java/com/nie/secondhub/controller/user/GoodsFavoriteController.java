package com.nie.secondhub.controller.user;

import com.nie.secondhub.common.context.LoginUserHolder;
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
    public Result toggleFavorite(@PathVariable Long goodsId) {
        Long userId = LoginUserHolder.requireUserId();
        return goodsFavoriteService.toggleFavorite(userId, goodsId);
    }

    @GetMapping
    public Result getMyFavorites() {
        Long userId = LoginUserHolder.requireUserId();
        return goodsFavoriteService.getFavoriteGoodsList(userId);
    }
}