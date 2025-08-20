<template>
  <view class="home-page">
    <!-- 背景图 -->
    <image src="/static/home/background@3x.png" class="background-image" mode="widthFix"></image>
    
    <!-- 顶部区域 -->
    <view class="header-section">
      <!-- 问候语 -->
      <view class="greeting">上午好</view>
      
      <!-- 搜索图标 -->
      <view class="search-icon" @click="goToSearch">
        <tn-icon name="search" size="20" color="#333"></tn-icon>
      </view>
    </view>

    <!-- 功能按钮区域 -->
    <view class="feature-section">
      <view class="feature-item" @click="goToCategory('relax')">
        <view class="feature-icon-wrapper">
          <image src="/static/home/relax-stress@2x.png" class="feature-icon" mode="aspectFit"></image>
          <text class="feature-text">放松减压</text>
        </view>
      </view>
      
      <view class="feature-item" @click="goToCategory('sleep')">
        <view class="feature-icon-wrapper">
          <image src="/static/home/improve-sleep@2x.png" class="feature-icon" mode="aspectFit"></image>
          <text class="feature-text">改善睡眠</text>
        </view>
      </view>
      
      <view class="feature-item" @click="goToCategory('focus')">
        <view class="feature-icon-wrapper">
          <image src="/static/home/improve-focus@2x.png" class="feature-icon" mode="aspectFit"></image>
          <text class="feature-text">提升专注</text>
        </view>
      </view>
      
      <view class="feature-item" @click="goToCategory('emotion')">
        <view class="feature-icon-wrapper">
          <image src="/static/home/emotion-regulation@2x.png" class="feature-icon" mode="aspectFit"></image>
          <text class="feature-text">情绪调节</text>
        </view>
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
      
      <scroll-view class="meditation-cards" scroll-x :show-scrollbar="false" enhanced>
        <view class="meditation-card-list">
          <view 
            v-for="(item, index) in meditationSlots" 
            :key="item.id"
            class="meditation-card"
            :class="index === 0 ? 'card-basic' : 'card-advanced'"
            @click="goToMeditation(item)"
          >
            <view class="card-content">
              <text class="card-title">{{ item.title||"123213" }}</text>
            </view>
            <view class="card-bottom-bar">
              <view class="card-duration">
                <uni-icons type="time" size="14" color="#fff"></uni-icons>
                <text class="duration-text">{{ item.duration || '10' }}分钟</text>
              </view>
              <view class="card-play">
                <image src="/static/home/play@3x.png" class="play-icon" mode="aspectFit"></image>
              </view>
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
      
      <scroll-view class="recommend-cards" scroll-x :show-scrollbar="false" enhanced>
        <view class="recommend-card-list">
          <view 
            v-for="item in recommendItems" 
            :key="item.id"
            class="recommend-card-wrapper"
            @click="goToRecommend(item)"
          >
            <view class="recommend-card">
              <image :src="item.cover || defaultCover" class="recommend-image" mode="aspectFill"></image>
              <view class="duration-tag">10分钟</view>
            </view>
            <text class="recommend-title">{{ item.title || '清理思绪' }}</text>
          </view>
        </view>
      </scroll-view>
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
          listSlots({ status: '0', pageSize: 10 }),
          listSlotItems({ status: '0', pageSize: 20 })
        ])
        
        // 处理推荐位数据
        if (slotsRes.code === 200 && slotsRes.rows) {
          const slots = slotsRes.rows
          
          // 冥想练习推荐位 - 根据code匹配
          this.meditationSlots = slots.filter(item => 
            item.code?.includes('meditation') || 
            item.name?.includes('冥想练习')
          ).slice(0, 2).map(slot => ({
            ...slot,
            title: slot.name,
            duration: 10,
            slotType: 'meditation'
          }))
        }
        
        // 处理推荐内容数据
        if (itemsRes.code === 200 && itemsRes.rows) {
          const items = itemsRes.rows
          
          // 根据slotId分组处理
          const slotGroups = {}
          items.forEach(item => {
            if (!slotGroups[item.slotId]) {
              slotGroups[item.slotId] = []
            }
            slotGroups[item.slotId].push(item)
          })
          
          // 处理推荐内容（每日推荐、热门系列等）
          const recommendSlotIds = [3, 5] // 每日推荐和热门系列的slot_id
          this.recommendItems = []
          recommendSlotIds.forEach(slotId => {
            if (slotGroups[slotId]) {
              this.recommendItems.push(...slotGroups[slotId])
            }
          })
          this.recommendItems = this.recommendItems.slice(0, 3).map(item => ({
            ...item,
            title: `内容${item.contentId}`, // 实际应该关联查询获取标题
            duration: 15,
            cover: this.defaultCover,
            targetId: item.contentId,
            targetType: item.contentType
          }))
          
          // 处理知识文章
          const knowledgeSlotId = 4 // 冥想知识的slot_id
          if (slotGroups[knowledgeSlotId]) {
            this.knowledgeItems = slotGroups[knowledgeSlotId].slice(0, 3).map(item => ({
              ...item,
              title: `文章${item.contentId}`, // 实际应该关联查询获取标题
              description: '探索冥想的奥秘',
              cover: this.defaultKnowledgeCover,
              targetId: item.contentId,
              targetType: item.contentType
            }))
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
  position: relative;
}

/* 背景图 */
.background-image {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  z-index: 0;
  opacity: 1;
}

/* 公共导航栏 */
.nav-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 30rpx;
}

.nav-title {
  font-size: 36rpx;
  font-weight: 500;
  color: #fff;
}

.nav-search {
  width: 60rpx;
  height: 60rpx;
  background: rgba(255, 255, 255, 0.2);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
}

/* 顶部区域 */
.header-section {
  padding: 40rpx 30rpx 30rpx;
  padding-top: calc(var(--status-bar-height) + 60rpx);
  position: relative;
  z-index: 1;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.greeting {
  font-size: 48rpx;
  font-weight: 600;
  color: #333;
}

.search-icon {
  width: 60rpx;
  height: 60rpx;
  background: rgba(255, 255, 255, 0.8);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 2rpx 8rpx rgba(0, 0, 0, 0.1);
}

/* 功能按钮区域 */
.feature-section {
  display: flex;
  justify-content: space-around;
  padding: 40rpx 30rpx 60rpx;
  position: relative;
  z-index: 1;
  
  .feature-item {
    display: flex;
    flex-direction: column;
    align-items: center;
    
    .feature-icon-wrapper {
      width: 150rpx;
      height: 180rpx;
      background: #fff;
      border-radius: 20rpx;
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;
      box-shadow: 0 4rpx 16rpx rgba(0, 0, 0, 0.1);
      transition: all 0.3s ease;
      
      &:active {
        transform: scale(0.95);
        box-shadow: 0 2rpx 8rpx rgba(0, 0, 0, 0.15);
      }
      
      .feature-icon {
        width: 88rpx;
        height: 88rpx;
        margin-bottom: 10rpx;
      }
      
      .feature-text {
        font-size: 28rpx;
        color: #333;
        text-align: center;
        line-height: 1.2;
      }
    }
  }
}

/* 通用区块样式 */
.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 30rpx;
  margin-bottom: 30rpx;
  
  .section-title {
    font-size: 36rpx;
    font-weight: 600;
    color: #333;
  }
  
  .section-more {
    display: flex;
    align-items: center;
    padding: 8rpx 16rpx;
    background: rgba(0, 0, 0, 0.05);
    border-radius: 20rpx;
    transition: all 0.3s ease;
    
    &:active {
      background: rgba(0, 0, 0, 0.1);
    }
  }
}

/* 冥想练习区域 */
.meditation-section {
  margin-bottom: 50rpx;
  position: relative;
  z-index: 1;
  
  .meditation-cards {
    padding-left: 30rpx;
    
    .meditation-card-list {
      display: flex;
      gap: 30rpx;
      padding-right: 30rpx;
      
      .meditation-card {
        flex-shrink: 0;
        width: 500rpx;
        height: 339rpx;
        border-radius: 32rpx;
        position: relative;
        overflow: hidden;
        
        &.card-basic {
          background: linear-gradient(135deg, #E8F4FF 0%, #F0F8FF 100%);
        }
        
        &.card-advanced {
          background: linear-gradient(135deg, #4A6FA5 0%, #2C4E7E 100%);
        }
        
        .card-content {
          position: absolute;
          top: 32rpx;
          left: 32rpx;
          right: 32rpx;
          
          .card-title {
            font-size: 48rpx;
            font-weight: 600;
            color: #fff;
            display: block;
            margin-bottom: 0;
            text-shadow: 0 2rpx 4rpx rgba(0, 0, 0, 0.1);
          }
        }
        
        .card-bottom-bar {
          position: absolute;
          bottom: 24rpx;
          left: 20rpx;
          width: 460rpx;
          height: 88rpx;
          background: rgba(121, 177, 218, 0.6);
          border-radius: 20rpx;
          backdrop-filter: blur(1px);
          display: flex;
          justify-content: space-between;
          align-items: center;
          padding: 0 32rpx;
          
          .card-duration {
            display: flex;
            align-items: center;
            gap: 12rpx;
            
            .duration-text {
              font-size: 28rpx;
              color: #fff;
              font-weight: 500;
            }
          }
          
          .card-play {
            width: 60rpx;
            height: 60rpx;
            background: rgba(255, 255, 255, 0.25);
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            backdrop-filter: blur(10rpx);
            
            .play-icon {
              width: 24rpx;
              height: 24rpx;
            }
          }
        }
      }
    }
  }
}

/* 冥想推荐区域 */
.recommend-section {
  margin-bottom: 50rpx;
  position: relative;
  z-index: 1;
  
  .recommend-cards {
    padding-left: 30rpx;
    
    .recommend-card-list {
      display: flex;
      gap: 24rpx;
      padding-right: 30rpx;
      
      .recommend-card-wrapper {
        display: flex;
        flex-direction: column;
        align-items: center;
        width: 260rpx;
      }

      .recommend-card {
        flex-shrink: 0;
        width: 260rpx;
        height: 260rpx;
        background: #fff;
        border-radius: 20rpx;
        overflow: hidden;
        box-shadow: 0 4rpx 20rpx rgba(0, 0, 0, 0.08);
        transition: all 0.3s ease;
        
        &:active {
          transform: scale(0.98);
          box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.12);
        }
        
        .recommend-image {
          width: 100%;
          height: 200rpx;
          flex-shrink: 0;
        }

        .duration-tag {
          position: absolute;
          top: 16rpx;
          left: 16rpx;
          background: rgba(121, 177, 218, 0.8);
          color: #fff;
          font-size: 22rpx;
          font-weight: 500;
          padding: 6rpx 12rpx;
          border-radius: 12rpx;
          backdrop-filter: blur(1px);
        }
      }
      
      .recommend-title {
        font-size: 26rpx;
        color: #333;
        margin-top: 10rpx;
        text-align: left;
        font-weight: 600;
        display: -webkit-box;
        -webkit-line-clamp: 2;
        -webkit-box-orient: vertical;
        overflow: hidden;
        text-overflow: ellipsis;
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