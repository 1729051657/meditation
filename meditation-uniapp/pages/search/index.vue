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

		<!-- 标签页切换 -->
		<view class="tabs" v-if="hasSearchResults">
			<view class="tab" :class="{ active: tab === 'track' }" @click="tab = 'track'">
				音频 ({{ tracks.length }})
			</view>
			<view class="tab" :class="{ active: tab === 'series' }" @click="tab = 'series'">
				系列 ({{ series.length }})
			</view>
			<view class="tab" :class="{ active: tab === 'article' }" @click="tab = 'article'">
				文章 ({{ articles.length }})
			</view>
		</view>

		<scroll-view class="list" scroll-y>
			<!-- 音频卡片列表 -->
			<view class="card-grid" v-if="tab === 'track' && tracks.length > 0">
				<view class="card-item" v-for="t in tracks" :key="t.id" @click="goTrack(t.id)">
					<image :src="oss(t.coverUrl)" class="card-image" mode="aspectFill" />
					<view class="duration-tag">{{ Math.ceil((t.durationSec || 0) / 60) }}分钟</view>
					<view class="card-info">
						<text class="card-title">{{ t.title }}</text>
						<view class="card-meta">
							<text class="card-subtitle" v-if="t.subtitle">{{ t.subtitle }}</text>
						</view>
					</view>
				</view>
			</view>
			
			<!-- 系列卡片列表 -->
			<view class="card-grid" v-else-if="tab === 'series' && series.length > 0">
				<view class="card-item" v-for="s in series" :key="s.id" @click="goSeries(s.id)">
					<image :src="oss(s.coverUrl)" class="card-image" mode="aspectFill" />
					<view class="series-tag">系列 · {{ s.episodeCount }}集</view>
					<view class="card-info">
						<text class="card-title">{{ s.title }}</text>
						<view class="card-meta">
							<text class="card-subtitle" v-if="s.description">{{ s.description }}</text>
						</view>
					</view>
				</view>
			</view>
			
			<!-- 文章卡片列表 -->
			<view class="card-grid" v-else-if="tab === 'article' && articles.length > 0">
				<view class="card-item" v-for="a in articles" :key="a.id" @click="goArticle(a.id)">
					<image :src="oss(a.coverUrl)" class="card-image" mode="aspectFill" />
					<view class="article-tag">文章</view>
					<view class="card-info">
						<text class="card-title">{{ a.title }}</text>
						<view class="card-meta">
							<text class="card-subtitle" v-if="a.summary">{{ a.summary }}</text>
						</view>
					</view>
				</view>
			</view>
			
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
				// 直接跳转到播放器，播放系列的第一个音频
				uni.navigateTo({
					url: `/pages/player/index?seriesId=${id}&type=series`
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
		background: #fff;
		margin: 20rpx 20rpx 0;
		border-radius: 16rpx;
		padding: 8rpx;
	}

	.tab {
		flex: 1;
		text-align: center;
		padding: 20rpx 0;
		border-radius: 12rpx;
		font-size: 28rpx;
		color: #666;
		transition: all 0.3s;
	}

	.tab.active {
		background: linear-gradient(135deg, #7C3AED, #A855F7);
		color: #fff;
		font-weight: 500;
	}

	.list {
		height: calc(100vh - 300rpx);
		padding: 20rpx;
	}

	// 卡片网格布局
	.card-grid {
		display: flex;
		flex-wrap: wrap;
		gap: 20rpx;
		padding-bottom: 40rpx;
	}

	.card-item {
		width: calc(50% - 10rpx);
		background: #fff;
		border-radius: 20rpx;
		overflow: hidden;
		box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.08);
		transition: all 0.3s ease;
		position: relative;

		&:active {
			transform: scale(0.98);
			box-shadow: 0 2rpx 8rpx rgba(0, 0, 0, 0.12);
		}

		.card-image {
			width: 100%;
			height: 340rpx;
			display: block;
		}

		// 时长标签
		.duration-tag {
			position: absolute;
			top: 16rpx;
			right: 16rpx;
			background: rgba(0, 0, 0, 0.6);
			color: #fff;
			padding: 8rpx 16rpx;
			border-radius: 20rpx;
			font-size: 24rpx;
			backdrop-filter: blur(10rpx);
		}

		// 系列标签
		.series-tag {
			position: absolute;
			top: 16rpx;
			right: 16rpx;
			background: linear-gradient(135deg, #7C3AED, #A855F7);
			color: #fff;
			padding: 8rpx 16rpx;
			border-radius: 20rpx;
			font-size: 24rpx;
		}

		// 文章标签
		.article-tag {
			position: absolute;
			top: 16rpx;
			right: 16rpx;
			background: linear-gradient(135deg, #10B981, #34D399);
			color: #fff;
			padding: 8rpx 16rpx;
			border-radius: 20rpx;
			font-size: 24rpx;
		}

		.card-info {
			padding: 20rpx;

			.card-title {
				font-size: 28rpx;
				color: #333;
				font-weight: 500;
				display: block;
				overflow: hidden;
				text-overflow: ellipsis;
				white-space: nowrap;
				margin-bottom: 8rpx;
			}

			.card-meta {
				.card-subtitle {
					font-size: 24rpx;
					color: #999;
					display: block;
					overflow: hidden;
					text-overflow: ellipsis;
					display: -webkit-box;
					-webkit-line-clamp: 2;
					-webkit-box-orient: vertical;
					line-height: 1.4;
				}
			}
		}
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