package org.dromara.meditation.service;

import org.dromara.meditation.domain.vo.FavoriteVo;
import org.dromara.meditation.domain.vo.FavoriteDetailVo;
import org.dromara.meditation.domain.bo.FavoriteBo;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 用户收藏Service接口
 *
 * @author kdc
 * @date 2025-08-14
 */
public interface IFavoriteService {

    /**
     * 查询用户收藏
     *
     * @param id 主键
     * @return 用户收藏
     */
    FavoriteVo queryById(Long id);

    /**
     * 分页查询用户收藏列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 用户收藏分页列表
     */
    TableDataInfo<FavoriteVo> queryPageList(FavoriteBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的用户收藏列表
     *
     * @param bo 查询条件
     * @return 用户收藏列表
     */
    List<FavoriteVo> queryList(FavoriteBo bo);

    /**
     * 新增用户收藏
     *
     * @param bo 用户收藏
     * @return 是否新增成功
     */
    Boolean insertByBo(FavoriteBo bo);

    /**
     * 修改用户收藏
     *
     * @param bo 用户收藏
     * @return 是否修改成功
     */
    Boolean updateByBo(FavoriteBo bo);

    /**
     * 校验并批量删除用户收藏信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    /**
     * 分页查询用户收藏详情列表（包含目标详细信息）
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 用户收藏详情分页列表
     */
    TableDataInfo<FavoriteDetailVo> queryDetailPageList(FavoriteBo bo, PageQuery pageQuery);

    /**
     * 查询用户收藏详情（包含目标详细信息）
     *
     * @param id 主键
     * @return 用户收藏详情
     */
    FavoriteDetailVo queryDetailById(Long id);
}
