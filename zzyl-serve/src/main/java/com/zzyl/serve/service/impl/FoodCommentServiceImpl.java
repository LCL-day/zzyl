package com.zzyl.serve.service.impl;

import java.util.List;
import com.zzyl.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.zzyl.serve.mapper.FoodCommentMapper;
import com.zzyl.serve.domain.FoodComment;
import com.zzyl.serve.service.IFoodCommentService;

/**
 * 美食评论Service业务层处理
 * 
 * @author admin
 * @date 2026-09-15
 */
@Service
public class FoodCommentServiceImpl implements IFoodCommentService 
{
    @Autowired
    private FoodCommentMapper foodCommentMapper;

    /**
     * 查询美食评论
     * 
     * @param id 美食评论主键
     * @return 美食评论
     */
    @Override
    public FoodComment selectFoodCommentById(Long id)
    {
        return foodCommentMapper.selectFoodCommentById(id);
    }

    /**
     * 查询美食评论列表
     * 
     * @param foodComment 美食评论
     * @return 美食评论
     */
    @Override
    public List<FoodComment> selectFoodCommentList(FoodComment foodComment)
    {
        return foodCommentMapper.selectFoodCommentList(foodComment);
    }

    /**
     * 新增美食评论
     * 
     * @param foodComment 美食评论
     * @return 结果
     */
    @Override
    public int insertFoodComment(FoodComment foodComment)
    {
        foodComment.setCreateTime(DateUtils.getNowDate());
        return foodCommentMapper.insertFoodComment(foodComment);
    }

    /**
     * 修改美食评论
     * 
     * @param foodComment 美食评论
     * @return 结果
     */
    @Override
    public int updateFoodComment(FoodComment foodComment)
    {
        foodComment.setUpdateTime(DateUtils.getNowDate());
        return foodCommentMapper.updateFoodComment(foodComment);
    }

    /**
     * 批量删除美食评论
     * 
     * @param ids 需要删除的美食评论主键
     * @return 结果
     */
    @Override
    public int deleteFoodCommentByIds(Long[] ids)
    {
        return foodCommentMapper.deleteFoodCommentByIds(ids);
    }

    /**
     * 删除美食评论信息
     * 
     * @param id 美食评论主键
     * @return 结果
     */
    @Override
    public int deleteFoodCommentById(Long id)
    {
        return foodCommentMapper.deleteFoodCommentById(id);
    }
}
