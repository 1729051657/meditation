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
import org.dromara.meditation.domain.bo.RecommendItemBo;
import org.dromara.meditation.domain.vo.RecommendItemVo;
import org.dromara.meditation.domain.vo.SeriesVo;
import org.dromara.meditation.domain.vo.ArticleVo;
import org.dromara.meditation.domain.vo.TrackVo;
import org.dromara.meditation.domain.RecommendItem;
import org.dromara.meditation.mapper.RecommendItemMapper;
import org.dromara.meditation.mapper.SeriesMapper;
import org.dromara.meditation.mapper.ArticleMapper;
import org.dromara.meditation.mapper.TrackMapper;
import org.dromara.meditation.service.IRecommendItemService;

import java.util.List;
import java.util.Map;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.HashMap;
import java.util.ArrayList;

/**
 * 推荐位内容Service业务层处理
 *
 * @author kdc
 * @date 2025-08-14
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class RecommendItemServiceImpl implements IRecommendItemService {

    private final RecommendItemMapper baseMapper;
    private final SeriesMapper seriesMapper;
    private final ArticleMapper articleMapper;
    private final TrackMapper trackMapper;

    /**
     * 查询推荐位内容
     *
     * @param id 主键
     * @return 推荐位内容
     */
    @Override
    public RecommendItemVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询推荐位内容列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 推荐位内容分页列表
     */
    @Override
    public TableDataInfo<RecommendItemVo> queryPageList(RecommendItemBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<RecommendItem> lqw = buildQueryWrapper(bo);
        Page<RecommendItemVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        
        // 批量加载内容详情，避免N+1问题
        if (result.getRecords() != null && !result.getRecords().isEmpty()) {
            loadContentDetails(result.getRecords());
        }
        
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的推荐位内容列表
     *
     * @param bo 查询条件
     * @return 推荐位内容列表
     */
    @Override
    public List<RecommendItemVo> queryList(RecommendItemBo bo) {
        LambdaQueryWrapper<RecommendItem> lqw = buildQueryWrapper(bo);
        List<RecommendItemVo> list = baseMapper.selectVoList(lqw);
        
        // 批量加载内容详情
        if (list != null && !list.isEmpty()) {
            loadContentDetails(list);
        }
        
        return list;
    }

    private LambdaQueryWrapper<RecommendItem> buildQueryWrapper(RecommendItemBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<RecommendItem> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getSlotId() != null, RecommendItem::getSlotId, bo.getSlotId());
        lqw.eq(StringUtils.isNotBlank(bo.getContentType()), RecommendItem::getContentType, bo.getContentType());
        lqw.eq(bo.getContentId() != null, RecommendItem::getContentId, bo.getContentId());
        lqw.eq(bo.getOrderNum() != null, RecommendItem::getOrderNum, bo.getOrderNum());
        lqw.eq(bo.getStartTime() != null, RecommendItem::getStartTime, bo.getStartTime());
        lqw.eq(bo.getEndTime() != null, RecommendItem::getEndTime, bo.getEndTime());
        lqw.eq(StringUtils.isNotBlank(bo.getStatus()), RecommendItem::getStatus, bo.getStatus());
        lqw.eq(RecommendItem::getDelFlag, "0");
        lqw.orderByDesc(RecommendItem::getOrderNum);
        return lqw;
    }

    /**
     * 新增推荐位内容
     *
     * @param bo 推荐位内容
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(RecommendItemBo bo) {
        RecommendItem add = MapstructUtils.convert(bo, RecommendItem.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改推荐位内容
     *
     * @param bo 推荐位内容
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(RecommendItemBo bo) {
        RecommendItem update = MapstructUtils.convert(bo, RecommendItem.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(RecommendItem entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除推荐位内容信息
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
     * 批量加载内容详情，避免N+1查询问题
     * 使用批量查询，按内容类型分组一次性加载
     */
    private void loadContentDetails(List<RecommendItemVo> items) {
        if (items == null || items.isEmpty()) {
            return;
        }
        
        // 按内容类型分组
        List<Long> seriesIds = new ArrayList<>();
        List<Long> articleIds = new ArrayList<>();
        List<Long> trackIds = new ArrayList<>();
        
        for (RecommendItemVo item : items) {
            if (item.getContentId() == null) {
                continue;
            }
            
            switch (item.getContentType()) {
                case "series":
                    seriesIds.add(item.getContentId());
                    break;
                case "article":
                    articleIds.add(item.getContentId());
                    break;
                case "track":
                    trackIds.add(item.getContentId());
                    break;
            }
        }
        
        // 批量查询系列内容
        Map<Long, SeriesVo> seriesMap = new HashMap<>();
        if (!seriesIds.isEmpty()) {
            List<SeriesVo> seriesList = seriesMapper.selectVoBatchIds(seriesIds);
            if (seriesList != null) {
                seriesMap = seriesList.stream()
                    .collect(Collectors.toMap(SeriesVo::getId, s -> s, (v1, v2) -> v1));
            }
        }
        
        // 批量查询文章内容
        Map<Long, ArticleVo> articleMap = new HashMap<>();
        if (!articleIds.isEmpty()) {
            List<ArticleVo> articleList = articleMapper.selectVoBatchIds(articleIds);
            if (articleList != null) {
                articleMap = articleList.stream()
                    .collect(Collectors.toMap(ArticleVo::getId, a -> a, (v1, v2) -> v1));
            }
        }
        
        // 批量查询音频内容
        Map<Long, TrackVo> trackMap = new HashMap<>();
        if (!trackIds.isEmpty()) {
            List<TrackVo> trackList = trackMapper.selectVoBatchIds(trackIds);
            if (trackList != null) {
                trackMap = trackList.stream()
                    .collect(Collectors.toMap(TrackVo::getId, t -> t, (v1, v2) -> v1));
            }
        }
        
        // 设置内容详情到对应的推荐项
        for (RecommendItemVo item : items) {
            if (item.getContentId() == null) {
                continue;
            }
            
            switch (item.getContentType()) {
                case "series":
                    item.setSeriesContent(seriesMap.get(item.getContentId()));
                    break;
                case "article":
                    item.setArticleContent(articleMap.get(item.getContentId()));
                    break;
                case "track":
                    item.setTrackContent(trackMap.get(item.getContentId()));
                    break;
            }
        }
    }
}
