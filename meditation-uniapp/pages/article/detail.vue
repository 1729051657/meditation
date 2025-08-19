<template>
  <view class="article-detail">
    <text class="title">{{ article && article.title ? article.title : '' }}</text>
    <image class="cover" :src="oss(article && article.cover ? article.cover : '')" mode="aspectFill" />
    <rich-text :nodes="article && article.content ? article.content : ''"></rich-text>
  </view>
</template>

<script>
import { getArticle } from '@/api/article'

export default {
  data(){ return { id:null, article:null } },
  async onLoad(query){
    this.id = Number(query.id)
    const res = await getArticle(this.id)
    this.article = res.data
  },
  methods:{ oss(id){ return id? `${this.$baseUrl}/system/oss/download/${id}`: '' } }
}
</script>

<style lang="scss" scoped>
.title{ font-size:34rpx; font-weight:600; padding:20rpx }
.cover{ width:92%; margin:0 4%; height:360rpx; border-radius:16rpx }
</style>


