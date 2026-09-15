package com.zzyl.serve.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.zzyl.common.annotation.Excel;

/**
 * 美食点赞记录对象 food_like
 * 
 * @author admin
 * @date 2026-09-15
 */
public class FoodLike implements java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    /** 点赞ID */
    private Long id;

    /** 美食ID */
    private Long foodId;

    /** 用户ID */
    private Long userId;

    /** 点赞时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "点赞时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /** 美食标题（关联查询） */
    @Excel(name = "美食标题")
    private String foodTitle;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setFoodId(Long foodId) 
    {
        this.foodId = foodId;
    }

    public Long getFoodId() 
    {
        return foodId;
    }
    public void setUserId(Long userId) 
    {
        this.userId = userId;
    }

    public Long getUserId() 
    {
        return userId;
    }
    public void setCreateTime(Date createTime) 
    {
        this.createTime = createTime;
    }

    public Date getCreateTime() 
    {
        return createTime;
    }
    public void setFoodTitle(String foodTitle) 
    {
        this.foodTitle = foodTitle;
    }

    public String getFoodTitle() 
    {
        return foodTitle;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("foodId", getFoodId())
            .append("userId", getUserId())
            .append("createTime", getCreateTime())
            .append("foodTitle", getFoodTitle())
            .toString();
    }
}
