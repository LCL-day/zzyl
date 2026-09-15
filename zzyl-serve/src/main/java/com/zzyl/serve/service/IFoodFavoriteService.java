package com.zzyl.serve.service;

import java.util.List;
import com.zzyl.serve.domain.FoodFavorite;

/**
 * 美食收藏记录Service接口
 * 
 * @author admin
 * @date 2026-09-15
 */
public interface IFoodFavoriteService 
{
    /**
     * 查询美食收藏记录
     * 
     * @param id 美食收藏记录主键
     * @return 美食收藏记录
     */
    public FoodFavorite selectFoodFavoriteById(Long id);

    /**
     * 查询美食收藏记录列表
     * 
     * @param foodFavorite 美食收藏记录
     * @return 美食收藏记录集合
     */
    public List<FoodFavorite> selectFoodFavoriteList(FoodFavorite foodFavorite);

    /**
     * 批量删除美食收藏记录
     * 
     * @param ids 需要删除的美食收藏记录主键集合
     * @return 结果
     */
    public int deleteFoodFavoriteByIds(Long[] ids);

    /**
     * 删除美食收藏记录信息
     * 
     * @param id 美食收藏记录主键
     * @return 结果
     */
    public int deleteFoodFavoriteById(Long id);
}
