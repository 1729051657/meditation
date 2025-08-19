package org.dromara.meditation.domain.bo;

import org.dromara.meditation.domain.Track;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;

/**
 * 冥想单集业务对象 mg_track
 *
 * @author kdc
 * @date 2025-08-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = Track.class, reverseConvertGenerate = false)
public class TrackBo extends BaseEntity {

    /**
     * 主键
     */
    private Long id;

    /**
     * 所属系列id（mg_series.id）
     */
    @NotNull(message = "所属系列id（mg_series.id）不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long seriesId;

    /**
     * 分类id（mg_category.id）
     */
    @NotNull(message = "分类id（mg_category.id）不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long categoryId;

    /**
     * 标题
     */
    @NotBlank(message = "标题不能为空", groups = { AddGroup.class, EditGroup.class })
    private String title;

    /**
     * 封面文件id（sys_oss.oss_id）
     */
    @NotNull(message = "封面文件id（sys_oss.oss_id）不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long cover;

    /**
     * 音频文件id（sys_oss.oss_id）
     */
    private Long audio;

    /**
     * 时长（秒）
     */
    @NotNull(message = "时长（秒）不能为空", groups = { AddGroup.class, EditGroup.class })
    private Integer durationSec;

    /**
     * 简介
     */
    @NotBlank(message = "简介不能为空", groups = { AddGroup.class, EditGroup.class })
    private String intro;

    /**
     * 在系列内排序（从小到大）
     */
    @NotNull(message = "在系列内排序（从小到大）不能为空", groups = { AddGroup.class, EditGroup.class })
    private Integer orderIndex;



    /**
     * 状态（0正常 1停用）
     */
    @NotBlank(message = "状态不能为空", groups = { AddGroup.class, EditGroup.class })
    private String status = "0";

    /**
     * 备注
     */
    @NotBlank(message = "备注不能为空", groups = { AddGroup.class, EditGroup.class })
    private String remark;


}
