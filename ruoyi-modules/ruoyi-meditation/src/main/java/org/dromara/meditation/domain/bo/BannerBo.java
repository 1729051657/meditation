package org.dromara.meditation.domain.bo;

import org.dromara.meditation.domain.Banner;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;
import org.dromara.common.translation.annotation.Translation;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.dromara.common.translation.constant.TransConstant;

/**
 * 横幅配置业务对象 mg_banner
 *
 * @author kdc
 * @date 2025-08-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = Banner.class, reverseConvertGenerate = false)
public class BannerBo extends BaseEntity {

    /**
     * 主键
     */
    private Long id;

    /**
     * 所属页面（home等）
     */
    @NotBlank(message = "所属页面（home等）不能为空", groups = { AddGroup.class, EditGroup.class })
    private String page;

    /**
     * 图片文件id（sys_oss.oss_id）
     */
    private Long image;

    /**
     * 链接类型（0无 1外链URL 2内容跳转）
     */
    @NotBlank(message = "链接类型（0无 1外链URL 2内容跳转）不能为空", groups = { AddGroup.class, EditGroup.class })
    private String linkType;

    /**
     * 链接目标（URL或内容定位）
     */
    @NotBlank(message = "链接目标（URL或内容定位）不能为空", groups = { AddGroup.class, EditGroup.class })
    private String linkTarget;

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
     * 生效时间
     */
    @NotNull(message = "生效时间不能为空", groups = { AddGroup.class, EditGroup.class })
    private Date startTime;

    /**
     * 失效时间
     */
    @NotNull(message = "失效时间不能为空", groups = { AddGroup.class, EditGroup.class })
    private Date endTime;


}
