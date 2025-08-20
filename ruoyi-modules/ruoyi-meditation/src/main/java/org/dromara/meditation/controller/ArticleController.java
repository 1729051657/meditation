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
import org.dromara.meditation.domain.vo.ArticleVo;
import org.dromara.meditation.domain.bo.ArticleBo;
import org.dromara.meditation.service.IArticleService;
import org.dromara.common.mybatis.core.page.TableDataInfo;

/**
 * 冥想知识文章
 *
 * @author kdc
 * @date 2025-08-14
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/meditation/article")
public class ArticleController extends BaseController {

    private final IArticleService articleService;

    /**
     * 查询冥想知识文章列表
     */
    @SaIgnore
    @GetMapping("/list")
    public TableDataInfo<ArticleVo> list(ArticleBo bo, PageQuery pageQuery) {
        return articleService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出冥想知识文章列表
     */
    @SaCheckPermission("meditation:article:export")
    @Log(title = "冥想知识文章", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(ArticleBo bo, HttpServletResponse response) {
        List<ArticleVo> list = articleService.queryList(bo);
        ExcelUtil.exportExcel(list, "冥想知识文章", ArticleVo.class, response);
    }

    /**
     * 获取冥想知识文章详细信息
     *
     * @param id 主键
     */
    @SaIgnore
    @GetMapping("/{id}")
    public R<ArticleVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(articleService.queryById(id));
    }

    /**
     * 新增冥想知识文章
     */
    @SaCheckPermission("meditation:article:add")
    @Log(title = "冥想知识文章", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody ArticleBo bo) {
        return toAjax(articleService.insertByBo(bo));
    }

    /**
     * 修改冥想知识文章
     */
    @SaCheckPermission("meditation:article:edit")
    @Log(title = "冥想知识文章", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody ArticleBo bo) {
        return toAjax(articleService.updateByBo(bo));
    }

    /**
     * 删除冥想知识文章
     *
     * @param ids 主键串
     */
    @SaCheckPermission("meditation:article:remove")
    @Log(title = "冥想知识文章", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(articleService.deleteWithValidByIds(List.of(ids), true));
    }
}
