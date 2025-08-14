package org.dromara.meditation.domain;

import org.dromara.common.tenant.core.TenantEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serial;

/**
 * 冥想知识文章对象 mg_article
 *
 * @author kdc
 * @date 2025-08-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("mg_article")
public class Article extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 标题
     */
    private String title;

    /**
     * 封面文件id（sys_oss.oss_id）
     */
    private Long cover;

    /**
     * 摘要
     */
    private String summary;

    /**
     * 内容（HTML/Markdown）
     */
    private String content;

    /**
     * 作者用户id
     */
    private Long authorId;

    /**
     * 状态（0正常 1停用）
     */
    private String status;

    /**
     * 发布时间
     */
    private Date publishTime;

    /**
     * 显示顺序
     */
    private Integer orderNum;

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
