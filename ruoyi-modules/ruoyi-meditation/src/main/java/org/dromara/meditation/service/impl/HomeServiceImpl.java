package org.dromara.meditation.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.meditation.domain.RecommendItem;
import org.dromara.meditation.domain.RecommendSlot;
import org.dromara.meditation.domain.vo.HomeDataVo;
import org.dromara.meditation.domain.vo.HomeDataVo.*;
import org.dromara.meditation.mapper.RecommendItemMapper;
import org.dromara.meditation.mapper.RecommendSlotMapper;
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
        homeData.setCategories(getCategories());

        // 获取冥想练习推荐位
        homeData.setMeditationSlots(getMeditationSlots());

        // 获取推荐内容
        homeData.setRecommendItems(getRecommendItems());

        // 获取知识内容
        homeData.setKnowledgeItems(getKnowledgeItems());

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
    private List<CategoryItem> getCategories() {
        List<CategoryItem> categories = new ArrayList<>();

        CategoryItem relax = new CategoryItem();
        relax.setType("relax");
        relax.setName("放松减压");
        relax.setIcon("/static/home/relax-stress@2x.png");
        categories.add(relax);

        CategoryItem sleep = new CategoryItem();
        sleep.setType("sleep");
        sleep.setName("改善睡眠");
        sleep.setIcon("/static/home/improve-sleep@2x.png");
        categories.add(sleep);

        CategoryItem focus = new CategoryItem();
        focus.setType("focus");
        focus.setName("提升专注");
        focus.setIcon("/static/home/improve-focus@2x.png");
        categories.add(focus);

        CategoryItem emotion = new CategoryItem();
        emotion.setType("emotion");
        emotion.setName("情绪调节");
        emotion.setIcon("/static/home/emotion-regulation@2x.png");
        categories.add(emotion);

        return categories;
    }

    /**
     * 获取冥想练习推荐位
     */
    private List<MeditationItem> getMeditationSlots() {
        List<MeditationItem> items = new ArrayList<>();

        // 查询首页推荐位
        LambdaQueryWrapper<RecommendSlot> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(RecommendSlot::getStatus, "0") // 正常状态
               .eq(RecommendSlot::getPage, "home") // 首页
               .orderByAsc(RecommendSlot::getOrderNum)
               .last("LIMIT 5");

        List<RecommendSlot> slots = recommendSlotMapper.selectList(wrapper);

        for (RecommendSlot slot : slots) {
            MeditationItem item = new MeditationItem();
            item.setId(slot.getId());
            item.setTitle(slot.getName());
            item.setDuration(10); // 默认10分钟
            item.setPosition("meditation");
            item.setSlotType("meditation");

            // 查询推荐位对应的内容
            LambdaQueryWrapper<RecommendItem> itemWrapper = Wrappers.lambdaQuery();
            itemWrapper.eq(RecommendItem::getSlotId, slot.getId())
                      .eq(RecommendItem::getStatus, "0")
                      .orderByAsc(RecommendItem::getOrderNum)
                      .last("LIMIT 1");

            RecommendItem recommendItem = recommendItemMapper.selectOne(itemWrapper);
            if (recommendItem != null) {
                item.setTargetId(recommendItem.getContentId());
                item.setTargetType(recommendItem.getContentType());
            }

            items.add(item);
        }

        // 如果没有推荐位数据，添加默认数据
        if (items.isEmpty()) {
            MeditationItem item1 = new MeditationItem();
            item1.setId(1L);
            item1.setTitle("基础冥想");
            item1.setDuration(10);
            item1.setPosition("meditation");
            item1.setSlotType("meditation");
            items.add(item1);

            MeditationItem item2 = new MeditationItem();
            item2.setId(2L);
            item2.setTitle("进阶冥想");
            item2.setDuration(15);
            item2.setPosition("meditation");
            item2.setSlotType("meditation");
            items.add(item2);
        }

        return items;
    }

    /**
     * 获取推荐内容
     */
    private List<RecommendContentItem> getRecommendItems() {
        List<RecommendContentItem> items = new ArrayList<>();

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
            RecommendContentItem contentItem = new RecommendContentItem();
            contentItem.setId(item.getId());
            contentItem.setTitle("清理思绪"); // 默认标题，实际应该从关联内容获取
            contentItem.setCover("/static/images/default-cover.png");
            contentItem.setDuration(10);
            contentItem.setTargetId(item.getContentId());
            contentItem.setTargetType(item.getContentType());
            contentItem.setItemType("recommend");
            items.add(contentItem);
        }

        // 如果没有推荐内容，添加默认数据
        if (items.isEmpty()) {
            for (int i = 1; i <= 3; i++) {
                RecommendContentItem item = new RecommendContentItem();
                item.setId((long) i);
                item.setTitle("冥想练习" + i);
                item.setCover("/static/images/default-cover.png");
                item.setDuration(10);
                item.setItemType("recommend");
                items.add(item);
            }
        }

        return items;
    }

    /**
     * 获取知识内容
     */
    private List<KnowledgeItem> getKnowledgeItems() {
        List<KnowledgeItem> items = new ArrayList<>();

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
            KnowledgeItem knowledgeItem = new KnowledgeItem();
            knowledgeItem.setId(item.getId());
            knowledgeItem.setTitle("冥想知识"); // 默认标题，实际应该从关联内容获取
            knowledgeItem.setDescription("了解冥想的益处和技巧");
            knowledgeItem.setCover("/static/images/default-knowledge.png");
            knowledgeItem.setTargetId(item.getContentId());
            knowledgeItem.setItemType("knowledge");
            items.add(knowledgeItem);
        }

        // 如果没有知识内容，添加默认数据
        if (items.isEmpty()) {
            for (int i = 1; i <= 3; i++) {
                KnowledgeItem item = new KnowledgeItem();
                item.setId((long) i);
                item.setTitle("冥想知识" + i);
                item.setDescription("了解冥想的益处和技巧" + i);
                item.setCover("/static/images/default-knowledge.png");
                item.setItemType("knowledge");
                items.add(item);
            }
        }

        return items;
    }
}
