/**
 * 日期工具类
 */

/**
 * 格式化日期
 * @param {Date|String|Number} date 日期
 * @param {String} fmt 格式化模板 yyyy-MM-dd HH:mm:ss
 * @returns {String} 格式化后的日期字符串
 */
export function formatDate(date, fmt = 'yyyy-MM-dd HH:mm:ss') {
  if (!date) return ''
  
  // 转换为Date对象
  if (!(date instanceof Date)) {
    date = new Date(date)
  }
  
  // 如果日期无效，返回空字符串
  if (isNaN(date.getTime())) {
    return ''
  }
  
  const o = {
    'M+': date.getMonth() + 1, // 月份
    'd+': date.getDate(), // 日
    'H+': date.getHours(), // 小时
    'm+': date.getMinutes(), // 分
    's+': date.getSeconds(), // 秒
    'q+': Math.floor((date.getMonth() + 3) / 3), // 季度
    'S': date.getMilliseconds() // 毫秒
  }
  
  if (/(y+)/.test(fmt)) {
    fmt = fmt.replace(RegExp.$1, (date.getFullYear() + '').substr(4 - RegExp.$1.length))
  }
  
  for (let k in o) {
    if (new RegExp('(' + k + ')').test(fmt)) {
      fmt = fmt.replace(RegExp.$1, (RegExp.$1.length === 1) ? (o[k]) : (('00' + o[k]).substr(('' + o[k]).length)))
    }
  }
  
  return fmt
}

/**
 * 获取相对时间描述
 * @param {Date|String|Number} date 日期
 * @returns {String} 相对时间描述
 */
export function getRelativeTime(date) {
  if (!date) return ''
  
  // 转换为Date对象
  if (!(date instanceof Date)) {
    date = new Date(date)
  }
  
  const now = new Date()
  const diff = now - date // 时间差（毫秒）
  
  // 未来时间
  if (diff < 0) {
    return formatDate(date, 'yyyy-MM-dd HH:mm')
  }
  
  const minute = 60 * 1000
  const hour = 60 * minute
  const day = 24 * hour
  const month = 30 * day
  const year = 365 * day
  
  if (diff < minute) {
    return '刚刚'
  } else if (diff < hour) {
    return Math.floor(diff / minute) + '分钟前'
  } else if (diff < day) {
    return Math.floor(diff / hour) + '小时前'
  } else if (diff < month) {
    return Math.floor(diff / day) + '天前'
  } else if (diff < year) {
    return Math.floor(diff / month) + '个月前'
  } else {
    return formatDate(date, 'yyyy-MM-dd')
  }
}

/**
 * 获取今天的日期
 * @param {String} fmt 格式化模板
 * @returns {String} 格式化后的日期字符串
 */
export function getToday(fmt = 'yyyy-MM-dd') {
  return formatDate(new Date(), fmt)
}

/**
 * 获取明天的日期
 * @param {String} fmt 格式化模板
 * @returns {String} 格式化后的日期字符串
 */
export function getTomorrow(fmt = 'yyyy-MM-dd') {
  const date = new Date()
  date.setDate(date.getDate() + 1)
  return formatDate(date, fmt)
}

/**
 * 获取昨天的日期
 * @param {String} fmt 格式化模板
 * @returns {String} 格式化后的日期字符串
 */
export function getYesterday(fmt = 'yyyy-MM-dd') {
  const date = new Date()
  date.setDate(date.getDate() - 1)
  return formatDate(date, fmt)
}

/**
 * 获取本周第一天（周一）
 * @param {String} fmt 格式化模板
 * @returns {String} 格式化后的日期字符串
 */
export function getWeekStart(fmt = 'yyyy-MM-dd') {
  const date = new Date()
  const day = date.getDay() || 7 // 周日为0，改为7
  date.setDate(date.getDate() - day + 1)
  return formatDate(date, fmt)
}

/**
 * 获取本周最后一天（周日）
 * @param {String} fmt 格式化模板
 * @returns {String} 格式化后的日期字符串
 */
export function getWeekEnd(fmt = 'yyyy-MM-dd') {
  const date = new Date()
  const day = date.getDay() || 7
  date.setDate(date.getDate() - day + 7)
  return formatDate(date, fmt)
}

/**
 * 获取本月第一天
 * @param {String} fmt 格式化模板
 * @returns {String} 格式化后的日期字符串
 */
export function getMonthStart(fmt = 'yyyy-MM-dd') {
  const date = new Date()
  date.setDate(1)
  return formatDate(date, fmt)
}

/**
 * 获取本月最后一天
 * @param {String} fmt 格式化模板
 * @returns {String} 格式化后的日期字符串
 */
export function getMonthEnd(fmt = 'yyyy-MM-dd') {
  const date = new Date()
  date.setMonth(date.getMonth() + 1)
  date.setDate(0)
  return formatDate(date, fmt)
}

/**
 * 计算两个日期之间的天数差
 * @param {Date|String|Number} date1 日期1
 * @param {Date|String|Number} date2 日期2
 * @returns {Number} 天数差
 */
export function getDaysDiff(date1, date2) {
  if (!date1 || !date2) return 0
  
  // 转换为Date对象
  if (!(date1 instanceof Date)) {
    date1 = new Date(date1)
  }
  if (!(date2 instanceof Date)) {
    date2 = new Date(date2)
  }
  
  // 获取时间戳
  const time1 = date1.getTime()
  const time2 = date2.getTime()
  
  // 计算天数差
  const diff = Math.abs(time2 - time1)
  return Math.floor(diff / (24 * 60 * 60 * 1000))
}

/**
 * 判断是否是今天
 * @param {Date|String|Number} date 日期
 * @returns {Boolean}
 */
export function isToday(date) {
  if (!date) return false
  return formatDate(date, 'yyyy-MM-dd') === getToday()
}

/**
 * 判断是否是本周
 * @param {Date|String|Number} date 日期
 * @returns {Boolean}
 */
export function isThisWeek(date) {
  if (!date) return false
  
  // 转换为Date对象
  if (!(date instanceof Date)) {
    date = new Date(date)
  }
  
  const weekStart = new Date(getWeekStart())
  const weekEnd = new Date(getWeekEnd())
  weekEnd.setHours(23, 59, 59, 999)
  
  return date >= weekStart && date <= weekEnd
}

/**
 * 判断是否是本月
 * @param {Date|String|Number} date 日期
 * @returns {Boolean}
 */
export function isThisMonth(date) {
  if (!date) return false
  
  // 转换为Date对象
  if (!(date instanceof Date)) {
    date = new Date(date)
  }
  
  const now = new Date()
  return date.getFullYear() === now.getFullYear() && date.getMonth() === now.getMonth()
}

/**
 * 添加天数
 * @param {Date|String|Number} date 日期
 * @param {Number} days 要添加的天数
 * @param {String} fmt 格式化模板
 * @returns {String} 格式化后的日期字符串
 */
export function addDays(date, days, fmt = 'yyyy-MM-dd') {
  if (!date) return ''
  
  // 转换为Date对象
  if (!(date instanceof Date)) {
    date = new Date(date)
  }
  
  const newDate = new Date(date)
  newDate.setDate(newDate.getDate() + days)
  return formatDate(newDate, fmt)
}

/**
 * 添加月份
 * @param {Date|String|Number} date 日期
 * @param {Number} months 要添加的月数
 * @param {String} fmt 格式化模板
 * @returns {String} 格式化后的日期字符串
 */
export function addMonths(date, months, fmt = 'yyyy-MM-dd') {
  if (!date) return ''
  
  // 转换为Date对象
  if (!(date instanceof Date)) {
    date = new Date(date)
  }
  
  const newDate = new Date(date)
  newDate.setMonth(newDate.getMonth() + months)
  return formatDate(newDate, fmt)
}

/**
 * 获取日期的中文星期
 * @param {Date|String|Number} date 日期
 * @returns {String} 星期几
 */
export function getWeekday(date) {
  if (!date) return ''
  
  // 转换为Date对象
  if (!(date instanceof Date)) {
    date = new Date(date)
  }
  
  const weekdays = ['星期日', '星期一', '星期二', '星期三', '星期四', '星期五', '星期六']
  return weekdays[date.getDay()]
}

/**
 * 获取时间段描述
 * @param {Number} hour 小时（0-23）
 * @returns {String} 时间段描述
 */
export function getTimeperiod(hour) {
  if (hour >= 0 && hour < 6) {
    return '凌晨'
  } else if (hour >= 6 && hour < 12) {
    return '上午'
  } else if (hour >= 12 && hour < 14) {
    return '中午'
  } else if (hour >= 14 && hour < 18) {
    return '下午'
  } else if (hour >= 18 && hour < 24) {
    return '晚上'
  }
  return ''
}

/**
 * 获取友好的时间显示
 * @param {Date|String|Number} date 日期
 * @returns {String} 友好的时间显示
 */
export function getFriendlyTime(date) {
  if (!date) return ''
  
  // 转换为Date对象
  if (!(date instanceof Date)) {
    date = new Date(date)
  }
  
  const now = new Date()
  const today = new Date(now.getFullYear(), now.getMonth(), now.getDate())
  const yesterday = new Date(today)
  yesterday.setDate(yesterday.getDate() - 1)
  
  const dateOnly = new Date(date.getFullYear(), date.getMonth(), date.getDate())
  
  if (dateOnly.getTime() === today.getTime()) {
    return '今天 ' + formatDate(date, 'HH:mm')
  } else if (dateOnly.getTime() === yesterday.getTime()) {
    return '昨天 ' + formatDate(date, 'HH:mm')
  } else if (date.getFullYear() === now.getFullYear()) {
    return formatDate(date, 'MM-dd HH:mm')
  } else {
    return formatDate(date, 'yyyy-MM-dd HH:mm')
  }
}

// 默认导出
export default {
  formatDate,
  getRelativeTime,
  getToday,
  getTomorrow,
  getYesterday,
  getWeekStart,
  getWeekEnd,
  getMonthStart,
  getMonthEnd,
  getDaysDiff,
  isToday,
  isThisWeek,
  isThisMonth,
  addDays,
  addMonths,
  getWeekday,
  getTimeperiod,
  getFriendlyTime
}