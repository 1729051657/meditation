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
import java.util.List;
import com.baomidou.mybatisplus.annotation.TableField;

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
    @NotBlank(message = "标题不能为空", groups = { AddGroup.class, EditGroup.class })
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
     * 显示顺序
     */
    private Integer orderNum;

    /**
     * 状态（0正常 1停用）
     */
    private String status = "0";

    /**
     * 发布时间
     */
    private Date publishTime;

    /**
     * 备注
     */
    private String remark;

    /**
     * 标签ID列表
     */
    @TableField(exist = false)
    private List<Long> tagIds;

}
