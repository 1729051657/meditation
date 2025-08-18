<template>
  <view class="player-page">
    <!-- 自定义导航栏 -->
    <tn-nav-bar
      :isBack="true"
      :bottomShadow="false"
      backgroundColor="transparent"
      :fixed="true"
    >
      <view class="nav-title">
        <text class="nav-title-text">{{ track.title || '播放器' }}</text>
      </view>
    </tn-nav-bar>
    
    <!-- 背景 -->
    <view class="bg-container">
      <image :src="track.cover || '/static/images/default-cover.jpg'" mode="aspectFill" class="bg-image"></image>
      <view class="bg-overlay"></view>
    </view>
    
    <!-- 主内容 -->
    <view class="main-content">
      <!-- 封面 -->
      <view class="cover-container">
        <image :src="track.cover || '/static/images/default-cover.jpg'" mode="aspectFill" class="cover-image"></image>
      </view>
      
      <!-- 标题信息 -->
      <view class="info-section">
        <text class="track-title">{{ track.title }}</text>
        <text class="track-artist">{{ track.artist || '冥想音乐' }}</text>
      </view>
      
      <!-- 进度条 -->
      <view class="progress-section">
        <view class="time-info">
          <text class="time-text">{{ formatTime(currentTime) }}</text>
          <text class="time-text">{{ formatTime(duration) }}</text>
        </view>
        <slider
          :value="progress"
          @change="onProgressChange"
          activeColor="#7C3AED"
          backgroundColor="#E5E7EB"
          block-color="#7C3AED"
          block-size="12"
        />
      </view>
      
      <!-- 控制按钮 -->
      <view class="control-section">
        <view class="control-btn" @click="previous">
          <text class="tn-icon-skip-previous-fill"></text>
        </view>
        <view class="control-btn play-btn" @click="togglePlay">
          <text :class="playing ? 'tn-icon-pause-circle-fill' : 'tn-icon-play-circle-fill'"></text>
        </view>
        <view class="control-btn" @click="next">
          <text class="tn-icon-skip-next-fill"></text>
        </view>
      </view>
      
      <!-- 其他功能 -->
      <view class="feature-section">
        <view class="feature-btn" @click="toggleFavorite">
          <text :class="isFavorite ? 'tn-icon-like-fill' : 'tn-icon-like'" :style="{color: isFavorite ? '#ef4444' : '#6B7280'}"></text>
          <text class="feature-text">收藏</text>
        </view>
        <view class="feature-btn" @click="showPlaylist">
          <text class="tn-icon-list"></text>
          <text class="feature-text">列表</text>
        </view>
        <view class="feature-btn" @click="share">
          <text class="tn-icon-share"></text>
          <text class="feature-text">分享</text>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
import { getTrackDetail, recordPlay } from '@/api/track'
import { addFavorite, removeFavorite, checkFavorite } from '@/api/favorite'

export default {
  data() {
    return {
      trackId: '',
      track: {},
      playing: false,
      currentTime: 0,
      duration: 0,
      progress: 0,
      isFavorite: false,
      audioContext: null
    }
  },
  
  onLoad(options) {
    this.trackId = options.id
    this.initAudio()
    this.loadTrack()
  },
  
  onUnload() {
    if (this.audioContext) {
      this.audioContext.destroy()
    }
  },
  
  methods: {
    initAudio() {
      this.audioContext = uni.createInnerAudioContext()
      
      this.audioContext.onPlay(() => {
        this.playing = true
      })
      
      this.audioContext.onPause(() => {
        this.playing = false
      })
      
      this.audioContext.onTimeUpdate(() => {
        this.currentTime = this.audioContext.currentTime
        this.duration = this.audioContext.duration
        if (this.duration > 0) {
          this.progress = (this.currentTime / this.duration) * 100
        }
      })
      
      this.audioContext.onEnded(() => {
        this.playing = false
        this.next()
      })
    },
    
    async loadTrack() {
      try {
        const res = await getTrackDetail(this.trackId)
        this.track = res.data
        
        if (this.track.audioUrl) {
          this.audioContext.src = this.track.audioUrl
        }
        
        // 检查收藏状态
        const favRes = await checkFavorite(this.trackId, 'track')
        this.isFavorite = favRes.data
        
        // 记录播放
        recordPlay(this.trackId)
      } catch (error) {
        uni.showToast({
          title: '加载失败',
          icon: 'none'
        })
      }
    },
    
    togglePlay() {
      if (this.playing) {
        this.audioContext.pause()
      } else {
        this.audioContext.play()
      }
    },
    
    previous() {
      // 实现上一曲逻辑
      uni.showToast({
        title: '暂无上一曲',
        icon: 'none'
      })
    },
    
    next() {
      // 实现下一曲逻辑
      uni.showToast({
        title: '暂无下一曲',
        icon: 'none'
      })
    },
    
    onProgressChange(e) {
      const position = (e.detail.value / 100) * this.duration
      this.audioContext.seek(position)
    },
    
    async toggleFavorite() {
      try {
        if (this.isFavorite) {
          await removeFavorite(this.trackId, 'track')
          this.isFavorite = false
          uni.showToast({
            title: '已取消收藏',
            icon: 'none'
          })
        } else {
          await addFavorite(this.trackId, 'track')
          this.isFavorite = true
          uni.showToast({
            title: '已收藏',
            icon: 'none'
          })
        }
      } catch (error) {
        uni.showToast({
          title: '操作失败',
          icon: 'none'
        })
      }
    },
    
    showPlaylist() {
      // 显示播放列表
      uni.showToast({
        title: '功能开发中',
        icon: 'none'
      })
    },
    
    share() {
      // 分享功能
      uni.showToast({
        title: '功能开发中',
        icon: 'none'
      })
    },
    
    formatTime(seconds) {
      if (!seconds) return '00:00'
      const min = Math.floor(seconds / 60)
      const sec = Math.floor(seconds % 60)
      return `${min.toString().padStart(2, '0')}:${sec.toString().padStart(2, '0')}`
    }
  }
}
</script>

<style lang="scss" scoped>
.player-page {
  min-height: 100vh;
  position: relative;
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

.bg-container {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  z-index: -1;
  
  .bg-image {
    width: 100%;
    height: 100%;
    filter: blur(50rpx);
  }
  
  .bg-overlay {
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background: rgba(0, 0, 0, 0.5);
  }
}

.main-content {
  padding-top: 200rpx;
  padding-bottom: 100rpx;
}

.cover-container {
  width: 500rpx;
  height: 500rpx;
  margin: 0 auto 60rpx;
  border-radius: 20rpx;
  overflow: hidden;
  box-shadow: 0 20rpx 60rpx rgba(0, 0, 0, 0.3);
  
  .cover-image {
    width: 100%;
    height: 100%;
  }
}

.info-section {
  text-align: center;
  margin-bottom: 60rpx;
  padding: 0 40rpx;
  
  .track-title {
    display: block;
    font-size: 36rpx;
    color: #fff;
    font-weight: 600;
    margin-bottom: 20rpx;
  }
  
  .track-artist {
    display: block;
    font-size: 28rpx;
    color: rgba(255, 255, 255, 0.7);
  }
}

.progress-section {
  padding: 0 40rpx;
  margin-bottom: 60rpx;
  
  .time-info {
    display: flex;
    justify-content: space-between;
    margin-bottom: 20rpx;
    
    .time-text {
      font-size: 24rpx;
      color: rgba(255, 255, 255, 0.7);
    }
  }
}

.control-section {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 80rpx;
  margin-bottom: 80rpx;
  
  .control-btn {
    display: flex;
    align-items: center;
    justify-content: center;
    
    text {
      font-size: 60rpx;
      color: #fff;
    }
    
    &.play-btn text {
      font-size: 100rpx;
    }
  }
}

.feature-section {
  display: flex;
  justify-content: space-around;
  padding: 0 80rpx;
  
  .feature-btn {
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 10rpx;
    
    text {
      font-size: 40rpx;
      color: #fff;
    }
    
    .feature-text {
      font-size: 24rpx;
      color: rgba(255, 255, 255, 0.7);
    }
  }
}
</style>