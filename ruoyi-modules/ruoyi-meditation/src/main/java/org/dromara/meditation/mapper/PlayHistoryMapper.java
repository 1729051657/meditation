package org.dromara.meditation.mapper;

import org.dromara.meditation.domain.PlayHistory;
import org.dromara.meditation.domain.vo.PlayHistoryVo;
import org.dromara.common.mybatis.core.mapper.BaseMapperPlus;
import org.dromara.common.mybatis.annotation.DataPermission;
import org.dromara.common.mybatis.annotation.DataColumn;

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

}
