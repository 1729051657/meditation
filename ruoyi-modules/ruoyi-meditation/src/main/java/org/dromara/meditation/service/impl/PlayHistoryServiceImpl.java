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
import java.util.Collection;

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
        return baseMapper.selectPlayHistoryWithTrackById(id);
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
        Page<PlayHistoryVo> result = baseMapper.selectPlayHistoryWithTrackPage(pageQuery.build(), lqw);
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
        return baseMapper.selectPlayHistoryWithTrackList(lqw);
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
        // 使用关联查询，避免N+1问题
        Page<PlayHistoryVo> result = baseMapper.selectPlayHistoryWithTrackPage(pageQuery.build(), lqw);
        
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
        // 使用关联查询，避免N+1问题
        PlayHistoryVo historyVo = baseMapper.selectPlayHistoryWithTrackById(id);
        if (historyVo == null) {
            return null;
        }
        return convertToDetailVo(historyVo);
    }

    /**
     * 转换为详情VO
     */
    private PlayHistoryDetailVo convertToDetailVo(PlayHistoryVo historyVo) {
        PlayHistoryDetailVo detailVo = new PlayHistoryDetailVo();
        BeanUtils.copyProperties(historyVo, detailVo);
        
        // 直接使用关联查询得到的track信息，避免N+1查询
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
            
            // 获取系列名称
            if (track.getSeriesId() != null) {
                SeriesVo series = seriesMapper.selectVoById(track.getSeriesId());
                if (series != null) {
                    detailVo.setSeriesTitle(series.getTitle());
                }
            }
            
            // 获取分类名称
            if (track.getCategoryId() != null) {
                CategoryVo category = categoryMapper.selectVoById(track.getCategoryId());
                if (category != null) {
                    detailVo.setCategoryName(category.getName());
                }
            }
        }
        
        return detailVo;
    }
}
