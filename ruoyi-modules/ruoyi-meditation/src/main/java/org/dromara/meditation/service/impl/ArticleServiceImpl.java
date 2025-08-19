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
import org.dromara.meditation.domain.bo.ArticleBo;
import org.dromara.meditation.domain.vo.ArticleVo;
import org.dromara.meditation.domain.Article;
import org.dromara.meditation.mapper.ArticleMapper;
import org.dromara.meditation.service.IArticleService;
import org.dromara.meditation.service.IContentTagService;
import org.dromara.meditation.service.ITagService;

import java.util.List;
import java.util.Map;
import java.util.Collection;
import cn.hutool.core.collection.CollUtil;

/**
 * 冥想知识文章Service业务层处理
 *
 * @author kdc
 * @date 2025-08-14
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class ArticleServiceImpl implements IArticleService {

    private final ArticleMapper baseMapper;
    private final IContentTagService contentTagService;
    private final ITagService tagService;

    /**
     * 查询冥想知识文章
     *
     * @param id 主键
     * @return 冥想知识文章
     */
    @Override
    public ArticleVo queryById(Long id){
        ArticleVo vo = baseMapper.selectVoById(id);
        if (vo != null) {
            // 查询标签
            List<Long> tagIds = contentTagService.getTagIdsByContent("article", id);
            vo.setTagIds(tagIds);
            if (CollUtil.isNotEmpty(tagIds)) {
                vo.setTags(tagService.queryByIds(tagIds));
            }
        }
        return vo;
    }

    /**
     * 分页查询冥想知识文章列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 冥想知识文章分页列表
     */
    @Override
    public TableDataInfo<ArticleVo> queryPageList(ArticleBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<Article> lqw = buildQueryWrapper(bo);
        Page<ArticleVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的冥想知识文章列表
     *
     * @param bo 查询条件
     * @return 冥想知识文章列表
     */
    @Override
    public List<ArticleVo> queryList(ArticleBo bo) {
        LambdaQueryWrapper<Article> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<Article> buildQueryWrapper(ArticleBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<Article> lqw = Wrappers.lambdaQuery();
        lqw.orderByAsc(Article::getId);
        lqw.eq(StringUtils.isNotBlank(bo.getTitle()), Article::getTitle, bo.getTitle());
        lqw.eq(bo.getCover() != null, Article::getCover, bo.getCover());
        lqw.eq(StringUtils.isNotBlank(bo.getSummary()), Article::getSummary, bo.getSummary());
        lqw.eq(StringUtils.isNotBlank(bo.getContent()), Article::getContent, bo.getContent());
        lqw.eq(bo.getAuthorId() != null, Article::getAuthorId, bo.getAuthorId());
        lqw.eq(StringUtils.isNotBlank(bo.getStatus()), Article::getStatus, bo.getStatus());
        lqw.eq(bo.getPublishTime() != null, Article::getPublishTime, bo.getPublishTime());
        lqw.eq(bo.getOrderNum() != null, Article::getOrderNum, bo.getOrderNum());
        return lqw;
    }

    /**
     * 新增冥想知识文章
     *
     * @param bo 冥想知识文章
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(ArticleBo bo) {
        Article add = MapstructUtils.convert(bo, Article.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
            // 保存标签关联
            if (CollUtil.isNotEmpty(bo.getTagIds())) {
                contentTagService.updateContentTags("article", add.getId(), bo.getTagIds());
            }
        }
        return flag;
    }

    /**
     * 修改冥想知识文章
     *
     * @param bo 冥想知识文章
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(ArticleBo bo) {
        Article update = MapstructUtils.convert(bo, Article.class);
        validEntityBeforeSave(update);
        boolean flag = baseMapper.updateById(update) > 0;
        if (flag) {
            // 更新标签关联
            contentTagService.updateContentTags("article", bo.getId(), bo.getTagIds());
        }
        return flag;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(Article entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除冥想知识文章信息
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
        boolean flag = baseMapper.deleteByIds(ids) > 0;
        if (flag) {
            // 删除标签关联
            contentTagService.deleteByContent("article", ids);
        }
        return flag;
    }
}
