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
import org.dromara.meditation.domain.RecommendItem;
import org.dromara.meditation.mapper.RecommendItemMapper;
import org.dromara.meditation.service.IRecommendItemService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

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
        return baseMapper.selectVoList(lqw);
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
}
