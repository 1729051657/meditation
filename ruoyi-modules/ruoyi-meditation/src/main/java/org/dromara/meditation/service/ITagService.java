package org.dromara.meditation.service;

import org.dromara.meditation.domain.vo.TagVo;
import org.dromara.meditation.domain.bo.TagBo;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 内容标签Service接口
 *
 * @author kdc
 * @date 2025-08-14
 */
public interface ITagService {

    /**
     * 查询内容标签
     *
     * @param id 主键
     * @return 内容标签
     */
    TagVo queryById(Long id);

    /**
     * 分页查询内容标签列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 内容标签分页列表
     */
    TableDataInfo<TagVo> queryPageList(TagBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的内容标签列表
     *
     * @param bo 查询条件
     * @return 内容标签列表
     */
    List<TagVo> queryList(TagBo bo);

    /**
     * 新增内容标签
     *
     * @param bo 内容标签
     * @return 是否新增成功
     */
    Boolean insertByBo(TagBo bo);

    /**
     * 修改内容标签
     *
     * @param bo 内容标签
     * @return 是否修改成功
     */
    Boolean updateByBo(TagBo bo);

    /**
     * 校验并批量删除内容标签信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
