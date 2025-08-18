package org.dromara.meditation.domain.bo;

import org.dromara.meditation.domain.Category;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;

/**
 * 冥想分类业务对象 mg_category
 *
 * @author kdc
 * @date 2025-08-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = Category.class, reverseConvertGenerate = false)
public class CategoryBo extends BaseEntity {

    /**
     * 主键
     */
    private Long id;

    /**
     * 父分类id
     */
    @NotNull(message = "父分类id不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long parentId;

    /**
     * 分类名称
     */
    private String name;

    /**
     * 分类编码（英文唯一标识）
     */
    @NotBlank(message = "分类编码（英文唯一标识）不能为空", groups = { AddGroup.class, EditGroup.class })
    private String code;

    /**
     * 图标文件id（sys_oss.oss_id）
     */
    @NotNull(message = "图标文件id（sys_oss.oss_id）不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long icon;

    /**
     * 简介
     */
    @NotBlank(message = "简介不能为空", groups = { AddGroup.class, EditGroup.class })
    private String description;

    /**
     * 显示顺序
     */
    @NotNull(message = "显示顺序不能为空", groups = { AddGroup.class, EditGroup.class })
    private Integer orderNum;

    /**
     * 状态（0正常 1停用）
     */
    private String status = "0";

    /**
     * 备注
     */
    @NotBlank(message = "备注不能为空", groups = { AddGroup.class, EditGroup.class })
    private String remark;


}
