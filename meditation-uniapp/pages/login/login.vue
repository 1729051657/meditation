<template>
  <view class="login-page">
    <!-- 背景装饰 -->
    <view class="bg-decoration">
      <view class="circle circle-1"></view>
      <view class="circle circle-2"></view>
      <view class="circle circle-3"></view>
    </view>
    
    <!-- 返回按钮 -->
    <view class="back-btn" @click="goBack">
      <text class="tn-icon-left"></text>
    </view>
    
    <!-- Logo区域 -->
    <view class="logo-section">
      <image src="/static/images/logo.svg" class="logo-image"></image>
      <text class="app-name">冥想空间</text>
      <text class="app-slogan">让心灵回归宁静</text>
    </view>
    
    <!-- 登录表单 -->
    <view class="form-section">
      <view class="form-item">
        <view class="input-wrapper">
          <text class="tn-icon-account input-icon"></text>
          <input 
            type="text" 
            placeholder="请输入手机号/用户名" 
            v-model="formData.username"
            class="input-field"
            placeholder-style="color: #C0C0C0"
          />
        </view>
      </view>
      
      <view class="form-item">
        <view class="input-wrapper">
          <text class="tn-icon-lock input-icon"></text>
          <input 
            type="password" 
            placeholder="请输入密码" 
            v-model="formData.password"
            class="input-field"
            placeholder-style="color: #C0C0C0"
          />
          <text class="tn-icon-eye-close password-toggle" @click="togglePassword"></text>
        </view>
      </view>
      
      <!-- 记住密码和忘记密码 -->
      <view class="form-options">
        <view class="remember-box" @click="toggleRemember">
          <view class="checkbox" :class="{ 'checked': rememberPassword }">
            <text class="tn-icon-success" v-if="rememberPassword"></text>
          </view>
          <text class="remember-text">记住密码</text>
        </view>
        <text class="forget-text" @click="forgetPassword">忘记密码？</text>
      </view>
      
      <!-- 登录按钮 -->
      <view class="login-btn" @click="handleLogin">
        <text class="btn-text">登录</text>
      </view>
      
      <!-- 其他登录方式 -->
      <view class="other-login">
        <view class="divider-box">
          <view class="divider"></view>
          <text class="divider-text">其他登录方式</text>
          <view class="divider"></view>
        </view>
        
        <view class="social-login">
          <view class="social-item" @click="wechatLogin">
            <image src="/static/images/wechat.svg" class="social-icon"></image>
          </view>
          <view class="social-item" @click="qqLogin">
            <image src="/static/images/qq.svg" class="social-icon"></image>
          </view>
          <view class="social-item" @click="appleLogin">
            <image src="/static/images/apple.svg" class="social-icon"></image>
          </view>
        </view>
      </view>
      
      <!-- 注册提示 -->
      <view class="register-tips">
        <text class="tips-text">还没有账号？</text>
        <text class="register-link" @click="goRegister">立即注册</text>
      </view>
    </view>
  </view>
</template>

<script>
import { mapActions } from 'vuex'

export default {
  data() {
    return {
      formData: {
        username: '',
        password: ''
      },
      rememberPassword: false,
      showPassword: false
    }
  },
  
  onLoad() {
    // 读取记住的账号密码
    const savedUsername = uni.getStorageSync('savedUsername')
    const savedPassword = uni.getStorageSync('savedPassword')
    if (savedUsername && savedPassword) {
      this.formData.username = savedUsername
      this.formData.password = savedPassword
      this.rememberPassword = true
    }
  },
  
  methods: {
    ...mapActions('user', ['login']),
    
    goBack() {
      uni.navigateBack()
    },
    
    togglePassword() {
      this.showPassword = !this.showPassword
    },
    
    toggleRemember() {
      this.rememberPassword = !this.rememberPassword
    },
    
    async handleLogin() {
      const { username, password } = this.formData
      
      if (!username) {
        uni.showToast({
          title: '请输入用户名',
          icon: 'none'
        })
        return
      }
      
      if (!password) {
        uni.showToast({
          title: '请输入密码',
          icon: 'none'
        })
        return
      }
      
      uni.showLoading({
        title: '登录中...'
      })
      
      try {
        // 调用store的登录方法
        await this.login({
          username,
          password
        })
        
        // 记住密码
        if (this.rememberPassword) {
          uni.setStorageSync('savedUsername', username)
          uni.setStorageSync('savedPassword', password)
        } else {
          uni.removeStorageSync('savedUsername')
          uni.removeStorageSync('savedPassword')
        }
        
        uni.hideLoading()
        uni.showToast({
          title: '登录成功',
          icon: 'success'
        })
        
        setTimeout(() => {
          uni.navigateBack()
        }, 1500)
        
      } catch (error) {
        uni.hideLoading()
        uni.showToast({
          title: error.message || '登录失败',
          icon: 'none'
        })
      }
    },
    
    forgetPassword() {
      uni.navigateTo({
        url: '/pages/forget/index'
      })
    },
    
    wechatLogin() {
      uni.showToast({
        title: '微信登录',
        icon: 'none'
      })
    },
    
    qqLogin() {
      uni.showToast({
        title: 'QQ登录',
        icon: 'none'
      })
    },
    
    appleLogin() {
      uni.showToast({
        title: 'Apple登录',
        icon: 'none'
      })
    },
    
    goRegister() {
      uni.navigateTo({
        url: '/pages/register/index'
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.login-page {
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  position: relative;
  overflow: hidden;
}

// 背景装饰
.bg-decoration {
  position: absolute;
  width: 100%;
  height: 100%;
  
  .circle {
    position: absolute;
    border-radius: 50%;
    opacity: 0.1;
    background: #FFFFFF;
    
    &.circle-1 {
      width: 400rpx;
      height: 400rpx;
      top: -200rpx;
      right: -100rpx;
    }
    
    &.circle-2 {
      width: 300rpx;
      height: 300rpx;
      bottom: 100rpx;
      left: -150rpx;
    }
    
    &.circle-3 {
      width: 200rpx;
      height: 200rpx;
      top: 50%;
      right: -100rpx;
    }
  }
}

// 返回按钮
.back-btn {
  position: absolute;
  top: 80rpx;
  left: 30rpx;
  width: 60rpx;
  height: 60rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  background: rgba(255, 255, 255, 0.2);
  border-radius: 50%;
  z-index: 10;
  
  text {
    font-size: 32rpx;
    color: #FFFFFF;
  }
}

// Logo区域
.logo-section {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding-top: 180rpx;
  margin-bottom: 60rpx;
  
  .logo-image {
    width: 120rpx;
    height: 120rpx;
    margin-bottom: 30rpx;
  }
  
  .app-name {
    font-size: 42rpx;
    color: #FFFFFF;
    font-weight: bold;
    margin-bottom: 16rpx;
  }
  
  .app-slogan {
    font-size: 28rpx;
    color: rgba(255, 255, 255, 0.8);
  }
}

// 表单区域
.form-section {
  padding: 0 50rpx;
  
  .form-item {
    margin-bottom: 30rpx;
    
    .input-wrapper {
      display: flex;
      align-items: center;
      height: 100rpx;
      background: rgba(255, 255, 255, 0.15);
      border-radius: 50rpx;
      padding: 0 40rpx;
      backdrop-filter: blur(10px);
      border: 1rpx solid rgba(255, 255, 255, 0.2);
      
      .input-icon {
        font-size: 36rpx;
        color: rgba(255, 255, 255, 0.8);
        margin-right: 20rpx;
      }
      
      .input-field {
        flex: 1;
        height: 100%;
        font-size: 30rpx;
        color: #FFFFFF;
      }
      
      .password-toggle {
        font-size: 36rpx;
        color: rgba(255, 255, 255, 0.6);
        padding: 10rpx;
      }
    }
  }
  
  .form-options {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 60rpx;
    padding: 0 10rpx;
    
    .remember-box {
      display: flex;
      align-items: center;
      
      .checkbox {
        width: 32rpx;
        height: 32rpx;
        border: 2rpx solid rgba(255, 255, 255, 0.6);
        border-radius: 6rpx;
        margin-right: 12rpx;
        display: flex;
        align-items: center;
        justify-content: center;
        
        &.checked {
          background: #FFFFFF;
          border-color: #FFFFFF;
          
          text {
            font-size: 24rpx;
            color: #7C3AED;
          }
        }
      }
      
      .remember-text {
        font-size: 28rpx;
        color: rgba(255, 255, 255, 0.8);
      }
    }
    
    .forget-text {
      font-size: 28rpx;
      color: rgba(255, 255, 255, 0.8);
    }
  }
  
  .login-btn {
    height: 100rpx;
    background: #FFFFFF;
    border-radius: 50rpx;
    display: flex;
    align-items: center;
    justify-content: center;
    box-shadow: 0 10rpx 30rpx rgba(0, 0, 0, 0.2);
    
    .btn-text {
      font-size: 34rpx;
      color: #7C3AED;
      font-weight: bold;
    }
    
    &:active {
      transform: scale(0.98);
    }
  }
  
  .other-login {
    margin-top: 80rpx;
    
    .divider-box {
      display: flex;
      align-items: center;
      margin-bottom: 40rpx;
      
      .divider {
        flex: 1;
        height: 1rpx;
        background: rgba(255, 255, 255, 0.3);
      }
      
      .divider-text {
        font-size: 26rpx;
        color: rgba(255, 255, 255, 0.7);
        margin: 0 30rpx;
      }
    }
    
    .social-login {
      display: flex;
      justify-content: center;
      gap: 60rpx;
      
      .social-item {
        width: 80rpx;
        height: 80rpx;
        background: rgba(255, 255, 255, 0.15);
        border-radius: 50%;
        display: flex;
        align-items: center;
        justify-content: center;
        backdrop-filter: blur(10px);
        border: 1rpx solid rgba(255, 255, 255, 0.2);
        
        .social-icon {
          width: 48rpx;
          height: 48rpx;
        }
        
        &:active {
          transform: scale(0.95);
        }
      }
    }
  }
  
  .register-tips {
    display: flex;
    align-items: center;
    justify-content: center;
    margin-top: 60rpx;
    
    .tips-text {
      font-size: 28rpx;
      color: rgba(255, 255, 255, 0.7);
    }
    
    .register-link {
      font-size: 28rpx;
      color: #FFFFFF;
      margin-left: 10rpx;
      font-weight: 500;
    }
  }
}
</style>