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
      <!-- 正在播放的音频信息条 -->
      <view class="playing-bar" v-if="currentPlaying" >
        <view class="playing-info" >
          <view class="playing-animation">
            <view class="bar" v-for="i in 3" :key="i"></view>
          </view>
          <text class="playing-title">{{ currentPlaying.title }}</text>
        </view>
        <view class="playing-controls">
          <!-- 定时器按钮 -->
          <sleep-timer :audioManager="backgroundAudioManager" @timer-start="onTimerStart" @timer-end="onTimerEnd" />
          <!-- 播放/暂停按钮 -->
          <text class="tn-icon-pause-circle-fill control-icon" v-if="isPlaying" @click.stop="pauseAudio"></text>
          <text class="tn-icon-play-circle-fill control-icon" v-else @click.stop="resumeAudio"></text>
        </view>
      </view>
      
      <!-- 收藏列表 -->
      <view class="favorites-grid" v-if="favoritesList.length > 0">
        <view 
          class="favorite-card" 
          v-for="(item, index) in favoritesList" 
          :key="item.id"
          :class="{ 'card-playing': currentPlaying && currentPlaying.targetId === item.targetId }"
          @click="playAudio(item)"
        >
          <view class="card-image-wrapper">
            <image :src="item.cover" mode="aspectFill" class="card-image"></image>
            <!-- 播放中标识 -->
            <view class="playing-overlay" v-if="currentPlaying && currentPlaying.targetId === item.targetId">
              <view class="playing-icon">
                <view class="wave" v-for="i in 3" :key="i"></view>
              </view>
            </view>
            <!-- 时长标签 -->
            <view class="duration-badge">{{ formatDuration(item.duration) }}</view>
            <!-- 收藏按钮 -->
            <view class="favorite-btn" @click.stop="toggleFavorite(item, index)">
              <text class="tn-icon-like-fill"></text>
            </view>
          </view>
          <view class="card-content">
            <text class="card-title">{{ item.title }}</text>
            <text class="card-subtitle" v-if="item.subtitle">{{ item.subtitle }}</text>
            <text class="card-category" v-if="item.categoryName">{{ item.categoryName }}</text>
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
import { listFavoritesDetail, removeFavorite } from '@/api/favorite'
import SleepTimer from '@/components/SleepTimer/index.vue'

export default {
  components: {
    SleepTimer
  },
  data() {
    return {
      favoritesList: [],
      loading: false,
      pageNum: 1,
      pageSize: 20,
      total: 0,
      hasMore: true,
      backgroundAudioManager: null,
      currentPlaying: null,
      isPlaying: false,
      sleepTimer: null,
      sleepTime: 0
    }
  },
  
  onLoad() {
    this.initBackgroundAudio()
    this.loadFavorites()
  },
  
  onShow() {
    // 页面显示时刷新列表和检查播放状态
    this.refreshFavorites()
    this.checkPlayingStatus()
  },
  
  onUnload() {
    if (this.sleepTimer) {
      clearInterval(this.sleepTimer)
    }
  },

  onPullDownRefresh() {
    this.refreshFavorites().finally(() => {
      uni.stopPullDownRefresh()
    })
  },
  
  onReachBottom() {
    if (this.hasMore && !this.loading) {
      this.pageNum++
      this.loadFavorites()
    }
  },
  
  methods: {
    // 初始化背景音频管理器
    initBackgroundAudio() {
      // 统一使用全局唯一的背景音频管理器
      this.backgroundAudioManager = uni.getBackgroundAudioManager()
      
      this.backgroundAudioManager.onPlay(() => {
        console.log('背景音频开始播放')
        this.isPlaying = true
      })
      
      this.backgroundAudioManager.onPause(() => {
        console.log('背景音频暂停')
        this.isPlaying = false
      })
      
      this.backgroundAudioManager.onStop(() => {
        console.log('背景音频停止')
        this.isPlaying = false
        this.currentPlaying = null
      })
      
      this.backgroundAudioManager.onEnded(() => {
        console.log('背景音频播放结束')
        this.playNext()
      })
      
      this.backgroundAudioManager.onError((res) => {
        console.error('背景音频播放错误:', res)
        uni.showToast({
          title: '播放失败',
          icon: 'none'
        })
        this.isPlaying = false
      })
    },
    
    // 检查当前播放状态
    checkPlayingStatus() {
      if (this.backgroundAudioManager) {
        this.isPlaying = !this.backgroundAudioManager.paused
      }
    },
    
    // 刷新收藏列表
    async refreshFavorites() {
      this.pageNum = 1
      this.favoritesList = []
      this.hasMore = true
      return this.loadFavorites()
    },
    
    // 加载收藏列表
    async loadFavorites() {
      if (this.loading) return
      
      try {
        this.loading = true
        const res = await listFavoritesDetail({
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          targetType: 'track' // 只查询音频类型
        })
        
        if (res.code === 200) {
          const { rows, total } = res
          
          // 格式化数据 - 根据新的数据结构
          const formattedList = rows.map(item => {
            // 根据targetType获取对应的数据
            let targetData = {}
            let cover = '/static/images/default-cover.png'
            let audioUrl = ''
            let duration = 0
            
            if (item.targetType === 'track' && item.track) {
              // 音频类型
              targetData = item.track
              cover = targetData.coverUrl || cover
              audioUrl = targetData.audioUrl || ''
              duration = targetData.durationSec || 0
            } else if (item.targetType === 'series' && item.series) {
              // 系列类型
              targetData = item.series
              cover = targetData.coverUrl || cover
              duration = targetData.recommendDuration || 0
            } else if (item.targetType === 'article' && item.article) {
              // 文章类型
              targetData = item.article
              cover = targetData.coverUrl || cover
            }
            
            return {
              id: item.id, // 收藏记录ID
              targetId: item.targetId,
              targetType: item.targetType,
              title: targetData.title || '未知标题',
              subtitle: targetData.subtitle || '',
              duration: duration,
              cover: cover,
              audioUrl: audioUrl,
              categoryName: item.category?.name || '',
              categoryId: item.category?.id,
              track: item.track,
              series: item.series,
              article: item.article,
              playCount: item.playCount || 0,
              viewCount: item.viewCount || 0,
              createTime: item.createTime
            }
          })
          
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
    
    // 播放音频
    playAudio(item) {
      // 只处理音频类型
      if (item.targetType !== 'track') {
        if (item.targetType === 'series') {
          // 系列类型，跳转到播放器播放系列
          uni.navigateTo({
            url: `/pages/player/index?seriesId=${item.targetId}&type=series`
          })
        } else if (item.targetType === 'article') {
          // 跳转到文章详情页
          uni.navigateTo({
            url: `/pages/article/detail?id=${item.targetId}`
          })
        }
        return
      }
      
      // 获取所有音频类型的收藏
      const audioFavorites = this.favoritesList.filter(fav => fav.targetType === 'track')
      
      // 准备列表数据
      const listData = encodeURIComponent(JSON.stringify(audioFavorites))
      
      // 跳转到播放器页面，传递收藏列表
      uni.navigateTo({
        url: `/pages/player/index?id=${item.targetId}&source=favorites&list=${listData}`
      })
    },
    
    // 暂停音频
    pauseAudio() {
      if (this.backgroundAudioManager) {
        this.backgroundAudioManager.pause()
      }
    },
    
    // 恢复播放
    resumeAudio() {
      if (this.backgroundAudioManager) {
        this.backgroundAudioManager.play()
      }
    },
    
    // 播放下一首
    playNext() {
      if (!this.favoritesList.length) return
      
      // 只播放音频类型
      const audioList = this.favoritesList.filter(item => item.targetType === 'track')
      if (!audioList.length) return
      
      const currentIndex = audioList.findIndex(item => 
        this.currentPlaying && item.targetId === this.currentPlaying.targetId
      )
      
      let nextIndex = currentIndex + 1
      if (nextIndex >= audioList.length) {
        nextIndex = 0 // 循环播放
      }
      
      this.playAudio(audioList[nextIndex])
    },
    
    // 跳转到播放器页面
    goToPlayer() {
      if (this.currentPlaying && this.currentPlaying.targetType === 'track') {
        uni.navigateTo({
          url: `/pages/player/index?id=${this.currentPlaying.targetId}&title=${encodeURIComponent(this.currentPlaying.title)}`
        })
      }
    },
    
    // 切换收藏状态（取消收藏）
    async toggleFavorite(item, index) {
      try {
        uni.showModal({
          title: '提示',
          content: '确定要取消收藏吗？',
          success: async (res) => {
            if (res.confirm) {
              uni.showLoading({
                title: '正在取消...'
              })
              
              const result = await removeFavorite(item.id)
              
              if (result.code === 200) {
                // 从列表中移除
                this.favoritesList.splice(index, 1)
                this.total--
                
                // 如果正在播放这首歌，停止播放
                if (this.currentPlaying && this.currentPlaying.targetId === item.targetId) {
                  this.backgroundAudioManager.stop()
                  this.currentPlaying = null
                }
                
                uni.hideLoading()
                uni.showToast({
                  title: '已取消收藏',
                  icon: 'success'
                })
              } else {
                throw new Error(result.msg || '取消收藏失败')
              }
            }
          }
        })
      } catch (error) {
        console.error('操作失败:', error)
        uni.hideLoading()
        uni.showToast({
          title: error.message || '操作失败',
          icon: 'none'
        })
      }
    },
    
    // 定时器开始事件
    onTimerStart(minutes) {
      console.log(`定时器已启动: ${minutes}分钟`)
    },
    
    // 定时器结束事件
    onTimerEnd() {
      console.log('定时器已结束')
      this.currentPlaying = null
      this.isPlaying = false
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

// 正在播放条
.playing-bar {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  height: 120rpx;
  background: linear-gradient(135deg, #7C3AED, #A855F7);
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 30rpx;
  box-shadow: 0 -4rpx 20rpx rgba(124, 58, 237, 0.2);
  z-index: 999;
  
  .playing-info {
    flex: 1;
    display: flex;
    align-items: center;
    
    .playing-animation {
      display: flex;
      align-items: flex-end;
      height: 40rpx;
      margin-right: 20rpx;
      
      .bar {
        width: 6rpx;
        background: #fff;
        margin: 0 3rpx;
        animation: wave 1.2s ease-in-out infinite;
        
        &:nth-child(1) {
          height: 20rpx;
          animation-delay: 0s;
        }
        
        &:nth-child(2) {
          height: 30rpx;
          animation-delay: 0.2s;
        }
        
        &:nth-child(3) {
          height: 25rpx;
          animation-delay: 0.4s;
        }
      }
    }
    
    .playing-title {
      color: #fff;
      font-size: 28rpx;
      font-weight: 500;
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
      max-width: 400rpx;
    }
  }
  
  .playing-controls {
    display: flex;
    align-items: center;
    gap: 20rpx;
    
    .control-icon {
      font-size: 60rpx;
      color: #fff;
    }
  }
}

@keyframes wave {
  0%, 100% {
    height: 20rpx;
  }
  50% {
    height: 40rpx;
  }
}

.favorites-grid {
  display: flex;
  flex-wrap: wrap;
  gap: 20rpx;
  padding: 20rpx;
  padding-bottom: 140rpx; // 为播放条留出空间
  
  .favorite-card {
    width: calc(50% - 10rpx);
    background: #FFFFFF;
    border-radius: 20rpx;
    overflow: hidden;
    box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.08);
    transition: all 0.3s ease;
    position: relative;
    
    &.card-playing {
      box-shadow: 0 8rpx 24rpx rgba(124, 58, 237, 0.3);
      transform: scale(1.02);
    }
    
    &:active {
      transform: scale(0.98);
      box-shadow: 0 2rpx 8rpx rgba(0, 0, 0, 0.12);
    }
    
    .card-image-wrapper {
      width: 100%;
      height: 340rpx;
      position: relative;
      overflow: hidden;
      
      .card-image {
        width: 100%;
        height: 100%;
        display: block;
      }
      
      // 播放中遮罩
      .playing-overlay {
        position: absolute;
        top: 0;
        left: 0;
        right: 0;
        bottom: 0;
        background: rgba(124, 58, 237, 0.8);
        display: flex;
        align-items: center;
        justify-content: center;
        
        .playing-icon {
          display: flex;
          align-items: flex-end;
          height: 60rpx;
          gap: 8rpx;
          
          .wave {
            width: 8rpx;
            background: #fff;
            border-radius: 4rpx;
            animation: waveAnimation 1.2s ease-in-out infinite;
            
            &:nth-child(1) {
              height: 30rpx;
              animation-delay: 0s;
            }
            
            &:nth-child(2) {
              height: 50rpx;
              animation-delay: 0.2s;
            }
            
            &:nth-child(3) {
              height: 40rpx;
              animation-delay: 0.4s;
            }
          }
        }
      }
      
      // 时长标签
      .duration-badge {
        position: absolute;
        top: 16rpx;
        left: 16rpx;
        background: rgba(0, 0, 0, 0.6);
        color: #fff;
        padding: 8rpx 16rpx;
        border-radius: 20rpx;
        font-size: 24rpx;
        backdrop-filter: blur(10rpx);
      }
      
      // 收藏按钮
      .favorite-btn {
        position: absolute;
        top: 16rpx;
        right: 16rpx;
        width: 60rpx;
        height: 60rpx;
        background: rgba(255, 255, 255, 0.9);
        border-radius: 50%;
        display: flex;
        align-items: center;
        justify-content: center;
        backdrop-filter: blur(10rpx);
        
        text {
          font-size: 32rpx;
          color: #FF6B6B;
        }
        
        &:active {
          transform: scale(0.9);
        }
      }
    }
    
    .card-content {
      padding: 20rpx;
      
      .card-title {
        font-size: 28rpx;
        color: #333333;
        font-weight: 500;
        display: block;
        margin-bottom: 8rpx;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
      }
      
      .card-subtitle {
        font-size: 24rpx;
        color: #999999;
        display: block;
        margin-bottom: 8rpx;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
      }
      
      .card-category {
        font-size: 22rpx;
        color: #7C3AED;
        display: block;
      }
    }
  }
}

@keyframes waveAnimation {
  0%, 100% {
    height: 30rpx;
  }
  50% {
    height: 60rpx;
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