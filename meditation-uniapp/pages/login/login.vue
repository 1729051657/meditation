<template>
	<view class="login-page">
		<!-- 自定义导航栏，处理顶部安全区域 -->
		<tn-nav-bar
			:isBack="false"
			:bottomShadow="false"
			backgroundColor="transparent"
			:fixed="true"
		>
			<view class="nav-title">
				<text class="nav-title-text">校园安全巡检</text>
			</view>
		</tn-nav-bar>
		
		<!-- 背景渐变 -->
		<view class="bg-container">
			<!-- 动态背景装饰 -->
			<view class="bg-decoration">
				<view class="wave wave-1"></view>
				<view class="wave wave-2"></view>
				<view class="wave wave-3"></view>
				<view class="circle circle-1"></view>
				<view class="circle circle-2"></view>
				<view class="circle circle-3"></view>
				<view class="circle circle-4"></view>
			</view>
		</view>

		<!-- 主内容区域 -->
		<view class="main-content">
			<!-- Logo区域 -->
			<view class="logo-container">
				<view class="logo-wrapper">
					<image class="logo-image" src="/static/images/logo.png" mode="aspectFit"></image>
					<view class="logo-shadow"></view>
				</view>
				<view class="app-info">
					<text class="app-title">校园安全巡检系统</text>
					<text class="app-subtitle">守护校园每一刻</text>
				</view>
			</view>

			<!-- 登录卡片 -->
			<view class="login-card">
				<!-- 卡片头部装饰 -->
				<view class="card-header">
					<view class="header-decoration"></view>
				</view>
				
				<!-- 学校选择 -->
				<view class="school-section">
					<view class="section-title">
						<text class="title-text">选择学校</text>
						<view class="title-decoration"></view>
					</view>
					<view class="school-selector" @click="selectSchool">
						<view class="selector-content">
							<view class="selector-icon">
								<text class="tn-icon-location"></text>
							</view>
							<view class="selector-text">
								<text class="school-name">{{ currentDeptName || '请选择您的学校' }}</text>
								<text class="school-hint" v-if="!currentDeptName">点击选择学校后进行登录</text>
							</view>
						</view>
						<view class="selector-arrow">
							<text class="tn-icon-right"></text>
						</view>
					</view>
				</view>

				<!-- 分割线 -->
				<view class="divider-section">
					<view class="divider-line"></view>
					<text class="divider-text">快速登录</text>
					<view class="divider-line"></view>
				</view>

				<!-- 微信登录按钮 -->
				<view class="login-section">
                    <tn-button 
                        @click="wxLoginSimple" 
                        :loading="wechatLoginLoading" 
						backgroundColor="tn-bg-green-light" 
						fontColor="tn-color-white"
						width="100%" 
						height="88rpx" 
						fontSize="30" 
						shape="round" 
						:shadow="true"
						:customStyle="{
							background: 'linear-gradient(135deg, #52c41a 0%, #73d13d 100%)',
							boxShadow: '0 8rpx 32rpx rgba(82, 196, 26, 0.3)'
						}"
					>
						<view class="button-content">
							<text class="tn-icon-wechat-fill button-icon"></text>
                            <text class="button-text">微信一键登录</text>
						</view>
					</tn-button>
					
					<!-- 登录提示 -->
					<view class="login-tips">
						<view class="tips-item">
							<text class="tn-icon-safe-fill tips-icon"></text>
							<text class="tips-text">安全加密，保护您的隐私</text>
						</view>
					</view>
				</view>

				<!-- 协议说明 -->
				<view class="agreement-section">
					<text class="agreement-text">登录即表示同意</text>
					<text class="agreement-link">《用户协议》</text>
					<text class="agreement-text">和</text>
					<text class="agreement-link">《隐私政策》</text>
				</view>
			</view>
		</view>

		<!-- 底部版权信息 -->
		<view class="footer-section">
			<view class="footer-content">
				<text class="version-text">Version {{ version }}</text>
				<text class="copyright-text">© 2024 河南校园监管 All Rights Reserved</text>
			</view>
		</view>
	</view>
</template>

<script>
	import {
		inspectionWechatLogin
	} from '@/api/auth'
	import {
		setToken,
		setUserInfo,
		isLoggedIn,
		getTenantId,
		setTenantId,
		getDeptId,
		setDeptId
	} from '@/utils/auth'
	import {
		appConfig
	} from '@/common/config'
	import {
		getUserInfo as fetchUserInfo
	} from '@/api/user'

	export default {
		name: 'Login',
		data() {
			return {
				currentDeptName: '', // 当前选择的学校名称
				wechatLoginLoading: false,
				version: appConfig.version,
				// 微信登录相关数据
				wechatCode: '',
				phoneEncryptedData: null
			}
		},

		onLoad() {
			this.initPage()
		},

		onShow() {
			// 从租户选择页面返回时，重新初始化租户ID
			this.initTenantId()
		},

		methods: {
			// 初始化页面
			initPage() {
				// 检查是否已登录
				if (isLoggedIn()) {
					this.redirectToHome()
					return
				}

				// 初始化租户ID
				this.initTenantId()
			},

			// 初始化租户ID
			initTenantId() {
				const savedTenantId = getTenantId()
				const savedDeptId = getDeptId()
				if (savedTenantId && savedDeptId) {
					// 从本地存储获取学校名称
					const savedDeptName = uni.getStorageSync('edu_dept_name')
					this.currentDeptName = savedDeptName || '已选择学校'
				} else {
					this.currentDeptName = ''
				}
			},

            // 选择学校（冥想版无需选择，提示默认租户）
            selectSchool() {
                uni.showToast({ title: '已使用默认租户', icon: 'none' })
            },

			// 跳转到首页
            redirectToHome() {
                uni.switchTab({ url: '/pages/home/index' })
            },

            // 精简版微信登录：仅使用 code，不要手机号
            async wxLoginSimple() {
                try {
                    this.wechatLoginLoading = true
                    const loginRes = await uni.login({ provider: 'weixin' })
                    if (loginRes[1].errMsg !== 'login:ok') throw new Error(loginRes[1].errMsg)
                    const code = loginRes[1].code
                    const result = await inspectionWechatLogin({ code, appid: appConfig.wechatAppId || '', tenantId: getTenantId(), deptId: getDeptId() })
                    if (result.code !== 200) throw new Error(result.msg || '登录失败')
                    const { access_token, user } = result.data
                    setToken(access_token); setUserInfo(user)
                    uni.showToast({ title:'登录成功', icon:'success' })
                    setTimeout(()=> this.redirectToHome(), 500)
                } catch (e) {
                    uni.showToast({ title: e.message || '登录失败', icon:'none' })
                } finally {
                    this.wechatLoginLoading = false
                }
            }
		}
	}
</script>

<style lang="scss" scoped>
	/* 页面容器 */
	.login-page {
		position: relative;
		width: 100vw;
		height: 100vh;
		overflow: hidden;
		background: linear-gradient(180deg, #6366F1 0%, #8B5CF6 50%, #A855F7 100%);
	}

	/* 导航栏标题 */
	.nav-title {
		width: 100%;
		text-align: center;
		
		.nav-title-text {
			color: #ffffff;
			font-size: 32rpx;
			font-weight: 500;
			opacity: 0.9;
		}
	}

	/* 背景容器 */
	.bg-container {
		position: absolute;
		top: 0;
		left: 0;
		right: 0;
		bottom: 0;
		z-index: 1;
	}

	/* 背景装饰 */
	.bg-decoration {
		position: relative;
		width: 100%;
		height: 100%;
		
		/* 波浪动画 */
		.wave {
			position: absolute;
			width: 200%;
			height: 200%;
			border-radius: 45%;
			background: linear-gradient(135deg, rgba(255, 255, 255, 0.05) 0%, rgba(255, 255, 255, 0.02) 100%);
			animation: wave 20s linear infinite;
			
			&.wave-1 {
				bottom: -150%;
				left: -50%;
				animation-duration: 18s;
			}
			
			&.wave-2 {
				bottom: -160%;
				right: -50%;
				animation-duration: 22s;
				animation-delay: -5s;
			}
			
			&.wave-3 {
				bottom: -170%;
				left: -25%;
				animation-duration: 25s;
				animation-delay: -10s;
			}
		}
		
		/* 圆形装饰 */
		.circle {
			position: absolute;
			border-radius: 50%;
			background: radial-gradient(circle, rgba(255, 255, 255, 0.1) 0%, rgba(255, 255, 255, 0) 70%);
			
			&.circle-1 {
				width: 300rpx;
				height: 300rpx;
				top: 10%;
				right: -100rpx;
				animation: float 8s ease-in-out infinite;
			}
			
			&.circle-2 {
				width: 200rpx;
				height: 200rpx;
				top: 30%;
				left: -50rpx;
				animation: float 10s ease-in-out infinite reverse;
			}
			
			&.circle-3 {
				width: 150rpx;
				height: 150rpx;
				bottom: 30%;
				right: 10%;
				animation: float 12s ease-in-out infinite;
			}
			
			&.circle-4 {
				width: 100rpx;
				height: 100rpx;
				top: 50%;
				left: 50%;
				animation: float 15s ease-in-out infinite reverse;
			}
		}
	}

	/* 波浪动画 */
	@keyframes wave {
		0% {
			transform: rotate(0deg);
		}
		100% {
			transform: rotate(360deg);
		}
	}

	/* 浮动动画 */
	@keyframes float {
		0%, 100% {
			transform: translateY(0) translateX(0);
		}
		25% {
			transform: translateY(-20rpx) translateX(10rpx);
		}
		50% {
			transform: translateY(10rpx) translateX(-10rpx);
		}
		75% {
			transform: translateY(-10rpx) translateX(20rpx);
		}
	}

	/* 主内容区域 */
	.main-content {
		position: relative;
		z-index: 10;
		width: 100%;
		height: 100%;
		display: flex;
		flex-direction: column;
		align-items: center;
		padding-top: calc(var(--status-bar-height) + 88rpx);
		padding-bottom: 40rpx;
		box-sizing: border-box;
	}

	/* Logo容器 */
	.logo-container {
		display: flex;
		flex-direction: column;
		align-items: center;
		margin-bottom: 60rpx;
		animation: fadeInDown 0.8s ease-out;
		
		.logo-wrapper {
			position: relative;
			width: 160rpx;
			height: 160rpx;
			margin-bottom: 30rpx;
			
			.logo-image {
				width: 100%;
				height: 100%;
				border-radius: 30rpx;
				background: rgba(255, 255, 255, 0.9);
				padding: 20rpx;
				box-sizing: border-box;
				box-shadow: 0 10rpx 40rpx rgba(0, 0, 0, 0.1);
			}
			
			.logo-shadow {
				position: absolute;
				bottom: -20rpx;
				left: 50%;
				transform: translateX(-50%);
				width: 120rpx;
				height: 20rpx;
				background: radial-gradient(ellipse, rgba(0, 0, 0, 0.2) 0%, transparent 70%);
				filter: blur(10rpx);
			}
		}
		
		.app-info {
			text-align: center;
			
			.app-title {
				display: block;
				font-size: 38rpx;
				font-weight: bold;
				color: #ffffff;
				margin-bottom: 10rpx;
				text-shadow: 0 2rpx 10rpx rgba(0, 0, 0, 0.2);
			}
			
			.app-subtitle {
				display: block;
				font-size: 26rpx;
				color: rgba(255, 255, 255, 0.8);
				font-weight: 300;
			}
		}
	}

	/* 登录卡片 */
	.login-card {
		position: relative;
		width: calc(100% - 60rpx);
		max-width: 680rpx;
		background: rgba(255, 255, 255, 0.98);
		border-radius: 30rpx;
		padding: 0 40rpx 40rpx;
		box-shadow: 0 20rpx 60rpx rgba(0, 0, 0, 0.15);
		backdrop-filter: blur(20rpx);
		animation: fadeInUp 0.8s ease-out;
		
		/* 卡片头部装饰 */
		.card-header {
			position: relative;
			height: 60rpx;
			margin-bottom: 40rpx;
			
			.header-decoration {
				position: absolute;
				top: 0;
				left: 50%;
				transform: translateX(-50%);
				width: 120rpx;
				height: 6rpx;
				background: linear-gradient(90deg, #6366F1 0%, #8B5CF6 50%, #A855F7 100%);
				border-radius: 3rpx;
			}
		}
		
		/* 学校选择区域 */
		.school-section {
			margin-bottom: 40rpx;
			
			.section-title {
				position: relative;
				margin-bottom: 20rpx;
				
				.title-text {
					font-size: 28rpx;
					color: #333;
					font-weight: 500;
				}
				
				.title-decoration {
					position: absolute;
					bottom: -8rpx;
					left: 0;
					width: 40rpx;
					height: 4rpx;
					background: linear-gradient(90deg, #6366F1 0%, #8B5CF6 100%);
					border-radius: 2rpx;
				}
			}
			
			.school-selector {
				display: flex;
				align-items: center;
				justify-content: space-between;
				padding: 24rpx;
				background: linear-gradient(135deg, #F8F9FF 0%, #F3F4FF 100%);
				border-radius: 16rpx;
				border: 2rpx solid #E8E9FF;
				transition: all 0.3s ease;
				
				&:active {
					transform: scale(0.98);
					background: linear-gradient(135deg, #F3F4FF 0%, #E8E9FF 100%);
				}
				
				.selector-content {
					display: flex;
					align-items: center;
					flex: 1;
					
					.selector-icon {
						width: 56rpx;
						height: 56rpx;
						background: linear-gradient(135deg, #6366F1 0%, #8B5CF6 100%);
						border-radius: 14rpx;
						display: flex;
						align-items: center;
						justify-content: center;
						margin-right: 20rpx;
						
						text {
							color: #ffffff;
							font-size: 28rpx;
						}
					}
					
					.selector-text {
						flex: 1;
						
						.school-name {
							display: block;
							font-size: 30rpx;
							color: #333;
							font-weight: 500;
							margin-bottom: 4rpx;
						}
						
						.school-hint {
							display: block;
							font-size: 24rpx;
							color: #999;
						}
					}
				}
				
				.selector-arrow {
					text {
						font-size: 28rpx;
						color: #C0C4CC;
					}
				}
			}
		}
		
		/* 分割线 */
		.divider-section {
			display: flex;
			align-items: center;
			margin: 40rpx 0;
			
			.divider-line {
				flex: 1;
				height: 1rpx;
				background: linear-gradient(90deg, transparent 0%, #E5E7EB 50%, transparent 100%);
			}
			
			.divider-text {
				padding: 0 24rpx;
				font-size: 24rpx;
				color: #9CA3AF;
			}
		}
		
		/* 登录区域 */
		.login-section {
			.button-content {
				display: flex;
				align-items: center;
				justify-content: center;
				
				.button-icon {
					font-size: 36rpx;
					margin-right: 12rpx;
				}
				
				.button-text {
					font-size: 30rpx;
					font-weight: 500;
				}
			}
			
			.login-tips {
				margin-top: 24rpx;
				
				.tips-item {
					display: flex;
					align-items: center;
					justify-content: center;
					
					.tips-icon {
						font-size: 24rpx;
						color: #52C41A;
						margin-right: 8rpx;
					}
					
					.tips-text {
						font-size: 24rpx;
						color: #999;
					}
				}
			}
		}
		
		/* 协议说明 */
		.agreement-section {
			margin-top: 40rpx;
			text-align: center;
			
			.agreement-text {
				font-size: 24rpx;
				color: #999;
			}
			
			.agreement-link {
				font-size: 24rpx;
				color: #6366F1;
				margin: 0 8rpx;
			}
		}
	}

	/* 底部版权 */
	.footer-section {
		position: absolute;
		bottom: 0;
		left: 0;
		right: 0;
		padding: 30rpx 0;
		padding-bottom: calc(30rpx + constant(safe-area-inset-bottom));
		padding-bottom: calc(30rpx + env(safe-area-inset-bottom));
		z-index: 10;
		
		.footer-content {
			text-align: center;
			
			.version-text,
			.copyright-text {
				display: block;
				font-size: 22rpx;
				color: rgba(255, 255, 255, 0.7);
				line-height: 1.8;
			}
		}
	}

	/* 淡入下降动画 */
	@keyframes fadeInDown {
		from {
			opacity: 0;
			transform: translateY(-30rpx);
		}
		to {
			opacity: 1;
			transform: translateY(0);
		}
	}

	/* 淡入上升动画 */
	@keyframes fadeInUp {
		from {
			opacity: 0;
			transform: translateY(30rpx);
		}
		to {
			opacity: 1;
			transform: translateY(0);
		}
	}

	/* 适配不同屏幕尺寸 */
	@media screen and (max-height: 667px) {
		.logo-container {
			margin-bottom: 40rpx;
			
			.logo-wrapper {
				width: 140rpx;
				height: 140rpx;
			}
		}
		
		.login-card {
			padding: 0 30rpx 30rpx;
		}
	}

	/* 适配更小的屏幕 */
	@media screen and (max-height: 568px) {
		.logo-container {
			margin-bottom: 30rpx;
			
			.logo-wrapper {
				width: 120rpx;
				height: 120rpx;
				margin-bottom: 20rpx;
			}
			
			.app-info {
				.app-title {
					font-size: 34rpx;
				}
				
				.app-subtitle {
					font-size: 24rpx;
				}
			}
		}
		
		.login-card {
			.card-header {
				height: 40rpx;
				margin-bottom: 30rpx;
			}
			
			.school-section {
				margin-bottom: 30rpx;
			}
			
			.divider-section {
				margin: 30rpx 0;
			}
			
			.agreement-section {
				margin-top: 30rpx;
			}
		}
	}
</style>