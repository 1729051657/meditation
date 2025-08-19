package org.dromara.meditation.service.impl;

import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.dromara.meditation.domain.bo.BannerBo;
import org.dromara.meditation.domain.vo.BannerVo;
import org.dromara.meditation.domain.Banner;
import org.dromara.meditation.mapper.BannerMapper;
import org.dromara.meditation.service.IBannerService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 横幅配置Service业务层处理
 *
 * @author kdc
 * @date 2025-08-14
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class BannerServiceImpl implements IBannerService {

    private final BannerMapper baseMapper;

    /**
     * 查询横幅配置
     *
     * @param id 主键
     * @return 横幅配置
     */
    @Override
    public BannerVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询横幅配置列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 横幅配置分页列表
     */
    @Override
    public TableDataInfo<BannerVo> queryPageList(BannerBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<Banner> lqw = buildQueryWrapper(bo);
        Page<BannerVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的横幅配置列表
     *
     * @param bo 查询条件
     * @return 横幅配置列表
     */
    @Override
    public List<BannerVo> queryList(BannerBo bo) {
        LambdaQueryWrapper<Banner> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<Banner> buildQueryWrapper(BannerBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<Banner> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getPage()), Banner::getPage, bo.getPage());
        lqw.eq(bo.getImage() != null, Banner::getImage, bo.getImage());
        lqw.eq(StringUtils.isNotBlank(bo.getLinkType()), Banner::getLinkType, bo.getLinkType());
        lqw.eq(StringUtils.isNotBlank(bo.getLinkTarget()), Banner::getLinkTarget, bo.getLinkTarget());
        lqw.eq(bo.getOrderNum() != null, Banner::getOrderNum, bo.getOrderNum());
        lqw.eq(StringUtils.isNotBlank(bo.getStatus()), Banner::getStatus, bo.getStatus());
        lqw.eq(bo.getStartTime() != null, Banner::getStartTime, bo.getStartTime());
        lqw.eq(bo.getEndTime() != null, Banner::getEndTime, bo.getEndTime());
        lqw.eq(Banner::getDelFlag, "0");
        lqw.orderByDesc(Banner::getOrderNum);
        return lqw;
    }

    /**
     * 新增横幅配置
     *
     * @param bo 横幅配置
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(BannerBo bo) {
        Banner add = MapstructUtils.convert(bo, Banner.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改横幅配置
     *
     * @param bo 横幅配置
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(BannerBo bo) {
        Banner update = MapstructUtils.convert(bo, Banner.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(Banner entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除横幅配置信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteByIds(ids) > 0;
    }
}
