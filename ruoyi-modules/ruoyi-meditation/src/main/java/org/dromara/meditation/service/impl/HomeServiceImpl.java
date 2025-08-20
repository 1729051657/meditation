package org.dromara.meditation.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.meditation.domain.Article;
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
        List<CategoryVo> categories = new ArrayList<>();

        CategoryVo relax = new CategoryVo();
        relax.setId(1L);
        relax.setName("放松减压");
        relax.setCode("relax");
        relax.setIcon(1L); // 这里应该设置实际的OSS文件ID
        relax.setStatus("0");
        relax.setOrderNum(1);
        categories.add(relax);

        CategoryVo sleep = new CategoryVo();
        sleep.setId(2L);
        sleep.setName("改善睡眠");
        sleep.setCode("sleep");
        sleep.setIcon(2L);
        sleep.setStatus("0");
        sleep.setOrderNum(2);
        categories.add(sleep);

        CategoryVo focus = new CategoryVo();
        focus.setId(3L);
        focus.setName("提升专注");
        focus.setCode("focus");
        focus.setIcon(3L);
        focus.setStatus("0");
        focus.setOrderNum(3);
        categories.add(focus);

        CategoryVo emotion = new CategoryVo();
        emotion.setId(4L);
        emotion.setName("情绪调节");
        emotion.setCode("emotion");
        emotion.setIcon(4L);
        emotion.setStatus("0");
        emotion.setOrderNum(4);
        categories.add(emotion);

        return categories;
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
               .orderByAsc(RecommendSlot::getOrderNum)
               .last("LIMIT 5");

        List<RecommendSlot> slots = recommendSlotMapper.selectList(wrapper);

        for (RecommendSlot slot : slots) {
            // 查询推荐位对应的内容
            LambdaQueryWrapper<RecommendItem> itemWrapper = Wrappers.lambdaQuery();
            itemWrapper.eq(RecommendItem::getSlotId, slot.getId())
                      .eq(RecommendItem::getStatus, "0")
                      .orderByAsc(RecommendItem::getOrderNum)
                      .last("LIMIT 1");

            RecommendItem recommendItem = recommendItemMapper.selectOne(itemWrapper);
            if (recommendItem != null && "series".equals(recommendItem.getContentType())) {
                Series series = seriesMapper.selectById(recommendItem.getContentId());
                if (series != null) {
                    SeriesVo seriesVo = new SeriesVo();
                    seriesVo.setId(series.getId());
                    seriesVo.setTitle(series.getTitle());
                    seriesVo.setSubtitle(series.getSubtitle());
                    seriesVo.setIntro(series.getIntro());
                    seriesVo.setEpisodeCount(series.getEpisodeCount());
                    seriesVo.setRecommendDuration(series.getRecommendDuration());
                    seriesVo.setOrderNum(series.getOrderNum());
                    seriesVo.setStatus(series.getStatus());
                    seriesVo.setPublishTime(series.getPublishTime());
                    
                    // 设置封面 - @Translation 注解会自动转换 OSS ID 为 URL
                    if (series.getCover() != null) {
                        seriesVo.setCover(series.getCover());
                    }
                    
                    // 设置横幅图 - @Translation 注解会自动转换 OSS ID 为 URL
                    if (series.getBanner() != null) {
                        seriesVo.setBanner(series.getBanner());
                    }
                    
                    items.add(seriesVo);
                }
            }
        }

        // 如果没有推荐位数据，添加默认数据
        if (items.isEmpty()) {
            SeriesVo item1 = new SeriesVo();
            item1.setId(1L);
            item1.setTitle("基础冥想");
            item1.setSubtitle("适合初学者的冥想练习");
            item1.setIntro("通过简单的呼吸练习和身体扫描，帮助初学者建立冥想习惯");
            item1.setEpisodeCount(10);
            item1.setRecommendDuration(600); // 10分钟
            item1.setStatus("0");
            item1.setOrderNum(1);
            items.add(item1);

            SeriesVo item2 = new SeriesVo();
            item2.setId(2L);
            item2.setTitle("进阶冥想");
            item2.setSubtitle("提升冥想深度的练习");
            item2.setIntro("在基础冥想的基础上，加入更深入的专注和觉察练习");
            item2.setEpisodeCount(15);
            item2.setRecommendDuration(900); // 15分钟
            item2.setStatus("0");
            item2.setOrderNum(2);
            items.add(item2);
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
               .le(RecommendItem::getStartTime, new Date())
               .and(w -> w.isNull(RecommendItem::getEndTime)
                         .or()
                         .ge(RecommendItem::getEndTime, new Date()))
               .orderByAsc(RecommendItem::getOrderNum)
               .last("LIMIT 3");

        List<RecommendItem> recommendItems = recommendItemMapper.selectList(wrapper);

        for (RecommendItem item : recommendItems) {
            if ("track".equals(item.getContentType())) {
                Track track = trackMapper.selectById(item.getContentId());
                if (track != null) {
                    TrackVo trackVo = new TrackVo();
                    trackVo.setId(track.getId());
                    trackVo.setTitle(track.getTitle());
                    trackVo.setIntro(track.getIntro());
                    trackVo.setDurationSec(track.getDurationSec());
                    trackVo.setOrderIndex(track.getOrderIndex());
                    trackVo.setStatus(track.getStatus());
                    trackVo.setSeriesId(track.getSeriesId());
                    trackVo.setCategoryId(track.getCategoryId());
                    
                    // 设置封面 - @Translation 注解会自动转换 OSS ID 为 URL
                    if (track.getCover() != null) {
                        trackVo.setCover(track.getCover());
                    }
                    
                    // 设置音频 - @Translation 注解会自动转换 OSS ID 为 URL
                    if (track.getAudio() != null) {
                        trackVo.setAudio(track.getAudio());
                    }
                    
                    items.add(trackVo);
                }
            }
        }

        // 如果没有推荐内容，添加默认数据
        if (items.isEmpty()) {
            for (int i = 1; i <= 3; i++) {
                TrackVo item = new TrackVo();
                item.setId((long) i);
                item.setTitle("冥想练习" + i);
                item.setIntro("放松身心，提升专注力");
                item.setDurationSec(600); // 10分钟
                item.setStatus("0");
                item.setOrderIndex(i);
                items.add(item);
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

        for (RecommendItem item : recommendItems) {
            // 查询文章内容
            Article article = articleMapper.selectById(item.getContentId());
            if (article != null) {
                ArticleVo articleVo = new ArticleVo();
                articleVo.setId(article.getId());
                articleVo.setTitle(article.getTitle());
                articleVo.setSummary(article.getSummary());
                articleVo.setContent(article.getContent());
                articleVo.setAuthorId(article.getAuthorId());
                articleVo.setStatus(article.getStatus());
                articleVo.setPublishTime(article.getPublishTime());
                articleVo.setOrderNum(article.getOrderNum());
                
                // 设置封面 - @Translation 注解会自动转换 OSS ID 为 URL
                if (article.getCover() != null) {
                    articleVo.setCover(article.getCover());
                }
                
                items.add(articleVo);
            }
        }

        // 如果没有知识内容，添加默认数据
        if (items.isEmpty()) {
            for (int i = 1; i <= 3; i++) {
                ArticleVo item = new ArticleVo();
                item.setId((long) i);
                item.setTitle("冥想知识" + i);
                item.setSummary("了解冥想的益处和技巧");
                item.setContent("冥想是一种古老的练习方法，可以帮助人们放松身心、提升专注力...");
                item.setStatus("0");
                item.setOrderNum(i);
                items.add(item);
            }
        }

        return items;
    }
}
