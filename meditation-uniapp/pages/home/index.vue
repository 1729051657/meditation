<template>
  <view class="mg-home">
    <view class="top-bar">
      <view class="greet">冥想</view>
      <view class="search" @click="goSearch">🔍</view>
    </view>
    <view class="chips">
      <view v-for="c in categories" :key="c.id" class="chip" @click="goCategory(c)">{{ c.name }}</view>
    </view>
    <swiper class="banner" v-if="banners.length">
      <swiper-item v-for="b in banners" :key="b.id">
        <image class="banner-img" :src="oss(b.image)" mode="aspectFill" />
      </swiper-item>
    </swiper>
    <view class="block" v-for="(items, code) in blocks" :key="code">
      <view class="block-title">{{ slotName(code) }}</view>
      <scroll-view class="hlist" scroll-x>
        <view class="card" v-for="item in items" :key="item.id" @click="enterItem(item)">
          <image class="cover" :src="coverOf(item)" mode="aspectFill" />
          <view class="title">{{ titleOf(item) }}</view>
          <view class="badge" v-if="durationOf(item)">{{ durationOf(item) }}</view>
        </view>
      </scroll-view>
    </view>
    <view class="articles" v-if="articles.length">
      <view class="block-title">冥想知识</view>
      <view class="article" v-for="a in articles" :key="a.id" @click="goArticle(a.id)">
        <image :src="oss(a.cover)" class="article-cover" mode="aspectFill" />
        <view class="article-title">{{ a.title }}</view>
      </view>
    </view>
  </view>
  </template>

<script>
import { listCategories } from '@/api/category'
import { listSlots, listSlotItems } from '@/api/recommend'
import { listBanners } from '@/api/banner'
import { listArticles } from '@/api/article'

export default {
  data() {
    return {
      categories: [],
      banners: [],
      blocks: {},
      articles: []
    }
  },
  async onLoad() {
    const [cats, slots, banners, arts] = await Promise.all([
      listCategories({ parentId: 0, status: 0 }),
      listSlots({ page: 'home', status: 0 }),
      listBanners({ page: 'home', status: 0 }),
      listArticles({ status: 0, pageNum: 1, pageSize: 6 })
    ])
    const slotArr = slots.rows || slots.data || []
    const itemsRes = await Promise.all(slotArr.map(s => listSlotItems({ slotId: s.id, status: 0 })))
    const blocks = {}
    slotArr.forEach((s, i) => { blocks[s.code] = (itemsRes[i].rows || itemsRes[i].data || []) })
    this.categories = cats.rows || cats.data || []
    this.banners = banners.rows || banners.data || []
    this.blocks = blocks
    this.articles = arts.rows || arts.data || []
  },
  methods: {
    goSearch() { uni.navigateTo({ url: '/pages/search/index' }) },
    goCategory(c) { uni.navigateTo({ url: `/pages/category/index?categoryId=${c.id}` }) },
    goArticle(id) { uni.navigateTo({ url: `/pages/article/detail?id=${id}` }) },
    enterItem(item) {
      const type = item.contentType || item.type
      const id = item.contentId || item.id
      if (type === 'series') {
        uni.navigateTo({ url: `/pages/series/detail?id=${id}` })
      } else if (type === 'track') {
        uni.navigateTo({ url: `/pages/player/index?trackId=${id}` })
      } else if (type === 'article') {
        this.goArticle(id)
      }
    },
    durationOf(it) { return it.durationSec ? Math.ceil(it.durationSec / 60) + '分钟' : '' },
    titleOf(it) { return it.title },
    coverOf(it) { return this.oss(it.cover) },
    oss(id) { return id ? `${this.$baseUrl}/system/oss/download/${id}` : '' },
    slotName(code) { return code }
  }
}
</script>

<style lang="scss" scoped>
.mg-home { padding: 24rpx; }
.top-bar{ display:flex; justify-content:space-between; align-items:center; margin-bottom:20rpx; }
.greet{ font-size:40rpx; font-weight:600; }
.search{ font-size:36rpx; }
.chips{ display:flex; gap:16rpx; margin:12rpx 0; flex-wrap:wrap; }
.chip{ background:#eef2f7; padding:12rpx 20rpx; border-radius:999rpx; font-size:26rpx; }
.banner{ height:260rpx; border-radius:16rpx; overflow:hidden; margin:16rpx 0; }
.banner-img{ width:100%; height:100%; }
.block{ margin-top:20rpx; }
.block-title{ font-size:32rpx; font-weight:600; margin:12rpx 4rpx; }
.hlist{ white-space:nowrap; }
.card{ width:300rpx; display:inline-block; margin-right:16rpx; position:relative; }
.cover{ width:300rpx; height:200rpx; border-radius:16rpx; }
.title{ font-size:26rpx; margin-top:8rpx; }
.badge{ position:absolute; left:12rpx; top:12rpx; background:rgba(0,0,0,.5); color:#fff; font-size:22rpx; padding:4rpx 10rpx; border-radius:8rpx; }
.articles{ margin-top:24rpx; }
.article{ display:flex; gap:16rpx; background:#fff; border-radius:12rpx; padding:12rpx; margin-bottom:12rpx; }
.article-cover{ width:160rpx; height:120rpx; border-radius:10rpx; }
.article-title{ font-size:28rpx; flex:1; }
</style>


