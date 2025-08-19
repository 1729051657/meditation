/**
 * 全局常量定义
 */

// 存储键名常量
export const STORAGE_KEYS = {
  // 用户认证相关
  TOKEN: 'meditation_token',           // 用户认证令牌
  USER_INFO: 'meditation_user_info',   // 用户信息
  REFRESH_TOKEN: 'meditation_refresh_token', // 刷新令牌
  
  // 租户和部门相关
  TENANT_ID: 'meditation_tenant_id',   // 租户ID
  DEPT_ID: 'meditation_dept_id',       // 部门ID
  
  // 微信相关
  OPENID: 'openid',                    // 微信openid
  SCOPE: 'scope',                      // 用户scope
  
  // 应用设置相关
  APP_SETTINGS: 'meditation_app_settings', // 应用设置
  THEME: 'meditation_theme',           // 主题设置
  LANGUAGE: 'meditation_language',     // 语言设置
  
  // 缓存相关
  CACHE_DATA: 'meditation_cache_data', // 缓存数据
  SEARCH_HISTORY: 'meditation_search_history', // 搜索历史
  
  // 其他
  FIRST_LAUNCH: 'meditation_first_launch', // 首次启动标记
  VERSION: 'meditation_version'        // 版本信息
}

// 默认值常量
export const DEFAULT_VALUES = {
  // 分页
  PAGE_SIZE: 10,
  PAGE_SIZE_MAX: 100,
  
  // 超时时间（毫秒）
  REQUEST_TIMEOUT: 30000,
  TOKEN_EXPIRE_TIME: 7200000, // 2小时
  
  // 缓存时间（毫秒）
  CACHE_EXPIRE_TIME: 300000, // 5分钟
  
  // 文件大小限制
  MAX_IMAGE_SIZE: 5 * 1024 * 1024, // 5MB
  MAX_AUDIO_SIZE: 50 * 1024 * 1024, // 50MB
}

// 状态常量
export const STATUS = {
  SUCCESS: 'success',
  ERROR: 'error',
  WARNING: 'warning',
  INFO: 'info',
  LOADING: 'loading'
}

// 用户角色常量
export const USER_ROLES = {
  ADMIN: 'admin',
  USER: 'user',
  GUEST: 'guest'
}

// 导出所有常量
export default {
  STORAGE_KEYS,
  DEFAULT_VALUES,
  STATUS,
  USER_ROLES
}
