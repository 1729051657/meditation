package org.dromara.meditation.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.unit.DataUnit;
import lombok.Data;
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
import org.dromara.common.satoken.utils.LoginHelper;

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
        
        // 如果没有指定用户ID，默认查询当前用户的历史
        if (bo.getUserId() == null) {
            Long currentUserId = LoginHelper.getUserId();
            if (currentUserId != null) {
                lqw.eq(SearchHistory::getUserId, currentUserId);
            }
        } else {
            lqw.eq(SearchHistory::getUserId, bo.getUserId());
        }
        
        lqw.eq(StringUtils.isNotBlank(bo.getKeyword()), SearchHistory::getKeyword, bo.getKeyword());
        lqw.eq(bo.getTimes() != null, SearchHistory::getTimes, bo.getTimes());
        lqw.eq(bo.getLastTime() != null, SearchHistory::getLastTime, bo.getLastTime());
        
        // 按最后搜索时间倒序排列，最近搜索的在前面
        lqw.orderByDesc(SearchHistory::getLastTime);
        
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
        // 设置当前用户ID
        Long userId = LoginHelper.getUserId();
        if (userId == null) {
            log.warn("添加搜索历史失败：用户未登录");
            return false;
        }
        bo.setUserId(userId);
        
        // 检查是否已存在相同的搜索历史
        LambdaQueryWrapper<SearchHistory> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(SearchHistory::getUserId, userId)
               .eq(SearchHistory::getKeyword, bo.getKeyword());
        SearchHistory existing = baseMapper.selectOne(wrapper);
        
        if (existing != null) {
            // 如果已存在，更新搜索次数和最后搜索时间
            existing.setTimes((existing.getTimes() == null ? 0 : existing.getTimes()) + 1);
            existing.setLastTime(DateUtil.date());
            return baseMapper.updateById(existing) > 0;
        } else {
            // 如果不存在，新增搜索历史
            SearchHistory add = MapstructUtils.convert(bo, SearchHistory.class);
            add.setUserId(userId);
            add.setTimes(1);
            add.setLastTime(DateUtil.date());
            validEntityBeforeSave(add);
            boolean flag = baseMapper.insert(add) > 0;
            if (flag) {
                bo.setId(add.getId());
            }
            return flag;
        }
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
    
    /**
     * 清空当前用户的搜索历史
     *
     * @return 是否清空成功
     */
    @Override
    public Boolean clearUserHistory() {
        Long userId = LoginHelper.getUserId();
        if (userId == null) {
            log.warn("清空搜索历史失败：用户未登录");
            return false;
        }
        
        // 构建查询条件，只删除当前用户的搜索历史
        LambdaQueryWrapper<SearchHistory> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(SearchHistory::getUserId, userId);
        
        int deleted = baseMapper.delete(wrapper);
        log.info("用户 {} 清空了 {} 条搜索历史", userId, deleted);
        
        return deleted >= 0; // 即使没有历史记录也返回成功
    }
}
