package org.dromara.web.controller;

import cn.dev33.satoken.annotation.SaIgnore;
import cn.hutool.core.util.ObjectUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.core.domain.R;
import org.dromara.common.core.domain.model.LoginBody;
import org.dromara.common.core.domain.model.XcxLoginBody;
import org.dromara.common.satoken.utils.LoginHelper;
import org.dromara.system.domain.vo.SysUserVo;
import org.dromara.system.service.ISysUserService;
import org.dromara.web.domain.vo.LoginVo;
import org.dromara.web.service.IAuthStrategy;
import org.dromara.web.service.SysLoginService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import java.util.HashMap;
import java.util.Map;

/**
 * 小程序认证控制器
 *
 * @author meditation
 */
@Slf4j
@SaIgnore
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/xcx/auth")
public class XcxAuthController {

    private final SysLoginService loginService;
    private final ISysUserService userService;
    private final IAuthStrategy authStrategy;

    /**
     * 小程序登录
     *
     * @param loginBody 登录信息
     * @return 登录结果
     */
    @PostMapping("/login")
    public R<Map<String, Object>> xcxLogin(@Validated @RequestBody XcxLoginBody loginBody) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 判断登录类型
            if ("xcx".equals(loginBody.getGrantType())) {
                // 微信小程序登录
                log.info("微信小程序登录，code: {}", loginBody.getXcxCode());
                
                // TODO: 调用微信API获取openid和session_key
                // 这里需要根据实际的微信小程序配置来实现
                // String openid = wxService.getOpenid(loginBody.getXcxCode());
                
                // 模拟登录逻辑，实际应该根据openid查找或创建用户
                LoginBody body = new LoginBody();
                body.setUsername("xcx_user_" + System.currentTimeMillis());
                body.setPassword("123456");
                body.setTenantId(loginBody.getTenantId());
                
                // 执行登录
                LoginVo loginVo = authStrategy.login(body, loginBody.getClientId(), "password");
                
                result.put("access_token", loginVo.getAccessToken());
                result.put("expires_in", loginVo.getExpireIn());
                
                // 获取用户信息
                Long userId = LoginHelper.getUserId();
                SysUserVo user = userService.selectUserById(userId);
                if (ObjectUtil.isNotNull(user)) {
                    Map<String, Object> userInfo = new HashMap<>();
                    userInfo.put("userId", user.getUserId());
                    userInfo.put("userName", user.getUserName());
                    userInfo.put("nickName", user.getNickName());
                    userInfo.put("avatar", user.getAvatar());
                    userInfo.put("phonenumber", user.getPhonenumber());
                    result.put("user", userInfo);
                }
                
                return R.ok(result);
            } else {
                // 普通账号密码登录
                LoginBody body = new LoginBody();
                body.setUsername(loginBody.getUsername());
                body.setPassword(loginBody.getPassword());
                body.setTenantId(loginBody.getTenantId());
                
                LoginVo loginVo = authStrategy.login(body, loginBody.getClientId(), "password");
                
                result.put("access_token", loginVo.getAccessToken());
                result.put("expires_in", loginVo.getExpireIn());
                
                // 获取用户信息
                Long userId = LoginHelper.getUserId();
                SysUserVo user = userService.selectUserById(userId);
                if (ObjectUtil.isNotNull(user)) {
                    Map<String, Object> userInfo = new HashMap<>();
                    userInfo.put("userId", user.getUserId());
                    userInfo.put("userName", user.getUserName());
                    userInfo.put("nickName", user.getNickName());
                    userInfo.put("avatar", user.getAvatar());
                    userInfo.put("phonenumber", user.getPhonenumber());
                    result.put("user", userInfo);
                }
                
                return R.ok(result);
            }
        } catch (Exception e) {
            log.error("小程序登录失败", e);
            return R.fail("登录失败：" + e.getMessage());
        }
    }

    /**
     * 退出登录
     */
    @PostMapping("/logout")
    public R<Void> logout() {
        loginService.logout();
        return R.ok("退出成功");
    }

    /**
     * 获取用户信息
     */
    @GetMapping("/userinfo")
    public R<Map<String, Object>> getUserInfo() {
        Long userId = LoginHelper.getUserId();
        SysUserVo user = userService.selectUserById(userId);
        
        if (ObjectUtil.isNull(user)) {
            return R.fail("用户信息不存在");
        }
        
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("userId", user.getUserId());
        userInfo.put("userName", user.getUserName());
        userInfo.put("nickName", user.getNickName());
        userInfo.put("avatar", user.getAvatar());
        userInfo.put("phonenumber", user.getPhonenumber());
        userInfo.put("email", user.getEmail());
        userInfo.put("sex", user.getSex());
        userInfo.put("deptId", user.getDeptId());
        userInfo.put("deptName", user.getDept() != null ? user.getDept().getDeptName() : "");
        
        return R.ok(userInfo);
    }
}