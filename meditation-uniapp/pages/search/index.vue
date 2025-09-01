<template>
	<view class="mg-search">
		<!-- 导航栏 -->
		<tn-nav-bar :alpha="true">
		</tn-nav-bar>
		<view class="search-bar">
			<input class="ipt" v-model="kw" placeholder="输入关键词" placeholder-color="#ADBCD1;" @confirm="doSearch" />
			<!-- <button class="btn" @click="doSearch">搜索</button> -->
			<tn-icon name="search" size="128" color="#79B1DA"></tn-icon>
		</view>

		<!-- 搜索历史和热门搜索 - 只在没有搜索结果时显示 -->
		<view v-if="!hasSearchResults">
			<view class="history">
				<view class="one">
					历史记录
				</view>
				<view class="onelist">
					<view class="p10">
						减压
					</view>
					<view class="p10">
						睡眠
					</view>
					<view class="p10">
						专注
					</view>
				</view>
			</view>

			<view class="history hot">
				<view class="one">
					热门搜索
				</view>
				<view class="chips" v-if="hot.length">
					<view class="chip" v-for="h in hot" :key="h.id" @click="useHot(h.keyword)">{{ h.keyword }}</view>
				</view>
			</view>
		</view>

		<scroll-view class="list" scroll-y>
			<block v-if="tab === 'track'">
				<view class="item" v-for="t in tracks" :key="t.id" @click="goTrack(t.id)">
					<image :src="oss(t.coverUrl)" class="cover" />
					<view class="title">{{ t.title }}</view>
					<view class="sub">{{ Math.ceil((t.durationSec || 0) / 60) }}分钟</view>
				</view>
			</block>
			<block v-else-if="tab === 'series'">
				<view class="item" v-for="s in series" :key="s.id" @click="goSeries(s.id)">
					<image :src="oss(s.coverUrl)" class="cover" />
					<view class="title">{{ s.title }}</view>
					<view class="sub">系列 · {{ s.episodeCount }}</view>
				</view>
			</block>
			<block v-else>
				<view class="item" v-for="a in articles" :key="a.id" @click="goArticle(a.id)">
					<image :src="oss(a.coverUrl)" class="cover" />
					<view class="title">{{ a.title }}</view>
					<view class="sub">{{ a.summary }}</view>
				</view>
			</block>
			<!-- 空状态 -->
			<view class="top" v-if="hasSearched && !hasSearchResults">
				<tn-icon name="search" size="320" color="#E0E0E0"></tn-icon>
				<text class="empty-text">没有找到相关内容</text>
			</view>
		</scroll-view>


	</view>
</template>

<script>
	import {
		listHot,
		addHistory,
		searchTracks,
		searchSeries,
		searchArticles
	} from '@/api/search'

	export default {
		data() {
			return {
				kw: '',
				tab: 'track',
				hot: [],
				tracks: [],
				series: [],
				articles: [],
				hasSearched: false
			}
		},
		computed: {
			hasSearchResults() {
				return this.tracks.length > 0 || this.series.length > 0 || this.articles.length > 0
			}
		},
		async onLoad() {
			const res = await listHot({
				status: 0
			});
			this.hot = res.rows || res.data || []
			console.log(res, 'hh')
		},
		methods: {
			async doSearch() {
				if (!this.kw) return;
				this.hasSearched = true
				await addHistory({
					keyword: this.kw
				});
				await this.searchAll(this.kw)
			},
			async searchAll(kw) {
				const [ts, ss, as] = await Promise.all([
					searchTracks(kw, {
						pageNum: 1,
						pageSize: 20
					}),
					searchSeries(kw, {
						pageNum: 1,
						pageSize: 20
					}),
					searchArticles(kw, {
						pageNum: 1,
						pageSize: 20
					})
				])
				this.tracks = ts.rows || ts.data || []
				this.series = ss.rows || ss.data || []
				this.articles = as.rows || as.data || []
			},
			useHot(k) {
				this.kw = k;
				this.doSearch()
			},
			goTrack(id) {
				uni.navigateTo({
					url: `/pages/player/index?id=${id}`
				})
			},
			goSeries(id) {
				uni.navigateTo({
					url: `/pages/series/detail?id=${id}`
				})
			},
			goArticle(id) {
				uni.navigateTo({
					url: `/pages/article/detail?id=${id}`
				})
			},
			oss(id) {
				return id ? `${this.$baseUrl}/system/oss/download/${id}` : ''
			}
		}
	}
</script>

<style lang="scss" scoped>
	.mg-search {
		width: 750rpx;
		min-height: 100vh;
		background: #D8E2F0;
	}

	.search-bar {
		display: flex;
		gap: 12rpx;
		padding: 20rpx;
		align-items: center;
	}

	.ipt {
		flex: 1;
		background: #FFFFFF;

		border-radius: 48rpx;
		padding: 0 32rpx;
		height: 88rpx;
		opacity: 0.7;

	}

	input::-webkit-input-placeholder {
		color: #ADBCD1;
	}

	.btn {
		background: #3b82f6;
		color: #fff;
		border-radius: 12rpx;
		padding: 0 20rpx;
	}

	.chips {
		display: flex;
		gap: 12rpx;
		padding: 0 20rpx;
		flex-wrap: wrap;
	}

	.chip {
		background: #eef2f7;
		padding: 10rpx 18rpx;
		border-radius: 999rpx;
		font-size: 24rpx
	}

	.tabs {
		display: flex;
	}

	.tab {
		flex: 1;
		text-align: center;
		padding: 16rpx 0;
		border-bottom: 4rpx solid transparent;
	}

	.tab.active {
		border-color: #3b82f6;
		color: #3b82f6
	}

	.list {
		height: calc(100vh - 220rpx);
		padding: 12rpx 20rpx;
	}

	.item {
		display: flex;
		gap: 12rpx;
		background: #fff;
		border-radius: 12rpx;
		padding: 12rpx;
		margin-bottom: 12rpx;
		align-items: center
	}

	.cover {
		width: 140rpx;
		height: 100rpx;
		border-radius: 8rpx
	}

	.title {
		font-size: 28rpx;
		flex: 1
	}

	.sub {
		color: #888;
		font-size: 22rpx
	}

	.w64 {
		width: 64rpx;
		height: 64rpx;
	}

	.w320 {
		width: 320rpx;
		height: 320rpx;
	}

	.top {
		margin-top: 150rpx;
		text-align: center;
	}

	.empty-text {
		display: block;
		margin-top: 20rpx;
		font-size: 28rpx;
		color: #999;
	}

	.history {
		margin-top: 48rpx;
		width: 100%;
		padding: 0 24rpx;
		box-sizing: border-box;
	}

	.onelist {
		display: flex;
		align-items: center;
		margin-top: 24rpx;
	}

	.p10 {
		padding: 10rpx 28rpx;
		background: #FFFFFF;
		border-radius: 48rpx;
		opacity: 0.7;
		font-weight: 400;
		font-size: 26rpx;
		color: #747F87;
		margin-right: 16rpx;
	}
</style>