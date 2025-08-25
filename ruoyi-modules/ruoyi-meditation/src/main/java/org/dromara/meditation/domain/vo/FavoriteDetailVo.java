package org.dromara.meditation.domain.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serial;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import org.dromara.common.translation.annotation.Translation;
import org.dromara.common.translation.constant.TransConstant;

/**
 * 用户收藏详情视图对象
 * 包含收藏目标的详细信息
 *
 * @author system
 * @date 2025-01-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class FavoriteDetailVo extends FavoriteVo {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 目标标题
     */
    private String targetTitle;

    /**
     * 目标副标题
     */
    private String targetSubtitle;

    /**
     * 目标作者
     */
    private String targetAuthor;

    /**
     * 目标封面
     */
    @Translation(type = TransConstant.OSS_ID_TO_URL)
    private Long targetCover;

    /**
     * 目标横幅（系列专用）
     */
    @Translation(type = TransConstant.OSS_ID_TO_URL)
    private Long targetBanner;

    /**
     * 目标简介
     */
    private String targetIntro;

    /**
     * 目标摘要（文章专用）
     */
    private String targetSummary;

    /**
     * 时长（秒）- 单集/系列专用
     */
    private Integer targetDuration;

    /**
     * 集数 - 系列专用
     */
    private Integer episodeCount;

    /**
     * 播放次数
     */
    private Integer playCount;

    /**
     * 浏览次数 - 文章专用
     */
    private Integer viewCount;

    /**
     * 音频URL - 单集专用
     */
    @Translation(type = TransConstant.OSS_ID_TO_URL)
    private Long audioUrl;

    /**
     * 分类ID
     */
    private Long categoryId;

    /**
     * 分类名称
     */
    private String categoryName;

    /**
     * 收藏时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 状态（0正常 1停用）
     */
    private Integer status;
}