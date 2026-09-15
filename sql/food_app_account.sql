-- ----------------------------------------------------------------------------
-- 美食分享平台 账号与登录开关
-- 1) 关闭登录验证码（登录、注册都不再需要验证码）
-- 2) 开放自主注册
-- 执行后需重启后端，或清理 Redis 中 sys_config:* 缓存（值为缓存 30 天）
-- ----------------------------------------------------------------------------
SET NAMES utf8mb4;

-- 不存在则插入，存在则更新，保证可重复执行
INSERT INTO `sys_config` (`config_key`, `config_value`, `config_type`, `create_by`, `create_time`, `remark`)
SELECT 'sys.account.captchaEnabled', 'false', 'Y', 'admin', sysdate(), '是否开启验证码功能（false 关闭登录验证码）'
 WHERE NOT EXISTS (SELECT 1 FROM `sys_config` WHERE `config_key` = 'sys.account.captchaEnabled');

INSERT INTO `sys_config` (`config_key`, `config_value`, `config_type`, `create_by`, `create_time`, `remark`)
SELECT 'sys.account.registerUser', 'true', 'Y', 'admin', sysdate(), '是否开启注册功能（true 开放自主注册）'
 WHERE NOT EXISTS (SELECT 1 FROM `sys_config` WHERE `config_key` = 'sys.account.registerUser');

UPDATE `sys_config` SET `config_value` = 'false' WHERE `config_key` = 'sys.account.captchaEnabled';
UPDATE `sys_config` SET `config_value` = 'true'  WHERE `config_key` = 'sys.account.registerUser';

-- 校验
SELECT `config_key` AS `配置项`, `config_value` AS `当前值`, `remark` AS `说明`
  FROM `sys_config`
 WHERE `config_key` IN ('sys.account.captchaEnabled', 'sys.account.registerUser');
