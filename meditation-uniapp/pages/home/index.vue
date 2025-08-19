<template>
  <view class="home-page">
    <!-- 顶部区域 -->
    <view class="header-section">
      <!-- 问候语 -->
      <view class="greeting">上午好</view>
      
      <!-- 搜索框 -->
      <view class="search-box" @click="goToSearch">
        <uni-icons type="search" size="20" color="#999"></uni-icons>
        <text class="search-placeholder">搜索</text>
      </view>
    </view>

    <!-- 功能按钮区域 -->
    <view class="feature-section">
      <view class="feature-item" @click="goToCategory('relax')">
        <view class="feature-icon-wrapper">
          <image src="/static/home/放松减压@2x.png" class="feature-icon" mode="aspectFit"></image>
        </view>
        <text class="feature-text">放松减压</text>
      </view>
      
      <view class="feature-item" @click="goToCategory('sleep')">
        <view class="feature-icon-wrapper">
          <image src="/static/home/改善睡眠@2x.png" class="feature-icon" mode="aspectFit"></image>
        </view>
        <text class="feature-text">改善睡眠</text>
      </view>
      
      <view class="feature-item" @click="goToCategory('focus')">
        <view class="feature-icon-wrapper">
          <image src="/static/home/提升专注@2x.png" class="feature-icon" mode="aspectFit"></image>
        </view>
        <text class="feature-text">提升专注</text>
      </view>
      
      <view class="feature-item" @click="goToCategory('emotion')">
        <view class="feature-icon-wrapper">
          <image src="/static/home/情绪调节@2x.png" class="feature-icon" mode="aspectFit"></image>
        </view>
        <text class="feature-text">情绪调节</text>
      </view>
    </view>

    <!-- 冥想练习区域 -->
    <view class="meditation-section" v-if="meditationSlots.length > 0">
      <view class="section-header">
        <text class="section-title">冥想练习</text>
        <view class="section-more" @click="goToMore('meditation')">
          <uni-icons type="right" size="16" color="#999"></uni-icons>
        </view>
      </view>
      
      <scroll-view class="meditation-cards" scroll-x>
        <view class="meditation-card-list">
          <view 
            v-for="(item, index) in meditationSlots" 
            :key="item.id"
            class="meditation-card"
            :class="index === 0 ? 'card-basic' : 'card-advanced'"
            @click="goToMeditation(item)"
          >
            <view class="card-content">
              <text class="card-title">{{ item.title }}</text>
              <view class="card-duration">
                <uni-icons type="time" size="14" color="#fff"></uni-icons>
                <text class="duration-text">{{ item.duration || '10' }}分钟</text>
              </view>
            </view>
            <view class="card-play">
              <image src="/static/home/播放@3x.png" class="play-icon" mode="aspectFit"></image>
            </view>
          </view>
        </view>
      </scroll-view>
    </view>

    <!-- 冥想推荐区域 -->
    <view class="recommend-section" v-if="recommendItems.length > 0">
      <view class="section-header">
        <text class="section-title">冥想推荐</text>
      </view>
      
      <view class="recommend-list">
        <view 
          v-for="item in recommendItems.slice(0, 3)" 
          :key="item.id"
          class="recommend-item"
          @click="goToRecommend(item)"
        >
          <image :src="item.cover || defaultCover" class="recommend-image" mode="aspectFill"></image>
          <view class="recommend-info">
            <text class="recommend-title">{{ item.title }}</text>
            <text class="recommend-duration">{{ item.duration || '10' }}分钟</text>
          </view>
        </view>
      </view>
    </view>

    <!-- 冥想知识区域 -->
    <view class="knowledge-section" v-if="knowledgeItems.length > 0">
      <view class="section-header">
        <text class="section-title">冥想知识</text>
        <view class="section-more" @click="goToMore('knowledge')">
          <uni-icons type="right" size="16" color="#999"></uni-icons>
        </view>
      </view>
      
      <view class="knowledge-list">
        <view 
          v-for="item in knowledgeItems.slice(0, 3)" 
          :key="item.id"
          class="knowledge-item"
          @click="goToKnowledge(item)"
        >
          <image :src="item.cover || defaultKnowledgeCover" class="knowledge-image" mode="aspectFill"></image>
          <view class="knowledge-content">
            <text class="knowledge-title">{{ item.title }}</text>
            <text class="knowledge-desc" v-if="item.description">{{ item.description }}</text>
          </view>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
import { listSlots, listSlotItems } from '@/api/recommend'

export default {
  data() {
    return {
      meditationSlots: [], // 冥想练习推荐位
      recommendItems: [], // 推荐内容
      knowledgeItems: [], // 知识内容
      defaultCover: '/static/images/default-cover.png',
      defaultKnowledgeCover: '/static/images/default-knowledge.png'
    }
  },
  
  onLoad() {
    this.loadHomeData()
  },
  
  onPullDownRefresh() {
    this.loadHomeData().finally(() => {
      uni.stopPullDownRefresh()
    })
  },
  
  methods: {
    // 加载首页数据
    async loadHomeData() {
      try {
        // 并行加载推荐位和推荐内容
        const [slotsRes, itemsRes] = await Promise.all([
          listSlots({ status: 1, pageSize: 10 }),
          listSlotItems({ status: 1, pageSize: 20 })
        ])
        
        // 处理推荐位数据
        if (slotsRes.code === 200 && slotsRes.rows) {
          // 根据slotType分类处理
          const slots = slotsRes.rows
          
          // 冥想练习 (type: meditation)
          this.meditationSlots = slots.filter(item => 
            item.slotType === 'meditation' || item.position === 'meditation'
          ).slice(0, 2) // 只取前两个
          
          // 如果没有明确的分类，就按位置分配
          if (this.meditationSlots.length === 0) {
            this.meditationSlots = slots.slice(0, 2)
          }
        }
        
        // 处理推荐内容数据
        if (itemsRes.code === 200 && itemsRes.rows) {
          const items = itemsRes.rows
          
          // 根据itemType分类
          this.recommendItems = items.filter(item => 
            item.itemType === 'recommend' || item.itemType === 'meditation' || !item.itemType
          ).slice(0, 3)
          
          this.knowledgeItems = items.filter(item => 
            item.itemType === 'knowledge' || item.itemType === 'article'
          ).slice(0, 3)
          
          // 如果没有明确分类，按顺序分配
          if (this.recommendItems.length === 0) {
            this.recommendItems = items.slice(0, 3)
          }
          if (this.knowledgeItems.length === 0) {
            this.knowledgeItems = items.slice(3, 6)
          }
        }
      } catch (error) {
        console.error('加载首页数据失败:', error)
        uni.showToast({
          title: '加载失败',
          icon: 'none'
        })
      }
    },
    
    // 跳转到搜索页
    goToSearch() {
      uni.navigateTo({
        url: '/pages/search/index'
      })
    },
    
    // 跳转到分类
    goToCategory(type) {
      uni.navigateTo({
        url: `/pages/category/index?type=${type}`
      })
    },
    
    // 跳转到冥想详情
    goToMeditation(item) {
      // 根据实际数据结构跳转
      if (item.targetId) {
        uni.navigateTo({
          url: `/pages/player/index?id=${item.targetId}&type=${item.targetType || 'meditation'}`
        })
      } else if (item.link) {
        uni.navigateTo({
          url: item.link
        })
      }
    },
    
    // 跳转到推荐详情
    goToRecommend(item) {
      if (item.targetId) {
        uni.navigateTo({
          url: `/pages/player/index?id=${item.targetId}&type=${item.targetType || 'meditation'}`
        })
      } else if (item.link) {
        uni.navigateTo({
          url: item.link
        })
      }
    },
    
    // 跳转到知识详情
    goToKnowledge(item) {
      if (item.targetId) {
        uni.navigateTo({
          url: `/pages/article/detail?id=${item.targetId}`
        })
      } else if (item.link) {
        uni.navigateTo({
          url: item.link
        })
      }
    },
    
    // 查看更多
    goToMore(type) {
      if (type === 'meditation') {
        uni.navigateTo({
          url: '/pages/series/index'
        })
      } else if (type === 'knowledge') {
        uni.navigateTo({
          url: '/pages/article/index'
        })
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.home-page {
  min-height: 100vh;
  background: linear-gradient(180deg, #E8F4FF 0%, #F5F7FA 100%);
  padding-bottom: 100rpx;
}

/* 顶部区域 */
.header-section {
  padding: 40rpx 30rpx 30rpx;
  padding-top: calc(var(--status-bar-height) + 40rpx);
}

.greeting {
  font-size: 36rpx;
  font-weight: 500;
  color: #333;
  margin-bottom: 20rpx;
}

.search-box {
  display: flex;
  align-items: center;
  background: #fff;
  border-radius: 36rpx;
  padding: 16rpx 24rpx;
  box-shadow: 0 2rpx 8rpx rgba(0, 0, 0, 0.06);
  
  .search-placeholder {
    margin-left: 12rpx;
    color: #999;
    font-size: 28rpx;
  }
}

/* 功能按钮区域 */
.feature-section {
  display: flex;
  justify-content: space-around;
  padding: 20rpx 30rpx 40rpx;
  
  .feature-item {
    display: flex;
    flex-direction: column;
    align-items: center;
    
    .feature-icon-wrapper {
      width: 120rpx;
      height: 120rpx;
      background: #fff;
      border-radius: 24rpx;
      display: flex;
      align-items: center;
      justify-content: center;
      box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.08);
      margin-bottom: 16rpx;
    }
    
    .feature-icon {
      width: 60rpx;
      height: 60rpx;
    }
    
    .feature-text {
      font-size: 26rpx;
      color: #333;
    }
  }
}

/* 通用区块样式 */
.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 30rpx;
  margin-bottom: 24rpx;
  
  .section-title {
    font-size: 32rpx;
    font-weight: 500;
    color: #333;
  }
  
  .section-more {
    display: flex;
    align-items: center;
  }
}

/* 冥想练习区域 */
.meditation-section {
  margin-bottom: 40rpx;
  
  .meditation-cards {
    padding-left: 30rpx;
    
    .meditation-card-list {
      display: flex;
      gap: 20rpx;
      padding-right: 30rpx;
      
      .meditation-card {
        flex-shrink: 0;
        width: 340rpx;
        height: 200rpx;
        border-radius: 20rpx;
        padding: 30rpx;
        display: flex;
        justify-content: space-between;
        align-items: flex-end;
        position: relative;
        overflow: hidden;
        
        &.card-basic {
          background: linear-gradient(135deg, #A8D5E8 0%, #6BA3D0 100%);
        }
        
        &.card-advanced {
          background: linear-gradient(135deg, #4A6FA5 0%, #2C4E7E 100%);
        }
        
        .card-content {
          flex: 1;
          
          .card-title {
            font-size: 32rpx;
            font-weight: 500;
            color: #fff;
            display: block;
            margin-bottom: 16rpx;
          }
          
          .card-duration {
            display: flex;
            align-items: center;
            gap: 8rpx;
            
            .duration-text {
              font-size: 24rpx;
              color: rgba(255, 255, 255, 0.9);
            }
          }
        }
        
        .card-play {
          width: 60rpx;
          height: 60rpx;
          background: rgba(255, 255, 255, 0.3);
          border-radius: 50%;
          display: flex;
          align-items: center;
          justify-content: center;
          
          .play-icon {
            width: 24rpx;
            height: 24rpx;
          }
        }
      }
    }
  }
}

/* 冥想推荐区域 */
.recommend-section {
  margin-bottom: 40rpx;
  
  .recommend-list {
    padding: 0 30rpx;
    display: flex;
    flex-direction: column;
    gap: 20rpx;
    
    .recommend-item {
      display: flex;
      background: #fff;
      border-radius: 16rpx;
      overflow: hidden;
      box-shadow: 0 2rpx 8rpx rgba(0, 0, 0, 0.06);
      
      .recommend-image {
        width: 200rpx;
        height: 150rpx;
        flex-shrink: 0;
      }
      
      .recommend-info {
        flex: 1;
        padding: 24rpx;
        display: flex;
        flex-direction: column;
        justify-content: center;
        
        .recommend-title {
          font-size: 30rpx;
          color: #333;
          margin-bottom: 12rpx;
          font-weight: 500;
        }
        
        .recommend-duration {
          font-size: 24rpx;
          color: #999;
        }
      }
    }
  }
}

/* 冥想知识区域 */
.knowledge-section {
  margin-bottom: 40rpx;
  
  .knowledge-list {
    padding: 0 30rpx;
    display: flex;
    flex-direction: column;
    gap: 20rpx;
    
    .knowledge-item {
      display: flex;
      background: #fff;
      border-radius: 16rpx;
      padding: 20rpx;
      box-shadow: 0 2rpx 8rpx rgba(0, 0, 0, 0.06);
      
      .knowledge-image {
        width: 120rpx;
        height: 120rpx;
        border-radius: 12rpx;
        flex-shrink: 0;
        margin-right: 20rpx;
      }
      
      .knowledge-content {
        flex: 1;
        display: flex;
        flex-direction: column;
        justify-content: center;
        
        .knowledge-title {
          font-size: 28rpx;
          color: #333;
          font-weight: 500;
          margin-bottom: 12rpx;
          display: -webkit-box;
          -webkit-line-clamp: 2;
          -webkit-box-orient: vertical;
          overflow: hidden;
          text-overflow: ellipsis;
        }
        
        .knowledge-desc {
          font-size: 24rpx;
          color: #999;
          display: -webkit-box;
          -webkit-line-clamp: 2;
          -webkit-box-orient: vertical;
          overflow: hidden;
          text-overflow: ellipsis;
        }
      }
    }
  }
}
</style>