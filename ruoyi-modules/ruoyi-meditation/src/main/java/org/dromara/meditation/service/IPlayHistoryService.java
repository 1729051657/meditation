package org.dromara.meditation.service;

import org.dromara.meditation.domain.vo.PlayHistoryVo;
import org.dromara.meditation.domain.bo.PlayHistoryBo;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 音频播放记录Service接口
 *
 * @author kdc
 * @date 2025-08-14
 */
public interface IPlayHistoryService {

    /**
     * 查询音频播放记录
     *
     * @param id 主键
     * @return 音频播放记录
     */
    PlayHistoryVo queryById(Long id);

    /**
     * 分页查询音频播放记录列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 音频播放记录分页列表
     */
    TableDataInfo<PlayHistoryVo> queryPageList(PlayHistoryBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的音频播放记录列表
     *
     * @param bo 查询条件
     * @return 音频播放记录列表
     */
    List<PlayHistoryVo> queryList(PlayHistoryBo bo);

    /**
     * 新增音频播放记录
     *
     * @param bo 音频播放记录
     * @return 是否新增成功
     */
    Boolean insertByBo(PlayHistoryBo bo);

    /**
     * 修改音频播放记录
     *
     * @param bo 音频播放记录
     * @return 是否修改成功
     */
    Boolean updateByBo(PlayHistoryBo bo);

    /**
     * 校验并批量删除音频播放记录信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
