import {
	getToken,
	getTenantId,
	getDeptId,
	getClientId
} from '@/utils/auth'
import {
	baseUrl
} from '@/common/config'

// 登录弹窗锁，防止多次弹窗
let isLoginDialogShowing = false
// 等待重新登录的请求队列
let pendingRequests = []
const tansParams = (params) => {
	let result = ''
	for (const propName of Object.keys(params)) {
		const value = params[propName];
		var part = encodeURIComponent(propName) + "=";
		if (value !== null && typeof(value) !== "undefined") {
			if (typeof value === 'object') {
				for (const key of Object.keys(value)) {
					if (value[key] !== null && typeof(value[key]) !== 'undefined') {
						let params = propName + '[' + key + ']';
						var subPart = encodeURIComponent(params) + "=";
						result += subPart + encodeURIComponent(value[key]) + "&";
					}
				}
			} else {
				result += part + encodeURIComponent(value) + "&";
			}
		}
	}
	return result
}
// 统一请求方法
const request = (options) => {
	const {
		url,
		method = 'GET',
		data,
		loading = true,
		loadingText = '加载中...',
		type,
		showError = true
	} = options

	// 显示加载提示
	if (loading && typeof loading === 'string') {
		uni.showLoading({
			title: loading,
			mask: true
		})
	} else if (loading === true) {
		uni.showLoading({
			title: loadingText,
			mask: true
		})
	}

	return new Promise((resolve, reject) => {
		const token = getToken()
		const tenantId = getTenantId()
		const deptId = getDeptId()
		const clientId = getClientId()
		console.log('请求tenantId:', tenantId, '请求deptId:', deptId, '请求clientId:', clientId, '请求URL:',
			baseUrl + url, '请求数据:', data)

		const headers = {
			'Authorization': token ? ('Bearer ' + token) : '',
			'Content-Type': type || 'application/json;charset=UTF-8'
		}

		// 添加租户ID到请求头
		if (tenantId) {
			headers['tenant-id'] = tenantId
		}

		// 添加部门ID到请求头
		if (deptId) {
			headers['dept-id'] = deptId
		}

		// 添加客户端ID到请求头
		if (clientId) {
			headers['clientid'] = clientId
		}

		uni.request({
			url: baseUrl + url,
			method,
			data,
			header: headers,
			success: (res) => {
				console.log('请求成功返回:', res)
				const result = res.data
				// 兼容你的后端返回结构
				if (result.code === 200) {
					resolve(result)
				} else if (result.code === 401) {
					// 防止重复弹出登录提示
					if (!isLoginDialogShowing) {
						isLoginDialogShowing = true

						// 清空之前的待处理请求队列
						pendingRequests = []

						uni.showModal({
							title: '提示',
							content: '登录状态已过期，请重新登录',
							showCancel: false,
							success: () => {
								isLoginDialogShowing = false
								// 清除本地存储的认证信息
								uni.removeStorageSync('merchant_token')
								uni.removeStorageSync('merchant_user_info')
								// 跳转到登录页
								uni.reLaunch({
									url: '/pages/login/login'
								})
							},
							fail: () => {
								isLoginDialogShowing = false
							}
						})
					}
					reject(new Error('登录状态已过期'))
				} else {
					console.error('请求业务错误:', result)
					if (showError) {
						uni.showToast({
							title: result.msg || '请求失败',
							icon: 'none'
						})
					}
					reject(new Error(result.msg || '请求失败'))
				}
			},
			fail: (error) => {
				console.error('请求网络错误:', error)
				if (showError) {
					uni.showToast({
						title: '网络请求失败',
						icon: 'none'
					})
				}
				reject(new Error('网络请求失败'))
			},
			complete: () => {
				if (loading) {
					uni.hideLoading()
				}
			}
		})
	})
}

// 封装GET请求
export const get = (url, data, loadingText, showError = true) => {

	if (data) {
		url = url + '?' + tansParams(data);
		url = url.slice(0, -1);
	}
	return request({
		url,
		method: 'GET',
		loading: loadingText,
		showError
	})
}

// 封装POST请求
export const post = (url, data, loadingText, showError = true) => {
	return request({
		url,
		method: 'POST',
		data,
		loading: loadingText,
		showError
	})
}

// 封装PUT请求
export const put = (url, data, loadingText, showError = true) => {
	return request({
		url,
		method: 'PUT',
		data,
		loading: loadingText,
		showError
	})
}

// 封装DELETE请求
export const del = (url, data, loadingText, showError = true) => {
	return request({
		url,
		method: 'DELETE',
		data,
		loading: loadingText,
		showError
	})
}

// 重置登录状态（用于登录成功后调用）
export const resetLoginState = () => {
	isLoginDialogShowing = false
	pendingRequests = []
}

// 检查是否正在显示登录弹窗
export const isShowingLoginDialog = () => {
	return isLoginDialogShowing
}

export default request