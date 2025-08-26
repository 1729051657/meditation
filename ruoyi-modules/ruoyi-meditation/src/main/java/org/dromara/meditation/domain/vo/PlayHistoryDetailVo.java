package org.dromara.meditation.domain.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serial;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import org.dromara.common.translation.annotation.Translation;
import org.dromara.common.translation.constant.TransConstant;

/**
 * 播放历史详情视图对象
 * 包含单集的详细信息
 *
 * @author system
 * @date 2025-01-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PlayHistoryDetailVo extends PlayHistoryVo {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 单集标题
     */
    private String trackTitle;

    /**
     * 单集副标题
     */
    private String trackSubtitle;

    /**
     * 单集作者
     */
    private String trackAuthor;

    /**
     * 单集封面ID
     */
    private Long trackCover;

    /**
     * 单集封面URL
     */
    @Translation(type = TransConstant.OSS_ID_TO_URL, mapper = "trackCover")
    private String trackCoverUrl;

    /**
     * 单集简介
     */
    private String trackIntro;

    /**
     * 音频ID
     */
    private Long audio;

    /**
     * 音频URL
     */
    @Translation(type = TransConstant.OSS_ID_TO_URL, mapper = "audio")
    private String audioUrl;

    /**
     * 总时长（秒）
     */
    private Integer totalDuration;

    /**
     * 播放进度百分比
     */
    private Double progressPercent;

    /**
     * 所属系列ID
     */
    private Long seriesId;

    /**
     * 所属系列名称
     */
    private String seriesTitle;

    /**
     * 分类ID
     */
    private Long categoryId;

    /**
     * 分类名称
     */
    private String categoryName;

    /**
     * 单集排序
     */
    private Integer orderIndex;

    /**
     * 单集状态（0正常 1停用）
     */
    private Integer status;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
}