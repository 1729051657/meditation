package org.dromara.meditation.domain.bo;

import org.dromara.meditation.domain.RecommendItem;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 推荐位内容业务对象 mg_recommend_item
 *
 * @author kdc
 * @date 2025-08-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = RecommendItem.class, reverseConvertGenerate = false)
public class RecommendItemBo extends BaseEntity {

    /**
     * 主键
     */
    private Long id;

    /**
     * 推荐位ID
     */
    private Long slotId;

    /**
     * 内容类型（series/track/article）
     */
    private String contentType;

    /**
     * 内容ID
     */
    private Long contentId;

    /**
     * 显示顺序
     */
    @NotNull(message = "显示顺序不能为空", groups = { AddGroup.class, EditGroup.class })
    private Integer orderNum;

    /**
     * 生效时间
     */
    @NotNull(message = "生效时间不能为空", groups = { AddGroup.class, EditGroup.class })
    private Date startTime;

    /**
     * 失效时间
     */
    @NotNull(message = "失效时间不能为空", groups = { AddGroup.class, EditGroup.class })
    private Date endTime;

    /**
     * 状态（0正常 1停用）
     */
    private String status = "0";
}
