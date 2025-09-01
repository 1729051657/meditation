<template>
  <view class="mini-player" v-if="showPlayer">
    <!-- 圆形封面 -->
    <image 
      class="cover-circle" 
      :src="(currentTrack && currentTrack.coverUrl) || '/static/images/default-cover.png'" 
      mode="aspectFill"
      @click="goToPlayer"
    />
    
    <!-- 长方形播放条 -->
    <view class="player-bar" @click="goToPlayer">
      <!-- 音乐信息 -->
      <view class="track-info">
        <text class="track-title">{{ (currentTrack && currentTrack.title) || '未知音频' }}</text>
        <!-- 定时器信息 -->
        <view class="timer-info" v-if="sleepTimerRemaining">
          <image class="timer-icon" src="/static/player/time-icon.png" />
          <text class="timer-text">{{ formatTimerRemaining }}</text>
        </view>
      </view>
      
      <!-- 播放/暂停按钮 -->
      <view class="play-btn" @click.stop="togglePlay">
        <image 
          class="play-icon" 
          :src="isPlaying ? '/static/player/close-pause-icon.png' : '/static/player/play-pause-icon.png'" 
        />
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
  left: 24rpx;
  right: 24rpx;
  display: flex;
  align-items: center;
  z-index: 999;
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

// 圆形封面
.cover-circle {
  width: 112rpx;
  height: 112rpx;
  border-radius: 50%;
  flex-shrink: 0;
  box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.15);
  background: #fff;
  z-index: 2;
  position: relative;
}

// 长方形播放条
.player-bar {
  flex: 1;
  height: 96rpx;
  background: #6D8BA1;
  border-radius: 52rpx;
  opacity: 0.9;
  backdrop-filter: blur(3px);
  display: flex;
  align-items: center;
  padding: 0 30rpx 0 70rpx; // 左边留出空间给圆形封面
  margin-left: -56rpx; // 让长方形与圆形拼接
  position: relative;
}

// 音乐信息
.track-info {
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: center;
  gap: 8rpx;
}

.track-title {
  font-size: 30rpx;
  color: #FFFFFF;
  font-weight: 500;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  max-width: 400rpx;
}

// 定时器信息
.timer-info {
  display: flex;
  align-items: center;
  gap: 8rpx;
}

.timer-icon {
  width: 24rpx;
  height: 24rpx;
  opacity: 0.8;
}

.timer-text {
  font-size: 24rpx;
  color: rgba(255, 255, 255, 0.8);
}

// 播放按钮
.play-btn {
  width: 60rpx;
  height: 60rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.2);
  transition: all 0.2s;
  
  &:active {
    transform: scale(0.9);
    background: rgba(255, 255, 255, 0.3);
  }
}

.play-icon {
  width: 36rpx;
  height: 36rpx;
}
</style>