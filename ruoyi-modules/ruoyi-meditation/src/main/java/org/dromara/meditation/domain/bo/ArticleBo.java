package org.dromara.meditation.domain.bo;

import org.dromara.meditation.domain.Article;
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
 * 冥想知识文章业务对象 mg_article
 *
 * @author kdc
 * @date 2025-08-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = Article.class, reverseConvertGenerate = false)
public class ArticleBo extends BaseEntity {

    /**
     * 主键
     */
    private Long id;

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
     * 摘要
     */
    @NotBlank(message = "摘要不能为空", groups = { AddGroup.class, EditGroup.class })
    private String summary;

    /**
     * 内容（HTML/Markdown）
     */
    @NotBlank(message = "内容（HTML/Markdown）不能为空", groups = { AddGroup.class, EditGroup.class })
    private String content;

    /**
     * 作者用户id
     */
    private Long authorId;

    /**
     * 状态（0正常 1停用）
     */
    private String status = "0";

    /**
     * 发布时间
     */
    @NotNull(message = "发布时间不能为空", groups = { AddGroup.class, EditGroup.class })
    private Date publishTime;

    /**
     * 显示顺序
     */
    private Integer orderNum;

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
