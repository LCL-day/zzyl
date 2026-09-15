package com.zzyl.serve.mapper;

import java.util.List;
import com.zzyl.serve.domain.FoodComment;

/**
 * 美食评论Mapper接口
 * 
 * @author admin
 * @date 2026-09-15
 */
public interface FoodCommentMapper 
{
    /**
     * 查询美食评论
     * 
     * @param id 美食评论主键
     * @return 美食评论
     */
    public FoodComment selectFoodCommentById(Long id);

    /**
     * 查询美食评论列表
     * 
     * @param foodComment 美食评论
     * @return 美食评论集合
     */
    public List<FoodComment> selectFoodCommentList(FoodComment foodComment);

    /**
     * 新增美食评论
     * 
     * @param foodComment 美食评论
     * @return 结果
     */
    public int insertFoodComment(FoodComment foodComment);

    /**
     * 修改美食评论
     * 
     * @param foodComment 美食评论
     * @return 结果
     */
    public int updateFoodComment(FoodComment foodComment);

    /**
     * 删除美食评论
     * 
     * @param id 美食评论主键
     * @return 结果
     */
    public int deleteFoodCommentById(Long id);

    /**
     * 批量删除美食评论
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFoodCommentByIds(Long[] ids);
}
