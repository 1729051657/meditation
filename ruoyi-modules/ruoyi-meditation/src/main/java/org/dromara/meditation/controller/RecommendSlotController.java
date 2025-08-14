package org.dromara.meditation.controller;

import java.util.List;

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
import org.dromara.meditation.domain.vo.RecommendSlotVo;
import org.dromara.meditation.domain.bo.RecommendSlotBo;
import org.dromara.meditation.service.IRecommendSlotService;
import org.dromara.common.mybatis.core.page.TableDataInfo;

/**
 * 推荐位
 *
 * @author kdc
 * @date 2025-08-14
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/meditation/recommendSlot")
public class RecommendSlotController extends BaseController {

    private final IRecommendSlotService recommendSlotService;

    /**
     * 查询推荐位列表
     */
    @SaCheckPermission("meditation:recommendSlot:list")
    @GetMapping("/list")
    public TableDataInfo<RecommendSlotVo> list(RecommendSlotBo bo, PageQuery pageQuery) {
        return recommendSlotService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出推荐位列表
     */
    @SaCheckPermission("meditation:recommendSlot:export")
    @Log(title = "推荐位", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(RecommendSlotBo bo, HttpServletResponse response) {
        List<RecommendSlotVo> list = recommendSlotService.queryList(bo);
        ExcelUtil.exportExcel(list, "推荐位", RecommendSlotVo.class, response);
    }

    /**
     * 获取推荐位详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("meditation:recommendSlot:query")
    @GetMapping("/{id}")
    public R<RecommendSlotVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(recommendSlotService.queryById(id));
    }

    /**
     * 新增推荐位
     */
    @SaCheckPermission("meditation:recommendSlot:add")
    @Log(title = "推荐位", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody RecommendSlotBo bo) {
        return toAjax(recommendSlotService.insertByBo(bo));
    }

    /**
     * 修改推荐位
     */
    @SaCheckPermission("meditation:recommendSlot:edit")
    @Log(title = "推荐位", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody RecommendSlotBo bo) {
        return toAjax(recommendSlotService.updateByBo(bo));
    }

    /**
     * 删除推荐位
     *
     * @param ids 主键串
     */
    @SaCheckPermission("meditation:recommendSlot:remove")
    @Log(title = "推荐位", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(recommendSlotService.deleteWithValidByIds(List.of(ids), true));
    }
}
