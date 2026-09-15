-- ----------------------------------------------------------------------------
-- 清理指向不存在内容的互动记录（历史遗留脏数据），并重算计数
-- 可重复执行
-- ----------------------------------------------------------------------------
SET NAMES utf8mb4;

DELETE FROM `food_comment`
 WHERE `food_id` NOT IN (SELECT `id` FROM (SELECT `id` FROM `food_info`) t);

DELETE FROM `food_like`
 WHERE `food_id` NOT IN (SELECT `id` FROM (SELECT `id` FROM `food_info`) t);

DELETE FROM `food_favorite`
 WHERE `food_id` NOT IN (SELECT `id` FROM (SELECT `id` FROM `food_info`) t);

DELETE FROM `food_comment`
 WHERE `parent_id` <> 0
   AND `parent_id` NOT IN (SELECT `id` FROM (SELECT `id` FROM `food_comment`) t);

UPDATE `food_info` fi SET
  fi.`like_count`     = (SELECT COUNT(1) FROM `food_like`     fl WHERE fl.`food_id` = fi.`id`),
  fi.`favorite_count` = (SELECT COUNT(1) FROM `food_favorite` ff WHERE ff.`food_id` = fi.`id`),
  fi.`comment_count`  = (SELECT COUNT(1) FROM `food_comment`  fc WHERE fc.`food_id` = fi.`id`);

SELECT '内容条数' AS `检查项`, COUNT(1) AS `结果` FROM `food_info`
UNION ALL SELECT '评论条数', COUNT(1) FROM `food_comment`
UNION ALL SELECT '孤立评论(指向不存在内容)', COUNT(1) FROM `food_comment`
  WHERE `food_id` NOT IN (SELECT `id` FROM (SELECT `id` FROM `food_info`) t)
UNION ALL SELECT '孤立点赞', COUNT(1) FROM `food_like`
  WHERE `food_id` NOT IN (SELECT `id` FROM (SELECT `id` FROM `food_info`) t)
UNION ALL SELECT '孤立收藏', COUNT(1) FROM `food_favorite`
  WHERE `food_id` NOT IN (SELECT `id` FROM (SELECT `id` FROM `food_info`) t)
UNION ALL SELECT '孤儿回复', COUNT(1) FROM `food_comment` c
  WHERE c.`parent_id` <> 0
    AND c.`parent_id` NOT IN (SELECT `id` FROM (SELECT `id` FROM `food_comment`) t);
