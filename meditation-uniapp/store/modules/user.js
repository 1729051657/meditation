import { get, post } from '@/utils/request'

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
		tenantInfo: null
	},

	mutations: {
		// 设置token
		SET_TOKEN(state, token) {
			state.token = token
			state.isLogin = !!token

			// 存储到本地 - 修复：使用merchant_前缀保持一致
			if (token) {
				uni.setStorageSync('edu_token', token)
			} else {
				uni.removeStorageSync('edu_token')
			}
		},

		// 设置用户信息
		SET_USER_INFO(state, userInfo) {
			state.userInfo = userInfo

			// 存储到本地
			// 存储到本地 - 修复：使用merchant_前缀保持一致
			if (userInfo) {
				uni.setStorageSync('edu_user_info', userInfo)
			} else {
				uni.removeStorageSync('edu_user_info')
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

			// 存储到本地 - 修复：使用merchant_前缀保持一致
			if (tenantInfo) {
				uni.setStorageSync('edu_tenant_id', tenantInfo.id || tenantInfo.tenantId)
			} else {
				uni.removeStorageSync('edu_tenant_id')
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

			// 清除本地存储 - 修复：使用merchant_前缀保持一致
			uni.removeStorageSync('edu_token')
			uni.removeStorageSync('edu_user_info')
			uni.removeStorageSync('edu_tenant_id')
		}
	},

	actions: {
		// 用户登录
		login({
			commit
		}, loginForm) {
			return new Promise((resolve, reject) => {
				post('/xcx/auth/login', loginForm, '登录中...')
					.then((data) => {
						if (data.code === 200) {
							const { token, userInfo, permissions, roles, tenantInfo } = data.data
							commit('SET_TOKEN', token)
							commit('SET_USER_INFO', userInfo)
							commit('SET_PERMISSIONS', permissions)
							commit('SET_ROLES', roles)
							commit('SET_TENANT_INFO', tenantInfo)
							resolve(data)
						} else {
							reject(data)
						}
					})
					.catch((error) => reject(error))
			})
		},

		// 获取用户信息
		getUserInfo({
			commit,
			state
		}) {
			return new Promise((resolve, reject) => {
				if (!state.token) {
					reject('未登录')
					return
				}
				get('/system/user/getInfo')
					.then((data) => {
						if (data.code === 200) {
							const { userInfo, permissions, roles } = data.data
							commit('SET_USER_INFO', userInfo)
							commit('SET_PERMISSIONS', permissions)
							commit('SET_ROLES', roles)
							resolve(data)
						} else {
							reject(data)
						}
					})
					.catch((error) => reject(error))
			})
		},

		// 用户登出
		logout({
			commit
		}) {
			return new Promise((resolve) => {
				commit('CLEAR_USER_INFO')

				// 跳转到登录页
				uni.reLaunch({
					url: '/pages/login/login'
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
		}
	}
}

export default user