package org.dromara.meditation.domain;

import org.dromara.common.tenant.core.TenantEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 冥想单集对象 mg_track
 *
 * @author kdc
 * @date 2025-08-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("mg_track")
public class Track extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 所属系列id（mg_series.id）
     */
    private Long seriesId;

    /**
     * 分类id（mg_category.id）
     */
    private Long categoryId;

    /**
     * 标题
     */
    private String title;

    /**
     * 封面文件id（sys_oss.oss_id）
     */
    private Long cover;

    /**
     * 音频文件id（sys_oss.oss_id）
     */
    private Long audio;

    /**
     * 时长（秒）
     */
    private Integer durationSec;

    /**
     * 简介
     */
    private String intro;

    /**
     * 在系列内排序（从小到大）
     */
    private Integer orderIndex;

    /**
     * 状态（0正常 1停用）
     */
    private Integer status;

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
