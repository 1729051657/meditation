<template>
  <view class="history-page">
    <!-- 内容区域 -->
    <view class="content-area">
      <!-- 历史记录列表 -->
      <view class="history-container" v-if="historyList.length > 0">
        <!-- 按日期分组 -->
        <view class="date-section" v-for="(group, dateIndex) in groupedHistory" :key="dateIndex">
          <text class="date-header">{{ group.date }}</text>
          
          <view class="history-grid">
            <view 
              class="history-card" 
              v-for="(item, index) in group.items" 
              :key="item.id"
              @click="playAudio(item)"
            >
              <view class="card-image-wrapper">
                <image :src="item.cover" mode="aspectFill" class="card-image"></image>
                <!-- 进度条 -->
                <view class="progress-bar">
                  <view class="progress-fill" :style="{ width: item.progressPercent + '%' }"></view>
                </view>
                <!-- 播放时间 -->
                <view class="time-badge">{{ formatTime(item.lastPlayTime) }}</view>
              </view>
              <view class="card-content">
                <text class="card-title">{{ item.title }}</text>
                <text class="card-series" v-if="item.seriesTitle">{{ item.seriesTitle }}</text>
                <view class="card-meta">
                  <text class="progress-text">已播放{{ item.progressPercent }}%</text>
                </view>
              </view>
            </view>
          </view>
        </view>
      </view>
      
      <!-- 空状态 -->
      <view class="empty-state" v-else>
        <image src="/static/images/empty-history.svg" class="empty-image"></image>
        <text class="empty-text">还没有播放记录</text>
        <text class="empty-tips">开始您的第一次冥想之旅吧</text>
        <view class="go-home-btn" @click="goHome">
          <text class="btn-text">去探索</text>
        </view>
      </view>
      
      <!-- 清空历史按钮 -->
      <view class="clear-btn" v-if="historyList.length > 0" @click="clearHistory">
        <text class="tn-icon-delete"></text>
        <text class="clear-text">清空历史</text>
      </view>
    </view>
  </view>
</template>

<script>
import { listPlayHistoryDetail, updatePlayHistory,listPlayHistory } from '@/api/play'

export default {
  data() {
    return {
      historyList: [],
      loading: false,
      pageNum: 1,
      pageSize: 20,
      total: 0,
      hasMore: true
    }
  },
  
  computed: {
    groupedHistory() {
      const groups = {}
      this.historyList.forEach(item => {
        const dateLabel = this.formatDateLabel(item.lastPlayTime)
        if (!groups[dateLabel]) {
          groups[dateLabel] = {
            date: dateLabel,
            items: []
          }
        }
        groups[dateLabel].items.push(item)
      })
      // 按日期排序
      const sortedGroups = Object.keys(groups).sort((a, b) => {
        // 今天和昨天优先
        if (a === '今天') return -1
        if (b === '今天') return 1
        if (a === '昨天') return -1
        if (b === '昨天') return 1
        return 0
      })
      return sortedGroups.map(key => groups[key])
    }
  },
  
  onLoad() {
    this.loadHistory()
  },
  
  onShow() {
    // 页面显示时刷新列表
    this.refreshHistory()
  },
  
  // 下拉刷新
  onPullDownRefresh() {
    this.refreshHistory().finally(() => {
      uni.stopPullDownRefresh()
    })
  },
  
  // 上拉加载更多
  onReachBottom() {
    if (this.hasMore && !this.loading) {
      this.pageNum++
      this.loadHistory()
    }
  },
  
  methods: {
    // 刷新历史记录
    async refreshHistory() {
      this.pageNum = 1
      this.historyList = []
      this.hasMore = true
      return this.loadHistory()
    },
    
    // 加载播放历史
    async loadHistory() {
      if (this.loading) return
      
      try {
        this.loading = true
        const res = await listPlayHistory({
          pageNum: this.pageNum,
          pageSize: this.pageSize
        })
        
        if (res.code === 200) {
          const { rows, total } = res
          
          // 格式化数据 - 适配新的数据结构（PlayHistoryVo包含track对象）
          const formattedList = rows.map(item => {
            // 从PlayHistoryVo中获取track信息（后端已经填充）
            const track = item.track || {}
            
            // 计算播放进度百分比
            let progressPercent = 0
            if (track.durationSec && track.durationSec > 0) {
              progressPercent = Math.min(100, Math.round((item.progressSec * 100) / track.durationSec))
            }
            
            return {
              id: item.id, // 播放历史ID
              trackId: item.trackId,
              title: track.title || item.trackTitle || '未知音频',
              cover: track.coverUrl || item.trackCoverUrl || '/static/images/default-cover.png',
              audioUrl: track.audioUrl || item.audioUrl || '',
              duration: track.durationSec || item.totalDuration || 0,
              seriesId: track.seriesId || item.seriesId,
              seriesTitle: item.seriesTitle || '',
              categoryName: item.categoryName || '',
              progressSec: item.progressSec || 0,
              progressPercent: item.progressPercent || progressPercent,
              isCompleted: item.isCompleted === 'Y',
              lastPlayTime: item.lastPlayTime,
              track: track,
              // 保留原有的series和category信息（如果有）
              series: item.series || {},
              category: item.category || {}
            }
          })
          
          if (this.pageNum === 1) {
            this.historyList = formattedList
          } else {
            this.historyList = [...this.historyList, ...formattedList]
          }
          
          this.total = total
          this.hasMore = this.historyList.length < total
        }
      } catch (error) {
        console.error('加载播放历史失败:', error)
        uni.showToast({
          title: '加载失败',
          icon: 'none'
        })
      } finally {
        this.loading = false
      }
    },
    
    // 格式化时间
    formatTime(timestamp) {
      if (!timestamp) return '--:--'
      const date = new Date(timestamp)
      const hours = date.getHours().toString().padStart(2, '0')
      const minutes = date.getMinutes().toString().padStart(2, '0')
      return `${hours}:${minutes}`
    },
    
    // 格式化日期标签
    formatDateLabel(timestamp) {
      if (!timestamp) return '未知日期'
      
      try {
        const date = new Date(timestamp)
        const today = new Date()
        const yesterday = new Date(today)
        yesterday.setDate(yesterday.getDate() - 1)
        
        // 比较日期字符串（只比较日期部分）
        const dateOnly = date.toDateString()
        const todayOnly = today.toDateString()
        const yesterdayOnly = yesterday.toDateString()
        
        if (dateOnly === todayOnly) {
          return '今天'
        } else if (dateOnly === yesterdayOnly) {
          return '昨天'
        } else {
          // 计算天数差
          const diffTime = Math.abs(today - date)
          const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24))
          
          if (diffDays <= 7) {
            return `${date.getMonth() + 1}月${date.getDate()}日`
          } else if (diffDays <= 30) {
            return `${Math.floor(diffDays / 7)}周前`
          } else {
            return `${date.getFullYear()}-${(date.getMonth() + 1).toString().padStart(2, '0')}-${date.getDate().toString().padStart(2, '0')}`
          }
        }
      } catch (error) {
        console.error('日期格式化错误:', error)
        return '未知日期'
      }
    },
    
    // 播放音频
    playAudio(item) {
      // 跳转到播放页面
      const params = {
        id: item.trackId,
        source: 'history'
      }
      
      // 如果有系列ID，也传递过去
      if (item.seriesId) {
        params.seriesId = item.seriesId
      }
      
      const queryString = Object.keys(params)
        .map(key => `${key}=${params[key]}`)
        .join('&')
      
      uni.navigateTo({
        url: `/pages/player/index?${queryString}`
      })
    },
    
    // 显示更多选项
    showMoreOptions(item) {
      const options = ['继续播放', '从头播放', '删除记录']
      if (item.seriesId) {
        options.push('查看系列')
      }
      
      uni.showActionSheet({
        itemList: options,
        success: (res) => {
          if (res.tapIndex === 0) {
            // 继续播放
            this.playAudio(item)
          } else if (res.tapIndex === 1) {
            // 从头播放
            const params = {
              id: item.trackId,
              source: 'history'
            }
            
            // 如果有系列ID，也传递过去
            if (item.seriesId) {
              params.seriesId = item.seriesId
            }
            
            const queryString = Object.keys(params)
              .map(key => `${key}=${params[key]}`)
              .join('&')
            
            uni.navigateTo({
              url: `/pages/player/index?${queryString}`
            })
          } else if (res.tapIndex === 2) {
            // 删除记录
            this.deleteHistory(item)
          } else if (res.tapIndex === 3 && item.seriesId) {
            // 查看系列
            uni.navigateTo({
              url: `/pages/series/detail?id=${item.seriesId}`
            })
          }
        }
      })
    },
    
    // 删除历史记录
    async deleteHistory(item) {
      try {
        uni.showModal({
          title: '提示',
          content: '确定要删除这条播放记录吗？',
          success: async (res) => {
            if (res.confirm) {
              // 在本地删除
              const index = this.historyList.findIndex(h => h.id === item.id)
              if (index > -1) {
                this.historyList.splice(index, 1)
                this.total--
                
                uni.showToast({
                  title: '已删除',
                  icon: 'success'
                })
              }
              
              // TODO: 当后端提供删除接口后，调用API删除
              // await deletePlayHistory(item.id)
            }
          }
        })
      } catch (error) {
        console.error('删除历史记录失败:', error)
        uni.showToast({
          title: '删除失败',
          icon: 'none'
        })
      }
    },
    
    // 清空历史记录
    clearHistory() {
      uni.showModal({
        title: '提示',
        content: '确定要清空所有播放记录吗？',
        success: async (res) => {
          if (res.confirm) {
            try {
              // 在本地清空
              this.historyList = []
              this.total = 0
              
              // TODO: 当后端提供清空接口后，调用API清空
              // await clearAllPlayHistory()
              
              uni.showToast({
                title: '已清空',
                icon: 'success'
              })
            } catch (error) {
              console.error('清空历史记录失败:', error)
              uni.showToast({
                title: '清空失败',
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
.history-page {
  min-height: 100vh;
  background: #D8E2F0;
}

.content-area {
  padding-top: 20rpx;
  padding-bottom: 120rpx;
}

.history-container {
  padding: 20rpx;
  padding-bottom: 120rpx;
  
  .date-section {
    margin-bottom: 40rpx;
    
    .date-header {
      display: block;
      font-size: 30rpx;
      color: #666666;
      margin-bottom: 20rpx;
      padding-left: 10rpx;
      font-weight: 600;
    }
    
    .history-grid {
      display: flex;
      flex-wrap: wrap;
      gap: 20rpx;
      
      .history-card {
        width: calc(50% - 10rpx);
        background: #FFFFFF;
        border-radius: 20rpx;
        overflow: hidden;
        box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.08);
        transition: all 0.3s ease;
        
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
          
          // 进度条
          .progress-bar {
            position: absolute;
            bottom: 0;
            left: 0;
            right: 0;
            height: 6rpx;
            background: rgba(0, 0, 0, 0.2);
            
            .progress-fill {
              height: 100%;
              background: linear-gradient(90deg, #7C3AED, #A855F7);
              transition: width 0.3s ease;
            }
          }
          
          // 时间标签
          .time-badge {
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
          
          .card-series {
            font-size: 24rpx;
            color: #999999;
            display: block;
            margin-bottom: 8rpx;
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;
          }
          
          .card-meta {
            display: flex;
            align-items: center;
            justify-content: space-between;
            
            .progress-text {
              font-size: 22rpx;
              color: #7C3AED;
              font-weight: 500;
            }
          }
        }
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

.clear-btn {
  position: fixed;
  bottom: 40rpx;
  left: 50%;
  transform: translateX(-50%);
  display: flex;
  align-items: center;
  padding: 20rpx 40rpx;
  background: #FFFFFF;
  border-radius: 50rpx;
  box-shadow: 0 4rpx 16rpx rgba(0, 0, 0, 0.1);
  
  text {
    font-size: 28rpx;
    color: #FF4444;
  }
  
  .clear-text {
    margin-left: 10rpx;
  }
  
  &:active {
    transform: translateX(-50%) scale(0.98);
  }
}
</style>