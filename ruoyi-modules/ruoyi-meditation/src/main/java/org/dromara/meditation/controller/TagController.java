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
import org.dromara.meditation.domain.vo.TagVo;
import org.dromara.meditation.domain.bo.TagBo;
import org.dromara.meditation.service.ITagService;
import org.dromara.common.mybatis.core.page.TableDataInfo;

/**
 * 内容标签
 *
 * @author kdc
 * @date 2025-08-14
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/meditation/tag")
public class TagController extends BaseController {

    private final ITagService tagService;

    /**
     * 查询内容标签列表
     */
    @SaCheckPermission("meditation:tag:list")
    @GetMapping("/list")
    public TableDataInfo<TagVo> list(TagBo bo, PageQuery pageQuery) {
        return tagService.queryPageList(bo, pageQuery);
    }

    /**
     * 获取所有可用标签（不分页）
     */
    @GetMapping("/all")
    public R<List<TagVo>> getAllAvailable() {
        return R.ok(tagService.queryAllAvailable());
    }

    /**
     * 导出内容标签列表
     */
    @SaCheckPermission("meditation:tag:export")
    @Log(title = "内容标签", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(TagBo bo, HttpServletResponse response) {
        List<TagVo> list = tagService.queryList(bo);
        ExcelUtil.exportExcel(list, "内容标签", TagVo.class, response);
    }

    /**
     * 获取内容标签详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("meditation:tag:query")
    @GetMapping("/{id}")
    public R<TagVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(tagService.queryById(id));
    }

    /**
     * 新增内容标签
     */
    @SaCheckPermission("meditation:tag:add")
    @Log(title = "内容标签", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody TagBo bo) {
        return toAjax(tagService.insertByBo(bo));
    }

    /**
     * 修改内容标签
     */
    @SaCheckPermission("meditation:tag:edit")
    @Log(title = "内容标签", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody TagBo bo) {
        return toAjax(tagService.updateByBo(bo));
    }

    /**
     * 删除内容标签
     *
     * @param ids 主键串
     */
    @SaCheckPermission("meditation:tag:remove")
    @Log(title = "内容标签", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(tagService.deleteWithValidByIds(List.of(ids), true));
    }
}
