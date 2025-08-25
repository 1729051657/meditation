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
import org.dromara.meditation.domain.vo.TrackVo;
import org.dromara.meditation.domain.bo.TrackBo;
import org.dromara.meditation.service.ITrackService;
import org.dromara.common.mybatis.core.page.TableDataInfo;

/**
 * 冥想单集
 *
 * @author kdc
 * @date 2025-08-14
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/meditation/track")
public class TrackController extends BaseController {

    private final ITrackService trackService;

    /**
     * 查询冥想单集列表
     */
    @SaIgnore
    @GetMapping("/list")
    public TableDataInfo<TrackVo> list(TrackBo bo, PageQuery pageQuery) {
        return trackService.queryPageList(bo, pageQuery);
    }

    /**
     * 获取所有冥想单集（不分页）
     */
    @SaIgnore
    @GetMapping("/all")
    public R<List<TrackVo>> getAllTracks(TrackBo bo) {
        return R.ok(trackService.queryList(bo));
    }

    /**
     * 导出冥想单集列表
     */
    @SaCheckPermission("meditation:track:export")
    @Log(title = "冥想单集", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(TrackBo bo, HttpServletResponse response) {
        List<TrackVo> list = trackService.queryList(bo);
        ExcelUtil.exportExcel(list, "冥想单集", TrackVo.class, response);
    }

    /**
     * 获取冥想单集详细信息
     *
     * @param id 主键
     */
    @SaIgnore
    @GetMapping("/{id}")
    public R<TrackVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(trackService.queryById(id));
    }

    /**
     * 新增冥想单集
     */
    @SaCheckPermission("meditation:track:add")
    @Log(title = "冥想单集", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody TrackBo bo) {
        return toAjax(trackService.insertByBo(bo));
    }

    /**
     * 修改冥想单集
     */
    @SaCheckPermission("meditation:track:edit")
    @Log(title = "冥想单集", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody TrackBo bo) {
        return toAjax(trackService.updateByBo(bo));
    }

    /**
     * 删除冥想单集
     *
     * @param ids 主键串
     */
    @SaCheckPermission("meditation:track:remove")
    @Log(title = "冥想单集", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(trackService.deleteWithValidByIds(List.of(ids), true));
    }
}
