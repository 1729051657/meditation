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
      <view class="playing-bar" v-if="currentPlaying" @click="goToPlayer">
        <view class="playing-info">
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
      <view class="favorites-list" v-if="favoritesList.length > 0">
        <view 
          class="favorite-item" 
          v-for="(item, index) in favoritesList" 
          :key="item.id"
          :class="{ 'item-playing': currentPlaying && currentPlaying.id === item.id }"
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
          <view class="item-action" @click.stop="toggleFavorite(item, index)">
            <text class="tn-icon-like-fill" :style="{ color: item.isFavorite !== false ? '#FF6B6B' : '#999' }"></text>
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
import { listFavoritesDetail, removeFavorite, addFavorite } from '@/api/favorite'
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
      backgroundAudioManager: null, // 背景音频管理器
      currentPlaying: null, // 当前播放的音频
      isPlaying: false, // 是否正在播放
      sleepTimer: null, // 定时关闭定时器
      sleepTime: 0 // 定时关闭剩余时间（秒）
    }
  },
  
  onLoad() {
    this.initBackgroundAudio()
    this.loadFavorites()
  },
  
  onShow() {
    // 页面显示时检查播放状态
    this.checkPlayingStatus()
  },
  
  onUnload() {
    // 页面卸载时清理定时器
    if (this.sleepTimer) {
      clearInterval(this.sleepTimer)
    }
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
    // 初始化背景音频管理器
    initBackgroundAudio() {
      // #ifdef MP-WEIXIN || MP-BAIDU || MP-QQ
      this.backgroundAudioManager = uni.getBackgroundAudioManager()
      
      // 监听播放事件
      this.backgroundAudioManager.onPlay(() => {
        console.log('背景音频开始播放')
        this.isPlaying = true
      })
      
      // 监听暂停事件
      this.backgroundAudioManager.onPause(() => {
        console.log('背景音频暂停')
        this.isPlaying = false
      })
      
      // 监听停止事件
      this.backgroundAudioManager.onStop(() => {
        console.log('背景音频停止')
        this.isPlaying = false
        this.currentPlaying = null
      })
      
      // 监听播放结束
      this.backgroundAudioManager.onEnded(() => {
        console.log('背景音频播放结束')
        this.playNext()
      })
      
      // 监听错误
      this.backgroundAudioManager.onError((res) => {
        console.error('背景音频播放错误:', res)
        uni.showToast({
          title: '播放失败',
          icon: 'none'
        })
        this.isPlaying = false
      })
      // #endif
      
      // #ifndef MP-WEIXIN || MP-BAIDU || MP-QQ
      // 非小程序环境使用普通音频上下文
      this.backgroundAudioManager = uni.createInnerAudioContext()
      // 设置类似的事件监听
      // #endif
    },
    
    // 检查当前播放状态
    checkPlayingStatus() {
      if (this.backgroundAudioManager) {
        // #ifdef MP-WEIXIN || MP-BAIDU || MP-QQ
        this.isPlaying = !this.backgroundAudioManager.paused
        // #endif
      }
    },
    
    async loadFavorites() {
      if (this.loading) return
      
      try {
        this.loading = true
        // 使用详情接口，只查询音频类型的收藏
        const res = await listFavoritesDetail({
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          targetType: 'track' // 只查询音频类型
        })
        
        if (res.code === 200) {
          const { rows, total } = res.data
          
          // 格式化数据
          const formattedList = rows.map(item => ({
            id: item.targetId,
            title: item.targetTitle || '未知音频',
            author: item.targetAuthor || '未知作者',
            duration: this.formatDuration(item.targetDuration || 0),
            plays: item.playCount || 0,
            cover: item.targetCover || '/static/images/default-cover.png',
            audioUrl: item.audioUrl, // 音频URL
            favoriteId: item.id, // 收藏记录ID
            isFavorite: true, // 标记为已收藏
            categoryName: item.categoryName || '默认分类'
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
    
    // 播放音频
    playAudio(item) {
      // 如果点击的是当前正在播放的音频
      if (this.currentPlaying && this.currentPlaying.id === item.id) {
        if (this.isPlaying) {
          this.pauseAudio()
        } else {
          this.resumeAudio()
        }
        return
      }
      
      // 播放新的音频
      this.currentPlaying = item
      
      // #ifdef MP-WEIXIN || MP-BAIDU || MP-QQ
      // 小程序使用背景音频管理器
      this.backgroundAudioManager.title = item.title
      this.backgroundAudioManager.singer = item.author
      this.backgroundAudioManager.coverImgUrl = item.cover
      this.backgroundAudioManager.src = item.audioUrl
      // #endif
      
      // #ifndef MP-WEIXIN || MP-BAIDU || MP-QQ
      // 非小程序环境跳转到播放页面
      uni.navigateTo({
        url: `/pages/player/index?id=${item.id}&title=${item.title}`
      })
      // #endif
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
      
      const currentIndex = this.favoritesList.findIndex(item => 
        this.currentPlaying && item.id === this.currentPlaying.id
      )
      
      let nextIndex = currentIndex + 1
      if (nextIndex >= this.favoritesList.length) {
        nextIndex = 0 // 循环播放
      }
      
      this.playAudio(this.favoritesList[nextIndex])
    },
    
    // 跳转到播放器页面
    goToPlayer() {
      if (this.currentPlaying) {
        uni.navigateTo({
          url: `/pages/player/index?id=${this.currentPlaying.id}&title=${this.currentPlaying.title}`
        })
      }
    },
    
    // 切换收藏状态
    async toggleFavorite(item, index) {
      try {
        if (item.isFavorite) {
          // 取消收藏
          uni.showModal({
            title: '提示',
            content: '确定要取消收藏吗？',
            success: async (res) => {
              if (res.confirm) {
                uni.showLoading({
                  title: '正在取消...'
                })
                
                const result = await removeFavorite(item.favoriteId, 'track')
                
                if (result.code === 200) {
                  // 从列表中移除
                  this.favoritesList.splice(index, 1)
                  this.total--
                  
                  // 如果正在播放这首歌，停止播放
                  if (this.currentPlaying && this.currentPlaying.id === item.id) {
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
        } else {
          // 添加收藏（一般不会出现这种情况，因为列表都是已收藏的）
          uni.showLoading({
            title: '正在收藏...'
          })
          
          const result = await addFavorite(item.id, 'track')
          
          if (result.code === 200) {
            item.isFavorite = true
            item.favoriteId = result.data.id
            
            uni.hideLoading()
            uni.showToast({
              title: '收藏成功',
              icon: 'success'
            })
          } else {
            throw new Error(result.msg || '收藏失败')
          }
        }
      } catch (error) {
        console.error('操作失败:', error)
        uni.hideLoading()
        uni.showToast({
          title: error.message || '操作失败',
          icon: 'none'
        })
      }
    },
    
    // 设置定时关闭
    setSleepTimer(minutes) {
      // 清除之前的定时器
      if (this.sleepTimer) {
        clearInterval(this.sleepTimer)
      }
      
      if (minutes <= 0) {
        this.sleepTime = 0
        uni.showToast({
          title: '已取消定时关闭',
          icon: 'none'
        })
        return
      }
      
      this.sleepTime = minutes * 60 // 转换为秒
      
      uni.showToast({
        title: `将在${minutes}分钟后关闭`,
        icon: 'none'
      })
      
      // 每秒更新一次剩余时间
      this.sleepTimer = setInterval(() => {
        this.sleepTime--
        
        if (this.sleepTime <= 0) {
          // 时间到，停止播放
          clearInterval(this.sleepTimer)
          this.sleepTimer = null
          
          if (this.backgroundAudioManager) {
            this.backgroundAudioManager.stop()
          }
          
          uni.showToast({
            title: '定时关闭',
            icon: 'none'
          })
        }
      }, 1000)
    },
    
    // 显示定时关闭选项
    showSleepTimerOptions() {
      const options = ['15分钟', '30分钟', '60分钟', '90分钟', '取消定时']
      const values = [15, 30, 60, 90, 0]
      
      uni.showActionSheet({
        itemList: options,
        success: (res) => {
          this.setSleepTimer(values[res.tapIndex])
        }
      })
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

.favorites-list {
  padding: 20rpx;
  padding-bottom: 140rpx; // 为播放条留出空间
  
  .favorite-item {
    display: flex;
    align-items: center;
    background: #FFFFFF;
    border-radius: 20rpx;
    padding: 20rpx;
    margin-bottom: 20rpx;
    box-shadow: 0 2rpx 8rpx rgba(0, 0, 0, 0.04);
    transition: all 0.3s ease;
    position: relative;
    
    &.item-playing {
      background: linear-gradient(135deg, rgba(124, 58, 237, 0.05), rgba(168, 85, 247, 0.05));
      border: 2rpx solid #7C3AED;
      
      .item-title {
        color: #7C3AED;
        font-weight: 600;
      }
    }
    
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