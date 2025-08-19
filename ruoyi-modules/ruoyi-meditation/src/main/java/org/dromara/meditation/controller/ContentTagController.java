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
import org.dromara.meditation.domain.vo.ContentTagVo;
import org.dromara.meditation.domain.bo.ContentTagBo;
import org.dromara.meditation.service.IContentTagService;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import lombok.Data;

/**
 * 内容标签关联管理
 *
 * @author kdc
 * @date 2025-08-14
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/meditation/contentTag")
public class ContentTagController extends BaseController {

    private final IContentTagService contentTagService;

    /**
     * 批量更新内容的标签
     */
    @Data
    public static class UpdateContentTagsRequest {
        @NotBlank(message = "内容类型不能为空")
        private String contentType;
        
        @NotNull(message = "内容ID不能为空")
        private Long contentId;
        
        private List<Long> tagIds;
    }

    /**
     * 更新内容标签
     */
    @SaCheckPermission("meditation:contentTag:edit")
    @Log(title = "内容标签关联", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PostMapping("/updateTags")
    public R<Void> updateContentTags(@Validated @RequestBody UpdateContentTagsRequest request) {
        return toAjax(contentTagService.updateContentTags(
            request.getContentType(), 
            request.getContentId(), 
            request.getTagIds()
        ));
    }

    /**
     * 获取内容的标签ID列表
     */
    @GetMapping("/getTagIds")
    public R<List<Long>> getTagIdsByContent(
            @RequestParam String contentType,
            @RequestParam Long contentId) {
        return R.ok(contentTagService.getTagIdsByContent(contentType, contentId));
    }

    /**
     * 查询内容-标签关联列表
     */
    @SaCheckPermission("meditation:contentTag:list")
    @GetMapping("/list")
    public TableDataInfo<ContentTagVo> list(ContentTagBo bo, PageQuery pageQuery) {
        return contentTagService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出内容-标签关联列表
     */
    @SaCheckPermission("meditation:contentTag:export")
    @Log(title = "内容-标签关联", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(ContentTagBo bo, HttpServletResponse response) {
        List<ContentTagVo> list = contentTagService.queryList(bo);
        ExcelUtil.exportExcel(list, "内容-标签关联", ContentTagVo.class, response);
    }

    /**
     * 获取内容-标签关联详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("meditation:contentTag:query")
    @GetMapping("/{id}")
    public R<ContentTagVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(contentTagService.queryById(id));
    }

    /**
     * 新增内容-标签关联
     */
    @SaCheckPermission("meditation:contentTag:add")
    @Log(title = "内容-标签关联", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody ContentTagBo bo) {
        return toAjax(contentTagService.insertByBo(bo));
    }

    /**
     * 修改内容-标签关联
     */
    @SaCheckPermission("meditation:contentTag:edit")
    @Log(title = "内容-标签关联", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody ContentTagBo bo) {
        return toAjax(contentTagService.updateByBo(bo));
    }

    /**
     * 删除内容-标签关联
     *
     * @param ids 主键串
     */
    @SaCheckPermission("meditation:contentTag:remove")
    @Log(title = "内容-标签关联", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(contentTagService.deleteWithValidByIds(List.of(ids), true));
    }
}