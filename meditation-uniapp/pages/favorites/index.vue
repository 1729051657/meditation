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
import { listFavorites, removeFavorite } from '@/api/favorite'

export default {
  data() {
    return {
      favoritesList: [],
      loading: false,
      pageNum: 1,
      pageSize: 20,
      total: 0,
      hasMore: true
    }
  },
  
  onLoad() {
    this.loadFavorites()
  },
  
  // 下拉刷新
  onPullDownRefresh() {
    this.pageNum = 1
    this.favoritesList = []
    this.hasMore = true
    this.loadFavorites().finally(() => {
      uni.stopPullDownRefresh()
    })
  },
  
  // 上拉加载更多
  onReachBottom() {
    if (this.hasMore && !this.loading) {
      this.pageNum++
      this.loadFavorites()
    }
  },
  
  methods: {
    async loadFavorites() {
      if (this.loading) return
      
      try {
        this.loading = true
        const res = await listFavorites({
          pageNum: this.pageNum,
          pageSize: this.pageSize
        })
        
        if (res.code === 200) {
          const { rows, total } = res.data
          
          // 格式化数据
          const formattedList = rows.map(item => ({
            id: item.trackId,
            title: item.trackTitle || item.trackName,
            author: item.author || item.artistName || '未知作者',
            duration: this.formatDuration(item.duration || 0),
            plays: item.playCount || 0,
            cover: item.coverUrl || item.trackCover || '/static/images/default-cover.png',
            favoriteId: item.favoriteId // 保存收藏记录ID用于删除
          }))
          
          if (this.pageNum === 1) {
            this.favoritesList = formattedList
          } else {
            this.favoritesList = [...this.favoritesList, ...formattedList]
          }
          
          this.total = total
          this.hasMore = this.favoritesList.length < total
        }
      } catch (error) {
        console.error('加载收藏列表失败:', error)
        uni.showToast({
          title: '加载失败',
          icon: 'none'
        })
      } finally {
        this.loading = false
      }
    },
    
    // 格式化时长
    formatDuration(seconds) {
      if (!seconds) return '00:00'
      const minutes = Math.floor(seconds / 60)
      const secs = seconds % 60
      return `${String(minutes).padStart(2, '0')}:${String(secs).padStart(2, '0')}`
    },
    
    playAudio(item) {
      // 跳转到播放页面
      uni.navigateTo({
        url: `/pages/player/index?id=${item.id}&title=${item.title}`
      })
    },
    
    async removeFavorite(item, index) {
      uni.showModal({
        title: '提示',
        content: '确定要取消收藏吗？',
        success: async (res) => {
          if (res.confirm) {
            try {
              uni.showLoading({
                title: '正在取消...'
              })
              
              // 调用API删除收藏
              const result = await removeFavorite(item.favoriteId || item.id)
              
              if (result.code === 200) {
                // 从列表中移除
                this.favoritesList.splice(index, 1)
                this.total--
                
                uni.hideLoading()
                uni.showToast({
                  title: '已取消收藏',
                  icon: 'success'
                })
              } else {
                throw new Error(result.msg || '取消收藏失败')
              }
            } catch (error) {
              console.error('取消收藏失败:', error)
              uni.hideLoading()
              uni.showToast({
                title: error.message || '取消收藏失败',
                icon: 'none'
              })
            }
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