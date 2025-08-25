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
import org.dromara.meditation.domain.vo.FavoriteVo;
import org.dromara.meditation.domain.vo.FavoriteDetailVo;
import org.dromara.meditation.domain.bo.FavoriteBo;
import org.dromara.meditation.service.IFavoriteService;
import org.dromara.common.mybatis.core.page.TableDataInfo;

/**
 * 用户收藏
 *
 * @author kdc
 * @date 2025-08-14
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/meditation/favorite")
public class FavoriteController extends BaseController {

    private final IFavoriteService favoriteService;

    /**
     * 查询用户收藏列表
     */
    @GetMapping("/list")
    public TableDataInfo<FavoriteVo> list(FavoriteBo bo, PageQuery pageQuery) {
        return favoriteService.queryPageList(bo, pageQuery);
    }

    /**
     * 查询用户收藏详情列表（包含目标详细信息）
     */
    @GetMapping("/detail/list")
    public TableDataInfo<FavoriteDetailVo> detailList(FavoriteBo bo, PageQuery pageQuery) {
        return favoriteService.queryDetailPageList(bo, pageQuery);
    }

    /**
     * 导出用户收藏列表
     */
    @SaCheckPermission("meditation:favorite:export")
    @Log(title = "用户收藏", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(FavoriteBo bo, HttpServletResponse response) {
        List<FavoriteVo> list = favoriteService.queryList(bo);
        ExcelUtil.exportExcel(list, "用户收藏", FavoriteVo.class, response);
    }

    /**
     * 获取用户收藏详细信息
     *
     * @param id 主键
     */
    @GetMapping("/{id}")
    public R<FavoriteVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(favoriteService.queryById(id));
    }

    /**
     * 新增用户收藏
     */
    @Log(title = "用户收藏", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody FavoriteBo bo) {
        return toAjax(favoriteService.insertByBo(bo));
    }

    /**
     * 修改用户收藏
     */
    @SaCheckPermission("meditation:favorite:edit")
    @Log(title = "用户收藏", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody FavoriteBo bo) {
        return toAjax(favoriteService.updateByBo(bo));
    }

    /**
     * 删除用户收藏
     *
     * @param ids 主键串
     */
    @Log(title = "用户收藏", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(favoriteService.deleteWithValidByIds(List.of(ids), true));
    }

    /**
     * 检查用户是否已收藏指定内容
     *
     * @param trackId 目标ID
     * @param type 目标类型
     * @return 是否已收藏
     */
    @GetMapping("/check")
    public R<Boolean> checkFavorite(@RequestParam Long trackId, @RequestParam String type) {
        Boolean isFavorite = favoriteService.checkFavoriteStatus(trackId, type);
        return R.ok(isFavorite);
    }
    
    /**
     * 检查用户是否已收藏指定内容（新接口）
     *
     * @param targetId 目标ID
     * @param targetType 目标类型
     * @return 是否已收藏
     */
    @GetMapping("/check/status")
    public R<Boolean> checkFavoriteStatus(@RequestParam Long targetId, @RequestParam String targetType) {
        Boolean isFavorite = favoriteService.checkFavoriteStatus(targetId, targetType);
        return R.ok(isFavorite);
    }
}
