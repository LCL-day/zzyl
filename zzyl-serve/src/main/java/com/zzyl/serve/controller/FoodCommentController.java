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
import com.zzyl.serve.domain.FoodComment;
import com.zzyl.serve.service.IFoodCommentService;
import com.zzyl.common.utils.poi.ExcelUtil;
import com.zzyl.common.core.page.TableDataInfo;

/**
 * 美食评论Controller
 * 
 * @author admin
 * @date 2026-09-15
 */
@RestController
@RequestMapping("/food/comment")
public class FoodCommentController extends BaseController
{
    @Autowired
    private IFoodCommentService foodCommentService;

    /**
     * 查询美食评论列表
     */
    @PreAuthorize("@ss.hasPermi('food:comment:list')")
    @GetMapping("/list")
    public TableDataInfo list(FoodComment foodComment)
    {
        startPage();
        List<FoodComment> list = foodCommentService.selectFoodCommentList(foodComment);
        return getDataTable(list);
    }

    /**
     * 导出美食评论列表
     */
    @PreAuthorize("@ss.hasPermi('food:comment:export')")
    @Log(title = "美食评论", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, FoodComment foodComment)
    {
        List<FoodComment> list = foodCommentService.selectFoodCommentList(foodComment);
        ExcelUtil<FoodComment> util = new ExcelUtil<FoodComment>(FoodComment.class);
        util.exportExcel(response, list, "美食评论数据");
    }

    /**
     * 获取美食评论详细信息
     */
    @PreAuthorize("@ss.hasPermi('food:comment:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(foodCommentService.selectFoodCommentById(id));
    }

    /**
     * 新增美食评论
     */
    @PreAuthorize("@ss.hasPermi('food:comment:add')")
    @Log(title = "美食评论", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody FoodComment foodComment)
    {
        return toAjax(foodCommentService.insertFoodComment(foodComment));
    }

    /**
     * 修改美食评论
     */
    @PreAuthorize("@ss.hasPermi('food:comment:edit')")
    @Log(title = "美食评论", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody FoodComment foodComment)
    {
        return toAjax(foodCommentService.updateFoodComment(foodComment));
    }

    /**
     * 删除美食评论
     */
    @PreAuthorize("@ss.hasPermi('food:comment:remove')")
    @Log(title = "美食评论", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(foodCommentService.deleteFoodCommentByIds(ids));
    }
}
