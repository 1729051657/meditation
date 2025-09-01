package org.dromara.meditation.controller;

import java.util.List;

import lombok.RequiredArgsConstructor;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.*;
import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.annotation.SaIgnore;
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
import org.dromara.meditation.domain.vo.PlayHistoryVo;
import org.dromara.meditation.domain.vo.PlayHistoryDetailVo;
import org.dromara.meditation.domain.bo.PlayHistoryBo;
import org.dromara.meditation.service.IPlayHistoryService;
import org.dromara.common.mybatis.core.page.TableDataInfo;

/**
 * 音频播放记录
 *
 * @author kdc
 * @date 2025-08-14
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/meditation/playHistory")
public class PlayHistoryController extends BaseController {

    private final IPlayHistoryService playHistoryService;

    /**
     * 查询音频播放记录列表
     */
    @GetMapping("/list")
    public TableDataInfo<PlayHistoryVo> list(PlayHistoryBo bo, PageQuery pageQuery) {
        return playHistoryService.queryPageList(bo, pageQuery);
    }

    /**
     * 查询播放历史详情列表（包含单集详细信息）
     */
    @GetMapping("/detail/list")
    public TableDataInfo<PlayHistoryDetailVo> detailList(PlayHistoryBo bo, PageQuery pageQuery) {
        return playHistoryService.queryDetailPageList(bo, pageQuery);
    }

    /**
     * 导出音频播放记录列表
     */
    @SaCheckPermission("meditation:playHistory:export")
    @Log(title = "音频播放记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(PlayHistoryBo bo, HttpServletResponse response) {
        List<PlayHistoryVo> list = playHistoryService.queryList(bo);
        ExcelUtil.exportExcel(list, "音频播放记录", PlayHistoryVo.class, response);
    }

    /**
     * 获取音频播放记录详细信息
     *
     * @param id 主键
     */
    @GetMapping("/{id}")
    public R<PlayHistoryVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(playHistoryService.queryById(id));
    }

    /**
     * 新增音频播放记录
     */
    @Log(title = "音频播放记录", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody PlayHistoryBo bo) {
        return toAjax(playHistoryService.insertByBo(bo));
    }

    /**
     * 新增或更新音频播放记录
     * 如果userId+trackId已存在，则更新记录（包括最后播放时间）
     * 如果不存在，则新增记录
     */
    @Log(title = "音频播放记录", businessType = BusinessType.INSERT)
    @PostMapping("/upsert")
    public R<Void> upsert(@Validated(AddGroup.class) @RequestBody PlayHistoryBo bo) {
        return toAjax(playHistoryService.insertOrUpdateByBo(bo));
    }

    /**
     * 修改音频播放记录
     */
    @SaCheckPermission("meditation:playHistory:edit")
    @Log(title = "音频播放记录", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody PlayHistoryBo bo) {
        return toAjax(playHistoryService.updateByBo(bo));
    }

    /**
     * 删除音频播放记录
     *
     * @param ids 主键串
     */
    @SaCheckPermission("meditation:playHistory:remove")
    @Log(title = "音频播放记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(playHistoryService.deleteWithValidByIds(List.of(ids), true));
    }
}
