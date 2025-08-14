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
import org.dromara.meditation.domain.bo.RecommendSlotBo;
import org.dromara.meditation.domain.vo.RecommendSlotVo;
import org.dromara.meditation.domain.RecommendSlot;
import org.dromara.meditation.mapper.RecommendSlotMapper;
import org.dromara.meditation.service.IRecommendSlotService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 推荐位Service业务层处理
 *
 * @author kdc
 * @date 2025-08-14
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class RecommendSlotServiceImpl implements IRecommendSlotService {

    private final RecommendSlotMapper baseMapper;

    /**
     * 查询推荐位
     *
     * @param id 主键
     * @return 推荐位
     */
    @Override
    public RecommendSlotVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询推荐位列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 推荐位分页列表
     */
    @Override
    public TableDataInfo<RecommendSlotVo> queryPageList(RecommendSlotBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<RecommendSlot> lqw = buildQueryWrapper(bo);
        Page<RecommendSlotVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的推荐位列表
     *
     * @param bo 查询条件
     * @return 推荐位列表
     */
    @Override
    public List<RecommendSlotVo> queryList(RecommendSlotBo bo) {
        LambdaQueryWrapper<RecommendSlot> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<RecommendSlot> buildQueryWrapper(RecommendSlotBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<RecommendSlot> lqw = Wrappers.lambdaQuery();
        lqw.orderByAsc(RecommendSlot::getId);
        lqw.eq(StringUtils.isNotBlank(bo.getCode()), RecommendSlot::getCode, bo.getCode());
        lqw.like(StringUtils.isNotBlank(bo.getName()), RecommendSlot::getName, bo.getName());
        lqw.eq(StringUtils.isNotBlank(bo.getPage()), RecommendSlot::getPage, bo.getPage());
        lqw.eq(StringUtils.isNotBlank(bo.getStatus()), RecommendSlot::getStatus, bo.getStatus());
        lqw.eq(bo.getOrderNum() != null, RecommendSlot::getOrderNum, bo.getOrderNum());
        return lqw;
    }

    /**
     * 新增推荐位
     *
     * @param bo 推荐位
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(RecommendSlotBo bo) {
        RecommendSlot add = MapstructUtils.convert(bo, RecommendSlot.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改推荐位
     *
     * @param bo 推荐位
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(RecommendSlotBo bo) {
        RecommendSlot update = MapstructUtils.convert(bo, RecommendSlot.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(RecommendSlot entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除推荐位信息
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
