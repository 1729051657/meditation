package org.dromara.meditation.service;

import org.dromara.meditation.domain.vo.TrackVo;
import org.dromara.meditation.domain.bo.TrackBo;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 冥想单集Service接口
 *
 * @author kdc
 * @date 2025-08-14
 */
public interface ITrackService {

    /**
     * 查询冥想单集
     *
     * @param id 主键
     * @return 冥想单集
     */
    TrackVo queryById(Long id);

    /**
     * 分页查询冥想单集列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 冥想单集分页列表
     */
    TableDataInfo<TrackVo> queryPageList(TrackBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的冥想单集列表
     *
     * @param bo 查询条件
     * @return 冥想单集列表
     */
    List<TrackVo> queryList(TrackBo bo);

    /**
     * 新增冥想单集
     *
     * @param bo 冥想单集
     * @return 是否新增成功
     */
    Boolean insertByBo(TrackBo bo);

    /**
     * 修改冥想单集
     *
     * @param bo 冥想单集
     * @return 是否修改成功
     */
    Boolean updateByBo(TrackBo bo);

    /**
     * 校验并批量删除冥想单集信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
