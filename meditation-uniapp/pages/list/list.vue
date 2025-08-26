<template>
    <!-- 头部 -->
    <view class="home-page">
        <!-- 导航栏 -->
        <tn-nav-bar :alpha="true">
        </tn-nav-bar>
        <view class="knowledge-section">
            <view class="knowledge-list">
                <view v-for="item in knowledgeItems" :key="item.id" class="knowledge-item" @click="goToKnowledge(item)">
                    <image :src="item.coverUrl || defaultKnowledgeCover" class="knowledge-image" mode="aspectFill"></image>
                    <view class="knowledge-content">
                        <text class="knowledge-title">{{ item.title }}</text>
                        <text class="knowledge-desc" v-if="item.summary">{{ item.summary }}</text>
                    </view>
                </view>
            </view>
        </view>
    </view>
</template>

<script>

import { getHomeData } from '@/api/home'
export default {
    data() {
        return {
            knowledgeItems: [], // 知识内容
        }
    },

    onLoad() {
        this.loadHomeData()

    },

    onPullDownRefresh() {

    },

    methods: {
        // 加载首页数据
        async loadHomeData() {
            try {
                const res = await getHomeData()

                if (res.code === 200 && res.data) {
                    const data = res.data
                    // 设置知识内容 - 使用 articeVoList 作为知识内容
                    this.knowledgeItems = data.articleVoList || []
                }
            } catch (error) {
                console.error('加载首页数据失败:', error)
                uni.showToast({
                    title: '加载失败',
                    icon: 'none'
                })
            }
        },
        // 跳转到知识详情
        goToKnowledge(item) {
            // 跳转到文章详情页
            uni.navigateTo({
                url: `/pages/article/detail?id=${item.id}`
            })
        },
    }
}
</script>

<style lang="scss" scoped>
.home-page {
    width: 100%;
    box-sizing: border-box;
    background-color: #D8E2F0;
    min-height: 100vh;
}

/* 冥想知识区域 */
.knowledge-section {
    margin-bottom: 40rpx;
    position: relative;
    z-index: 1;

    .knowledge-list {
        padding: 0 30rpx;
        display: flex;
        flex-direction: column;
        gap: 20rpx;

        .knowledge-item {
            display: flex;
            // background: #fff;
            border-radius: 16rpx;
            padding: 20rpx;
            // box-shadow: 0 2rpx 8rpx rgba(0, 0, 0, 0.06);

            .knowledge-image {
                width: 120rpx;
                height: 120rpx;
                border-radius: 12rpx;
                flex-shrink: 0;
                margin-right: 20rpx;
            }

            .knowledge-content {
                flex: 1;
                display: flex;
                flex-direction: column;
                justify-content: center;

                .knowledge-title {
                    font-size: 28rpx;
                    color: #333;
                    font-weight: 500;
                    margin-bottom: 12rpx;
                    display: -webkit-box;
                    -webkit-line-clamp: 2;
                    -webkit-box-orient: vertical;
                    overflow: hidden;
                    text-overflow: ellipsis;
                }

                .knowledge-desc {
                    font-size: 24rpx;
                    color: #999;
                    display: -webkit-box;
                    -webkit-line-clamp: 2;
                    -webkit-box-orient: vertical;
                    overflow: hidden;
                    text-overflow: ellipsis;
                }
            }
        }
    }
}
</style>