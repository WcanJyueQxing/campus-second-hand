CREATE DATABASE IF NOT EXISTS `db_second_hub` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE `db_second_hub`;

CREATE TABLE IF NOT EXISTS `user` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '用户ID，主键，自增',
  `openid` VARCHAR(64) DEFAULT NULL COMMENT '微信OpenID，用于微信登录标识',
  `nickname` VARCHAR(64) NOT NULL COMMENT '用户昵称',
  `avatar_url` VARCHAR(255) DEFAULT NULL COMMENT '用户头像URL',
  `phone` VARCHAR(32) DEFAULT NULL COMMENT '用户手机号',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '用户状态：1-正常，0-禁用',
  `password` VARCHAR(64) DEFAULT NULL COMMENT '用户密码（MD5加密）',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除：1-已删除，0-未删除（逻辑删除）',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_openid` (`openid`),
  KEY `idx_user_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

CREATE TABLE IF NOT EXISTS `admin_user` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '管理员ID，主键，自增',
  `username` VARCHAR(64) NOT NULL COMMENT '管理员用户名（登录账号）',
  `password` VARCHAR(64) NOT NULL COMMENT '管理员密码（MD5加密）',
  `real_name` VARCHAR(64) NOT NULL COMMENT '管理员真实姓名',
  `role_name` VARCHAR(32) NOT NULL DEFAULT 'AUDITOR' COMMENT '角色名称：SUPER_ADMIN-超级管理员，AUDITOR-审核员',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '管理员状态：1-正常，0-禁用',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除：1-已删除，0-未删除（逻辑删除）',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_admin_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='管理员用户表';

CREATE TABLE IF NOT EXISTS `category` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '分类ID，主键，自增',
  `name` VARCHAR(64) NOT NULL COMMENT '分类名称',
  `sort` INT NOT NULL DEFAULT 0 COMMENT '排序字段，数值越小越靠前',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '分类状态：1-启用，0-禁用',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除：1-已删除，0-未删除（逻辑删除）',
  PRIMARY KEY (`id`),
  KEY `idx_category_sort` (`sort`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品分类表';

CREATE TABLE IF NOT EXISTS `goods` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '商品ID，主键，自增',
  `user_id` BIGINT NOT NULL COMMENT '发布用户ID，关联user表',
  `category_id` BIGINT NOT NULL COMMENT '商品分类ID，关联category表',
  `title` VARCHAR(128) NOT NULL COMMENT '商品标题',
  `description` TEXT NOT NULL COMMENT '商品描述详情',
  `price` DECIMAL(10,2) NOT NULL COMMENT '商品价格',
  `cover_image` VARCHAR(255) NOT NULL COMMENT '商品封面图片URL',
  `status` VARCHAR(32) NOT NULL DEFAULT 'PENDING' COMMENT '商品状态：PENDING-待审核，APPROVED-已通过，REJECTED-已拒绝，ONLINE-上架中，OFFLINE-已下架',
  `reject_reason` VARCHAR(255) DEFAULT NULL COMMENT '审核拒绝原因',
  `view_count` INT NOT NULL DEFAULT 0 COMMENT '商品浏览次数',
  `favorite_count` INT NOT NULL DEFAULT 0 COMMENT '商品收藏次数',
  `comment_count` INT NOT NULL DEFAULT 0 COMMENT '商品评论次数',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除：1-已删除，0-未删除（逻辑删除）',
  PRIMARY KEY (`id`),
  KEY `idx_goods_user` (`user_id`),
  KEY `idx_goods_category` (`category_id`),
  KEY `idx_goods_status` (`status`),
  KEY `idx_goods_created_at` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品表';

CREATE TABLE IF NOT EXISTS `goods_image` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '图片ID，主键，自增',
  `goods_id` BIGINT NOT NULL COMMENT '商品ID，关联goods表',
  `image_url` VARCHAR(255) NOT NULL COMMENT '图片URL',
  `sort` INT NOT NULL DEFAULT 1 COMMENT '图片排序，用于展示顺序',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除：1-已删除，0-未删除（逻辑删除）',
  PRIMARY KEY (`id`),
  KEY `idx_goods_image_goods` (`goods_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品图片表';

CREATE TABLE IF NOT EXISTS `goods_favorite` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '收藏记录ID，主键，自增',
  `user_id` BIGINT NOT NULL COMMENT '用户ID，关联user表',
  `goods_id` BIGINT NOT NULL COMMENT '商品ID，关联goods表',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '收藏时间',
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除：1-已删除，0-未删除（逻辑删除）',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_goods_favorite` (`user_id`,`goods_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品收藏表';

CREATE TABLE IF NOT EXISTS `goods_comment` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '评论ID，主键，自增',
  `goods_id` BIGINT NOT NULL COMMENT '商品ID，关联goods表',
  `user_id` BIGINT NOT NULL COMMENT '评论用户ID，关联user表',
  `content` VARCHAR(500) NOT NULL COMMENT '评论内容',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '评论时间',
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除：1-已删除，0-未删除（逻辑删除）',
  PRIMARY KEY (`id`),
  KEY `idx_goods_comment_goods` (`goods_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品评论表';

CREATE TABLE IF NOT EXISTS `trade_order` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '订单ID，主键，自增',
  `order_no` VARCHAR(64) NOT NULL COMMENT '订单编号（唯一标识）',
  `goods_id` BIGINT NOT NULL COMMENT '商品ID，关联goods表',
  `buyer_id` BIGINT NOT NULL COMMENT '买家ID，关联user表',
  `seller_id` BIGINT NOT NULL COMMENT '卖家ID，关联user表',
  `amount` DECIMAL(10,2) NOT NULL COMMENT '订单金额',
  `note` VARCHAR(500) DEFAULT NULL COMMENT '订单备注',
  `order_status` VARCHAR(32) NOT NULL DEFAULT 'PENDING_PAYMENT' COMMENT '订单状态：PENDING_PAYMENT-待付款，PAID-已付款，CONFIRMED-已确认，FINISHED-已完成，CANCELLED-已取消',
  `pay_status` VARCHAR(32) NOT NULL DEFAULT 'UNPAID' COMMENT '支付状态：UNPAID-未支付，PAID-已支付',
  `buyer_confirmed` TINYINT NOT NULL DEFAULT 0 COMMENT '买家是否确认收货：1-已确认，0-未确认',
  `seller_confirmed` TINYINT NOT NULL DEFAULT 0 COMMENT '卖家是否确认订单：1-已确认，0-未确认',
  `paid_at` DATETIME DEFAULT NULL COMMENT '支付时间',
  `finished_at` DATETIME DEFAULT NULL COMMENT '订单完成时间',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除：1-已删除，0-未删除（逻辑删除）',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_order_no` (`order_no`),
  KEY `idx_order_buyer` (`buyer_id`),
  KEY `idx_order_seller` (`seller_id`),
  KEY `idx_order_status` (`order_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单表';

CREATE TABLE IF NOT EXISTS `goods_report` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '举报记录ID，主键，自增',
  `goods_id` BIGINT NOT NULL COMMENT '被举报商品ID，关联goods表',
  `reporter_id` BIGINT NOT NULL COMMENT '举报用户ID，关联user表',
  `reason` VARCHAR(255) NOT NULL COMMENT '举报原因（如：虚假信息、违规内容等）',
  `content` VARCHAR(500) DEFAULT NULL COMMENT '举报详情描述',
  `status` VARCHAR(32) NOT NULL DEFAULT 'PENDING' COMMENT '举报状态：PENDING-待处理，HANDLED-已处理',
  `handler_id` BIGINT DEFAULT NULL COMMENT '处理管理员ID，关联admin_user表',
  `handle_result` VARCHAR(500) DEFAULT NULL COMMENT '处理结果说明',
  `handled_at` DATETIME DEFAULT NULL COMMENT '处理时间',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '举报时间',
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除：1-已删除，0-未删除（逻辑删除）',
  PRIMARY KEY (`id`),
  KEY `idx_report_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品举报表';

CREATE TABLE IF NOT EXISTS `notice` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '公告ID，主键，自增',
  `title` VARCHAR(128) NOT NULL COMMENT '公告标题',
  `content` TEXT NOT NULL COMMENT '公告内容',
  `cover_url` VARCHAR(255) DEFAULT NULL COMMENT '公告封面图片URL',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '公告状态：1-发布，0-草稿',
  `publish_admin_id` BIGINT DEFAULT NULL COMMENT '发布管理员ID，关联admin_user表',
  `published_at` DATETIME DEFAULT NULL COMMENT '发布时间',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除：1-已删除，0-未删除（逻辑删除）',
  PRIMARY KEY (`id`),
  KEY `idx_notice_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='公告表';

CREATE TABLE IF NOT EXISTS `goods_audit` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '审核记录ID，主键，自增',
  `goods_id` BIGINT NOT NULL COMMENT '被审核商品ID，关联goods表',
  `admin_id` BIGINT NOT NULL COMMENT '审核管理员ID，关联admin_user表',
  `result` VARCHAR(32) NOT NULL COMMENT '审核结果：APPROVED-通过，REJECTED-拒绝',
  `reason` VARCHAR(255) DEFAULT NULL COMMENT '审核拒绝原因',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '审核时间',
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除：1-已删除，0-未删除（逻辑删除）',
  PRIMARY KEY (`id`),
  KEY `idx_audit_goods` (`goods_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品审核记录表';

CREATE TABLE IF NOT EXISTS `history_record` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '浏览记录ID，主键，自增',
  `user_id` BIGINT NOT NULL COMMENT '浏览用户ID，关联user表',
  `goods_id` BIGINT NOT NULL COMMENT '浏览商品ID，关联goods表',
  `title` VARCHAR(128) NOT NULL COMMENT '商品标题（冗余存储，避免关联查询）',
  `price` DECIMAL(10,2) NOT NULL COMMENT '商品价格（冗余存储）',
  `images` VARCHAR(255) DEFAULT NULL COMMENT '商品图片URL（冗余存储）',
  `view_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '浏览时间',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除：1-已删除，0-未删除（逻辑删除）',
  PRIMARY KEY (`id`),
  KEY `idx_history_user` (`user_id`),
  KEY `idx_history_view_time` (`view_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='浏览记录表';

INSERT INTO `admin_user` (`username`,`password`,`real_name`,`role_name`,`status`) VALUES
('admin','e10adc3949ba59abbe56e057f20f883e','系统管理员','SUPER_ADMIN',1)
ON DUPLICATE KEY UPDATE `updated_at` = CURRENT_TIMESTAMP;

INSERT INTO `user` (`openid`,`nickname`,`avatar_url`,`phone`,`status`,`password`) VALUES
('wx_seed_user_001','测试用户A',NULL,'13800000001',1,'e10adc3949ba59abbe56e057f20f883e'),
('wx_seed_user_002','测试用户B',NULL,'13800000002',1,'e10adc3949ba59abbe56e057f20f883e')
ON DUPLICATE KEY UPDATE `updated_at` = CURRENT_TIMESTAMP;

INSERT INTO `category` (`name`,`sort`,`status`) VALUES
('手机数码',100,1),('电脑办公',90,1),('家电家具',80,1),('图书文体',70,1),('服饰鞋包',60,1)
ON DUPLICATE KEY UPDATE `updated_at` = CURRENT_TIMESTAMP;