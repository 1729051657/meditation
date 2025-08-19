package org.dromara.web.controller;

import cn.dev33.satoken.annotation.SaIgnore;
import cn.hutool.core.util.ObjectUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.core.constant.SystemConstants;
import org.dromara.common.core.domain.R;
import org.dromara.common.core.domain.model.SmsLoginBody;
import org.dromara.common.core.domain.model.XcxLoginBody;
import org.dromara.common.core.utils.MessageUtils;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.json.utils.JsonUtils;
import org.dromara.common.satoken.utils.LoginHelper;
import org.dromara.common.sse.dto.SseMessageDto;
import org.dromara.common.sse.utils.SseMessageUtils;
import org.dromara.system.domain.vo.SysClientVo;
import org.dromara.system.service.ISysClientService;
import org.dromara.web.domain.vo.LoginVo;
import org.dromara.web.service.IAuthStrategy;
import org.dromara.web.service.SysLoginService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 小程序认证控制器（无加密）
 * 专门为小程序端提供不需要加密依赖的登录接口
 *
 * @author beauty
 */
@Slf4j
@SaIgnore
@RequiredArgsConstructor
@RestController
@RequestMapping("/xcx/auth")
public class XcxAuthController {

    private final SysLoginService loginService;
    private final ISysClientService clientService;
    private final ScheduledExecutorService scheduledExecutorService;

    /**
     * 小程序登录方法（无加密）
     *
     * @param xcxLoginBody 小程序登录信息
     * @return 结果
     */
    @PostMapping("/login")
    public R<LoginVo> xcxLogin(@Validated @RequestBody XcxLoginBody xcxLoginBody) {
        log.info("小程序登录请求：{}", xcxLoginBody);

        try {
            // 授权类型和客户端id
            String clientId = xcxLoginBody.getClientId();
            String grantType = xcxLoginBody.getGrantType();

            // 设置默认值
            if (StringUtils.isBlank(clientId)) {
                clientId = "xcx"; // 默认小程序客户端ID
            }
            if (StringUtils.isBlank(grantType)) {
                grantType = "xcx"; // 默认小程序授权类型
            }

            SysClientVo client = clientService.queryByClientId(clientId);

            // 查询不到 client 或 client 内不包含 grantType
            if (ObjectUtil.isNull(client) || !StringUtils.contains(client.getGrantType(), grantType)) {
                log.info("小程序客户端id: {} 认证类型：{} 异常!.", clientId, grantType);
                return R.fail(MessageUtils.message("auth.grant.type.error"));
            } else if (!SystemConstants.NORMAL.equals(client.getStatus())) {
                return R.fail(MessageUtils.message("auth.grant.type.blocked"));
            }

            // 校验租户（小程序可能不需要租户）
            if (StringUtils.isNotBlank(xcxLoginBody.getTenantId())) {
                loginService.checkTenant(xcxLoginBody.getTenantId());
            }

            // 登录 - 转换为JSON字符串再调用现有的登录策略
            String jsonBody = JsonUtils.toJsonString(xcxLoginBody);
            LoginVo loginVo = IAuthStrategy.login(jsonBody, client, grantType);

            // 发送欢迎消息
            Long userId = LoginHelper.getUserId();
            scheduledExecutorService.schedule(() -> {
                SseMessageDto dto = new SseMessageDto();
                dto.setMessage("欢迎使用美研通小程序");
                dto.setUserIds(List.of(userId));
                SseMessageUtils.publishMessage(dto);
            }, 2, TimeUnit.SECONDS);

            return R.ok(loginVo);

        } catch (Exception e) {
            log.error("小程序登录异常：", e);
            return R.fail("登录失败：" + e.getMessage());
        }
    }

    /**
     * 商户端小程序登录方法（无加密）
     *
     * @param xcxLoginBody 商户端小程序登录信息
     * @return 结果
     */
    @PostMapping("/merchant/login")
    public R<LoginVo> merchantXcxLogin(@Validated @RequestBody XcxLoginBody xcxLoginBody) {
        log.info("商户端小程序登录请求：{}", xcxLoginBody);

        try {
            // 授权类型和客户端id
            String clientId = xcxLoginBody.getClientId();
            String grantType = xcxLoginBody.getGrantType();

            SysClientVo client = clientService.queryByClientId(clientId);

            // 查询不到 client 或 client 内不包含 grantType
            if (ObjectUtil.isNull(client) || !StringUtils.contains(client.getGrantType(), grantType)) {
                log.info("商户端小程序客户端id: {} 认证类型：{} 异常!.", clientId, grantType);
                return R.fail(MessageUtils.message("auth.grant.type.error"));
            } else if (!SystemConstants.NORMAL.equals(client.getStatus())) {
                return R.fail(MessageUtils.message("auth.grant.type.blocked"));
            }

            // 校验租户（商户端必需）
            if (StringUtils.isBlank(xcxLoginBody.getTenantId())) {
                return R.fail("商户端登录必须提供租户ID");
            }
            loginService.checkTenant(xcxLoginBody.getTenantId());

            // 登录 - 转换为JSON字符串再调用现有的登录策略
            String jsonBody = JsonUtils.toJsonString(xcxLoginBody);
            LoginVo loginVo = IAuthStrategy.login(jsonBody, client, grantType);

            // 发送欢迎消息
            Long userId = LoginHelper.getUserId();
            scheduledExecutorService.schedule(() -> {
                SseMessageDto dto = new SseMessageDto();
                dto.setMessage("欢迎使用美研通商户端");
                dto.setUserIds(List.of(userId));
                SseMessageUtils.publishMessage(dto);
            }, 2, TimeUnit.SECONDS);

            return R.ok(loginVo);

        } catch (Exception e) {
            log.error("商户端小程序登录异常：", e);
            return R.fail("登录失败：" + e.getMessage());
        }
    }

    /**
     * 商户端手机号验证码登录方法（无加密）
     *
     * @param smsLoginBody 手机号登录信息
     * @return 结果
     */
    @PostMapping("/merchant/sms/login")
    public R<LoginVo> merchantSmsLogin(@Validated @RequestBody SmsLoginBody smsLoginBody) {
        log.info("商户端手机号登录请求：{}", smsLoginBody);

        try {
            // 授权类型和客户端id
            String clientId = "merchant_xcx"; // 商户端小程序客户端ID
            String grantType = "sms"; // 商户端短信授权类型

            SysClientVo client = clientService.queryByClientId(clientId);

            // 查询不到 client 或 client 内不包含 grantType
            if (ObjectUtil.isNull(client) || !StringUtils.contains(client.getGrantType(), grantType)) {
                log.info("商户端手机号登录客户端id: {} 认证类型：{} 异常!.", clientId, grantType);
                return R.fail(MessageUtils.message("auth.grant.type.error"));
            } else if (!SystemConstants.NORMAL.equals(client.getStatus())) {
                return R.fail(MessageUtils.message("auth.grant.type.blocked"));
            }

            // 校验租户（商户端必需）
            if (StringUtils.isBlank(smsLoginBody.getTenantId())) {
                return R.fail("商户端登录必须提供租户ID");
            }
            loginService.checkTenant(smsLoginBody.getTenantId());

            // 登录 - 转换为JSON字符串再调用现有的登录策略
            String jsonBody = JsonUtils.toJsonString(smsLoginBody);
            LoginVo loginVo = IAuthStrategy.login(jsonBody, client, grantType);

            // 发送欢迎消息
            Long userId = LoginHelper.getUserId();
            scheduledExecutorService.schedule(() -> {
                SseMessageDto dto = new SseMessageDto();
                dto.setMessage("欢迎使用美研通商户端");
                dto.setUserIds(List.of(userId));
                SseMessageUtils.publishMessage(dto);
            }, 2, TimeUnit.SECONDS);

            return R.ok(loginVo);

        } catch (Exception e) {
            log.error("商户端手机号登录异常：", e);
            return R.fail("登录失败：" + e.getMessage());
        }
    }

    /**
     * 小程序退出登录
     */
    @PostMapping("/logout")
    public R<Void> logout() {
        try {
            loginService.logout();
            return R.ok("退出成功");
        } catch (Exception e) {
            log.error("小程序退出登录异常：", e);
            return R.fail("退出失败：" + e.getMessage());
        }
    }
}
