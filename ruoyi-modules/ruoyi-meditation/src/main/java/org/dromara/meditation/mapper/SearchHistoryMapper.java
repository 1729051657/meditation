package org.dromara.meditation.mapper;

import org.dromara.common.mybatis.annotation.DataColumn;
import org.dromara.common.mybatis.annotation.DataPermission;
import org.dromara.meditation.domain.SearchHistory;
import org.dromara.meditation.domain.vo.SearchHistoryVo;
import org.dromara.common.mybatis.core.mapper.BaseMapperPlus;

/**
 * 搜索历史Mapper接口
 *
 * @author kdc
 * @date 2025-08-14
 */
@DataPermission({
    @DataColumn(key = "userName", value = "user_id")
})
public interface SearchHistoryMapper extends BaseMapperPlus<SearchHistory, SearchHistoryVo> {

}
