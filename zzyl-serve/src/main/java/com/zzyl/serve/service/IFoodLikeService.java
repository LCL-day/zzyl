package com.zzyl.serve.service;

import java.util.List;
import com.zzyl.serve.domain.FoodLike;

/**
 * 美食点赞记录Service接口
 * 
 * @author admin
 * @date 2026-09-15
 */
public interface IFoodLikeService 
{
    /**
     * 查询美食点赞记录
     * 
     * @param id 美食点赞记录主键
     * @return 美食点赞记录
     */
    public FoodLike selectFoodLikeById(Long id);

    /**
     * 查询美食点赞记录列表
     * 
     * @param foodLike 美食点赞记录
     * @return 美食点赞记录集合
     */
    public List<FoodLike> selectFoodLikeList(FoodLike foodLike);

    /**
     * 批量删除美食点赞记录
     * 
     * @param ids 需要删除的美食点赞记录主键集合
     * @return 结果
     */
    public int deleteFoodLikeByIds(Long[] ids);

    /**
     * 删除美食点赞记录信息
     * 
     * @param id 美食点赞记录主键
     * @return 结果
     */
    public int deleteFoodLikeById(Long id);
}
