package org.dromara.meditation.domain.vo;

import lombok.Data;
import java.io.Serial;
import java.io.Serializable;

/**
 * FAQ问题视图对象
 *
 * @author system
 * @date 2024-01-10
 */
@Data
public class FaqQuestionVo implements Serializable {
    
    @Serial
    private static final long serialVersionUID = 1L;
    
    /**
     * 问题ID
     */
    private String id;
    
    /**
     * 问题标题
     */
    private String question;
    
    /**
     * 答案内容
     */
    private String answer;
    
    /**
     * 排序号
     */
    private Integer sort;
    
    /**
     * 是否展开（前端使用）
     */
    private Boolean expanded = false;
}