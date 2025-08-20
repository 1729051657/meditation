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
import org.dromara.meditation.domain.vo.SeriesVo;
import org.dromara.meditation.domain.bo.SeriesBo;
import org.dromara.meditation.service.ISeriesService;
import org.dromara.common.mybatis.core.page.TableDataInfo;

/**
 * 冥想系列
 *
 * @author kdc
 * @date 2025-08-14
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/meditation/series")
public class SeriesController extends BaseController {

    private final ISeriesService seriesService;

    /**
     * 查询冥想系列列表
     */
    @SaIgnore
    @GetMapping("/list")
    public TableDataInfo<SeriesVo> list(SeriesBo bo, PageQuery pageQuery) {
        return seriesService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出冥想系列列表
     */
    @SaCheckPermission("meditation:series:export")
    @Log(title = "冥想系列", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(SeriesBo bo, HttpServletResponse response) {
        List<SeriesVo> list = seriesService.queryList(bo);
        ExcelUtil.exportExcel(list, "冥想系列", SeriesVo.class, response);
    }

    /**
     * 获取冥想系列详细信息
     *
     * @param id 主键
     */
    @SaIgnore
    @GetMapping("/{id}")
    public R<SeriesVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(seriesService.queryById(id));
    }

    /**
     * 新增冥想系列
     */
    @SaCheckPermission("meditation:series:add")
    @Log(title = "冥想系列", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody SeriesBo bo) {
        return toAjax(seriesService.insertByBo(bo));
    }

    /**
     * 修改冥想系列
     */
    @SaCheckPermission("meditation:series:edit")
    @Log(title = "冥想系列", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody SeriesBo bo) {
        return toAjax(seriesService.updateByBo(bo));
    }

    /**
     * 删除冥想系列
     *
     * @param ids 主键串
     */
    @SaCheckPermission("meditation:series:remove")
    @Log(title = "冥想系列", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(seriesService.deleteWithValidByIds(List.of(ids), true));
    }
}
