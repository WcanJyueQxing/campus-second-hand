package com.nie.secondhub.controller.user;

import com.nie.secondhub.common.context.LoginUserHolder;
import com.nie.secondhub.service.GoodsFavoriteService;
import com.nie.secondhub.util.Result;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;

/**
 * 商品收藏控制器
 * 提供商品收藏/取消收藏、查询收藏列表的接口
 */
@RestController
@RequestMapping("/api/user/favorites")
public class GoodsFavoriteController {

    @Resource
    private GoodsFavoriteService goodsFavoriteService;

    /**
     * 切换商品收藏状态
     * 如果已收藏则取消收藏，如果未收藏则添加收藏
     * 
     * @param goodsId 商品ID
     * @return 操作结果（包含当前收藏状态）
     */
    @PostMapping("/{goodsId}")
    public Result toggleFavorite(@PathVariable Long goodsId) {
        Long userId = LoginUserHolder.requireUserId();
        return goodsFavoriteService.toggleFavorite(userId, goodsId);
    }

    /**
     * 获取当前用户的收藏列表
     * 
     * @return 收藏的商品列表
     */
    @GetMapping
    public Result getMyFavorites() {
        Long userId = LoginUserHolder.requireUserId();
        return goodsFavoriteService.getFavoriteGoodsList(userId);
    }
}