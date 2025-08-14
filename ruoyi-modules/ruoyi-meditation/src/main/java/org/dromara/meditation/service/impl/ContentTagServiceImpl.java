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
import org.dromara.meditation.domain.bo.ContentTagBo;
import org.dromara.meditation.domain.vo.ContentTagVo;
import org.dromara.meditation.domain.ContentTag;
import org.dromara.meditation.mapper.ContentTagMapper;
import org.dromara.meditation.service.IContentTagService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 内容-标签关联Service业务层处理
 *
 * @author kdc
 * @date 2025-08-14
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class ContentTagServiceImpl implements IContentTagService {

    private final ContentTagMapper baseMapper;

    /**
     * 查询内容-标签关联
     *
     * @param id 主键
     * @return 内容-标签关联
     */
    @Override
    public ContentTagVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询内容-标签关联列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 内容-标签关联分页列表
     */
    @Override
    public TableDataInfo<ContentTagVo> queryPageList(ContentTagBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<ContentTag> lqw = buildQueryWrapper(bo);
        Page<ContentTagVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的内容-标签关联列表
     *
     * @param bo 查询条件
     * @return 内容-标签关联列表
     */
    @Override
    public List<ContentTagVo> queryList(ContentTagBo bo) {
        LambdaQueryWrapper<ContentTag> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<ContentTag> buildQueryWrapper(ContentTagBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<ContentTag> lqw = Wrappers.lambdaQuery();
        lqw.orderByAsc(ContentTag::getId);
        lqw.eq(StringUtils.isNotBlank(bo.getContentType()), ContentTag::getContentType, bo.getContentType());
        lqw.eq(bo.getContentId() != null, ContentTag::getContentId, bo.getContentId());
        lqw.eq(bo.getTagId() != null, ContentTag::getTagId, bo.getTagId());
        return lqw;
    }

    /**
     * 新增内容-标签关联
     *
     * @param bo 内容-标签关联
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(ContentTagBo bo) {
        ContentTag add = MapstructUtils.convert(bo, ContentTag.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改内容-标签关联
     *
     * @param bo 内容-标签关联
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(ContentTagBo bo) {
        ContentTag update = MapstructUtils.convert(bo, ContentTag.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(ContentTag entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除内容-标签关联信息
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
