<template>
  <view class="me-page">
    <!-- 顶部背景 -->
    <view class="header-bg">
      <image src="/static/me/background@3x.png" mode="aspectFill" class="bg-image"></image>
    </view>

    <!-- 主要内容区域 -->
    <view class="main-content">
      <!-- 用户信息卡片 - 左右布局 -->
      <view class="user-card">
        <view class="user-info">
          <view class="user-avatar">
            <image
              :src="userInfo.avatar || '/static/images/default-avatar.png'"
              mode="aspectFill"
              class="avatar-image"
            />
          </view>
          <view class="user-details">
            <text class="user-name">{{ userInfo.nickName || '昵称' }}</text>
            <text class="user-desc">享受冥想时光</text>
          </view>
        </view>
        <image src="/static/me/arrow@3x.png" class="arrow-icon"></image>
      </view>

      <!-- 功能菜单列表 -->
      <view class="menu-list">
        <!-- 我的收藏 -->
        <view class="menu-item" @click="goToFavorites">
          <view class="menu-left">
            <image src="/static/me/favorites@3x.png" class="menu-icon"></image>
            <text class="menu-text">我的收藏</text>
          </view>
          <image src="/static/me/arrow@3x.png" class="arrow-icon"></image>
        </view>

        <!-- 最近播放 -->
        <view class="menu-item" @click="goToRecent">
          <view class="menu-left">
            <image src="/static/me/recent@3x.png" class="menu-icon"></image>
            <text class="menu-text">最近播放</text>
          </view>
          <image src="/static/me/arrow@3x.png" class="arrow-icon"></image>
        </view>
      </view>

      <!-- 其他设置 - 合并区域 -->
      <view class="settings-section">
        <!-- 常见问题 -->
        <view class="setting-item" @click="goToFAQ">
          <view class="setting-left">
            <image src="/static/me/faq@3x.png" class="setting-icon"></image>
            <text class="setting-text">常见问题</text>
          </view>
          <image src="/static/me/arrow@3x.png" class="arrow-icon"></image>
        </view>
        
        <!-- 分割线 -->
        <view class="divider"></view>
        
        <!-- 退出登录 -->
        <view class="setting-item" @click="goToLogout">
          <view class="setting-left">
            <image src="/static/me/logout@3x.png" class="setting-icon"></image>
            <text class="setting-text">退出登录</text>
          </view>
          <image src="/static/me/arrow@3x.png" class="arrow-icon"></image>
        </view>
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
  background: #F5F7FA;
  position: relative;
}

// 顶部背景
.header-bg {
  width: 100%;
  height: 320rpx;
  position: relative;
  overflow: hidden;
  background: linear-gradient(135deg, #7C3AED, #A855F7);

  .bg-image {
    width: 100%;
    height: 100%;
    display: block;
    opacity: 0.3;
  }
}

// 主要内容区域
.main-content {
  margin-top: -100rpx;
  padding: 0 30rpx 100rpx;
  position: relative;
  z-index: 10;
}

// 用户信息卡片 - 左右布局
.user-card {
  background: #FFFFFF;
  border-radius: 24rpx;
  padding: 30rpx;
  display: flex;
  align-items: center;
  justify-content: space-between;
  box-shadow: 0 8rpx 24rpx rgba(124, 58, 237, 0.1);
  margin-bottom: 30rpx;

  .user-info {
    display: flex;
    align-items: center;
    flex: 1;

    .user-avatar {
      width: 120rpx;
      height: 120rpx;
      border-radius: 50%;
      overflow: hidden;
      border: 4rpx solid #F0F0F0;
      background: #FFFFFF;
      flex-shrink: 0;

      .avatar-image {
        width: 100%;
        height: 100%;
        display: block;
      }
    }

    .user-details {
      margin-left: 30rpx;
      display: flex;
      flex-direction: column;

      .user-name {
        font-size: 36rpx;
        font-weight: 600;
        color: #1A1A1A;
        margin-bottom: 8rpx;
      }

      .user-desc {
        font-size: 26rpx;
        color: #999999;
      }
    }
  }

  .arrow-icon {
    width: 24rpx;
    height: 24rpx;
    opacity: 0.4;
    flex-shrink: 0;
  }
}

// 功能菜单列表
.menu-list {
  background: #FFFFFF;
  border-radius: 24rpx;
  overflow: hidden;
  box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.04);
  margin-bottom: 30rpx;

  .menu-item {
    height: 110rpx;
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 0 30rpx;
    transition: background-color 0.3s ease;

    &:not(:last-child) {
      border-bottom: 1rpx solid #F5F5F5;
    }

    &:active {
      background-color: #F8F8F8;
    }

    .menu-left {
      display: flex;
      align-items: center;
      flex: 1;

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

    .arrow-icon {
      width: 24rpx;
      height: 24rpx;
      opacity: 0.4;
      flex-shrink: 0;
    }
  }
}

// 设置区域 - 合并的部分
.settings-section {
  background: #FFFFFF;
  border-radius: 24rpx;
  overflow: hidden;
  box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.04);

  .setting-item {
    height: 110rpx;
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 0 30rpx;
    transition: background-color 0.3s ease;

    &:active {
      background-color: #F8F8F8;
    }

    .setting-left {
      display: flex;
      align-items: center;
      flex: 1;

      .setting-icon {
        width: 48rpx;
        height: 48rpx;
        margin-right: 24rpx;
      }

      .setting-text {
        font-size: 30rpx;
        color: #333333;
        font-weight: 400;
      }
    }

    .arrow-icon {
      width: 24rpx;
      height: 24rpx;
      opacity: 0.4;
      flex-shrink: 0;
    }
  }

  // 分割线
  .divider {
    height: 1rpx;
    background: #F5F5F5;
    margin: 0 30rpx;
  }
}
</style>
