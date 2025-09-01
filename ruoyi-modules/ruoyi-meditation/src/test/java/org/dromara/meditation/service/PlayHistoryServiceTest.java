package org.dromara.meditation.service;

import org.dromara.meditation.domain.bo.PlayHistoryBo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 播放历史服务测试类
 * 测试upsert功能：userId+trackId重复时更新记录
 */
@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class PlayHistoryServiceTest {

    @Autowired
    private IPlayHistoryService playHistoryService;

    @Test
    public void testInsertOrUpdateByBo_NewRecord() {
        // 准备测试数据
        PlayHistoryBo bo = new PlayHistoryBo();
        bo.setUserId(1L);
        bo.setTrackId(100L);
        bo.setProgressSec(120);
        bo.setIsCompleted("N");

        // 第一次插入
        Boolean result = playHistoryService.insertOrUpdateByBo(bo);
        assertTrue(result, "第一次插入应该成功");
        assertNotNull(bo.getId(), "插入后应该有ID");
        
        Long firstId = bo.getId();
        
        // 准备第二次调用的数据（相同的userId和trackId）
        PlayHistoryBo updateBo = new PlayHistoryBo();
        updateBo.setUserId(1L);
        updateBo.setTrackId(100L);
        updateBo.setProgressSec(240); // 更新进度
        updateBo.setIsCompleted("Y"); // 更新完成状态
        
        // 第二次调用应该更新而不是插入
        Boolean updateResult = playHistoryService.insertOrUpdateByBo(updateBo);
        assertTrue(updateResult, "更新应该成功");
        assertEquals(firstId, updateBo.getId(), "应该是更新同一条记录");
    }

    @Test
    public void testInsertOrUpdateByBo_DifferentTracks() {
        // 测试同一用户不同音轨
        PlayHistoryBo bo1 = new PlayHistoryBo();
        bo1.setUserId(1L);
        bo1.setTrackId(101L);
        bo1.setProgressSec(60);
        
        PlayHistoryBo bo2 = new PlayHistoryBo();
        bo2.setUserId(1L);
        bo2.setTrackId(102L);
        bo2.setProgressSec(90);
        
        Boolean result1 = playHistoryService.insertOrUpdateByBo(bo1);
        Boolean result2 = playHistoryService.insertOrUpdateByBo(bo2);
        
        assertTrue(result1, "第一个音轨应该插入成功");
        assertTrue(result2, "第二个音轨应该插入成功");
        assertNotEquals(bo1.getId(), bo2.getId(), "不同音轨应该是不同的记录");
    }

    @Test
    public void testInsertOrUpdateByBo_DifferentUsers() {
        // 测试不同用户相同音轨
        PlayHistoryBo bo1 = new PlayHistoryBo();
        bo1.setUserId(1L);
        bo1.setTrackId(103L);
        bo1.setProgressSec(30);
        
        PlayHistoryBo bo2 = new PlayHistoryBo();
        bo2.setUserId(2L);
        bo2.setTrackId(103L);
        bo2.setProgressSec(45);
        
        Boolean result1 = playHistoryService.insertOrUpdateByBo(bo1);
        Boolean result2 = playHistoryService.insertOrUpdateByBo(bo2);
        
        assertTrue(result1, "用户1应该插入成功");
        assertTrue(result2, "用户2应该插入成功");
        assertNotEquals(bo1.getId(), bo2.getId(), "不同用户应该是不同的记录");
    }
}