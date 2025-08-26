import { get, post, del } from '@/utils/request'
import { getTrack } from './track'
import { getSeries } from './series'
import { getArticle } from './article'

/**
 * 增强的收藏API - 包含详细信息的获取
 */

// 获取收藏列表（带详细信息）
export const listFavoritesWithDetails = async (params) => {
  try {
    // 获取收藏列表
    const res = await get('/meditation/favorite/list', params)
    
    if (res.code === 200 && res.data) {
      const { rows, total } = res.data
      
      // 获取每个收藏项的详细信息
      const detailedRows = await Promise.all(rows.map(async (item) => {
        let details = {}
        
        try {
          // 根据类型获取详细信息
          if (item.targetType === 'track') {
            const trackRes = await getTrack(item.targetId)
            if (trackRes.code === 200 && trackRes.data) {
              details = {
                ...item,
                trackId: item.targetId,
                trackTitle: trackRes.data.title,
                trackName: trackRes.data.title,
                author: trackRes.data.author || '未知作者',
                duration: trackRes.data.durationSec || 0,
                playCount: trackRes.data.playCount || 0,
                coverUrl: trackRes.data.coverUrl,
                trackCover: trackRes.data.coverUrl,
                audioUrl: trackRes.data.audioUrl || trackRes.data.audioId
              }
            }
          } else if (item.targetType === 'series') {
            const seriesRes = await getSeries(item.targetId)
            if (seriesRes.code === 200 && seriesRes.data) {
              details = {
                ...item,
                seriesId: item.targetId,
                seriesTitle: seriesRes.data.title,
                seriesName: seriesRes.data.title,
                author: seriesRes.data.author || '系列',
                episodeCount: seriesRes.data.episodeCount || 0,
                totalDuration: seriesRes.data.totalDuration || 0,
                playCount: seriesRes.data.playCount || 0,
                coverUrl: seriesRes.data.coverUrl,
                seriesCover: seriesRes.data.coverUrl
              }
            }
          } else if (item.targetType === 'article') {
            const articleRes = await getArticle(item.targetId)
            if (articleRes.code === 200 && articleRes.data) {
              details = {
                ...item,
                articleId: item.targetId,
                articleTitle: articleRes.data.title,
                articleName: articleRes.data.title,
                author: articleRes.data.author || '未知作者',
                summary: articleRes.data.summary,
                viewCount: articleRes.data.viewCount || 0,
                coverUrl: articleRes.data.coverUrl,
                articleCover: articleRes.data.coverUrl
              }
            }
          }
        } catch (error) {
          console.error(`获取收藏项详情失败 (${item.targetType} ${item.targetId}):`, error)
          // 如果获取详情失败，返回基本信息
          details = {
            ...item,
            trackTitle: '加载失败',
            trackName: '加载失败',
            author: '未知'
          }
        }
        
        return {
          ...item,
          ...details,
          favoriteId: item.id // 保存收藏记录ID用于删除
        }
      }))
      
      return {
        code: 200,
        data: {
          rows: detailedRows,
          total
        }
      }
    }
    
    return res
  } catch (error) {
    console.error('获取收藏列表失败:', error)
    throw error
  }
}

// 添加收藏（兼容多种参数格式）
export const addFavoriteEnhanced = (targetId, targetType = 'track') => {
  // 兼容不同的调用方式
  if (typeof targetId === 'object') {
    return post('/meditation/favorite', targetId)
  }
  return post('/meditation/favorite', { 
    targetId, 
    targetType 
  })
}

// 移除收藏（支持多种方式）
export const removeFavoriteEnhanced = async (favoriteIdOrTargetId, targetType) => {
  // 如果传入的是收藏记录ID
  if (!targetType) {
    return del(`/meditation/favorite/${favoriteIdOrTargetId}`)
  }
  
  // 如果传入的是目标ID和类型，需要先查询收藏记录ID
  try {
    const res = await get('/meditation/favorite/list', {
      targetId: favoriteIdOrTargetId,
      targetType: targetType,
      pageSize: 1
    })
    
    if (res.code === 200 && res.data && res.data.rows.length > 0) {
      const favoriteId = res.data.rows[0].id
      return del(`/meditation/favorite/${favoriteId}`)
    }
    
    return { code: 200, msg: '未找到收藏记录' }
  } catch (error) {
    console.error('移除收藏失败:', error)
    throw error
  }
}

// 检查收藏状态
export const checkFavoriteEnhanced = async (targetId, targetType = 'track') => {
  try {
    const res = await get('/meditation/favorite/list', {
      targetId,
      targetType,
      pageSize: 1
    })
    
    return {
      code: 200,
      data: res.code === 200 && res.data && res.data.rows.length > 0
    }
  } catch (error) {
    console.error('检查收藏状态失败:', error)
    return { code: 200, data: false }
  }
}

export default {
  listFavoritesWithDetails,
  addFavoriteEnhanced,
  removeFavoriteEnhanced,
  checkFavoriteEnhanced
}