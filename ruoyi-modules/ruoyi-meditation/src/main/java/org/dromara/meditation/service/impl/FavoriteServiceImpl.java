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
import org.dromara.meditation.domain.vo.FavoriteDetailVo;
import org.dromara.meditation.domain.Favorite;
import org.dromara.meditation.mapper.FavoriteMapper;
import org.dromara.meditation.mapper.TrackMapper;
import org.dromara.meditation.mapper.SeriesMapper;
import org.dromara.meditation.mapper.ArticleMapper;
import org.dromara.meditation.mapper.CategoryMapper;
import org.dromara.meditation.service.IFavoriteService;
import org.dromara.meditation.domain.vo.TrackVo;
import org.dromara.meditation.domain.vo.SeriesVo;
import org.dromara.meditation.domain.vo.ArticleVo;
import org.dromara.meditation.domain.vo.CategoryVo;
import org.dromara.common.satoken.utils.LoginHelper;
import org.springframework.beans.BeanUtils;
import java.util.ArrayList;

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
    private final TrackMapper trackMapper;
    private final SeriesMapper seriesMapper;
    private final ArticleMapper articleMapper;
    private final CategoryMapper categoryMapper;

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

    /**
     * 分页查询用户收藏详情列表
     */
    @Override
    public TableDataInfo<FavoriteDetailVo> queryDetailPageList(FavoriteBo bo, PageQuery pageQuery) {
        // 设置当前用户ID
        if (bo.getUserId() == null) {
            bo.setUserId(LoginHelper.getUserId());
        }
        
        LambdaQueryWrapper<Favorite> lqw = buildQueryWrapper(bo);
        Page<FavoriteVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        
        // 转换为详情VO
        List<FavoriteDetailVo> detailList = new ArrayList<>();
        for (FavoriteVo favoriteVo : result.getRecords()) {
            FavoriteDetailVo detailVo = convertToDetailVo(favoriteVo);
            detailList.add(detailVo);
        }
        
        Page<FavoriteDetailVo> detailPage = new Page<>();
        detailPage.setRecords(detailList);
        detailPage.setTotal(result.getTotal());
        detailPage.setCurrent(result.getCurrent());
        detailPage.setSize(result.getSize());
        
        return TableDataInfo.build(detailPage);
    }

    /**
     * 查询用户收藏详情
     */
    @Override
    public FavoriteDetailVo queryDetailById(Long id) {
        FavoriteVo favoriteVo = baseMapper.selectVoById(id);
        if (favoriteVo == null) {
            return null;
        }
        return convertToDetailVo(favoriteVo);
    }

    /**
     * 转换为详情VO
     */
    private FavoriteDetailVo convertToDetailVo(FavoriteVo favoriteVo) {
        FavoriteDetailVo detailVo = new FavoriteDetailVo();
        BeanUtils.copyProperties(favoriteVo, detailVo);
        
        // 根据目标类型获取详细信息
        if ("track".equals(favoriteVo.getTargetType())) {
            TrackVo track = trackMapper.selectVoById(favoriteVo.getTargetId());
            if (track != null) {
                detailVo.setTargetTitle(track.getTitle());
                detailVo.setTargetSubtitle(track.getSubtitle());
                detailVo.setTargetAuthor(track.getAuthor());
                detailVo.setTargetCover(track.getCover());
                detailVo.setTargetIntro(track.getIntro());
                detailVo.setTargetDuration(track.getDurationSec());
                detailVo.setAudioUrl(track.getAudio()); // 使用audio字段
                detailVo.setPlayCount(track.getPlayCount());
                detailVo.setCategoryId(track.getCategoryId());
                detailVo.setStatus(track.getStatus());
                
                // 获取分类名称
                if (track.getCategoryId() != null) {
                    CategoryVo category = categoryMapper.selectVoById(track.getCategoryId());
                    if (category != null) {
                        detailVo.setCategoryName(category.getName());
                    }
                }
            }
        } else if ("series".equals(favoriteVo.getTargetType())) {
            SeriesVo series = seriesMapper.selectVoById(favoriteVo.getTargetId());
            if (series != null) {
                detailVo.setTargetTitle(series.getTitle());
                detailVo.setTargetSubtitle(series.getSubtitle());
                detailVo.setTargetAuthor(series.getAuthor());
                detailVo.setTargetCover(series.getCover());
                detailVo.setTargetBanner(series.getBanner());
                detailVo.setTargetIntro(series.getIntro());
                detailVo.setTargetDuration(series.getTotalDuration());
                detailVo.setEpisodeCount(series.getEpisodeCount());
                detailVo.setPlayCount(series.getPlayCount());
                detailVo.setCategoryId(series.getCategoryId());
                detailVo.setStatus(series.getStatus());
                
                // 获取分类名称
                if (series.getCategoryId() != null) {
                    CategoryVo category = categoryMapper.selectVoById(series.getCategoryId());
                    if (category != null) {
                        detailVo.setCategoryName(category.getName());
                    }
                }
            }
        } else if ("article".equals(favoriteVo.getTargetType())) {
            ArticleVo article = articleMapper.selectVoById(favoriteVo.getTargetId());
            if (article != null) {
                detailVo.setTargetTitle(article.getTitle());
                detailVo.setTargetAuthor(article.getAuthor());
                detailVo.setTargetCover(article.getCover());
                detailVo.setTargetSummary(article.getSummary());
                detailVo.setViewCount(article.getViewCount());
                detailVo.setStatus(article.getStatus());
            }
        }
        
        return detailVo;
    }

    /**
     * 检查用户是否已收藏指定内容
     */
    @Override
    public Boolean checkFavoriteStatus(Long targetId, String targetType) {
        // 获取当前登录用户ID
        Long userId = LoginHelper.getUserId();
        if (userId == null) {
            return false;
        }
        
        // 构建查询条件
        LambdaQueryWrapper<Favorite> lqw = Wrappers.lambdaQuery();
        lqw.eq(Favorite::getUserId, userId);
        lqw.eq(Favorite::getTargetId, targetId);
        lqw.eq(Favorite::getTargetType, targetType);
        
        // 查询是否存在收藏记录
        long count = baseMapper.selectCount(lqw);
        return count > 0;
    }
}
