package com.zzyl.serve.mapper;

import java.util.List;
import com.zzyl.serve.domain.FoodLike;

/**
 * 美食点赞记录Mapper接口
 * 
 * @author admin
 * @date 2026-09-15
 */
public interface FoodLikeMapper 
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
     * 新增美食点赞记录
     * 
     * @param foodLike 美食点赞记录
     * @return 结果
     */
    public int insertFoodLike(FoodLike foodLike);

    /**
     * 删除美食点赞记录
     * 
     * @param id 美食点赞记录主键
     * @return 结果
     */
    public int deleteFoodLikeById(Long id);

    /**
     * 批量删除美食点赞记录
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFoodLikeByIds(Long[] ids);
}
