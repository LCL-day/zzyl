-- ----------------------------------------------------------------------------
-- 美食分享平台 App 端升级脚本
-- 1) 修正示例数据的外键错位（种子数据按 id 从 1 写，实际自增从 100 开始）
-- 2) 按实际记录重算点赞/收藏/评论计数
-- 3) 新增 App 端菜单（首页/收藏/发布/我的…），并补齐角色菜单授权
-- 本脚本可重复执行
-- ----------------------------------------------------------------------------
SET NAMES utf8mb4;

-- ---------------------------------------------------------------------------
-- 1、修正美食信息的分类外键（food_category 实际 id 为 100~104）
-- ---------------------------------------------------------------------------
UPDATE `food_info` SET `category_id` = 100 WHERE `category_id` = 1;
UPDATE `food_info` SET `category_id` = 101 WHERE `category_id` = 2;
UPDATE `food_info` SET `category_id` = 102 WHERE `category_id` = 3;
UPDATE `food_info` SET `category_id` = 103 WHERE `category_id` = 4;
UPDATE `food_info` SET `category_id` = 104 WHERE `category_id` = 5;

-- ---------------------------------------------------------------------------
-- 2、修正互动记录的美食外键（food_info 实际 id 为 100~105）
-- ---------------------------------------------------------------------------
UPDATE `food_like`     SET `food_id` = `food_id` + 99 WHERE `food_id` BETWEEN 1 AND 5;
UPDATE `food_favorite` SET `food_id` = `food_id` + 99 WHERE `food_id` BETWEEN 1 AND 5;
UPDATE `food_comment`  SET `food_id` = `food_id` + 99 WHERE `food_id` BETWEEN 1 AND 5;

-- ---------------------------------------------------------------------------
-- 3、按实际记录重算计数，保证列表数字与详情一致
-- ---------------------------------------------------------------------------
UPDATE `food_info` fi SET
  fi.`like_count`     = (SELECT COUNT(1) FROM `food_like`     fl WHERE fl.`food_id` = fi.`id`),
  fi.`favorite_count` = (SELECT COUNT(1) FROM `food_favorite` ff WHERE ff.`food_id` = fi.`id`),
  fi.`comment_count`  = (SELECT COUNT(1) FROM `food_comment`  fc WHERE fc.`food_id` = fi.`id`);

-- ---------------------------------------------------------------------------
-- 4、App 端菜单（menu_id 使用 3100 段，可重复执行）
-- ---------------------------------------------------------------------------
DELETE FROM `sys_role_menu` WHERE `menu_id` BETWEEN 3100 AND 3108;
DELETE FROM `sys_menu`      WHERE `menu_id` BETWEEN 3100 AND 3108;

INSERT INTO `sys_menu` VALUES ('3100', '美食App', '0',  '6', 'app',      NULL, '', '', 1, 0, 'M', '0', '0', '',                     'phone', 'admin', sysdate(), '', NULL, '美食分享平台App端目录');
INSERT INTO `sys_menu` VALUES ('3101', '首页',    '3100', '1', 'home',     'food/app/home/index',      '', '', 1, 0, 'C', '0', '0', 'food:app:home',      'home',    'admin', sysdate(), '', NULL, '首页（含搜索）');
INSERT INTO `sys_menu` VALUES ('3102', '收藏',    '3100', '2', 'favorite', 'food/app/favorite/index',  '', '', 1, 0, 'C', '0', '0', 'food:app:favorite',  'star',    'admin', sysdate(), '', NULL, '我的收藏');
INSERT INTO `sys_menu` VALUES ('3103', '发布',    '3100', '3', 'publish',  'food/app/publish/index',   '', '', 1, 0, 'C', '0', '0', 'food:app:publish',   'edit',    'admin', sysdate(), '', NULL, '发布美食');
INSERT INTO `sys_menu` VALUES ('3104', '我的',    '3100', '4', 'mine',     'food/app/mine/index',      '', '', 1, 0, 'C', '0', '0', 'food:app:mine',      'user',    'admin', sysdate(), '', NULL, '我的（含评论/收到评论）');
INSERT INTO `sys_menu` VALUES ('3105', '内容详情', '3100', '5', 'detail',  'food/app/detail/index',    '', '', 0, 0, 'C', '1', '0', 'food:app:detail',    'document','admin', sysdate(), '', NULL, '详情页（隐藏菜单）');

-- ---------------------------------------------------------------------------
-- 5、角色菜单授权
--    role 1 超级管理员：持有全部菜单，保证后台 CRUD 不受静态路由改造影响
--    role 2 普通用户  ：仅持有 App 端菜单（发布/收藏/我的）
-- ---------------------------------------------------------------------------
DELETE FROM `sys_role_menu` WHERE `role_id` IN (1, 2);
-- 超级管理员：美食管理 + App 端
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`)
SELECT 1, `menu_id` FROM `sys_menu` WHERE `menu_id` BETWEEN 3000 AND 3024;
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`)
SELECT 2, `menu_id` FROM `sys_menu` WHERE `menu_id` BETWEEN 3100 AND 3108;

-- 发布权限（App 端发布内容所需的按钮权限标识，供后台角色配置使用）
DELETE FROM `sys_menu` WHERE `menu_id` = 3025;
INSERT INTO `sys_menu` VALUES ('3025', '内容发布', '3007', '6', '', '', '', '', 1, 0, 'F', '0', '0', 'food:info:publish', '#', 'admin', sysdate(), '', NULL, '');

-- ---------------------------------------------------------------------------
-- 6、校验输出
-- ---------------------------------------------------------------------------
SELECT '分类已关联的条数' AS `检查项`, COUNT(1) AS `结果` FROM `food_info` fi
  INNER JOIN `food_category` fc ON fc.`id` = fi.`category_id`
UNION ALL
SELECT '点赞记录关联有效美食', COUNT(1) FROM `food_like` fl
  INNER JOIN `food_info` fi ON fi.`id` = fl.`food_id`
UNION ALL
SELECT '收藏记录关联有效美食', COUNT(1) FROM `food_favorite` ff
  INNER JOIN `food_info` fi ON fi.`id` = ff.`food_id`
UNION ALL
SELECT '评论关联有效美食', COUNT(1) FROM `food_comment` fc
  INNER JOIN `food_info` fi ON fi.`id` = fc.`food_id`
UNION ALL
SELECT 'App菜单数量', COUNT(1) FROM `sys_menu` WHERE `menu_id` BETWEEN 3100 AND 3108
UNION ALL
SELECT '角色菜单授权数', COUNT(1) FROM `sys_role_menu`;
