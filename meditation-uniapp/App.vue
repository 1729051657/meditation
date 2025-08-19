<script>
	import {
		mapState
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
		onLaunch: function() {
			console.log('冥想空间应用启动')
			
			// 初始化应用
			this.initApplication()
		},
		onShow: function() {
			console.log('App Show')
		},
		onHide: function() {
			console.log('App Hide')
		},
		methods: {
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

				if (token && userInfo) {
					this.$store.commit('user/SET_TOKEN', token)
					this.$store.commit('user/SET_USER_INFO', userInfo)
				}
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
</script>

<style lang="scss">
	/* 注意要写在第一行，同时给style标签加入lang="scss"属性 */
	@import './tuniao-ui/index.scss';
	@import './tuniao-ui/iconfont.css';
</style>