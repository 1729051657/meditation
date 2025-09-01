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
      <image class="w64" src="/static/player/back-arrow.png" lazy-load="false" binderror="" bindload="" />

    </view>

    <!-- 背景 -->
    <view class="bg-container">
      <!-- 模糊背景图 -->
      <image :src="track.coverUrl || '/static/images/default-cover.jpg'" mode="aspectFill" class="bg-image"></image>
      <!-- 半透明遮罩层 -->
      <view class="bg-overlay"></view>
    </view>

    <!-- 主内容 -->
    <view class="main-content">
      <!-- 封面 -->
      <view class="cover-container">
        <view class="cover-wrapper" :class="{ playing: playing }">
          <image :src="track.coverUrl || '/static/images/default-cover.jpg'" mode="aspectFill" class="cover-image"></image>
          <!-- 播放状态指示器 -->
          <view v-if="playing" class="playing-indicator">
            <view class="wave wave1"></view>
            <view class="wave wave2"></view>
            <view class="wave wave3"></view>
          </view>
        </view>
      </view>


      <view class="multimedia">
        <!-- 标题信息 -->
        <view class="info-section">
          <text class="track-title">{{ track.title }}</text>
          <!-- <text class="track-artist">{{ track.artist || '冥想音乐' }}</text> -->
          <view class="collect">
            <image class="w64" @click="toggleFavorite"
              :src="isFavorite ? '/static/player/favorite-active.png' : '/static/player/favorite-icon.png'"
              lazy-load="false" binderror="" bindload="" />
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
            <image class="w72" src="/static/player/playlist-icon.png" lazy-load="false" binderror="" bindload="" />

          </view>
          <view class="feature-btn" @click="togglePlay">
            <image class="w140"
              :src="playing ? '/static/player/close-pause-icon.png' : '/static/player/play-pause-icon.png'"
              lazy-load="false" binderror="" bindload="" />
          </view>
          <view class="feature-btn" @click="share">
            <image class="w72" src="/static/player/timer-icon.png" lazy-load="false" binderror="" bindload="" />
            <!-- 显示倒计时 -->
            <text v-if="sleepTimerRemaining > 0" class="timer-countdown">{{ formatTimerRemaining }}</text>
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
              :src="index === currentIndex ? '/static/player/close.png' : '/static/player/play-icon.png'"
              lazy-load="false" binderror="" bindload="" />
          </view>
          <view class="playlist-img">
            <image class="w32" src="/static/player/time-icon.png" lazy-load="false" binderror="" bindload="" />
            <view class="time">
              {{ item.duration }}
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
          <image class="w32" src="/static/player/close-icon.png" mode="aspectFit|aspectFill|widthFix" lazy-load="false"
            binderror="" bindload="" />
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
import { getTrackDetail, recordPlay, listTracks } from '@/api/track'
import { addFavorite, removeFavorite, checkFavorite, listFavorites } from '@/api/favorite'
import { addPlayHistory, updatePlayHistory } from '@/api/play'
import { mapState, mapGetters, mapActions } from 'vuex'

export default {
  // components: {
  //   SleepTimer  // 如果需要使用独立组件，取消注释
  // },
  data() {
    return {
      trackId: '',
      track: {},
      playing: false,
      currentTime: 0,
      duration: 0,
      progress: 0,
      isFavorite: false,
      favoriteId: null, // 收藏记录ID
      audioContext: null,
      showModal: false,
      currentIndex: 0,
      playlist: [], // 播放列表，从API获取
      seriesId: null, // 系列ID
      categoryId: null, // 分类ID
      selectedTime: 5, // 默认选中5分钟
      customTime: 0,   // 自定义时间
      showCustomModal: false, // 是否显示自定义模态框
      customInput: '',  // 自定义输入,
      showbad: false,
      navHeight: "", // 导航栏高度
      statusBarHeight: '', // 状态栏高度
      playHistoryId: null, // 播放历史记录ID
      lastUpdateTime: 0, // 上次更新播放进度的时间
      updateInterval: null, // 更新播放进度的定时器
    }
  },

  async onLoad(options) {
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

    this.trackId = options.id
    this.seriesId = options.seriesId || null
    this.categoryId = options.categoryId || null

    // 重置收藏状态
    this.isFavorite = false
    this.favoriteId = null

    // 如果有传递播放进度，恢复播放进度
    if (options.progress) {
      this.currentTime = parseInt(options.progress) || 0
    }

    // 如果是从迷你播放器跳转过来的（resume参数），不重新初始化音频
    if (options.resume === 'true') {
      // 只同步当前播放状态
      const audioContext = uni.getBackgroundAudioManager()
      if (audioContext && audioContext.src) {
        this.audioContext = audioContext
        this.playing = !audioContext.paused
        this.currentTime = audioContext.currentTime || 0
        this.duration = audioContext.duration || 0
        if (this.duration > 0) {
          this.progress = (this.currentTime / this.duration) * 100
        }
        // 设置音轨信息
        this.track = {
          title: audioContext.title || '未知音频',
          coverUrl: audioContext.coverImgUrl || '/static/images/default-cover.jpg',
          audioUrl: audioContext.src
        }
        // 如果正在播放，开始进度跟踪
        if (this.playing) {
          this.startProgressTracking()
        }
        // 加载播放列表
        this.loadPlaylist()
        // 检查收藏状态
        this.checkFavoriteStatus(this.trackId)
        return
      }
    }


    // 初始化音频管理器
    this.initAudio()
    // 加载指定的音频信息并播放
    await this.loadTrack()
    // 然后加载播放列表（不会影响当前播放）
    this.loadPlaylist()
  },
  computed: {
    ...mapState('timer', ['isTimerActive']),
    ...mapGetters('timer', ['remainingSeconds', 'remainingTime']),
    ...mapState('playlist', ['playlist', 'currentIndex', 'currentTrack', 'playMode']),
    ...mapGetters('playlist', ['hasNext', 'hasPrevious']),
    
    // 定时器剩余时间（兼容旧代码）
    sleepTimerRemaining() {
      return this.remainingSeconds
    },
    
    // 计算是否是自定义时间
    isCustom() {
      return this.selectedTime === this.customTime && this.customTime > 0;
    },

    // 格式化定时器剩余时间
    formatTimerRemaining() {
      return this.remainingTime
    }
  },
  onUnload() {
    // 页面卸载时，定时器继续在后台运行，不需要清理

    // 页面卸载时，如果音频正在播放，保持后台播放
    // 如果音频已停止，则清理资源
    if (this.audioContext && this.audioContext.src) {
      if (this.playing) {
        // 音频正在播放，保持后台播放，只清理页面相关资源
        this.cleanupPageResources()
      } else {
        // 音频已停止，清理所有资源
        this.cleanupAudioResources()
      }
    } else {
      // 没有音频，只清理页面资源
      this.cleanupPageResources()
    }
  },

  onHide() {
    // 页面隐藏时保存播放进度，保持后台播放
    this.savePlayProgress()

    // 确保音频继续在后台播放
    if (this.audioContext && this.audioContext.src && this.playing) {
      console.log('页面隐藏，音频继续在后台播放')
    }
  },

  onShow() {
    // 页面显示时恢复播放状态
    if (this.audioContext && this.audioContext.src) {
      // 同步播放状态
      this.playing = !this.audioContext.paused

      // 如果音频正在播放，重新开始进度跟踪
      if (this.playing) {
        this.startProgressTracking()
      }

      console.log('页面显示，播放状态已同步:', this.playing ? '播放中' : '已暂停')
    }
  },

  methods: {
    ...mapActions('timer', ['startSleepTimer', 'stopSleepTimer']),
    ...mapActions('playlist', ['setPlaylistAndPlay', 'playNext', 'playPrevious', 'togglePlayMode', 'addAndPlay']),
    initAudio() {
      // 使用后台音频管理器，支持后台播放
      this.audioContext = uni.getBackgroundAudioManager()

      console.log(this.audioContext)

      // 设置自动播放为false，需要用户手动触发
      this.audioContext.autoplay = false

      // 注意：这里不设置默认值，等待 loadTrack 时设置具体信息
      // 避免覆盖已有的音频信息

      this.audioContext.onPlay(() => {
        console.log('音频开始播放')
        this.playing = true
        this.startProgressTracking()
      })

      this.audioContext.onPause(() => {
        console.log('音频暂停')
        this.playing = false
        this.stopProgressTracking()
      })

      this.audioContext.onTimeUpdate(() => {
        this.currentTime = this.audioContext.currentTime
        this.duration = this.audioContext.duration
        if (this.duration > 0) {
          this.progress = (this.currentTime / this.duration) * 100
        }
      })

      this.audioContext.onEnded(() => {
        console.log('音频播放结束')
        this.playing = false
        this.markAsCompleted()

        // 播放下一曲
        this.next()
      })

      this.audioContext.onError((error) => {
        console.error('音频播放错误:', error)
        this.playing = false
        uni.showToast({
          title: '播放出错',
          icon: 'none'
        })
      })

      this.audioContext.onCanplay(() => {
        console.log('音频可以播放，时长:', this.audioContext.duration)
      })

      this.audioContext.onWaiting(() => {
        console.log('音频加载中...')
      })

      // 后台播放相关事件
      this.audioContext.onStop(() => {
        console.log('音频停止播放')
        this.playing = false
        this.stopProgressTracking()
      })

      this.audioContext.onPrev(() => {
        console.log('上一曲')
        this.previous()
      })

      this.audioContext.onNext(() => {
        console.log('下一曲')
        this.next()
      })
    },

    async loadTrack() {
      try {
        const res = await getTrackDetail(this.trackId)

        if (res.code === 200 && res.data) {
          // 映射后端返回的数据字段
          this.track = {
            id: res.data.id,
            title: res.data.title || '未知音频',
            artist: res.data.subtitle || res.data.author || '冥想音乐',
            coverUrl: res.data.coverUrl || '/static/images/default-cover.jpg',
            audioUrl: res.data.audioUrl || res.data.audio,
            duration: res.data.durationSec || 0,
            intro: res.data.intro || '',
            categoryId: res.data.categoryId,
            seriesId: res.data.seriesId
          }

          // 设置seriesId和categoryId如果没有传入
          if (!this.seriesId && res.data.seriesId) {
            this.seriesId = res.data.seriesId
          }
          if (!this.categoryId && res.data.categoryId) {
            this.categoryId = res.data.categoryId
          }

          if (this.track.audioUrl) {
            console.log('设置音频信息:', this.track)

            // 根据文档，title 是必填项，必须在设置 src 之前设置
            // 先停止当前播放（如果有）
            if (this.audioContext.src) {
              this.audioContext.stop()
            }

            // 重要：根据文档，必须先设置 title，再设置 src
            // title 必须在设置 src 之前设置，否则会显示"未知音频"
            this.audioContext.title = this.track.title || '冥想音频' // 必填，必须先设置

            // 设置其他可选属性
            this.audioContext.epname = this.track.title || '冥想音频' // 专辑名，选填
            this.audioContext.singer = this.track.artist || '冥想音乐' // 歌手名，选填
            this.audioContext.coverImgUrl = this.track.coverUrl || '/static/images/default-cover.jpg' // 封面图，选填

            // 最后设置 src，这会触发音频加载
            // 注意：src 必须最后设置
            this.audioContext.src = this.track.audioUrl

            console.log('音频信息设置完成:', {
              src: this.audioContext.src,
              title: this.audioContext.title,
              singer: this.audioContext.singer,
              coverImgUrl: this.audioContext.coverImgUrl
            })
            
            // 更新全局播放状态
            const trackForPlaylist = {
              id: this.track.id,
              title: this.track.title || '未知音频',
              artist: this.track.artist || '冥想音乐',
              durationSec: this.track.durationSec || 0,
              audioUrl: this.track.audioUrl,
              coverUrl: this.track.coverUrl || '/static/images/default-cover.jpg',
              categoryCode: this.track.categoryCode,
              seriesId: this.track.seriesId
            }
            this.$store.commit('playlist/SET_CURRENT_TRACK', trackForPlaylist)

            // 保存当前播放的音轨ID到本地存储，供迷你播放器使用
            uni.setStorageSync('currentTrackId', this.trackId)

            // 如果有传递的播放进度，恢复播放位置
            if (this.currentTime > 0) {
              setTimeout(() => {
                this.audioContext.seek(this.currentTime)
              }, 100)
            }
          }
        } else {
          throw new Error(res.msg || '获取音频信息失败')
        }

        // 检查收藏状态
        await this.checkFavoriteStatus(this.trackId)

        // 添加播放历史记录
        this.addToPlayHistory()
      } catch (error) {
        console.error('加载音频失败:', error)
        uni.showToast({
          title: '加载失败',
          icon: 'none'
        })
      }
    },

    // 添加到播放历史
    async addToPlayHistory() {
      try {
        const res = await addPlayHistory({
          trackId: this.trackId,
          progressSec: Math.floor(this.currentTime),
          isCompleted: '0'
        })

        if (res.code === 200 && res.data) {
          this.playHistoryId = res.data.id
        }
      } catch (error) {
        console.error('添加播放历史失败:', error)
      }
    },

    // 开始跟踪播放进度
    startProgressTracking() {
      // 每10秒更新一次播放进度
      this.updateInterval = setInterval(() => {
        this.savePlayProgress()
      }, 10000)
    },

    // 停止跟踪播放进度
    stopProgressTracking() {
      if (this.updateInterval) {
        clearInterval(this.updateInterval)
        this.updateInterval = null
      }
      // 暂停时立即保存进度
      this.savePlayProgress()
    },

    // 保存播放进度
    async savePlayProgress() {
      if (!this.playHistoryId || !this.trackId) return

      try {
        const progressSec = Math.floor(this.currentTime)
        const isCompleted = this.duration > 0 && this.currentTime >= this.duration * 0.95 ? '1' : '0'

        await updatePlayHistory({
          id: this.playHistoryId,
          trackId: this.trackId,
          progressSec: progressSec,
          isCompleted: isCompleted
        })
      } catch (error) {
        console.error('更新播放进度失败:', error)
      }
    },

    // 标记为已完成
    async markAsCompleted() {
      if (!this.playHistoryId || !this.trackId) return

      try {
        await updatePlayHistory({
          id: this.playHistoryId,
          trackId: this.trackId,
          progressSec: Math.floor(this.duration),
          isCompleted: '1'
        })
      } catch (error) {
        console.error('标记完成失败:', error)
      }
    },

    // 清理页面资源（保持音频播放）
    cleanupPageResources() {
      // 清理定时器
      if (this.updateInterval) {
        clearInterval(this.updateInterval)
      }

      // 保存播放进度
      this.savePlayProgress()

      console.log('页面资源已清理，音频继续在后台播放')
    },

    // 清理音频资源（完全停止播放）
    cleanupAudioResources() {
      // 清理定时器
      if (this.updateInterval) {
        clearInterval(this.updateInterval)
      }

      // 保存播放进度
      this.savePlayProgress()

      // 清理音频资源 - BackgroundAudioManager 没有 destroy 方法
      if (this.audioContext) {
        try {
          // 停止播放
          this.audioContext.stop()
          // 移除所有事件监听器
          this.audioContext.offPlay()
          this.audioContext.offPause()
          this.audioContext.offTimeUpdate()
          this.audioContext.offEnded()
          this.audioContext.offError()
          this.audioContext.offCanplay()
          this.audioContext.offWaiting()
          this.audioContext.offStop()
          this.audioContext.offPrev()
          this.audioContext.offNext()
          // 注意：不要清空 src，避免 invalid BackgroundAudioManager.src 错误

          // 清除存储的当前音轨ID
          uni.removeStorageSync('currentTrackId')
        } catch (error) {
          console.error('清理音频资源时出错:', error)
        }
      }
    },

    togglePlay() {
      console.log(this.audioContext)
      if (!this.audioContext) {
        uni.showToast({
          title: '音频未加载',
          icon: 'none'
        })
        return
      }

      if (!this.audioContext.src) {
        console.log(this.audioContext)
        uni.showToast({
          title: '音频地址无效',
          icon: 'none'
        })
        return
      }

      if (this.playing) {
        this.audioContext.pause()
      } else {
        this.audioContext.play().catch(error => {
          console.error('播放失败:', error)
          uni.showToast({
            title: '播放失败',
            icon: 'none'
          })
        })
      }
    },

    previous() {
      // 实现上一曲逻辑
      if (this.playlist.length === 0) {
        uni.showToast({
          title: '暂无上一曲',
          icon: 'none'
        })
        return
      }

      // 使用全局播放列表的上一首
      this.playPrevious()
    },

    next() {
      // 实现下一曲逻辑
      if (this.playlist.length === 0) {
        uni.showToast({
          title: '暂无下一曲',
          icon: 'none'
        })
        return
      }

      // 使用全局播放列表的下一首
      this.playNext()
    },


    // 加载播放列表
    async loadPlaylist() {
      try {
        let params = {
          status: 0,
          pageNum: 1,
          pageSize: 100
        }

        // 如果有系列ID，获取同系列的音频
        if (this.seriesId) {
          params.seriesId = this.seriesId
          params.orderByColumn = 'order_index'
          params.isAsc = 'asc'
        }
        // 如果有分类ID，获取同分类的音频
        else if (this.categoryId) {
          params.categoryId = this.categoryId
          params.orderByColumn = 'create_time'
          params.isAsc = 'desc'
        }
        // 否则获取推荐音频（适用于从首页冥想推荐进入的情况）
        else {
          params.orderByColumn = 'create_time'  // 使用创建时间排序
          params.isAsc = 'desc'  // 最新的在前
          params.pageSize = 50  // 限制数量，获取最新推荐
        }

        const res = await listTracks(params)

        if (res.code === 200) {
          const tracks = res.rows || res.data || []
          
          // 如果当前音频不在列表中，将其添加到列表开头
          let finalTracks = [...tracks]
          const currentTrackInList = tracks.find(item => item.id === parseInt(this.trackId))
          
          if (!currentTrackInList && this.track) {
            // 将当前播放的音频添加到列表开头
            finalTracks = [this.track, ...tracks]
          }
          
          // 格式化播放列表数据
          const formattedTracks = finalTracks.map(item => ({
            id: item.id,
            title: item.title || '未知音频',
            artist: item.subtitle || item.author || '冥想音乐',
            durationSec: item.durationSec || 0,
            audioUrl: item.audioUrl || item.audio,
            coverUrl: item.coverUrl || '/static/images/default-cover.jpg',
            categoryCode: item.categoryCode,
            seriesId: item.seriesId
          }))
          
          // 找到当前播放音频在列表中的位置
          const currentIndex = formattedTracks.findIndex(item => item.id === parseInt(this.trackId))
          
          // 只设置播放列表，不重新播放（因为loadTrack已经在播放了）
          this.$store.commit('playlist/SET_PLAYLIST', formattedTracks)
          if (currentIndex !== -1) {
            this.$store.commit('playlist/SET_CURRENT_INDEX', currentIndex)
          }
          
          // 本地也保存一份用于显示
          this.playlist = formattedTracks.map(item => ({
            id: item.id,
            name: item.title,
            duration: item.durationSec ? this.formatDuration(item.durationSec) : '未知',
            audioUrl: item.audioUrl,
            coverUrl: item.coverUrl,
            artist: item.artist
          }))
          
          if (currentIndex !== -1) {
            this.currentIndex = currentIndex
          }
        }
      } catch (error) {
        console.error('加载播放列表失败:', error)
      }
    },

    // 播放指定索引的音频
    async playTrackAtIndex(index) {
      if (index < 0 || index >= this.playlist.length) return

      const track = this.playlist[index]
      if (!track) return

      // 停止当前播放
      if (this.audioContext) {
        this.audioContext.stop()
      }

      // 更新当前音频信息
      this.trackId = track.id
      this.currentIndex = index
      this.track = {
        id: track.id,
        title: track.name,
        artist: track.artist,
        coverUrl: track.coverUrl,
        audioUrl: track.audioUrl,

      }
       console.log(track)
      // 设置新的音频源并播放
      if (track.audioUrl) {
        // 重要：根据文档，必须先设置 title，再设置 src
        // title 必须在设置 src 之前设置，否则会显示"未知音频"
        this.audioContext.title = track.name || '冥想音频' // 必填，必须先设置

        // 设置其他可选属性
        this.audioContext.epname = track.name || '冥想音频' // 专辑名
        this.audioContext.singer = track.artist || '冥想音乐' // 歌手名
        this.audioContext.coverImgUrl = track.coverUrl || '/static/images/default-cover.jpg' // 封面图

        // 最后设置 src，这会触发音频加载
        // 注意：src 必须最后设置
        this.audioContext.src = track.audioUrl

        // 保存当前播放的音轨ID
        uni.setStorageSync('currentTrackId', track.id)

        this.currentTime = 0
        this.progress = 0

        // 延迟一下再播放，确保音频加载
        setTimeout(() => {
          this.audioContext.play()
        }, 100)
      }

      // 检查收藏状态
      await this.checkFavoriteStatus(track.id)

      // 添加播放历史
      this.addToPlayHistory()
    },

    // 格式化时长
    formatDuration(seconds) {
      if (!seconds) return '未知'
      const minutes = Math.ceil(seconds / 60)
      return `${minutes}分钟`
    },

    // 检查收藏状态
    async checkFavoriteStatus(trackId) {
      try {
        console.log('检查收藏状态, trackId:', trackId)

        // 先检查是否收藏
        const checkRes = await checkFavorite(trackId, 'track')
        console.log('收藏状态检查结果:', checkRes)

        if (checkRes.code === 200) {
          this.isFavorite = checkRes.data === true || checkRes.data === 'true' || checkRes.data === 1
          console.log('是否已收藏:', this.isFavorite)

          // 如果已收藏，获取收藏记录ID
          if (this.isFavorite) {
            const listRes = await listFavorites({
              targetId: trackId,
              targetType: 'track',
              pageNum: 1,
              pageSize: 1
            })
            console.log('收藏列表结果:', listRes)

            if (listRes.code === 200 && listRes.rows && listRes.rows.length > 0) {
              this.favoriteId = listRes.rows[0].id
              console.log('收藏记录ID (rows):', this.favoriteId)
            } else if (listRes.code === 200 && listRes.data && listRes.data.length > 0) {
              this.favoriteId = listRes.data[0].id
              console.log('收藏记录ID (data):', this.favoriteId)
            } else {
              console.warn('已收藏但未找到收藏记录ID')
              this.favoriteId = null
            }
          } else {
            this.favoriteId = null
            console.log('未收藏，清空favoriteId')
          }
        }
      } catch (error) {
        console.error('检查收藏状态失败:', error)
        this.isFavorite = false
        this.favoriteId = null
      }
    },

    onProgressChange(e) {
      const position = (e.detail.value / 100) * this.duration
      this.audioContext.seek(position)
    },

    async toggleFavorite() {
      try {
        console.log('切换收藏状态, 当前状态:', this.isFavorite, 'favoriteId:', this.favoriteId, 'trackId:', this.trackId)

        uni.showLoading({
          title: this.isFavorite ? '取消收藏中...' : '收藏中...'
        })

        if (this.isFavorite) {
          // 取消收藏
          if (!this.favoriteId) {
            // 如果没有favoriteId，先获取
            const listRes = await listFavorites({
              targetId: this.trackId,
              targetType: 'track',
              pageNum: 1,
              pageSize: 1
            })

            if (listRes.code === 200) {
              if (listRes.rows && listRes.rows.length > 0) {
                this.favoriteId = listRes.rows[0].id
              } else if (listRes.data && listRes.data.length > 0) {
                this.favoriteId = listRes.data[0].id
              }
            }
          }

          if (this.favoriteId) {
            console.log('取消收藏, favoriteId:', this.favoriteId)
            const res = await removeFavorite(this.favoriteId)
            console.log('取消收藏结果:', res)

            if (res.code === 200) {
              this.isFavorite = false
              this.favoriteId = null
              console.log('收藏已取消')

              uni.hideLoading()
              uni.showToast({
                title: '已取消收藏',
                icon: 'success'
              })
            } else {
              throw new Error(res.msg || '取消收藏失败')
            }
          } else {
            throw new Error('未找到收藏记录')
          }
        } else {
          // 添加收藏
          const res = await addFavorite(this.trackId, 'track')
          console.log('添加收藏结果:', res)

          if (res.code === 200) {
            this.isFavorite = true
            // 尝试从不同的响应格式中获取ID
            this.favoriteId = res.data?.id || res.data || res.id || null
            console.log('新收藏记录ID:', this.favoriteId)

            uni.hideLoading()
            uni.showToast({
              title: '收藏成功',
              icon: 'success'
            })
          } else {
            throw new Error(res.msg || '收藏失败')
          }
        }
      } catch (error) {
        uni.hideLoading()
        console.error('收藏操作失败:', error)
        uni.showToast({
          title: error.message || '操作失败',
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
      // 显示定时器面板
      this.showbad = true;
    },

    // 清除睡眠定时器（使用全局方法）
    clearSleepTimer() {
      this.stopSleepTimer()
      this.selectedTime = 5
      this.customTime = 0
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
      this.playTrackAtIndex(index);
      this.closeModal();
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
      // 立即启动全局定时器
      this.startSleepTimer(time);
      // 关闭定时器面板
      this.showbad = false;
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
        uni.showToast({
          title: '请输入有效的时间',
          icon: 'none'
        });
        return;
      }

      this.customTime = parseInt(this.customInput);
      this.selectedTime = this.customTime;
      this.showCustomModal = false;
      console.log('自定义了', this.customTime, '分钟');
      // 启动全局定时器
      this.startSleepTimer(this.customTime);
      // 关闭定时器面板
      this.showbad = false;
    },

    // 关闭定时器
    closeTimer() {
      // 如果有正在运行的定时器，清除它
      if (this.isTimerActive) {
        this.clearSleepTimer()
        uni.showToast({
          title: '已取消定时',
          icon: 'none'
        })
      }
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
  overflow: hidden;

  .bg-image {
    width: 110%;
    height: 110%;
    margin: -5%;
    filter: blur(20rpx) brightness(0.85) saturate(1.2);
    transform: scale(1.1);
    animation: slowZoom 20s ease-in-out infinite alternate;
  }

  .bg-overlay {
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background: linear-gradient(
      180deg,
      rgba(0, 0, 0, 0.15) 0%,
      rgba(0, 0, 0, 0.25) 50%,
      rgba(0, 0, 0, 0.4) 100%
    );
  }
}

@keyframes slowZoom {
  0% {
    transform: scale(1.1);
  }
  100% {
    transform: scale(1.2);
  }
}

@keyframes rotate {
  from {
    transform: rotate(0deg);
  }
  to {
    transform: rotate(360deg);
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
  perspective: 1000rpx;

  .cover-wrapper {
    width: 100%;
    height: 100%;
    border-radius: 30rpx;
    overflow: hidden;
    box-shadow: 0 30rpx 80rpx rgba(0, 0, 0, 0.4);
    position: relative;
    transition: transform 0.3s ease;

    &.playing {
      animation: float 6s ease-in-out infinite;
    }

    .cover-image {
      width: 100%;
      height: 100%;
      object-fit: cover;
    }

    .playing-indicator {
      position: absolute;
      bottom: 30rpx;
      right: 30rpx;
      display: flex;
      align-items: flex-end;
      gap: 6rpx;

      .wave {
        width: 6rpx;
        background: linear-gradient(to top, #7C3AED, #A855F7);
        border-radius: 3rpx;
        animation: wave 1.2s ease-in-out infinite;

        &.wave1 {
          height: 20rpx;
          animation-delay: 0s;
        }

        &.wave2 {
          height: 30rpx;
          animation-delay: 0.2s;
        }

        &.wave3 {
          height: 25rpx;
          animation-delay: 0.4s;
        }
      }
    }
  }
}

@keyframes float {
  0%, 100% {
    transform: translateY(0) rotateY(0);
  }
  50% {
    transform: translateY(-20rpx) rotateY(5deg);
  }
}

@keyframes wave {
  0%, 100% {
    transform: scaleY(0.5);
    opacity: 0.5;
  }
  50% {
    transform: scaleY(1);
    opacity: 1;
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
.timer-countdown {
  position: absolute;
  bottom: -10rpx;
  left: 50%;
  transform: translateX(-50%);
  font-size: 20rpx;
  color: #7C3AED;
  font-weight: 600;
  background: rgba(124, 58, 237, 0.1);
  padding: 2rpx 8rpx;
  border-radius: 10rpx;
  min-width: 50rpx;
  text-align: center;
}
</style>
