package org.dromara.meditation.domain.bo;

import org.dromara.meditation.domain.SeriesProgress;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;

/**
 * 系列学习进度业务对象 mg_series_progress
 *
 * @author kdc
 * @date 2025-08-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = SeriesProgress.class, reverseConvertGenerate = false)
public class SeriesProgressBo extends BaseEntity {

    /**
     * 主键
     */
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 系列ID（mg_series.id）
     */
    private Long seriesId;

    /**
     * 最近播放的单集ID（mg_track.id）
     */
    @NotNull(message = "最近播放的单集ID（mg_track.id）不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long lastTrackId;

    /**
     * 已完成小节数
     */
    @NotNull(message = "已完成小节数不能为空", groups = { AddGroup.class, EditGroup.class })
    private Integer completedCount;


}
