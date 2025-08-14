<template>
  <view class="fav">
    <view class="tabs">
      <view :class="['tab', type==='track'?'active':'']" @click="switchType('track')">单集</view>
      <view :class="['tab', type==='series'?'active':'']" @click="switchType('series')">系列</view>
      <view :class="['tab', type==='article'?'active':'']" @click="switchType('article')">文章</view>
    </view>
    <scroll-view class="list" scroll-y @scrolltolower="loadMore">
      <view class="item" v-for="it in list" :key="it.id" @click="enter(it)">
        <image class="cover" :src="oss(it.cover)" />
        <view class="title">{{ it.title }}</view>
      </view>
      <view class="more" v-if="hasMore">下拉加载更多</view>
    </scroll-view>
  </view>
</template>

<script>
import { listFavorites } from '@/api/favorite'

export default {
  data(){ return { type:'track', pageNum:1, list:[], hasMore:true } },
  onLoad(){ this.fetch(true) },
  methods:{
    async fetch(reset=false){
      if(reset){ this.pageNum=1; this.list=[]; this.hasMore=true }
      if(!this.hasMore) return
      const res = await listFavorites({ targetType:this.type, pageNum:this.pageNum, pageSize:10 })
      const rows = res.rows || res.data || []
      this.list = this.list.concat(rows)
      this.hasMore = rows.length>=10
      if(this.hasMore) this.pageNum+=1
    },
    switchType(t){ this.type=t; this.fetch(true) },
    loadMore(){ this.fetch() },
    enter(it){
      if(this.type==='series') uni.navigateTo({ url:`/pages/series/detail?id=${it.targetId||it.id}` })
      else if(this.type==='track') uni.navigateTo({ url:`/pages/player/index?trackId=${it.targetId||it.id}` })
      else uni.navigateTo({ url:`/pages/article/detail?id=${it.targetId||it.id}` })
    },
    oss(id){ return id? `${this.$baseUrl}/system/oss/download/${id}`: '' }
  }
}
</script>

<style lang="scss" scoped>
.tabs{ display:flex }
.tab{ flex:1; text-align:center; padding:16rpx 0; border-bottom:4rpx solid transparent }
.tab.active{ border-color:#3b82f6; color:#3b82f6 }
.list{ height: calc(100vh - 100rpx); padding:12rpx 16rpx }
.item{ display:flex; gap:12rpx; background:#fff; padding:12rpx; border-radius:12rpx; margin-bottom:12rpx; align-items:center }
.cover{ width:120rpx; height:90rpx; border-radius:8rpx }
.title{ font-size:28rpx }
.more{ text-align:center; color:#888; padding:16rpx }
</style>


