USE db_second_hub;

-- 添加测试商品
INSERT INTO goods (user_id, category_id, title, description, price, cover_image, status, view_count, favorite_count, comment_count) VALUES
(1, 1, '测试商品1', '测试商品1描述', 100.00, 'test1.jpg', 'APPROVED', 10, 2, 1),
(1, 2, '测试商品2', '测试商品2描述', 200.00, 'test2.jpg', 'APPROVED', 15, 3, 2),
(2, 3, '测试商品3', '测试商品3描述', 300.00, 'test3.jpg', 'APPROVED', 20, 4, 3);

-- 添加测试收藏记录
INSERT INTO goods_favorite (user_id, goods_id) VALUES
(1, 1),
(1, 2),
(2, 3);
