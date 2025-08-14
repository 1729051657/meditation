package org.dromara.meditation.service;

import org.dromara.meditation.domain.vo.RecommendSlotVo;
import org.dromara.meditation.domain.bo.RecommendSlotBo;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 推荐位Service接口
 *
 * @author kdc
 * @date 2025-08-14
 */
public interface IRecommendSlotService {

    /**
     * 查询推荐位
     *
     * @param id 主键
     * @return 推荐位
     */
    RecommendSlotVo queryById(Long id);

    /**
     * 分页查询推荐位列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 推荐位分页列表
     */
    TableDataInfo<RecommendSlotVo> queryPageList(RecommendSlotBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的推荐位列表
     *
     * @param bo 查询条件
     * @return 推荐位列表
     */
    List<RecommendSlotVo> queryList(RecommendSlotBo bo);

    /**
     * 新增推荐位
     *
     * @param bo 推荐位
     * @return 是否新增成功
     */
    Boolean insertByBo(RecommendSlotBo bo);

    /**
     * 修改推荐位
     *
     * @param bo 推荐位
     * @return 是否修改成功
     */
    Boolean updateByBo(RecommendSlotBo bo);

    /**
     * 校验并批量删除推荐位信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
