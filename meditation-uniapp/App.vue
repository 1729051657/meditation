<script>
import {
	mapState,
	mapActions
} from 'vuex'
import Vue from 'vue'
import store from './store/index.js'
import updateCustomBarInfo from './tuniao-ui/libs/function/updateCustomBarInfo.js'
import { isLoggedIn, getTenantId } from './utils/auth.js'
import { STORAGE_KEYS } from './common/config.js'

export default {
	name: 'App',
	computed: {
		...mapState('user', ['userInfo', 'token'])
	},
	onLaunch: function () {
		console.log('冥想空间应用启动')

		// 初始化应用
		this.initApplication()

		// 执行无感登录
		this.silentLogin()
		
		// 初始化全局音频管理器和恢复定时器
		this.initGlobalAudio()
	},
	onShow: function () {
		console.log('App Show')
		
		// 应用显示时恢复定时器
		this.$store.dispatch('timer/restoreTimer')
	},
	onHide: function () {
		console.log('App Hide')
	},
	methods: {
		...mapActions('user', ['wxLogin']),

		/**
		 * 应用初始化主方法
		 */
		initApplication() {
			// 1. 检查登录状态
			this.checkLoginStatus()

			// 2. 初始化系统信息
			this.initSystemInfo()

			// 3. 初始化状态栏信息
			this.initStatusBarInfo()

			// 4. 初始化微信小程序更新检测
			this.initWechatUpdate()
		},

		/**
		 * 初始化系统信息
		 */
		initSystemInfo() {
			uni.getSystemInfo({
				success: (e) => {
					// #ifndef H5
					// 获取手机系统版本
					const system = e.system.toLowerCase()
					const platform = e.platform.toLowerCase()

					// 判断设备类型
					if (platform.indexOf('ios') != -1 && (system.indexOf('ios') != -1 || system.indexOf('macos') != -1)) {
						Vue.prototype.SystemPlatform = 'apple'
					} else if (platform.indexOf('android') != -1 && system.indexOf('android') != -1) {
						Vue.prototype.SystemPlatform = 'android'
					} else {
						Vue.prototype.SystemPlatform = 'devtools'
					}
					// #endif

					// 存储系统信息到store
					this.$store.commit('app/SET_SYSTEM_INFO', e)
				}
			})
		},

		/**
		 * 初始化状态栏信息
		 */
		initStatusBarInfo() {
			// 设置状态栏样式
			// #ifdef APP-PLUS
			plus.navigator.setStatusBarStyle('dark')
			// #endif

			// 获取设备的状态栏信息和自定义顶栏信息
			updateCustomBarInfo().then((res) => {
				// 使用app模块的mutation来设置状态栏信息
				store.commit('app/SET_SYSTEM_INFO', {
					statusBarHeight: res.statusBarHeight,
					customBarHeight: res.customBarHeight
				})
			})
		},

		/**
		 * 初始化微信小程序更新检测
		 */
		initWechatUpdate() {
			// #ifdef MP-WEIXIN
			if (wx.canIUse('getUpdateManager')) {
				const updateManager = wx.getUpdateManager()

				updateManager.onCheckForUpdate((res) => {
					if (res.hasUpdate) {
						this.handleUpdateReady(updateManager)
						this.handleUpdateFailed(updateManager)
					}
				})
			} else {
				this.showUpdateError()
			}
			// #endif
		},

		/**
		 * 处理更新就绪
		 */
		handleUpdateReady(updateManager) {
			updateManager.onUpdateReady(() => {
				uni.showModal({
					title: '更新提示',
					content: '新版本已经准备就绪，是否需要重新启动应用？',
					success: (res) => {
						if (res.confirm) {
							uni.clearStorageSync() // 更新完成后刷新storage的数据
							updateManager.applyUpdate()
						}
					}
				})
			})
		},

		/**
		 * 处理更新失败
		 */
		handleUpdateFailed(updateManager) {
			updateManager.onUpdateFailed(() => {
				uni.showModal({
					title: '已有新版本上线',
					content: '小程序自动更新失败，请删除该小程序后重新搜索打开哟~~~',
					showCancel: false
				})
			})
		},

		/**
		 * 显示更新错误提示
		 */
		showUpdateError() {
			uni.showModal({
				title: '提示',
				content: '当前微信版本过低，无法使用该功能，请更新到最新的微信后再重试。',
				showCancel: false
			})
		},

		/**
		 * 检查登录状态
		 */
		checkLoginStatus() {
			const token = uni.getStorageSync(STORAGE_KEYS.TOKEN)
			const userInfo = uni.getStorageSync(STORAGE_KEYS.USER_INFO)
			const tenantId = uni.getStorageSync(STORAGE_KEYS.TENANT_ID)
			const openid = uni.getStorageSync(STORAGE_KEYS.OPENID)
			const scope = uni.getStorageSync(STORAGE_KEYS.SCOPE)

			if (token && userInfo) {
				// 检查token是否过期
				if (this.isTokenExpired(token)) {
					console.log('Token已过期，清除登录状态')
					this.clearLoginStatus()
					return
				}

				// 恢复完整的登录状态
				this.$store.commit('user/SET_TOKEN', token)
				this.$store.commit('user/SET_USER_INFO', userInfo)

				if (tenantId) {
					this.$store.commit('user/SET_TENANT_INFO', { tenantId })
				}

				if (openid) {
					this.$store.commit('user/SET_OPENID', openid)
				}

				if (scope) {
					this.$store.commit('user/SET_SCOPE', scope)
				}

				console.log('已恢复登录状态')
			}
		},

		// 获取微信openid
		getOpenid() {
			return uni.getStorageSync(STORAGE_KEYS.OPENID) || ''
		},

		// 获取用户scope
		getScope() {
			return uni.getStorageSync(STORAGE_KEYS.SCOPE) || ''
		},

		/**
		 * 清除登录状态
		 */
		clearLoginStatus() {
			console.log('清除之前的登录状态')
			// 清除store中的状态
			this.$store.commit('user/CLEAR_USER_INFO')
			// 清除本地存储
			uni.removeStorageSync(STORAGE_KEYS.TOKEN)
			uni.removeStorageSync(STORAGE_KEYS.USER_INFO)
			uni.removeStorageSync(STORAGE_KEYS.TENANT_ID)
			uni.removeStorageSync(STORAGE_KEYS.OPENID)
			uni.removeStorageSync(STORAGE_KEYS.SCOPE)
		},

		/**
		 * 检查token是否过期
		 */
		isTokenExpired(token) {
			try {
				// 解析JWT token（如果使用JWT）
				if (token && token.includes('.')) {
					const payload = JSON.parse(atob(token.split('.')[1]))
					const currentTime = Math.floor(Date.now() / 1000)

					// 检查exp字段（过期时间）
					if (payload.exp && payload.exp < currentTime) {
						return true
					}

					// 检查iat字段（签发时间），如果超过2小时认为过期
					if (payload.iat && (currentTime - payload.iat) > 7200) {
						return true
					}
				}

				// 如果不是JWT或没有过期信息，检查本地存储时间
				const tokenTime = uni.getStorageSync('meditation_token_time')
				if (tokenTime) {
					const currentTime = Date.now()
					const tokenAge = currentTime - parseInt(tokenTime)
					// 如果token超过2小时，认为过期
					if (tokenAge > 7200000) { // 2小时 = 7200000毫秒
						return true
					}
				}

				return false
			} catch (error) {
				console.error('检查token过期失败:', error)
				// 如果解析失败，认为token无效
				return true
			}
		},

		/**
		 * 执行无感登录
		 */
		async silentLogin() {
			// #ifdef MP-WEIXIN
			try {
				console.log('开始执行无感登录...')

				// 先清除之前的登录状态
				this.clearLoginStatus()

				// 显示加载提示
				uni.showLoading({
					title: '登录中...',
					mask: true
				})

				// 调用store中的wxLogin方法
				await this.wxLogin()

				// 隐藏加载提示
				uni.hideLoading()

				console.log('无感登录成功')

				// 登录成功后，可以获取用户信息等其他操作
				// 这里不需要跳转页面，让用户继续使用小程序

			} catch (error) {
				// 隐藏加载提示
				uni.hideLoading()

				console.error('无感登录失败:', error)

				// 如果是网络错误或其他可恢复的错误，可以稍后重试
				if (error.message && error.message.includes('网络')) {
					console.log('网络错误，稍后重试登录')
					// 可以设置一个定时器，稍后重试登录
					setTimeout(() => {
						this.silentLogin()
					}, 5000) // 5秒后重试
				}

				// 无感登录失败不影响用户使用，可以在需要登录的功能处再提示
				// 不显示错误提示，避免影响用户体验
			}
			// #endif

			// #ifndef MP-WEIXIN
			console.log('非微信小程序环境，跳过无感登录')
			// #endif
		},
		
		/**
		 * 初始化全局音频管理器
		 */
		initGlobalAudio() {
			// 初始化背景音频管理器
			const audioManager = this.$store.dispatch('timer/initAudioManager')
			
			// 初始化播放列表管理器
			this.$store.dispatch('playlist/initAudioManager')
			this.$store.dispatch('playlist/initPlaylist')
			
			// 恢复定时器（如果有）
			this.$store.dispatch('timer/restoreTimer')
			
			console.log('全局音频管理器初始化完成')
		}
	}
}
</script>

<style lang="scss">
/* 注意要写在第一行，同时给style标签加入lang="scss"属性 */
@import './tuniao-ui/index.scss';
@import './tuniao-ui/iconfont.css';

.music {
	width: 702rpx;
	height: 96rpx;
	background: #6D8BA1;
	border-radius: 52rpx;
	opacity: 0.7;
	backdrop-filter: blur(3px);
	position: fixed;
	bottom: 30rpx;
	left: 24rpx;
	z-index: 100000;
	display: flex;
	align-items: center;
	padding-right: 44rpx;
	box-sizing: border-box;

}

.musicimg {
	width: 112rpx;
	height: 112rpx;
	position: absolute;
	top: -10rpx;
	left: -2rpx;
	border-radius: 50%;
}

.timing {
	font-weight: 400;
	font-size: 24rpx;
	color: #FFFFFF;
	flex: 1;
	text-align: right;
}

.pause {
	width: 56rpx;
	height: 56rpx;
	margin-left: 14rpx;
}

.musicName {
	font-weight: 500;
	font-size: 32rpx;
	color: #FFFFFF;
	margin-left: 136rpx;
}
</style>