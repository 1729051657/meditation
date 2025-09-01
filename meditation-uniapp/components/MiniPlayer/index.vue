<template>
  <view class="mini-player" v-if="isPlaying || currentTrack" @click="goToPlayer">
    <view class="player-content">
      <!-- 封面图 -->
      <image 
        class="cover-image" 
        :src="currentTrack.coverUrl || '/static/images/default-cover.png'" 
        mode="aspectFill"
      />
      
      <!-- 歌曲信息 -->
      <view class="track-info">
        <text class="track-title">{{ currentTrack.title || '未知音频' }}</text>
        <view class="timer-info" v-if="sleepTimerRemaining">
          <image class="timer-icon" src="/static/player/time-icon.png" />
          <text class="timer-text">定时停止 {{ formatTimerRemaining }}</text>
        </view>
      </view>
      
      <!-- 播放控制 -->
      <view class="controls">
        <view class="control-btn" @click.stop="togglePlay">
          <image 
            class="play-icon" 
            :src="isPlaying ? '/static/player/close-pause-icon.png' : '/static/player/play-pause-icon.png'" 
          />
        </view>
        <view class="control-btn" @click.stop="closePlayer">
          <image class="close-icon" src="/static/player/close.png" />
        </view>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  name: 'MiniPlayer',
  
  data() {
    return {
      audioContext: null,
      isPlaying: false,
      currentTrack: null,
      sleepTimerRemaining: 0,
      timerInterval: null
    }
  },
  
  computed: {
    // 格式化定时器剩余时间
    formatTimerRemaining() {
      if (!this.sleepTimerRemaining) return ''
      const minutes = Math.floor(this.sleepTimerRemaining / 60)
      const seconds = this.sleepTimerRemaining % 60
      return `${minutes}:${seconds.toString().padStart(2, '0')}`
    }
  },
  
  mounted() {
    this.initAudioContext()
    this.checkSleepTimer()
  },
  
  beforeDestroy() {
    if (this.timerInterval) {
      clearInterval(this.timerInterval)
    }
  },
  
  methods: {
    // 初始化音频上下文
    initAudioContext() {
      // 获取全局的背景音频管理器
      this.audioContext = uni.getBackgroundAudioManager()
      
      // 检查当前播放状态
      if (this.audioContext && this.audioContext.src) {
        // 有音频正在播放或暂停
        this.currentTrack = {
          title: this.audioContext.title,
          coverUrl: this.audioContext.coverImgUrl,
          audioUrl: this.audioContext.src
        }
        
        // 检查播放状态
        this.isPlaying = !this.audioContext.paused
        
        // 监听播放状态变化
        this.setupAudioListeners()
      }
    },
    
    // 设置音频事件监听
    setupAudioListeners() {
      if (!this.audioContext) return
      
      // 播放事件
      this.audioContext.onPlay(() => {
        this.isPlaying = true
        this.updateTrackInfo()
      })
      
      // 暂停事件
      this.audioContext.onPause(() => {
        this.isPlaying = false
      })
      
      // 停止事件
      this.audioContext.onStop(() => {
        this.isPlaying = false
        this.currentTrack = null
      })
      
      // 播放结束事件
      this.audioContext.onEnded(() => {
        this.isPlaying = false
      })
      
      // 错误事件
      this.audioContext.onError((error) => {
        console.error('迷你播放器音频错误:', error)
        this.isPlaying = false
      })
    },
    
    // 更新音轨信息
    updateTrackInfo() {
      if (this.audioContext && this.audioContext.src) {
        this.currentTrack = {
          title: this.audioContext.title || '未知音频',
          coverUrl: this.audioContext.coverImgUrl || '/static/images/default-cover.png',
          audioUrl: this.audioContext.src
        }
      }
    },
    
    // 检查睡眠定时器
    checkSleepTimer() {
      // 从本地存储获取定时器信息
      const timerEndTime = uni.getStorageSync('sleepTimerEndTime')
      if (timerEndTime) {
        const now = Date.now()
        const remaining = Math.floor((timerEndTime - now) / 1000)
        
        if (remaining > 0) {
          this.sleepTimerRemaining = remaining
          this.startTimerCountdown()
        } else {
          // 定时器已过期，清除存储
          uni.removeStorageSync('sleepTimerEndTime')
        }
      }
    },
    
    // 开始定时器倒计时
    startTimerCountdown() {
      if (this.timerInterval) {
        clearInterval(this.timerInterval)
      }
      
      this.timerInterval = setInterval(() => {
        this.sleepTimerRemaining--
        
        if (this.sleepTimerRemaining <= 0) {
          clearInterval(this.timerInterval)
          this.timerInterval = null
          uni.removeStorageSync('sleepTimerEndTime')
        }
      }, 1000)
    },
    
    // 切换播放/暂停
    togglePlay() {
      if (!this.audioContext) return
      
      if (this.isPlaying) {
        this.audioContext.pause()
      } else {
        this.audioContext.play()
      }
    },
    
    // 关闭播放器
    closePlayer() {
      if (this.audioContext) {
        this.audioContext.stop()
        this.currentTrack = null
        this.isPlaying = false
      }
    },
    
    // 跳转到播放器页面
    goToPlayer() {
      // 获取当前播放的音轨ID（可能需要从存储中获取）
      const currentTrackId = uni.getStorageSync('currentTrackId')
      
      if (currentTrackId) {
        uni.navigateTo({
          url: `/pages/player/index?id=${currentTrackId}&resume=true`
        })
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.mini-player {
  position: fixed;
  bottom: 100rpx;
  left: 20rpx;
  right: 20rpx;
  height: 120rpx;
  background: rgba(255, 255, 255, 0.95);
  border-radius: 20rpx;
  box-shadow: 0 4rpx 20rpx rgba(0, 0, 0, 0.15);
  z-index: 999;
  backdrop-filter: blur(10px);
  animation: slideUp 0.3s ease-out;
}

@keyframes slideUp {
  from {
    transform: translateY(100%);
    opacity: 0;
  }
  to {
    transform: translateY(0);
    opacity: 1;
  }
}

.player-content {
  display: flex;
  align-items: center;
  height: 100%;
  padding: 15rpx;
}

.cover-image {
  width: 90rpx;
  height: 90rpx;
  border-radius: 12rpx;
  flex-shrink: 0;
}

.track-info {
  flex: 1;
  margin: 0 20rpx;
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.track-title {
  font-size: 28rpx;
  color: #333;
  font-weight: 500;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  margin-bottom: 8rpx;
}

.timer-info {
  display: flex;
  align-items: center;
  gap: 8rpx;
}

.timer-icon {
  width: 24rpx;
  height: 24rpx;
}

.timer-text {
  font-size: 22rpx;
  color: #7B9FD4;
}

.controls {
  display: flex;
  align-items: center;
  gap: 20rpx;
}

.control-btn {
  width: 60rpx;
  height: 60rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 50%;
  background: rgba(123, 159, 212, 0.1);
  transition: all 0.2s;
  
  &:active {
    transform: scale(0.9);
    background: rgba(123, 159, 212, 0.2);
  }
}

.play-icon {
  width: 48rpx;
  height: 48rpx;
}

.close-icon {
  width: 28rpx;
  height: 28rpx;
}
</style>