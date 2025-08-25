package org.dromara.meditation.mapper;

import org.dromara.meditation.domain.Favorite;
import org.dromara.meditation.domain.vo.FavoriteVo;
import org.dromara.common.mybatis.core.mapper.BaseMapperPlus;
import org.dromara.common.mybatis.annotation.DataPermission;
import org.dromara.common.mybatis.annotation.DataColumn;

/**
 * 用户收藏Mapper接口
 *
 * @author kdc
 * @date 2025-08-14
 */
@DataPermission({
    @DataColumn(key = "userName", value = "user_id")
})
public interface FavoriteMapper extends BaseMapperPlus<Favorite, FavoriteVo> {

}
