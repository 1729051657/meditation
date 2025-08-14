package org.dromara.meditation.domain;

import org.dromara.common.tenant.core.TenantEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serial;

/**
 * 冥想系列对象 mg_series
 *
 * @author kdc
 * @date 2025-08-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("mg_series")
public class Series extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 分类id（mg_category.id）
     */
    private Long categoryId;

    /**
     * 标题
     */
    private String title;

    /**
     * 副标题
     */
    private String subtitle;

    /**
     * 封面文件id（sys_oss.oss_id）
     */
    private Long cover;

    /**
     * 横幅图文件id（sys_oss.oss_id）
     */
    private Long banner;

    /**
     * 简介
     */
    private String intro;

    /**
     * 小节数
     */
    private Integer episodeCount;

    /**
     * 建议时长（秒）
     */
    private Integer recommendDuration;

    /**
     * 是否免费（Y是 N否）
     */
    private String isFree;

    /**
     * 显示顺序
     */
    private Integer orderNum;

    /**
     * 状态（0正常 1停用）
     */
    private String status;

    /**
     * 发布时间
     */
    private Date publishTime;

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
