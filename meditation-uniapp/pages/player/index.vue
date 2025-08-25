<template>
  <view class="player-page">
    <!-- 自定义导航栏 -->
    <!-- <tn-nav-bar :isBack="true" :bottomShadow="false" backgroundColor="transparent" :fixed="true">
      <view class="nav-title">
        <text class="nav-title-text">{{ track.title || '播放器' }}</text>
      </view>
    </tn-nav-bar> -->
    <view class="topNav" :style="{ height: navHeight + 'px', paddingTop: statusBarHeight + 'px' }">
    </view>
    <view class="imggo" @click="go">
      <tn-icon name="left" size="64" color="#FFFFFF"></tn-icon>

    </view>

    <!-- 背景 -->
    <view class="bg-container">
      <image :src="track.cover || '/static/images/default-cover.jpg'" mode="aspectFill" class="bg-image"></image>
      <view class="bg-overlay"></view>
    </view>

    <!-- 主内容 -->
    <view class="main-content">
      <!-- 封面 -->
      <!-- <view class="cover-container">
        <image :src="track.cover || '/static/images/default-cover.jpg'" mode="aspectFill" class="cover-image"></image>
      </view> -->


      <view class="multimedia">
        <!-- 标题信息 -->
        <view class="info-section">
          <text class="track-title">{{ track.title }}</text>
          <text class="track-artist">{{ track.artist || '冥想音乐' }}</text>
          <view class="collect">
            <tn-icon @click="toggleFavorite" :name="isFavorite ? 'like-fill' : 'like'" size="64" :color="isFavorite ? '#FF6B6B' : '#FFFFFF'"></tn-icon>
          </view>
        </view>
        <!-- 进度条 -->
        <view class="progress-section">
          <view class="time-info">
            <text class="time-text">{{ formatTime(currentTime) }}</text>
            <text class="time-text">{{ formatTime(duration) }}</text>
          </view>
          <slider :value="progress" @change="onProgressChange" activeColor="#7C3AED" backgroundColor="#E5E7EB"
            block-color="#7C3AED" block-size="12" />
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
          <view class="feature-btn" @click="openModal">
            <!-- <text :class="isFavorite ? 'tn-icon-like-fill' : 'tn-icon-like'"
              :style="{ color: isFavorite ? '#ef4444' : '#6B7280' }"></text>
            <text class="feature-text">收藏</text> -->
            <image class="w72"
              src="https://xc-obs-shwg-ssmwl-01.bawutech.com:443/travel-1755843485258En21GcZ17B9gadf295beb3f3ee975af8672cb46409f1.png"
              lazy-load="false" binderror="" bindload="" />

          </view>
          <view class="feature-btn">
            <!-- <text class="tn-icon-list"></text>
            <text class="feature-text">列表</text> -->
            <image class="w140"
              src="https://xc-obs-shwg-ssmwl-01.bawutech.com:443/travel-1755843561221KAeMI4d0Fvzdb07dd1a364a83bdc6c99e7d5e09510a2.png"
              lazy-load="false" binderror="" bindload="" />
          </view>
          <view class="feature-btn" @click="share">
            <!-- <text class="tn-icon-share"></text>
            <text class="feature-text">分享</text> -->
            <image class="w72"
              src="https://xc-obs-shwg-ssmwl-01.bawutech.com:443/travel-1755843633473Bqn1b9qNgof7d783fc205660866c24080089d2f9ca2c.png"
              lazy-load="false" binderror="" bindload="" />
          </view>
        </view>
      </view>
    </view>
    <!-- 弹框 -->
    <view class="modal-overlay" :style="{ display: showModal ? 'block' : 'none' }" @click="closeModal"></view>

    <view class="modal-content" :style="{ display: showModal ? 'block' : 'none' }">
      <view class="modal-header">
        <view class="modal-title">当前播放列表</view>
      </view>

      <view class="playlist">
        <div class="">
          <!-- 内容 -->
        </div>
        <view v-for="(item, index) in playlist" :key="index" @click="setCurrent(index)">
          <view class="playlist-item">
            <view class="song-info">
              <view class="song-index">{{ index + 1 }}</view>
              <view class="song-name" :class="{ song: index === currentIndex }">{{ item.name }}</view>
            </view>
            <image class="w56"
              src="https://xc-obs-shwg-ssmwl-01.bawutech.com:443/travel-1755847898305VhP0ejwQ1BpUcf7a9d5915d6277a02a4125c26c2c4b8.png"
              lazy-load="false" binderror="" bindload="" />
          </view>
          <view class="playlist-img">
            <image class="w32"
              src="https://xc-obs-shwg-ssmwl-01.bawutech.com:443/travel-1755848032634RdnYr76e4Es2d783fc205660866c24080089d2f9ca2c.png"
              lazy-load="false" binderror="" bindload="" />
            <view class="time">
              10分钟
            </view>

          </view>

          <!-- <view class="song-duration" :class="{ 'current-playing': index === currentIndex }" >○ {{ item.duration }}</view> -->
        </view>
      </view>
    </view>
    <!-- 定时器选项 -->
    <!-- 主定时器模态框 -->
    <view class="modal-container" v-if="showbad">
      <view class="header">
        <view class="header-title">定时器</view>
        <view class="close-icon" @click="closeTimer">
          <image class="w32"
            src="https://xc-obs-shwg-ssmwl-01.bawutech.com:443/travel-1756088299065evuipLOb7sNW1cde2600d40e47e989f15a77377d4097.png"
            mode="aspectFit|aspectFill|widthFix" lazy-load="false" binderror="" bindload="" />
          结束定时
        </view>
      </view>

      <view class="timer-options">
        <view class="option-row">
          <view class="option" :class="{ active: selectedTime === 5 }" @click="selectTime(5)">
            5分钟
            <!-- <view class="selected-indicator" v-if="selectedTime === 5">✓</view> -->
          </view>
          <view class="option" :class="{ active: selectedTime === 10 }" @click="selectTime(10)">
            10分钟
            <!-- <view class="selected-indicator" v-if="selectedTime === 10">✓</view> -->
          </view>
          <view class="option" :class="{ active: selectedTime === 20 }" @click="selectTime(20)">
            20分钟
            <!-- <view class="selected-indicator" v-if="selectedTime === 20">✓</view> -->
          </view>
        </view>

        <view class="option-row">
          <view class="option" :class="{ active: selectedTime === 30 }" @click="selectTime(30)">
            30分钟
            <!-- <view class="selected-indicator" v-if="selectedTime === 30">✓</view> -->
          </view>
          <view class="option" :class="{ active: selectedTime === 60 }" @click="selectTime(60)">
            60分钟
            <!-- <view class="selected-indicator" v-if="selectedTime === 60">✓</view> -->
          </view>
          <view class="option" :class="{ active: isCustom }" @click="openCustomModal">
            {{ customTime ? customTime + '分钟' : '自定义' }}
            <!-- <view class="selected-indicator" v-if="isCustom">✓</view> -->
          </view>
        </view>
      </view>
    </view>

    <!-- 自定义时间模态框 -->
    <view class="custom-modal" :class="{ active: showCustomModal }">
      <view class="modal-contents">
        <view class="modal-title" style="margin: 40rpx 0;">自定义定时</view>
        <input type="number" class="time-input" placeholder="输入时间（分钟）" v-model="customInput">
        <view class="modal-buttons">
          <view class="modal-btn cancel-btn" @click="cancelCustom">取消</view>
          <view class="modal-btn confirm-btn" @click="confirmCustom">确定</view>
        </view>
      </view>
    </view>

  </view>

</template>

<script>
import { getTrack, recordPlay, listTracks } from '@/api/track'
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
      audioContext: null,
      showModal: false,
      currentIndex: 0,
      playlist: [], // 播放列表，从API获取
      seriesId: null, // 系列ID
      selectedTime: 5, // 默认选中5分钟
      customTime: 0,   // 自定义时间
      showCustomModal: false, // 是否显示自定义模态框
      customInput: '',  // 自定义输入,
      showbad: false,
      navHeight: "", // 导航栏高度
      statusBarHeight: '', // 状态栏高度
    }
  },

  onLoad(options) {
    //获取手机系统的信息 里面有状态栏高度和设备型号
    let {
      statusBarHeight,
      system
    } = uni.getSystemInfoSync()
    // 注意返回的单位是px 不是rpx
    console.log('状态栏高度是', statusBarHeight + 'px', '设备型号是', system);
    this.statusBarHeight = statusBarHeight
    // 而导航栏的高度则 = 状态栏的高度 + 判断机型的值（是ios就+40，否则+44）
    // 这个高度刚好和胶囊对齐
    this.navHeight = statusBarHeight + (system.indexOf('iOS') > -1 ? 40 : 44)
    console.log(this.navHeight, "导航栏高度");

    // 支持两种参数：trackId 或 id
    this.trackId = options.trackId || options.id
    this.seriesId = options.seriesId
    this.initAudio()
    this.loadTrack()
    if (this.seriesId) {
      this.loadPlaylist()
    }
  },
  computed: {
    // 计算是否是自定义时间
    isCustom() {
      return this.selectedTime === this.customTime && this.customTime > 0;
    }
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
        const res = await getTrack(this.trackId)
        this.track = res.data
        
        // 如果没有传seriesId，从track中获取
        if (!this.seriesId && this.track.seriesId) {
          this.seriesId = this.track.seriesId
          this.loadPlaylist()
        }

        // 处理音频URL - 如果是OSS ID需要转换
        if (this.track.audioUrl) {
          // 如果audioUrl是数字（OSS ID），转换为URL
          if (!isNaN(this.track.audioUrl)) {
            this.audioContext.src = this.oss(this.track.audioUrl)
          } else {
            this.audioContext.src = this.track.audioUrl
          }
        } else if (this.track.audioId) {
          // 兼容audioId字段
          this.audioContext.src = this.oss(this.track.audioId)
        }

        // 检查收藏状态
        try {
          const favRes = await checkFavorite(this.trackId, 'track')
          this.isFavorite = favRes.data || false
        } catch (error) {
          console.error('检查收藏状态失败:', error)
          this.isFavorite = false
        }

        // 记录播放
        recordPlay(this.trackId)
        
        // 更新当前索引
        if (this.playlist.length > 0) {
          this.currentIndex = this.playlist.findIndex(item => item.id == this.trackId)
        }
      } catch (error) {
        console.error('加载音频失败:', error)
        uni.showToast({
          title: '加载失败',
          icon: 'none'
        })
      }
    },
    
    // 加载播放列表
    async loadPlaylist() {
      if (!this.seriesId) return
      
      try {
        const res = await listTracks({
          seriesId: this.seriesId,
          status: 0,
          orderByColumn: 'order_index',
          isAsc: 'asc',
          pageNum: 1,
          pageSize: 100
        })
        
        const tracks = res.rows || res.data || []
        this.playlist = tracks.map(item => ({
          id: item.id,
          name: item.title,
          duration: this.formatDuration(item.durationSec)
        }))
        
        // 更新当前索引
        if (this.trackId) {
          this.currentIndex = this.playlist.findIndex(item => item.id == this.trackId)
        }
      } catch (error) {
        console.error('加载播放列表失败:', error)
      }
    },
    
    // 格式化时长
    formatDuration(seconds) {
      if (!seconds) return '0分钟'
      const minutes = Math.ceil(seconds / 60)
      return `${minutes}分钟`
    },
    
    // OSS文件转URL
    oss(id) {
      if (!id) return ''
      const baseUrl = this.$baseUrl || 'https://thoughtadmin.bawutech.com/prod-api/'
      return `${baseUrl}system/oss/download/${id}`
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
      this.showbad = true;
    },

    formatTime(seconds) {
      if (!seconds) return '00:00'
      const min = Math.floor(seconds / 60)
      const sec = Math.floor(seconds % 60)
      return `${min.toString().padStart(2, '0')}:${sec.toString().padStart(2, '0')}`
    },
    openModal() {
      this.showModal = true;
    },
    closeModal() {
      this.showModal = false;
    },
    setCurrent(index) {
      this.currentIndex = index;
    },
    go() {
      uni.navigateBack({
        delta: 1
      })
    },
    // 定时器
    // 选择时间
    selectTime(time) {
      this.selectedTime = time;
      this.customTime = 0; // 重置自定义时间
      console.log('选择了', time, '分钟');
    },

    // 打开自定义模态框
    openCustomModal() {
      this.showCustomModal = true;
      this.customInput = this.customTime > 0 ? this.customTime : '';
    },

    // 取消自定义
    cancelCustom() {
      this.showCustomModal = false;
      this.customInput = '';
    },

    // 确认自定义时间
    confirmCustom() {
      if (!this.customInput || this.customInput <= 0) {
        alert('请输入有效的时间');
        return;
      }

      this.customTime = parseInt(this.customInput);
      this.selectedTime = this.customTime;
      this.showCustomModal = false;
      console.log('自定义了', this.customTime, '分钟');
    },

    // 关闭定时器
    closeTimer() {
      this.showbad = false;
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

  margin-bottom: 60rpx;
  padding: 0 40rpx;
  display: flex;

  .track-title {
    display: block;
    font-size: 36rpx;
    color: #fff;
    font-weight: 600;
    margin-bottom: 20rpx;
  }

  .track-artist {
    display: block;
    font-weight: 600;
    font-size: 44rpx;
    color: #FFFFFF;
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
  align-items: center;
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

.multimedia {
  position: absolute;
  bottom: 50rpx;
  width: 100%;
}

.w64 {
  width: 64rpx;
  height: 64rpx;
}

.collect {
  width: 100%;
  flex: 1;
  display: flex;
  align-items: flex-start;
  justify-content: flex-end;
}

.w72 {
  width: 72rpx;
  height: 72rpx;
}

.w140 {
  width: 140rpx;
  height: 140rpx;
}

// 模态框
/* 页面容器 */
.container {
  width: 100%;
  height: 100vh;
  background: #f7f7f7;
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 40rpx;
}

/* 页面标题 */
.page-title {
  font-size: 36rpx;
  font-weight: 600;
  color: #333;
  margin: 40rpx 0;
  text-align: center;
}

/* 触发按钮 */
.open-btn {
  background-color: #07C160;
  color: white;
  border: none;
  padding: 24rpx 48rpx;
  border-radius: 8rpx;
  font-size: 32rpx;
  cursor: pointer;
  display: block;
  margin: 40rpx auto;
  box-shadow: 0 8rpx 24rpx rgba(7, 193, 96, 0.3);
  width: 80%;
}

/* 模态框覆盖层 */
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  display: none;
  z-index: 999;
}

/* 模态框内容 */
.modal-content {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  background: #464C51;
  border-radius: 40rpx 40rpx 0rpx 0rpx;
  padding: 40rpx;
  height: 70vh;
  overflow-y: auto;
  box-shadow: 0 -4rpx 20rpx rgba(0, 0, 0, 0.1);
  display: none;
  z-index: 1000;
}

/* 模态框标题 */
.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.modal-title {
  font-weight: 500;
  font-size: 36rpx;
  color: #FFFFFF;
}

.close-btn {
  background: none;
  border: none;
  font-size: 40rpx;
  color: #999;
  cursor: pointer;
}

/* 播放列表 */
.playlist {
  list-style: none;
  margin-top: 20rpx;
}

.playlist-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 24rpx 0;
  padding-bottom: 4rpx;

}

.playlist-item:last-child {
  border-bottom: none;
}

.song-info {
  display: flex;
  align-items: center;
  flex: 1;
}

.song-index {
  font-size: 32rpx;
  font-weight: 500;
  font-size: 36rpx;
  color: #93B0C5;
  margin-right: 24rpx;
  min-width: 40rpx;
}

.song-name {
  font-weight: 500;
  font-size: 36rpx;
  color: #FFFFFF;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  max-width: 480rpx;
}

.song-duration {
  font-size: 28rpx;
  color: #999;
}

/* 当前播放指示器 */
.current-playing {
  position: relative;
}

.current-playing::after {
  content: '';
  display: inline-block;
  width: 16rpx;
  height: 16rpx;
  border-radius: 50%;
  background-color: #07C160;
  margin-left: 16rpx;
  animation: pulse 1.5s infinite;
}

@keyframes pulse {
  0% {
    opacity: 1;
  }

  50% {
    opacity: 0.4;
  }

  100% {
    opacity: 1;
  }
}

/* 响应式设计 */
@media (min-width: 768px) {
  .container {
    max-width: 375px;
    margin: 0 auto;
    border: 1rpx solid #ddd;
    border-radius: 24rpx;
    height: 700rpx;
    margin-top: 40rpx;
  }

  .modal-content {
    left: 50%;
    right: auto;
    width: 600rpx;
    transform: translateX(-50%);
    border-radius: 24rpx;
    bottom: 50%;
    transform: translate(-50%, 50%);
  }
}

.w56 {
  width: 56rpx;
  height: 56rpx;
}

.w32 {
  width: 32rpx;
  height: 32rpx;
  margin-right: 4rpx;
}

.playlist-img {
  display: flex;
  align-items: center;
  padding-bottom: 24rpx;
}

.time {
  font-weight: 400;
  font-size: 28rpx;
  color: #888888;
}

.song {
  font-weight: 500;
  font-size: 36rpx;
  color: #6CC0FF;
}

// 定时器
/* 外部容器 */
.page-container {
  width: 100%;
  padding: 40rpx;
  display: flex;
  justify-content: center;
  align-items: center;
}

/* 模态框容器 */
.modal-container {
  width: 100%;
  background: #464C51;
  border-radius: 16rpx;
  box-shadow: 0 8rpx 24rpx rgba(0, 0, 0, 0.1);
  overflow: hidden;
  box-sizing: border-box;
  position: absolute;
  bottom: 0;
  height: 696rpx;
  border-radius: 40rpx 40rpx 0rpx 0rpx;
}

/* 头部样式 */
.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 32rpx;
  background: #464C51;
  border-radius: 40rpx 40rpx 0rpx 0rpx;
  color: white;
  position: relative;
}

.header-title {
  font-size: 36rpx;
  font-weight: 500;
  flex: 1;

}

.close-icon {
  width: 208rpx;
  height: 64rpx;
  background: rgba(255, 255, 255, 0.2);
  border-radius: 32rpx;
  font-weight: 400;
  font-size: 32rpx;
  color: #FFFFFF;
  display: flex;
  align-items: center;
  justify-content: center;
}

/* 定时器选项 */
.timer-options {
  padding: 32rpx;
}

.option-row {
  display: flex;
  justify-content: space-between;
  margin-bottom: 30rpx;
}

.option {
  width: 220rpx;
  height: 72rpx;

  border-radius: 36rpx;

  display: flex;
  justify-content: center;
  align-items: center;
  font-weight: 400;
  font-size: 32rpx;

  transition: all 0.3s;
  cursor: pointer;
  position: relative;
  background: rgba(255, 255, 255, 0.2);
  color: #FFFFFF;
}

.option.active {
  background: rgba(255, 255, 255, 0.2);
  border-radius: 36rpx;
  color: #FFFFFF;
  border: 1rpx solid rgba(255, 255, 255, 0.8);

}

.selected-indicator {
  position: absolute;
  top: 10rpx;
  right: 10rpx;
  width: 36rpx;
  height: 36rpx;
  background: #fff;
  border-radius: 50%;
  display: flex;
  justify-content: center;
  align-items: center;
  color: #07C160;
  font-size: 24rpx;
  font-weight: bold;
}

/* 自定义模态框 */
.custom-modal {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;

  display: flex;
  justify-content: center;
  align-items: center;
  opacity: 0;
  pointer-events: none;
  transition: opacity 0.3s;
  z-index: 1000;
}

.custom-modal.active {
  opacity: 1;
  pointer-events: all;
}

.modal-contents {
  width: 100%;
  background: #464C51;
  border-radius: 16rpx;
  box-shadow: 0 8rpx 24rpx rgba(0, 0, 0, 0.1);
  overflow: hidden;
  box-sizing: border-box;
  position: absolute;
  bottom: 0;
  height: 696rpx;
  padding: 0 24rpx;
  border-radius: 40rpx 40rpx 0rpx 0rpx;
  z-index: 1;
}

.modal-title {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: #464C51;
  border-radius: 40rpx 40rpx 0rpx 0rpx;
  color: white;
  position: relative;
}

.time-input {
  width: 100%;

  height: 104rpx;
  background: rgba(255, 255, 255, 0.2);
  border-radius: 52rpx;
  padding: 0 40rpx;
  color: #fff;
  margin-bottom: 76rpx;
}

.modal-buttons {
  display: flex;
  justify-content: space-around;
}

.modal-btn {
  width: 220rpx;
  height: 80rpx;
  background: #FFFFFF;
  border-radius: 40rpx;
  background: rgba(255, 255, 255, 0.2);
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 400;
  font-size: 32rpx;
  color: #FFFFFF;

}



.confirm-btn {
  background: #79B1DA;

}

.imggo {
  width: 100%;
  padding: 0 24rpx;
}

.w64 {
  width: 64rpx;
  height: 64rpx;
}
</style>