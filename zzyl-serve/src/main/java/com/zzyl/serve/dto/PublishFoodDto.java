package com.zzyl.serve.dto;

/**
 * App 端发布美食请求体
 *
 * @author admin
 * @date 2026-09-15
 */
public class PublishFoodDto
{
    /** 美食标题 */
    private String title;

    /** 分类ID */
    private Long categoryId;

    /** 简介 */
    private String description;

    /** 封面图片 */
    private String image;

    /** 详细内容 */
    private String content;

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public Long getCategoryId()
    {
        return categoryId;
    }

    public void setCategoryId(Long categoryId)
    {
        this.categoryId = categoryId;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getImage()
    {
        return image;
    }

    public void setImage(String image)
    {
        this.image = image;
    }

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }
}
