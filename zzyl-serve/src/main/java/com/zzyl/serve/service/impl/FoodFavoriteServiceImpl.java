package com.zzyl.serve.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.zzyl.serve.mapper.FoodFavoriteMapper;
import com.zzyl.serve.domain.FoodFavorite;
import com.zzyl.serve.service.IFoodFavoriteService;

/**
 * 美食收藏记录Service业务层处理
 * 
 * @author admin
 * @date 2026-09-15
 */
@Service
public class FoodFavoriteServiceImpl implements IFoodFavoriteService 
{
    @Autowired
    private FoodFavoriteMapper foodFavoriteMapper;

    /**
     * 查询美食收藏记录
     * 
     * @param id 美食收藏记录主键
     * @return 美食收藏记录
     */
    @Override
    public FoodFavorite selectFoodFavoriteById(Long id)
    {
        return foodFavoriteMapper.selectFoodFavoriteById(id);
    }

    /**
     * 查询美食收藏记录列表
     * 
     * @param foodFavorite 美食收藏记录
     * @return 美食收藏记录
     */
    @Override
    public List<FoodFavorite> selectFoodFavoriteList(FoodFavorite foodFavorite)
    {
        return foodFavoriteMapper.selectFoodFavoriteList(foodFavorite);
    }

    /**
     * 批量删除美食收藏记录
     * 
     * @param ids 需要删除的美食收藏记录主键
     * @return 结果
     */
    @Override
    public int deleteFoodFavoriteByIds(Long[] ids)
    {
        return foodFavoriteMapper.deleteFoodFavoriteByIds(ids);
    }

    /**
     * 删除美食收藏记录信息
     * 
     * @param id 美食收藏记录主键
     * @return 结果
     */
    @Override
    public int deleteFoodFavoriteById(Long id)
    {
        return foodFavoriteMapper.deleteFoodFavoriteById(id);
    }
}
