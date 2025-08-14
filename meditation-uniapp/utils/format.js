/**
 * 格式化工具函数
 * 用于美容院管理系统的数据格式化
 */

/**
 * 格式化金额
 * @param {number} amount 金额
 * @param {number} precision 精度，默认2位小数
 * @returns {string} 格式化后的金额字符串
 */
export function formatAmount(amount, precision = 2) {
  if (!amount || isNaN(amount)) return '0.00'
  
  return Number(amount).toFixed(precision)
}

/**
 * 格式化金额（带千分位分隔符）
 * @param {number} amount 金额
 * @param {number} precision 精度，默认2位小数
 * @returns {string} 格式化后的金额字符串
 */
export function formatAmountWithComma(amount, precision = 2) {
  if (!amount || isNaN(amount)) return '0.00'
  
  const formatted = Number(amount).toFixed(precision)
  return formatted.replace(/\B(?=(\d{3})+(?!\d))/g, ',')
}

/**
 * 兼容解析日期函数（解决 iOS 无法解析 "yyyy-MM-dd HH:mm:ss" 等格式问题）
 * @param {string|number|Date} dateInput 日期输入
 * @returns {Date} 可用的 Date 对象，解析失败返回 Invalid Date
 */
export function parseDateCompat(dateInput) {
  if (!dateInput && dateInput !== 0) return new Date('')
  // 已是 Date
  if (dateInput instanceof Date) return dateInput
  // 数字时间戳
  if (typeof dateInput === 'number') return new Date(dateInput)

  if (typeof dateInput === 'string') {
    // 先尝试原生解析
    let d = new Date(dateInput)
    if (!isNaN(d.getTime())) return d

    // 1) 将日期部分中的 '-' 替换为 '/'
    d = new Date(dateInput.replace(/-/g, '/'))
    if (!isNaN(d.getTime())) return d

    // 2) 使用 ISO T 分隔符
    const tFormat = dateInput.replace(' ', 'T')
    d = new Date(tFormat)
    if (!isNaN(d.getTime())) return d
  }
  // 最终仍解析失败
  return new Date('')
}

/**
 * 格式化日期
 * @param {string|Date} date 日期
 * @param {string} format 格式，默认YYYY-MM-DD
 * @returns {string} 格式化后的日期字符串
 */
export function formatDate(date, format = 'YYYY-MM-DD') {
  if (!date) return ''
  const d = parseDateCompat(date)
  if (isNaN(d.getTime())) return ''
  
  const year = d.getFullYear()
  const month = String(d.getMonth() + 1).padStart(2, '0')
  const day = String(d.getDate()).padStart(2, '0')
  const hour = String(d.getHours()).padStart(2, '0')
  const minute = String(d.getMinutes()).padStart(2, '0')
  const second = String(d.getSeconds()).padStart(2, '0')
  
  return format
    .replace('YYYY', year)
    .replace('MM', month)
    .replace('DD', day)
    .replace('HH', hour)
    .replace('mm', minute)
    .replace('ss', second)
}

/**
 * 格式化时间
 * @param {string|Date} date 日期时间
 * @returns {string} 格式化后的时间字符串
 */
export function formatTime(date) {
  if (!date) return ''
  
  const now = new Date()
  const target = parseDateCompat(date)
  
  if (isNaN(target.getTime())) return ''
  
  const diff = now.getTime() - target.getTime()
  const diffMinutes = Math.floor(diff / (1000 * 60))
  const diffHours = Math.floor(diff / (1000 * 60 * 60))
  const diffDays = Math.floor(diff / (1000 * 60 * 60 * 24))
  
  if (diffMinutes < 1) {
    return '刚刚'
  } else if (diffMinutes < 60) {
    return `${diffMinutes}分钟前`
  } else if (diffHours < 24) {
    return `${diffHours}小时前`
  } else if (diffDays < 7) {
    return `${diffDays}天前`
  } else {
    return formatDate(date, 'MM-DD')
  }
}

/**
 * 格式化手机号
 * @param {string} phone 手机号
 * @returns {string} 格式化后的手机号
 */
export function formatPhone(phone) {
  if (!phone) return ''
  
  const cleanPhone = phone.replace(/\D/g, '')
  
  if (cleanPhone.length === 11) {
    return cleanPhone.replace(/(\d{3})(\d{4})(\d{4})/, '$1****$3')
  }
  
  return phone
}

/**
 * 格式化身份证号
 * @param {string} idCard 身份证号
 * @returns {string} 格式化后的身份证号
 */
export function formatIdCard(idCard) {
  if (!idCard) return ''
  
  if (idCard.length === 18) {
    return idCard.replace(/(\d{6})\d{8}(\d{4})/, '$1********$2')
  }
  
  return idCard
}

/**
 * 格式化文件大小
 * @param {number} bytes 字节数
 * @returns {string} 格式化后的文件大小
 */
export function formatFileSize(bytes) {
  if (!bytes) return '0 B'
  
  const sizes = ['B', 'KB', 'MB', 'GB', 'TB']
  const i = Math.floor(Math.log(bytes) / Math.log(1024))
  
  return Math.round(bytes / Math.pow(1024, i) * 100) / 100 + ' ' + sizes[i]
}

/**
 * 格式化百分比
 * @param {number} value 数值
 * @param {number} total 总数
 * @param {number} precision 精度，默认1位小数
 * @returns {string} 格式化后的百分比
 */
export function formatPercentage(value, total, precision = 1) {
  if (!total || total === 0) return '0%'
  
  const percentage = (value / total) * 100
  return percentage.toFixed(precision) + '%'
}

/**
 * 格式化距离
 * @param {number} distance 距离（米）
 * @returns {string} 格式化后的距离
 */
export function formatDistance(distance) {
  if (!distance) return '0m'
  
  if (distance < 1000) {
    return Math.round(distance) + 'm'
  } else {
    return (distance / 1000).toFixed(1) + 'km'
  }
}

/**
 * 格式化时长
 * @param {number} duration 时长（秒）
 * @returns {string} 格式化后的时长
 */
export function formatDuration(duration) {
  if (!duration) return '0秒'
  
  const hours = Math.floor(duration / 3600)
  const minutes = Math.floor((duration % 3600) / 60)
  const seconds = duration % 60
  
  if (hours > 0) {
    return `${hours}小时${minutes}分钟`
  } else if (minutes > 0) {
    return `${minutes}分钟${seconds}秒`
  } else {
    return `${seconds}秒`
  }
}

/**
 * 获取星期几
 * @param {string|Date} date 日期
 * @returns {string} 星期几
 */
export function getWeekday(date) {
  if (!date) return ''
  
  const d = parseDateCompat(date)
  if (isNaN(d.getTime())) return ''
  
  const weekdays = ['日', '一', '二', '三', '四', '五', '六']
  return '星期' + weekdays[d.getDay()]
}

/**
 * 判断是否为今天
 * @param {string|Date} date 日期
 * @returns {boolean} 是否为今天
 */
export function isToday(date) {
  if (!date) return false
  
  const today = new Date()
  const target = parseDateCompat(date)
  
  return today.getFullYear() === target.getFullYear() &&
         today.getMonth() === target.getMonth() &&
         today.getDate() === target.getDate()
}

/**
 * 判断是否为本周
 * @param {string|Date} date 日期
 * @returns {boolean} 是否为本周
 */
export function isThisWeek(date) {
  if (!date) return false
  
  const today = new Date()
  const target = parseDateCompat(date)
  
  // 获取本周一的日期
  const startOfWeek = new Date(today)
  startOfWeek.setDate(today.getDate() - today.getDay() + 1)
  startOfWeek.setHours(0, 0, 0, 0)
  
  // 获取本周日的日期
  const endOfWeek = new Date(startOfWeek)
  endOfWeek.setDate(startOfWeek.getDate() + 6)
  endOfWeek.setHours(23, 59, 59, 999)
  
  return target >= startOfWeek && target <= endOfWeek
}

/**
 * 脱敏处理
 * @param {string} str 原字符串
 * @param {number} start 开始位置
 * @param {number} end 结束位置
 * @param {string} mask 掩码字符
 * @returns {string} 脱敏后的字符串
 */
export function desensitize(str, start = 0, end = 0, mask = '*') {
  if (!str) return ''
  
  const len = str.length
  if (start + end >= len) return str
  
  const startStr = str.substring(0, start)
  const endStr = str.substring(len - end)
  const maskStr = mask.repeat(len - start - end)
  
  return startStr + maskStr + endStr
}

/**
 * 格式化日期时间
 * @param {string|Date} datetime - 日期时间
 * @param {string} format - 格式化模式，默认 'YYYY-MM-DD HH:mm:ss'
 * @returns {string} 格式化后的日期时间字符串
 */
export function formatDateTime(datetime, format = 'YYYY-MM-DD HH:mm:ss') {
  if (!datetime) return ''
  
  const d = parseDateCompat(datetime)
  if (isNaN(d.getTime())) return ''
  
  const year = d.getFullYear()
  const month = String(d.getMonth() + 1).padStart(2, '0')
  const day = String(d.getDate()).padStart(2, '0')
  const hours = String(d.getHours()).padStart(2, '0')
  const minutes = String(d.getMinutes()).padStart(2, '0')
  const seconds = String(d.getSeconds()).padStart(2, '0')
  
  switch (format) {
    case 'YYYY-MM-DD HH:mm:ss':
      return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`
    case 'YYYY-MM-DD HH:mm':
      return `${year}-${month}-${day} ${hours}:${minutes}`
    case 'MM-DD HH:mm':
      return `${month}-${day} ${hours}:${minutes}`
    case 'HH:mm:ss':
      return `${hours}:${minutes}:${seconds}`
    case 'HH:mm':
      return `${hours}:${minutes}`
    case 'YYYY年MM月DD日 HH:mm:ss':
      return `${year}年${month}月${day}日 ${hours}:${minutes}:${seconds}`
    case 'YYYY年MM月DD日 HH:mm':
      return `${year}年${month}月${day}日 ${hours}:${minutes}`
    default:
      return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`
  }
}

/**
 * 格式化会员卡号
 * @param {string} cardNumber - 卡号
 * @param {boolean} mask - 是否脱敏，默认false
 * @returns {string} 格式化后的卡号
 */
export function formatCardNumber(cardNumber, mask = false) {
  if (!cardNumber) return ''
  
  if (mask) {
    // 脱敏处理：显示前4位和后4位
    if (cardNumber.length >= 8) {
      const start = cardNumber.substring(0, 4)
      const end = cardNumber.substring(cardNumber.length - 4)
      const middle = '*'.repeat(cardNumber.length - 8)
      return start + middle + end
    }
  }
  
  // 每4位添加空格
  return cardNumber.replace(/(.{4})/g, '$1 ').trim()
}

/**
 * 格式化相对时间
 * @param {string|Date} date - 日期
 * @returns {string} 相对时间字符串
 */
export function formatRelativeTime(date) {
  if (!date) return ''
  
  const d = parseDateCompat(date)
  if (isNaN(d.getTime())) return ''
  
  const now = new Date()
  const diff = now.getTime() - d.getTime()
  const seconds = Math.floor(diff / 1000)
  const minutes = Math.floor(seconds / 60)
  const hours = Math.floor(minutes / 60)
  const days = Math.floor(hours / 24)
  const months = Math.floor(days / 30)
  const years = Math.floor(months / 12)
  
  if (years > 0) {
    return `${years}年前`
  } else if (months > 0) {
    return `${months}个月前`
  } else if (days > 0) {
    return `${days}天前`
  } else if (hours > 0) {
    return `${hours}小时前`
  } else if (minutes > 0) {
    return `${minutes}分钟前`
  } else if (seconds > 10) {
    return `${seconds}秒前`
  } else {
    return '刚刚'
  }
}

/**
 * 格式化时间显示（24小时制）
 * @param {string|Date} date 日期时间
 * @returns {string} 格式化后的时间字符串（HH:mm）
 */
export function formatDisplayTime(date) {
  if (!date) return ''
  
  const d = parseDateCompat(date)
  if (isNaN(d.getTime())) return ''
  
  const hour = String(d.getHours()).padStart(2, '0')
  const minute = String(d.getMinutes()).padStart(2, '0')
  
  return `${hour}:${minute}`
}

/**
 * 格式化今日日期显示
 * @param {string|Date} date 日期
 * @returns {string} 格式化后的日期字符串
 */
export function formatTodayDate(date) {
  if (!date) return ''
  
  const d = parseDateCompat(date)
  if (isNaN(d.getTime())) return ''
  
  const month = String(d.getMonth() + 1).padStart(2, '0')
  const day = String(d.getDate()).padStart(2, '0')
  const weekday = getWeekday(date)
  
  return `${month}月${day}日 ${weekday}`
}

/**
 * 格式化日期部分（不含星期几）
 * @param {string|Date} date 日期
 * @returns {string} 格式化后的日期字符串
 */
export function formatDateOnly(date) {
  if (!date) return ''
  
  const d = parseDateCompat(date)
  if (isNaN(d.getTime())) return ''
  
  const month = String(d.getMonth() + 1).padStart(2, '0')
  const day = String(d.getDate()).padStart(2, '0')
  
  return `${month}月${day}日`
}

/**
 * 格式化星期几部分
 * @param {string|Date} date 日期
 * @returns {string} 格式化后的星期几字符串
 */
export function formatWeekdayOnly(date) {
  if (!date) return ''
  
  return getWeekday(date)
}

/**
 * 格式化数字显示（自动处理千分位）
 * @param {number} num 数字
 * @returns {string} 格式化后的数字字符串
 */
export function formatNumber(num) {
  if (!num || isNaN(num)) return '0'
  
  return Number(num).toLocaleString()
}

/**
 * 格式化预约时间段
 * @param {string|Date} startTime 开始时间
 * @param {string|Date} endTime 结束时间
 * @returns {string} 格式化后的时间段
 */
export function formatTimeSlot(startTime, endTime) {
  if (!startTime) return ''
  
  const start = formatDisplayTime(startTime)
  
  if (!endTime) return start
  
  const end = formatDisplayTime(endTime)
  return `${start}-${end}`
}

/**
 * 格式化增长率
 * @param {number} rate 增长率
 * @param {boolean} showSign 是否显示符号
 * @returns {string} 格式化后的增长率
 */
export function formatGrowthRate(rate, showSign = true) {
  if (!rate || isNaN(rate)) return '0%'
  
  const formattedRate = Math.abs(rate).toFixed(1) + '%'
  
  if (!showSign) return formattedRate
  
  return rate > 0 ? `+${formattedRate}` : rate < 0 ? `-${formattedRate}` : formattedRate
}

/**
 * 格式化营业额显示（自动选择单位）
 * @param {number} amount 金额
 * @returns {string} 格式化后的金额字符串
 */
export function formatRevenueDisplay(amount) {
  if (!amount || isNaN(amount)) return '0'
  
  if (amount >= 10000) {
    return (amount / 10000).toFixed(1) + '万'
  } else if (amount >= 1000) {
    return (amount / 1000).toFixed(1) + 'k'
  } else {
    return amount.toString()
  }
}

/**
 * 获取状态对应的颜色类型
 * @param {string} statusColor 状态颜色
 * @returns {string} 对应的类型
 */
export function getStatusTypeByColor(statusColor) {
  const colorMap = {
    '#faad14': 'warning',
    '#1890ff': 'primary',
    '#52c41a': 'success',
    '#8c8c8c': 'info',
    '#ff4d4f': 'danger',
    '#fa8c16': 'warning',
    '#722ed1': 'purple',
    '#13c2c2': 'cyan'
  }
  return colorMap[statusColor] || 'info'
}

/**
 * 格式化卡片余额显示
 * @param {number} balance 余额
 * @param {string} unit 单位（如：元、次）
 * @returns {string} 格式化后的余额字符串
 */
export function formatCardBalance(balance, unit = '元') {
  if (!balance || isNaN(balance)) return `0${unit}`
  
  if (unit === '元') {
    return `${formatAmount(balance)}${unit}`
  } else {
    return `${Math.floor(balance)}${unit}`
  }
}

/**
 * 格式化会员等级显示
 * @param {string} level 等级
 * @param {string} levelName 等级名称
 * @returns {string} 格式化后的等级字符串
 */
export function formatMemberLevel(level, levelName) {
  if (!level && !levelName) return '普通会员'
  
  return levelName || `V${level}` || '普通会员'
}

// 导出专用的时间格式化函数（不冲突）
export { formatDisplayTime as formatTimeOnly }

// 导出专用的日期格式化函数（不冲突）
export { formatTodayDate as formatDateWithWeekday } 