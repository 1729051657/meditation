package org.dromara.meditation.domain.bo;

import org.dromara.meditation.domain.Favorite;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;

/**
 * 用户收藏业务对象 mg_favorite
 *
 * @author kdc
 * @date 2025-08-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = Favorite.class, reverseConvertGenerate = false)
public class FavoriteBo extends BaseEntity {

    /**
     * 主键
     */
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 目标类型（series/track/article）
     */
    private String targetType;

    /**
     * 目标ID
     */
    private Long targetId;


}
