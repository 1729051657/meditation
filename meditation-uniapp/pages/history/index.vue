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
            :key="index"
            @click="playAudio(item)"
          >
            <image :src="item.cover" mode="aspectFill" class="item-cover"></image>
            <view class="item-info">
              <text class="item-title">{{ item.title }}</text>
              <text class="item-author">{{ item.author }}</text>
              <view class="item-meta">
                <text class="meta-time">{{ item.playTime }}</text>
                <text class="meta-progress">已播放{{ item.progress }}%</text>
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
export default {
  data() {
    return {
      historyList: [
        {
          id: 1,
          title: '深度睡眠冥想',
          author: '冥想大师',
          playTime: '22:30',
          progress: 75,
          cover: '/static/home/meditation1.jpg',
          date: '2024-01-15'
        },
        {
          id: 2,
          title: '清晨正念练习',
          author: '静心导师',
          playTime: '08:15',
          progress: 100,
          cover: '/static/home/meditation2.jpg',
          date: '2024-01-15'
        },
        {
          id: 3,
          title: '压力释放冥想',
          author: '身心疗愈师',
          playTime: '19:45',
          progress: 50,
          cover: '/static/home/meditation3.jpg',
          date: '2024-01-14'
        },
        {
          id: 4,
          title: '专注力训练',
          author: '专注教练',
          playTime: '14:20',
          progress: 30,
          cover: '/static/home/meditation4.jpg',
          date: '2024-01-14'
        },
        {
          id: 5,
          title: '呼吸放松练习',
          author: '瑜伽导师',
          playTime: '10:00',
          progress: 100,
          cover: '/static/home/meditation5.jpg',
          date: '2024-01-13'
        }
      ]
    }
  },
  
  computed: {
    groupedHistory() {
      const groups = {}
      this.historyList.forEach(item => {
        const dateLabel = this.formatDateLabel(item.date)
        if (!groups[dateLabel]) {
          groups[dateLabel] = {
            date: dateLabel,
            items: []
          }
        }
        groups[dateLabel].items.push(item)
      })
      return Object.values(groups)
    }
  },
  
  onLoad() {
    this.loadHistory()
  },
  
  methods: {
    loadHistory() {
      // 从本地存储或API加载历史记录
      const cached = uni.getStorageSync('historyList')
      if (cached) {
        this.historyList = cached
      }
    },
    
    formatDateLabel(dateStr) {
      const date = new Date(dateStr)
      const today = new Date()
      const yesterday = new Date(today)
      yesterday.setDate(yesterday.getDate() - 1)
      
      if (date.toDateString() === today.toDateString()) {
        return '今天'
      } else if (date.toDateString() === yesterday.toDateString()) {
        return '昨天'
      } else {
        return `${date.getMonth() + 1}月${date.getDate()}日`
      }
    },
    
    playAudio(item) {
      // 跳转到播放页面，从上次进度继续播放
      uni.navigateTo({
        url: `/pages/player/index?id=${item.id}&title=${item.title}&progress=${item.progress}`
      })
    },
    
    showMoreOptions(item) {
      uni.showActionSheet({
        itemList: ['继续播放', '从头播放', '删除记录'],
        success: (res) => {
          if (res.tapIndex === 0) {
            this.playAudio(item)
          } else if (res.tapIndex === 1) {
            uni.navigateTo({
              url: `/pages/player/index?id=${item.id}&title=${item.title}&progress=0`
            })
          } else if (res.tapIndex === 2) {
            this.deleteHistory(item)
          }
        }
      })
    },
    
    deleteHistory(item) {
      const index = this.historyList.findIndex(h => h.id === item.id)
      if (index > -1) {
        this.historyList.splice(index, 1)
        uni.setStorageSync('historyList', this.historyList)
        uni.showToast({
          title: '已删除',
          icon: 'success'
        })
      }
    },
    
    clearHistory() {
      uni.showModal({
        title: '提示',
        content: '确定要清空所有播放记录吗？',
        success: (res) => {
          if (res.confirm) {
            this.historyList = []
            uni.removeStorageSync('historyList')
            uni.showToast({
              title: '已清空',
              icon: 'success'
            })
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
        
        .item-author {
          font-size: 26rpx;
          color: #999999;
          margin-bottom: 12rpx;
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