-- ----------------------------------------------------------------------------
-- 美食分享平台 建表脚本
-- 数据库：ry
-- ----------------------------------------------------------------------------

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- 1、美食分类表
-- ----------------------------
DROP TABLE IF EXISTS `food_category`;
CREATE TABLE `food_category` (
  `id`          bigint(20)      NOT NULL AUTO_INCREMENT COMMENT '分类ID',
  `name`        varchar(50)     DEFAULT '' COMMENT '分类名称',
  `order_num`   int(4)          DEFAULT 0 COMMENT '显示顺序',
  `status`      char(1)         DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `create_by`   varchar(64)     DEFAULT '' COMMENT '创建者',
  `create_time` datetime COMMENT '创建时间',
  `update_by`   varchar(64)     DEFAULT '' COMMENT '更新者',
  `update_time` datetime COMMENT '更新时间',
  `remark`      varchar(500)    DEFAULT '' COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=100 COMMENT = '美食分类表';

-- ----------------------------
-- 2、美食信息表（发布内容）
-- ----------------------------
DROP TABLE IF EXISTS `food_info`;
CREATE TABLE `food_info` (
  `id`             bigint(20)      NOT NULL AUTO_INCREMENT COMMENT '美食ID',
  `category_id`    bigint(20)      DEFAULT NULL COMMENT '分类ID',
  `title`          varchar(100)    DEFAULT '' COMMENT '美食标题',
  `image`          varchar(255)    DEFAULT '' COMMENT '封面图片',
  `description`    varchar(500)    DEFAULT '' COMMENT '简介',
  `content`        text COMMENT '详细内容',
  `like_count`     int(11)         DEFAULT 0 COMMENT '点赞数',
  `favorite_count` int(11)         DEFAULT 0 COMMENT '收藏数',
  `comment_count`  int(11)         DEFAULT 0 COMMENT '评论数',
  `status`         char(1)         DEFAULT '0' COMMENT '状态（0发布 1下架）',
  `create_by`      varchar(64)     DEFAULT '' COMMENT '创建者',
  `create_time`    datetime COMMENT '创建时间',
  `update_by`      varchar(64)     DEFAULT '' COMMENT '更新者',
  `update_time`    datetime COMMENT '更新时间',
  `remark`         varchar(500)    DEFAULT '' COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=100 COMMENT = '美食信息表';

-- ----------------------------
-- 3、点赞记录表
-- ----------------------------
DROP TABLE IF EXISTS `food_like`;
CREATE TABLE `food_like` (
  `id`          bigint(20)      NOT NULL AUTO_INCREMENT COMMENT '点赞ID',
  `food_id`     bigint(20)      NOT NULL COMMENT '美食ID',
  `user_id`     bigint(20)      DEFAULT NULL COMMENT '用户ID',
  `create_time` datetime COMMENT '点赞时间',
  PRIMARY KEY (`id`),
  KEY `idx_food_id` (`food_id`)
) ENGINE=InnoDB AUTO_INCREMENT=100 COMMENT = '美食点赞记录表';

-- ----------------------------
-- 4、收藏记录表
-- ----------------------------
DROP TABLE IF EXISTS `food_favorite`;
CREATE TABLE `food_favorite` (
  `id`          bigint(20)      NOT NULL AUTO_INCREMENT COMMENT '收藏ID',
  `food_id`     bigint(20)      NOT NULL COMMENT '美食ID',
  `user_id`     bigint(20)      DEFAULT NULL COMMENT '用户ID',
  `create_time` datetime COMMENT '收藏时间',
  PRIMARY KEY (`id`),
  KEY `idx_food_id` (`food_id`)
) ENGINE=InnoDB AUTO_INCREMENT=100 COMMENT = '美食收藏记录表';

-- ----------------------------
-- 5、评论表
-- ----------------------------
DROP TABLE IF EXISTS `food_comment`;
CREATE TABLE `food_comment` (
  `id`          bigint(20)      NOT NULL AUTO_INCREMENT COMMENT '评论ID',
  `food_id`     bigint(20)      NOT NULL COMMENT '美食ID',
  `user_id`     bigint(20)      DEFAULT NULL COMMENT '用户ID',
  `content`     varchar(500)    DEFAULT '' COMMENT '评论内容',
  `parent_id`   bigint(20)      DEFAULT 0 COMMENT '父评论ID（回复用）',
  `like_count`  int(11)         DEFAULT 0 COMMENT '点赞数',
  `status`      char(1)         DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `create_by`   varchar(64)     DEFAULT '' COMMENT '创建者',
  `create_time` datetime COMMENT '评论时间',
  `update_by`   varchar(64)     DEFAULT '' COMMENT '更新者',
  `update_time` datetime COMMENT '更新时间',
  `remark`      varchar(500)    DEFAULT '' COMMENT '备注',
  PRIMARY KEY (`id`),
  KEY `idx_food_id` (`food_id`)
) ENGINE=InnoDB AUTO_INCREMENT=100 COMMENT = '美食评论表';

SET FOREIGN_KEY_CHECKS = 1;

-- ----------------------------------------------------------------------------
-- 菜单与按钮权限
-- ----------------------------------------------------------------------------
-- 菜单与按钮权限（menu_id 使用 3000 系列，与若依内置菜单区段不冲突）
-- ----------------------------------------------------------------------------
-- 清空旧的同段菜单，保证脚本可重复执行
DELETE FROM `sys_menu` WHERE menu_id BETWEEN 3000 AND 3024;

-- 一级目录：美食管理
INSERT INTO `sys_menu` VALUES ('3000', '美食管理', '0', '5', 'food', NULL, '', '', 1, 0, 'M', '0', '0', '', 'star', 'admin', sysdate(), '', NULL, '美食分享管理目录');

-- 美食分类
INSERT INTO `sys_menu` VALUES ('3001', '美食分类', '3000', '1', 'category', 'food/category/index', '', '', 1, 0, 'C', '0', '0', 'food:category:list', 'tree', 'admin', sysdate(), '', NULL, '美食分类菜单');
INSERT INTO `sys_menu` VALUES ('3002', '分类查询', '3001', '1', '', '', '', '', 1, 0, 'F', '0', '0', 'food:category:query', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO `sys_menu` VALUES ('3003', '分类新增', '3001', '2', '', '', '', '', 1, 0, 'F', '0', '0', 'food:category:add', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO `sys_menu` VALUES ('3004', '分类修改', '3001', '3', '', '', '', '', 1, 0, 'F', '0', '0', 'food:category:edit', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO `sys_menu` VALUES ('3005', '分类删除', '3001', '4', '', '', '', '', 1, 0, 'F', '0', '0', 'food:category:remove', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO `sys_menu` VALUES ('3006', '分类导出', '3001', '5', '', '', '', '', 1, 0, 'F', '0', '0', 'food:category:export', '#', 'admin', sysdate(), '', NULL, '');

-- 美食信息
INSERT INTO `sys_menu` VALUES ('3007', '美食信息', '3000', '2', 'info', 'food/info/index', '', '', 1, 0, 'C', '0', '0', 'food:info:list', 'example', 'admin', sysdate(), '', NULL, '美食信息菜单');
INSERT INTO `sys_menu` VALUES ('3008', '美食查询', '3007', '1', '', '', '', '', 1, 0, 'F', '0', '0', 'food:info:query', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO `sys_menu` VALUES ('3009', '美食新增', '3007', '2', '', '', '', '', 1, 0, 'F', '0', '0', 'food:info:add', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO `sys_menu` VALUES ('3010', '美食修改', '3007', '3', '', '', '', '', 1, 0, 'F', '0', '0', 'food:info:edit', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO `sys_menu` VALUES ('3011', '美食删除', '3007', '4', '', '', '', '', 1, 0, 'F', '0', '0', 'food:info:remove', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO `sys_menu` VALUES ('3012', '美食导出', '3007', '5', '', '', '', '', 1, 0, 'F', '0', '0', 'food:info:export', '#', 'admin', sysdate(), '', NULL, '');

-- 点赞管理
INSERT INTO `sys_menu` VALUES ('3013', '点赞管理', '3000', '3', 'like', 'food/like/index', '', '', 1, 0, 'C', '0', '0', 'food:like:list', 'star', 'admin', sysdate(), '', NULL, '点赞记录菜单');
INSERT INTO `sys_menu` VALUES ('3014', '点赞查询', '3013', '1', '', '', '', '', 1, 0, 'F', '0', '0', 'food:like:query', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO `sys_menu` VALUES ('3015', '点赞删除', '3013', '2', '', '', '', '', 1, 0, 'F', '0', '0', 'food:like:remove', '#', 'admin', sysdate(), '', NULL, '');

-- 收藏管理
INSERT INTO `sys_menu` VALUES ('3016', '收藏管理', '3000', '4', 'favorite', 'food/favorite/index', '', '', 1, 0, 'C', '0', '0', 'food:favorite:list', 'star', 'admin', sysdate(), '', NULL, '收藏记录菜单');
INSERT INTO `sys_menu` VALUES ('3017', '收藏查询', '3016', '1', '', '', '', '', 1, 0, 'F', '0', '0', 'food:favorite:query', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO `sys_menu` VALUES ('3018', '收藏删除', '3016', '2', '', '', '', '', 1, 0, 'F', '0', '0', 'food:favorite:remove', '#', 'admin', sysdate(), '', NULL, '');

-- 评论管理
INSERT INTO `sys_menu` VALUES ('3019', '评论管理', '3000', '5', 'comment', 'food/comment/index', '', '', 1, 0, 'C', '0', '0', 'food:comment:list', 'message', 'admin', sysdate(), '', NULL, '评论管理菜单');
INSERT INTO `sys_menu` VALUES ('3020', '评论查询', '3019', '1', '', '', '', '', 1, 0, 'F', '0', '0', 'food:comment:query', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO `sys_menu` VALUES ('3021', '评论新增', '3019', '2', '', '', '', '', 1, 0, 'F', '0', '0', 'food:comment:add', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO `sys_menu` VALUES ('3022', '评论修改', '3019', '3', '', '', '', '', 1, 0, 'F', '0', '0', 'food:comment:edit', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO `sys_menu` VALUES ('3023', '评论删除', '3019', '4', '', '', '', '', 1, 0, 'F', '0', '0', 'food:comment:remove', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO `sys_menu` VALUES ('3024', '评论导出', '3019', '5', '', '', '', '', 1, 0, 'F', '0', '0', 'food:comment:export', '#', 'admin', sysdate(), '', NULL, '');
