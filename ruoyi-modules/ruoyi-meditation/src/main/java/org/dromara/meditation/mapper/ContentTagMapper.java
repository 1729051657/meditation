package org.dromara.meditation.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.dromara.meditation.domain.ContentTag;
import org.dromara.meditation.domain.vo.ContentTagVo;
import org.dromara.common.mybatis.core.mapper.BaseMapperPlus;

/**
 * 内容-标签关联Mapper接口
 *
 * @author kdc
 * @date 2025-08-14
 */
public interface ContentTagMapper extends BaseMapperPlus<ContentTag, ContentTagVo> {

    /**
     * 物理删除指定内容的所有标签关联
     */
    @Delete("DELETE FROM mg_content_tag WHERE tenant_id = #{tenantId} AND content_type = #{contentType} AND content_id = #{contentId}")
    int deletePhysicalByContent(@Param("tenantId") String tenantId,
                                @Param("contentType") String contentType,
                                @Param("contentId") Long contentId);
}
