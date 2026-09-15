package com.zzyl.serve.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.zzyl.serve.mapper.FoodLikeMapper;
import com.zzyl.serve.domain.FoodLike;
import com.zzyl.serve.service.IFoodLikeService;

/**
 * 美食点赞记录Service业务层处理
 * 
 * @author admin
 * @date 2026-09-15
 */
@Service
public class FoodLikeServiceImpl implements IFoodLikeService 
{
    @Autowired
    private FoodLikeMapper foodLikeMapper;

    /**
     * 查询美食点赞记录
     * 
     * @param id 美食点赞记录主键
     * @return 美食点赞记录
     */
    @Override
    public FoodLike selectFoodLikeById(Long id)
    {
        return foodLikeMapper.selectFoodLikeById(id);
    }

    /**
     * 查询美食点赞记录列表
     * 
     * @param foodLike 美食点赞记录
     * @return 美食点赞记录
     */
    @Override
    public List<FoodLike> selectFoodLikeList(FoodLike foodLike)
    {
        return foodLikeMapper.selectFoodLikeList(foodLike);
    }

    /**
     * 批量删除美食点赞记录
     * 
     * @param ids 需要删除的美食点赞记录主键
     * @return 结果
     */
    @Override
    public int deleteFoodLikeByIds(Long[] ids)
    {
        return foodLikeMapper.deleteFoodLikeByIds(ids);
    }

    /**
     * 删除美食点赞记录信息
     * 
     * @param id 美食点赞记录主键
     * @return 结果
     */
    @Override
    public int deleteFoodLikeById(Long id)
    {
        return foodLikeMapper.deleteFoodLikeById(id);
    }
}
