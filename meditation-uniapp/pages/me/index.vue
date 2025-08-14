<template>
  <view class="me">
    <view class="header">
      <image class="avatar" :src="(user && user.avatarUrl) || '/static/images/default-avatar.png'" />
      <view class="info">
        <view class="name">{{ (user && user.nickName) || '未登录' }}</view>
        <view class="sub">{{ user && user.userName }}</view>
      </view>
    </view>

    <view class="cells">
      <view class="cell" @click="goFavorites">
        <text>我的收藏</text>
        <text class="arrow">›</text>
      </view>
      <view class="cell warn" @click="logout">退出登录</view>
    </view>
  </view>
</template>

<script>
import { getUserInfo, clearAuth } from '@/utils/auth'

export default {
  data(){ return { user: null } },
  onShow(){ this.user = getUserInfo() },
  methods:{
    goFavorites(){ uni.navigateTo({ url: '/pages/favorites/index' }) },
    logout(){ clearAuth(); uni.reLaunch({ url: '/pages/login/login' }) }
  }
}
</script>

<style lang="scss" scoped>
.header{ display:flex; gap:16rpx; padding:24rpx; background:#fff }
.avatar{ width:96rpx; height:96rpx; border-radius:50% }
.name{ font-size:30rpx; font-weight:600 }
.sub{ color:#888; font-size:24rpx }
.cells{ margin-top:16rpx; background:#fff }
.cell{ display:flex; justify-content:space-between; padding:28rpx 24rpx; border-bottom:1px solid #f0f0f0; font-size:28rpx }
.cell.warn{ color:#e11d48 }
.arrow{ color:#bbb }
</style>


