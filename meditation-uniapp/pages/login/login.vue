<template>
	<view class="login-page">
		<!-- 自定义导航栏 -->
		<tn-nav-bar
			:isBack="false"
			:bottomShadow="false"
			backgroundColor="transparent"
			:fixed="true"
		>
			<view class="nav-title">
				<text class="nav-title-text">冥想空间</text>
			</view>
		</tn-nav-bar>
		
		<!-- 背景渐变 -->
		<view class="bg-container">
			<!-- 动态背景装饰 -->
			<view class="bg-decoration">
				<view class="wave wave-1"></view>
				<view class="wave wave-2"></view>
				<view class="circle circle-1"></view>
				<view class="circle circle-2"></view>
			</view>
		</view>

		<!-- 主内容区域 -->
		<view class="main-content">
			<!-- Logo区域 -->
			<view class="logo-container">
				<view class="logo-wrapper">
					<image class="logo-image" src="/static/images/meditation-logo.png" mode="aspectFit"></image>
					<view class="logo-shadow"></view>
				</view>
				<view class="app-info">
					<text class="app-title">冥想空间</text>
					<text class="app-subtitle">让心灵回归宁静</text>
				</view>
			</view>

			<!-- 登录卡片 -->
			<view class="login-card">
				<!-- 卡片头部装饰 -->
				<view class="card-header">
					<view class="header-decoration"></view>
				</view>
				
				<!-- 登录方式选择 -->
				<view class="login-tabs">
					<view 
						class="tab-item" 
						:class="{ active: loginType === 'wechat' }"
						@click="loginType = 'wechat'"
					>
						<text>微信登录</text>
					</view>
					<view 
						class="tab-item" 
						:class="{ active: loginType === 'account' }"
						@click="loginType = 'account'"
					>
						<text>账号登录</text>
					</view>
				</view>

				<!-- 微信登录 -->
				<view class="login-section" v-if="loginType === 'wechat'">
					<button 
						class="wechat-login-btn"
						open-type="getPhoneNumber"
						@getphonenumber="getPhoneNumber"
						v-if="canIUseGetUserProfile"
					>
						<view class="btn-content">
							<view class="btn-icon">
								<text class="tn-icon-wechat-fill"></text>
							</view>
							<text class="btn-text">微信快速登录</text>
						</view>
					</button>
					
					<button 
						class="wechat-login-btn"
						@click="wxLogin"
						v-else
					>
						<view class="btn-content">
							<view class="btn-icon">
								<text class="tn-icon-wechat-fill"></text>
							</view>
							<text class="btn-text">微信登录</text>
						</view>
					</button>
				</view>

				<!-- 账号密码登录 -->
				<view class="login-section" v-else>
					<view class="form-item">
						<view class="form-icon">
							<text class="tn-icon-my-fill"></text>
						</view>
						<input 
							class="form-input" 
							type="text" 
							placeholder="请输入用户名/手机号"
							v-model="loginForm.username"
							placeholder-class="placeholder"
						/>
					</view>
					
					<view class="form-item">
						<view class="form-icon">
							<text class="tn-icon-lock-fill"></text>
						</view>
						<input 
							class="form-input" 
							type="password" 
							placeholder="请输入密码"
							v-model="loginForm.password"
							placeholder-class="placeholder"
						/>
					</view>
					
					<button class="login-btn" @click="handleLogin">
						<text class="btn-text">登录</text>
					</button>
					
					<view class="extra-options">
						<text class="option-text" @click="goRegister">注册账号</text>
						<text class="option-text" @click="goForget">忘记密码？</text>
					</view>
				</view>

				<!-- 用户协议 -->
				<view class="agreement-section">
					<view class="agreement-checkbox" @click="toggleAgreement">
						<view class="checkbox" :class="{ checked: isAgree }">
							<text class="tn-icon-success" v-if="isAgree"></text>
						</view>
						<text class="agreement-text">
							我已阅读并同意
							<text class="link" @click.stop="openAgreement('user')">《用户协议》</text>
							和
							<text class="link" @click.stop="openAgreement('privacy')">《隐私政策》</text>
						</text>
					</view>
				</view>
			</view>
		</view>
	</view>
</template>

<script>
import { login, inspectionWechatLogin } from '@/api/auth'
import { setToken, setUserInfo } from '@/utils/auth'
import { appConfig } from '@/common/config'

export default {
	data() {
		return {
			loginType: 'wechat', // wechat or account
			loginForm: {
				username: '',
				password: ''
			},
			isAgree: false,
			canIUseGetUserProfile: false,
			loading: false
		}
	},
	
	onLoad() {
		// 检查是否支持getUserProfile
		if (uni.getUserProfile) {
			this.canIUseGetUserProfile = true
		}
	},
	
	methods: {
		// 切换协议勾选
		toggleAgreement() {
			this.isAgree = !this.isAgree
		},
		
		// 打开协议
		openAgreement(type) {
			uni.navigateTo({
				url: `/subPages/agreement/index?type=${type}`
			})
		},
		
		// 微信登录
		async wxLogin() {
			if (!this.isAgree) {
				uni.showToast({
					title: '请先同意用户协议',
					icon: 'none'
				})
				return
			}
			
			uni.showLoading({ title: '登录中...' })
			
			try {
				// 获取code
				const loginRes = await uni.login({
					provider: 'weixin'
				})
				
				if (loginRes.code) {
					// 调用后端接口
					const res = await inspectionWechatLogin({
						code: loginRes.code,
						appid: appConfig.wechatAppId
					})
					
					if (res.code === 200) {
						// 保存token和用户信息
						setToken(res.data.access_token)
						setUserInfo(res.data.user)
						
						uni.showToast({
							title: '登录成功',
							icon: 'success'
						})
						
						setTimeout(() => {
							uni.switchTab({
								url: '/pages/home/index'
							})
						}, 1500)
					}
				}
			} catch (error) {
				console.error('登录失败', error)
				uni.showToast({
					title: error.message || '登录失败',
					icon: 'none'
				})
			} finally {
				uni.hideLoading()
			}
		},
		
		// 获取手机号登录
		async getPhoneNumber(e) {
			if (!this.isAgree) {
				uni.showToast({
					title: '请先同意用户协议',
					icon: 'none'
				})
				return
			}
			
			if (e.detail.errMsg === 'getPhoneNumber:ok') {
				uni.showLoading({ title: '登录中...' })
				
				try {
					// 获取code
					const loginRes = await uni.login({
						provider: 'weixin'
					})
					
					if (loginRes.code) {
						// 调用后端接口，传递code和加密的手机号信息
						const res = await inspectionWechatLogin({
							code: loginRes.code,
							appid: appConfig.wechatAppId,
							encryptedData: e.detail.encryptedData,
							iv: e.detail.iv
						})
						
						if (res.code === 200) {
							// 保存token和用户信息
							setToken(res.data.access_token)
							setUserInfo(res.data.user)
							
							uni.showToast({
								title: '登录成功',
								icon: 'success'
							})
							
							setTimeout(() => {
								uni.switchTab({
									url: '/pages/home/index'
								})
							}, 1500)
						}
					}
				} catch (error) {
					console.error('登录失败', error)
					uni.showToast({
						title: error.message || '登录失败',
						icon: 'none'
					})
				} finally {
					uni.hideLoading()
				}
			}
		},
		
		// 账号密码登录
		async handleLogin() {
			if (!this.isAgree) {
				uni.showToast({
					title: '请先同意用户协议',
					icon: 'none'
				})
				return
			}
			
			if (!this.loginForm.username) {
				uni.showToast({
					title: '请输入用户名',
					icon: 'none'
				})
				return
			}
			
			if (!this.loginForm.password) {
				uni.showToast({
					title: '请输入密码',
					icon: 'none'
				})
				return
			}
			
			uni.showLoading({ title: '登录中...' })
			
			try {
				const res = await login(this.loginForm)
				
				if (res.code === 200) {
					// 保存token和用户信息
					setToken(res.data.access_token)
					setUserInfo(res.data.user)
					
					uni.showToast({
						title: '登录成功',
						icon: 'success'
					})
					
					setTimeout(() => {
						uni.switchTab({
							url: '/pages/home/index'
						})
					}, 1500)
				}
			} catch (error) {
				console.error('登录失败', error)
				uni.showToast({
					title: error.message || '登录失败',
					icon: 'none'
				})
			} finally {
				uni.hideLoading()
			}
		},
		
		// 注册
		goRegister() {
			uni.navigateTo({
				url: '/subPages/register/index'
			})
		},
		
		// 忘记密码
		goForget() {
			uni.navigateTo({
				url: '/subPages/forget/index'
			})
		}
	}
}
</script>

<style lang="scss" scoped>
.login-page {
	min-height: 100vh;
	position: relative;
	overflow: hidden;
}

.nav-title {
	flex: 1;
	display: flex;
	align-items: center;
	justify-content: center;
	
	.nav-title-text {
		font-size: 36rpx;
		color: #fff;
		font-weight: 600;
	}
}

// 背景容器
.bg-container {
	position: fixed;
	top: 0;
	left: 0;
	right: 0;
	bottom: 0;
	background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
	z-index: -1;
}

.bg-decoration {
	position: relative;
	width: 100%;
	height: 100%;
	overflow: hidden;
	
	.wave {
		position: absolute;
		width: 200%;
		height: 200%;
		border-radius: 45%;
		opacity: 0.1;
		animation: wave 15s linear infinite;
		
		&.wave-1 {
			background: rgba(255, 255, 255, 0.3);
			top: -100%;
			left: -50%;
			animation-duration: 20s;
		}
		
		&.wave-2 {
			background: rgba(255, 255, 255, 0.2);
			bottom: -100%;
			right: -50%;
			animation-duration: 25s;
			animation-direction: reverse;
		}
	}
	
	.circle {
		position: absolute;
		border-radius: 50%;
		background: rgba(255, 255, 255, 0.1);
		
		&.circle-1 {
			width: 200rpx;
			height: 200rpx;
			top: 10%;
			right: 10%;
			animation: float 8s ease-in-out infinite;
		}
		
		&.circle-2 {
			width: 150rpx;
			height: 150rpx;
			bottom: 20%;
			left: 5%;
			animation: float 10s ease-in-out infinite;
			animation-delay: 2s;
		}
	}
}

@keyframes wave {
	0% { transform: rotate(0deg); }
	100% { transform: rotate(360deg); }
}

@keyframes float {
	0%, 100% { transform: translateY(0); }
	50% { transform: translateY(-30rpx); }
}

// 主内容
.main-content {
	position: relative;
	z-index: 1;
	padding-top: 120rpx;
	padding-bottom: 40rpx;
}

// Logo区域
.logo-container {
	display: flex;
	flex-direction: column;
	align-items: center;
	margin-bottom: 60rpx;
	
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
		}
		
		.logo-shadow {
			position: absolute;
			bottom: -10rpx;
			left: 10%;
			right: 10%;
			height: 20rpx;
			background: rgba(0, 0, 0, 0.2);
			border-radius: 50%;
			filter: blur(10rpx);
		}
	}
	
	.app-info {
		text-align: center;
		
		.app-title {
			display: block;
			font-size: 42rpx;
			color: #fff;
			font-weight: 600;
			margin-bottom: 10rpx;
		}
		
		.app-subtitle {
			display: block;
			font-size: 28rpx;
			color: rgba(255, 255, 255, 0.8);
		}
	}
}

// 登录卡片
.login-card {
	margin: 0 40rpx;
	background: rgba(255, 255, 255, 0.95);
	border-radius: 30rpx;
	padding: 40rpx;
	backdrop-filter: blur(20rpx);
	box-shadow: 0 20rpx 60rpx rgba(0, 0, 0, 0.1);
	
	.card-header {
		position: relative;
		height: 8rpx;
		margin: -40rpx -40rpx 40rpx;
		
		.header-decoration {
			position: absolute;
			top: 0;
			left: 0;
			right: 0;
			height: 100%;
			background: linear-gradient(90deg, #667eea 0%, #764ba2 100%);
			border-radius: 30rpx 30rpx 0 0;
		}
	}
}

// 登录方式切换
.login-tabs {
	display: flex;
	margin-bottom: 40rpx;
	background: #f3f4f6;
	border-radius: 20rpx;
	padding: 8rpx;
	
	.tab-item {
		flex: 1;
		padding: 20rpx;
		text-align: center;
		border-radius: 16rpx;
		transition: all 0.3s;
		
		text {
			font-size: 30rpx;
			color: #6b7280;
		}
		
		&.active {
			background: #fff;
			box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.08);
			
			text {
				color: #667eea;
				font-weight: 500;
			}
		}
	}
}

// 登录区域
.login-section {
	margin-bottom: 40rpx;
}

// 微信登录按钮
.wechat-login-btn {
	width: 100%;
	height: 100rpx;
	background: linear-gradient(135deg, #11c37c 0%, #0ea968 100%);
	border-radius: 50rpx;
	border: none;
	margin-bottom: 30rpx;
	transition: all 0.3s;
	
	&:active {
		transform: scale(0.98);
	}
	
	.btn-content {
		display: flex;
		align-items: center;
		justify-content: center;
		
		.btn-icon {
			margin-right: 15rpx;
			
			text {
				font-size: 44rpx;
				color: #fff;
			}
		}
		
		.btn-text {
			font-size: 32rpx;
			color: #fff;
			font-weight: 500;
		}
	}
}

// 表单项
.form-item {
	display: flex;
	align-items: center;
	height: 100rpx;
	background: #f9fafb;
	border-radius: 20rpx;
	padding: 0 30rpx;
	margin-bottom: 30rpx;
	
	.form-icon {
		margin-right: 20rpx;
		
		text {
			font-size: 36rpx;
			color: #9ca3af;
		}
	}
	
	.form-input {
		flex: 1;
		height: 100%;
		font-size: 30rpx;
		color: #1f2937;
	}
	
	.placeholder {
		color: #9ca3af;
	}
}

// 登录按钮
.login-btn {
	width: 100%;
	height: 100rpx;
	background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
	border-radius: 50rpx;
	border: none;
	margin-bottom: 30rpx;
	transition: all 0.3s;
	
	&:active {
		transform: scale(0.98);
	}
	
	.btn-text {
		font-size: 32rpx;
		color: #fff;
		font-weight: 500;
	}
}

// 额外选项
.extra-options {
	display: flex;
	justify-content: space-between;
	
	.option-text {
		font-size: 28rpx;
		color: #667eea;
	}
}

// 协议区域
.agreement-section {
	margin-top: 40rpx;
	padding-top: 40rpx;
	border-top: 1rpx solid #e5e7eb;
	
	.agreement-checkbox {
		display: flex;
		align-items: flex-start;
		
		.checkbox {
			width: 32rpx;
			height: 32rpx;
			border: 2rpx solid #d1d5db;
			border-radius: 8rpx;
			margin-right: 15rpx;
			margin-top: 4rpx;
			display: flex;
			align-items: center;
			justify-content: center;
			transition: all 0.3s;
			
			&.checked {
				background: #667eea;
				border-color: #667eea;
				
				text {
					font-size: 24rpx;
					color: #fff;
				}
			}
		}
		
		.agreement-text {
			flex: 1;
			font-size: 26rpx;
			color: #6b7280;
			line-height: 40rpx;
			
			.link {
				color: #667eea;
			}
		}
	}
}
</style>