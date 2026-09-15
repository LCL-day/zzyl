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
import com.zzyl.serve.domain.FoodFavorite;
import com.zzyl.serve.service.IFoodFavoriteService;
import com.zzyl.common.core.page.TableDataInfo;

/**
 * 美食收藏记录Controller
 * 
 * @author admin
 * @date 2026-09-15
 */
@RestController
@RequestMapping("/food/favorite")
public class FoodFavoriteController extends BaseController
{
    @Autowired
    private IFoodFavoriteService foodFavoriteService;

    /**
     * 查询美食收藏记录列表
     */
    @PreAuthorize("@ss.hasPermi('food:favorite:list')")
    @GetMapping("/list")
    public TableDataInfo list(FoodFavorite foodFavorite)
    {
        startPage();
        List<FoodFavorite> list = foodFavoriteService.selectFoodFavoriteList(foodFavorite);
        return getDataTable(list);
    }

    /**
     * 获取美食收藏记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('food:favorite:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(foodFavoriteService.selectFoodFavoriteById(id));
    }

    /**
     * 删除美食收藏记录
     */
    @PreAuthorize("@ss.hasPermi('food:favorite:remove')")
    @Log(title = "美食收藏记录", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(foodFavoriteService.deleteFoodFavoriteByIds(ids));
    }
}
