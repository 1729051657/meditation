package org.dromara.meditation.service.impl;

import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.dromara.meditation.domain.bo.FavoriteBo;
import org.dromara.meditation.domain.vo.FavoriteVo;
import org.dromara.meditation.domain.Favorite;
import org.dromara.meditation.mapper.FavoriteMapper;
import org.dromara.meditation.service.IFavoriteService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 用户收藏Service业务层处理
 *
 * @author kdc
 * @date 2025-08-14
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class FavoriteServiceImpl implements IFavoriteService {

    private final FavoriteMapper baseMapper;

    /**
     * 查询用户收藏
     *
     * @param id 主键
     * @return 用户收藏
     */
    @Override
    public FavoriteVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询用户收藏列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 用户收藏分页列表
     */
    @Override
    public TableDataInfo<FavoriteVo> queryPageList(FavoriteBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<Favorite> lqw = buildQueryWrapper(bo);
        Page<FavoriteVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的用户收藏列表
     *
     * @param bo 查询条件
     * @return 用户收藏列表
     */
    @Override
    public List<FavoriteVo> queryList(FavoriteBo bo) {
        LambdaQueryWrapper<Favorite> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<Favorite> buildQueryWrapper(FavoriteBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<Favorite> lqw = Wrappers.lambdaQuery();
        lqw.orderByAsc(Favorite::getId);
        lqw.eq(bo.getUserId() != null, Favorite::getUserId, bo.getUserId());
        lqw.eq(StringUtils.isNotBlank(bo.getTargetType()), Favorite::getTargetType, bo.getTargetType());
        lqw.eq(bo.getTargetId() != null, Favorite::getTargetId, bo.getTargetId());
        return lqw;
    }

    /**
     * 新增用户收藏
     *
     * @param bo 用户收藏
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(FavoriteBo bo) {
        Favorite add = MapstructUtils.convert(bo, Favorite.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改用户收藏
     *
     * @param bo 用户收藏
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(FavoriteBo bo) {
        Favorite update = MapstructUtils.convert(bo, Favorite.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(Favorite entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除用户收藏信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteByIds(ids) > 0;
    }
}
