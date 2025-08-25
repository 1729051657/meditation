package org.dromara.meditation.domain.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serial;

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
     * 播放次数统计
     */
    private Long playCount;

    /**
     * 浏览次数统计
     */
    private Long viewCount;
}