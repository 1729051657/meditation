<template>
  <view class="favorites-page">
    <!-- 自定义导航栏 -->
    <tn-nav-bar
      :isBack="true"
      :bottomShadow="false"
      backgroundColor="#7C3AED"
      :fixed="true"
    >
      <view class="nav-title">
        <text class="nav-title-text">我的收藏</text>
      </view>
    </tn-nav-bar>
    
    <!-- 内容区域 -->
    <view class="content-area">
      <!-- 收藏列表 -->
      <view class="favorites-list" v-if="favoritesList.length > 0">
        <view 
          class="favorite-item" 
          v-for="(item, index) in favoritesList" 
          :key="index"
          @click="playAudio(item)"
        >
          <image :src="item.cover" mode="aspectFill" class="item-cover"></image>
          <view class="item-info">
            <text class="item-title">{{ item.title }}</text>
            <text class="item-author">{{ item.author }}</text>
            <view class="item-meta">
              <text class="meta-duration">{{ item.duration }}</text>
              <text class="meta-plays">{{ item.plays }}次播放</text>
            </view>
          </view>
          <view class="item-action" @click.stop="removeFavorite(item, index)">
            <text class="tn-icon-like-fill"></text>
          </view>
        </view>
      </view>
      
      <!-- 空状态 -->
      <view class="empty-state" v-else>
        <image src="/static/images/empty-favorites.svg" class="empty-image"></image>
        <text class="empty-text">还没有收藏内容</text>
        <text class="empty-tips">快去发现喜欢的冥想音频吧</text>
        <view class="go-home-btn" @click="goHome">
          <text class="btn-text">去发现</text>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      favoritesList: [
        {
          id: 1,
          title: '深度睡眠冥想',
          author: '冥想大师',
          duration: '15:00',
          plays: 1234,
          cover: '/static/home/meditation1.jpg'
        },
        {
          id: 2,
          title: '清晨正念练习',
          author: '静心导师',
          duration: '10:00',
          plays: 892,
          cover: '/static/home/meditation2.jpg'
        },
        {
          id: 3,
          title: '压力释放冥想',
          author: '身心疗愈师',
          duration: '20:00',
          plays: 2156,
          cover: '/static/home/meditation3.jpg'
        },
        {
          id: 4,
          title: '专注力训练',
          author: '专注教练',
          duration: '12:00',
          plays: 567,
          cover: '/static/home/meditation4.jpg'
        }
      ]
    }
  },
  
  onLoad() {
    this.loadFavorites()
  },
  
  methods: {
    loadFavorites() {
      // 从本地存储或API加载收藏列表
      const cached = uni.getStorageSync('favoritesList')
      if (cached) {
        this.favoritesList = cached
      }
    },
    
    playAudio(item) {
      // 跳转到播放页面
      uni.navigateTo({
        url: `/pages/player/index?id=${item.id}&title=${item.title}`
      })
    },
    
    removeFavorite(item, index) {
      uni.showModal({
        title: '提示',
        content: '确定要取消收藏吗？',
        success: (res) => {
          if (res.confirm) {
            this.favoritesList.splice(index, 1)
            // 更新本地存储
            uni.setStorageSync('favoritesList', this.favoritesList)
            uni.showToast({
              title: '已取消收藏',
              icon: 'success'
            })
          }
        }
      })
    },
    
    goHome() {
      uni.switchTab({
        url: '/pages/home/index'
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.favorites-page {
  min-height: 100vh;
  background: #F5F7FA;
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

.content-area {
  padding-top: 120rpx;
  padding-bottom: 40rpx;
}

.favorites-list {
  padding: 20rpx;
  
  .favorite-item {
    display: flex;
    align-items: center;
    background: #FFFFFF;
    border-radius: 20rpx;
    padding: 20rpx;
    margin-bottom: 20rpx;
    box-shadow: 0 2rpx 8rpx rgba(0, 0, 0, 0.04);
    transition: all 0.3s ease;
    
    &:active {
      transform: scale(0.98);
      box-shadow: 0 1rpx 4rpx rgba(0, 0, 0, 0.08);
    }
    
    .item-cover {
      width: 120rpx;
      height: 120rpx;
      border-radius: 16rpx;
      flex-shrink: 0;
    }
    
    .item-info {
      flex: 1;
      margin-left: 24rpx;
      display: flex;
      flex-direction: column;
      
      .item-title {
        font-size: 30rpx;
        color: #333333;
        font-weight: 500;
        margin-bottom: 8rpx;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
      }
      
      .item-author {
        font-size: 26rpx;
        color: #999999;
        margin-bottom: 12rpx;
      }
      
      .item-meta {
        display: flex;
        align-items: center;
        
        .meta-duration {
          font-size: 24rpx;
          color: #7C3AED;
          margin-right: 20rpx;
        }
        
        .meta-plays {
          font-size: 24rpx;
          color: #BBBBBB;
        }
      }
    }
    
    .item-action {
      padding: 10rpx;
      
      text {
        font-size: 40rpx;
        color: #FF6B6B;
      }
    }
  }
}

.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 100rpx 40rpx;
  
  .empty-image {
    width: 300rpx;
    height: 300rpx;
    margin-bottom: 40rpx;
  }
  
  .empty-text {
    font-size: 32rpx;
    color: #333333;
    margin-bottom: 16rpx;
  }
  
  .empty-tips {
    font-size: 28rpx;
    color: #999999;
    margin-bottom: 60rpx;
  }
  
  .go-home-btn {
    padding: 20rpx 80rpx;
    background: linear-gradient(135deg, #7C3AED, #A855F7);
    border-radius: 50rpx;
    box-shadow: 0 8rpx 20rpx rgba(124, 58, 237, 0.3);
    
    .btn-text {
      font-size: 30rpx;
      color: #FFFFFF;
      font-weight: 500;
    }
    
    &:active {
      transform: scale(0.98);
    }
  }
}
</style>