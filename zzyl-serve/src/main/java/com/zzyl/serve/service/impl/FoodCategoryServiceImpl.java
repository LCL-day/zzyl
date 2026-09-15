package com.zzyl.serve.service.impl;

import java.util.List;
import com.zzyl.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.zzyl.serve.mapper.FoodCategoryMapper;
import com.zzyl.serve.domain.FoodCategory;
import com.zzyl.serve.service.IFoodCategoryService;

/**
 * 美食分类Service业务层处理
 * 
 * @author admin
 * @date 2026-09-15
 */
@Service
public class FoodCategoryServiceImpl implements IFoodCategoryService 
{
    @Autowired
    private FoodCategoryMapper foodCategoryMapper;

    /**
     * 查询美食分类
     * 
     * @param id 美食分类主键
     * @return 美食分类
     */
    @Override
    public FoodCategory selectFoodCategoryById(Long id)
    {
        return foodCategoryMapper.selectFoodCategoryById(id);
    }

    /**
     * 查询美食分类列表
     * 
     * @param foodCategory 美食分类
     * @return 美食分类
     */
    @Override
    public List<FoodCategory> selectFoodCategoryList(FoodCategory foodCategory)
    {
        return foodCategoryMapper.selectFoodCategoryList(foodCategory);
    }

    /**
     * 新增美食分类
     * 
     * @param foodCategory 美食分类
     * @return 结果
     */
    @Override
    public int insertFoodCategory(FoodCategory foodCategory)
    {
        foodCategory.setCreateTime(DateUtils.getNowDate());
        return foodCategoryMapper.insertFoodCategory(foodCategory);
    }

    /**
     * 修改美食分类
     * 
     * @param foodCategory 美食分类
     * @return 结果
     */
    @Override
    public int updateFoodCategory(FoodCategory foodCategory)
    {
        foodCategory.setUpdateTime(DateUtils.getNowDate());
        return foodCategoryMapper.updateFoodCategory(foodCategory);
    }

    /**
     * 批量删除美食分类
     * 
     * @param ids 需要删除的美食分类主键
     * @return 结果
     */
    @Override
    public int deleteFoodCategoryByIds(Long[] ids)
    {
        return foodCategoryMapper.deleteFoodCategoryByIds(ids);
    }

    /**
     * 删除美食分类信息
     * 
     * @param id 美食分类主键
     * @return 结果
     */
    @Override
    public int deleteFoodCategoryById(Long id)
    {
        return foodCategoryMapper.deleteFoodCategoryById(id);
    }
}
