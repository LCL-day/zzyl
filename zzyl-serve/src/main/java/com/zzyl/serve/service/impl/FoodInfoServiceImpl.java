package com.zzyl.serve.service.impl;

import java.util.List;
import com.zzyl.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.zzyl.serve.mapper.FoodInfoMapper;
import com.zzyl.serve.domain.FoodInfo;
import com.zzyl.serve.service.IFoodInfoService;

/**
 * 美食信息Service业务层处理
 * 
 * @author admin
 * @date 2026-09-15
 */
@Service
public class FoodInfoServiceImpl implements IFoodInfoService 
{
    @Autowired
    private FoodInfoMapper foodInfoMapper;

    /**
     * 查询美食信息
     * 
     * @param id 美食信息主键
     * @return 美食信息
     */
    @Override
    public FoodInfo selectFoodInfoById(Long id)
    {
        return foodInfoMapper.selectFoodInfoById(id);
    }

    /**
     * 查询美食信息列表
     * 
     * @param foodInfo 美食信息
     * @return 美食信息
     */
    @Override
    public List<FoodInfo> selectFoodInfoList(FoodInfo foodInfo)
    {
        return foodInfoMapper.selectFoodInfoList(foodInfo);
    }

    /**
     * 新增美食信息
     * 
     * @param foodInfo 美食信息
     * @return 结果
     */
    @Override
    public int insertFoodInfo(FoodInfo foodInfo)
    {
        foodInfo.setCreateTime(DateUtils.getNowDate());
        return foodInfoMapper.insertFoodInfo(foodInfo);
    }

    /**
     * 修改美食信息
     * 
     * @param foodInfo 美食信息
     * @return 结果
     */
    @Override
    public int updateFoodInfo(FoodInfo foodInfo)
    {
        foodInfo.setUpdateTime(DateUtils.getNowDate());
        return foodInfoMapper.updateFoodInfo(foodInfo);
    }

    /**
     * 批量删除美食信息
     * 
     * @param ids 需要删除的美食信息主键
     * @return 结果
     */
    @Override
    public int deleteFoodInfoByIds(Long[] ids)
    {
        return foodInfoMapper.deleteFoodInfoByIds(ids);
    }

    /**
     * 删除美食信息信息
     * 
     * @param id 美食信息主键
     * @return 结果
     */
    @Override
    public int deleteFoodInfoById(Long id)
    {
        return foodInfoMapper.deleteFoodInfoById(id);
    }
}
