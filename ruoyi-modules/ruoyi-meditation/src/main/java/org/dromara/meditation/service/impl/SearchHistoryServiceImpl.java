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
import org.dromara.meditation.domain.bo.SearchHistoryBo;
import org.dromara.meditation.domain.vo.SearchHistoryVo;
import org.dromara.meditation.domain.SearchHistory;
import org.dromara.meditation.mapper.SearchHistoryMapper;
import org.dromara.meditation.service.ISearchHistoryService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 搜索历史Service业务层处理
 *
 * @author kdc
 * @date 2025-08-14
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class SearchHistoryServiceImpl implements ISearchHistoryService {

    private final SearchHistoryMapper baseMapper;

    /**
     * 查询搜索历史
     *
     * @param id 主键
     * @return 搜索历史
     */
    @Override
    public SearchHistoryVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询搜索历史列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 搜索历史分页列表
     */
    @Override
    public TableDataInfo<SearchHistoryVo> queryPageList(SearchHistoryBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<SearchHistory> lqw = buildQueryWrapper(bo);
        Page<SearchHistoryVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的搜索历史列表
     *
     * @param bo 查询条件
     * @return 搜索历史列表
     */
    @Override
    public List<SearchHistoryVo> queryList(SearchHistoryBo bo) {
        LambdaQueryWrapper<SearchHistory> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<SearchHistory> buildQueryWrapper(SearchHistoryBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<SearchHistory> lqw = Wrappers.lambdaQuery();
        lqw.orderByAsc(SearchHistory::getId);
        lqw.eq(bo.getUserId() != null, SearchHistory::getUserId, bo.getUserId());
        lqw.eq(StringUtils.isNotBlank(bo.getKeyword()), SearchHistory::getKeyword, bo.getKeyword());
        lqw.eq(bo.getTimes() != null, SearchHistory::getTimes, bo.getTimes());
        lqw.eq(bo.getLastTime() != null, SearchHistory::getLastTime, bo.getLastTime());
        return lqw;
    }

    /**
     * 新增搜索历史
     *
     * @param bo 搜索历史
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(SearchHistoryBo bo) {
        SearchHistory add = MapstructUtils.convert(bo, SearchHistory.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改搜索历史
     *
     * @param bo 搜索历史
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(SearchHistoryBo bo) {
        SearchHistory update = MapstructUtils.convert(bo, SearchHistory.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(SearchHistory entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除搜索历史信息
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
