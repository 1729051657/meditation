package org.dromara.meditation.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.meditation.domain.Article;
import org.dromara.meditation.domain.Category;
import org.dromara.meditation.domain.RecommendItem;
import org.dromara.meditation.domain.RecommendSlot;
import org.dromara.meditation.domain.Series;
import org.dromara.meditation.domain.Track;
import org.dromara.meditation.domain.vo.HomeDataVo;
import org.dromara.meditation.domain.vo.CategoryVo;
import org.dromara.meditation.domain.vo.SeriesVo;
import org.dromara.meditation.domain.vo.TrackVo;
import org.dromara.meditation.domain.vo.ArticleVo;
import org.dromara.meditation.mapper.ArticleMapper;
import org.dromara.meditation.mapper.CategoryMapper;
import org.dromara.meditation.mapper.RecommendItemMapper;
import org.dromara.meditation.mapper.RecommendSlotMapper;
import org.dromara.meditation.mapper.SeriesMapper;
import org.dromara.meditation.mapper.TrackMapper;
import org.dromara.meditation.service.IHomeService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 首页数据服务实现
 *
 * @author system
 * @date 2025-01-01
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class HomeServiceImpl implements IHomeService {

    private final RecommendSlotMapper recommendSlotMapper;
    private final RecommendItemMapper recommendItemMapper;
    private final SeriesMapper seriesMapper;
    private final TrackMapper trackMapper;
    private final ArticleMapper articleMapper;
    private final CategoryMapper categoryMapper;

    /**
     * 获取首页数据
     *
     * @return 首页数据
     */
    @Override
    public HomeDataVo getHomeData() {
        HomeDataVo homeData = new HomeDataVo();

        // 设置问候语
        homeData.setGreeting(getGreeting());

        // 设置功能分类
        homeData.setCategoryVoList(getCategories());

        // 获取冥想练习推荐位
        homeData.setSeriesVoList(getMeditationSlots());

        // 获取推荐内容
        homeData.setTrackVoList(getRecommendItems());

        // 获取知识内容
        homeData.setArticleVoList(getKnowledgeItems());

        return homeData;
    }

    /**
     * 获取问候语
     */
    private String getGreeting() {
        int hour = LocalDateTime.now().getHour();
        if (hour >= 5 && hour < 12) {
            return "上午好";
        } else if (hour >= 12 && hour < 18) {
            return "下午好";
        } else {
            return "晚上好";
        }
    }

    /**
     * 获取功能分类
     */
    private List<CategoryVo> getCategories() {
        // 查询启用状态的分类，按排序号排序
        LambdaQueryWrapper<Category> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(Category::getStatus, "0") // 正常状态
               .orderByAsc(Category::getOrderNum);

        return categoryMapper.selectVoList(wrapper);
    }

    /**
     * 获取冥想练习推荐位
     */
    private List<SeriesVo> getMeditationSlots() {
        List<SeriesVo> items = new ArrayList<>();

        // 查询首页推荐位
        LambdaQueryWrapper<RecommendSlot> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(RecommendSlot::getStatus, "0") // 正常状态
               .eq(RecommendSlot::getPage, "home") // 首页
               .orderByAsc(RecommendSlot::getOrderNum);

        List<RecommendSlot> slots = recommendSlotMapper.selectList(wrapper);
        if (slots.isEmpty()) {
            return items;
        }

        // 批量查询推荐位对应的内容
        List<Long> slotIds = slots.stream().map(RecommendSlot::getId).toList();
        LambdaQueryWrapper<RecommendItem> itemWrapper = Wrappers.lambdaQuery();
        itemWrapper.in(RecommendItem::getSlotId, slotIds)
                  .eq(RecommendItem::getStatus, "0")
                  .eq(RecommendItem::getContentType, "series")
                  .orderByAsc(RecommendItem::getOrderNum);

        List<RecommendItem> recommendItems = recommendItemMapper.selectList(itemWrapper);
        if (recommendItems.isEmpty()) {
            return items;
        }

        // 批量查询系列信息
        List<Long> seriesIds = recommendItems.stream()
                .map(RecommendItem::getContentId)
                .distinct()
                .toList();
        
        List<SeriesVo> seriesVoList = seriesMapper.selectVoList(
            Wrappers.<Series>lambdaQuery().in(Series::getId, seriesIds)
        );

        // 按推荐位顺序组织数据
        for (RecommendSlot slot : slots) {
            RecommendItem firstItem = recommendItems.stream()
                    .filter(item -> item.getSlotId().equals(slot.getId()))
                    .findFirst()
                    .orElse(null);
            
            if (firstItem != null) {
                SeriesVo seriesVo = seriesVoList.stream()
                        .filter(series -> series.getId().equals(firstItem.getContentId()))
                        .findFirst()
                        .orElse(null);
                
                if (seriesVo != null) {
                    items.add(seriesVo);
                }
            }
        }

        return items;
    }

    /**
     * 获取推荐内容
     */
    private List<TrackVo> getRecommendItems() {
        List<TrackVo> items = new ArrayList<>();

        // 查询推荐内容
        LambdaQueryWrapper<RecommendItem> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(RecommendItem::getStatus, "0")
               .eq(RecommendItem::getContentType, "track")
               .le(RecommendItem::getStartTime, new Date())
               .and(w -> w.isNull(RecommendItem::getEndTime)
                         .or()
                         .ge(RecommendItem::getEndTime, new Date()))
               .orderByAsc(RecommendItem::getOrderNum)
               .last("LIMIT 3");

        List<RecommendItem> recommendItems = recommendItemMapper.selectList(wrapper);
        
        if (recommendItems.isEmpty()) {
            return items;
        }

        // 批量查询Track信息
        List<Long> trackIds = recommendItems.stream()
                .map(RecommendItem::getContentId)
                .toList();
        
        List<TrackVo> trackVoList = trackMapper.selectVoList(
            Wrappers.<Track>lambdaQuery().in(Track::getId, trackIds)
        );

        // 按推荐顺序组织数据
        for (RecommendItem item : recommendItems) {
            TrackVo trackVo = trackVoList.stream()
                    .filter(track -> track.getId().equals(item.getContentId()))
                    .findFirst()
                    .orElse(null);
            
            if (trackVo != null) {
                items.add(trackVo);
            }
        }

        return items;
    }

    /**
     * 获取知识内容
     */
    private List<ArticleVo> getKnowledgeItems() {
        List<ArticleVo> items = new ArrayList<>();

        // 查询知识类型的推荐内容
        LambdaQueryWrapper<RecommendItem> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(RecommendItem::getStatus, "0")
               .eq(RecommendItem::getContentType, "article")
               .le(RecommendItem::getStartTime, new Date())
               .and(w -> w.isNull(RecommendItem::getEndTime)
                         .or()
                         .ge(RecommendItem::getEndTime, new Date()))
               .orderByAsc(RecommendItem::getOrderNum)
               .last("LIMIT 3");

        List<RecommendItem> recommendItems = recommendItemMapper.selectList(wrapper);
        
        if (recommendItems.isEmpty()) {
            return items;
        }

        // 批量查询Article信息
        List<Long> articleIds = recommendItems.stream()
                .map(RecommendItem::getContentId)
                .toList();
        
        List<ArticleVo> articleVoList = articleMapper.selectVoList(
            Wrappers.<Article>lambdaQuery().in(Article::getId, articleIds)
        );

        // 按推荐顺序组织数据
        for (RecommendItem item : recommendItems) {
            ArticleVo articleVo = articleVoList.stream()
                    .filter(article -> article.getId().equals(item.getContentId()))
                    .findFirst()
                    .orElse(null);
            
            if (articleVo != null) {
                items.add(articleVo);
            }
        }

        return items;
    }
}
