<template>
  <view class="verify-page">
    <view class="status-card" v-if="verifyStatus !== 'none'">
      <view class="status-icon" :class="'status-' + verifyStatus">
        <text class="iconfont" :class="statusIcon"></text>
      </view>
      <text class="status-text">{{ statusText }}</text>
      <text class="status-desc" v-if="verifyStatus === 'reviewing'">
        您的认证信息正在审核中，请耐心等待
      </text>
      <text class="status-desc" v-if="verifyStatus === 'verified'">
        您已完成实名认证
      </text>
      <text class="status-desc" v-if="verifyStatus === 'failed'">
        {{ failReason }}
      </text>
    </view>
    
    <view class="form-container" v-if="verifyStatus === 'none' || verifyStatus === 'failed'">
      <view class="form-section">
        <text class="section-title">基本信息</text>
        
        <view class="form-item">
          <text class="label">真实姓名</text>
          <input 
            class="input" 
            v-model="formData.realName"
            placeholder="请输入您的真实姓名"
          />
        </view>
        
        <view class="form-item">
          <text class="label">身份证号</text>
          <input 
            class="input" 
            v-model="formData.idCard"
            placeholder="请输入18位身份证号码"
            maxlength="18"
          />
        </view>
        
        <view class="form-item">
          <text class="label">手机号码</text>
          <input 
            class="input" 
            type="number"
            v-model="formData.phone"
            placeholder="请输入手机号码"
            maxlength="11"
          />
        </view>
      </view>
      
      <view class="form-section">
        <text class="section-title">证件照片</text>
        
        <view class="upload-grid">
          <view class="upload-item">
            <view class="upload-box" @click="chooseImage('front')">
              <image v-if="formData.idCardFront" :src="formData.idCardFront" mode="aspectFill" />
              <view v-else class="upload-placeholder">
                <text class="iconfont icon-camera"></text>
                <text class="upload-text">身份证正面</text>
              </view>
            </view>
          </view>
          
          <view class="upload-item">
            <view class="upload-box" @click="chooseImage('back')">
              <image v-if="formData.idCardBack" :src="formData.idCardBack" mode="aspectFill" />
              <view v-else class="upload-placeholder">
                <text class="iconfont icon-camera"></text>
                <text class="upload-text">身份证反面</text>
              </view>
            </view>
          </view>
        </view>
        
        <view class="upload-tips">
          <text class="tips-item">• 请上传清晰的身份证正反面照片</text>
          <text class="tips-item">• 确保证件信息完整可见</text>
          <text class="tips-item">• 支持jpg、png格式，大小不超过5MB</text>
        </view>
      </view>
      
      <view class="agreement">
        <checkbox-group @change="onAgreeChange">
          <label class="checkbox-label">
            <checkbox :checked="agreed" />
            <text>我已阅读并同意</text>
            <text class="link" @click.stop="showAgreement">《实名认证协议》</text>
          </label>
        </checkbox-group>
      </view>
      
      <button class="submit-btn" @click="submitVerify" :disabled="!canSubmit">
        提交认证
      </button>
    </view>
  </view>
</template>

<script>
import { getVerifyStatus, submitVerification, uploadVerifyImage } from '@/api/profile'

export default {
  data() {
    return {
      verifyStatus: 'none', // none, reviewing, verified, failed
      failReason: '',
      formData: {
        realName: '',
        idCard: '',
        phone: '',
        idCardFront: '',
        idCardBack: ''
      },
      agreed: false
    }
  },
  
  computed: {
    statusIcon() {
      const iconMap = {
        'reviewing': 'icon-time',
        'verified': 'icon-check',
        'failed': 'icon-close'
      }
      return iconMap[this.verifyStatus] || ''
    },
    
    statusText() {
      const textMap = {
        'reviewing': '审核中',
        'verified': '已认证',
        'failed': '认证失败'
      }
      return textMap[this.verifyStatus] || ''
    },
    
    canSubmit() {
      return this.formData.realName &&
             this.formData.idCard &&
             this.formData.phone &&
             this.formData.idCardFront &&
             this.formData.idCardBack &&
             this.agreed
    }
  },
  
  onLoad() {
    this.getVerifyStatus()
  },
  
  methods: {
    async getVerifyStatus() {
      try {
        const res = await getVerifyStatus()
        if (res.code === 200) {
          this.verifyStatus = res.data.status || 'none'
          this.failReason = res.data.failReason || ''
        }
      } catch (error) {
        console.error('获取认证状态失败', error)
      }
    },
    
    chooseImage(type) {
      uni.chooseImage({
        count: 1,
        sizeType: ['compressed'],
        sourceType: ['album', 'camera'],
        success: (res) => {
          const tempFilePath = res.tempFilePaths[0]
          
          // 检查文件大小
          uni.getFileInfo({
            filePath: tempFilePath,
            success: async (fileInfo) => {
              if (fileInfo.size > 5 * 1024 * 1024) {
                uni.showToast({
                  title: '图片大小不能超过5MB',
                  icon: 'none'
                })
                return
              }
              
              // 上传图片到服务器
              try {
                const uploadRes = await uploadVerifyImage(tempFilePath)
                if (uploadRes.code === 200) {
                  if (type === 'front') {
                    this.formData.idCardFront = uploadRes.data.url
                  } else {
                    this.formData.idCardBack = uploadRes.data.url
                  }
                } else {
                  uni.showToast({
                    title: '图片上传失败',
                    icon: 'none'
                  })
                }
              } catch (uploadError) {
                uni.showToast({
                  title: '图片上传失败',
                  icon: 'none'
                })
              }
            }
          })
        }
      })
    },
    
    onAgreeChange(e) {
      this.agreed = e.detail.value.length > 0
    },
    
    showAgreement() {
      uni.navigateTo({
        url: '/pages/webview/index?url=' + encodeURIComponent('https://example.com/verify-agreement')
      })
    },
    
    validateForm() {
      if (!this.formData.realName) {
        uni.showToast({
          title: '请输入真实姓名',
          icon: 'none'
        })
        return false
      }
      
      if (!/^[\u4e00-\u9fa5]{2,10}$/.test(this.formData.realName)) {
        uni.showToast({
          title: '请输入正确的中文姓名',
          icon: 'none'
        })
        return false
      }
      
      if (!this.formData.idCard) {
        uni.showToast({
          title: '请输入身份证号',
          icon: 'none'
        })
        return false
      }
      
      if (!/^\d{17}[\dXx]$/.test(this.formData.idCard)) {
        uni.showToast({
          title: '请输入正确的身份证号',
          icon: 'none'
        })
        return false
      }
      
      if (!this.formData.phone) {
        uni.showToast({
          title: '请输入手机号码',
          icon: 'none'
        })
        return false
      }
      
      if (!/^1[3-9]\d{9}$/.test(this.formData.phone)) {
        uni.showToast({
          title: '请输入正确的手机号码',
          icon: 'none'
        })
        return false
      }
      
      return true
    },
    
    async submitVerify() {
      if (!this.validateForm()) return
      
      uni.showModal({
        title: '提示',
        content: '确定提交实名认证信息吗？提交后将进入审核流程。',
        success: async (res) => {
          if (res.confirm) {
            uni.showLoading({ title: '提交中...' })
            
            try {
              const res = await submitVerification({
                realName: this.formData.realName,
                idCard: this.formData.idCard,
                phone: this.formData.phone,
                idCardFront: this.formData.idCardFront,
                idCardBack: this.formData.idCardBack
              })
              
              if (res.code === 200) {
                this.verifyStatus = 'reviewing'
                uni.showToast({
                  title: '提交成功',
                  icon: 'success'
                })
                
                setTimeout(() => {
                  uni.navigateBack()
                }, 1500)
              }
            } catch (error) {
              uni.showToast({
                title: '提交失败',
                icon: 'none'
              })
            } finally {
              uni.hideLoading()
            }
          }
        }
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.verify-page {
  min-height: 100vh;
  background-color: #f5f5f5;
  padding: 20rpx;
  
  .status-card {
    background-color: #fff;
    border-radius: 16rpx;
    padding: 60rpx;
    text-align: center;
    margin-bottom: 20rpx;
    
    .status-icon {
      width: 120rpx;
      height: 120rpx;
      margin: 0 auto 30rpx;
      border-radius: 50%;
      display: flex;
      align-items: center;
      justify-content: center;
      
      &.status-reviewing {
        background-color: #FFF3E0;
        
        .iconfont {
          color: #FF9800;
          font-size: 60rpx;
        }
      }
      
      &.status-verified {
        background-color: #E8F5E9;
        
        .iconfont {
          color: #4CAF50;
          font-size: 60rpx;
        }
      }
      
      &.status-failed {
        background-color: #FFEBEE;
        
        .iconfont {
          color: #F44336;
          font-size: 60rpx;
        }
      }
    }
    
    .status-text {
      display: block;
      font-size: 36rpx;
      font-weight: bold;
      color: #333;
      margin-bottom: 20rpx;
    }
    
    .status-desc {
      font-size: 28rpx;
      color: #666;
      line-height: 1.5;
    }
  }
  
  .form-container {
    .form-section {
      background-color: #fff;
      border-radius: 16rpx;
      padding: 30rpx;
      margin-bottom: 20rpx;
      
      .section-title {
        display: block;
        font-size: 32rpx;
        font-weight: bold;
        color: #333;
        margin-bottom: 30rpx;
      }
      
      .form-item {
        margin-bottom: 30rpx;
        
        .label {
          display: block;
          font-size: 28rpx;
          color: #666;
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
        }
      }
      
      .upload-grid {
        display: flex;
        gap: 30rpx;
        
        .upload-item {
          flex: 1;
          
          .upload-box {
            width: 100%;
            height: 200rpx;
            border: 2rpx dashed #ddd;
            border-radius: 12rpx;
            overflow: hidden;
            position: relative;
            
            image {
              width: 100%;
              height: 100%;
            }
            
            .upload-placeholder {
              height: 100%;
              display: flex;
              flex-direction: column;
              align-items: center;
              justify-content: center;
              
              .iconfont {
                font-size: 48rpx;
                color: #999;
                margin-bottom: 10rpx;
              }
              
              .upload-text {
                font-size: 24rpx;
                color: #999;
              }
            }
          }
        }
      }
      
      .upload-tips {
        margin-top: 20rpx;
        padding: 20rpx;
        background-color: #f8f8f8;
        border-radius: 8rpx;
        
        .tips-item {
          display: block;
          font-size: 24rpx;
          color: #999;
          line-height: 1.5;
          margin-bottom: 5rpx;
        }
      }
    }
    
    .agreement {
      margin: 30rpx 0;
      
      .checkbox-label {
        display: flex;
        align-items: center;
        font-size: 26rpx;
        color: #666;
        
        .link {
          color: #007AFF;
          margin-left: 5rpx;
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
      
      &[disabled] {
        background-color: #ccc;
      }
    }
  }
}
</style>