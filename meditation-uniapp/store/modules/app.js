const app = {
	namespaced: true,
	
	state: {
		// 系统信息
		systemInfo: {},
		// 网络状态
		networkType: 'unknown',
		// 应用版本
		version: '1.0.0',
		// 自定义导航栏高度
		customBarHeight: 0,
		// 状态栏高度
		statusBarHeight: 0,
		// 底部安全区域高度
		safeAreaBottom: 0,
		// 屏幕方向
		orientation: 'portrait',
		// 主题色
		primaryColor: '#007aff'
	},
	
	mutations: {
		// 设置系统信息
		SET_SYSTEM_INFO(state, info) {
			state.systemInfo = info
			state.statusBarHeight = info.statusBarHeight || 0
			state.safeAreaBottom = info.safeAreaInsets ? info.safeAreaInsets.bottom : 0
			
			// 计算自定义导航栏高度
			// #ifdef MP-WEIXIN
			state.customBarHeight = info.statusBarHeight + 44
			// #endif
			// #ifdef APP-PLUS
			state.customBarHeight = info.statusBarHeight + 44
			// #endif
		},
		
		// 设置网络状态
		SET_NETWORK_TYPE(state, type) {
			state.networkType = type
		},
		
		// 设置屏幕方向
		SET_ORIENTATION(state, orientation) {
			state.orientation = orientation
		},
		
		// 设置主题色
		SET_PRIMARY_COLOR(state, color) {
			state.primaryColor = color
		}
	},
	
	actions: {
		// 获取系统信息
		getSystemInfo({ commit }) {
			return new Promise((resolve) => {
				uni.getSystemInfo({
					success: (res) => {
						commit('SET_SYSTEM_INFO', res)
						resolve(res)
					}
				})
			})
		},
		
		// 获取网络状态
		getNetworkType({ commit }) {
			return new Promise((resolve) => {
				uni.getNetworkType({
					success: (res) => {
						commit('SET_NETWORK_TYPE', res.networkType)
						resolve(res.networkType)
					}
				})
			})
		},
		
		// 监听网络状态变化
		onNetworkStatusChange({ commit }) {
			uni.onNetworkStatusChange((res) => {
				commit('SET_NETWORK_TYPE', res.networkType)
			})
		}
	}
}

export default app 