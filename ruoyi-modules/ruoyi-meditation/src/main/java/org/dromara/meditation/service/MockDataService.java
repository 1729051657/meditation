package org.dromara.meditation.service;

import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.meditation.domain.vo.RecommendSlotVo;
import org.dromara.meditation.domain.vo.RecommendItemVo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 模拟数据服务
 * 用于生成首页展示的模拟数据
 * 
 * @author system
 * @date 2025-01-01
 */
@Service
public class MockDataService {

    /**
     * 生成推荐位模拟数据
     */
    public TableDataInfo<RecommendSlotVo> generateMockSlots() {
        List<RecommendSlotVo> slots = new ArrayList<>();
        
        // 冥想练习推荐位
        RecommendSlotVo slot1 = new RecommendSlotVo();
        slot1.setId(1L);
        slot1.setCode("meditation_basic");
        slot1.setName("基础冥想");
        slot1.setPage("home");
        slot1.setStatus("0");
        slot1.setOrderNum(1);
        slot1.setRemark("适合初学者的基础冥想练习");
        slots.add(slot1);
        
        RecommendSlotVo slot2 = new RecommendSlotVo();
        slot2.setId(2L);
        slot2.setCode("meditation_advanced");
        slot2.setName("进阶冥想");
        slot2.setPage("home");
        slot2.setStatus("0");
        slot2.setOrderNum(2);
        slot2.setRemark("适合有经验者的深度冥想");
        slots.add(slot2);
        
        // 推荐内容推荐位
        RecommendSlotVo slot3 = new RecommendSlotVo();
        slot3.setId(3L);
        slot3.setCode("recommend_daily");
        slot3.setName("每日推荐");
        slot3.setPage("home");
        slot3.setStatus("0");
        slot3.setOrderNum(3);
        slot3.setRemark("精选每日推荐内容");
        slots.add(slot3);
        
        // 知识文章推荐位
        RecommendSlotVo slot4 = new RecommendSlotVo();
        slot4.setId(4L);
        slot4.setCode("knowledge_articles");
        slot4.setName("冥想知识");
        slot4.setPage("home");
        slot4.setStatus("0");
        slot4.setOrderNum(4);
        slot4.setRemark("冥想相关知识文章");
        slots.add(slot4);
        
        TableDataInfo<RecommendSlotVo> result = new TableDataInfo<>();
        result.setCode(200);
        result.setMsg("查询成功");
        result.setRows(slots);
        result.setTotal(slots.size());
        return result;
    }
    
    /**
     * 生成推荐内容模拟数据
     */
    public TableDataInfo<RecommendItemVo> generateMockItems() {
        List<RecommendItemVo> items = new ArrayList<>();
        
        // 冥想练习内容
        RecommendItemVo item1 = new RecommendItemVo();
        item1.setId(1L);
        item1.setSlotId(1L);
        item1.setContentType("meditation");
        item1.setContentId(101L);
        item1.setOrderNum(1);
        item1.setStatus("0");
        item1.setStartTime(new Date());
        items.add(item1);
        
        RecommendItemVo item2 = new RecommendItemVo();
        item2.setId(2L);
        item2.setSlotId(2L);
        item2.setContentType("meditation");
        item2.setContentId(102L);
        item2.setOrderNum(2);
        item2.setStatus("0");
        item2.setStartTime(new Date());
        items.add(item2);
        
        // 推荐内容
        RecommendItemVo item3 = new RecommendItemVo();
        item3.setId(3L);
        item3.setSlotId(3L);
        item3.setContentType("series");
        item3.setContentId(201L);
        item3.setOrderNum(1);
        item3.setStatus("0");
        item3.setStartTime(new Date());
        items.add(item3);
        
        RecommendItemVo item4 = new RecommendItemVo();
        item4.setId(4L);
        item4.setSlotId(3L);
        item4.setContentType("track");
        item4.setContentId(202L);
        item4.setOrderNum(2);
        item4.setStatus("0");
        item4.setStartTime(new Date());
        items.add(item4);
        
        RecommendItemVo item5 = new RecommendItemVo();
        item5.setId(5L);
        item5.setSlotId(3L);
        item5.setContentType("series");
        item5.setContentId(203L);
        item5.setOrderNum(3);
        item5.setStatus("0");
        item5.setStartTime(new Date());
        items.add(item5);
        
        // 知识文章内容
        RecommendItemVo item6 = new RecommendItemVo();
        item6.setId(6L);
        item6.setSlotId(4L);
        item6.setContentType("article");
        item6.setContentId(301L);
        item6.setOrderNum(1);
        item6.setStatus("0");
        item6.setStartTime(new Date());
        items.add(item6);
        
        RecommendItemVo item7 = new RecommendItemVo();
        item7.setId(7L);
        item7.setSlotId(4L);
        item7.setContentType("article");
        item7.setContentId(302L);
        item7.setOrderNum(2);
        item7.setStatus("0");
        item7.setStartTime(new Date());
        items.add(item7);
        
        RecommendItemVo item8 = new RecommendItemVo();
        item8.setId(8L);
        item8.setSlotId(4L);
        item8.setContentType("article");
        item8.setContentId(303L);
        item8.setOrderNum(3);
        item8.setStatus("0");
        item8.setStartTime(new Date());
        items.add(item8);
        
        TableDataInfo<RecommendItemVo> result = new TableDataInfo<>();
        result.setCode(200);
        result.setMsg("查询成功");
        result.setRows(items);
        result.setTotal(items.size());
        return result;
    }
}