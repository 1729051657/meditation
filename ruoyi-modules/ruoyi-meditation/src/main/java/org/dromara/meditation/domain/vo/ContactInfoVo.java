package org.dromara.meditation.domain.vo;

import lombok.Data;
import java.io.Serial;
import java.io.Serializable;

/**
 * 联系信息视图对象
 *
 * @author system
 * @date 2024-01-10
 */
@Data
public class ContactInfoVo implements Serializable {
    
    @Serial
    private static final long serialVersionUID = 1L;
    
    /**
     * 邮箱地址
     */
    private String email;
    
    /**
     * 联系电话
     */
    private String phone;
    
    /**
     * 微信号
     */
    private String wechat;
    
    /**
     * QQ号
     */
    private String qq;
    
    /**
     * 联系地址
     */
    private String address;
}