<template>
  <view class="password-page">
    <view class="form-container">
      <view class="form-item">
        <text class="label">当前密码</text>
        <input 
          class="input" 
          type="password" 
          v-model="formData.oldPassword"
          placeholder="请输入当前密码"
        />
      </view>
      
      <view class="form-item">
        <text class="label">新密码</text>
        <input 
          class="input" 
          type="password" 
          v-model="formData.newPassword"
          placeholder="请输入新密码（6-20位）"
          @input="checkPasswordStrength"
        />
        <view class="password-strength" v-if="formData.newPassword">
          <text class="strength-label">密码强度：</text>
          <view class="strength-bar">
            <view 
              class="strength-level" 
              :class="'level-' + passwordStrength"
              :style="{ width: strengthWidth }"
            ></view>
          </view>
          <text class="strength-text" :class="'strength-' + passwordStrength">
            {{ strengthText }}
          </text>
        </view>
      </view>
      
      <view class="form-item">
        <text class="label">确认新密码</text>
        <input 
          class="input" 
          type="password" 
          v-model="formData.confirmPassword"
          placeholder="请再次输入新密码"
        />
      </view>
      
      <view class="tips">
        <text class="tips-title">密码要求：</text>
        <text class="tips-item">• 长度为6-20个字符</text>
        <text class="tips-item">• 包含字母和数字</text>
        <text class="tips-item">• 可包含特殊字符增强安全性</text>
      </view>
    </view>
    
    <button class="submit-btn" @click="submitChange" :disabled="!canSubmit">
      确认修改
    </button>
  </view>
</template>

<script>
import { changePassword } from '@/api/profile'

export default {
  data() {
    return {
      formData: {
        oldPassword: '',
        newPassword: '',
        confirmPassword: ''
      },
      passwordStrength: 0
    }
  },
  
  computed: {
    canSubmit() {
      return this.formData.oldPassword && 
             this.formData.newPassword && 
             this.formData.confirmPassword &&
             this.formData.newPassword.length >= 6 &&
             this.formData.newPassword.length <= 20
    },
    
    strengthWidth() {
      const widthMap = {
        0: '0%',
        1: '33%',
        2: '66%',
        3: '100%'
      }
      return widthMap[this.passwordStrength] || '0%'
    },
    
    strengthText() {
      const textMap = {
        0: '',
        1: '弱',
        2: '中',
        3: '强'
      }
      return textMap[this.passwordStrength] || ''
    }
  },
  
  methods: {
    checkPasswordStrength() {
      const password = this.formData.newPassword
      let strength = 0
      
      if (password.length >= 6) {
        strength++
      }
      
      if (/[a-zA-Z]/.test(password) && /[0-9]/.test(password)) {
        strength++
      }
      
      if (/[!@#$%^&*(),.?":{}|<>]/.test(password)) {
        strength++
      }
      
      this.passwordStrength = strength
    },
    
    validateForm() {
      if (!this.formData.oldPassword) {
        uni.showToast({
          title: '请输入当前密码',
          icon: 'none'
        })
        return false
      }
      
      if (!this.formData.newPassword) {
        uni.showToast({
          title: '请输入新密码',
          icon: 'none'
        })
        return false
      }
      
      if (this.formData.newPassword.length < 6 || this.formData.newPassword.length > 20) {
        uni.showToast({
          title: '新密码长度应为6-20位',
          icon: 'none'
        })
        return false
      }
      
      if (this.formData.newPassword === this.formData.oldPassword) {
        uni.showToast({
          title: '新密码不能与当前密码相同',
          icon: 'none'
        })
        return false
      }
      
      if (this.formData.newPassword !== this.formData.confirmPassword) {
        uni.showToast({
          title: '两次输入的密码不一致',
          icon: 'none'
        })
        return false
      }
      
      return true
    },
    
    async submitChange() {
      if (!this.validateForm()) return
      
      uni.showLoading({ title: '提交中...' })
      
      try {
        const res = await changePassword({
          oldPassword: this.formData.oldPassword,
          newPassword: this.formData.newPassword
        })
        
        if (res.code === 200) {
          uni.showToast({
            title: '密码修改成功',
            icon: 'success'
          })
          
          // 修改成功后清除登录信息并跳转到登录页
          setTimeout(() => {
            uni.removeStorageSync('token')
            uni.removeStorageSync('userInfo')
            uni.reLaunch({
              url: '/pages/login/index'
            })
          }, 1500)
        }
      } catch (error) {
        uni.showToast({
          title: error.message || '修改失败',
          icon: 'none'
        })
      } finally {
        uni.hideLoading()
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.password-page {
  min-height: 100vh;
  background-color: #f5f5f5;
  padding: 20rpx;
  
  .form-container {
    background-color: #fff;
    border-radius: 16rpx;
    padding: 30rpx;
    
    .form-item {
      margin-bottom: 40rpx;
      
      .label {
        display: block;
        font-size: 30rpx;
        color: #333;
        margin-bottom: 15rpx;
      }
      
      .input {
        width: 100%;
        height: 88rpx;
        padding: 0 30rpx;
        border: 2rpx solid #ddd;
        border-radius: 44rpx;
        font-size: 28rpx;
        background-color: #f8f8f8;
        
        &:focus {
          border-color: #007AFF;
          background-color: #fff;
        }
      }
      
      .password-strength {
        margin-top: 15rpx;
        display: flex;
        align-items: center;
        
        .strength-label {
          font-size: 24rpx;
          color: #999;
          margin-right: 10rpx;
        }
        
        .strength-bar {
          flex: 1;
          height: 8rpx;
          background-color: #eee;
          border-radius: 4rpx;
          overflow: hidden;
          margin-right: 10rpx;
          
          .strength-level {
            height: 100%;
            transition: width 0.3s;
            
            &.level-1 {
              background-color: #FF3B30;
            }
            
            &.level-2 {
              background-color: #FF9500;
            }
            
            &.level-3 {
              background-color: #4CD964;
            }
          }
        }
        
        .strength-text {
          font-size: 24rpx;
          
          &.strength-1 {
            color: #FF3B30;
          }
          
          &.strength-2 {
            color: #FF9500;
          }
          
          &.strength-3 {
            color: #4CD964;
          }
        }
      }
    }
    
    .tips {
      padding: 20rpx;
      background-color: #FFF3E0;
      border-radius: 12rpx;
      
      .tips-title {
        display: block;
        font-size: 28rpx;
        color: #FF9800;
        font-weight: bold;
        margin-bottom: 10rpx;
      }
      
      .tips-item {
        display: block;
        font-size: 24rpx;
        color: #F57C00;
        line-height: 1.5;
        margin-bottom: 5rpx;
      }
    }
  }
  
  .submit-btn {
    width: 100%;
    height: 88rpx;
    line-height: 88rpx;
    background-color: #007AFF;
    color: #fff;
    font-size: 32rpx;
    border-radius: 44rpx;
    border: none;
    margin-top: 60rpx;
    
    &[disabled] {
      background-color: #ccc;
    }
  }
}
</style>