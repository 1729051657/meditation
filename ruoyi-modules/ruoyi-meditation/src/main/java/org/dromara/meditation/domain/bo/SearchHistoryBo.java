package org.dromara.meditation.domain.bo;

import org.dromara.meditation.domain.SearchHistory;
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
 * 搜索历史业务对象 mg_search_history
 *
 * @author kdc
 * @date 2025-08-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = SearchHistory.class, reverseConvertGenerate = false)
public class SearchHistoryBo extends BaseEntity {

    /**
     * 主键
     */
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 搜索关键字
     */
    private String keyword;

    /**
     * 搜索次数
     */
    @NotNull(message = "搜索次数不能为空", groups = { AddGroup.class, EditGroup.class })
    private Integer times;

    /**
     * 最后搜索时间
     */
    @NotNull(message = "最后搜索时间不能为空", groups = { AddGroup.class, EditGroup.class })
    private Date lastTime;


}
