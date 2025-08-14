import { get, post } from '@/utils/request'
import { appConfig } from '@/common/config'

/**
 * 认证相关API
 */

// 普通用户名密码登录
export const login = (data) => {
  return post('/xcx/auth/login', data, '登录中...')
}

// 小程序登录（不需要手机号，仅 code）
// 参数：code, appid, tenantId, clientId, grantType
export const inspectionWechatLogin = (data) => {
  const loginData = {
    xcxCode: data.code,
    appid: data.appid,
    clientId: appConfig.wechatAppId,
    grantType: 'xcx',
    tenantId: data.tenantId || '000000',
    deptId: data.deptId
  }
  return post('/xcx/auth/login', loginData, '验证登录中...')
}

// 登出（使用小程序专用接口）
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

 