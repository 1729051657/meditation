package org.dromara.meditation.domain.bo;

import org.dromara.meditation.domain.PlayHistory;
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
 * 音频播放记录业务对象 mg_play_history
 *
 * @author kdc
 * @date 2025-08-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = PlayHistory.class, reverseConvertGenerate = false)
public class PlayHistoryBo extends BaseEntity {

    /**
     * 主键
     */
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 单集ID（mg_track.id）
     */
    private Long trackId;

    /**
     * 已播放时长（秒）
     */
    @NotNull(message = "已播放时长（秒）不能为空", groups = { AddGroup.class, EditGroup.class })
    private Integer progressSec;

    /**
     * 最后播放时间
     */
    @NotNull(message = "最后播放时间不能为空", groups = { AddGroup.class, EditGroup.class })
    private Date lastPlayTime;

    /**
     * 播放次数
     */
    @NotNull(message = "播放次数不能为空", groups = { AddGroup.class, EditGroup.class })
    private Integer playCount;

    /**
     * 是否已听完（Y是 N否）
     */
    @NotBlank(message = "是否已听完（Y是 N否）不能为空", groups = { AddGroup.class, EditGroup.class })
    private String isCompleted;


}
