package com.zzyl.serve.mapper;

import org.apache.ibatis.annotations.Param;

/**
 * App 端用户相关Mapper接口（注册时的角色关联）
 *
 * @author admin
 * @date 2026-09-15
 */
public interface AppUserMapper
{
    /**
     * 查询角色ID（按角色标识，如 common）
     *
     * @param roleKey 角色标识
     * @return 角色ID，不存在返回 null
     */
    public Long selectRoleIdByKey(@Param("roleKey") String roleKey);

    /**
     * 新增用户与角色关联
     *
     * @param userId 用户ID
     * @param roleId 角色ID
     * @return 结果
     */
    public int insertUserRole(@Param("userId") Long userId, @Param("roleId") Long roleId);
}
