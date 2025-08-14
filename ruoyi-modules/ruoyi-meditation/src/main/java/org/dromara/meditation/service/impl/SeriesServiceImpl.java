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
import org.dromara.meditation.domain.bo.SeriesBo;
import org.dromara.meditation.domain.vo.SeriesVo;
import org.dromara.meditation.domain.Series;
import org.dromara.meditation.mapper.SeriesMapper;
import org.dromara.meditation.service.ISeriesService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 冥想系列Service业务层处理
 *
 * @author kdc
 * @date 2025-08-14
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class SeriesServiceImpl implements ISeriesService {

    private final SeriesMapper baseMapper;

    /**
     * 查询冥想系列
     *
     * @param id 主键
     * @return 冥想系列
     */
    @Override
    public SeriesVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询冥想系列列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 冥想系列分页列表
     */
    @Override
    public TableDataInfo<SeriesVo> queryPageList(SeriesBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<Series> lqw = buildQueryWrapper(bo);
        Page<SeriesVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的冥想系列列表
     *
     * @param bo 查询条件
     * @return 冥想系列列表
     */
    @Override
    public List<SeriesVo> queryList(SeriesBo bo) {
        LambdaQueryWrapper<Series> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<Series> buildQueryWrapper(SeriesBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<Series> lqw = Wrappers.lambdaQuery();
        lqw.orderByAsc(Series::getId);
        lqw.eq(bo.getCategoryId() != null, Series::getCategoryId, bo.getCategoryId());
        lqw.eq(StringUtils.isNotBlank(bo.getTitle()), Series::getTitle, bo.getTitle());
        lqw.eq(StringUtils.isNotBlank(bo.getSubtitle()), Series::getSubtitle, bo.getSubtitle());
        lqw.eq(bo.getCover() != null, Series::getCover, bo.getCover());
        lqw.eq(bo.getBanner() != null, Series::getBanner, bo.getBanner());
        lqw.eq(StringUtils.isNotBlank(bo.getIntro()), Series::getIntro, bo.getIntro());
        lqw.eq(bo.getEpisodeCount() != null, Series::getEpisodeCount, bo.getEpisodeCount());
        lqw.eq(bo.getRecommendDuration() != null, Series::getRecommendDuration, bo.getRecommendDuration());
        lqw.eq(StringUtils.isNotBlank(bo.getIsFree()), Series::getIsFree, bo.getIsFree());
        lqw.eq(bo.getOrderNum() != null, Series::getOrderNum, bo.getOrderNum());
        lqw.eq(StringUtils.isNotBlank(bo.getStatus()), Series::getStatus, bo.getStatus());
        lqw.eq(bo.getPublishTime() != null, Series::getPublishTime, bo.getPublishTime());
        return lqw;
    }

    /**
     * 新增冥想系列
     *
     * @param bo 冥想系列
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(SeriesBo bo) {
        Series add = MapstructUtils.convert(bo, Series.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改冥想系列
     *
     * @param bo 冥想系列
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(SeriesBo bo) {
        Series update = MapstructUtils.convert(bo, Series.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(Series entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除冥想系列信息
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
