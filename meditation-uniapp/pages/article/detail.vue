<template>
  <view class="article-detail">
    <!-- 导航栏 -->
    <tn-nav-bar :alpha="true">
    </tn-nav-bar>
    <text class="title">{{ article && article.title ? article.title : '' }}</text>
    <image class="cover" :src="oss(article && article.cover ? article.cover : '')" mode="aspectFill" />
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

.cover {
  width: 92%;
  margin: 0 4%;
  height: 360rpx;
  border-radius: 16rpx
}

.p24 {
  width: 100%;
  box-sizing: border-box;
  padding: 0 24rpx;
}
</style>
