USE `db_second_hub`;

-- 用户表添加电话索引
ALTER TABLE `user` ADD INDEX `idx_user_phone` (`phone`);

-- 商品表添加复合索引
ALTER TABLE `goods` ADD INDEX `idx_goods_status_created` (`status`, `created_at`);

-- 评论表添加用户索引
ALTER TABLE `goods_comment` ADD INDEX `idx_goods_comment_user` (`user_id`);
