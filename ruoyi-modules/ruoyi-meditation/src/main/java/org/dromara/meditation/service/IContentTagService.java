package org.dromara.meditation.service;

import org.dromara.meditation.domain.vo.ContentTagVo;
import org.dromara.meditation.domain.bo.ContentTagBo;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 内容-标签关联Service接口
 *
 * @author kdc
 * @date 2025-08-14
 */
public interface IContentTagService {

    /**
     * 查询内容-标签关联
     *
     * @param id 主键
     * @return 内容-标签关联
     */
    ContentTagVo queryById(Long id);

    /**
     * 分页查询内容-标签关联列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 内容-标签关联分页列表
     */
    TableDataInfo<ContentTagVo> queryPageList(ContentTagBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的内容-标签关联列表
     *
     * @param bo 查询条件
     * @return 内容-标签关联列表
     */
    List<ContentTagVo> queryList(ContentTagBo bo);

    /**
     * 新增内容-标签关联
     *
     * @param bo 内容-标签关联
     * @return 是否新增成功
     */
    Boolean insertByBo(ContentTagBo bo);

    /**
     * 修改内容-标签关联
     *
     * @param bo 内容-标签关联
     * @return 是否修改成功
     */
    Boolean updateByBo(ContentTagBo bo);

    /**
     * 校验并批量删除内容-标签关联信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    /**
     * 批量更新内容的标签
     *
     * @param contentType 内容类型（series/article/track）
     * @param contentId   内容ID
     * @param tagIds      标签ID列表
     * @return 是否更新成功
     */
    Boolean updateContentTags(String contentType, Long contentId, List<Long> tagIds);

    /**
     * 根据内容获取标签列表
     *
     * @param contentType 内容类型
     * @param contentId   内容ID
     * @return 标签列表
     */
    List<Long> getTagIdsByContent(String contentType, Long contentId);

    /**
     * 批量删除内容的所有标签
     *
     * @param contentType 内容类型
     * @param contentIds  内容ID列表
     * @return 是否删除成功
     */
    Boolean deleteByContent(String contentType, Collection<Long> contentIds);
}
