package org.dromara.meditation.controller;

import java.util.List;

import cn.dev33.satoken.annotation.SaIgnore;
import lombok.RequiredArgsConstructor;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.*;
import cn.dev33.satoken.annotation.SaCheckPermission;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;
import org.dromara.common.idempotent.annotation.RepeatSubmit;
import org.dromara.common.log.annotation.Log;
import org.dromara.common.web.core.BaseController;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.core.domain.R;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import org.dromara.common.log.enums.BusinessType;
import org.dromara.common.excel.utils.ExcelUtil;
import org.dromara.meditation.domain.vo.CategoryVo;
import org.dromara.meditation.domain.bo.CategoryBo;
import org.dromara.meditation.service.ICategoryService;

/**
 * 冥想分类
 *
 * @author kdc
 * @date 2025-08-14
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/meditation/category")
public class CategoryController extends BaseController {

    private final ICategoryService categoryService;

    /**
     * 查询冥想分类列表
     */
    @SaIgnore
    @GetMapping("/list")
    public R<List<CategoryVo>> list(CategoryBo bo) {
        List<CategoryVo> list = categoryService.queryList(bo);
        return R.ok(list);
    }

    /**
     * 导出冥想分类列表
     */
    @SaCheckPermission("meditation:category:export")
    @Log(title = "冥想分类", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(CategoryBo bo, HttpServletResponse response) {
        List<CategoryVo> list = categoryService.queryList(bo);
        ExcelUtil.exportExcel(list, "冥想分类", CategoryVo.class, response);
    }

    /**
     * 获取冥想分类详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("meditation:category:query")
    @GetMapping("/{id}")
    public R<CategoryVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(categoryService.queryById(id));
    }

    /**
     * 新增冥想分类
     */
    @SaCheckPermission("meditation:category:add")
    @Log(title = "冥想分类", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody CategoryBo bo) {
        return toAjax(categoryService.insertByBo(bo));
    }

    /**
     * 修改冥想分类
     */
    @SaCheckPermission("meditation:category:edit")
    @Log(title = "冥想分类", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody CategoryBo bo) {
        return toAjax(categoryService.updateByBo(bo));
    }

    /**
     * 删除冥想分类
     *
     * @param ids 主键串
     */
    @SaCheckPermission("meditation:category:remove")
    @Log(title = "冥想分类", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(categoryService.deleteWithValidByIds(List.of(ids), true));
    }
}
