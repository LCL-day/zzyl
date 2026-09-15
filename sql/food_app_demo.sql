-- ----------------------------------------------------------------------------
-- 美食分享平台 App 端演示数据补充
-- 1) 清理引用不存在用户的互动记录（原示例数据用到了 user_id=3，sys_user 中并无该用户）
-- 2) 补充他人对 admin 内容的评论，使「收到评论」有数据可看
-- 本脚本可重复执行
-- ----------------------------------------------------------------------------
SET NAMES utf8mb4;

-- 1、把不存在用户的记录修正为已有用户
UPDATE `food_like`     SET `user_id` = 2 WHERE `user_id` NOT IN (SELECT `user_id` FROM `sys_user`);
UPDATE `food_favorite` SET `user_id` = 2 WHERE `user_id` NOT IN (SELECT `user_id` FROM `sys_user`);
UPDATE `food_comment`  SET `user_id` = 2 WHERE `user_id` NOT IN (SELECT `user_id` FROM `sys_user`);

-- 2、补充演示评论（user_id=2 若依 评论 admin 发布的内容，parent_id=0 为直接评论）
DELETE FROM `food_comment` WHERE `create_by` = 'demo';
INSERT INTO `food_comment` (`food_id`, `user_id`, `content`, `parent_id`, `like_count`, `status`, `create_by`, `create_time`) VALUES
  (101, 2, '鸡丁很嫩，花生也脆，下饭！', 0, 1, '0', 'demo', sysdate()),
  (103, 2, '红烧肉炖得很入味，肥而不腻。', 0, 2, '0', 'demo', sysdate()),
  (104, 2, '甜度刚好，孩子很喜欢。', 0, 0, '0', 'demo', sysdate());

-- 3、清理历史孤儿回复（父评论已被删除的回复），并重算计数
DELETE FROM `food_comment`
 WHERE `parent_id` <> 0
   AND `parent_id` NOT IN (SELECT `id` FROM (SELECT `id` FROM `food_comment`) t);

UPDATE `food_info` fi SET
  fi.`like_count`     = (SELECT COUNT(1) FROM `food_like`     fl WHERE fl.`food_id` = fi.`id`),
  fi.`favorite_count` = (SELECT COUNT(1) FROM `food_favorite` ff WHERE ff.`food_id` = fi.`id`),
  fi.`comment_count`  = (SELECT COUNT(1) FROM `food_comment`  fc WHERE fc.`food_id` = fi.`id`);

-- 4、校验输出
SELECT '无主的互动记录' AS `检查项`, (
  (SELECT COUNT(1) FROM `food_like`     WHERE `user_id` NOT IN (SELECT `user_id` FROM `sys_user`)) +
  (SELECT COUNT(1) FROM `food_favorite` WHERE `user_id` NOT IN (SELECT `user_id` FROM `sys_user`)) +
  (SELECT COUNT(1) FROM `food_comment`  WHERE `user_id` NOT IN (SELECT `user_id` FROM `sys_user`))
) AS `结果`
UNION ALL
SELECT 'admin 内容的评论数（收到评论）', COUNT(1) FROM `food_comment` c
  INNER JOIN `food_info` fi ON fi.`id` = c.`food_id`
    AND (fi.`create_by` = 'admin' OR fi.`create_by` = '1')
  WHERE c.`user_id` != 1
UNION ALL
SELECT '评论总数', COUNT(1) FROM `food_comment`;
