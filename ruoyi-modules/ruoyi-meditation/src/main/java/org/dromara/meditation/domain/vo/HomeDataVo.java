package org.dromara.meditation.domain.vo;

import lombok.Data;
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
    private List<CategoryItem> categories;

    /**
     * 冥想练习列表
     */
    private List<MeditationItem> meditationSlots;

    /**
     * 冥想推荐列表
     */
    private List<RecommendContentItem> recommendItems;

    /**
     * 冥想知识列表
     */
    private List<KnowledgeItem> knowledgeItems;

    /**
     * 功能分类项
     */
    @Data
    public static class CategoryItem implements Serializable {
        @Serial
        private static final long serialVersionUID = 1L;
        
        /**
         * 分类类型
         */
        private String type;
        
        /**
         * 分类名称
         */
        private String name;
        
        /**
         * 图标路径
         */
        private String icon;
    }

    /**
     * 冥想练习项
     */
    @Data
    public static class MeditationItem implements Serializable {
        @Serial
        private static final long serialVersionUID = 1L;
        
        /**
         * ID
         */
        private Long id;
        
        /**
         * 标题
         */
        private String title;
        
        /**
         * 时长（分钟）
         */
        private Integer duration;
        
        /**
         * 目标ID
         */
        private Long targetId;
        
        /**
         * 目标类型
         */
        private String targetType;
        
        /**
         * 跳转链接
         */
        private String link;
        
        /**
         * 位置/类型
         */
        private String position;
        
        /**
         * 推荐位类型
         */
        private String slotType;
    }

    /**
     * 推荐内容项
     */
    @Data
    public static class RecommendContentItem implements Serializable {
        @Serial
        private static final long serialVersionUID = 1L;
        
        /**
         * ID
         */
        private Long id;
        
        /**
         * 标题
         */
        private String title;
        
        /**
         * 封面图
         */
        private String cover;
        
        /**
         * 时长（分钟）
         */
        private Integer duration;
        
        /**
         * 目标ID
         */
        private Long targetId;
        
        /**
         * 目标类型
         */
        private String targetType;
        
        /**
         * 跳转链接
         */
        private String link;
        
        /**
         * 内容类型
         */
        private String itemType;
    }

    /**
     * 知识内容项
     */
    @Data
    public static class KnowledgeItem implements Serializable {
        @Serial
        private static final long serialVersionUID = 1L;
        
        /**
         * ID
         */
        private Long id;
        
        /**
         * 标题
         */
        private String title;
        
        /**
         * 描述
         */
        private String description;
        
        /**
         * 封面图
         */
        private String cover;
        
        /**
         * 目标ID
         */
        private Long targetId;
        
        /**
         * 跳转链接
         */
        private String link;
        
        /**
         * 内容类型
         */
        private String itemType;
    }
}
