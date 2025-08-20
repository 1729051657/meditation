package org.dromara.meditation.domain.vo;

import lombok.Data;
import org.dromara.common.translation.annotation.Translation;
import org.dromara.common.translation.constant.TransConstant;
import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 首页数据视图对象
 *
 * @author system
 * @date 2025-01-01
 */
@Data
public class HomeDataVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 问候语
     */
    private String greeting;

    /**
     * 功能分类列表
     */
    private List<CategoryVo> categoryVoList;

    /**
     * 冥想练习列表
     */
    private List<SeriesVo> seriesVoList;

    /**
     * 冥想推荐列表
     */
    private List<TrackVo> trackVoList;

    /**
     * 冥想知识列表
     */
    private List<ArticleVo> articleVoList;
}
