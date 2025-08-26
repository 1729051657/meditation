<template>
  <view class="series-detail">
      <!-- 导航栏 -->
    <tn-nav-bar :alpha="true">
    </tn-nav-bar>
    <view class="header">
      <text class="title">{{ series && series.title ? series.title : '' }}</text>
      <text class="sub">系列 · {{ series && series.episodeCount ? series.episodeCount : 0 }}</text>
    </view>
    <image class="banner" :src="oss(series && series.bannerUrl ? series.bannerUrl : (series && series.coverUrl ? series.coverUrl : ''))" mode="aspectFill" />
    <view class="intro">{{ series && series.intro ? series.intro : '' }}</view>
    <view class="list">
      <view class="item" v-for="t in tracks" :key="t.id" @click="play(t.id)">
        <view class="left">
          <image :src="oss(t.coverUrl)" class="cover" />
        </view>
        <view class="right">
          <view class="t-title">{{ t.title }}</view>
          <view class="t-sub">{{ Math.ceil((t.durationSec||0)/60) }}分钟</view>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
import { getSeries } from '@/api/series'
import { listTracks } from '@/api/track'

export default {
  data(){ return { id:null, series:null, tracks:[] } },
  async onLoad(query){
    this.id = Number(query.id)
    const [s, ts] = await Promise.all([
      getSeries(this.id),
      listTracks({ seriesId:this.id, status:0, orderByColumn:'order_index', isAsc:'asc', pageNum:1, pageSize:100 })
    ])
    this.series = s.data
    this.tracks = ts.rows || ts.data || []
  },
  methods:{
    play(id){ uni.navigateTo({ url:`/pages/player/index?id=${id}&seriesId=${this.id}` }) },
    oss(id){ return id? `${this.$baseUrl}/system/oss/download/${id}`: '' }
  }
}
</script>

<style lang="scss" scoped>
.header{ padding:20rpx; }
.title{ font-size:34rpx; font-weight:600 }
.sub{ display:block; color:#888; margin-top:6rpx }
.banner{ width:100%; height:340rpx; border-radius:16rpx }
.intro{ padding:16rpx 20rpx; color:#555 }
.list{ padding:0 12rpx }
.item{ display:flex; gap:12rpx; background:#fff; border-radius:12rpx; padding:12rpx; margin:12rpx 0 }
.cover{ width:160rpx; height:120rpx; border-radius:8rpx }
.t-title{ font-size:28rpx }
.t-sub{ color:#888; font-size:22rpx }
</style>


