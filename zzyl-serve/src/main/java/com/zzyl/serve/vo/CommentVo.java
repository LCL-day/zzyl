package com.zzyl.serve.vo;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * App 端评论视图对象
 *
 * @author admin
 * @date 2026-09-15
 */
public class CommentVo
{
    /** 评论ID */
    private Long id;

    /** 美食ID */
    private Long foodId;

    /** 美食标题（我的评论列表用） */
    private String foodTitle;

    /** 美食封面（收到评论列表用） */
    private String foodImage;

    /** 评论人ID */
    private Long userId;

    /** 评论人昵称 */
    private String nickName;

    /** 评论人头像 */
    private String avatar;

    /** 评论内容 */
    private String content;

    /** 父评论ID */
    private Long parentId;

    /** 父评论内容（回复关系展示用） */
    private String parentContent;

    /** 父评论人昵称 */
    private String parentNickName;

    /** 评论时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getFoodId()
    {
        return foodId;
    }

    public void setFoodId(Long foodId)
    {
        this.foodId = foodId;
    }

    public String getFoodTitle()
    {
        return foodTitle;
    }

    public void setFoodTitle(String foodTitle)
    {
        this.foodTitle = foodTitle;
    }

    public String getFoodImage()
    {
        return foodImage;
    }

    public void setFoodImage(String foodImage)
    {
        this.foodImage = foodImage;
    }

    public Long getUserId()
    {
        return userId;
    }

    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public String getNickName()
    {
        return nickName;
    }

    public void setNickName(String nickName)
    {
        this.nickName = nickName;
    }

    public String getAvatar()
    {
        return avatar;
    }

    public void setAvatar(String avatar)
    {
        this.avatar = avatar;
    }

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public Long getParentId()
    {
        return parentId;
    }

    public void setParentId(Long parentId)
    {
        this.parentId = parentId;
    }

    public String getParentContent()
    {
        return parentContent;
    }

    public void setParentContent(String parentContent)
    {
        this.parentContent = parentContent;
    }

    public String getParentNickName()
    {
        return parentNickName;
    }

    public void setParentNickName(String parentNickName)
    {
        this.parentNickName = parentNickName;
    }

    public Date getCreateTime()
    {
        return createTime;
    }

    public void setCreateTime(Date createTime)
    {
        this.createTime = createTime;
    }
}
