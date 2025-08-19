import { get, post } from '@/utils/request'
import { appConfig } from '@/common/config'

/**
 * 认证相关API
 */

// 普通用户名密码登录
export const login = (data) => {
  return post('/xcx/auth/login', data, '登录中...')
}

// 冥想小程序登录（不需要手机号，仅 code）
// 参数：code, appid, tenantId, clientId
export const inspectionWechatLogin = (data) => {
  const loginData = {
    xcxCode: data.code,
    appid: data.appid || appConfig.wechatAppId,
    clientId: data.clientId || appConfig.clientId,
    grantType: 'xcx', // 授权类型
    tenantId: data.tenantId || appConfig.defaultTenantId || '000000'
  }
  return post('/xcx/auth/login', loginData, '验证登录中...')
}

// 登出
export const logout = () => {
  return post('/xcx/auth/logout', {}, '退出中...')
}

// 获取验证码图片
export const getCodeImg = () => {
  return get('/code', {}, false)
}

// 获取租户列表
export const getTenantList = () => {
  return get('/auth/tenant/list', {}, false)
}

 