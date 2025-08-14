<template>
  <view class="mg-category">
    <view class="header">
      <text class="back" @click="goBack">〈</text>
      <text class="title">{{ category?.name || '分类' }}</text>
    </view>
    <view class="chips">
      <view class="chip" :class="{active: sub.id===currentSubId}" v-for="sub in subCats" :key="sub.id" @click="switchSub(sub.id)">{{ sub.name }}</view>
    </view>
    <view class="grid">
      <view class="card" v-for="t in tracks" :key="t.id" @click="play(t)">
        <image :src="oss(t.cover)" class="cover" mode="aspectFill" />
        <text class="t-title">{{ t.title }}</text>
        <text class="badge">{{ Math.ceil((t.durationSec||0)/60) }}分钟</text>
      </view>
    </view>
    <view class="load-more" v-if="hasMore" @click="loadMore">加载更多</view>
  </view>
</template>

<script>
import { listCategories, getCategory } from '@/api/category'
import { listTracks } from '@/api/track'

export default {
  data(){
    return { categoryId: null, category: null, subCats: [], currentSubId: null, tracks: [], pageNum: 1, hasMore: true }
  },
  async onLoad(query){
    this.categoryId = Number(query.categoryId)
    const [cat, subs] = await Promise.all([
      getCategory(this.categoryId),
      listCategories({ parentId: this.categoryId, status: 0 })
    ])
    this.category = (cat.data) || null
    this.subCats = subs.rows || subs.data || []
    this.currentSubId = this.categoryId
    this.loadList(true)
  },
  methods:{
    async loadList(reset=false){
      if(reset){ this.pageNum=1; this.tracks=[]; this.hasMore=true }
      if(!this.hasMore) return
      const res = await listTracks({ categoryId: this.currentSubId, status:0, pageNum:this.pageNum, pageSize:10 })
      const rows = res.rows || res.data || []
      this.tracks = this.tracks.concat(rows)
      this.hasMore = rows.length>=10
      if(this.hasMore) this.pageNum+=1
    },
    switchSub(id){ this.currentSubId=id; this.loadList(true) },
    play(t){ uni.navigateTo({ url:`/pages/player/index?trackId=${t.id}` }) },
    goBack(){ uni.navigateBack() },
    oss(id){ return id? `${this.$baseUrl}/system/oss/download/${id}`: '' }
  },
  onReachBottom(){ this.loadList() },
  onPullDownRefresh(){ this.loadList(true); uni.stopPullDownRefresh() }
}
</script>

<style lang="scss" scoped>
.header{ display:flex; align-items:center; gap:12rpx; padding:20rpx; }
.title{ font-size:32rpx; font-weight:600; }
.chips{ display:flex; gap:12rpx; padding:0 20rpx 12rpx; flex-wrap:wrap; }
.chip{ background:#eef2f7; padding:10rpx 18rpx; border-radius:999rpx; font-size:26rpx; color:#333; }
.chip.active{ background:#dbeafe; color:#1d4ed8 }
.grid{ display:grid; grid-template-columns: repeat(2, 1fr); gap:16rpx; padding:0 20rpx; }
.card{ background:#fff; border-radius:12rpx; overflow:hidden; position:relative; }
.cover{ width:100%; height:260rpx; }
.t-title{ display:block; padding:10rpx 12rpx 22rpx; font-size:26rpx; }
.badge{ position:absolute; left:12rpx; top:12rpx; background:rgba(0,0,0,.55); color:#fff; font-size:22rpx; padding:4rpx 10rpx; border-radius:8rpx; }
.load-more{ text-align:center; color:#666; padding:20rpx }
</style>


