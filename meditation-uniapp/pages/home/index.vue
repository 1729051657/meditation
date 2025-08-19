<template>
  <view class="home-page">
    <!-- 自定义导航栏 -->
    <tn-nav-bar
      :isBack="false"
      :bottomShadow="false"
      backgroundColor="#7C3AED"
      :fixed="true"
    >
      <view class="nav-title">
        <text class="nav-title-text">冥想</text>
      </view>
    </tn-nav-bar>

    <!-- 登录状态提示 -->
    <view v-if="!isLogin" class="login-tip">
      <view class="loading-spinner"></view>
      <text class="loading-text">正在登录中...</text>
    </view>

    <!-- 主要内容 -->
    <view v-else class="main-content">
      <!-- 轮播图 -->
      <view class="banner-section" v-if="bannerList.length > 0">
        <swiper class="banner-swiper" :indicator-dots="true" :autoplay="true" :interval="3000" :duration="500">
          <swiper-item v-for="banner in bannerList" :key="banner.id" @click="handleBannerClick(banner)">
            <image :src="oss(banner.image)" class="banner-image" mode="aspectFill" />
          </swiper-item>
        </swiper>
      </view>

      <!-- 分类导航 -->
      <view class="category-section" v-if="categoryList.length > 0">
        <view class="section-title">分类导航</view>
        <view class="category-grid">
          <view 
            class="category-item" 
            v-for="category in categoryList" 
            :key="category.id"
            @click="goToCategory(category.id)"
          >
            <image :src="oss(category.icon)" class="category-icon" mode="aspectFill" />
            <text class="category-name">{{ category.name }}</text>
          </view>
        </view>
      </view>

      <!-- 推荐系列 -->
      <view class="series-section" v-if="seriesList.length > 0">
        <view class="section-title">为你推荐</view>
        <scroll-view class="series-scroll" scroll-x>
          <view class="series-list">
            <view 
              class="series-item" 
              v-for="series in seriesList" 
              :key="series.id"
              @click="goToSeries(series.id)"
            >
              <image :src="oss(series.cover)" class="series-cover" mode="aspectFill" />
              <view class="series-info">
                <text class="series-title">{{ series.title }}</text>
                <text class="series-subtitle">{{ series.subtitle }}</text>
                <text class="series-episodes">{{ series.episodeCount }}集</text>
              </view>
            </view>
          </view>
        </scroll-view>
      </view>

      <!-- 热门文章 -->
      <view class="article-section" v-if="articleList.length > 0">
        <view class="section-title">热门文章</view>
        <view class="article-list">
          <view 
            class="article-item" 
            v-for="article in articleList" 
            :key="article.id"
            @click="goToArticle(article.id)"
          >
            <image :src="oss(article.cover)" class="article-cover" mode="aspectFill" />
            <view class="article-info">
              <text class="article-title">{{ article.title }}</text>
              <text class="article-summary">{{ article.summary }}</text>
            </view>
          </view>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
import { mapState, mapActions } from 'vuex'
import { listCategory } from '@/api/category'
import { listSeries } from '@/api/series'
import { listArticle } from '@/api/article'
import { listBanner } from '@/api/banner'

export default {
  data() {
    return {
      loginLoading: false,
      categoryList: [],
      seriesList: [],
      articleList: [],
      bannerList: []
    }
  },
  
  computed: {
    ...mapState('user', ['isLogin', 'token', 'userInfo'])
  },
  
  onLoad() {
    // 页面加载时自动登录
    this.autoLogin()
  },
  
  methods: {
    ...mapActions('user', ['wxLogin']),
    
    // 自动微信登录
    async autoLogin() {
      try {
        this.loginLoading = true
        
        // 检查是否已登录
        if (this.token) {
          console.log('已登录，直接加载数据')
          this.loadData()
          return
        }
        
        // 调用store中的小程序登录方法
        await this.wxLogin({
          tenantId: '000000'
        })
        
        console.log('登录成功，用户信息：', this.userInfo)
        
        // 登录成功，加载数据
        this.loadData()
        
      } catch (error) {
        console.error('自动登录失败:', error)
        
        // 如果是开发环境，可能是因为不在微信环境
        // #ifdef H5
        console.log('H5环境，跳过微信登录')
        this.loadData()
        // #endif
        
        // #ifndef H5
        uni.showToast({
          title: error.message || '登录失败',
          icon: 'none'
        })
        // 登录失败时也尝试加载数据（可能有些接口不需要登录）
        this.loadData()
        // #endif
      } finally {
        this.loginLoading = false
      }
    },
    
    // 加载页面数据
    async loadData() {
      try {
        // 并发加载数据
        const [categoryRes, seriesRes, articleRes, bannerRes] = await Promise.all([
          listCategory(),
          listSeries({ pageNum: 1, pageSize: 10 }),
          listArticle({ pageNum: 1, pageSize: 5 }),
          listBanner({ pageNum: 1, pageSize: 5 })
        ])
        
        if (categoryRes.code === 200) {
          this.categoryList = categoryRes.data || []
        }
        
        if (seriesRes.code === 200) {
          this.seriesList = seriesRes.rows || []
        }
        
        if (articleRes.code === 200) {
          this.articleList = articleRes.rows || []
        }
        
        if (bannerRes.code === 200) {
          this.bannerList = bannerRes.rows || []
        }
        
      } catch (error) {
        console.error('加载数据失败:', error)
      }
    },
    
    // OSS资源链接
    oss(path) {
      if (!path) return '/static/images/default-cover.png'
      return this.$baseUrl + path
    },
    
    // 横幅点击
    handleBannerClick(banner) {
      if (banner.linkType === 'series') {
        this.goToSeries(banner.linkTarget)
      } else if (banner.linkType === 'article') {
        this.goToArticle(banner.linkTarget)
      } else if (banner.linkType === 'url' && banner.linkTarget) {
        // 打开外部链接
        uni.navigateTo({
          url: `/subPages/webview/index?url=${encodeURIComponent(banner.linkTarget)}`
        })
      }
    },
    
    // 跳转到分类页
    goToCategory(categoryId) {
      uni.navigateTo({
        url: `/pages/category/index?categoryId=${categoryId}`
      })
    },
    
    // 跳转到系列详情
    goToSeries(seriesId) {
      uni.navigateTo({
        url: `/subPages/series/detail?seriesId=${seriesId}`
      })
    },
    
    // 跳转到文章详情
    goToArticle(articleId) {
      uni.navigateTo({
        url: `/subPages/article/detail?articleId=${articleId}`
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.home-page {
  min-height: 100vh;
  background: #f5f5f7;
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

.login-tip {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 200rpx 0;
  
  .loading-spinner {
    width: 60rpx;
    height: 60rpx;
    border: 4rpx solid #e5e7eb;
    border-top: 4rpx solid #7C3AED;
    border-radius: 50%;
    animation: spin 1s linear infinite;
    margin-bottom: 20rpx;
  }
  
  .loading-text {
    color: #6b7280;
    font-size: 28rpx;
  }
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

.main-content {
  padding-top: 120rpx;
}

.banner-section {
  margin: 20rpx;
  
  .banner-swiper {
    height: 300rpx;
    border-radius: 20rpx;
    overflow: hidden;
    
    .banner-image {
      width: 100%;
      height: 100%;
    }
  }
}

.section-title {
  font-size: 32rpx;
  font-weight: 600;
  color: #1f2937;
  margin: 40rpx 30rpx 20rpx;
}

.category-section {
  .category-grid {
    display: grid;
    grid-template-columns: repeat(4, 1fr);
    gap: 20rpx;
    padding: 0 30rpx;
    
    .category-item {
      display: flex;
      flex-direction: column;
      align-items: center;
      padding: 20rpx;
      background: #fff;
      border-radius: 16rpx;
      
      .category-icon {
        width: 80rpx;
        height: 80rpx;
        border-radius: 16rpx;
        margin-bottom: 12rpx;
      }
      
      .category-name {
        font-size: 24rpx;
        color: #374151;
        text-align: center;
      }
    }
  }
}

.series-section {
  .series-scroll {
    white-space: nowrap;
    padding: 0 30rpx;
    
    .series-list {
      display: inline-flex;
      gap: 20rpx;
      
      .series-item {
        width: 280rpx;
        background: #fff;
        border-radius: 16rpx;
        overflow: hidden;
        
        .series-cover {
          width: 100%;
          height: 180rpx;
        }
        
        .series-info {
          padding: 20rpx;
          
          .series-title {
            display: block;
            font-size: 28rpx;
            font-weight: 500;
            color: #1f2937;
            margin-bottom: 8rpx;
            white-space: nowrap;
            overflow: hidden;
            text-overflow: ellipsis;
          }
          
          .series-subtitle {
            display: block;
            font-size: 24rpx;
            color: #6b7280;
            margin-bottom: 8rpx;
            white-space: nowrap;
            overflow: hidden;
            text-overflow: ellipsis;
          }
          
          .series-episodes {
            font-size: 22rpx;
            color: #7C3AED;
            background: rgba(124, 58, 237, 0.1);
            padding: 4rpx 12rpx;
            border-radius: 12rpx;
          }
        }
      }
    }
  }
}

.article-section {
  .article-list {
    padding: 0 30rpx;
    
    .article-item {
      display: flex;
      background: #fff;
      border-radius: 16rpx;
      padding: 20rpx;
      margin-bottom: 20rpx;
      
      .article-cover {
        width: 120rpx;
        height: 120rpx;
        border-radius: 12rpx;
        margin-right: 20rpx;
      }
      
      .article-info {
        flex: 1;
        
        .article-title {
          display: block;
          font-size: 28rpx;
          font-weight: 500;
          color: #1f2937;
          margin-bottom: 12rpx;
          line-height: 1.4;
        }
        
        .article-summary {
          font-size: 24rpx;
          color: #6b7280;
          line-height: 1.5;
          display: -webkit-box;
          -webkit-line-clamp: 2;
          -webkit-box-orient: vertical;
          overflow: hidden;
        }
      }
    }
  }
}
</style>


