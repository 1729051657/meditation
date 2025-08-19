<template>
  <view class="me-page">
    <!-- 自定义导航栏 -->
    <tn-nav-bar
      :isBack="false"
      :bottomShadow="false"
      backgroundColor="#7C3AED"
      :fixed="true"
    >
      <view class="nav-title">
        <text class="nav-title-text">个人中心</text>
      </view>
    </tn-nav-bar>
    
    <!-- 用户信息头部 -->
    <view class="user-header">
      <view class="header-bg"></view>
      <view class="user-info">
        <image 
          class="avatar" 
          :src="userInfo.avatar || '/static/images/default-avatar.png'" 
          mode="aspectFill"
          @click="updateAvatar"
        />
        <view class="info-content">
          <text class="nickname">{{ userInfo.nickName || '冥想者' }}</text>
          <text class="username">{{ userInfo.userName || '点击登录' }}</text>
        </view>
        <view class="edit-btn" @click="editProfile">
          <text class="tn-icon-edit"></text>
        </view>
      </view>
      
      <!-- 统计信息 -->
      <view class="stats-container">
        <view class="stat-item">
          <text class="stat-value">{{ stats.totalMinutes || 0 }}</text>
          <text class="stat-label">总时长(分)</text>
        </view>
        <view class="stat-item">
          <text class="stat-value">{{ stats.totalDays || 0 }}</text>
          <text class="stat-label">坚持天数</text>
        </view>
        <view class="stat-item">
          <text class="stat-value">{{ stats.favorites || 0 }}</text>
          <text class="stat-label">收藏数</text>
        </view>
      </view>
    </view>
    
    <!-- 功能菜单 -->
    <view class="menu-section">
      <view class="menu-group">
        <view class="menu-item" @click="goTo('/pages/favorites/index')">
          <view class="menu-left">
            <view class="menu-icon" style="background: linear-gradient(135deg, #FF6B6B, #FF8E53);">
              <text class="tn-icon-like-fill"></text>
            </view>
            <text class="menu-title">我的收藏</text>
          </view>
          <text class="tn-icon-right menu-arrow"></text>
        </view>
        
        <view class="menu-item" @click="goTo('/pages/history/index')">
          <view class="menu-left">
            <view class="menu-icon" style="background: linear-gradient(135deg, #4ECDC4, #44A08D);">
              <text class="tn-icon-time-fill"></text>
            </view>
            <text class="menu-title">播放历史</text>
          </view>
          <text class="tn-icon-right menu-arrow"></text>
        </view>
        
        <view class="menu-item" @click="goTo('/pages/progress/index')">
          <view class="menu-left">
            <view class="menu-icon" style="background: linear-gradient(135deg, #667eea, #764ba2);">
              <text class="tn-icon-chart-bar"></text>
            </view>
            <text class="menu-title">冥想进度</text>
          </view>
          <text class="tn-icon-right menu-arrow"></text>
        </view>
      </view>
      
      <view class="menu-group">
        <view class="menu-item" @click="goTo('/pages/settings/index')">
          <view class="menu-left">
            <view class="menu-icon" style="background: linear-gradient(135deg, #6B7280, #374151);">
              <text class="tn-icon-set-fill"></text>
            </view>
            <text class="menu-title">设置</text>
          </view>
          <text class="tn-icon-right menu-arrow"></text>
        </view>
        
        <view class="menu-item" @click="goTo('/pages/about/index')">
          <view class="menu-left">
            <view class="menu-icon" style="background: linear-gradient(135deg, #3B82F6, #1E40AF);">
              <text class="tn-icon-info-circle-fill"></text>
            </view>
            <text class="menu-title">关于我们</text>
          </view>
          <text class="tn-icon-right menu-arrow"></text>
        </view>
      </view>
      
      <view class="menu-group" v-if="isLogin">
        <view class="menu-item logout-item" @click="handleLogout">
          <view class="menu-left">
            <view class="menu-icon" style="background: linear-gradient(135deg, #EF4444, #DC2626);">
              <text class="tn-icon-logout"></text>
            </view>
            <text class="menu-title">退出登录</text>
          </view>
          <text class="tn-icon-right menu-arrow"></text>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
import { mapState, mapActions } from 'vuex'
import { getUserProfile, getUserStats } from '@/api/user'

export default {
  data() {
    return {
      stats: {}
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
        // 获取最新用户信息
        try {
          await this.getUserInfo()
        } catch (error) {
          console.error('获取用户信息失败', error)
        }
        
        // 获取统计信息
        this.loadStats()
      } else {
        this.stats = {}
      }
    },
    
    async loadStats() {
      try {
        const res = await getUserStats()
        if (res.code === 200) {
          this.stats = res.data
        }
      } catch (error) {
        console.error('获取统计信息失败', error)
      }
    },
    
    goTo(url) {
      if (!this.isLogin && url !== '/pages/about/index') {
        uni.showToast({
          title: '请先登录',
          icon: 'none'
        })
        setTimeout(() => {
          uni.navigateTo({
            url: '/pages/login/login'
          })
        }, 1500)
        return
      }
      
      uni.navigateTo({ url })
    },
    
    editProfile() {
      if (!this.isLogin) {
        uni.navigateTo({
          url: '/pages/login/login'
        })
        return
      }
      
      uni.navigateTo({
        url: '/pages/profile/edit'
      })
    },
    
    updateAvatar() {
      if (!this.isLogin) {
        uni.navigateTo({
          url: '/pages/login/login'
        })
        return
      }
      
      uni.chooseImage({
        count: 1,
        success: (res) => {
          // 这里实现上传头像的逻辑
          uni.showToast({
            title: '功能开发中',
            icon: 'none'
          })
        }
      })
    },
    
    handleLogout() {
      uni.showModal({
        title: '提示',
        content: '确定要退出登录吗？',
        success: async (res) => {
          if (res.confirm) {
            // 调用store中的登出方法
            await this.logout()
            this.stats = {}
            
            uni.showToast({
              title: '已退出登录',
              icon: 'success'
            })
          }
        }
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.me-page {
  min-height: 100vh;
  background: #f5f5f7;
}

.nav-title {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  
  .nav-title-text {
    font-size: 32rpx;
    color: #fff;
    font-weight: 500;
  }
}

// 用户头部
.user-header {
  position: relative;
  padding-top: 120rpx;
  background: #fff;
  
  .header-bg {
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    height: 200rpx;
    background: linear-gradient(135deg, #7C3AED 0%, #a855f7 100%);
  }
  
  .user-info {
    position: relative;
    display: flex;
    align-items: center;
    padding: 0 30rpx 30rpx;
    
    .avatar {
      width: 120rpx;
      height: 120rpx;
      border-radius: 50%;
      border: 4rpx solid #fff;
      box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.1);
    }
    
    .info-content {
      flex: 1;
      margin-left: 30rpx;
      
      .nickname {
        display: block;
        font-size: 34rpx;
        font-weight: 600;
        color: #1f2937;
        margin-bottom: 8rpx;
      }
      
      .username {
        display: block;
        font-size: 26rpx;
        color: #6b7280;
      }
    }
    
    .edit-btn {
      width: 60rpx;
      height: 60rpx;
      display: flex;
      align-items: center;
      justify-content: center;
      background: #f3f4f6;
      border-radius: 50%;
      
      text {
        font-size: 32rpx;
        color: #6b7280;
      }
    }
  }
  
  .stats-container {
    display: flex;
    padding: 30rpx;
    border-top: 1rpx solid #f3f4f6;
    
    .stat-item {
      flex: 1;
      display: flex;
      flex-direction: column;
      align-items: center;
      
      .stat-value {
        font-size: 36rpx;
        font-weight: 600;
        color: #7C3AED;
        margin-bottom: 8rpx;
      }
      
      .stat-label {
        font-size: 24rpx;
        color: #9ca3af;
      }
    }
  }
}

// 菜单部分
.menu-section {
  padding: 20rpx;
  
  .menu-group {
    background: #fff;
    border-radius: 20rpx;
    overflow: hidden;
    margin-bottom: 20rpx;
    
    .menu-item {
      display: flex;
      align-items: center;
      justify-content: space-between;
      padding: 30rpx;
      border-bottom: 1rpx solid #f3f4f6;
      
      &:last-child {
        border-bottom: none;
      }
      
      &:active {
        background: #f9fafb;
      }
      
      .menu-left {
        display: flex;
        align-items: center;
        
        .menu-icon {
          width: 60rpx;
          height: 60rpx;
          display: flex;
          align-items: center;
          justify-content: center;
          border-radius: 16rpx;
          margin-right: 24rpx;
          
          text {
            font-size: 32rpx;
            color: #fff;
          }
        }
        
        .menu-title {
          font-size: 30rpx;
          color: #1f2937;
        }
      }
      
      .menu-arrow {
        font-size: 28rpx;
        color: #d1d5db;
      }
    }
  }
}
</style>