import { get, post, put, del } from '@/utils/request'

/**
 * 用户相关API
 */

// 获取用户信息
export const getUserInfo = () => {
  return get('/system/user/profile', {}, false)
}

// 更新用户信息
export const updateUserInfo = (data) => {
  return put('/system/user/profile', data, '更新中...')
}

// 修改密码
export const changePassword = (data) => {
  return put('/system/user/profile/password', data, '修改中...')
}

// 更新头像
export const updateAvatar = (data) => {
  return post('/system/user/profile/avatar', data, '上传中...')
}

// 提交实名认证
export const submitRealNameAuth = (data) => {
  return post('/system/user/realNameAuth', data, '认证中...')
}