package org.dromara.meditation.service;

import org.dromara.meditation.domain.vo.SeriesVo;
import org.dromara.meditation.domain.bo.SeriesBo;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 冥想系列Service接口
 *
 * @author kdc
 * @date 2025-08-14
 */
public interface ISeriesService {

    /**
     * 查询冥想系列
     *
     * @param id 主键
     * @return 冥想系列
     */
    SeriesVo queryById(Long id);

    /**
     * 分页查询冥想系列列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 冥想系列分页列表
     */
    TableDataInfo<SeriesVo> queryPageList(SeriesBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的冥想系列列表
     *
     * @param bo 查询条件
     * @return 冥想系列列表
     */
    List<SeriesVo> queryList(SeriesBo bo);

    /**
     * 新增冥想系列
     *
     * @param bo 冥想系列
     * @return 是否新增成功
     */
    Boolean insertByBo(SeriesBo bo);

    /**
     * 修改冥想系列
     *
     * @param bo 冥想系列
     * @return 是否修改成功
     */
    Boolean updateByBo(SeriesBo bo);

    /**
     * 校验并批量删除冥想系列信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
