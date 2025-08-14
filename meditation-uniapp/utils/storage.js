/**
 * 缓存工具类
 * 封装uni.storage API，提供更便捷的缓存操作
 */

/**
 * 设置缓存（同步）
 * @param {String} key 缓存键
 * @param {Any} value 缓存值
 * @param {Number} expire 过期时间（秒）
 */
export function setStorageSync(key, value, expire = 0) {
	try {
		const data = {
			value: value,
			timestamp: Date.now()
		}
		
		if (expire > 0) {
			data.expire = Date.now() + expire * 1000
		}
		
		uni.setStorageSync(key, JSON.stringify(data))
		return true
	} catch (error) {
		console.error('设置缓存失败:', error)
		return false
	}
}

/**
 * 获取缓存（同步）
 * @param {String} key 缓存键
 * @returns {Any} 缓存值
 */
export function getStorageSync(key) {
	try {
		const dataStr = uni.getStorageSync(key)
		if (!dataStr) return null
		
		const data = JSON.parse(dataStr)
		
		// 检查是否过期
		if (data.expire && data.expire < Date.now()) {
			uni.removeStorageSync(key)
			return null
		}
		
		return data.value
	} catch (error) {
		console.error('获取缓存失败:', error)
		return null
	}
}

/**
 * 设置缓存（异步）
 * @param {String} key 缓存键
 * @param {Any} value 缓存值
 * @param {Number} expire 过期时间（秒）
 */
export function setStorage(key, value, expire = 0) {
	return new Promise((resolve, reject) => {
		try {
			const data = {
				value: value,
				timestamp: Date.now()
			}
			
			if (expire > 0) {
				data.expire = Date.now() + expire * 1000
			}
			
			uni.setStorage({
				key: key,
				data: JSON.stringify(data),
				success: () => resolve(true),
				fail: (err) => {
					console.error('设置缓存失败:', err)
					reject(err)
				}
			})
		} catch (error) {
			console.error('设置缓存失败:', error)
			reject(error)
		}
	})
}

/**
 * 获取缓存（异步）
 * @param {String} key 缓存键
 * @returns {Promise<Any>} 缓存值
 */
export function getStorage(key) {
	return new Promise((resolve, reject) => {
		uni.getStorage({
			key: key,
			success: (res) => {
				try {
					const data = JSON.parse(res.data)
					
					// 检查是否过期
					if (data.expire && data.expire < Date.now()) {
						uni.removeStorage({ key })
						resolve(null)
					} else {
						resolve(data.value)
					}
				} catch (error) {
					console.error('解析缓存失败:', error)
					resolve(null)
				}
			},
			fail: () => {
				resolve(null)
			}
		})
	})
}

/**
 * 移除缓存
 * @param {String} key 缓存键
 */
export function removeStorage(key) {
	return new Promise((resolve) => {
		uni.removeStorage({
			key: key,
			success: () => resolve(true),
			fail: () => resolve(false)
		})
	})
}

/**
 * 清空所有缓存
 */
export function clearStorage() {
	return new Promise((resolve) => {
		uni.clearStorage({
			success: () => resolve(true),
			fail: () => resolve(false)
		})
	})
}

/**
 * 获取缓存信息
 */
export function getStorageInfo() {
	return new Promise((resolve, reject) => {
		uni.getStorageInfo({
			success: (res) => resolve(res),
			fail: (err) => reject(err)
		})
	})
}

/**
 * 缓存管理类
 */
class CacheManager {
	constructor(prefix = 'cache_') {
		this.prefix = prefix
		this.memoryCache = new Map()
	}
	
	/**
	 * 生成缓存键
	 */
	getCacheKey(key) {
		return `${this.prefix}${key}`
	}
	
	/**
	 * 设置缓存（支持内存和持久化）
	 */
	set(key, value, options = {}) {
		const {
			expire = 0,
			memory = true,
			persistent = true
		} = options
		
		const cacheKey = this.getCacheKey(key)
		
		// 内存缓存
		if (memory) {
			this.memoryCache.set(cacheKey, {
				value,
				expire: expire > 0 ? Date.now() + expire * 1000 : 0
			})
		}
		
		// 持久化缓存
		if (persistent) {
			setStorageSync(cacheKey, value, expire)
		}
		
		return true
	}
	
	/**
	 * 获取缓存（优先从内存获取）
	 */
	get(key) {
		const cacheKey = this.getCacheKey(key)
		
		// 先从内存获取
		if (this.memoryCache.has(cacheKey)) {
			const data = this.memoryCache.get(cacheKey)
			
			// 检查是否过期
			if (data.expire && data.expire < Date.now()) {
				this.memoryCache.delete(cacheKey)
			} else {
				return data.value
			}
		}
		
		// 从持久化存储获取
		const value = getStorageSync(cacheKey)
		
		// 如果存在，同步到内存
		if (value !== null) {
			this.memoryCache.set(cacheKey, {
				value,
				expire: 0
			})
		}
		
		return value
	}
	
	/**
	 * 移除缓存
	 */
	remove(key) {
		const cacheKey = this.getCacheKey(key)
		
		// 移除内存缓存
		this.memoryCache.delete(cacheKey)
		
		// 移除持久化缓存
		removeStorage(cacheKey)
		
		return true
	}
	
	/**
	 * 清空缓存
	 */
	clear() {
		// 清空内存缓存
		this.memoryCache.clear()
		
		// 清空匹配前缀的持久化缓存
		uni.getStorageInfo({
			success: (res) => {
				res.keys.forEach(key => {
					if (key.startsWith(this.prefix)) {
						uni.removeStorageSync(key)
					}
				})
			}
		})
	}
	
	/**
	 * 获取缓存大小
	 */
	getSize() {
		return this.memoryCache.size
	}
}

// 创建默认缓存管理器实例
export const cacheManager = new CacheManager()

// 页面数据缓存
export const pageCache = new CacheManager('page_')

// API响应缓存
export const apiCache = new CacheManager('api_')

/**
 * 缓存装饰器（用于方法缓存）
 * @param {Number} expire 过期时间（秒）
 */
export function cacheable(expire = 300) {
	return function(target, propertyKey, descriptor) {
		const originalMethod = descriptor.value
		
		descriptor.value = async function(...args) {
			// 生成缓存键
			const cacheKey = `${propertyKey}_${JSON.stringify(args)}`
			
			// 尝试获取缓存
			const cached = apiCache.get(cacheKey)
			if (cached !== null) {
				console.log(`[Cache Hit] ${propertyKey}`)
				return cached
			}
			
			// 执行原方法
			const result = await originalMethod.apply(this, args)
			
			// 缓存结果
			if (result) {
				apiCache.set(cacheKey, result, { expire })
			}
			
			return result
		}
		
		return descriptor
	}
}

// 导出默认对象
export default {
	setStorageSync,
	getStorageSync,
	setStorage,
	getStorage,
	removeStorage,
	clearStorage,
	getStorageInfo,
	cacheManager,
	pageCache,
	apiCache,
	cacheable
}