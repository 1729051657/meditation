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
import org.dromara.meditation.domain.bo.TagBo;
import org.dromara.meditation.domain.vo.TagVo;
import org.dromara.meditation.domain.Tag;
import org.dromara.meditation.mapper.TagMapper;
import org.dromara.meditation.service.ITagService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 内容标签Service业务层处理
 *
 * @author kdc
 * @date 2025-08-14
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class TagServiceImpl implements ITagService {

    private final TagMapper baseMapper;

    /**
     * 查询内容标签
     *
     * @param id 主键
     * @return 内容标签
     */
    @Override
    public TagVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询内容标签列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 内容标签分页列表
     */
    @Override
    public TableDataInfo<TagVo> queryPageList(TagBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<Tag> lqw = buildQueryWrapper(bo);
        Page<TagVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的内容标签列表
     *
     * @param bo 查询条件
     * @return 内容标签列表
     */
    @Override
    public List<TagVo> queryList(TagBo bo) {
        LambdaQueryWrapper<Tag> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<Tag> buildQueryWrapper(TagBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<Tag> lqw = Wrappers.lambdaQuery();
        lqw.orderByAsc(Tag::getId);
        lqw.like(StringUtils.isNotBlank(bo.getName()), Tag::getName, bo.getName());
        lqw.eq(StringUtils.isNotBlank(bo.getType()), Tag::getType, bo.getType());
        lqw.eq(StringUtils.isNotBlank(bo.getStatus()), Tag::getStatus, bo.getStatus());
        lqw.eq(bo.getOrderNum() != null, Tag::getOrderNum, bo.getOrderNum());
        return lqw;
    }

    /**
     * 新增内容标签
     *
     * @param bo 内容标签
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(TagBo bo) {
        Tag add = MapstructUtils.convert(bo, Tag.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改内容标签
     *
     * @param bo 内容标签
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(TagBo bo) {
        Tag update = MapstructUtils.convert(bo, Tag.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(Tag entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除内容标签信息
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
