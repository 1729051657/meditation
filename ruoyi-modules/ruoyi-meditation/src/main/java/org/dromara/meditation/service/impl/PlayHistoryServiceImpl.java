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
import org.dromara.meditation.domain.bo.PlayHistoryBo;
import org.dromara.meditation.domain.vo.PlayHistoryVo;
import org.dromara.meditation.domain.vo.PlayHistoryDetailVo;
import org.dromara.meditation.domain.PlayHistory;
import org.dromara.meditation.mapper.PlayHistoryMapper;
import org.dromara.meditation.mapper.TrackMapper;
import org.dromara.meditation.mapper.SeriesMapper;
import org.dromara.meditation.mapper.CategoryMapper;
import org.dromara.meditation.service.IPlayHistoryService;
import org.dromara.meditation.domain.vo.TrackVo;
import org.dromara.meditation.domain.vo.SeriesVo;
import org.dromara.meditation.domain.vo.CategoryVo;
import org.dromara.common.satoken.utils.LoginHelper;
import org.springframework.beans.BeanUtils;
import java.util.ArrayList;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Collection;
import java.util.stream.Collectors;
import cn.hutool.core.collection.CollUtil;

/**
 * 音频播放记录Service业务层处理
 *
 * @author kdc
 * @date 2025-08-14
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class PlayHistoryServiceImpl implements IPlayHistoryService {

    private final PlayHistoryMapper baseMapper;
    private final TrackMapper trackMapper;
    private final SeriesMapper seriesMapper;
    private final CategoryMapper categoryMapper;

    /**
     * 查询音频播放记录
     *
     * @param id 主键
     * @return 音频播放记录
     */
    @Override
    public PlayHistoryVo queryById(Long id){
        PlayHistoryVo historyVo = baseMapper.selectVoById(id);
        if (historyVo != null && historyVo.getTrackId() != null) {
            // 查询并设置Track信息
            TrackVo track = trackMapper.selectVoById(historyVo.getTrackId());
            historyVo.setTrack(track);
        }
        return historyVo;
    }

    /**
     * 分页查询音频播放记录列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 音频播放记录分页列表
     */
    @Override
    public TableDataInfo<PlayHistoryVo> queryPageList(PlayHistoryBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<PlayHistory> lqw = buildQueryWrapper(bo);
        Page<PlayHistoryVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);

        // 批量查询并设置Track信息
        fillTrackInfo(result.getRecords());

        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的音频播放记录列表
     *
     * @param bo 查询条件
     * @return 音频播放记录列表
     */
    @Override
    public List<PlayHistoryVo> queryList(PlayHistoryBo bo) {
        LambdaQueryWrapper<PlayHistory> lqw = buildQueryWrapper(bo);
        List<PlayHistoryVo> list = baseMapper.selectVoList(lqw);

        // 批量查询并设置Track信息
        fillTrackInfo(list);

        return list;
    }

    private LambdaQueryWrapper<PlayHistory> buildQueryWrapper(PlayHistoryBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<PlayHistory> lqw = Wrappers.lambdaQuery();
        lqw.orderByAsc(PlayHistory::getId);
        lqw.eq(bo.getUserId() != null, PlayHistory::getUserId, bo.getUserId());
        lqw.eq(bo.getTrackId() != null, PlayHistory::getTrackId, bo.getTrackId());
        lqw.eq(bo.getProgressSec() != null, PlayHistory::getProgressSec, bo.getProgressSec());
        lqw.eq(bo.getLastPlayTime() != null, PlayHistory::getLastPlayTime, bo.getLastPlayTime());
        lqw.eq(bo.getPlayCount() != null, PlayHistory::getPlayCount, bo.getPlayCount());
        lqw.eq(StringUtils.isNotBlank(bo.getIsCompleted()), PlayHistory::getIsCompleted, bo.getIsCompleted());
        return lqw;
    }

    /**
     * 新增音频播放记录
     *
     * @param bo 音频播放记录
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(PlayHistoryBo bo) {
        PlayHistory add = MapstructUtils.convert(bo, PlayHistory.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改音频播放记录
     *
     * @param bo 音频播放记录
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(PlayHistoryBo bo) {
        PlayHistory update = MapstructUtils.convert(bo, PlayHistory.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(PlayHistory entity){
        // 自动设置当前用户ID
        if (entity.getUserId() == null) {
            try {
                Long userId = LoginHelper.getUserId();
                entity.setUserId(userId);
            } catch (Exception e) {
                log.warn("获取当前用户ID失败，可能是未登录状态: {}", e.getMessage());
            }
        }
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除音频播放记录信息
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
     * 分页查询播放历史详情列表
     */
    @Override
    public TableDataInfo<PlayHistoryDetailVo> queryDetailPageList(PlayHistoryBo bo, PageQuery pageQuery) {
        // 设置当前用户ID
        if (bo.getUserId() == null) {
            bo.setUserId(LoginHelper.getUserId());
        }

        LambdaQueryWrapper<PlayHistory> lqw = buildQueryWrapper(bo);
        Page<PlayHistoryVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);

        // 批量查询并设置Track信息
        fillTrackInfo(result.getRecords());

        // 批量查询Series和Category信息
        fillSeriesAndCategoryInfo(result.getRecords());

        // 转换为详情VO
        List<PlayHistoryDetailVo> detailList = new ArrayList<>();
        for (PlayHistoryVo historyVo : result.getRecords()) {
            PlayHistoryDetailVo detailVo = convertToDetailVo(historyVo);
            detailList.add(detailVo);
        }

        Page<PlayHistoryDetailVo> detailPage = new Page<>();
        detailPage.setRecords(detailList);
        detailPage.setTotal(result.getTotal());
        detailPage.setCurrent(result.getCurrent());
        detailPage.setSize(result.getSize());

        return TableDataInfo.build(detailPage);
    }

    /**
     * 查询播放历史详情
     */
    @Override
    public PlayHistoryDetailVo queryDetailById(Long id) {
        PlayHistoryVo historyVo = baseMapper.selectVoById(id);
        if (historyVo == null) {
            return null;
        }

        // 查询并设置Track信息
        if (historyVo.getTrackId() != null) {
            TrackVo track = trackMapper.selectVoById(historyVo.getTrackId());
            historyVo.setTrack(track);

            // 查询Series和Category信息
            if (track != null) {
                if (track.getSeriesId() != null) {
                    SeriesVo series = seriesMapper.selectVoById(track.getSeriesId());
                    // 将series信息临时存储在track的remark字段中（仅用于传递）
                    if (series != null) {
                        track.setRemark(series.getTitle());
                    }
                }
            }
        }

        return convertToDetailVo(historyVo);
    }

    /**
     * 转换为详情VO
     */
    private PlayHistoryDetailVo convertToDetailVo(PlayHistoryVo historyVo) {
        PlayHistoryDetailVo detailVo = new PlayHistoryDetailVo();
        BeanUtils.copyProperties(historyVo, detailVo);

        // 直接使用已经查询好的track信息，避免N+1查询
        TrackVo track = historyVo.getTrack();
        if (track != null) {
            detailVo.setTrackTitle(track.getTitle());
            // Track实体没有subtitle和author字段，这两个字段可以设置为null或空字符串
            detailVo.setTrackSubtitle("");
            detailVo.setTrackAuthor("");
            detailVo.setTrackCover(track.getCover());
            detailVo.setTrackIntro(track.getIntro());
            detailVo.setAudioUrl(track.getAudio()); // 使用audio字段
            detailVo.setTotalDuration(track.getDurationSec());
            detailVo.setSeriesId(track.getSeriesId());
            detailVo.setCategoryId(track.getCategoryId());
            detailVo.setOrderIndex(track.getOrderIndex());
            detailVo.setStatus(track.getStatus());

            // 计算播放进度百分比
            if (track.getDurationSec() != null && track.getDurationSec() > 0) {
                double percent = (historyVo.getProgressSec() * 100.0) / track.getDurationSec();
                detailVo.setProgressPercent(Math.min(100.0, Math.round(percent * 100.0) / 100.0));
            }

            // 使用临时存储在remark中的series名称（如果有）
            if (track.getRemark() != null && !track.getRemark().isEmpty()) {
                detailVo.setSeriesTitle(track.getRemark());
            }
        }

        return detailVo;
    }

    /**
     * 批量填充Track信息
     */
    private void fillTrackInfo(List<PlayHistoryVo> historyList) {
        if (CollUtil.isEmpty(historyList)) {
            return;
        }

        // 收集所有trackId
        List<Long> trackIds = historyList.stream()
            .map(PlayHistoryVo::getTrackId)
            .filter(id -> id != null)
            .distinct()
            .collect(Collectors.toList());

        if (CollUtil.isEmpty(trackIds)) {
            return;
        }

        // 批量查询Track信息
        List<TrackVo> tracks = trackMapper.selectVoByIds(trackIds);

        // 构建Map以便快速查找
        Map<Long, TrackVo> trackMap = tracks.stream()
            .collect(Collectors.toMap(TrackVo::getId, track -> track));

        // 设置Track信息到对应的PlayHistoryVo
        for (PlayHistoryVo historyVo : historyList) {
            if (historyVo.getTrackId() != null) {
                historyVo.setTrack(trackMap.get(historyVo.getTrackId()));
            }
        }
    }

    /**
     * 批量填充Series和Category信息
     */
    private void fillSeriesAndCategoryInfo(List<PlayHistoryVo> historyList) {
        if (CollUtil.isEmpty(historyList)) {
            return;
        }

        // 收集所有seriesId和categoryId
        List<Long> seriesIds = new ArrayList<>();
        List<Long> categoryIds = new ArrayList<>();

        for (PlayHistoryVo historyVo : historyList) {
            TrackVo track = historyVo.getTrack();
            if (track != null) {
                if (track.getSeriesId() != null) {
                    seriesIds.add(track.getSeriesId());
                }
                if (track.getCategoryId() != null) {
                    categoryIds.add(track.getCategoryId());
                }
            }
        }

        // 去重
        seriesIds = seriesIds.stream().distinct().collect(Collectors.toList());
        categoryIds = categoryIds.stream().distinct().collect(Collectors.toList());

        // 批量查询Series信息
        Map<Long, SeriesVo> seriesMap = new HashMap<>();
        if (CollUtil.isNotEmpty(seriesIds)) {
            List<SeriesVo> seriesList = seriesMapper.selectVoByIds(seriesIds);
            seriesMap = seriesList.stream()
                .collect(Collectors.toMap(SeriesVo::getId, series -> series));
        }

        // 批量查询Category信息
        Map<Long, CategoryVo> categoryMap = new HashMap<>();
        if (CollUtil.isNotEmpty(categoryIds)) {
            List<CategoryVo> categoryList = categoryMapper.selectVoByIds(categoryIds);
            categoryMap = categoryList.stream()
                .collect(Collectors.toMap(CategoryVo::getId, category -> category));
        }

        // 将Series和Category信息临时存储在Track的remark字段中
        for (PlayHistoryVo historyVo : historyList) {
            TrackVo track = historyVo.getTrack();
            if (track != null) {
                if (track.getSeriesId() != null) {
                    SeriesVo series = seriesMap.get(track.getSeriesId());
                    if (series != null) {
                        // 临时存储series名称
                        track.setRemark(series.getTitle());
                    }
                }
                // 如果需要category名称，可以使用其他字段或创建临时属性
            }
        }
    }
}
