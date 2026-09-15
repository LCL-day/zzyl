-- ----------------------------------------------------------------------------
-- 美食分享平台 示例数据
-- ----------------------------------------------------------------------------
SET NAMES utf8mb4;

-- 清空示例数据（保证可重复执行）
DELETE FROM `food_comment`;
DELETE FROM `food_like`;
DELETE FROM `food_favorite`;
DELETE FROM `food_info`;
DELETE FROM `food_category`;

-- 1、美食分类
INSERT INTO `food_category` (`name`, `order_num`, `status`, `create_by`, `create_time`, `remark`) VALUES
('川菜', 1, '0', 'admin', sysdate(), '麻辣鲜香'),
('粤菜', 2, '0', 'admin', sysdate(), '清淡鲜美'),
('鲁菜', 3, '0', 'admin', sysdate(), '咸鲜为主'),
('甜品', 4, '0', 'admin', sysdate(), '饭后甜点'),
('小吃', 5, '0', 'admin', sysdate(), '各地特色');

-- 2、美食信息
INSERT INTO `food_info` (`category_id`, `title`, `image`, `description`, `content`, `like_count`, `favorite_count`, `comment_count`, `status`, `create_by`, `create_time`, `remark`) VALUES
(1, '麻婆豆腐', '', '麻辣鲜香，下饭神器', '经典川菜，豆腐嫩滑，麻辣鲜香，是川菜中的代表作。', 128, 56, 12, '0', 'admin', sysdate(), ''),
(1, '宫保鸡丁', '', '酸甜微辣，鸡肉嫩滑', '川菜代表，花生酥脆，鸡肉鲜嫩，酸甜微辣。', 96, 40, 8, '0', 'admin', sysdate(), ''),
(2, '白切鸡', '', '皮爽肉滑，原汁原味', '粤菜经典，皮爽肉滑，蘸料提鲜。', 85, 32, 6, '0', 'admin', sysdate(), ''),
(3, '红烧肉', '', '肥而不腻，入口即化', '鲁菜代表，色泽红亮，软糯香甜，肥而不腻。', 150, 70, 15, '0', 'admin', sysdate(), ''),
(4, '提拉米苏', '', '咖啡与奶油的完美结合', '意式甜点，咖啡与奶油的完美结合，入口即化。', 200, 120, 20, '0', 'admin', sysdate(), ''),
(5, '小笼包', '', '皮薄馅多，汤汁鲜美', '江南特色小吃，皮薄馅多，先开窗后喝汤。', 180, 90, 18, '0', 'admin', sysdate(), '');

-- 3、点赞记录
INSERT INTO `food_like` (`food_id`, `user_id`, `create_time`) VALUES
(1, 1, sysdate()), (1, 2, sysdate()), (2, 1, sysdate()), (3, 2, sysdate()), (4, 1, sysdate()), (5, 3, sysdate());

-- 4、收藏记录
INSERT INTO `food_favorite` (`food_id`, `user_id`, `create_time`) VALUES
(1, 1, sysdate()), (4, 2, sysdate()), (5, 1, sysdate());

-- 5、评论
INSERT INTO `food_comment` (`food_id`, `user_id`, `content`, `parent_id`, `like_count`, `status`, `create_by`, `create_time`) VALUES
(1, 2, '味道超赞，下次还来！', 0, 5, '0', 'admin', sysdate()),
(1, 3, '麻辣程度刚好', 0, 3, '0', 'admin', sysdate()),
(4, 1, '甜品天花板！', 0, 8, '0', 'admin', sysdate()),
(5, 2, '汤汁很鲜', 0, 2, '0', 'admin', sysdate());
