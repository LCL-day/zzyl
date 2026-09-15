package com.zzyl.serve.controller;

import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.zzyl.common.annotation.Anonymous;
import com.zzyl.common.constant.UserConstants;
import com.zzyl.common.core.controller.BaseController;
import com.zzyl.common.core.domain.AjaxResult;
import com.zzyl.common.core.domain.entity.SysUser;
import com.zzyl.common.utils.SecurityUtils;
import com.zzyl.common.utils.StringUtils;
import com.zzyl.serve.dto.RegisterDto;
import com.zzyl.serve.mapper.AppUserMapper;
import com.zzyl.system.service.ISysConfigService;
import com.zzyl.system.service.ISysUserService;

/**
 * 美食分享平台 App 端账号接口（注册、注册开关）
 *
 * @author admin
 * @date 2026-09-15
 */
@RestController
@RequestMapping("/food/app/auth")
public class AppAuthController extends BaseController
{
    /** 账号只允许字母、数字、下划线，避免与内容表 create_by 的匹配规则冲突 */
    private static final Pattern USERNAME_PATTERN = Pattern.compile("^[A-Za-z0-9_]{2,20}$");

    /** 注册后默认授予的角色标识 */
    private static final String DEFAULT_ROLE_KEY = "common";
    private static final long FALLBACK_ROLE_ID = 2L;

    @Autowired
    private ISysUserService sysUserService;

    @Autowired
    private ISysConfigService configService;

    @Autowired
    private AppUserMapper appUserMapper;

    /**
     * 是否开放注册（前端据此显示「注册」入口），匿名可访问
     */
    @Anonymous
    @GetMapping("/registerEnabled")
    public AjaxResult registerEnabled()
    {
        boolean enabled = "true".equals(configService.selectConfigByKey("sys.account.registerUser"));
        return success(enabled);
    }

    /**
     * 注册新用户，匿名可访问
     */
    @Anonymous
    @PostMapping("/register")
    public AjaxResult register(@RequestBody RegisterDto dto)
    {
        // 1、注册开关
        if (!"true".equals(configService.selectConfigByKey("sys.account.registerUser")))
        {
            return error("当前系统没有开启注册功能！");
        }

        String username = dto.getUsername() == null ? "" : dto.getUsername().trim();
        String nickName = dto.getNickName() == null ? "" : dto.getNickName().trim();
        String password = dto.getPassword() == null ? "" : dto.getPassword();

        // 2、账号校验
        if (StringUtils.isEmpty(username))
        {
            return error("请输入登录账号");
        }
        if (!USERNAME_PATTERN.matcher(username).matches())
        {
            return error("账号只能使用 2-20 位字母、数字或下划线");
        }
        // 3、昵称校验
        if (StringUtils.isEmpty(nickName))
        {
            nickName = username;
        }
        if (nickName.length() > 30)
        {
            return error("昵称最多 30 个字符");
        }
        // 4、密码校验
        if (StringUtils.isEmpty(password))
        {
            return error("请输入登录密码");
        }
        if (password.length() < UserConstants.PASSWORD_MIN_LENGTH
                || password.length() > UserConstants.PASSWORD_MAX_LENGTH)
        {
            return error("密码长度必须在 " + UserConstants.PASSWORD_MIN_LENGTH
                    + " 到 " + UserConstants.PASSWORD_MAX_LENGTH + " 个字符之间");
        }
        if (StringUtils.isNotEmpty(dto.getConfirmPassword())
                && !password.equals(dto.getConfirmPassword()))
        {
            return error("两次输入的密码不一致");
        }
        // 5、账号唯一性
        SysUser query = new SysUser();
        query.setUserName(username);
        if (!sysUserService.checkUserNameUnique(query))
        {
            return error("账号「" + username + "」已被注册，请更换");
        }

        // 6、写入用户
        SysUser user = new SysUser();
        user.setUserName(username);
        user.setNickName(nickName);
        user.setPassword(SecurityUtils.encryptPassword(password));
        user.setStatus("0");
        user.setRemark("App 端自主注册");
        if (!sysUserService.registerUser(user))
        {
            return error("注册失败，请稍后重试");
        }

        // 7、分配默认角色（普通用户），使新账号能看到 App 端菜单
        Long roleId = appUserMapper.selectRoleIdByKey(DEFAULT_ROLE_KEY);
        if (roleId == null)
        {
            roleId = FALLBACK_ROLE_ID;
        }
        Long userId = user.getUserId();
        if (userId == null)
        {
            SysUser created = sysUserService.selectUserByUserName(username);
            userId = created == null ? null : created.getUserId();
        }
        if (userId != null)
        {
            appUserMapper.insertUserRole(userId, roleId);
        }

        AjaxResult result = success("注册成功，请登录");
        result.put("userName", username);
        result.put("nickName", nickName);
        return result;
    }
}
