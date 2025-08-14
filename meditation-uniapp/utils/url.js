/**
 * URL参数解码工具函数
 * 解决小程序页面跳转时中文参数乱码问题
 */

/**
 * 解码URL参数
 * @param {Object} params - 原始参数对象
 * @returns {Object} 解码后的参数对象
 */
export function decodeUrlParams(params) {
  if (!params || typeof params !== 'object') {
    return {}
  }

  const decoded = {}
  for (const key in params) {
    if (params.hasOwnProperty(key)) {
      try {
        // 尝试解码参数值
        decoded[key] = decodeURIComponent(params[key])
      } catch (e) {
        // 如果解码失败，使用原始值
        console.warn(`URL参数解码失败: ${key} = ${params[key]}`, e)
        decoded[key] = params[key]
      }
    }
  }
  return decoded
}

/**
 * 编码URL参数
 * @param {Object} params - 参数对象
 * @returns {Object} 编码后的参数对象
 */
export function encodeUrlParams(params) {
  if (!params || typeof params !== 'object') {
    return {}
  }

  const encoded = {}
  for (const key in params) {
    if (params.hasOwnProperty(key)) {
      try {
        // 编码参数值
        encoded[key] = encodeURIComponent(params[key])
      } catch (e) {
        // 如果编码失败，使用原始值
        console.warn(`URL参数编码失败: ${key} = ${params[key]}`, e)
        encoded[key] = params[key]
      }
    }
  }
  return encoded
}

/**
 * 构建带参数的URL
 * @param {string} baseUrl - 基础URL
 * @param {Object} params - 参数对象
 * @returns {string} 完整的URL
 */
export function buildUrl(baseUrl, params = {}) {
  if (!baseUrl) return ''
  
  const encodedParams = encodeUrlParams(params)
  const queryString = Object.keys(encodedParams)
    .map(key => `${key}=${encodedParams[key]}`)
    .join('&')
  
  return queryString ? `${baseUrl}?${queryString}` : baseUrl
}

/**
 * 解析URL查询字符串
 * @param {string} queryString - 查询字符串
 * @returns {Object} 解析后的参数对象
 */
export function parseQueryString(queryString) {
  if (!queryString) return {}
  
  const params = {}
  const pairs = queryString.split('&')
  
  for (const pair of pairs) {
    const [key, value] = pair.split('=')
    if (key) {
      try {
        params[key] = decodeURIComponent(value || '')
      } catch (e) {
        params[key] = value || ''
      }
    }
  }
  
  return params
}

/**
 * 安全跳转页面（自动处理参数编码）
 * @param {string} url - 页面路径
 * @param {Object} params - 参数对象
 */
export function safeNavigateTo(url, params = {}) {
  const fullUrl = buildUrl(url, params)
  uni.navigateTo({ url: fullUrl })
}

/**
 * 安全重定向页面（自动处理参数编码）
 * @param {string} url - 页面路径
 * @param {Object} params - 参数对象
 */
export function safeRedirectTo(url, params = {}) {
  const fullUrl = buildUrl(url, params)
  uni.redirectTo({ url: fullUrl })
}

/**
 * 安全切换标签页（自动处理参数编码）
 * @param {string} url - 页面路径
 * @param {Object} params - 参数对象
 */
export function safeSwitchTab(url, params = {}) {
  const fullUrl = buildUrl(url, params)
  uni.switchTab({ url: fullUrl })
}

/**
 * 安全重新启动页面（自动处理参数编码）
 * @param {string} url - 页面路径
 * @param {Object} params - 参数对象
 */
export function safeReLaunch(url, params = {}) {
  const fullUrl = buildUrl(url, params)
  uni.reLaunch({ url: fullUrl })
} 