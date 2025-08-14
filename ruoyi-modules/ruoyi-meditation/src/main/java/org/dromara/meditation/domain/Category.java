package org.dromara.meditation.domain;

import org.dromara.common.tenant.core.TenantEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 冥想分类对象 mg_category
 *
 * @author kdc
 * @date 2025-08-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("mg_category")
public class Category extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 父分类id
     */
    private Long parentId;

    /**
     * 分类名称
     */
    private String name;

    /**
     * 分类编码（英文唯一标识）
     */
    private String code;

    /**
     * 图标文件id（sys_oss.oss_id）
     */
    private Long icon;

    /**
     * 简介
     */
    private String description;

    /**
     * 显示顺序
     */
    private Integer orderNum;

    /**
     * 状态（0正常 1停用）
     */
    private String status;

    /**
     * 删除标志（0代表存在 1代表删除）
     */
    @TableLogic
    private String delFlag;

    /**
     * 备注
     */
    private String remark;


}
