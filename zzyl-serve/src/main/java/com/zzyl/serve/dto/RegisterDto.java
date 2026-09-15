package com.zzyl.serve.dto;

/**
 * App 端注册请求体
 *
 * @author admin
 * @date 2026-09-15
 */
public class RegisterDto
{
    /** 登录账号 */
    private String username;

    /** 昵称（为空时默认取账号） */
    private String nickName;

    /** 登录密码 */
    private String password;

    /** 确认密码 */
    private String confirmPassword;

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getNickName()
    {
        return nickName;
    }

    public void setNickName(String nickName)
    {
        this.nickName = nickName;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getConfirmPassword()
    {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword)
    {
        this.confirmPassword = confirmPassword;
    }
}
