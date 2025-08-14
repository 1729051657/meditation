package org.dromara.meditation.service;

import org.dromara.meditation.domain.vo.CategoryVo;
import org.dromara.meditation.domain.bo.CategoryBo;

import java.util.Collection;
import java.util.List;

/**
 * 冥想分类Service接口
 *
 * @author kdc
 * @date 2025-08-14
 */
public interface ICategoryService {

    /**
     * 查询冥想分类
     *
     * @param id 主键
     * @return 冥想分类
     */
    CategoryVo queryById(Long id);


    /**
     * 查询符合条件的冥想分类列表
     *
     * @param bo 查询条件
     * @return 冥想分类列表
     */
    List<CategoryVo> queryList(CategoryBo bo);

    /**
     * 新增冥想分类
     *
     * @param bo 冥想分类
     * @return 是否新增成功
     */
    Boolean insertByBo(CategoryBo bo);

    /**
     * 修改冥想分类
     *
     * @param bo 冥想分类
     * @return 是否修改成功
     */
    Boolean updateByBo(CategoryBo bo);

    /**
     * 校验并批量删除冥想分类信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
