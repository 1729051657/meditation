<template>
  <view class="about-page">
    <view class="logo-section">
      <image class="logo" src="/static/images/logo.png" mode="aspectFit" />
      <text class="app-name">EduGuard 校园安全管理系统</text>
      <text class="version">版本 {{ version }}</text>
    </view>
    
    <view class="info-section">
      <view class="info-item" @click="handleItemClick('intro')">
        <text class="label">功能介绍</text>
        <text class="arrow">></text>
      </view>
      
      <view class="info-item" @click="handleItemClick('update')">
        <text class="label">检查更新</text>
        <text class="value">{{ updateText }}</text>
        <text class="arrow">></text>
      </view>
      
      <view class="info-item" @click="handleItemClick('agreement')">
        <text class="label">用户协议</text>
        <text class="arrow">></text>
      </view>
      
      <view class="info-item" @click="handleItemClick('privacy')">
        <text class="label">隐私政策</text>
        <text class="arrow">></text>
      </view>
      
      <view class="info-item" @click="handleItemClick('contact')">
        <text class="label">联系我们</text>
        <text class="arrow">></text>
      </view>
    </view>
    
    <view class="footer">
      <text class="copyright">© 2024 EduGuard. All rights reserved.</text>
      <text class="company">技术支持：校园安全科技有限公司</text>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      version: '1.0.0',
      updateText: '已是最新版本'
    }
  },
  
  methods: {
    handleItemClick(type) {
      switch(type) {
        case 'intro':
          uni.navigateTo({
            url: '/pages/webview/index?url=' + encodeURIComponent('https://example.com/intro')
          })
          break
        case 'update':
          this.checkUpdate()
          break
        case 'agreement':
          uni.navigateTo({
            url: '/pages/webview/index?url=' + encodeURIComponent('https://example.com/agreement')
          })
          break
        case 'privacy':
          uni.navigateTo({
            url: '/pages/webview/index?url=' + encodeURIComponent('https://example.com/privacy')
          })
          break
        case 'contact':
          this.showContact()
          break
      }
    },
    
    checkUpdate() {
      uni.showLoading({ title: '检查更新中...' })
      
      setTimeout(() => {
        uni.hideLoading()
        uni.showToast({
          title: '已是最新版本',
          icon: 'none'
        })
      }, 1500)
    },
    
    showContact() {
      uni.showModal({
        title: '联系我们',
        content: '客服电话：400-123-4567\n客服邮箱：support@eduguard.com\n工作时间：周一至周五 9:00-18:00',
        showCancel: false,
        confirmText: '我知道了'
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.about-page {
  min-height: 100vh;
  background-color: #f5f5f5;
  
  .logo-section {
    padding: 80rpx 0;
    text-align: center;
    background-color: #fff;
    
    .logo {
      width: 160rpx;
      height: 160rpx;
      margin-bottom: 30rpx;
    }
    
    .app-name {
      display: block;
      font-size: 36rpx;
      font-weight: bold;
      color: #333;
      margin-bottom: 20rpx;
    }
    
    .version {
      font-size: 28rpx;
      color: #999;
    }
  }
  
  .info-section {
    margin-top: 20rpx;
    background-color: #fff;
    
    .info-item {
      display: flex;
      align-items: center;
      padding: 30rpx;
      border-bottom: 1rpx solid #f0f0f0;
      
      &:last-child {
        border-bottom: none;
      }
      
      .label {
        flex: 1;
        font-size: 30rpx;
        color: #333;
      }
      
      .value {
        font-size: 26rpx;
        color: #999;
        margin-right: 10rpx;
      }
      
      .arrow {
        font-size: 28rpx;
        color: #ccc;
      }
    }
  }
  
  .footer {
    position: absolute;
    bottom: 60rpx;
    left: 0;
    right: 0;
    text-align: center;
    
    .copyright {
      display: block;
      font-size: 24rpx;
      color: #999;
      margin-bottom: 10rpx;
    }
    
    .company {
      font-size: 24rpx;
      color: #999;
    }
  }
}
</style>