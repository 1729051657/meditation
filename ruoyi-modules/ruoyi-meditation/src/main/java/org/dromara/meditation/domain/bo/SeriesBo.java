package org.dromara.meditation.domain.bo;

import org.dromara.meditation.domain.Series;
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
 * 冥想系列业务对象 mg_series
 *
 * @author kdc
 * @date 2025-08-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = Series.class, reverseConvertGenerate = false)
public class SeriesBo extends BaseEntity {

    /**
     * 主键
     */
    private Long id;

    /**
     * 分类id（mg_category.id）
     */
    @NotNull(message = "分类id（mg_category.id）不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long categoryId;

    /**
     * 标题
     */
    private String title;

    /**
     * 副标题
     */
    @NotBlank(message = "副标题不能为空", groups = { AddGroup.class, EditGroup.class })
    private String subtitle;

    /**
     * 封面文件id（sys_oss.oss_id）
     */
    @NotNull(message = "封面文件id（sys_oss.oss_id）不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long cover;

    /**
     * 横幅图文件id（sys_oss.oss_id）
     */
    @NotNull(message = "横幅图文件id（sys_oss.oss_id）不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long banner;

    /**
     * 简介
     */
    @NotBlank(message = "简介不能为空", groups = { AddGroup.class, EditGroup.class })
    private String intro;

    /**
     * 小节数
     */
    @NotNull(message = "小节数不能为空", groups = { AddGroup.class, EditGroup.class })
    private Integer episodeCount;

    /**
     * 建议时长（秒）
     */
    @NotNull(message = "建议时长（秒）不能为空", groups = { AddGroup.class, EditGroup.class })
    private Integer recommendDuration;

    /**
     * 是否免费（Y是 N否）
     */
    @NotBlank(message = "是否免费（Y是 N否）不能为空", groups = { AddGroup.class, EditGroup.class })
    private String isFree;

    /**
     * 显示顺序
     */
    @NotNull(message = "显示顺序不能为空", groups = { AddGroup.class, EditGroup.class })
    private Integer orderNum;

    /**
     * 状态（0正常 1停用）
     */
    @NotBlank(message = "状态（0正常 1停用）不能为空", groups = { AddGroup.class, EditGroup.class })
    private String status;

    /**
     * 发布时间
     */
    @NotNull(message = "发布时间不能为空", groups = { AddGroup.class, EditGroup.class })
    private Date publishTime;

    /**
     * 备注
     */
    @NotBlank(message = "备注不能为空", groups = { AddGroup.class, EditGroup.class })
    private String remark;


}
