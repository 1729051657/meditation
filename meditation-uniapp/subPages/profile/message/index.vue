<template>
  <view class="message-page">
    <!-- 消息类型切换 -->
    <view class="tab-bar">
      <view 
        class="tab-item" 
        :class="{ active: activeTab === 'system' }"
        @click="switchTab('system')"
      >
        <text>系统消息</text>
        <view class="badge" v-if="unreadCount.system > 0">{{ unreadCount.system }}</view>
      </view>
      <view 
        class="tab-item" 
        :class="{ active: activeTab === 'task' }"
        @click="switchTab('task')"
      >
        <text>任务通知</text>
        <view class="badge" v-if="unreadCount.task > 0">{{ unreadCount.task }}</view>
      </view>
      <view 
        class="tab-item" 
        :class="{ active: activeTab === 'safety' }"
        @click="switchTab('safety')"
      >
        <text>安全提醒</text>
        <view class="badge" v-if="unreadCount.safety > 0">{{ unreadCount.safety }}</view>
      </view>
    </view>
    
    <!-- 消息列表 -->
    <scroll-view 
      class="message-list" 
      scroll-y 
      @scrolltolower="loadMore"
      :refresher-enabled="true"
      @refresherrefresh="onRefresh"
      :refresher-triggered="refreshing"
    >
      <view 
        class="message-item" 
        v-for="item in messageList" 
        :key="item.id"
        @click="viewMessage(item)"
      >
        <view class="message-header">
          <view class="message-title-wrap">
            <view class="unread-dot" v-if="!item.isRead"></view>
            <text class="message-title">{{ item.title }}</text>
          </view>
          <text class="message-time">{{ item.time }}</text>
        </view>
        <text class="message-content">{{ item.content }}</text>
      </view>
      
      <!-- 空状态 -->
      <view class="empty-state" v-if="!loading && messageList.length === 0">
        <image src="/static/images/empty-message.png" mode="widthFix" />
        <text class="empty-text">暂无消息</text>
      </view>
      
      <!-- 加载更多 -->
      <view class="loadmore" v-if="messageList.length > 0">
        <text v-if="!hasMore">没有更多消息了</text>
        <text v-else>{{ loading ? '加载中...' : '上拉加载更多' }}</text>
      </view>
    </scroll-view>
    
    <!-- 操作栏 -->
    <view class="action-bar">
      <button class="action-btn" @click="markAllRead">全部标记已读</button>
      <button class="action-btn delete" @click="clearMessages">清空消息</button>
    </view>
  </view>
</template>

<script>
import { getMessageList, getMessageStatistics, markMessageRead, markAllMessagesRead, clearMessages } from '@/api/profile'

export default {
  data() {
    return {
      activeTab: 'system',
      loading: false,
      refreshing: false,
      hasMore: true,
      page: 1,
      pageSize: 20,
      unreadCount: {
        system: 2,
        task: 1,
        safety: 0
      },
      messageList: []
    }
  },
  
  onLoad() {
    this.getStatistics()
    this.loadMessages()
  },
  
  methods: {
    switchTab(tab) {
      this.activeTab = tab
      this.page = 1
      this.messageList = []
      this.loadMessages()
    },
    
    async loadMessages() {
      if (this.loading) return
      
      this.loading = true
      try {
        const params = {
          pageNum: this.page,
          pageSize: this.pageSize,
          type: this.activeTab
        }
        
        const res = await getMessageList(params)
        if (res.code === 200) {
          if (this.page === 1) {
            this.messageList = res.rows || []
          } else {
            this.messageList = [...this.messageList, ...(res.rows || [])]
          }
          
          this.hasMore = (res.rows || []).length === this.pageSize
        }
      } catch (error) {
        uni.showToast({
          title: '加载失败',
          icon: 'none'
        })
      } finally {
        this.loading = false
        this.refreshing = false
      }
    },
    
    async getStatistics() {
      try {
        const res = await getMessageStatistics()
        if (res.code === 200) {
          this.unreadCount = res.data || {
            system: 0,
            task: 0,
            safety: 0
          }
        }
      } catch (error) {
        console.error('获取消息统计失败', error)
      }
    },
    
    getMockData() {
      const typeData = {
        system: [
          {
            id: '1',
            title: '系统维护通知',
            content: '系统将于今晚22:00-23:00进行维护升级，期间可能无法正常使用，请提前做好准备。',
            time: '2024-01-15 10:00',
            isRead: false
          },
          {
            id: '2',
            title: '版本更新提醒',
            content: '新版本1.1.0已发布，修复了若干已知问题，优化了用户体验。',
            time: '2024-01-14 15:30',
            isRead: false
          }
        ],
        task: [
          {
            id: '3',
            title: '新任务分配',
            content: '您有一项新的巡检任务待完成，请尽快处理。',
            time: '2024-01-15 08:00',
            isRead: false
          }
        ],
        safety: [
          {
            id: '4',
            title: '安全提醒',
            content: '近期校园内发生多起电动车充电安全事故，请规范充电行为。',
            time: '2024-01-13 09:00',
            isRead: true
          }
        ]
      }
      
      return typeData[this.activeTab] || []
    },
    
    onRefresh() {
      this.refreshing = true
      this.page = 1
      this.loadMessages()
    },
    
    loadMore() {
      if (this.hasMore && !this.loading) {
        this.page++
        this.loadMessages()
      }
    },
    
    async viewMessage(item) {
      if (!item.isRead) {
        try {
          await markMessageRead(item.id)
          item.isRead = true
          this.unreadCount[this.activeTab]--
        } catch (error) {
          console.error('标记已读失败', error)
        }
      }
      
      uni.navigateTo({
        url: `/subPages/profile/messageDetail/index?id=${item.id}`
      })
    },
    
    markAllRead() {
      uni.showModal({
        title: '提示',
        content: '确定将所有消息标记为已读吗？',
        success: async (res) => {
          if (res.confirm) {
            try {
              await markAllMessagesRead(this.activeTab)
              this.messageList.forEach(item => {
                item.isRead = true
              })
              this.unreadCount[this.activeTab] = 0
              uni.showToast({
                title: '操作成功',
                icon: 'success'
              })
            } catch (error) {
              uni.showToast({
                title: '操作失败',
                icon: 'none'
              })
            }
          }
        }
      })
    },
    
    clearMessages() {
      uni.showModal({
        title: '提示',
        content: '确定要清空所有消息吗？此操作不可恢复。',
        success: async (res) => {
          if (res.confirm) {
            try {
              await clearMessages(this.activeTab)
              this.messageList = []
              this.unreadCount[this.activeTab] = 0
              uni.showToast({
                title: '已清空',
                icon: 'success'
              })
            } catch (error) {
              uni.showToast({
                title: '操作失败',
                icon: 'none'
              })
            }
          }
        }
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.message-page {
  display: flex;
  flex-direction: column;
  height: 100vh;
  background-color: #f5f5f5;
  
  .tab-bar {
    display: flex;
    background-color: #fff;
    border-bottom: 1rpx solid #eee;
    
    .tab-item {
      flex: 1;
      position: relative;
      padding: 25rpx 0;
      text-align: center;
      font-size: 30rpx;
      color: #666;
      
      &.active {
        color: #007AFF;
        
        &::after {
          content: '';
          position: absolute;
          bottom: 0;
          left: 50%;
          transform: translateX(-50%);
          width: 60rpx;
          height: 4rpx;
          background-color: #007AFF;
        }
      }
      
      .badge {
        position: absolute;
        top: 15rpx;
        right: calc(50% - 40rpx);
        min-width: 30rpx;
        height: 30rpx;
        line-height: 30rpx;
        padding: 0 8rpx;
        background-color: #FF3B30;
        color: #fff;
        font-size: 20rpx;
        border-radius: 15rpx;
      }
    }
  }
  
  .message-list {
    flex: 1;
    
    .message-item {
      background-color: #fff;
      margin: 20rpx;
      padding: 30rpx;
      border-radius: 16rpx;
      
      .message-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 15rpx;
        
        .message-title-wrap {
          display: flex;
          align-items: center;
          flex: 1;
          
          .unread-dot {
            width: 16rpx;
            height: 16rpx;
            background-color: #FF3B30;
            border-radius: 50%;
            margin-right: 10rpx;
          }
          
          .message-title {
            font-size: 32rpx;
            font-weight: bold;
            color: #333;
          }
        }
        
        .message-time {
          font-size: 24rpx;
          color: #999;
        }
      }
      
      .message-content {
        font-size: 28rpx;
        color: #666;
        line-height: 1.5;
        display: -webkit-box;
        -webkit-box-orient: vertical;
        -webkit-line-clamp: 2;
        overflow: hidden;
      }
    }
    
    .empty-state {
      text-align: center;
      padding: 200rpx 0;
      
      image {
        width: 200rpx;
        margin-bottom: 30rpx;
      }
      
      .empty-text {
        font-size: 28rpx;
        color: #999;
      }
    }
    
    .loadmore {
      text-align: center;
      padding: 30rpx 0;
      font-size: 26rpx;
      color: #999;
    }
  }
  
  .action-bar {
    display: flex;
    gap: 20rpx;
    padding: 20rpx;
    background-color: #fff;
    border-top: 1rpx solid #eee;
    
    .action-btn {
      flex: 1;
      height: 80rpx;
      line-height: 80rpx;
      background-color: #007AFF;
      color: #fff;
      font-size: 30rpx;
      border-radius: 40rpx;
      border: none;
      
      &.delete {
        background-color: #fff;
        color: #FF3B30;
        border: 2rpx solid #FF3B30;
      }
    }
  }
}
</style>