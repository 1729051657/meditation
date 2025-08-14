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
import org.dromara.meditation.domain.vo.SeriesProgressVo;
import org.dromara.meditation.domain.bo.SeriesProgressBo;
import org.dromara.meditation.service.ISeriesProgressService;
import org.dromara.common.mybatis.core.page.TableDataInfo;

/**
 * 系列学习进度
 *
 * @author kdc
 * @date 2025-08-14
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/meditation/seriesProgress")
public class SeriesProgressController extends BaseController {

    private final ISeriesProgressService seriesProgressService;

    /**
     * 查询系列学习进度列表
     */
    @SaCheckPermission("meditation:seriesProgress:list")
    @GetMapping("/list")
    public TableDataInfo<SeriesProgressVo> list(SeriesProgressBo bo, PageQuery pageQuery) {
        return seriesProgressService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出系列学习进度列表
     */
    @SaCheckPermission("meditation:seriesProgress:export")
    @Log(title = "系列学习进度", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(SeriesProgressBo bo, HttpServletResponse response) {
        List<SeriesProgressVo> list = seriesProgressService.queryList(bo);
        ExcelUtil.exportExcel(list, "系列学习进度", SeriesProgressVo.class, response);
    }

    /**
     * 获取系列学习进度详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("meditation:seriesProgress:query")
    @GetMapping("/{id}")
    public R<SeriesProgressVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(seriesProgressService.queryById(id));
    }

    /**
     * 新增系列学习进度
     */
    @SaCheckPermission("meditation:seriesProgress:add")
    @Log(title = "系列学习进度", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody SeriesProgressBo bo) {
        return toAjax(seriesProgressService.insertByBo(bo));
    }

    /**
     * 修改系列学习进度
     */
    @SaCheckPermission("meditation:seriesProgress:edit")
    @Log(title = "系列学习进度", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody SeriesProgressBo bo) {
        return toAjax(seriesProgressService.updateByBo(bo));
    }

    /**
     * 删除系列学习进度
     *
     * @param ids 主键串
     */
    @SaCheckPermission("meditation:seriesProgress:remove")
    @Log(title = "系列学习进度", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(seriesProgressService.deleteWithValidByIds(List.of(ids), true));
    }
}
