<template>
  <view class="mg-search">
    <view class="search-bar">
      <input class="ipt" v-model="kw" placeholder="搜索冥想/系列/文章" @confirm="doSearch" />
      <button class="btn" @click="doSearch">搜索</button>
    </view>
    <view class="chips" v-if="hot.length">
      <view class="chip" v-for="h in hot" :key="h.id" @click="useHot(h.keyword)">{{ h.keyword }}</view>
    </view>
    <view class="tabs">
      <view :class="['tab', tab==='track'?'active':'']" @click="tab='track'">单集</view>
      <view :class="['tab', tab==='series'?'active':'']" @click="tab='series'">系列</view>
      <view :class="['tab', tab==='article'?'active':'']" @click="tab='article'">文章</view>
    </view>
    <scroll-view class="list" scroll-y>
      <block v-if="tab==='track'">
        <view class="item" v-for="t in tracks" :key="t.id" @click="goTrack(t.id)">
          <image :src="oss(t.cover)" class="cover" />
          <view class="title">{{ t.title }}</view>
          <view class="sub">{{ Math.ceil((t.durationSec||0)/60) }}分钟</view>
        </view>
      </block>
      <block v-else-if="tab==='series'">
        <view class="item" v-for="s in series" :key="s.id" @click="goSeries(s.id)">
          <image :src="oss(s.cover)" class="cover" />
          <view class="title">{{ s.title }}</view>
          <view class="sub">系列 · {{ s.episodeCount }}</view>
        </view>
      </block>
      <block v-else>
        <view class="item" v-for="a in articles" :key="a.id" @click="goArticle(a.id)">
          <image :src="oss(a.cover)" class="cover" />
          <view class="title">{{ a.title }}</view>
          <view class="sub">{{ a.summary }}</view>
        </view>
      </block>
    </scroll-view>
  </view>
</template>

<script>
import { listHot, addHistory, searchTracks, searchSeries, searchArticles } from '@/api/search'

export default {
  data(){ return { kw:'', tab:'track', hot:[], tracks:[], series:[], articles:[] } },
  async onLoad(){ const res = await listHot({ status:0 }); this.hot = res.rows || res.data || [] },
  methods:{
    async doSearch(){ if(!this.kw) return; await addHistory({ keyword:this.kw }); await this.searchAll(this.kw) },
    async searchAll(kw){
      const [ts, ss, as] = await Promise.all([
        searchTracks(kw, { pageNum:1, pageSize:20 }),
        searchSeries(kw, { pageNum:1, pageSize:20 }),
        searchArticles(kw, { pageNum:1, pageSize:20 })
      ])
      this.tracks = ts.rows || ts.data || []
      this.series = ss.rows || ss.data || []
      this.articles = as.rows || as.data || []
    },
    useHot(k){ this.kw=k; this.doSearch() },
    goTrack(id){ uni.navigateTo({ url:`/pages/player/index?trackId=${id}` }) },
    goSeries(id){ uni.navigateTo({ url:`/pages/series/detail?id=${id}` }) },
    goArticle(id){ uni.navigateTo({ url:`/pages/article/detail?id=${id}` }) },
    oss(id){ return id? `${this.$baseUrl}/system/oss/download/${id}`: '' }
  }
}
</script>

<style lang="scss" scoped>
.search-bar{ display:flex; gap:12rpx; padding:20rpx; }
.ipt{ flex:1; background:#f3f4f6; border-radius:12rpx; padding:16rpx; }
.btn{ background:#3b82f6; color:#fff; border-radius:12rpx; padding:0 20rpx; }
.chips{ display:flex; gap:12rpx; padding:0 20rpx; flex-wrap:wrap; }
.chip{ background:#eef2f7; padding:10rpx 18rpx; border-radius:999rpx; font-size:24rpx }
.tabs{ display:flex; }
.tab{ flex:1; text-align:center; padding:16rpx 0; border-bottom:4rpx solid transparent; }
.tab.active{ border-color:#3b82f6; color:#3b82f6 }
.list{ height: calc(100vh - 220rpx); padding:12rpx 20rpx; }
.item{ display:flex; gap:12rpx; background:#fff; border-radius:12rpx; padding:12rpx; margin-bottom:12rpx; align-items:center }
.cover{ width:140rpx; height:100rpx; border-radius:8rpx }
.title{ font-size:28rpx; flex:1 }
.sub{ color:#888; font-size:22rpx }
</style>


