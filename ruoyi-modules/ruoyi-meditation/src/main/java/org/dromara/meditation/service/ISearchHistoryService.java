package org.dromara.meditation.service;

import org.dromara.meditation.domain.vo.SearchHistoryVo;
import org.dromara.meditation.domain.bo.SearchHistoryBo;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 搜索历史Service接口
 *
 * @author kdc
 * @date 2025-08-14
 */
public interface ISearchHistoryService {

    /**
     * 查询搜索历史
     *
     * @param id 主键
     * @return 搜索历史
     */
    SearchHistoryVo queryById(Long id);

    /**
     * 分页查询搜索历史列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 搜索历史分页列表
     */
    TableDataInfo<SearchHistoryVo> queryPageList(SearchHistoryBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的搜索历史列表
     *
     * @param bo 查询条件
     * @return 搜索历史列表
     */
    List<SearchHistoryVo> queryList(SearchHistoryBo bo);

    /**
     * 新增搜索历史
     *
     * @param bo 搜索历史
     * @return 是否新增成功
     */
    Boolean insertByBo(SearchHistoryBo bo);

    /**
     * 修改搜索历史
     *
     * @param bo 搜索历史
     * @return 是否修改成功
     */
    Boolean updateByBo(SearchHistoryBo bo);

    /**
     * 校验并批量删除搜索历史信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    /**
     * 清除当前用户的搜索历史记录
     *
     * @return 是否清除成功
     */
    Boolean clearMyHistory();
}
