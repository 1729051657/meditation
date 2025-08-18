package org.dromara.meditation.domain.bo;

import org.dromara.meditation.domain.HotKeyword;
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
 * 热搜关键词业务对象 mg_hot_keyword
 *
 * @author kdc
 * @date 2025-08-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = HotKeyword.class, reverseConvertGenerate = false)
public class HotKeywordBo extends BaseEntity {

    /**
     * 主键
     */
    private Long id;

    /**
     * 热搜词
     */
    private String keyword;

    /**
     * 排序
     */
    @NotNull(message = "排序不能为空", groups = { AddGroup.class, EditGroup.class })
    private Integer orderNum;

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

    /**
     * 状态（0正常 1停用）
     */
    private String status = "0";


}
