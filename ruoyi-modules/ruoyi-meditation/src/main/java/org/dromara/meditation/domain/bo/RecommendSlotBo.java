package org.dromara.meditation.domain.bo;

import org.dromara.meditation.domain.RecommendSlot;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;

/**
 * 推荐位业务对象 mg_recommend_slot
 *
 * @author kdc
 * @date 2025-08-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = RecommendSlot.class, reverseConvertGenerate = false)
public class RecommendSlotBo extends BaseEntity {

    /**
     * 主键
     */
    private Long id;

    /**
     * 推荐位编码
     */
    private String code;

    /**
     * 推荐位名称
     */
    private String name;

    /**
     * 所属页面（home/category/search等）
     */
    @NotBlank(message = "所属页面（home/category/search等）不能为空", groups = { AddGroup.class, EditGroup.class })
    private String page;

    /**
     * 状态（0正常 1停用）
     */
    private String status = "0";

    /**
     * 显示顺序
     */
    @NotNull(message = "显示顺序不能为空", groups = { AddGroup.class, EditGroup.class })
    private Integer orderNum;

    /**
     * 备注
     */
    @NotBlank(message = "备注不能为空", groups = { AddGroup.class, EditGroup.class })
    private String remark;


}
