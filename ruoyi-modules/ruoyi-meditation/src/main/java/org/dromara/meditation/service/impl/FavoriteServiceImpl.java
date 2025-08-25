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
import java.util.stream.Collectors;
import java.util.Objects;

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
        // 设置当前用户ID
        if (bo.getUserId() == null) {
            bo.setUserId(LoginHelper.getUserId());
        }

        // 检查是否已经收藏
        if (checkFavoriteExists(bo.getUserId(), bo.getTargetId(), bo.getTargetType())) {
            log.warn("用户 {} 已经收藏了 {} 类型的 {}", bo.getUserId(), bo.getTargetType(), bo.getTargetId());
            return false;
        }

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
        // 验证必填字段
        if (entity.getUserId() == null) {
            throw new IllegalArgumentException("用户ID不能为空");
        }
        if (entity.getTargetId() == null) {
            throw new IllegalArgumentException("目标ID不能为空");
        }
        if (StringUtils.isBlank(entity.getTargetType())) {
            throw new IllegalArgumentException("目标类型不能为空");
        }

        // 验证目标类型
        if (!"track".equals(entity.getTargetType())
            && !"series".equals(entity.getTargetType())
            && !"article".equals(entity.getTargetType())) {
            throw new IllegalArgumentException("不支持的收藏类型: " + entity.getTargetType());
        }
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

        // 转换为详情VO - 批量处理避免N+1问题
        List<FavoriteDetailVo> detailList = convertToDetailVoBatch(result.getRecords());

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
     * 批量转换为详情VO - 避免N+1查询问题
     */
    private List<FavoriteDetailVo> convertToDetailVoBatch(List<FavoriteVo> favoriteVoList) {
        if (favoriteVoList == null || favoriteVoList.isEmpty()) {
            return new ArrayList<>();
        }

        // 按类型分组收藏记录
        Map<String, List<FavoriteVo>> typeGroupMap = favoriteVoList.stream()
            .filter(fav -> fav != null && fav.getTargetType() != null)
            .collect(Collectors.groupingBy(FavoriteVo::getTargetType));

        // 批量查询各类型的目标数据
        Map<Long, TrackVo> trackMap = new java.util.HashMap<>();
        Map<Long, SeriesVo> seriesMap = new java.util.HashMap<>();
        Map<Long, ArticleVo> articleMap = new java.util.HashMap<>();
        Map<Long, CategoryVo> categoryMap = new java.util.HashMap<>();

        // 批量查询track数据
        List<FavoriteVo> trackFavorites = typeGroupMap.get("track");
        if (trackFavorites != null && !trackFavorites.isEmpty()) {
            List<Long> trackIds = trackFavorites.stream()
                .map(FavoriteVo::getTargetId)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
            if (!trackIds.isEmpty()) {
                List<TrackVo> tracks = trackMapper.selectVoByIds(trackIds);
                trackMap = tracks.stream()
                    .filter(Objects::nonNull)
                    .collect(Collectors.toMap(TrackVo::getId, t -> t, (v1, v2) -> v1));

                // 收集所有分类ID
                List<Long> categoryIds = tracks.stream()
                    .map(TrackVo::getCategoryId)
                    .filter(Objects::nonNull)
                    .distinct()
                    .collect(Collectors.toList());
                if (!categoryIds.isEmpty()) {
                    categoryMap.putAll(loadCategories(categoryIds));
                }
            }
        }

        // 批量查询series数据
        List<FavoriteVo> seriesFavorites = typeGroupMap.get("series");
        if (seriesFavorites != null && !seriesFavorites.isEmpty()) {
            List<Long> seriesIds = seriesFavorites.stream()
                .map(FavoriteVo::getTargetId)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
            if (!seriesIds.isEmpty()) {
                List<SeriesVo> seriesList = seriesMapper.selectVoByIds(seriesIds);
                seriesMap = seriesList.stream()
                    .filter(Objects::nonNull)
                    .collect(Collectors.toMap(SeriesVo::getId, s -> s, (v1, v2) -> v1));

                // 收集所有分类ID
                List<Long> categoryIds = seriesList.stream()
                    .map(SeriesVo::getCategoryId)
                    .filter(Objects::nonNull)
                    .distinct()
                    .collect(Collectors.toList());
                if (!categoryIds.isEmpty()) {
                    categoryMap.putAll(loadCategories(categoryIds));
                }
            }
        }

        // 批量查询article数据
        List<FavoriteVo> articleFavorites = typeGroupMap.get("article");
        if (articleFavorites != null && !articleFavorites.isEmpty()) {
            List<Long> articleIds = articleFavorites.stream()
                .map(FavoriteVo::getTargetId)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
            if (!articleIds.isEmpty()) {
                List<ArticleVo> articles = articleMapper.selectVoByIds(articleIds);
                articleMap = articles.stream()
                    .filter(Objects::nonNull)
                    .collect(Collectors.toMap(ArticleVo::getId, a -> a, (v1, v2) -> v1));
            }
        }

        // 组装详情数据
        List<FavoriteDetailVo> detailList = new ArrayList<>();
        for (FavoriteVo favoriteVo : favoriteVoList) {
            FavoriteDetailVo detailVo = new FavoriteDetailVo();
            BeanUtils.copyProperties(favoriteVo, detailVo);

            if (favoriteVo.getTargetId() != null && favoriteVo.getTargetType() != null) {
                switch (favoriteVo.getTargetType()) {
                    case "track":
                        TrackVo track = trackMap.get(favoriteVo.getTargetId());
                        if (track != null) {
                            detailVo.setTrack(track);
                            // 设置分类
                            if (track.getCategoryId() != null) {
                                detailVo.setCategory(categoryMap.get(track.getCategoryId()));
                            }
                        }
                        break;
                    case "series":
                        SeriesVo series = seriesMap.get(favoriteVo.getTargetId());
                        if (series != null) {
                            detailVo.setSeries(series);
                            // 设置分类
                            if (series.getCategoryId() != null) {
                                detailVo.setCategory(categoryMap.get(series.getCategoryId()));
                            }
                        }
                        break;
                    case "article":
                        ArticleVo article = articleMap.get(favoriteVo.getTargetId());
                        if (article != null) {
                            detailVo.setArticle(article);
                        }
                        break;
                    default:
                        log.warn("Unknown target type: {}", favoriteVo.getTargetType());
                }
            }

            detailList.add(detailVo);
        }

        return detailList;
    }

    /**
     * 批量加载分类信息
     */
    private Map<Long, CategoryVo> loadCategories(List<Long> categoryIds) {
        if (categoryIds == null || categoryIds.isEmpty()) {
            return new java.util.HashMap<>();
        }
        List<CategoryVo> categories = categoryMapper.selectVoByIds(categoryIds);
        return categories.stream()
            .filter(Objects::nonNull)
            .collect(Collectors.toMap(CategoryVo::getId, c -> c, (v1, v2) -> v1));
    }

    /**
     * 转换为详情VO - 单个查询
     */
    private FavoriteDetailVo convertToDetailVo(FavoriteVo favoriteVo) {
        if (favoriteVo == null) {
            return null;
        }
        List<FavoriteDetailVo> detailList = convertToDetailVoBatch(java.util.Collections.singletonList(favoriteVo));
        return detailList.isEmpty() ? new FavoriteDetailVo() : detailList.get(0);
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

        return checkFavoriteExists(userId, targetId, targetType);
    }

    /**
     * 检查收藏是否存在
     */
    private Boolean checkFavoriteExists(Long userId, Long targetId, String targetType) {
        if (userId == null || targetId == null || StringUtils.isBlank(targetType)) {
            return false;
        }

        // 构建查询条件
        LambdaQueryWrapper<Favorite> lqw = Wrappers.lambdaQuery();
        lqw.eq(Favorite::getUserId, userId);
        lqw.eq(Favorite::getTargetId, targetId);
        lqw.eq(Favorite::getTargetType, targetType);
        lqw.last("LIMIT 1"); // 优化查询，只需要知道是否存在

        // 查询是否存在收藏记录
        return baseMapper.selectCount(lqw) > 0;
    }
}