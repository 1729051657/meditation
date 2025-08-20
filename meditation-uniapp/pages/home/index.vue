<template>
  <view class="home-page">
    <!-- 背景图 -->
    <image src="/static/home/background@3x.png" class="background-image" mode="widthFix"></image>
    
    <!-- 顶部区域 -->
    <view class="header-section">
      <!-- 问候语 -->
      <view class="greeting">上午好</view>
      
      <!-- 搜索框 -->
      <view class="search-box" @click="goToSearch">
        <image src="/static/home/search@3x.png" class="search-icon" mode="aspectFit"></image>
        <text class="search-placeholder">搜索</text>
      </view>
    </view>

    <!-- 功能按钮区域 -->
    <view class="feature-section">
      <view class="feature-item" @click="goToCategory('relax')">
        <view class="feature-icon-wrapper">
          <image src="/static/home/relax-stress@2x.png" class="feature-icon" mode="aspectFit"></image>
        </view>
        <text class="feature-text">放松减压</text>
      </view>
      
      <view class="feature-item" @click="goToCategory('sleep')">
        <view class="feature-icon-wrapper">
          <image src="/static/home/improve-sleep@2x.png" class="feature-icon" mode="aspectFit"></image>
        </view>
        <text class="feature-text">改善睡眠</text>
      </view>
      
      <view class="feature-item" @click="goToCategory('focus')">
        <view class="feature-icon-wrapper">
          <image src="/static/home/improve-focus@2x.png" class="feature-icon" mode="aspectFit"></image>
        </view>
        <text class="feature-text">提升专注</text>
      </view>
      
      <view class="feature-item" @click="goToCategory('emotion')">
        <view class="feature-icon-wrapper">
          <image src="/static/home/emotion-regulation@2x.png" class="feature-icon" mode="aspectFit"></image>
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
              <image src="/static/home/play@3x.png" class="play-icon" mode="aspectFit"></image>
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
      defaultKnowledgeCover: '/static/images/default-knowledge.png',
      // 模拟数据映射
      mockDataMap: {
        meditation: [
          { id: 101, title: '初学者呼吸冥想', duration: 10, description: '适合初学者的基础呼吸练习' },
          { id: 102, title: '深度放松冥想', duration: 20, description: '帮助深度放松身心的进阶练习' }
        ],
        series: [
          { id: 201, title: '7天入门冥想课程', duration: 15, cover: '/static/images/series1.jpg' },
          { id: 203, title: '21天冥想习惯养成', duration: 20, cover: '/static/images/series2.jpg' }
        ],
        track: [
          { id: 202, title: '雨声冥想音乐', duration: 30, cover: '/static/images/track1.jpg' }
        ],
        article: [
          { id: 301, title: '冥想的科学原理', description: '了解冥想如何改变大脑结构' },
          { id: 302, title: '如何开始你的冥想之旅', description: '新手必读的冥想入门指南' },
          { id: 303, title: '冥想与睡眠质量', description: '探索冥想对睡眠的积极影响' }
        ]
      }
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
          listSlots({ status: 0, pageSize: 10 }),
          listSlotItems({ status: 0, pageSize: 20 })
        ])
        
        // 处理推荐位数据
        if (slotsRes.code === 200 && slotsRes.rows) {
          const slots = slotsRes.rows
          
          // 处理冥想练习推荐位 - 匹配code或name包含meditation
          this.meditationSlots = slots.filter(item => 
            item.code?.includes('meditation') || 
            item.name?.includes('冥想') ||
            item.page === 'home'
          ).slice(0, 2).map(slot => {
            // 如果是模拟数据，添加额外信息
            const mockData = this.mockDataMap.meditation.find(m => m.id === 101 || m.id === 102)
            return {
              ...slot,
              title: slot.name || mockData?.title,
              duration: mockData?.duration || 10,
              description: slot.remark || mockData?.description,
              slotType: 'meditation',
              position: 'meditation'
            }
          })
        }
        
        // 处理推荐内容数据
        if (itemsRes.code === 200 && itemsRes.rows) {
          const items = itemsRes.rows
          
          // 处理推荐内容 - 根据contentType分类并添加详细信息
          const recommendContentTypes = ['series', 'track', 'meditation']
          const knowledgeContentTypes = ['article', 'knowledge']
          
          this.recommendItems = items.filter(item => 
            recommendContentTypes.includes(item.contentType) || !item.contentType
          ).slice(0, 3).map(item => {
            // 根据contentType和contentId查找模拟数据
            const mockList = this.mockDataMap[item.contentType] || []
            const mockData = mockList.find(m => m.id === item.contentId)
            return {
              ...item,
              title: mockData?.title || `推荐内容 ${item.contentId}`,
              duration: mockData?.duration || 15,
              cover: mockData?.cover || this.defaultCover,
              itemType: 'recommend',
              targetId: item.contentId,
              targetType: item.contentType
            }
          })
          
          this.knowledgeItems = items.filter(item => 
            knowledgeContentTypes.includes(item.contentType)
          ).slice(0, 3).map(item => {
            // 根据contentType和contentId查找模拟数据
            const mockList = this.mockDataMap[item.contentType] || []
            const mockData = mockList.find(m => m.id === item.contentId)
            return {
              ...item,
              title: mockData?.title || `知识文章 ${item.contentId}`,
              description: mockData?.description || '探索冥想的奥秘',
              cover: mockData?.cover || this.defaultKnowledgeCover,
              itemType: 'knowledge',
              targetId: item.contentId,
              targetType: item.contentType
            }
          })
        }
        
        // 如果数据为空，使用纯模拟数据
        if (this.meditationSlots.length === 0) {
          this.meditationSlots = [
            { id: 1, title: '初学者呼吸冥想', duration: 10, slotType: 'meditation' },
            { id: 2, title: '深度放松冥想', duration: 20, slotType: 'meditation' }
          ]
        }
        
        if (this.recommendItems.length === 0) {
          this.recommendItems = [
            { id: 1, title: '7天入门冥想课程', duration: 15, cover: this.defaultCover, targetId: 201, targetType: 'series' },
            { id: 2, title: '雨声冥想音乐', duration: 30, cover: this.defaultCover, targetId: 202, targetType: 'track' },
            { id: 3, title: '21天冥想习惯养成', duration: 20, cover: this.defaultCover, targetId: 203, targetType: 'series' }
          ]
        }
        
        if (this.knowledgeItems.length === 0) {
          this.knowledgeItems = [
            { id: 1, title: '冥想的科学原理', description: '了解冥想如何改变大脑结构', cover: this.defaultKnowledgeCover },
            { id: 2, title: '如何开始你的冥想之旅', description: '新手必读的冥想入门指南', cover: this.defaultKnowledgeCover },
            { id: 3, title: '冥想与睡眠质量', description: '探索冥想对睡眠的积极影响', cover: this.defaultKnowledgeCover }
          ]
        }
      } catch (error) {
        console.error('加载首页数据失败:', error)
        // 加载失败时使用默认模拟数据
        this.meditationSlots = [
          { id: 1, title: '初学者呼吸冥想', duration: 10, slotType: 'meditation' },
          { id: 2, title: '深度放松冥想', duration: 20, slotType: 'meditation' }
        ]
        this.recommendItems = [
          { id: 1, title: '7天入门冥想课程', duration: 15, cover: this.defaultCover },
          { id: 2, title: '雨声冥想音乐', duration: 30, cover: this.defaultCover },
          { id: 3, title: '21天冥想习惯养成', duration: 20, cover: this.defaultCover }
        ]
        this.knowledgeItems = [
          { id: 1, title: '冥想的科学原理', description: '了解冥想如何改变大脑结构', cover: this.defaultKnowledgeCover },
          { id: 2, title: '如何开始你的冥想之旅', description: '新手必读的冥想入门指南', cover: this.defaultKnowledgeCover },
          { id: 3, title: '冥想与睡眠质量', description: '探索冥想对睡眠的积极影响', cover: this.defaultKnowledgeCover }
        ]
        uni.showToast({
          title: '已加载默认数据',
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
  position: relative;
}

/* 背景图 */
.background-image {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  z-index: 0;
  opacity: 0.6;
}

/* 顶部区域 */
.header-section {
  padding: 40rpx 30rpx 30rpx;
  padding-top: calc(var(--status-bar-height) + 40rpx);
  position: relative;
  z-index: 1;
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
  
  .search-icon {
    width: 36rpx;
    height: 36rpx;
  }
  
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
  position: relative;
  z-index: 1;
  
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
  position: relative;
  z-index: 1;
  
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
  position: relative;
  z-index: 1;
  
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
  position: relative;
  z-index: 1;
  
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