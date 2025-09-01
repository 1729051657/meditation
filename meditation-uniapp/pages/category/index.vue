<template>
  <view class="mg-category">
    <!-- 导航栏 -->
    <tn-nav-bar :alpha="true">
    </tn-nav-bar>


    <!-- 分类标签 -->
    <view class="category-chips">
      <scroll-view class="chips-scroll" scroll-x :show-scrollbar="false" enhanced>
        <view class="chips-container">
          <view class="chip" :class="{ active: category.id === currentCategoryId }" v-for="category in categories"
            :key="category.id" @click="switchCategory(category.id)">
            <view class="newchip">



            <image :src="getCategoryIcon(category.code, category.id)" class="chipImg"></image>

            {{ category.name }}
             </view>
          </view>
        </view>
      </scroll-view>
    </view>

    <!-- 为您推荐系列 -->
    <view class="content-section">
      <view class="section-header">
        <text class="section-title">为您推荐</text>
      </view>

      <scroll-view class="series-scroll" scroll-x :show-scrollbar="false" enhanced>
        <view class="series-container">
          <view class="series-card" v-for="s in series" :key="s.id" @click="playSeries(s)">
             <view class="series-content">
              <text class="series-title">{{ s.title }}</text>
              <view class="series-subtitle">
                <tn-icon name="play-circle" size="24" color="#79B1DA" style="margin-right: 8rpx;"></tn-icon>
                系列·{{ s.episodeCount || 0 }}节
              </view>
            </view>
            <view class="series-image-wrapper">
              <image :src="s.coverUrl" class="series-cover" mode="aspectFill" />
            </view>

          </view>
        </view>
      </scroll-view>
    </view>

    <!-- 全部冥想单集 -->
    <view class="content-section">
      <view class="section-header">
        <text class="section-title">全部冥想</text>
      </view>

      <view class="tracks-grid">
        <view class="track-card" v-for="t in tracks" :key="t.id" @click="playTrack(t)">
          <view class="track-image-wrapper">
            <image :src="t.coverUrl" class="track-cover" mode="aspectFill" />
            <view class="duration-badge">{{ Math.ceil((t.durationSec || 0) / 60) }}分钟</view>
            <view class="track-title-overlay">{{ t.title }}</view>
          </view>
        </view>
      </view>
    </view>

    <!-- 加载更多 -->
    <view class="load-more" v-if="hasMore" @click="loadMore">
      <tn-icon name="more" size="16" color="#666"></tn-icon>
      <text class="load-text">加载更多</text>
    </view>

    <!-- 空状态 -->
    <view class="empty-state" v-if="series.length === 0 && tracks.length === 0 && !loading">
      <tn-icon name="empty" size="80" color="#ccc"></tn-icon>
      <text class="empty-text">暂无内容</text>
    </view>
  </view>
</template>

<script>
import { listCategories } from '@/api/category'
import { listSeries } from '@/api/series'
import { listTracks } from '@/api/track'

export default {
  data() {
    return {
      categories: [], // 分类列表
      currentCategoryId: null,
      series: [], // 系列数据
      tracks: [], // 单集数据
      seriesPageNum: 1, // 系列分页
      tracksPageNum: 1, // 单集分页
      seriesHasMore: true, // 系列是否还有更多
      tracksHasMore: true, // 单集是否还有更多
      loading: false,
      pageSize: 10 // 每页数据量
    }
  },
  computed: {
    hasMore() {
      return this.seriesHasMore || this.tracksHasMore
    }
  },
  async onLoad(query) {
    console.log('页面加载，参数:', query)
    await this.loadCategoryData()
  },

  onShow() {
    console.log('页面显示')
    // 页面显示时可以刷新数据
  },
  methods: {
    // 加载分类数据
    async loadCategoryData() {
      try {
        console.log('开始加载分类数据...')
        const res = await listCategories({ status: 0 })
        console.log('分类数据响应:', res)

        // 兼容不同的响应格式
        if (res.rows) {
          this.categories = res.rows
        } else if (res.data && Array.isArray(res.data)) {
          this.categories = res.data
        } else if (res.data && res.data.rows) {
          this.categories = res.data.rows
        } else {
          this.categories = []
        }

        console.log('解析后的分类数据:', this.categories)

        if (this.categories.length > 0) {
          this.currentCategoryId = this.categories[0].id
          console.log('设置当前分类ID:', this.currentCategoryId)
          // 默认加载第一个分类的系列和单集数据
          await Promise.all([
            this.loadSeriesList(true),
            this.loadTracksList(true)
          ])
        } else {
          console.log('没有可用的分类数据')
        }
      } catch (error) {
        console.error('加载分类数据失败:', error)
        uni.showToast({
          title: '加载失败',
          icon: 'none'
        })
      }
    },

    // 加载系列列表
    async loadSeriesList(reset = false) {
      if (reset) {
        this.seriesPageNum = 1;
        this.series = [];
        this.seriesHasMore = true
      }
      if (!this.seriesHasMore || (this.loading && !reset)) return

      this.loading = true
      try {
        console.log('加载系列列表，分类ID:', this.currentCategoryId)
        const params = {
          categoryId: this.currentCategoryId,
          status: '0', // 注意：SeriesBo中status是String类型
          pageNum: this.seriesPageNum,
          pageSize: this.pageSize
        }
        console.log('系列请求参数:', params)

        const res = await listSeries(params)
        console.log('系列数据响应:', res)

        // 兼容不同的响应格式
        let rows = []
        if (res.rows) {
          rows = res.rows
        } else if (res.data && Array.isArray(res.data)) {
          rows = res.data
        } else if (res.data && res.data.rows) {
          rows = res.data.rows
        }

        console.log('解析后的系列数据:', rows)

        // 处理封面URL（如果需要）
        rows = rows.map(item => {
          // 如果没有coverUrl但有cover，记录日志
          if (!item.coverUrl && item.cover) {
            console.log('系列缺少coverUrl，cover ID:', item.cover)
          }
          return item
        })

        if (reset) {
          this.series = rows
        } else {
          this.series = this.series.concat(rows)
        }

        // 判断是否还有更多数据
        this.seriesHasMore = rows.length >= this.pageSize
        if (this.seriesHasMore) {
          this.seriesPageNum += 1
        }

        console.log('当前系列数据总数:', this.series.length, '还有更多:', this.seriesHasMore)
      } catch (error) {
        console.error('加载系列列表失败:', error)
        this.seriesHasMore = false
        if (reset) {
          this.series = []
        }
      } finally {
        this.loading = false
      }
    },

    // 加载单集列表
    async loadTracksList(reset = false) {
      if (reset) {
        this.tracksPageNum = 1;
        this.tracks = [];
        this.tracksHasMore = true
      }
      if (!this.tracksHasMore || (this.loading && !reset)) return

      this.loading = true
      try {
        console.log('加载单集列表，分类ID:', this.currentCategoryId)
        const params = {
          categoryId: this.currentCategoryId,
          status: 0, // TrackBo中status是Integer类型
          pageNum: this.tracksPageNum,
          pageSize: this.pageSize
        }
        console.log('单集请求参数:', params)

        const res = await listTracks(params)
        console.log('单集数据响应:', res)

        // 兼容不同的响应格式
        let rows = []
        if (res.rows) {
          rows = res.rows
        } else if (res.data && Array.isArray(res.data)) {
          rows = res.data
        } else if (res.data && res.data.rows) {
          rows = res.data.rows
        }

        console.log('解析后的单集数据:', rows)

        // 处理封面URL和音频URL（如果需要）
        rows = rows.map(item => {
          // 如果没有coverUrl但有cover，记录日志
          if (!item.coverUrl && item.cover) {
            console.log('单集缺少coverUrl，cover ID:', item.cover)
          }
          // 如果没有audioUrl但有audio，记录日志
          if (!item.audioUrl && item.audio) {
            console.log('单集缺少audioUrl，audio ID:', item.audio)
          }
          return item
        })

        if (reset) {
          this.tracks = rows
        } else {
          this.tracks = this.tracks.concat(rows)
        }

        // 判断是否还有更多数据
        this.tracksHasMore = rows.length >= this.pageSize
        if (this.tracksHasMore) {
          this.tracksPageNum += 1
        }

        console.log('当前单集数据总数:', this.tracks.length, '还有更多:', this.tracksHasMore)
      } catch (error) {
        console.error('加载单集列表失败:', error)
        this.tracksHasMore = false
        if (reset) {
          this.tracks = []
        }
      } finally {
        this.loading = false
      }
    },

    // 切换分类
    async switchCategory(id) {
      if (this.currentCategoryId === id) {
        console.log('分类未改变，不重新加载')
        return
      }

      console.log('切换分类，从', this.currentCategoryId, '到', id)
      this.currentCategoryId = id

      // 重置分页参数
      this.seriesPageNum = 1
      this.tracksPageNum = 1
      this.seriesHasMore = true
      this.tracksHasMore = true

      // 重新加载系列和单集数据
      await Promise.all([
        this.loadSeriesList(true),
        this.loadTracksList(true)
      ])
    },

    // 播放系列
    playSeries(series) {
      uni.navigateTo({
        url: `/pages/series/detail?id=${series.id}`
      })
    },

    // 播放单集
    playTrack(track) {
      uni.navigateTo({
        url: `/pages/player/index?id=${track.id}&categoryId=${this.currentCategoryId}`
      })
    },

    goBack() { uni.navigateBack() },
    goToSearch() { uni.navigateTo({ url: '/pages/search/index' }) },

    // 加载更多
    async loadMore() {
      console.log('点击加载更多')
      // 同时加载系列和单集的下一页
      const promises = []
      if (this.seriesHasMore) {
        promises.push(this.loadSeriesList())
      }
      if (this.tracksHasMore) {
        promises.push(this.loadTracksList())
      }
      if (promises.length > 0) {
        await Promise.all(promises)
      }
    },

    // 获取当前分类名称
    getCurrentCategoryName() {
      if (this.currentCategoryId && this.categories.length > 0) {
        const currentCategory = this.categories.find(cat => cat.id === this.currentCategoryId)
        return currentCategory ? currentCategory.name : '分类'
      }
      return '分类'
    },

    // 根据分类code获取对应的图标
    getCategoryIcon(categoryCode, categoryId) {
      // 基础图标映射 - 使用code匹配
      const iconMap = {
        'emotion': '/static/category/emotion-regulation@2x.png',
        'focus': '/static/category/improve-focus@2x.png',
        'sleep': '/static/category/improve-sleep@2x.png',
        'relax': '/static/category/relax-stress@2x.png'
      }

      // 如果分类被选中，返回对应的选中状态图标
      if (categoryId === this.currentCategoryId) {
        // 根据分类code返回对应的选中状态图标
        const selectedIconMap = {
          'emotion': '/static/home/emotion-regulation@2x.png',
          'focus': '/static/home/improve-focus@2x.png',
          'sleep': '/static/home/improve-sleep@2x.png',
          'relax': '/static/home/relax-stress@2x.png'
        }
        return selectedIconMap[categoryCode] || iconMap[categoryCode] || '/static/images/category/category-icon.png'
      }

      // 未选中状态返回基础图标
      return iconMap[categoryCode] || '/static/images/category/category-icon.png'
    }
  },
  onReachBottom() {
    // 根据滚动位置决定加载哪个模块的数据
    // 这里简单处理，优先加载系列数据
    if (this.seriesHasMore) {
      this.loadSeriesList()
    } else if (this.tracksHasMore) {
      this.loadTracksList()
    }
  },

  async onPullDownRefresh() {
    // 刷新时重新加载所有数据
    console.log('下拉刷新触发')
    await Promise.all([
      this.loadSeriesList(true),
      this.loadTracksList(true)
    ])
    uni.stopPullDownRefresh()
  }
}
</script>

<style lang="scss" scoped>
.mg-category {
  min-height: 100vh;
  background: linear-gradient(180deg, #E8F4FF 0%, #F5F7FA 100%);
  padding-bottom: 100rpx;
}

/* 导航栏样式 */
.nav-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 30rpx;
  height: 88rpx;
}

.nav-left,
.nav-right {
  width: 60rpx;
  height: 60rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  background: rgba(255, 255, 255, 0.8);
  border-radius: 50%;
  box-shadow: 0 2rpx 8rpx rgba(0, 0, 0, 0.1);
}

.nav-title {
  font-size: 36rpx;
  font-weight: 600;
  color: #333;
}


/* 分类标签样式 */
.category-chips {
  padding: 30rpx 0;
  position: relative;
  z-index: 1;
}

/* 系列横向滚动样式 */
.series-scroll {
  margin-bottom: 40rpx;
}

.series-container {
  display: flex;
  align-items: center;
  gap: 24rpx;
  padding-right: 30rpx;
}

.series-card {
  flex-shrink: 0;
  width: 540rpx;
  background: #fff;
  border-radius: 20rpx;
  overflow: hidden;

  box-shadow: 0 4rpx 20rpx rgba(0, 0, 0, 0.08);
  transition: all 0.3s ease;
  display: flex;


  &:active {
    transform: scale(0.98);
    box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.12);
  }
}

.series-image-wrapper {
  position: relative;
  width: 160rpx;

  overflow: hidden;
  padding:20rpx;
  box-sizing: border-box;
}

.series-cover {
  width: 100%;
  height: 100%;
  border-radius: 20rpx;
}

.series-content {
  flex:1;
  box-sizing: border-box;
  padding: 20rpx;
}

.series-title {
  display: block;
  font-size: 28rpx;
  font-weight: 600;
  height: 80rpx;
  color: #333;
  margin-bottom: 8rpx;
  line-height: 1.4;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  text-overflow: ellipsis;
}

.series-subtitle {
  display: block;
  font-size: 24rpx;
  color: #999;
  line-height: 1.4;
  margin-top:48rpx;
  display: flex;
  align-items: center;
}

/* 单集网格样式 */
.tracks-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 24rpx;
}

.track-card {
  background: #fff;
  border-radius: 20rpx;
  overflow: hidden;
  box-shadow: 0 4rpx 20rpx rgba(0, 0, 0, 0.08);
  transition: all 0.3s ease;

  &:active {
    transform: scale(0.98);
    box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.12);
  }
}

.track-image-wrapper {
  position: relative;
 width: 342rpx;
height: 360rpx;
  overflow: hidden;
}

.track-cover {
  width: 100%;
  height: 100%;
 border-radius: 24rpx;
}

.track-title-overlay {
  position: absolute;
  bottom: 16rpx;
  left: 16rpx;
  color: #fff;
  font-size: 26rpx;
  font-weight: 500;
  text-shadow: 0 2rpx 4rpx rgba(0, 0, 0, 0.5);
  max-width: calc(100% - 32rpx);
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  text-overflow: ellipsis;
}

.chips-scroll {
  padding: 0 30rpx;
}

.chips-container {
  display: flex;
  gap: 20rpx;
  padding-right: 30rpx;
}

.chip {
  flex-shrink: 0;
  // background: #fff;
  // padding: 16rpx 32rpx;
  // border-radius: 999rpx;
  // font-size: 28rpx;
  // color: #666;
  // box-shadow: 0 2rpx 8rpx rgba(0, 0, 0, 0.08);
  font-weight: 400;
  font-size: 36rpx;
  color: #888888;
  transition: all 0.3s ease;
  border: 2rpx solid transparent;
  opacity: 0.7;

  &:active {
    transform: scale(0.95);
    opacity: 1;
  }

  &.active {
    // background: linear-gradient(135deg, #4A6FA5 0%, #2C4E7E 100%);
    // color: #fff;
    // box-shadow: 0 4rpx 16rpx rgba(74, 111, 165, 0.3);
    font-weight: 600;
    font-size: 40rpx;
    color: #25292D;
  }
}

/* 内容区域样式 */
.content-section {
  padding: 0 30rpx;
  margin-bottom: 40rpx;
}

.section-header {
  margin-bottom: 30rpx;
}

.section-title {
 font-weight: 600;
font-size: 32rpx;
color: #25292D;
margin-top: 40rpx;
}

/* 网格布局 */
.grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 24rpx;
}

.card {
  background: #fff;
  border-radius: 20rpx;
  overflow: hidden;
  box-shadow: 0 4rpx 20rpx rgba(0, 0, 0, 0.08);
  transition: all 0.3s ease;

  &:active {
    transform: scale(0.98);
    box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.12);
  }

  &.series-card {
    border-left: 6rpx solid #4A6FA5;
  }

  &.track-card {
    border-left: 6rpx solid #2C4E7E;
  }
}

.card-image-wrapper {
  position: relative;
  width: 100%;
  height: 260rpx;
  overflow: hidden;
}

.cover {
  width: 100%;
  height: 100%;
  transition: transform 0.3s ease;
}

.card:hover .cover {
  transform: scale(1.05);
}

.duration-badge {
  position: absolute;
  left: 16rpx;
  top: 16rpx;
  background: rgba(0, 0, 0, 0.7);
  color: #fff;
  font-size: 22rpx;
  padding: 6rpx 12rpx;
  border-radius: 12rpx;
  backdrop-filter: blur(10rpx);
}



.card-content {
  padding: 20rpx;
}

.card-title {
  display: block;
  font-size: 28rpx;
  font-weight: 600;
  color: #333;
  margin-bottom: 8rpx;
  line-height: 1.4;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  text-overflow: ellipsis;
}

.card-subtitle {
  display: block;
  font-size: 24rpx;
  color: #666;
  margin-bottom: 8rpx;
  line-height: 1.4;
  display: -webkit-box;
  -webkit-line-clamp: 1;
  -webkit-box-orient: vertical;
  overflow: hidden;
  text-overflow: ellipsis;
}

.card-desc {
  display: block;
  font-size: 24rpx;
  color: #999;
  line-height: 1.4;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  text-overflow: ellipsis;
}

/* 加载更多 */
.load-more {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 12rpx;
  padding: 30rpx;
  color: #666;
  font-size: 28rpx;
  opacity: 0.5;

  &:active {
    opacity:1;
  }
}

.load-text {
  font-size: 28rpx;
}

/* 空状态 */
.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 100rpx 30rpx;
  text-align: center;
}

.empty-text {
  font-size: 28rpx;
  color: #999;
  margin-top: 20rpx;
}
.chipImg{
  width: 40rpx;
  height: 40rpx;
  margin-right: 4rpx;

}
.newchip{
  display: flex;
  align-items: center;
}
.play{
  width: 40rpx;
  height: 40rpx;
  margin-right: 8rpx;
}
</style>
