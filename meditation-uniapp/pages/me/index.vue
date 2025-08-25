<template>
  <view class="me-page">
    <!-- 顶部背景 -->
    <view class="header-bg">
      <image src="/static/me/background@3x.png" mode="aspectFill" class="bg-image"></image>
    </view>
    
    <!-- 用户信息区域 -->
    <view class="user-section">
      <view class="user-avatar">
        <image 
          :src="userInfo.avatar || '/static/images/default-avatar.png'" 
          mode="aspectFill"
          class="avatar-image"
        />
      </view>
      <text class="user-name">{{ userInfo.nickName || '昵称' }}</text>
    </view>
    
    <!-- 功能卡片区域 -->
    <view class="cards-section">
      <!-- 第一行卡片 -->
      <view class="card-row">
        <view class="card-item" @click="goToFavorites">
          <tn-icon name="like-fill" size="60" color="#79B1DA" class="card-icon"></tn-icon>
          <text class="card-text">我的收藏</text>
        </view>
        
        <view class="card-item" @click="goToRecent">
          <tn-icon name="time-fill" size="60" color="#79B1DA" class="card-icon"></tn-icon>
          <text class="card-text">最近播放</text>
        </view>
      </view>
    </view>
    
    <!-- 菜单列表区域 -->
    <view class="menu-section">
      <!-- 常见问题 -->
      <view class="menu-item" @click="goToFAQ">
        <view class="menu-left">
          <tn-icon name="help-fill" size="48" color="#79B1DA" class="menu-icon"></tn-icon>
          <text class="menu-text">常见问题</text>
        </view>
        <tn-icon name="right" size="32" color="#C0C0C0"></tn-icon>
      </view>
      
      <!-- 退出登录 -->
      <view class="menu-item" @click="goToLogout">
        <view class="menu-left">
          <tn-icon name="logout" size="48" color="#79B1DA" class="menu-icon"></tn-icon>
          <text class="menu-text">退出登录</text>
        </view>
        <tn-icon name="right" size="32" color="#C0C0C0"></tn-icon>
      </view>
    </view>
  </view>
</template>

<script>
import { mapState, mapActions } from 'vuex'

export default {
  data() {
    return {
      
    }
  },
  
  computed: {
    ...mapState('user', ['isLogin', 'token', 'userInfo'])
  },
  
  onShow() {
    this.loadUserInfo()
  },
  
  methods: {
    ...mapActions('user', ['logout', 'getUserInfo']),
    
    async loadUserInfo() {
      if (this.token) {
        try {
          await this.getUserInfo()
        } catch (error) {
          console.error('获取用户信息失败', error)
        }
      }
    },
    
    // 我的收藏
    async goToFavorites() {
      const isLoggedIn = await this.checkLogin()
      if (!isLoggedIn) return
      uni.navigateTo({
        url: '/pages/favorites/index'
      })
    },
    
    // 最近播放
    async goToRecent() {
      const isLoggedIn = await this.checkLogin()
      if (!isLoggedIn) return
      uni.navigateTo({
        url: '/pages/history/index'
      })
    },
    
    // 常见问题
    goToFAQ() {
      uni.navigateTo({
        url: '/pages/faq/index'
      })
    },
    
    // 退出登录
    goToLogout() {
      if (!this.isLogin) {
        uni.showToast({
          title: '您还未登录',
          icon: 'none'
        })
        return
      }
      
      uni.showModal({
        title: '提示',
        content: '确定要退出登录吗？',
        confirmText: '确定',
        cancelText: '取消',
        success: async (res) => {
          if (res.confirm) {
            await this.logout()
            uni.showToast({
              title: '已退出登录',
              icon: 'success'
            })
            
            // 退出登录后停留在当前页面
            // 用户可以继续浏览，下次需要登录时会自动触发无感登录
          }
        }
      })
    },
    
    // 检查登录状态
    async checkLogin() {
      if (!this.isLogin) {
        // #ifdef MP-WEIXIN
        // 微信小程序环境，尝试无感登录
        uni.showLoading({
          title: '正在登录...'
        })
        try {
          await this.$store.dispatch('user/wxLogin')
          uni.hideLoading()
          uni.showToast({
            title: '登录成功',
            icon: 'success'
          })
          return true
        } catch (error) {
          uni.hideLoading()
          uni.showToast({
            title: '登录失败，请重试',
            icon: 'none'
          })
          return false
        }
        // #endif
        
        // #ifndef MP-WEIXIN
        uni.showToast({
          title: '请先登录',
          icon: 'none'
        })
        return false
        // #endif
      }
      return true
    }
  }
}
</script>

<style lang="scss" scoped>
.me-page {
  min-height: 100vh;
  background: linear-gradient(180deg, #E8F0FF 0%, #F5F8FF 100%);
  position: relative;
  padding-bottom: 100rpx;
}

// 顶部背景
.header-bg {
  width: 100%;
  height: 400rpx;
  position: relative;
  overflow: hidden;
  
  .bg-image {
    width: 100%;
    height: 100%;
    display: block;
  }
}

// 用户信息区域
.user-section {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-top: -120rpx;
  position: relative;
  z-index: 10;
  
  .user-avatar {
    width: 160rpx;
    height: 160rpx;
    border-radius: 50%;
    overflow: hidden;
    border: 6rpx solid #FFFFFF;
    box-shadow: 0 8rpx 24rpx rgba(0, 0, 0, 0.1);
    background: #FFFFFF;
    
    .avatar-image {
      width: 100%;
      height: 100%;
      display: block;
    }
  }
  
  .user-name {
    margin-top: 24rpx;
    font-size: 36rpx;
    font-weight: 500;
    color: #1A1A1A;
  }
}

// 功能卡片区域
.cards-section {
  padding: 40rpx 30rpx;
  
  .card-row {
    display: flex;
    justify-content: space-between;
    gap: 24rpx;
    
    .card-item {
      flex: 1;
      height: 160rpx;
      background: #FFFFFF;
      border-radius: 24rpx;
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;
      box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.04);
      transition: all 0.3s ease;
      
      &:active {
        transform: scale(0.98);
        box-shadow: 0 2rpx 8rpx rgba(0, 0, 0, 0.08);
      }
      
      .card-icon {
        margin-bottom: 16rpx;
      }
      
      .card-text {
        font-size: 28rpx;
        color: #333333;
        font-weight: 400;
      }
    }
  }
}

// 菜单列表区域
.menu-section {
  padding: 0 30rpx;
  
  .menu-item {
    height: 120rpx;
    background: #FFFFFF;
    border-radius: 24rpx;
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 0 32rpx;
    margin-bottom: 20rpx;
    box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.04);
    transition: all 0.3s ease;
    
    &:active {
      transform: scale(0.98);
      box-shadow: 0 2rpx 8rpx rgba(0, 0, 0, 0.08);
    }
    
    .menu-left {
      display: flex;
      align-items: center;
      
      .menu-icon {
        width: 48rpx;
        height: 48rpx;
        margin-right: 24rpx;
      }
      
      .menu-text {
        font-size: 30rpx;
        color: #333333;
        font-weight: 400;
      }
    }
    
    .menu-arrow {
      width: 24rpx;
      height: 24rpx;
      opacity: 0.5;
    }
  }
}
</style>