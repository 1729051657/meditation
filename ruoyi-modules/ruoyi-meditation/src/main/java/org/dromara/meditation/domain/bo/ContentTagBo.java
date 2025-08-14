package org.dromara.meditation.domain.bo;

import org.dromara.meditation.domain.ContentTag;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;

/**
 * 内容-标签关联业务对象 mg_content_tag
 *
 * @author kdc
 * @date 2025-08-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = ContentTag.class, reverseConvertGenerate = false)
public class ContentTagBo extends BaseEntity {

    /**
     * 主键
     */
    private Long id;

    /**
     * 内容类型（series/track/article）
     */
    private String contentType;

    /**
     * 内容ID
     */
    private Long contentId;

    /**
     * 标签ID
     */
    private Long tagId;


}
