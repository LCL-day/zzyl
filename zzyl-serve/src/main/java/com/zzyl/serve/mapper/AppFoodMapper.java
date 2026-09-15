package com.zzyl.serve.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.zzyl.serve.vo.CommentVo;
import com.zzyl.serve.vo.FoodAppVo;

/**
 * App 端美食Mapper接口
 *
 * 说明：内容表的 create_by 统一存放「发布者账号」，因此凡是判断内容归属
 * （我发布的 / 我收到的评论）都按账号 #{userName} 匹配，与示例数据保持一致；
 * 互动记录表（点赞/收藏/评论）按数字 user_id 匹配。
 *
 * @author admin
 * @date 2026-09-15
 */
public interface AppFoodMapper
{
    /**
     * 查询美食列表（App 首页/搜索），带分类名与当前用户互动状态
     *
     * @param title 标题模糊查询（可空）
     * @param categoryId 分类ID（可空）
     * @param userId 当前用户ID（用于点赞/收藏状态）
     * @return 美食视图集合
     */
    public List<FoodAppVo> selectAppFoodList(@Param("title") String title,
                                             @Param("categoryId") Long categoryId,
                                             @Param("userId") Long userId);

    /**
     * 查询美食详情（App），带分类名与当前用户互动状态
     *
     * @param id 美食ID
     * @param userId 当前用户ID
     * @return 美食视图对象
     */
    public FoodAppVo selectAppFoodById(@Param("id") Long id, @Param("userId") Long userId);

    /**
     * 查询我收藏的美食列表
     *
     * @param userId 当前用户ID
     * @return 美食视图集合
     */
    public List<FoodAppVo> selectMyFavoriteFoodList(@Param("userId") Long userId);

    /**
     * 查询某美食的评论（含评论人昵称、头像与父评论信息）
     *
     * @param foodId 美食ID
     * @return 评论视图集合
     */
    public List<CommentVo> selectCommentVoList(@Param("foodId") Long foodId);

    /**
     * 查询我发表的评论（含美食标题与父评论信息）
     *
     * @param userId 当前用户ID
     * @return 评论视图集合
     */
    public List<CommentVo> selectMyComments(@Param("userId") Long userId);

    /**
     * 查询我收到的评论（他人评论我发布的美食）
     *
     * @param userName 当前用户账号（内容表 create_by 存账号）
     * @return 评论视图集合
     */
    public List<CommentVo> selectReceivedComments(@Param("userName") String userName);

    /**
     * 统计我发布的内容数
     *
     * @param userName 当前用户账号
     * @return 数量
     */
    public int countMyPublished(@Param("userName") String userName);

    /**
     * 统计我的收藏数
     *
     * @param userId 当前用户ID
     * @return 数量
     */
    public int countMyFavorite(@Param("userId") Long userId);

    /**
     * 统计我发表的评论数
     *
     * @param userId 当前用户ID
     * @return 数量
     */
    public int countMyComment(@Param("userId") Long userId);

    /**
     * 统计我收到的评论数（他人评论我发布的美食）
     *
     * @param userName 当前用户账号
     * @return 数量
     */
    public int countReceivedComment(@Param("userName") String userName);
}
