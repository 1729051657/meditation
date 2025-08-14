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
import org.dromara.meditation.domain.bo.SeriesProgressBo;
import org.dromara.meditation.domain.vo.SeriesProgressVo;
import org.dromara.meditation.domain.SeriesProgress;
import org.dromara.meditation.mapper.SeriesProgressMapper;
import org.dromara.meditation.service.ISeriesProgressService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 系列学习进度Service业务层处理
 *
 * @author kdc
 * @date 2025-08-14
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class SeriesProgressServiceImpl implements ISeriesProgressService {

    private final SeriesProgressMapper baseMapper;

    /**
     * 查询系列学习进度
     *
     * @param id 主键
     * @return 系列学习进度
     */
    @Override
    public SeriesProgressVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询系列学习进度列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 系列学习进度分页列表
     */
    @Override
    public TableDataInfo<SeriesProgressVo> queryPageList(SeriesProgressBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<SeriesProgress> lqw = buildQueryWrapper(bo);
        Page<SeriesProgressVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的系列学习进度列表
     *
     * @param bo 查询条件
     * @return 系列学习进度列表
     */
    @Override
    public List<SeriesProgressVo> queryList(SeriesProgressBo bo) {
        LambdaQueryWrapper<SeriesProgress> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<SeriesProgress> buildQueryWrapper(SeriesProgressBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<SeriesProgress> lqw = Wrappers.lambdaQuery();
        lqw.orderByAsc(SeriesProgress::getId);
        lqw.eq(bo.getUserId() != null, SeriesProgress::getUserId, bo.getUserId());
        lqw.eq(bo.getSeriesId() != null, SeriesProgress::getSeriesId, bo.getSeriesId());
        lqw.eq(bo.getLastTrackId() != null, SeriesProgress::getLastTrackId, bo.getLastTrackId());
        lqw.eq(bo.getCompletedCount() != null, SeriesProgress::getCompletedCount, bo.getCompletedCount());
        return lqw;
    }

    /**
     * 新增系列学习进度
     *
     * @param bo 系列学习进度
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(SeriesProgressBo bo) {
        SeriesProgress add = MapstructUtils.convert(bo, SeriesProgress.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改系列学习进度
     *
     * @param bo 系列学习进度
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(SeriesProgressBo bo) {
        SeriesProgress update = MapstructUtils.convert(bo, SeriesProgress.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(SeriesProgress entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除系列学习进度信息
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
