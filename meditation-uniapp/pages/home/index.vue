<template>
  <!-- 头部 -->
  <view class="home-page">
    <view class="topNav" :style="{ height: navHeight + 'px', paddingTop: statusBarHeight + 'px' }"></view>
    <!-- 背景图 -->
    <image src="/static/home/background@3x.png" class="background-image" mode="widthFix"></image>

    <!-- 顶部区域 -->
    <view class="header-section">
      <!-- 问候语和用户状态 -->
      <view class="header-left">
        <view class="greeting">{{ greeting }}</view>
        <view class="user-status" v-if="isLoggedIn">
          <text class="status-text">已登录</text>
        </view>
      </view>

      <!-- 搜索图标 -->
      <view class="search-icon" @click="goToSearch">
        <tn-icon name="search" size="20" color="#333"></tn-icon>
      </view>
    </view>

    <!-- 功能按钮区域 -->
    <view class="feature-section">
      <view v-for="category in categories" :key="category.id" class="feature-item" @click="goToCategory(category.code)">
        <view class="feature-icon-wrapper">
          <image :src="category.icon || categoryIconMap[category.code] || '/static/images/default-category.png'"
            class="feature-icon" mode="aspectFit"></image>
          <text class="feature-text">{{ category.name }}</text>
        </view>
      </view>
    </view>

    <!-- 冥想练习区域 -->
    <view class="meditation-section" v-if="meditationSlots.length > 0">
      <view class="section-header">
        <text class="section-title">冥想练习</text>
      </view>

      <scroll-view class="meditation-cards" scroll-x :show-scrollbar="false" enhanced>
        <view class="meditation-card-list">
          <view v-for="(item, index) in meditationSlots" :key="item.id" class="meditation-card"
            :class="index === 0 ? 'card-basic' : 'card-advanced'"
            :style="item.coverUrl ? `background-image: url(${item.coverUrl}); background-size: cover; background-position: center;` : ''"
            @click="goToMeditation(item)">
            <view class="card-content">
              <text class="card-title">{{ item.title }}</text>
              <text class="card-subtitle" v-if="item.subtitle">{{ item.subtitle }}</text>
            </view>
            <view class="card-bottom-bar">
              <view class="card-duration">
                <image class="timeImg" src="/static/home/time.png"  lazy-load="false" binderror="" bindload="" />
                <text class="duration-text">{{ Math.round(item.recommendDuration / 60) || 15 }}分钟</text>
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
          <view v-for="item in recommendItems" :key="item.id" class="recommend-card-wrapper"
            @click="goToRecommend(item)">
            <view class="recommend-card">
              <image :src="item.coverUrl || defaultCover" class="recommend-image" mode="aspectFill"></image>
              <view class="duration-tag">{{ Math.round(item.durationSec / 60) || 15 }}分钟</view>
            </view>
            <text class="recommend-title">{{ item.title }}</text>
          </view>
        </view>
      </scroll-view>
    </view>

    <!-- 冥想知识区域 -->
    <view class="knowledge-section" v-if="knowledgeItems.length > 0">
      <view class="section-header" @click="goToMore('knowledge')">
        <text class="section-title">冥想知识</text>
        <view class="section-more">
          <uni-icons type="right" size="16" color="#999"></uni-icons>
        </view>
      </view>

      <view class="knowledge-list">
        <view v-for="item in knowledgeItems.slice(0, 3)" :key="item.id" class="knowledge-item"
          @click="goToKnowledge(item)">
          <image :src="item.coverUrl || defaultKnowledgeCover" class="knowledge-image" mode="aspectFill"></image>
          <view class="knowledge-content">
            <text class="knowledge-title">{{ item.title }}</text>
            <text class="knowledge-desc" v-if="item.summary">{{ item.summary }}</text>
          </view>
        </view>
      </view>
    </view>
    <!-- <view class="music">
      <image class="musicimg" src="https://bawu-film.oss-cn-shanghai.aliyuncs.com/2025/08/27/9d999a824b444832b78b23261bb721e5.png"  lazy-load="false" binderror="" bindload="" />
      <view class="musicName">
        名称
      </view>
        
        <view class="timing">
          定时停止 9:12
        </view>
        <image class="pause" src="/static/player/close.png"  lazy-load="false" binderror="" bindload="" />
          
          
    </view> -->
    
    <!-- 迷你播放器 -->
    <MiniPlayer ref="miniPlayer" />
      
  </view>
</template>

<script>
import { getHomeData } from '@/api/home'
import MiniPlayer from '@/components/MiniPlayer/index.vue'

export default {
  components: {
    MiniPlayer
  },
  data() {
    return {
      navHeight: "", // 导航栏高度
      statusBarHeight: '', // 状态栏高度
      greeting: '', // 问候语
      categories: [], // 功能分类
      meditationSlots: [], // 冥想练习推荐位
      recommendItems: [], // 推荐内容
      knowledgeItems: [], // 知识内容
      defaultCover: '/static/images/default-cover.png',
      defaultKnowledgeCover: '/static/images/default-knowledge.png',
      isLoggedIn: false, // 登录状态
      showMiniPlayer: false, // 是否显示迷你播放器
      miniPlayerData: null, // 迷你播放器数据
      // 分类图标映射
      categoryIconMap: {
        'relax': '/static/home/relax-stress@2x.png',
        'sleep': '/static/home/improve-sleep@2x.png',
        'focus': '/static/home/improve-focus@2x.png',
        'emotion': '/static/home/emotion-regulation@2x.png'
      }
    }
  },

  onLoad() {
    this.loadHomeData()
    this.checkLoginStatus()
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

  },
  
  onShow() {
    // 页面显示时，检查是否有音频在播放
    console.log('首页显示，检查音频状态')
    this.checkAudioStatus()
  },

  onPullDownRefresh() {
    this.loadHomeData().finally(() => {
      uni.stopPullDownRefresh()
    })
  },

  methods: {
    // 检查音频播放状态
    checkAudioStatus() {
      const audioContext = uni.getBackgroundAudioManager()
      
      if (audioContext && audioContext.src) {
        console.log('首页检测到音频:', {
          src: audioContext.src,
          title: audioContext.title,
          paused: audioContext.paused
        })
        
        this.showMiniPlayer = true
        this.miniPlayerData = {
          title: audioContext.title || '未知音频',
          coverUrl: audioContext.coverImgUrl || '/static/images/default-cover.png',
          audioUrl: audioContext.src,
          isPlaying: !audioContext.paused
        }
      } else {
        console.log('首页未检测到音频')
        this.showMiniPlayer = false
        this.miniPlayerData = null
      }
    },
    
    // 检查登录状态
    checkLoginStatus() {
      const token = uni.getStorageSync('meditation_token')
      const userInfo = uni.getStorageSync('meditation_user_info')

      if (token && userInfo) {
        // 检查token是否过期
        if (this.isTokenExpired(token)) {
          console.log('Token已过期，尝试重新登录')
          this.isLoggedIn = false
          this.autoRelogin()
          return
        }

        console.log('用户已登录:', userInfo)
        this.isLoggedIn = true
        // 可以在这里更新UI显示登录状态
      } else {
        console.log('用户未登录')
        this.isLoggedIn = false
        // 可以在这里显示登录提示或引导
      }
    },

    // 检查token是否过期
    isTokenExpired(token) {
      try {
        // 检查本地存储时间
        const tokenTime = uni.getStorageSync('meditation_token_time')
        if (tokenTime) {
          const currentTime = Date.now()
          const tokenAge = currentTime - parseInt(tokenTime)
          // 如果token超过2小时，认为过期
          if (tokenAge > 7200000) { // 2小时 = 7200000毫秒
            return true
          }
        }
        return false
      } catch (error) {
        console.error('检查token过期失败:', error)
        return true
      }
    },

    // 自动重新登录
    async autoRelogin() {
      try {
        console.log('开始自动重新登录...')

        // 显示加载提示
        uni.showLoading({
          title: '重新登录中...',
          mask: true
        })

        // 调用store中的wxLogin方法
        await this.$store.dispatch('user/wxLogin')

        // 隐藏加载提示
        uni.hideLoading()

        console.log('自动重新登录成功')
        this.isLoggedIn = true

        // 重新加载首页数据
        this.loadHomeData()

      } catch (error) {
        // 隐藏加载提示
        uni.hideLoading()

        console.error('自动重新登录失败:', error)
        this.isLoggedIn = false

        // 显示登录失败提示
        uni.showToast({
          title: '登录失败，请重试',
          icon: 'none',
          duration: 2000
        })
      }
    },

    // 加载首页数据
    async loadHomeData() {
      try {
        const res = await getHomeData()

        if (res.code === 200 && res.data) {
          const data = res.data

          // 设置问候语
          this.greeting = data.greeting || '您好'

          // 设置功能分类
          this.categories = data.categoryVoList || []

          // 设置冥想练习 - 使用 seriesVoList 作为冥想练习数据
          this.meditationSlots = data.seriesVoList || []

          // 设置推荐内容 - 使用 trackVoList 作为推荐内容
          this.recommendItems = data.trackVoList || []

          // 设置知识内容 - 使用 articleVoList 作为知识内容
          this.knowledgeItems = data.articleVoList || []
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
    // 跳转到列表
    goToMore(type) {
      uni.navigateTo({
        url: `/pages/list/list`
      })
    },

    // 跳转到冥想详情
    goToMeditation(item) {
      // 跳转到系列详情页
      uni.navigateTo({
        url: `/pages/player/index?id=${item.id}`
      })
    },

    // 跳转到推荐详情
    goToRecommend(item) {
      // 跳转到播放器页面
      uni.navigateTo({
        url: `/pages/player/index?id=${item.id}&type=track`
      })
    },

    // 跳转到知识详情
    goToKnowledge(item) {
      // 跳转到文章详情页
      uni.navigateTo({
        url: `/pages/article/detail?id=${item.id}`
      })
    },

    // 查看更多
    // goToMore(type) {
    //   if (type === 'meditation') {
    //     uni.navigateTo({
    //       url: '/pages/series/index'
    //     })
    //   } else if (type === 'knowledge') {
    //     uni.navigateTo({
    //       url: '/pages/article/index'
    //     })
    //   }
    // }
  }
}
</script>

<style lang="scss" scoped>
.home-page {
  min-height: 100vh;
  // background: linear-gradient(180deg, #E8F4FF 0%, #F5F7FA 100%);
  background: #D8E2F0;
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
  padding: 0 30rpx;

  position: relative;
  z-index: 1;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.header-left {
  display: flex;
  flex-direction: column;
  gap: 8rpx;
}

.greeting {
  font-size: 48rpx;
  font-weight: 600;
  color: #333;
}

.user-status {
  display: flex;
  align-items: center;
  gap: 8rpx;
}

.status-text {
  font-size: 24rpx;
  color: #4CAF50;
  background: rgba(76, 175, 80, 0.1);
  padding: 4rpx 12rpx;
  border-radius: 12rpx;
  font-weight: 500;
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
      opacity: 0.7;

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

  align-items: center;
  padding: 0 30rpx;
  margin-bottom: 30rpx;

  .section-title {
    font-size: 32rpx;
    font-weight: 600;
    color: #25292D;
  }

  .section-more {
    display: flex;
    align-items: center;
    // padding: 8rpx 16rpx;
    // background: rgba(0, 0, 0, 0.05);
    // border-radius: 20rpx;
    transition: all 0.3s ease;
    justify-content: flex-start;
    margin-left: 4rpx;

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
        background-size: cover;
        background-position: center;

        &.card-basic {
          background-color: linear-gradient(135deg, #E8F4FF 0%, #F0F8FF 100%);
        }

        &.card-advanced {
          background-color: linear-gradient(135deg, #4A6FA5 0%, #2C4E7E 100%);
        }

        &::before {
          content: '';
          position: absolute;
          top: 0;
          left: 0;
          right: 0;
          bottom: 0;
          background: linear-gradient(180deg, rgba(0, 0, 0, 0.3) 0%, rgba(0, 0, 0, 0.5) 100%);
          z-index: 1;
        }

        .card-content {
          position: absolute;
          top: 32rpx;
          left: 32rpx;
          right: 32rpx;
          z-index: 2;

          .card-title {
            font-size: 48rpx;
            font-weight: 600;
            color: #fff;
            display: block;
            margin-bottom: 8rpx;
            text-shadow: 0 2rpx 4rpx rgba(0, 0, 0, 0.1);
          }

          .card-subtitle {
            font-size: 28rpx;
            color: rgba(255, 255, 255, 0.9);
            display: block;
            text-shadow: 0 1rpx 2rpx rgba(0, 0, 0, 0.1);
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
          z-index: 2;

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
              width: 60rpx;
              height: 60rpx;
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
          height:100%;
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
      // background: #fff;
      border-radius: 16rpx;
      // box-shadow: 0 2rpx 8rpx rgba(0, 0, 0, 0.06);

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

// 导航栏高度
.topNav {
  height: 100rpx;

  display: flex;
  justify-content: flex-start;
  align-items: center;
  padding: 0 20rpx;
  box-sizing: border-box;
}

.nav-left {
  font-size: 36rpx;
  font-weight: 600;

  margin-right: 30rpx;
  font-style: italic;
}

.nav-search input {
  width: 60%;
  height: 62rpx;
  border-radius: 30rpx;
  padding-left: 25rpx;
  background-color: #f0f8ffa6;
  box-sizing: border-box;
}

.placClass {
  font-size: 24rpx;
  color: #fff;
}

.w24 {
  width: 24rpx;
  height: 24rpx;
}
.timeImg{
  width: 24rpx;
  height: 24rpx;
}

</style>