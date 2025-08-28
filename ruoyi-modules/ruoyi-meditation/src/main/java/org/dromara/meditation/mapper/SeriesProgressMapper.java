package org.dromara.meditation.mapper;

import org.dromara.common.mybatis.annotation.DataColumn;
import org.dromara.common.mybatis.annotation.DataPermission;
import org.dromara.meditation.domain.SeriesProgress;
import org.dromara.meditation.domain.vo.SeriesProgressVo;
import org.dromara.common.mybatis.core.mapper.BaseMapperPlus;

/**
 * 系列学习进度Mapper接口
 *
 * @author kdc
 * @date 2025-08-14
 */
@DataPermission({
    @DataColumn(key = "userName", value = "user_id")
})
public interface SeriesProgressMapper extends BaseMapperPlus<SeriesProgress, SeriesProgressVo> {

}
