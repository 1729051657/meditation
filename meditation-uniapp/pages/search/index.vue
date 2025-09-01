<template>
	<view class="mg-search">
		<!-- 导航栏 -->
		<tn-nav-bar :alpha="true">
		</tn-nav-bar>
		<view class="search-bar">
			<input class="ipt" v-model="kw" placeholder="搜索单集" placeholder-color="#ADBCD1;" @confirm="doSearch" />
			<view class="search-btn" @click="doSearch">
				<tn-icon name="search" size="32" color="#FFFFFF"></tn-icon>
			</view>
		</view>

		<!-- 搜索历史和热门搜索 - 只在没有搜索结果时显示 -->
		<view v-if="!hasSearchResults">
			<!-- 搜索历史 -->
			<view class="history" v-if="searchHistory.length > 0">
				<view class="history-header">
					<view class="one">历史记录</view>
					<view class="clear-btn" @click="clearHistory">
						<tn-icon name="delete" size="36" color="#999"></tn-icon>
					</view>
				</view>
				<view class="onelist">
					<view class="p10" v-for="(item, index) in searchHistory" :key="index" @click="useHistory(item.keyword)">
						{{ item.keyword }}
					</view>
				</view>
			</view>

			<!-- 热门搜索 -->
			<view class="history hot">
				<view class="one">热门搜索</view>
				<view class="hot-grid" v-if="hot.length">
					<view class="hot-item" v-for="(h, index) in hot" :key="h.id" @click="useHot(h.keyword)">
						<text class="hot-rank" :class="{'top-three': index < 3}">{{ index + 1 }}</text>
						<text class="hot-text">{{ h.keyword }}</text>
					</view>
				</view>
			</view>
		</view>

		<!-- 搜索结果列表 - 只显示单集 -->
		<scroll-view class="list" scroll-y v-if="hasSearchResults || hasSearched">
			<view class="result-header" v-if="hasSearchResults">
				<text class="result-count">找到 {{ tracks.length }} 个单集</text>
			</view>
			<view class="item" v-for="t in tracks" :key="t.id" @click="goTrack(t.id)">
				<view class="cover-wrapper">
					<image
						:src="t.coverUrl"
						class="cover"
						mode="aspectFill"
						@error="onImageError($event, t)"
						@load="onImageLoad($event, t)"
						:class="{ 'cover-loaded': t.imageLoaded }"
					/>
					<!-- 默认封面 -->
					<view class="cover-placeholder" v-if="!t.coverUrl || !t.imageLoaded">
						<tn-icon name="music" size="48" color="#999"></tn-icon>
					</view>
					<!-- 加载状态 -->
					<view class="cover-loading" v-if="t.coverUrl && !t.imageLoaded && !t.imageError">
						<tn-icon name="loading" size="32" color="#999"></tn-icon>
					</view>
				</view>
				<view class="item-content">
					<view class="title">{{ t.title }}</view>
					<view class="sub">
						<tn-icon name="time" size="24" color="#999"></tn-icon>
						<text>{{ Math.ceil((t.durationSec || 0) / 60) }}分钟</text>
					</view>
				</view>
				<tn-icon name="right" size="32" color="#999"></tn-icon>
			</view>

			<!-- 空状态 -->
			<view class="empty-state" v-if="hasSearched && !hasSearchResults">
				<tn-icon name="search" size="320" color="#E0E0E0"></tn-icon>
				<text class="empty-text">没有找到相关单集</text>
				<text class="empty-hint">换个关键词试试吧</text>
			</view>
		</scroll-view>
	</view>
</template>

<script>
	import {
		listHot,
		listHistory,
		addHistory,
		clearMyHistory
	} from '@/api/search'
	import { listTracks } from '@/api/track'

	export default {
		data() {
			return {
				kw: '',
				hot: [],
				searchHistory: [],
				tracks: [],
				hasSearched: false,
				loading: false,
				hasLoaded: false // 标记是否已经加载过，避免重复调用
			}
		},
		computed: {
			hasSearchResults() {
				return this.tracks.length > 0
			}
		},
		async onLoad() {
			await this.loadInitialData()
		},
		async onShow() {
			// 只有在不是首次加载时才刷新搜索历史（避免重复调用）
			if (this.hasLoaded) {
				await this.loadSearchHistory()
			}
			this.hasLoaded = true
		},
		methods: {
			async loadInitialData() {
				try {
					// 并行加载热门搜索和搜索历史
					const [hotRes, historyRes] = await Promise.all([
						listHot({ status: 0, pageSize: 10 }),
						listHistory({ pageSize: 10 })
					])

					this.hot = hotRes.rows || hotRes.data || []
					this.searchHistory = historyRes.rows || historyRes.data || []
				} catch (error) {
					console.error('加载初始数据失败:', error)
				}
			},

			async loadSearchHistory() {
				try {
					const res = await listHistory({ pageSize: 10 })
					this.searchHistory = res.rows || res.data || []
				} catch (error) {
					console.error('加载搜索历史失败:', error)
				}
			},

						async doSearch() {
				if (!this.kw.trim()) {
					uni.showToast({
						title: '请输入搜索关键词',
						icon: 'none'
					})
					return
				}

				this.loading = true
				this.hasSearched = true

				try {
					// 使用listTracks接口搜索单集
					const res = await listTracks({
						title: this.kw.trim(),
						status: 0,
						pageNum: 1,
						pageSize: 50
					})

					this.tracks = (res.rows || res.data || []).map(track => ({
						...track,
						imageLoaded: false,
						imageError: false
					}))
				} catch (error) {
					console.error('搜索失败:', error)
					uni.showToast({
						title: '搜索失败，请重试',
						icon: 'none'
					})
				} finally {
					this.loading = false
				}

				// 搜索完成后，异步添加到搜索历史（不显示loading）
				try {
					await addHistory({ keyword: this.kw.trim() })
					
					// 检查是否已有相同关键词的搜索记录，如果没有才更新历史记录
					const hasExistingKeyword = this.searchHistory.some(item => item.keyword === this.kw.trim())
					if (!hasExistingKeyword) {
						// 搜索成功后更新历史记录
						await this.loadSearchHistory()
					}
				} catch (err) {
					console.error('添加搜索历史失败:', err)
				}
			},

			useHot(keyword) {
				this.kw = keyword
				this.doSearch()
			},

			useHistory(keyword) {
				this.kw = keyword
				this.doSearch()
			},

			clearHistory() {
			console.log(111)
				uni.showModal({
					title: '提示',
					content: '确定要清空搜索历史吗？',
					success: (res) => {
						if (res.confirm) {
							try {
								// 调用清空当前用户历史记录的接口
								clearMyHistory()
								this.searchHistory = []
								uni.showToast({
									title: '已清空历史记录',
									icon: 'success'
								})
							} catch (error) {
								console.error('清空历史记录失败:', error)
								uni.showToast({
									title: '操作失败',
									icon: 'none'
								})
							}
						}
					}
				})
			},

			goTrack(id) {
				uni.navigateTo({
					url: `/pages/player/index?id=${id}`
				})
			},

			// 图片加载成功
			onImageLoad(e, track) {
				// 设置图片加载成功状态
				this.$set(track, 'imageLoaded', true)
				this.$set(track, 'imageError', false)
			},

			// 图片加载失败
			onImageError(e, track) {
				// 设置图片加载失败状态
				this.$set(track, 'imageLoaded', false)
				this.$set(track, 'imageError', true)
				console.log('图片加载失败:', track.title, e)
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
		gap: 20rpx;
		padding: 20rpx 24rpx;
		align-items: center;
	}

	.ipt {
		flex: 1;
		background: #FFFFFF;
		border-radius: 48rpx;
		padding: 0 32rpx;
		height: 88rpx;
		opacity: 0.9;
		font-size: 28rpx;
		color: #333;
	}

	.search-btn {
		width: 88rpx;
		height: 88rpx;
		background: #79B1DA;
		border-radius: 48rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		transition: all 0.3s;

		&:active {
			background: #6A9BC4;
			transform: scale(0.95);
		}
	}

	input::-webkit-input-placeholder {
		color: #ADBCD1;
	}

	.history {
		margin-top: 48rpx;
		width: 100%;
		padding: 0 24rpx;
		box-sizing: border-box;
	}

	.history-header {
		display: flex;
		justify-content: space-between;
		align-items: center;
		margin-bottom: 24rpx;
	}

	.one {
		font-size: 32rpx;
		font-weight: 500;
		color: #333;
	}

	.clear-btn {
		padding: 8rpx;
		border-radius: 8rpx;
		transition: all 0.3s;
		display: flex;
		align-items: center;
		justify-content: center;

		&:active {
			background: rgba(0, 0, 0, 0.1);
			transform: scale(0.95);
		}
	}

	.onelist {
		display: flex;
		flex-wrap: wrap;
		gap: 16rpx;
		margin-top: 24rpx;
	}

	.p10 {
		padding: 16rpx 32rpx;
		background: #FFFFFF;
		border-radius: 48rpx;
		opacity: 0.9;
		font-weight: 400;
		font-size: 26rpx;
		color: #747F87;
		transition: all 0.3s;

		&:active {
			opacity: 0.7;
			transform: scale(0.95);
		}
	}

	.hot {
		margin-top: 60rpx;
	}

	.hot-grid {
		margin-top: 24rpx;
	}

	.hot-item {
		display: flex;
		align-items: center;
		padding: 20rpx 0;
		border-bottom: 1rpx solid rgba(255, 255, 255, 0.3);

		&:last-child {
			border-bottom: none;
		}

		&:active {
			background: rgba(255, 255, 255, 0.1);
		}
	}

	.hot-rank {
		width: 48rpx;
		text-align: center;
		font-size: 28rpx;
		color: #999;
		margin-right: 24rpx;

		&.top-three {
			color: #FF6B6B;
			font-weight: bold;
		}
	}

	.hot-text {
		font-size: 28rpx;
		color: #333;
	}

	.list {
		height: calc(100vh - 220rpx);
		padding: 0 20rpx;
	}

	.result-header {
		padding: 20rpx 0;
	}

	.result-count {
		font-size: 24rpx;
		color: #666;
	}

	.item {
		display: flex;
		align-items: center;
		background: #fff;
		border-radius: 16rpx;
		padding: 20rpx;
		margin-bottom: 16rpx;
		box-shadow: 0 2rpx 8rpx rgba(0, 0, 0, 0.05);
		transition: all 0.3s;

		&:active {
			transform: scale(0.98);
			box-shadow: 0 1rpx 4rpx rgba(0, 0, 0, 0.1);
		}
	}

	.cover-wrapper {
		position: relative;
		width: 120rpx;
		height: 120rpx;
		margin-right: 20rpx;
		border-radius: 12rpx;
		overflow: hidden;
		background: #f5f5f5;
	}

	.cover {
		width: 100%;
		height: 100%;
		border-radius: 12rpx;
		transition: all 0.3s;
	}

	.cover-placeholder {
		position: absolute;
		top: 0;
		left: 0;
		width: 100%;
		height: 100%;
		display: flex;
		align-items: center;
		justify-content: center;
		background: #f0f0f0;
		border-radius: 12rpx;
	}

	.cover-loading {
		position: absolute;
		top: 0;
		left: 0;
		width: 100%;
		height: 100%;
		display: flex;
		align-items: center;
		justify-content: center;
		background: rgba(245, 245, 245, 0.8);
		border-radius: 12rpx;
	}

	.cover-loaded {
		opacity: 1;
		transform: scale(1);
	}

	.cover:not(.cover-loaded) {
		opacity: 0;
		transform: scale(0.9);
	}

	.item-content {
		flex: 1;
	}

	.title {
		font-size: 30rpx;
		color: #333;
		font-weight: 500;
		margin-bottom: 12rpx;
		overflow: hidden;
		text-overflow: ellipsis;
		display: -webkit-box;
		-webkit-line-clamp: 2;
		line-clamp: 2;
		-webkit-box-orient: vertical;
	}

	.sub {
		display: flex;
		align-items: center;
		gap: 8rpx;
		color: #999;
		font-size: 24rpx;
	}

	.empty-state {
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: center;
		padding-top: 150rpx;
	}

	.empty-text {
		display: block;
		margin-top: 40rpx;
		font-size: 32rpx;
		color: #666;
		font-weight: 500;
	}

	.empty-hint {
		display: block;
		margin-top: 16rpx;
		font-size: 26rpx;
		color: #999;
	}
</style>
