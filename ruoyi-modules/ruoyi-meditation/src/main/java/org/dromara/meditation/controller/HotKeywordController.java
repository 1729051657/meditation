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
import org.dromara.meditation.domain.vo.HotKeywordVo;
import org.dromara.meditation.domain.bo.HotKeywordBo;
import org.dromara.meditation.service.IHotKeywordService;
import org.dromara.common.mybatis.core.page.TableDataInfo;

/**
 * 热搜关键词
 *
 * @author kdc
 * @date 2025-08-14
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/meditation/hotKeyword")
public class HotKeywordController extends BaseController {

    private final IHotKeywordService hotKeywordService;

    /**
     * 查询热搜关键词列表
     */
    @SaCheckPermission("meditation:hotKeyword:list")
    @GetMapping("/list")
    public TableDataInfo<HotKeywordVo> list(HotKeywordBo bo, PageQuery pageQuery) {
        return hotKeywordService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出热搜关键词列表
     */
    @SaCheckPermission("meditation:hotKeyword:export")
    @Log(title = "热搜关键词", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HotKeywordBo bo, HttpServletResponse response) {
        List<HotKeywordVo> list = hotKeywordService.queryList(bo);
        ExcelUtil.exportExcel(list, "热搜关键词", HotKeywordVo.class, response);
    }

    /**
     * 获取热搜关键词详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("meditation:hotKeyword:query")
    @GetMapping("/{id}")
    public R<HotKeywordVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(hotKeywordService.queryById(id));
    }

    /**
     * 新增热搜关键词
     */
    @SaCheckPermission("meditation:hotKeyword:add")
    @Log(title = "热搜关键词", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody HotKeywordBo bo) {
        return toAjax(hotKeywordService.insertByBo(bo));
    }

    /**
     * 修改热搜关键词
     */
    @SaCheckPermission("meditation:hotKeyword:edit")
    @Log(title = "热搜关键词", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody HotKeywordBo bo) {
        return toAjax(hotKeywordService.updateByBo(bo));
    }

    /**
     * 删除热搜关键词
     *
     * @param ids 主键串
     */
    @SaCheckPermission("meditation:hotKeyword:remove")
    @Log(title = "热搜关键词", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(hotKeywordService.deleteWithValidByIds(List.of(ids), true));
    }
}
