package org.dromara.meditation.service;

import org.dromara.meditation.domain.vo.BannerVo;
import org.dromara.meditation.domain.bo.BannerBo;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 横幅配置Service接口
 *
 * @author kdc
 * @date 2025-08-14
 */
public interface IBannerService {

    /**
     * 查询横幅配置
     *
     * @param id 主键
     * @return 横幅配置
     */
    BannerVo queryById(Long id);

    /**
     * 分页查询横幅配置列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 横幅配置分页列表
     */
    TableDataInfo<BannerVo> queryPageList(BannerBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的横幅配置列表
     *
     * @param bo 查询条件
     * @return 横幅配置列表
     */
    List<BannerVo> queryList(BannerBo bo);

    /**
     * 新增横幅配置
     *
     * @param bo 横幅配置
     * @return 是否新增成功
     */
    Boolean insertByBo(BannerBo bo);

    /**
     * 修改横幅配置
     *
     * @param bo 横幅配置
     * @return 是否修改成功
     */
    Boolean updateByBo(BannerBo bo);

    /**
     * 校验并批量删除横幅配置信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
