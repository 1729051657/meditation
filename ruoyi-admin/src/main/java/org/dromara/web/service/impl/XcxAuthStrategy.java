package org.dromara.web.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.stp.parameter.SaLoginParameter;
import cn.hutool.core.util.ObjectUtil;
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
import org.dromara.web.domain.vo.LoginVo;
import org.dromara.web.service.IAuthStrategy;
import org.dromara.web.service.SysLoginService;
import org.springframework.stereotype.Service;

/**
 * 冥想小程序认证策略
 * <p>
 * 功能说明：
 * 1. 支持冥想小程序登录（不需要手机号授权，自动创建用户）
 * 2. 冥想端自动创建用户，用户类型为xcx_user
 * 3. 支持租户隔离
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
        String tenantId = loginBody.getTenantId();

        log.info("冥想小程序登录，租户ID：{}", tenantId);

        try {
            // 校验微信登录，获取openid和unionId
            AuthRequest authRequest = new AuthWechatMiniProgramRequest(AuthConfig.builder()
                .clientId(appid).clientSecret(client.getClientSecret())
                .ignoreCheckRedirectUri(true).ignoreCheckState(true).build());
            AuthCallback authCallback = new AuthCallback();
            authCallback.setCode(xcxCode);
            AuthResponse<AuthUser> resp = authRequest.login(authCallback);

            String openid, unionId;
            if (resp.ok()) {
                AuthToken token = resp.getData().getToken();
                openid = token.getOpenId();
                unionId = token.getUnionId();
            } else {
                throw new ServiceException("微信授权失败：" + resp.getMsg());
            }

            // 处理冥想小程序登录逻辑
            XcxLoginUser loginUser = TenantHelper.dynamic(tenantId, () -> {
                return loadOrCreateMeditationUser(openid, unionId);
            });

            loginUser.setClientKey(client.getClientKey());
            loginUser.setDeviceType(client.getDeviceType());

            SaLoginParameter model = new SaLoginParameter();
            model.setDeviceType(client.getDeviceType());
            // 小程序较长的过期时间（24小时）
            model.setTimeout(client.getTimeout());
            model.setActiveTimeout(client.getActiveTimeout());
            model.setExtra(LoginHelper.CLIENT_KEY, client.getClientId());
            // 生成token
            LoginHelper.login(loginUser, model);

            LoginVo loginVo = new LoginVo();
            loginVo.setAccessToken(StpUtil.getTokenValue());
            loginVo.setExpireIn(StpUtil.getTokenTimeout());
            loginVo.setClientId(client.getClientId());
            // 添加小程序用户相关信息
            loginVo.setOpenid(openid);
            loginVo.setScope("xcx_user");
            // 设置用户信息
            loginVo.setUser(loginUser);
            return loginVo;

        } catch (Exception e) {
            log.error("冥想小程序登录失败", e);
            throw e;
        }
    }

    /**
     * 加载或创建冥想用户
     *
     * @param openid  微信openid
     * @param unionId 微信unionid
     * @return 登录用户信息
     */
    private XcxLoginUser loadOrCreateMeditationUser(String openid, String unionId) {
        // 先根据openid查找用户
        SysUserVo user = userService.selectUserByWechatOpenId(openid);

        if (ObjectUtil.isNull(user)) {
            // 用户不存在，自动创建冥想用户
            log.info("冥想小程序登录：用户不存在，自动创建用户，openid: {}", openid);
            user = userService.createMeditationUser(openid, unionId);
        } else {
            // 检查用户状态
            if (SystemConstants.DISABLE.equals(user.getStatus())) {
                log.info("冥想小程序登录失败：用户已被停用");
                throw new ServiceException("账号已被停用，请联系管理员");
            }

            // 更新微信信息
            if (ObjectUtil.isNull(user.getWechatUnionId()) || !unionId.equals(user.getWechatUnionId())) {
                log.info("为用户 {} 更新微信UnionID: {}", user.getUserName(), unionId);
                SysUserVo finalUser = user;
                DataPermissionHelper.ignore(() -> {
                    userService.updateUserWechatInfo(finalUser.getUserId(), openid, unionId);
                });
                // 重新查询用户信息
                user = userService.selectUserById(user.getUserId());
            }
        }

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

        log.info("冥想小程序用户 {} 登录成功", user.getUserName());
        return xcxLoginUser;
    }
}
