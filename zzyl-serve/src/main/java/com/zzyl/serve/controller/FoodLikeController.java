package com.zzyl.serve.controller;

import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.zzyl.common.annotation.Log;
import com.zzyl.common.core.controller.BaseController;
import com.zzyl.common.core.domain.AjaxResult;
import com.zzyl.common.enums.BusinessType;
import com.zzyl.serve.domain.FoodLike;
import com.zzyl.serve.service.IFoodLikeService;
import com.zzyl.common.core.page.TableDataInfo;

/**
 * 美食点赞记录Controller
 * 
 * @author admin
 * @date 2026-09-15
 */
@RestController
@RequestMapping("/food/like")
public class FoodLikeController extends BaseController
{
    @Autowired
    private IFoodLikeService foodLikeService;

    /**
     * 查询美食点赞记录列表
     */
    @PreAuthorize("@ss.hasPermi('food:like:list')")
    @GetMapping("/list")
    public TableDataInfo list(FoodLike foodLike)
    {
        startPage();
        List<FoodLike> list = foodLikeService.selectFoodLikeList(foodLike);
        return getDataTable(list);
    }

    /**
     * 获取美食点赞记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('food:like:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(foodLikeService.selectFoodLikeById(id));
    }

    /**
     * 删除美食点赞记录
     */
    @PreAuthorize("@ss.hasPermi('food:like:remove')")
    @Log(title = "美食点赞记录", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(foodLikeService.deleteFoodLikeByIds(ids));
    }
}
