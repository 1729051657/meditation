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
import org.dromara.meditation.domain.bo.TrackBo;
import org.dromara.meditation.domain.vo.TrackVo;
import org.dromara.meditation.domain.Track;
import org.dromara.meditation.mapper.TrackMapper;
import org.dromara.meditation.service.ITrackService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 冥想单集Service业务层处理
 *
 * @author kdc
 * @date 2025-08-14
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class TrackServiceImpl implements ITrackService {

    private final TrackMapper baseMapper;

    /**
     * 查询冥想单集
     *
     * @param id 主键
     * @return 冥想单集
     */
    @Override
    public TrackVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询冥想单集列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 冥想单集分页列表
     */
    @Override
    public TableDataInfo<TrackVo> queryPageList(TrackBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<Track> lqw = buildQueryWrapper(bo);
        Page<TrackVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的冥想单集列表
     *
     * @param bo 查询条件
     * @return 冥想单集列表
     */
    @Override
    public List<TrackVo> queryList(TrackBo bo) {
        LambdaQueryWrapper<Track> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<Track> buildQueryWrapper(TrackBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<Track> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getSeriesId() != null, Track::getSeriesId, bo.getSeriesId());
        lqw.eq(bo.getCategoryId() != null, Track::getCategoryId, bo.getCategoryId());
        lqw.eq(StringUtils.isNotBlank(bo.getTitle()), Track::getTitle, bo.getTitle());
        lqw.eq(bo.getCover() != null, Track::getCover, bo.getCover());
        lqw.eq(bo.getAudio() != null, Track::getAudio, bo.getAudio());
        lqw.eq(bo.getDurationSec() != null, Track::getDurationSec, bo.getDurationSec());
        lqw.eq(StringUtils.isNotBlank(bo.getIntro()), Track::getIntro, bo.getIntro());
        lqw.eq(bo.getOrderIndex() != null, Track::getOrderIndex, bo.getOrderIndex());
        lqw.eq(StringUtils.isNotBlank(bo.getIsFree()), Track::getIsFree, bo.getIsFree());
        lqw.eq(StringUtils.isNotBlank(bo.getStatus()), Track::getStatus, bo.getStatus());
        lqw.eq(Track::getDelFlag, "0");
        lqw.orderByAsc(Track::getOrderIndex);
        return lqw;
    }

    /**
     * 新增冥想单集
     *
     * @param bo 冥想单集
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(TrackBo bo) {
        Track add = MapstructUtils.convert(bo, Track.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改冥想单集
     *
     * @param bo 冥想单集
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(TrackBo bo) {
        Track update = MapstructUtils.convert(bo, Track.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(Track entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除冥想单集信息
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
