<template>
  <view class="mini-player" v-if="showPlayer" @click="goToPlayer">
    <view class="player-content">
      <!-- 封面图 -->
      <image 
        class="cover-image" 
        :src="(currentTrack && currentTrack.coverUrl) || '/static/images/default-cover.png'" 
        mode="aspectFill"
      />
      
      <!-- 歌曲信息 -->
      <view class="track-info">
        <text class="track-title">{{ (currentTrack && currentTrack.title) || '未知音频' }}</text>
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
      timerInterval: null,
      statusCheckInterval: null
    }
  },
  
  computed: {
    // 格式化定时器剩余时间
    formatTimerRemaining() {
      if (!this.sleepTimerRemaining) return ''
      const minutes = Math.floor(this.sleepTimerRemaining / 60)
      const seconds = this.sleepTimerRemaining % 60
      return `${minutes}:${seconds.toString().padStart(2, '0')}`
    },
    
    // 是否显示播放器
    showPlayer() {
      return !!(this.currentTrack && this.currentTrack.audioUrl)
    }
  },
  
  created() {
    // 组件创建时初始化
    console.log('MiniPlayer: 组件创建')
  },
  
  mounted() {
    console.log('MiniPlayer: 组件挂载')
    this.initAudioContext()
    this.checkSleepTimer()
    
    // 定时检查播放状态，以防错过状态变化
    // 每500ms检查一次，确保及时响应
    this.statusCheckInterval = setInterval(() => {
      this.checkAudioStatus()
    }, 500)
  },
  
  beforeDestroy() {
    if (this.timerInterval) {
      clearInterval(this.timerInterval)
    }
    if (this.statusCheckInterval) {
      clearInterval(this.statusCheckInterval)
    }
    // 移除事件监听
    if (this.audioContext) {
      this.removeAudioListeners()
    }
  },
  
  methods: {
    // 初始化音频上下文
    initAudioContext() {
      console.log('MiniPlayer: 初始化音频上下文')
      
      // 获取全局的背景音频管理器
      this.audioContext = uni.getBackgroundAudioManager()
      
      // 检查当前播放状态
      if (this.audioContext && this.audioContext.src) {
        console.log('MiniPlayer: 检测到音频', {
          src: this.audioContext.src,
          title: this.audioContext.title,
          paused: this.audioContext.paused
        })
        
        // 有音频正在播放或暂停
        this.currentTrack = {
          title: this.audioContext.title || '未知音频',
          coverUrl: this.audioContext.coverImgUrl || '/static/images/default-cover.png',
          audioUrl: this.audioContext.src
        }
        
        // 检查播放状态
        this.isPlaying = !this.audioContext.paused
        
        // 监听播放状态变化
        this.setupAudioListeners()
      } else {
        console.log('MiniPlayer: 没有检测到音频')
        this.currentTrack = null
        this.isPlaying = false
      }
    },
    
    // 设置音频事件监听
    setupAudioListeners() {
      if (!this.audioContext) return
      
      // 先移除旧的监听器，避免重复
      this.removeAudioListeners()
      
      // 播放事件
      this.audioContext.onPlay(() => {
        console.log('MiniPlayer: 音频开始播放')
        this.isPlaying = true
        this.updateTrackInfo()
      })
      
      // 暂停事件
      this.audioContext.onPause(() => {
        console.log('MiniPlayer: 音频暂停')
        this.isPlaying = false
      })
      
      // 停止事件
      this.audioContext.onStop(() => {
        console.log('MiniPlayer: 音频停止')
        this.isPlaying = false
        this.currentTrack = null
      })
      
      // 播放结束事件
      this.audioContext.onEnded(() => {
        console.log('MiniPlayer: 音频播放结束')
        this.isPlaying = false
      })
      
      // 错误事件
      this.audioContext.onError((error) => {
        console.error('MiniPlayer: 音频错误:', error)
        this.isPlaying = false
      })
    },
    
    // 移除音频事件监听
    removeAudioListeners() {
      if (!this.audioContext) return
      
      try {
        this.audioContext.offPlay()
        this.audioContext.offPause()
        this.audioContext.offStop()
        this.audioContext.offEnded()
        this.audioContext.offError()
      } catch (error) {
        console.error('MiniPlayer: 移除事件监听失败', error)
      }
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
    
    // 检查音频状态（用于定时检查）
    checkAudioStatus() {
      const audioContext = uni.getBackgroundAudioManager()
      
      if (audioContext && audioContext.src) {
        // 有音频
        if (!this.currentTrack || this.currentTrack.audioUrl !== audioContext.src) {
          // 音频改变了，更新信息
          console.log('MiniPlayer: 检测到新音频或音频变化', {
            src: audioContext.src,
            title: audioContext.title,
            coverImgUrl: audioContext.coverImgUrl,
            paused: audioContext.paused
          })
          
          this.currentTrack = {
            title: audioContext.title || '未知音频',
            coverUrl: audioContext.coverImgUrl || '/static/images/default-cover.png',
            audioUrl: audioContext.src
          }
        }
        
        // 更新播放状态
        const newPlayingState = !audioContext.paused
        if (this.isPlaying !== newPlayingState) {
          console.log('MiniPlayer: 播放状态变化', newPlayingState ? '播放' : '暂停')
          this.isPlaying = newPlayingState
        }
      } else {
        // 没有音频
        if (this.currentTrack) {
          console.log('MiniPlayer: 音频已停止或清除')
          this.currentTrack = null
          this.isPlaying = false
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