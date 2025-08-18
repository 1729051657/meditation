import { get, post, put, del } from '@/utils/request'

/**
 * 用户相关API
 */

// 获取用户信息
export const getUserProfile = () => {
  return get('/api/user/profile', {}, false)
}

// 更新用户信息
export const updateUserInfo = (data) => {
  return put('/api/user/profile', data, '更新中...')
}

// 修改密码
export const changePassword = (data) => {
  return put('/api/user/password', data, '修改中...')
}

// 更新头像
export const updateAvatar = (data) => {
  return post('/api/user/avatar', data, '上传中...')
}

// 获取用户统计信息
export const getUserStats = () => {
  return get('/api/user/stats', {}, false)
}

// 兼容旧接口名称
export const getUserInfo = getUserProfile