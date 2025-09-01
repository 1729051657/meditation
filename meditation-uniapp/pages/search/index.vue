<template>
	<view class="mg-search">
		<!-- 导航栏 -->
		<tn-nav-bar :alpha="true">
		</tn-nav-bar>
		<view class="search-bar">
			<input class="ipt" v-model="kw" placeholder="搜索单集" placeholder-color="#ADBCD1;" @confirm="doSearch" />
			<tn-icon name="search" size="48" color="#79B1DA" @click="doSearch"></tn-icon>
		</view>

		<!-- 搜索历史和热门搜索 - 只在没有搜索结果时显示 -->
		<view v-if="!hasSearchResults">
			<!-- 搜索历史 -->
			<view class="history" v-if="searchHistory.length > 0">
				<view class="history-header">
					<view class="one">历史记录</view>
					<tn-icon name="delete" size="36" color="#999" @click="clearHistory"></tn-icon>
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
				<image :src="oss(t.coverUrl)" class="cover" mode="aspectFill" />
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
		removeHistory
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
				loading: false
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
			// 每次显示页面时刷新搜索历史
			await this.loadSearchHistory()
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
					// 添加到搜索历史
					await addHistory({ keyword: this.kw.trim() })
					
					// 使用listTracks接口搜索单集
					const res = await listTracks({
						title: this.kw.trim(),
						status: 0,
						pageNum: 1,
						pageSize: 50
					})
					
					this.tracks = res.rows || res.data || []
					
					// 搜索成功后更新历史记录
					await this.loadSearchHistory()
				} catch (error) {
					console.error('搜索失败:', error)
					uni.showToast({
						title: '搜索失败，请重试',
						icon: 'none'
					})
				} finally {
					this.loading = false
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
			
			async clearHistory() {
				uni.showModal({
					title: '提示',
					content: '确定要清空搜索历史吗？',
					success: async (res) => {
						if (res.confirm) {
							try {
								// 获取所有历史记录的ID
								const ids = this.searchHistory.map(item => item.id).join(',')
								if (ids) {
									await removeHistory(ids)
									this.searchHistory = []
									uni.showToast({
										title: '已清空历史记录',
										icon: 'success'
									})
								}
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

	.cover {
		width: 120rpx;
		height: 120rpx;
		border-radius: 12rpx;
		margin-right: 20rpx;
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