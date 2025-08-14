package org.dromara.meditation.service;

import org.dromara.meditation.domain.vo.SeriesProgressVo;
import org.dromara.meditation.domain.bo.SeriesProgressBo;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 系列学习进度Service接口
 *
 * @author kdc
 * @date 2025-08-14
 */
public interface ISeriesProgressService {

    /**
     * 查询系列学习进度
     *
     * @param id 主键
     * @return 系列学习进度
     */
    SeriesProgressVo queryById(Long id);

    /**
     * 分页查询系列学习进度列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 系列学习进度分页列表
     */
    TableDataInfo<SeriesProgressVo> queryPageList(SeriesProgressBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的系列学习进度列表
     *
     * @param bo 查询条件
     * @return 系列学习进度列表
     */
    List<SeriesProgressVo> queryList(SeriesProgressBo bo);

    /**
     * 新增系列学习进度
     *
     * @param bo 系列学习进度
     * @return 是否新增成功
     */
    Boolean insertByBo(SeriesProgressBo bo);

    /**
     * 修改系列学习进度
     *
     * @param bo 系列学习进度
     * @return 是否修改成功
     */
    Boolean updateByBo(SeriesProgressBo bo);

    /**
     * 校验并批量删除系列学习进度信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
