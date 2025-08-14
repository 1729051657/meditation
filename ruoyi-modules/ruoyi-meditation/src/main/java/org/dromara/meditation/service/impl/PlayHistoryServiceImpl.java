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
import org.dromara.meditation.domain.PlayHistory;
import org.dromara.meditation.mapper.PlayHistoryMapper;
import org.dromara.meditation.service.IPlayHistoryService;

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

    /**
     * 查询音频播放记录
     *
     * @param id 主键
     * @return 音频播放记录
     */
    @Override
    public PlayHistoryVo queryById(Long id){
        return baseMapper.selectVoById(id);
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
        return baseMapper.selectVoList(lqw);
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
}
