/**
 * 小程序配置文件
 */

// 引入全局常量
import { STORAGE_KEYS, DEFAULT_VALUES } from './constants.js'

// 根据环境设置不同的baseUrl
const envConfig = {
  // 开发环境
  development: {
    baseUrl: 'https://thoughtadmin.bawutech.com/prod-api',
    debug: true
  },
  // 测试环境
  test: {
    baseUrl: 'https://thoughtadmin.bawutech.com/prod-api',
    debug: true
  },
  // 生产环境
  production: {
    baseUrl: 'https://thoughtadmin.bawutech.com/prod-api',
    debug: false
  }
}

// 获取当前环境，默认为开发环境
const currentEnv = process.env.NODE_ENV || 'development'

// 当前环境配置
const config = envConfig[currentEnv] || envConfig.development

// 导出基础URL
export const baseUrl = config.baseUrl

// 导出调试开关
export const debug = config.debug

// 导出存储键名常量
export { STORAGE_KEYS, DEFAULT_VALUES }

// 其他配置
export const appConfig = {
  // 应用名称
  appName: '冥想空间',
  
  // 版本号
  version: '1.0.0',
  
  // 默认租户ID
  defaultTenantId: '000000',
  
  // 超时时间（毫秒）
  timeout: DEFAULT_VALUES.REQUEST_TIMEOUT,
  
  // 分页默认大小
  pageSize: DEFAULT_VALUES.PAGE_SIZE,
  
  // 图片上传大小限制（字节）
  maxImageSize: DEFAULT_VALUES.MAX_IMAGE_SIZE,
  
  // 支持的图片格式
  imageTypes: ['jpg', 'jpeg', 'png', 'gif'],
  
  // 微信小程序AppID
  wechatAppId: 'wx1ef177b90f6fb543', // 冥想小程序AppID
  
  // 客户端配置
  clientId: 'wx1ef177b90f6fb543', // 对应sys_client表中的client_id
  clientKey: 'user_xcx', // 对应sys_client表中的client_key
  
  // 微信小程序Secret（仅在后端配置，前端不需要）
  // wechatAppSecret: 'your_app_secret'
}

// 错误码配置
export const errorCodes = {
  SUCCESS: 200,
  UNAUTHORIZED: 401,
  FORBIDDEN: 403,
  NOT_FOUND: 404,
  SERVER_ERROR: 500,
  NETWORK_ERROR: -1
}

// 导出所有配置
export default {
  baseUrl,
  debug,
  STORAGE_KEYS,
  DEFAULT_VALUES,
  appConfig,
  errorCodes
}
