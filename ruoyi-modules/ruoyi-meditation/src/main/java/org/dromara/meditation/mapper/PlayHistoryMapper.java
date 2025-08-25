package org.dromara.meditation.mapper;

import org.dromara.meditation.domain.PlayHistory;
import org.dromara.meditation.domain.vo.PlayHistoryVo;
import org.dromara.common.mybatis.core.mapper.BaseMapperPlus;
import org.dromara.common.mybatis.annotation.DataPermission;
import org.dromara.common.mybatis.annotation.DataColumn;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 音频播放记录Mapper接口
 *
 * @author kdc
 * @date 2025-08-14
 */
@DataPermission({
    @DataColumn(key = "userName", value = "user_id")
})
public interface PlayHistoryMapper extends BaseMapperPlus<PlayHistory, PlayHistoryVo> {

    /**
     * 查询播放历史列表（包含关联的Track信息）
     *
     * @param wrapper 查询条件
     * @return 播放历史列表
     */
    List<PlayHistoryVo> selectPlayHistoryWithTrackList(@Param(Constants.WRAPPER) LambdaQueryWrapper<PlayHistory> wrapper);

    /**
     * 分页查询播放历史列表（包含关联的Track信息）
     *
     * @param page 分页对象
     * @param wrapper 查询条件
     * @return 播放历史分页列表
     */
    Page<PlayHistoryVo> selectPlayHistoryWithTrackPage(Page<PlayHistoryVo> page, @Param(Constants.WRAPPER) LambdaQueryWrapper<PlayHistory> wrapper);

    /**
     * 根据ID查询播放历史（包含关联的Track信息）
     *
     * @param id 主键
     * @return 播放历史详情
     */
    PlayHistoryVo selectPlayHistoryWithTrackById(Long id);

}
