package org.dromara.meditation.service;

import org.dromara.meditation.domain.vo.RecommendItemVo;
import org.dromara.meditation.domain.bo.RecommendItemBo;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 推荐位内容Service接口
 *
 * @author kdc
 * @date 2025-08-14
 */
public interface IRecommendItemService {

    /**
     * 查询推荐位内容
     *
     * @param id 主键
     * @return 推荐位内容
     */
    RecommendItemVo queryById(Long id);

    /**
     * 分页查询推荐位内容列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 推荐位内容分页列表
     */
    TableDataInfo<RecommendItemVo> queryPageList(RecommendItemBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的推荐位内容列表
     *
     * @param bo 查询条件
     * @return 推荐位内容列表
     */
    List<RecommendItemVo> queryList(RecommendItemBo bo);

    /**
     * 新增推荐位内容
     *
     * @param bo 推荐位内容
     * @return 是否新增成功
     */
    Boolean insertByBo(RecommendItemBo bo);

    /**
     * 修改推荐位内容
     *
     * @param bo 推荐位内容
     * @return 是否修改成功
     */
    Boolean updateByBo(RecommendItemBo bo);

    /**
     * 校验并批量删除推荐位内容信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
