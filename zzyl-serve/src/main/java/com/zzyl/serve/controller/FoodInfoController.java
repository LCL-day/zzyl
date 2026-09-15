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
import com.zzyl.serve.domain.FoodInfo;
import com.zzyl.serve.service.IFoodInfoService;
import com.zzyl.common.utils.poi.ExcelUtil;
import com.zzyl.common.core.page.TableDataInfo;

/**
 * 美食信息Controller
 * 
 * @author admin
 * @date 2026-09-15
 */
@RestController
@RequestMapping("/food/info")
public class FoodInfoController extends BaseController
{
    @Autowired
    private IFoodInfoService foodInfoService;

    /**
     * 查询美食信息列表
     */
    @PreAuthorize("@ss.hasPermi('food:info:list')")
    @GetMapping("/list")
    public TableDataInfo list(FoodInfo foodInfo)
    {
        startPage();
        List<FoodInfo> list = foodInfoService.selectFoodInfoList(foodInfo);
        return getDataTable(list);
    }

    /**
     * 导出美食信息列表
     */
    @PreAuthorize("@ss.hasPermi('food:info:export')")
    @Log(title = "美食信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, FoodInfo foodInfo)
    {
        List<FoodInfo> list = foodInfoService.selectFoodInfoList(foodInfo);
        ExcelUtil<FoodInfo> util = new ExcelUtil<FoodInfo>(FoodInfo.class);
        util.exportExcel(response, list, "美食信息数据");
    }

    /**
     * 获取美食信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('food:info:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(foodInfoService.selectFoodInfoById(id));
    }

    /**
     * 新增美食信息
     */
    @PreAuthorize("@ss.hasPermi('food:info:add')")
    @Log(title = "美食信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody FoodInfo foodInfo)
    {
        return toAjax(foodInfoService.insertFoodInfo(foodInfo));
    }

    /**
     * 修改美食信息
     */
    @PreAuthorize("@ss.hasPermi('food:info:edit')")
    @Log(title = "美食信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody FoodInfo foodInfo)
    {
        return toAjax(foodInfoService.updateFoodInfo(foodInfo));
    }

    /**
     * 删除美食信息
     */
    @PreAuthorize("@ss.hasPermi('food:info:remove')")
    @Log(title = "美食信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(foodInfoService.deleteFoodInfoByIds(ids));
    }
}
