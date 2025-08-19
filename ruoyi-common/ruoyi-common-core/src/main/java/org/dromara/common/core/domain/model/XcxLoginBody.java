package org.dromara.common.core.domain.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 小程序用户登录对象
 *
 * @author meditation
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class XcxLoginBody implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户名（账号密码登录时使用）
     */
    private String username;

    /**
     * 用户密码（账号密码登录时使用）
     */
    private String password;

    /**
     * 小程序code（微信登录时使用）
     */
    private String xcxCode;

    /**
     * 小程序appid
     */
    private String appid;

    /**
     * 客户端id
     */
    private String clientId;

    /**
     * 授权类型（xcx: 小程序登录, password: 密码登录）
     */
    private String grantType;

    /**
     * 租户ID
     */
    private String tenantId;

    /**
     * 部门ID
     */
    private Long deptId;

    /**
     * 加密数据（获取手机号时使用）
     */
    private String encryptedData;

    /**
     * 加密算法的初始向量（获取手机号时使用）
     */
    private String iv;

    /**
     * 验证码
     */
    private String code;

    /**
     * 唯一标识
     */
    private String uuid;
}
