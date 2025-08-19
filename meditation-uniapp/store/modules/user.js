import { get, post } from '@/utils/request'
import { STORAGE_KEYS, appConfig } from '@/common/config.js'

const user = {
	namespaced: true,

	state: {
		// 用户token
		token: '',
		// 用户信息
		userInfo: null,
		// 用户权限
		permissions: [],
		// 用户角色
		roles: [],
		// 登录状态
		isLogin: false,
		// 当前门店信息
		tenantInfo: null,
		// 微信openid
		openid: '',
		// 用户scope
		scope: ''
	},

	mutations: {
		// 设置token
		SET_TOKEN(state, token) {
			state.token = token
			state.isLogin = !!token

			// 存储到本地 - 修复：使用meditation_前缀保持一致
			if (token) {
				uni.setStorageSync(STORAGE_KEYS.TOKEN, token)
			} else {
				uni.removeStorageSync(STORAGE_KEYS.TOKEN)
			}
		},

		// 设置用户信息
		SET_USER_INFO(state, userInfo) {
			state.userInfo = userInfo

			// 存储到本地
			// 存储到本地 - 修复：使用meditation_前缀保持一致
			if (userInfo) {
				uni.setStorageSync(STORAGE_KEYS.USER_INFO, userInfo)
			} else {
				uni.removeStorageSync(STORAGE_KEYS.USER_INFO)
			}
		},

		// 设置用户权限
		SET_PERMISSIONS(state, permissions) {
			state.permissions = permissions
		},

		// 设置用户角色
		SET_ROLES(state, roles) {
			state.roles = roles
		},

		// 设置门店信息
		SET_TENANT_INFO(state, tenantInfo) {
			state.tenantInfo = tenantInfo

			// 存储到本地 - 修复：使用meditation_前缀保持一致
			if (tenantInfo) {
				uni.setStorageSync(STORAGE_KEYS.TENANT_ID, tenantInfo.id || tenantInfo.tenantId)
			} else {
				uni.removeStorageSync(STORAGE_KEYS.TENANT_ID)
			}
		},

		// 设置微信openid
		SET_OPENID(state, openid) {
			state.openid = openid
			if (openid) {
				uni.setStorageSync(STORAGE_KEYS.OPENID, openid)
			} else {
				uni.removeStorageSync(STORAGE_KEYS.OPENID)
			}
		},

		// 设置用户scope
		SET_SCOPE(state, scope) {
			state.scope = scope
			if (scope) {
				uni.setStorageSync(STORAGE_KEYS.SCOPE, scope)
			} else {
				uni.removeStorageSync(STORAGE_KEYS.SCOPE)
			}
		},

		// 清除用户信息
		CLEAR_USER_INFO(state) {
			state.token = ''
			state.userInfo = null
			state.permissions = []
			state.roles = []
			state.isLogin = false
			state.tenantInfo = null
			state.openid = ''
			state.scope = ''

			// 清除本地存储 - 修复：使用meditation_前缀保持一致
			uni.removeStorageSync(STORAGE_KEYS.TOKEN)
			uni.removeStorageSync(STORAGE_KEYS.USER_INFO)
			uni.removeStorageSync(STORAGE_KEYS.TENANT_ID)
			uni.removeStorageSync(STORAGE_KEYS.OPENID)
			uni.removeStorageSync(STORAGE_KEYS.SCOPE)
		}
	},

	getters: {
		// 获取登录状态
		isLogin: state => state.isLogin,
		
		// 获取用户token
		token: state => state.token,
		
		// 获取用户信息
		userInfo: state => state.userInfo,
		
		// 获取用户权限
		permissions: state => state.permissions,
		
		// 获取用户角色
		roles: state => state.roles,
		
		// 获取租户信息
		tenantInfo: state => state.tenantInfo,
		
		// 获取微信openid
		openid: state => state.openid,
		
		// 获取用户scope
		scope: state => state.scope,
		
		// 检查是否有特定权限
		hasPermission: state => permission => {
			return state.permissions && state.permissions.includes(permission)
		},
		
		// 检查是否有特定角色
		hasRole: state => role => {
			return state.roles && state.roles.includes(role)
		}
	},

	actions: {
		// 小程序登录
		wxLogin({
			commit
		}, params = {}) {
			return new Promise(async (resolve, reject) => {
				try {
					// 获取微信登录凭证
					const [error, loginRes] = await uni.login({ provider: 'weixin' })
					if (error || loginRes.errMsg !== 'login:ok') {
						throw new Error('微信登录失败')
					}

					const code = loginRes.code

					// 构建登录参数 - 使用全局配置参数
					const loginData = {
						xcxCode: code,
						appid: params.appid || appConfig.wechatAppId,
						clientId: params.clientId || appConfig.clientId,
						grantType: 'xcx',
						tenantId: params.tenantId || appConfig.defaultTenantId
					}

					// 调用后端登录接口
					const result = await post('/xcx/auth/login', loginData, '登录中...')

					if (result.code === 200) {
						// 处理登录返回数据
						const data = result.data || {}
						const token = data.access_token || data.accessToken || data.token
						const userInfo = data.user || data.userInfo || {}
						const permissions = data.permissions || []
						const roles = data.roles || []
						const tenantInfo = data.tenantInfo || null
						const openid = data.openid || ''
						const scope = data.scope || ''

						// 保存到store
						commit('SET_TOKEN', token)
						commit('SET_USER_INFO', userInfo)
						commit('SET_PERMISSIONS', permissions)
						commit('SET_ROLES', roles)
						commit('SET_TENANT_INFO', tenantInfo)
						commit('SET_OPENID', openid)
						commit('SET_SCOPE', scope)

						resolve(result)
					} else {
						throw new Error(result.msg || '登录失败')
					}
				} catch (error) {
					reject(error)
				}
			})
		},

		// 用户登出
		logout({
			commit
		}) {
			return new Promise((resolve) => {
				commit('CLEAR_USER_INFO')

				// 跳转到首页（小程序一般不需要登录页）
				uni.reLaunch({
					url: '/pages/home/index'
				})

				resolve()
			})
		},

		// 检查权限
		checkPermission({
			state
		}, permission) {
			if (!state.permissions || state.permissions.length === 0) {
				return false
			}
			return state.permissions.includes(permission)
		},

		// 检查角色
		checkRole({
			state
		}, role) {
			if (!state.roles || state.roles.length === 0) {
				return false
			}
			return state.roles.includes(role)
		},

		// 获取用户信息
		getUserInfo({
			state
		}) {
			return state.userInfo
		},

		// 获取用户token
		getToken({
			state
		}) {
			return state.token
		},

		// 获取微信openid
		getOpenid() {
			return uni.getStorageSync(STORAGE_KEYS.OPENID) || ''
		},

		// 获取用户scope
		getScope() {
			return uni.getStorageSync(STORAGE_KEYS.SCOPE) || ''
		}
	}
}

export default user
