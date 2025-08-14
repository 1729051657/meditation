package org.dromara.meditation.service;

import org.dromara.meditation.domain.vo.ArticleVo;
import org.dromara.meditation.domain.bo.ArticleBo;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 冥想知识文章Service接口
 *
 * @author kdc
 * @date 2025-08-14
 */
public interface IArticleService {

    /**
     * 查询冥想知识文章
     *
     * @param id 主键
     * @return 冥想知识文章
     */
    ArticleVo queryById(Long id);

    /**
     * 分页查询冥想知识文章列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 冥想知识文章分页列表
     */
    TableDataInfo<ArticleVo> queryPageList(ArticleBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的冥想知识文章列表
     *
     * @param bo 查询条件
     * @return 冥想知识文章列表
     */
    List<ArticleVo> queryList(ArticleBo bo);

    /**
     * 新增冥想知识文章
     *
     * @param bo 冥想知识文章
     * @return 是否新增成功
     */
    Boolean insertByBo(ArticleBo bo);

    /**
     * 修改冥想知识文章
     *
     * @param bo 冥想知识文章
     * @return 是否修改成功
     */
    Boolean updateByBo(ArticleBo bo);

    /**
     * 校验并批量删除冥想知识文章信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
