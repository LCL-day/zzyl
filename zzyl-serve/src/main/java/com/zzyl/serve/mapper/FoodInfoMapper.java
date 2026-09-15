package com.zzyl.serve.mapper;

import java.util.List;
import com.zzyl.serve.domain.FoodInfo;

/**
 * 美食信息Mapper接口
 * 
 * @author admin
 * @date 2026-09-15
 */
public interface FoodInfoMapper 
{
    /**
     * 查询美食信息
     * 
     * @param id 美食信息主键
     * @return 美食信息
     */
    public FoodInfo selectFoodInfoById(Long id);

    /**
     * 查询美食信息列表
     * 
     * @param foodInfo 美食信息
     * @return 美食信息集合
     */
    public List<FoodInfo> selectFoodInfoList(FoodInfo foodInfo);

    /**
     * 新增美食信息
     * 
     * @param foodInfo 美食信息
     * @return 结果
     */
    public int insertFoodInfo(FoodInfo foodInfo);

    /**
     * 修改美食信息
     * 
     * @param foodInfo 美食信息
     * @return 结果
     */
    public int updateFoodInfo(FoodInfo foodInfo);

    /**
     * 删除美食信息
     * 
     * @param id 美食信息主键
     * @return 结果
     */
    public int deleteFoodInfoById(Long id);

    /**
     * 批量删除美食信息
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFoodInfoByIds(Long[] ids);
}
