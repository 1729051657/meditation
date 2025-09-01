package org.dromara.meditation.domain.vo;

import lombok.Data;
import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * FAQ页面数据视图对象
 *
 * @author system
 * @date 2024-01-10
 */
@Data
public class FaqDataVo implements Serializable {
    
    @Serial
    private static final long serialVersionUID = 1L;
    
    /**
     * 联系信息
     */
    private ContactInfoVo contactInfo;
    
    /**
     * FAQ问题列表
     */
    private List<FaqQuestionVo> questions;
}