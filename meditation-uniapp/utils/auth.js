/**
 * 认证相关工具函数
 */

import { appConfig } from '@/common/config.js'

const TOKEN_KEY = 'merchant_token'
const USER_INFO_KEY = 'merchant_user_info'
const TENANT_ID_KEY = 'merchant_tenant_id'
const DEPT_ID_KEY = 'merchant_dept_id'

/**
 * 获取token
 */
export function getToken() {
  return uni.getStorageSync(TOKEN_KEY)
}

/**
 * 设置token
 */
export function setToken(token) {
  return uni.setStorageSync(TOKEN_KEY, token)
}

/**
 * 删除token
 */
export function removeToken() {
  return uni.removeStorageSync(TOKEN_KEY)
}

/**
 * 获取用户信息
 */
export function getUserInfo() {
  const userInfo = uni.getStorageSync(USER_INFO_KEY)
  return userInfo ? JSON.parse(userInfo) : null
}

/**
 * 设置用户信息
 */
export function setUserInfo(userInfo) {
  return uni.setStorageSync(USER_INFO_KEY, JSON.stringify(userInfo))
}

/**
 * 删除用户信息
 */
export function removeUserInfo() {
  return uni.removeStorageSync(USER_INFO_KEY)
}

/**
 * 获取租户ID
 */
export function getTenantId() {
  return uni.getStorageSync(TENANT_ID_KEY)
}

/**
 * 设置租户ID
 */
export function setTenantId(tenantId) {
  return uni.setStorageSync(TENANT_ID_KEY, tenantId)
}

/**
 * 删除租户ID
 */
export function removeTenantId() {
  return uni.removeStorageSync(TENANT_ID_KEY)
}

/**
 * 获取部门ID
 */
export function getDeptId() {
  return uni.getStorageSync(DEPT_ID_KEY)
}

/**
 * 设置部门ID
 */
export function setDeptId(deptId) {
  return uni.setStorageSync(DEPT_ID_KEY, deptId)
}

/**
 * 删除部门ID
 */
export function removeDeptId() {
  return uni.removeStorageSync(DEPT_ID_KEY)
}

/**
 * 检查是否已登录
 */
export function isLoggedIn() {
  return !!getToken()
}

/**
 * 清除所有认证信息
 */
export function clearAuth() {
  removeToken()
  removeUserInfo()
  removeTenantId()
  removeDeptId()
}

/**
 * 获取客户端ID（小程序ID）
 */
export function getClientId() {
  return appConfig.wechatAppId
}

/**
 * 获取完整的认证头信息
 */
export function getAuthHeader() {
  const token = getToken()
  const tenantId = getTenantId()
  const deptId = getDeptId()
  const clientId = getClientId()
  
  const headers = {}
  if (token) {
    headers['Authorization'] = 'Bearer ' + token
  }
  if (tenantId) {
    headers['tenant-id'] = tenantId
  }
  if (deptId) {
    headers['dept-id'] = deptId
  }
  if (clientId) {
    headers['clientid'] = clientId
  }
  
  return headers
} 