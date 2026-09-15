package com.zzyl.serve.dto;

/**
 * App 端发表评论请求体
 *
 * @author admin
 * @date 2026-09-15
 */
public class CommentDto
{
    /** 美食ID */
    private Long foodId;

    /** 评论内容 */
    private String content;

    /** 父评论ID，0 表示直接评论 */
    private Long parentId;

    public Long getFoodId()
    {
        return foodId;
    }

    public void setFoodId(Long foodId)
    {
        this.foodId = foodId;
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
}
