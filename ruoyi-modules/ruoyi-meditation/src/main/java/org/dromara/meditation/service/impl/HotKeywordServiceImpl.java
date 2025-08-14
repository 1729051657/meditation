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
import org.dromara.meditation.domain.bo.HotKeywordBo;
import org.dromara.meditation.domain.vo.HotKeywordVo;
import org.dromara.meditation.domain.HotKeyword;
import org.dromara.meditation.mapper.HotKeywordMapper;
import org.dromara.meditation.service.IHotKeywordService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 热搜关键词Service业务层处理
 *
 * @author kdc
 * @date 2025-08-14
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class HotKeywordServiceImpl implements IHotKeywordService {

    private final HotKeywordMapper baseMapper;

    /**
     * 查询热搜关键词
     *
     * @param id 主键
     * @return 热搜关键词
     */
    @Override
    public HotKeywordVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询热搜关键词列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 热搜关键词分页列表
     */
    @Override
    public TableDataInfo<HotKeywordVo> queryPageList(HotKeywordBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<HotKeyword> lqw = buildQueryWrapper(bo);
        Page<HotKeywordVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的热搜关键词列表
     *
     * @param bo 查询条件
     * @return 热搜关键词列表
     */
    @Override
    public List<HotKeywordVo> queryList(HotKeywordBo bo) {
        LambdaQueryWrapper<HotKeyword> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<HotKeyword> buildQueryWrapper(HotKeywordBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<HotKeyword> lqw = Wrappers.lambdaQuery();
        lqw.orderByAsc(HotKeyword::getId);
        lqw.eq(StringUtils.isNotBlank(bo.getKeyword()), HotKeyword::getKeyword, bo.getKeyword());
        lqw.eq(bo.getOrderNum() != null, HotKeyword::getOrderNum, bo.getOrderNum());
        lqw.eq(bo.getStartTime() != null, HotKeyword::getStartTime, bo.getStartTime());
        lqw.eq(bo.getEndTime() != null, HotKeyword::getEndTime, bo.getEndTime());
        lqw.eq(StringUtils.isNotBlank(bo.getStatus()), HotKeyword::getStatus, bo.getStatus());
        return lqw;
    }

    /**
     * 新增热搜关键词
     *
     * @param bo 热搜关键词
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(HotKeywordBo bo) {
        HotKeyword add = MapstructUtils.convert(bo, HotKeyword.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改热搜关键词
     *
     * @param bo 热搜关键词
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(HotKeywordBo bo) {
        HotKeyword update = MapstructUtils.convert(bo, HotKeyword.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(HotKeyword entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除热搜关键词信息
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
