package org.dromara.web.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.stp.parameter.SaLoginParameter;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.zhyd.oauth.config.AuthConfig;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.model.AuthResponse;
import me.zhyd.oauth.model.AuthToken;
import me.zhyd.oauth.model.AuthUser;
import me.zhyd.oauth.request.AuthRequest;
import me.zhyd.oauth.request.AuthWechatMiniProgramRequest;
import org.dromara.common.core.constant.SystemConstants;
import org.dromara.common.core.domain.model.LoginUser;
import org.dromara.common.core.domain.model.XcxLoginBody;
import org.dromara.common.core.domain.model.XcxLoginUser;
import org.dromara.common.core.exception.ServiceException;
import org.dromara.common.core.utils.ValidatorUtils;
import org.dromara.common.json.utils.JsonUtils;
import org.dromara.common.mybatis.helper.DataPermissionHelper;
import org.dromara.common.satoken.utils.LoginHelper;
import org.dromara.common.tenant.helper.TenantHelper;
import org.dromara.system.domain.vo.SysClientVo;
import org.dromara.system.domain.vo.SysUserVo;
import org.dromara.system.service.ISysUserService;
import org.dromara.utils.WechatDecryptUtil;
import org.dromara.web.domain.vo.LoginVo;
import org.dromara.web.service.IAuthStrategy;
import org.dromara.web.service.SysLoginService;
import org.springframework.stereotype.Service;

/**
 * 巡检端小程序认证策略
 * <p>
 * 功能说明：
 * 1. 支持手机号授权登录
 * 2. 只允许巡检端用户（inspection_user）或系统用户（sys_user）登录
 * 3. 用户必须在系统中已存在，否则登录失败
 * 4. 支持租户隔离
 * 5. 人脸验证在巡检打卡时进行，登录时不需要
 *
 * @author 系统
 */
@Slf4j
@Service("xcx" + IAuthStrategy.BASE_NAME)
@RequiredArgsConstructor
public class XcxAuthStrategy implements IAuthStrategy {

    private final SysLoginService loginService;
    private final ISysUserService userService;

    @Override
    public LoginVo login(String body, SysClientVo client) {
        XcxLoginBody loginBody = JsonUtils.parseObject(body, XcxLoginBody.class);
        ValidatorUtils.validate(loginBody);

        // xcxCode 为小程序调用 wx.login 授权后获取
        String xcxCode = loginBody.getXcxCode();
        String appid = loginBody.getAppid();
        String encryptedData = loginBody.getEncryptedData();
        String iv = loginBody.getIv();
        String tenantId = loginBody.getTenantId();

        log.info("巡检端小程序登录，租户ID：{}", tenantId);

        try {
            // 校验微信登录，获取openid和session_key
            AuthRequest authRequest = new AuthWechatMiniProgramRequest(AuthConfig.builder()
                .clientId(appid).clientSecret(client.getClientSecret())
                .ignoreCheckRedirectUri(true).ignoreCheckState(true).build());
            AuthCallback authCallback = new AuthCallback();
            authCallback.setCode(xcxCode);
            AuthResponse<AuthUser> resp = authRequest.login(authCallback);

            String openid, unionId, sessionKey;
            if (resp.ok()) {
                AuthToken token = resp.getData().getToken();
                openid = token.getOpenId();
                unionId = token.getUnionId();
                sessionKey = token.getAccessToken(); // session_key存储在accessToken中
            } else {
                throw new ServiceException("微信授权失败：" + resp.getMsg());
            }

            // 获取手机号（优先使用直接传入的，其次解密微信手机号）
            final String finalPhoneNumber;
            if (StrUtil.isNotBlank(encryptedData) && StrUtil.isNotBlank(iv)) {
                try {
                    finalPhoneNumber = WechatDecryptUtil.decryptPhone(encryptedData, iv, sessionKey);
                    log.info("巡检端小程序登录，成功解密手机号：{}", finalPhoneNumber);
                } catch (Exception e) {
                    log.error("巡检端小程序登录，手机号解密失败", e);
                    throw new ServiceException("手机号解密失败，请重试");
                }
            } else {
                throw new ServiceException("巡检端登录需要手机号授权");
            }

            // 根据手机号查找巡检端用户并构建登录用户信息
            XcxLoginUser loginUser = TenantHelper.dynamic(tenantId, () -> {
                // 通过手机号查找用户
                SysUserVo user = loadInspectionUserByPhone(finalPhoneNumber, openid, unionId);

                // 使用loginService.buildLoginUser构建完整的LoginUser，然后转换为XcxLoginUser
                LoginUser baseLoginUser = loginService.buildLoginUser(user);

                // 创建XcxLoginUser并复制所有权限信息
                XcxLoginUser xcxLoginUser = new XcxLoginUser();
                xcxLoginUser.setTenantId(baseLoginUser.getTenantId());
                xcxLoginUser.setUserId(baseLoginUser.getUserId());
                xcxLoginUser.setDeptId(baseLoginUser.getDeptId());
                xcxLoginUser.setDeptCategory(baseLoginUser.getDeptCategory());
                xcxLoginUser.setDeptName(baseLoginUser.getDeptName());
                xcxLoginUser.setUsername(baseLoginUser.getUsername());
                xcxLoginUser.setNickname(baseLoginUser.getNickname());
                xcxLoginUser.setUserType(baseLoginUser.getUserType());
                xcxLoginUser.setMenuPermission(baseLoginUser.getMenuPermission());
                xcxLoginUser.setRolePermission(baseLoginUser.getRolePermission());
                xcxLoginUser.setRoles(baseLoginUser.getRoles());
                xcxLoginUser.setPosts(baseLoginUser.getPosts());
                xcxLoginUser.setRoleId(baseLoginUser.getRoleId());

                // 设置小程序特有的字段
                xcxLoginUser.setOpenid(openid);

                return xcxLoginUser;
            });

            loginUser.setClientKey(client.getClientKey());
            loginUser.setDeviceType(client.getDeviceType());

            SaLoginParameter model = new SaLoginParameter();
            model.setDeviceType(client.getDeviceType());
            // 巡检端较长的过期时间（24小时）
            model.setTimeout(client.getTimeout());
            model.setActiveTimeout(client.getActiveTimeout());
            model.setExtra(LoginHelper.CLIENT_KEY, client.getClientId());

            // 生成token
            LoginHelper.login(loginUser, model);

            LoginVo loginVo = new LoginVo();
            loginVo.setAccessToken(StpUtil.getTokenValue());
            loginVo.setExpireIn(StpUtil.getTokenTimeout());
            loginVo.setClientId(client.getClientId());
            loginVo.setOpenid(openid);
            return loginVo;

        } catch (Exception e) {
            log.error("巡检端小程序登录失败", e);
            throw e;
        }
    }

    /**
     * 根据手机号加载巡检端用户
     *
     * @param phoneNumber 手机号
     * @param openid      微信openid
     * @param unionId     微信unionid
     * @return 用户信息
     */
    private SysUserVo loadInspectionUserByPhone(String phoneNumber, String openid, String unionId) {
        // 先根据手机号查找用户
        SysUserVo user = userService.selectUserByPhonenumber(phoneNumber);

        if (ObjectUtil.isNull(user)) {
            log.info("巡检端登录失败：手机号 {} 未在系统中注册", phoneNumber);
            throw new ServiceException("该手机号未在系统中注册，请联系管理员");
        }

        // 检查用户类型，必须是巡检端用户或系统用户
        if (!"inspection_user".equals(user.getUserType()) && !"sys_user".equals(user.getUserType())) {
            log.info("巡检端登录失败：用户 {} 类型为 {}，不允许登录巡检端", phoneNumber, user.getUserType());
            throw new ServiceException("该账号无权限登录巡检端");
        }

        // 检查用户状态
        if (SystemConstants.DISABLE.equals(user.getStatus())) {
            log.info("巡检端登录失败：用户 {} 已被停用", phoneNumber);
            throw new ServiceException("账号已被停用，请联系管理员");
        }

        // 绑定或更新微信信息
        if (StrUtil.isBlank(user.getWechatOpenId()) || !openid.equals(user.getWechatOpenId())) {
            log.info("为用户 {} 绑定微信OpenID: {}", phoneNumber, openid);
            final Long userId = user.getUserId(); // 提取userId避免lambda中引用可变变量
            // 登录过程中跳过数据权限检查，避免token未生成时的权限检查异常
            DataPermissionHelper.ignore(() -> {
                userService.updateUserWechatInfo(userId, openid, unionId);
            });
            // 重新查询用户信息
            user = userService.selectUserById(user.getUserId());
        }

        log.info("巡检端用户 {} 登录成功", phoneNumber);
        return user;
    }


}
