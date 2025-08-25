<template>
  <view class="history-page">
    <!-- 自定义导航栏 -->
    <tn-nav-bar
      :isBack="true"
      :bottomShadow="false"
      backgroundColor="#7C3AED"
      :fixed="true"
    >
      <view class="nav-title">
        <text class="nav-title-text">最近播放</text>
      </view>
    </tn-nav-bar>
    
    <!-- 内容区域 -->
    <view class="content-area">
      <!-- 历史记录列表 -->
      <view class="history-list" v-if="historyList.length > 0">
        <!-- 按日期分组 -->
        <view class="date-group" v-for="(group, dateIndex) in groupedHistory" :key="dateIndex">
          <text class="date-title">{{ group.date }}</text>
          
          <view 
            class="history-item" 
            v-for="(item, index) in group.items" 
            :key="item.id"
            @click="playAudio(item)"
          >
            <image :src="item.cover" mode="aspectFill" class="item-cover"></image>
            <view class="item-info">
              <text class="item-title">{{ item.title }}</text>
              <text class="item-series" v-if="item.seriesTitle">{{ item.seriesTitle }}</text>
              <view class="item-meta">
                <text class="meta-time">{{ formatTime(item.lastPlayTime) }}</text>
                <text class="meta-progress">已播放{{ item.progressPercent }}%</text>
              </view>
            </view>
            <view class="item-action" @click.stop="showMoreOptions(item)">
              <text class="tn-icon-more-vertical"></text>
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
import { listPlayHistoryDetail, updatePlayHistory } from '@/api/play'

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
        const res = await listPlayHistoryDetail({
          pageNum: this.pageNum,
          pageSize: this.pageSize
        })
        
        if (res.code === 200) {
          const { rows, total } = res
          
          // 格式化数据 - 根据新的数据结构
          const formattedList = rows.map(item => {
            // 获取音频信息
            const track = item.track || {}
            const series = item.series || {}
            const category = item.category || {}
            
            return {
              id: item.id, // 播放历史ID
              trackId: item.trackId,
              title: track.title || '未知音频',
              cover: track.cover || '/static/images/default-cover.png',
              audioUrl: track.audio || '',
              duration: track.durationSec || 0,
              seriesId: track.seriesId,
              seriesTitle: series.title || '',
              categoryName: category.name || '',
              progressSec: item.progressSec || 0,
              progressPercent: item.progressPercent || 0,
              isCompleted: item.isCompleted === '1',
              lastPlayTime: item.lastPlayTime,
              track: track,
              series: series,
              category: category
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
      // 跳转到播放页面，传递播放进度信息
      const params = {
        id: item.trackId,
        title: encodeURIComponent(item.title),
        progress: item.progressSec,
        totalDuration: item.duration
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
              title: encodeURIComponent(item.title),
              progress: 0,
              totalDuration: item.duration
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
  padding-bottom: 120rpx;
}

.history-list {
  padding: 20rpx;
  
  .date-group {
    margin-bottom: 40rpx;
    
    .date-title {
      display: block;
      font-size: 28rpx;
      color: #999999;
      margin-bottom: 20rpx;
      padding-left: 10rpx;
      font-weight: 500;
    }
    
    .history-item {
      display: flex;
      align-items: center;
      background: #FFFFFF;
      border-radius: 20rpx;
      padding: 20rpx;
      margin-bottom: 16rpx;
      box-shadow: 0 2rpx 8rpx rgba(0, 0, 0, 0.04);
      transition: all 0.3s ease;
      
      &:active {
        transform: scale(0.98);
        box-shadow: 0 1rpx 4rpx rgba(0, 0, 0, 0.08);
      }
      
      .item-cover {
        width: 100rpx;
        height: 100rpx;
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
        
        .item-series {
          font-size: 26rpx;
          color: #999999;
          margin-bottom: 8rpx;
          overflow: hidden;
          text-overflow: ellipsis;
          white-space: nowrap;
        }
        
        .item-meta {
          display: flex;
          align-items: center;
          
          .meta-time {
            font-size: 24rpx;
            color: #BBBBBB;
            margin-right: 20rpx;
          }
          
          .meta-progress {
            font-size: 24rpx;
            color: #7C3AED;
            font-weight: 500;
          }
        }
      }
      
      .item-action {
        padding: 10rpx;
        
        text {
          font-size: 36rpx;
          color: #CCCCCC;
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