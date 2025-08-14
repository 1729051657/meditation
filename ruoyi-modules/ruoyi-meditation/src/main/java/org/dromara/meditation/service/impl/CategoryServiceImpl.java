package org.dromara.meditation.service.impl;

import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.core.utils.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.dromara.meditation.domain.bo.CategoryBo;
import org.dromara.meditation.domain.vo.CategoryVo;
import org.dromara.meditation.domain.Category;
import org.dromara.meditation.mapper.CategoryMapper;
import org.dromara.meditation.service.ICategoryService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 冥想分类Service业务层处理
 *
 * @author kdc
 * @date 2025-08-14
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class CategoryServiceImpl implements ICategoryService {

    private final CategoryMapper baseMapper;

    /**
     * 查询冥想分类
     *
     * @param id 主键
     * @return 冥想分类
     */
    @Override
    public CategoryVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }


    /**
     * 查询符合条件的冥想分类列表
     *
     * @param bo 查询条件
     * @return 冥想分类列表
     */
    @Override
    public List<CategoryVo> queryList(CategoryBo bo) {
        LambdaQueryWrapper<Category> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<Category> buildQueryWrapper(CategoryBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<Category> lqw = Wrappers.lambdaQuery();
        lqw.orderByAsc(Category::getId);
        lqw.eq(bo.getParentId() != null, Category::getParentId, bo.getParentId());
        lqw.like(StringUtils.isNotBlank(bo.getName()), Category::getName, bo.getName());
        lqw.eq(StringUtils.isNotBlank(bo.getCode()), Category::getCode, bo.getCode());
        lqw.eq(bo.getIcon() != null, Category::getIcon, bo.getIcon());
        lqw.eq(StringUtils.isNotBlank(bo.getDescription()), Category::getDescription, bo.getDescription());
        lqw.eq(bo.getOrderNum() != null, Category::getOrderNum, bo.getOrderNum());
        lqw.eq(StringUtils.isNotBlank(bo.getStatus()), Category::getStatus, bo.getStatus());
        return lqw;
    }

    /**
     * 新增冥想分类
     *
     * @param bo 冥想分类
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(CategoryBo bo) {
        Category add = MapstructUtils.convert(bo, Category.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改冥想分类
     *
     * @param bo 冥想分类
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(CategoryBo bo) {
        Category update = MapstructUtils.convert(bo, Category.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(Category entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除冥想分类信息
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
