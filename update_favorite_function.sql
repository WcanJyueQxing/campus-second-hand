-- 确保goods_favorite表结构正确
CREATE DATABASE IF NOT EXISTS db_second_hub DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE db_second_hub;

-- 1. 检查并添加唯一约束（如果不存在）
ALTER TABLE goods_favorite ADD UNIQUE INDEX IF NOT EXISTS uk_user_goods_favorite (user_id, goods_id);

-- 2. 确保goods表的favorite_count字段正确
ALTER TABLE goods MODIFY COLUMN favorite_count INT NOT NULL DEFAULT 0;

-- 3. 查看当前数据状态
SELECT * FROM goods LIMIT 5;
SELECT * FROM goods_favorite LIMIT 5;

-- 4. 添加测试数据（如果需要）
INSERT INTO goods (user_id, category_id, title, description, price, cover_image, status, view_count, favorite_count, comment_count) VALUES
(1, 1, '测试商品1', '测试商品1描述', 100.00, 'test1.jpg', 'APPROVED', 10, 2, 1),
(1, 2, '测试商品2', '测试商品2描述', 200.00, 'test2.jpg', 'APPROVED', 15, 3, 2),
(2, 3, '测试商品3', '测试商品3描述', 300.00, 'test3.jpg', 'APPROVED', 20, 4, 3)
ON DUPLICATE KEY UPDATE updated_at = CURRENT_TIMESTAMP;

-- 添加测试收藏记录
INSERT IGNORE INTO goods_favorite (user_id, goods_id) VALUES
(1, 1),
(1, 2),
(2, 3);
