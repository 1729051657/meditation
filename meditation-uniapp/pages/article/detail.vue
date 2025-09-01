<template>
  <view class="article-detail">
    <!-- 导航栏 -->
    <tn-nav-bar :alpha="true" backgroundColor="#D8E2F0">
    </tn-nav-bar>
    <text class="title">{{ article && article.title ? article.title : '' }}</text>
    <view class="border">
      <image class="cover" :src="article && article.coverUrl ? article.coverUrl : ''" mode="widthFix" />
    </view>
    <view class="p24">
      <rich-text :nodes="article && article.content ? article.content : ''"></rich-text>
    </view>


  </view>
</template>

<script>
import { getArticle } from '@/api/article'

export default {
  data() { return { id: null, article: null } },
  async onLoad(query) {
    this.id = Number(query.id)
    const res = await getArticle(this.id)
    this.article = res.data
  },
  methods: { oss(id) { return id ? `${this.$baseUrl}/system/oss/download/${id}` : '' } }
}
</script>

<style lang="scss" scoped>
.article-detail {
  width: 100%;
  background: #D8E2F0;
  box-sizing: border-box;
  min-height: 100vh;
}

.title {
  font-size: 34rpx;
  font-weight: 600;
  padding: 20rpx
}
.border{
  padding:0 24rpx;
  box-sizing: border-box;
  margin-bottom: 26rpx;
  margin-top:24rpx;
}
.cover {
  width: 100%;

  border-radius: 16rpx;
  box-sizing: border-box;
}

.p24 {
  width: 100%;
  box-sizing: border-box;
  padding: 0 24rpx;
}
</style>
