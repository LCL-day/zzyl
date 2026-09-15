package com.zzyl.serve.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.zzyl.common.annotation.Log;
import com.zzyl.common.config.RuoYiConfig;
import com.zzyl.common.core.controller.BaseController;
import com.zzyl.common.core.domain.AjaxResult;
import com.zzyl.common.core.domain.entity.SysUser;
import com.zzyl.common.core.page.TableDataInfo;
import com.zzyl.common.enums.BusinessType;
import com.zzyl.common.utils.SecurityUtils;
import com.zzyl.serve.domain.FoodCategory;
import com.zzyl.serve.domain.FoodComment;
import com.zzyl.serve.domain.FoodFavorite;
import com.zzyl.serve.domain.FoodInfo;
import com.zzyl.serve.domain.FoodLike;
import com.zzyl.serve.dto.CommentDto;
import com.zzyl.serve.dto.PublishFoodDto;
import com.zzyl.serve.mapper.AppFoodMapper;
import com.zzyl.serve.mapper.FoodCategoryMapper;
import com.zzyl.serve.mapper.FoodCommentMapper;
import com.zzyl.serve.mapper.FoodFavoriteMapper;
import com.zzyl.serve.mapper.FoodInfoMapper;
import com.zzyl.serve.mapper.FoodLikeMapper;
import com.zzyl.serve.util.CoverUtils;
import com.zzyl.serve.vo.CommentVo;
import com.zzyl.serve.vo.FoodAppVo;

/**
 * 美食分享平台 App 端接口（首页/搜索、发布、收藏、点赞、评论、我的）
 *
 * @author admin
 * @date 2026-09-15
 */
@RestController
@RequestMapping("/food/app")
public class AppFoodController extends BaseController
{
    @Autowired
    private AppFoodMapper appFoodMapper;

    @Autowired
    private FoodInfoMapper foodInfoMapper;

    @Autowired
    private FoodCategoryMapper foodCategoryMapper;

    @Autowired
    private FoodLikeMapper foodLikeMapper;

    @Autowired
    private FoodFavoriteMapper foodFavoriteMapper;

    @Autowired
    private FoodCommentMapper foodCommentMapper;

    // ------------------------------------------------------------------ 首页

    /**
     * 首页美食列表（支持标题/简介搜索、分类筛选、分页）
     */
    @GetMapping("/list")
    public TableDataInfo list(FoodInfo query)
    {
        startPage();
        List<FoodAppVo> list = appFoodMapper.selectAppFoodList(query.getTitle(), query.getCategoryId(), getUserId());
        return getDataTable(list);
    }

    /**
     * 分类列表（首页快筛用）
     */
    @GetMapping("/categories")
    public AjaxResult categories()
    {
        return success(foodCategoryMapper.selectFoodCategoryList(null));
    }

    /**
     * 美食详情
     */
    @GetMapping("/detail/{id}")
    public AjaxResult detail(@PathVariable("id") Long id)
    {
        FoodAppVo vo = appFoodMapper.selectAppFoodById(id, getUserId());
        if (vo == null)
        {
            return error("内容不存在或已下架");
        }
        return success(vo);
    }

    // ------------------------------------------------------------------ 点赞

    /**
     * 点赞 / 取消点赞
     */
    @Log(title = "美食点赞", businessType = BusinessType.INSERT)
    @PostMapping("/like/{foodId}")
    public AjaxResult toggleLike(@PathVariable("foodId") Long foodId)
    {
        Long userId = getUserId();
        if (foodInfoMapper.selectFoodInfoById(foodId) == null)
        {
            return error("内容不存在");
        }
        FoodLike query = new FoodLike();
        query.setFoodId(foodId);
        query.setUserId(userId);
        List<FoodLike> exists = foodLikeMapper.selectFoodLikeList(query);

        FoodInfo update = new FoodInfo();
        update.setId(foodId);
        boolean liked;
        if (exists != null && !exists.isEmpty())
        {
            Long[] ids = new Long[exists.size()];
            for (int i = 0; i < exists.size(); i++)
            {
                ids[i] = exists.get(i).getId();
            }
            foodLikeMapper.deleteFoodLikeByIds(ids);
            update.setLikeCount(Math.max(0, currentCount(foodId, "like_count") - exists.size()));
            liked = false;
        }
        else
        {
            FoodLike like = new FoodLike();
            like.setFoodId(foodId);
            like.setUserId(userId);
            like.setCreateTime(new Date());
            foodLikeMapper.insertFoodLike(like);
            update.setLikeCount(currentCount(foodId, "like_count") + 1);
            liked = true;
        }
        foodInfoMapper.updateFoodInfo(update);
        return success(buildToggle(liked, currentCount(foodId, "like_count")));
    }

    // ------------------------------------------------------------------ 收藏

    /**
     * 收藏 / 取消收藏
     */
    @Log(title = "美食收藏", businessType = BusinessType.INSERT)
    @PostMapping("/favorite/{foodId}")
    public AjaxResult toggleFavorite(@PathVariable("foodId") Long foodId)
    {
        Long userId = getUserId();
        if (foodInfoMapper.selectFoodInfoById(foodId) == null)
        {
            return error("内容不存在");
        }
        FoodFavorite query = new FoodFavorite();
        query.setFoodId(foodId);
        query.setUserId(userId);
        List<FoodFavorite> exists = foodFavoriteMapper.selectFoodFavoriteList(query);

        FoodInfo update = new FoodInfo();
        update.setId(foodId);
        boolean favorited;
        if (exists != null && !exists.isEmpty())
        {
            Long[] ids = new Long[exists.size()];
            for (int i = 0; i < exists.size(); i++)
            {
                ids[i] = exists.get(i).getId();
            }
            foodFavoriteMapper.deleteFoodFavoriteByIds(ids);
            update.setFavoriteCount(Math.max(0, currentCount(foodId, "favorite_count") - exists.size()));
            favorited = false;
        }
        else
        {
            FoodFavorite favorite = new FoodFavorite();
            favorite.setFoodId(foodId);
            favorite.setUserId(userId);
            favorite.setCreateTime(new Date());
            foodFavoriteMapper.insertFoodFavorite(favorite);
            update.setFavoriteCount(currentCount(foodId, "favorite_count") + 1);
            favorited = true;
        }
        foodInfoMapper.updateFoodInfo(update);
        return success(buildToggle(favorited, currentCount(foodId, "favorite_count")));
    }

    /**
     * 我的收藏列表
     */
    @GetMapping("/favorites")
    public TableDataInfo favorites()
    {
        startPage();
        return getDataTable(appFoodMapper.selectMyFavoriteFoodList(getUserId()));
    }

    /**
     * 取消收藏（收藏页直接移除）
     */
    @Log(title = "美食收藏", businessType = BusinessType.DELETE)
    @DeleteMapping("/favorite/{foodId}")
    public AjaxResult removeFavorite(@PathVariable("foodId") Long foodId)
    {
        Long userId = getUserId();
        FoodFavorite query = new FoodFavorite();
        query.setFoodId(foodId);
        query.setUserId(userId);
        List<FoodFavorite> exists = foodFavoriteMapper.selectFoodFavoriteList(query);
        if (exists != null && !exists.isEmpty())
        {
            Long[] ids = new Long[exists.size()];
            for (int i = 0; i < exists.size(); i++)
            {
                ids[i] = exists.get(i).getId();
            }
            foodFavoriteMapper.deleteFoodFavoriteByIds(ids);
            FoodInfo update = new FoodInfo();
            update.setId(foodId);
            update.setFavoriteCount(Math.max(0, currentCount(foodId, "favorite_count") - exists.size()));
            foodInfoMapper.updateFoodInfo(update);
        }
        return success();
    }

    // ------------------------------------------------------------------ 发布

    /**
     * 发布美食
     */
    @Log(title = "发布美食", businessType = BusinessType.INSERT)
    @PostMapping("/publish")
    public AjaxResult publish(@RequestBody PublishFoodDto dto)
    {
        if (dto.getTitle() == null || dto.getTitle().trim().isEmpty())
        {
            return error("请填写标题");
        }
        if (dto.getTitle().trim().length() > 100)
        {
            return error("标题最多 100 个字");
        }
        if (dto.getContent() == null || dto.getContent().trim().isEmpty())
        {
            return error("请填写内容");
        }
        FoodInfo food = new FoodInfo();
        food.setTitle(dto.getTitle().trim());
        food.setCategoryId(dto.getCategoryId());
        food.setDescription(dto.getDescription() == null ? "" : dto.getDescription().trim());
        food.setImage(dto.getImage() == null ? "" : dto.getImage());
        food.setContent(dto.getContent());
        food.setLikeCount(0);
        food.setFavoriteCount(0);
        food.setCommentCount(0);
        food.setStatus("0");
        // 未上传封面时，用「美食名称 + 分类名称」自动生成封面
        food.setImage(resolveCover(dto.getImage(), dto.getTitle().trim(), dto.getCategoryId()));
        // 与示例数据保持一致，create_by 记录发布者账号；同时回写 remark 便于排查
        food.setCreateBy(getUsername());
        food.setCreateTime(new Date());
        foodInfoMapper.insertFoodInfo(food);
        return success(food.getId());
    }

    /**
     * 我发布的
     */
    @GetMapping("/mine")
    public TableDataInfo mine(FoodInfo query)
    {
        // 内容归属按账号匹配，与发布时写入的 create_by 口径一致
        query.setCreateBy(getUsername());
        startPage();
        return getDataTable(foodInfoMapper.selectFoodInfoList(query));
    }

    /**
     * 获取我发布的某条内容详情（编辑回显用）
     */
    @GetMapping("/mine/{foodId}")
    public AjaxResult getMine(@PathVariable("foodId") Long foodId)
    {
        FoodInfo food = foodInfoMapper.selectFoodInfoById(foodId);
        if (food == null)
        {
            return error("内容不存在");
        }
        if (!isOwner(food.getCreateBy()))
        {
            return error("只能查看自己发布的内容");
        }
        return success(food);
    }

    /**
     * 修改我发布的内容
     */
    @Log(title = "修改我的发布", businessType = BusinessType.UPDATE)
    @PutMapping("/mine/{foodId}")
    public AjaxResult updateMine(@PathVariable("foodId") Long foodId, @RequestBody PublishFoodDto dto)
    {
        FoodInfo exist = foodInfoMapper.selectFoodInfoById(foodId);
        if (exist == null)
        {
            return error("内容不存在");
        }
        if (!isOwner(exist.getCreateBy()))
        {
            return error("只能修改自己发布的内容");
        }
        if (dto.getTitle() == null || dto.getTitle().trim().isEmpty())
        {
            return error("请填写标题");
        }
        if (dto.getTitle().trim().length() > 100)
        {
            return error("标题最多 100 个字");
        }
        if (dto.getContent() == null || dto.getContent().trim().isEmpty())
        {
            return error("请填写内容");
        }
        if (dto.getCategoryId() != null && foodCategoryMapper.selectFoodCategoryById(dto.getCategoryId()) == null)
        {
            return error("所选分类不存在");
        }

        // 只更新允许修改的字段，互动计数、状态、创建信息保持不变
        FoodInfo update = new FoodInfo();
        update.setId(foodId);
        update.setTitle(dto.getTitle().trim());
        if (dto.getCategoryId() != null)
        {
            update.setCategoryId(dto.getCategoryId());
        }
        update.setDescription(dto.getDescription() == null ? "" : dto.getDescription().trim());
        update.setContent(dto.getContent());
        // 未重新上传封面时保留原封面（含系统自动生成的封面）
        if (dto.getImage() != null && !dto.getImage().trim().isEmpty())
        {
            update.setImage(dto.getImage().trim());
        }
        update.setUpdateBy(getUsername());
        update.setUpdateTime(new Date());
        foodInfoMapper.updateFoodInfo(update);
        return success();
    }

    /**
     * 删除我发布的内容
     */
    @Log(title = "删除我的发布", businessType = BusinessType.DELETE)
    @DeleteMapping("/mine/{foodId}")
    public AjaxResult removeMine(@PathVariable("foodId") Long foodId)
    {
        FoodInfo food = foodInfoMapper.selectFoodInfoById(foodId);
        if (food == null)
        {
            return error("内容不存在");
        }
        if (!isOwner(food.getCreateBy()))
        {
            return error("只能删除自己发布的内容");
        }
        // 连带清理该内容的评论（含回复）、点赞与收藏，避免留下脏数据
        removeFoodInteractions(foodId);
        foodInfoMapper.deleteFoodInfoByIds(new Long[] { foodId });
        return success();
    }

    // ------------------------------------------------------------------ 评论

    /**
     * 某美食的评论列表
     */
    @GetMapping("/comments/{foodId}")
    public TableDataInfo comments(@PathVariable("foodId") Long foodId)
    {
        startPage();
        return getDataTable(appFoodMapper.selectCommentVoList(foodId));
    }

    /**
     * 发表评论 / 回复评论
     */
    @Log(title = "发表评论", businessType = BusinessType.INSERT)
    @PostMapping("/comment")
    public AjaxResult comment(@RequestBody CommentDto dto)
    {
        if (dto.getFoodId() == null)
        {
            return error("缺少美食ID");
        }
        if (dto.getContent() == null || dto.getContent().trim().isEmpty())
        {
            return error("请填写评论内容");
        }
        if (dto.getContent().trim().length() > 500)
        {
            return error("评论最多 500 个字");
        }
        if (foodInfoMapper.selectFoodInfoById(dto.getFoodId()) == null)
        {
            return error("内容不存在");
        }
        if (dto.getParentId() != null && dto.getParentId() > 0 && foodCommentMapper.selectFoodCommentById(dto.getParentId()) == null)
        {
            return error("被回复的评论不存在");
        }
        Long userId = getUserId();
        FoodComment comment = new FoodComment();
        comment.setFoodId(dto.getFoodId());
        comment.setUserId(userId);
        comment.setContent(dto.getContent().trim());
        comment.setParentId(dto.getParentId() == null ? 0L : dto.getParentId());
        comment.setLikeCount(0);
        comment.setStatus("0");
        comment.setCreateBy(String.valueOf(userId));
        comment.setCreateTime(new Date());
        foodCommentMapper.insertFoodComment(comment);

        FoodInfo update = new FoodInfo();
        update.setId(dto.getFoodId());
        update.setCommentCount(currentCount(dto.getFoodId(), "comment_count") + 1);
        foodInfoMapper.updateFoodInfo(update);
        return success();
    }

    /**
     * 删除我的评论
     */
    @Log(title = "删除我的评论", businessType = BusinessType.DELETE)
    @DeleteMapping("/comment/{id}")
    public AjaxResult removeComment(@PathVariable("id") Long id)
    {
        FoodComment comment = foodCommentMapper.selectFoodCommentById(id);
        if (comment == null)
        {
            return error("评论不存在");
        }
        if (!isOwner(comment.getCreateBy()) && !String.valueOf(getUserId()).equals(String.valueOf(comment.getUserId())))
        {
            return error("只能删除自己的评论");
        }
        // 连同其下的回复一起删除
        int removed = collectWithReplies(comment);
        FoodInfo update = new FoodInfo();
        update.setId(comment.getFoodId());
        update.setCommentCount(Math.max(0, currentCount(comment.getFoodId(), "comment_count") - removed));
        foodInfoMapper.updateFoodInfo(update);
        return success();
    }

    /**
     * 我的评论 / 收到的评论
     *
     * @param type mine=我发表的评论，received=我收到的评论
     */
    @GetMapping("/profile/comments")
    public TableDataInfo profileComments(@RequestParam(value = "type", defaultValue = "mine") String type)
    {
        Long userId = getUserId();
        startPage();
        List<CommentVo> list;
        if ("received".equals(type))
        {
            list = appFoodMapper.selectReceivedComments(getUsername());
        }
        else
        {
            list = appFoodMapper.selectMyComments(userId);
        }
        return getDataTable(list);
    }

    // ------------------------------------------------------------------ 我的

    /**
     * 我的信息（昵称、头像、发布/收藏/评论/收到评论数量）
     */
    @GetMapping("/profile")
    public AjaxResult profile()
    {
        SysUser user = SecurityUtils.getLoginUser().getUser();
        Long userId = user.getUserId();

        Map<String, Object> data = new HashMap<>();
        data.put("userId", userId);
        data.put("userName", user.getUserName());
        data.put("nickName", user.getNickName());
        data.put("avatar", user.getAvatar());
        data.put("phonenumber", user.getPhonenumber());
        data.put("publishedCount", appFoodMapper.countMyPublished(getUsername()));
        data.put("favoriteCount", appFoodMapper.countMyFavorite(userId));
        data.put("commentCount", appFoodMapper.countMyComment(userId));
        data.put("receivedCount", appFoodMapper.countReceivedComment(getUsername()));
        return success(data);
    }

    // ------------------------------------------------------------------ 内部方法

    /**
     * 解析封面：
     * 用户上传了封面则直接使用；否则按「美食名称 + 分类名称」自动生成一张封面图。
     * 生成失败时返回空串，前端会退回占位样式，不影响发布流程。
     */
    private String resolveCover(String image, String title, Long categoryId)
    {
        if (image != null && !image.trim().isEmpty())
        {
            return image.trim();
        }
        String categoryName = "";
        if (categoryId != null)
        {
            FoodCategory category = foodCategoryMapper.selectFoodCategoryById(categoryId);
            if (category != null)
            {
                categoryName = category.getName();
            }
        }
        String generated = CoverUtils.generate(RuoYiConfig.getProfile(), title, categoryName);
        return generated == null ? "" : generated;
    }

    /**
     * 判断内容/评论是否属于当前登录用户
     * create_by 历史数据既可能是账号也可能是用户ID，这里两种都兼容
     */
    private boolean isOwner(String createBy)
    {
        if (createBy == null || createBy.trim().isEmpty())
        {
            return false;
        }
        String value = createBy.trim();
        return value.equals(getUsername()) || value.equals(String.valueOf(getUserId()));
    }

    /**
     * 清理某条内容的全部互动数据：评论（含其下回复）、点赞、收藏
     */
    private void removeFoodInteractions(Long foodId)
    {
        FoodComment commentQuery = new FoodComment();
        commentQuery.setFoodId(foodId);
        List<FoodComment> comments = foodCommentMapper.selectFoodCommentList(commentQuery);
        if (comments != null && !comments.isEmpty())
        {
            Long[] commentIds = new Long[comments.size()];
            for (int i = 0; i < comments.size(); i++)
            {
                commentIds[i] = comments.get(i).getId();
            }
            foodCommentMapper.deleteFoodCommentByIds(commentIds);
        }

        FoodLike likeQuery = new FoodLike();
        likeQuery.setFoodId(foodId);
        List<FoodLike> likes = foodLikeMapper.selectFoodLikeList(likeQuery);
        if (likes != null && !likes.isEmpty())
        {
            Long[] likeIds = new Long[likes.size()];
            for (int i = 0; i < likes.size(); i++)
            {
                likeIds[i] = likes.get(i).getId();
            }
            foodLikeMapper.deleteFoodLikeByIds(likeIds);
        }

        FoodFavorite favoriteQuery = new FoodFavorite();
        favoriteQuery.setFoodId(foodId);
        List<FoodFavorite> favorites = foodFavoriteMapper.selectFoodFavoriteList(favoriteQuery);
        if (favorites != null && !favorites.isEmpty())
        {
            Long[] favoriteIds = new Long[favorites.size()];
            for (int i = 0; i < favorites.size(); i++)
            {
                favoriteIds[i] = favorites.get(i).getId();
            }
            foodFavoriteMapper.deleteFoodFavoriteByIds(favoriteIds);
        }
    }

    /**
     * 删除指定评论及其全部回复，返回实际删除条数
     */
    private int collectWithReplies(FoodComment target)
    {
        FoodComment query = new FoodComment();
        query.setFoodId(target.getFoodId());
        List<FoodComment> all = foodCommentMapper.selectFoodCommentList(query);
        Set<Long> remove = new HashSet<>();
        remove.add(target.getId());
        boolean changed = true;
        while (changed)
        {
            changed = false;
            for (FoodComment item : all)
            {
                if (item.getParentId() != null && remove.contains(item.getParentId()) && remove.add(item.getId()))
                {
                    changed = true;
                }
            }
        }
        foodCommentMapper.deleteFoodCommentByIds(remove.toArray(new Long[0]));
        return remove.size();
    }

    /**
     * 查询某美食当前的互动计数
     */
    private int currentCount(Long foodId, String column)
    {
        FoodInfo food = foodInfoMapper.selectFoodInfoById(foodId);
        if (food == null)
        {
            return 0;
        }
        if ("like_count".equals(column))
        {
            return food.getLikeCount() == null ? 0 : food.getLikeCount();
        }
        if ("favorite_count".equals(column))
        {
            return food.getFavoriteCount() == null ? 0 : food.getFavoriteCount();
        }
        return food.getCommentCount() == null ? 0 : food.getCommentCount();
    }

    /**
     * 组装互动切换结果
     */
    private Map<String, Object> buildToggle(boolean active, int count)
    {
        Map<String, Object> data = new HashMap<>();
        data.put("active", active);
        data.put("count", count);
        return data;
    }
}
