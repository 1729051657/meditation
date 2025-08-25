<template>
  <view>
    <!-- 定时关闭按钮 -->
    <view class="sleep-timer-btn" @click="showSleepTimerModal">
      <text class="tn-icon-time" :style="{ color: remainingTime > 0 ? '#7C3AED' : '#666' }"></text>
      <text class="timer-text" v-if="remainingTime > 0">{{ formatRemainingTime }}</text>
      <text class="timer-text" v-else>定时</text>
    </view>
    
    <!-- 定时器模态框 -->
    <view class="timer-modal" v-if="showModal">
      <view class="modal-mask" @click="closeModal"></view>
      <view class="modal-content">
        <view class="modal-header">
          <text class="modal-title">定时关闭</text>
          <text class="tn-icon-close modal-close" @click="closeModal"></text>
        </view>
        
        <view class="timer-options">
          <view 
            class="timer-option" 
            v-for="option in timerOptions" 
            :key="option.value"
            :class="{ active: selectedTime === option.value }"
            @click="selectTime(option.value)"
          >
            <text class="option-text">{{ option.label }}</text>
            <text class="tn-icon-success-fill check-icon" v-if="selectedTime === option.value"></text>
          </view>
          
          <!-- 自定义时间 -->
          <view class="timer-option custom-option" @click="showCustomInput">
            <text class="option-text">自定义时间</text>
            <text class="tn-icon-right"></text>
          </view>
          
          <!-- 取消定时 -->
          <view class="timer-option cancel-option" @click="cancelTimer" v-if="remainingTime > 0">
            <text class="option-text">取消定时</text>
          </view>
        </view>
        
        <view class="modal-footer">
          <button class="confirm-btn" @click="confirmTimer">确定</button>
        </view>
      </view>
    </view>
    
    <!-- 自定义时间输入框 -->
    <view class="custom-modal" v-if="showCustomModal">
      <view class="modal-mask" @click="closeCustomModal"></view>
      <view class="custom-content">
        <text class="custom-title">设置定时（分钟）</text>
        <input 
          type="number" 
          class="custom-input" 
          v-model="customMinutes"
          placeholder="请输入1-999之间的数字"
          maxlength="3"
        />
        <view class="custom-buttons">
          <button class="custom-cancel" @click="closeCustomModal">取消</button>
          <button class="custom-confirm" @click="confirmCustomTime">确定</button>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  name: 'SleepTimer',
  
  props: {
    // 外部传入的背景音频管理器
    audioManager: {
      type: Object,
      default: null
    }
  },
  
  data() {
    return {
      showModal: false,
      showCustomModal: false,
      selectedTime: 0,
      customMinutes: '',
      remainingTime: 0, // 剩余时间（秒）
      timer: null,
      timerOptions: [
        { label: '5分钟', value: 5 },
        { label: '10分钟', value: 10 },
        { label: '15分钟', value: 15 },
        { label: '30分钟', value: 30 },
        { label: '45分钟', value: 45 },
        { label: '60分钟', value: 60 },
        { label: '90分钟', value: 90 },
        { label: '120分钟', value: 120 }
      ]
    }
  },
  
  computed: {
    formatRemainingTime() {
      if (this.remainingTime <= 0) return ''
      
      const hours = Math.floor(this.remainingTime / 3600)
      const minutes = Math.floor((this.remainingTime % 3600) / 60)
      const seconds = this.remainingTime % 60
      
      if (hours > 0) {
        return `${hours}:${String(minutes).padStart(2, '0')}:${String(seconds).padStart(2, '0')}`
      } else {
        return `${minutes}:${String(seconds).padStart(2, '0')}`
      }
    }
  },
  
  beforeDestroy() {
    if (this.timer) {
      clearInterval(this.timer)
    }
  },
  
  methods: {
    showSleepTimerModal() {
      this.showModal = true
    },
    
    closeModal() {
      this.showModal = false
    },
    
    selectTime(value) {
      this.selectedTime = value
    },
    
    confirmTimer() {
      if (this.selectedTime > 0) {
        this.startTimer(this.selectedTime)
      }
      this.closeModal()
    },
    
    cancelTimer() {
      if (this.timer) {
        clearInterval(this.timer)
        this.timer = null
      }
      this.remainingTime = 0
      this.selectedTime = 0
      
      uni.showToast({
        title: '已取消定时',
        icon: 'none'
      })
      
      this.closeModal()
    },
    
    showCustomInput() {
      this.showCustomModal = true
      this.customMinutes = ''
    },
    
    closeCustomModal() {
      this.showCustomModal = false
      this.customMinutes = ''
    },
    
    confirmCustomTime() {
      const minutes = parseInt(this.customMinutes)
      
      if (isNaN(minutes) || minutes < 1 || minutes > 999) {
        uni.showToast({
          title: '请输入1-999之间的数字',
          icon: 'none'
        })
        return
      }
      
      this.selectedTime = minutes
      this.closeCustomModal()
      this.startTimer(minutes)
      this.closeModal()
    },
    
    startTimer(minutes) {
      // 清除之前的定时器
      if (this.timer) {
        clearInterval(this.timer)
      }
      
      this.remainingTime = minutes * 60
      
      uni.showToast({
        title: `将在${minutes}分钟后关闭`,
        icon: 'none'
      })
      
      // 每秒更新剩余时间
      this.timer = setInterval(() => {
        this.remainingTime--
        
        if (this.remainingTime <= 0) {
          this.stopAudioAndTimer()
        }
        
        // 最后10秒提醒
        if (this.remainingTime === 10) {
          uni.showToast({
            title: '10秒后将自动关闭',
            icon: 'none'
          })
        }
      }, 1000)
      
      // 发送事件通知父组件
      this.$emit('timer-start', minutes)
    },
    
    stopAudioAndTimer() {
      clearInterval(this.timer)
      this.timer = null
      this.remainingTime = 0
      this.selectedTime = 0
      
      // 停止音频播放
      if (this.audioManager) {
        this.audioManager.stop()
      }
      
      uni.showToast({
        title: '定时关闭',
        icon: 'none'
      })
      
      // 发送事件通知父组件
      this.$emit('timer-end')
    }
  }
}
</script>

<style lang="scss" scoped>
.sleep-timer-btn {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 10rpx;
  
  text {
    font-size: 40rpx;
    margin-bottom: 4rpx;
  }
  
  .timer-text {
    font-size: 20rpx;
    color: #666;
  }
}

.timer-modal {
  .modal-mask {
    position: fixed;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background: rgba(0, 0, 0, 0.5);
    z-index: 1000;
  }
  
  .modal-content {
    position: fixed;
    bottom: 0;
    left: 0;
    right: 0;
    background: #fff;
    border-radius: 24rpx 24rpx 0 0;
    z-index: 1001;
    animation: slideUp 0.3s ease;
    
    .modal-header {
      display: flex;
      align-items: center;
      justify-content: space-between;
      padding: 30rpx;
      border-bottom: 1rpx solid #f0f0f0;
      
      .modal-title {
        font-size: 32rpx;
        font-weight: 600;
        color: #333;
      }
      
      .modal-close {
        font-size: 40rpx;
        color: #999;
      }
    }
    
    .timer-options {
      padding: 20rpx;
      max-height: 600rpx;
      overflow-y: auto;
      
      .timer-option {
        display: flex;
        align-items: center;
        justify-content: space-between;
        padding: 30rpx 20rpx;
        border-radius: 16rpx;
        margin-bottom: 20rpx;
        background: #f7f7f7;
        transition: all 0.3s;
        
        &.active {
          background: linear-gradient(135deg, rgba(124, 58, 237, 0.1), rgba(168, 85, 247, 0.1));
          border: 2rpx solid #7C3AED;
          
          .option-text {
            color: #7C3AED;
            font-weight: 500;
          }
          
          .check-icon {
            color: #7C3AED;
            font-size: 36rpx;
          }
        }
        
        &.custom-option {
          background: #fff;
          border: 2rpx solid #e0e0e0;
        }
        
        &.cancel-option {
          background: #fff5f5;
          border: 2rpx solid #ff6b6b;
          
          .option-text {
            color: #ff6b6b;
          }
        }
        
        .option-text {
          font-size: 30rpx;
          color: #333;
        }
      }
    }
    
    .modal-footer {
      padding: 20rpx 30rpx 40rpx;
      
      .confirm-btn {
        width: 100%;
        height: 88rpx;
        background: linear-gradient(135deg, #7C3AED, #A855F7);
        color: #fff;
        font-size: 32rpx;
        font-weight: 500;
        border-radius: 44rpx;
        border: none;
        
        &:active {
          transform: scale(0.98);
        }
      }
    }
  }
}

.custom-modal {
  .modal-mask {
    position: fixed;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background: rgba(0, 0, 0, 0.5);
    z-index: 1002;
  }
  
  .custom-content {
    position: fixed;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    width: 600rpx;
    background: #fff;
    border-radius: 24rpx;
    padding: 40rpx;
    z-index: 1003;
    
    .custom-title {
      font-size: 32rpx;
      font-weight: 600;
      color: #333;
      text-align: center;
      margin-bottom: 40rpx;
    }
    
    .custom-input {
      width: 100%;
      height: 88rpx;
      border: 2rpx solid #e0e0e0;
      border-radius: 16rpx;
      padding: 0 30rpx;
      font-size: 32rpx;
      text-align: center;
      margin-bottom: 40rpx;
      
      &:focus {
        border-color: #7C3AED;
      }
    }
    
    .custom-buttons {
      display: flex;
      gap: 20rpx;
      
      button {
        flex: 1;
        height: 80rpx;
        border-radius: 40rpx;
        font-size: 30rpx;
        border: none;
        
        &.custom-cancel {
          background: #f0f0f0;
          color: #666;
        }
        
        &.custom-confirm {
          background: linear-gradient(135deg, #7C3AED, #A855F7);
          color: #fff;
        }
        
        &:active {
          transform: scale(0.98);
        }
      }
    }
  }
}

@keyframes slideUp {
  from {
    transform: translateY(100%);
  }
  to {
    transform: translateY(0);
  }
}
</style>