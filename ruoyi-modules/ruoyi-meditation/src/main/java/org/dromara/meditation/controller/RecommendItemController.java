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
import org.dromara.meditation.domain.vo.RecommendItemVo;
import org.dromara.meditation.domain.bo.RecommendItemBo;
import org.dromara.meditation.service.IRecommendItemService;
import org.dromara.common.mybatis.core.page.TableDataInfo;

/**
 * 推荐位内容
 *
 * @author kdc
 * @date 2025-08-14
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/meditation/recommendItem")
public class RecommendItemController extends BaseController {

    private final IRecommendItemService recommendItemService;

    /**
     * 查询推荐位内容列表
     */
    @SaIgnore
    @GetMapping("/list")
    public TableDataInfo<RecommendItemVo> list(RecommendItemBo bo, PageQuery pageQuery) {
        return recommendItemService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出推荐位内容列表
     */
    @SaCheckPermission("meditation:recommendItem:export")
    @Log(title = "推荐位内容", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(RecommendItemBo bo, HttpServletResponse response) {
        List<RecommendItemVo> list = recommendItemService.queryList(bo);
        ExcelUtil.exportExcel(list, "推荐位内容", RecommendItemVo.class, response);
    }

    /**
     * 获取推荐位内容详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("meditation:recommendItem:query")
    @GetMapping("/{id}")
    public R<RecommendItemVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(recommendItemService.queryById(id));
    }

    /**
     * 新增推荐位内容
     */
    @SaCheckPermission("meditation:recommendItem:add")
    @Log(title = "推荐位内容", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody RecommendItemBo bo) {
        return toAjax(recommendItemService.insertByBo(bo));
    }

    /**
     * 修改推荐位内容
     */
    @SaCheckPermission("meditation:recommendItem:edit")
    @Log(title = "推荐位内容", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody RecommendItemBo bo) {
        return toAjax(recommendItemService.updateByBo(bo));
    }

    /**
     * 删除推荐位内容
     *
     * @param ids 主键串
     */
    @SaCheckPermission("meditation:recommendItem:remove")
    @Log(title = "推荐位内容", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(recommendItemService.deleteWithValidByIds(List.of(ids), true));
    }
}
