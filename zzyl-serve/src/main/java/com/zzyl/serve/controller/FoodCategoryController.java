package com.zzyl.serve.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.zzyl.common.annotation.Log;
import com.zzyl.common.core.controller.BaseController;
import com.zzyl.common.core.domain.AjaxResult;
import com.zzyl.common.enums.BusinessType;
import com.zzyl.serve.domain.FoodCategory;
import com.zzyl.serve.service.IFoodCategoryService;
import com.zzyl.common.utils.poi.ExcelUtil;
import com.zzyl.common.core.page.TableDataInfo;

/**
 * 美食分类Controller
 * 
 * @author admin
 * @date 2026-09-15
 */
@RestController
@RequestMapping("/food/category")
public class FoodCategoryController extends BaseController
{
    @Autowired
    private IFoodCategoryService foodCategoryService;

    /**
     * 查询美食分类列表
     */
    @PreAuthorize("@ss.hasPermi('food:category:list')")
    @GetMapping("/list")
    public TableDataInfo list(FoodCategory foodCategory)
    {
        startPage();
        List<FoodCategory> list = foodCategoryService.selectFoodCategoryList(foodCategory);
        return getDataTable(list);
    }

    /**
     * 导出美食分类列表
     */
    @PreAuthorize("@ss.hasPermi('food:category:export')")
    @Log(title = "美食分类", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, FoodCategory foodCategory)
    {
        List<FoodCategory> list = foodCategoryService.selectFoodCategoryList(foodCategory);
        ExcelUtil<FoodCategory> util = new ExcelUtil<FoodCategory>(FoodCategory.class);
        util.exportExcel(response, list, "美食分类数据");
    }

    /**
     * 获取美食分类详细信息
     */
    @PreAuthorize("@ss.hasPermi('food:category:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(foodCategoryService.selectFoodCategoryById(id));
    }

    /**
     * 新增美食分类
     */
    @PreAuthorize("@ss.hasPermi('food:category:add')")
    @Log(title = "美食分类", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody FoodCategory foodCategory)
    {
        return toAjax(foodCategoryService.insertFoodCategory(foodCategory));
    }

    /**
     * 修改美食分类
     */
    @PreAuthorize("@ss.hasPermi('food:category:edit')")
    @Log(title = "美食分类", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody FoodCategory foodCategory)
    {
        return toAjax(foodCategoryService.updateFoodCategory(foodCategory));
    }

    /**
     * 删除美食分类
     */
    @PreAuthorize("@ss.hasPermi('food:category:remove')")
    @Log(title = "美食分类", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(foodCategoryService.deleteFoodCategoryByIds(ids));
    }
}
