package org.dromara.meditation.service;

import org.dromara.meditation.domain.vo.HotKeywordVo;
import org.dromara.meditation.domain.bo.HotKeywordBo;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 热搜关键词Service接口
 *
 * @author kdc
 * @date 2025-08-14
 */
public interface IHotKeywordService {

    /**
     * 查询热搜关键词
     *
     * @param id 主键
     * @return 热搜关键词
     */
    HotKeywordVo queryById(Long id);

    /**
     * 分页查询热搜关键词列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 热搜关键词分页列表
     */
    TableDataInfo<HotKeywordVo> queryPageList(HotKeywordBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的热搜关键词列表
     *
     * @param bo 查询条件
     * @return 热搜关键词列表
     */
    List<HotKeywordVo> queryList(HotKeywordBo bo);

    /**
     * 新增热搜关键词
     *
     * @param bo 热搜关键词
     * @return 是否新增成功
     */
    Boolean insertByBo(HotKeywordBo bo);

    /**
     * 修改热搜关键词
     *
     * @param bo 热搜关键词
     * @return 是否修改成功
     */
    Boolean updateByBo(HotKeywordBo bo);

    /**
     * 校验并批量删除热搜关键词信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
