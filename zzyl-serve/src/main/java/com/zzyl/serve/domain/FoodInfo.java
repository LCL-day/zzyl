package com.zzyl.serve.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.zzyl.common.annotation.Excel;
import com.zzyl.common.core.domain.BaseEntity;

/**
 * 美食信息对象 food_info
 * 
 * @author admin
 * @date 2026-09-15
 */
public class FoodInfo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 美食ID */
    private Long id;

    /** 分类ID */
    private Long categoryId;

    /** 美食标题 */
    @Excel(name = "美食标题")
    private String title;

    /** 封面图片 */
    private String image;

    /** 简介 */
    @Excel(name = "简介")
    private String description;

    /** 详细内容 */
    private String content;

    /** 点赞数 */
    @Excel(name = "点赞数")
    private Integer likeCount;

    /** 收藏数 */
    @Excel(name = "收藏数")
    private Integer favoriteCount;

    /** 评论数 */
    @Excel(name = "评论数")
    private Integer commentCount;

    /** 状态（0发布 1下架） */
    @Excel(name = "状态", readConverterExp = "0=发布,1=下架")
    private String status;

    /** 分类名称（关联查询） */
    @Excel(name = "分类名称")
    private String categoryName;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setCategoryId(Long categoryId) 
    {
        this.categoryId = categoryId;
    }

    public Long getCategoryId() 
    {
        return categoryId;
    }
    public void setTitle(String title) 
    {
        this.title = title;
    }

    public String getTitle() 
    {
        return title;
    }
    public void setImage(String image) 
    {
        this.image = image;
    }

    public String getImage() 
    {
        return image;
    }
    public void setDescription(String description) 
    {
        this.description = description;
    }

    public String getDescription() 
    {
        return description;
    }
    public void setContent(String content) 
    {
        this.content = content;
    }

    public String getContent() 
    {
        return content;
    }
    public void setLikeCount(Integer likeCount) 
    {
        this.likeCount = likeCount;
    }

    public Integer getLikeCount() 
    {
        return likeCount;
    }
    public void setFavoriteCount(Integer favoriteCount) 
    {
        this.favoriteCount = favoriteCount;
    }

    public Integer getFavoriteCount() 
    {
        return favoriteCount;
    }
    public void setCommentCount(Integer commentCount) 
    {
        this.commentCount = commentCount;
    }

    public Integer getCommentCount() 
    {
        return commentCount;
    }
    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }
    public void setCategoryName(String categoryName) 
    {
        this.categoryName = categoryName;
    }

    public String getCategoryName() 
    {
        return categoryName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("categoryId", getCategoryId())
            .append("title", getTitle())
            .append("image", getImage())
            .append("description", getDescription())
            .append("content", getContent())
            .append("likeCount", getLikeCount())
            .append("favoriteCount", getFavoriteCount())
            .append("commentCount", getCommentCount())
            .append("status", getStatus())
            .append("createBy", getCreateBy())
            .append("updateBy", getUpdateBy())
            .append("remark", getRemark())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
