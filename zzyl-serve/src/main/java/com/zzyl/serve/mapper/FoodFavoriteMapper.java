package com.zzyl.serve.mapper;

import java.util.List;
import com.zzyl.serve.domain.FoodFavorite;

/**
 * 美食收藏记录Mapper接口
 * 
 * @author admin
 * @date 2026-09-15
 */
public interface FoodFavoriteMapper 
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
     * 新增美食收藏记录
     * 
     * @param foodFavorite 美食收藏记录
     * @return 结果
     */
    public int insertFoodFavorite(FoodFavorite foodFavorite);

    /**
     * 删除美食收藏记录
     * 
     * @param id 美食收藏记录主键
     * @return 结果
     */
    public int deleteFoodFavoriteById(Long id);

    /**
     * 批量删除美食收藏记录
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFoodFavoriteByIds(Long[] ids);
}
