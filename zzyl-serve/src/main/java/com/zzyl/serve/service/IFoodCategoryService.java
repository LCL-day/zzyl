package com.zzyl.serve.service;

import java.util.List;
import com.zzyl.serve.domain.FoodCategory;

/**
 * 美食分类Service接口
 * 
 * @author admin
 * @date 2026-09-15
 */
public interface IFoodCategoryService 
{
    /**
     * 查询美食分类
     * 
     * @param id 美食分类主键
     * @return 美食分类
     */
    public FoodCategory selectFoodCategoryById(Long id);

    /**
     * 查询美食分类列表
     * 
     * @param foodCategory 美食分类
     * @return 美食分类集合
     */
    public List<FoodCategory> selectFoodCategoryList(FoodCategory foodCategory);

    /**
     * 新增美食分类
     * 
     * @param foodCategory 美食分类
     * @return 结果
     */
    public int insertFoodCategory(FoodCategory foodCategory);

    /**
     * 修改美食分类
     * 
     * @param foodCategory 美食分类
     * @return 结果
     */
    public int updateFoodCategory(FoodCategory foodCategory);

    /**
     * 批量删除美食分类
     * 
     * @param ids 需要删除的美食分类主键集合
     * @return 结果
     */
    public int deleteFoodCategoryByIds(Long[] ids);

    /**
     * 删除美食分类信息
     * 
     * @param id 美食分类主键
     * @return 结果
     */
    public int deleteFoodCategoryById(Long id);
}
