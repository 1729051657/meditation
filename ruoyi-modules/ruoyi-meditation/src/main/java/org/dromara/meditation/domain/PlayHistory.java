package org.dromara.meditation.domain;

import org.dromara.common.tenant.core.TenantEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serial;

/**
 * 音频播放记录对象 mg_play_history
 *
 * @author kdc
 * @date 2025-08-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("mg_play_history")
public class PlayHistory extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id")
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
    private Integer progressSec;

    /**
     * 最后播放时间
     */
    private Date lastPlayTime;

    /**
     * 播放次数
     */
    private Integer playCount;

    /**
     * 是否已听完（Y是 N否）
     */
    private String isCompleted;

    /**
     * 删除标志（0代表存在 1代表删除）
     */
    @TableLogic
    private String delFlag;


}
