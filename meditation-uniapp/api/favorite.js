import { get, post, del } from '@/utils/request'

// 收藏 - 基础接口
export const listFavorites = (params) => get('/meditation/favorite/list', params)
export const addFavorite = (targetId, targetType = 'track') => post('/meditation/favorite', { targetId, targetType })
export const removeFavorite = (favoriteId, targetType = 'track') => del(`/meditation/favorite/${favoriteId}`)
export const checkFavorite = (targetId, targetType = 'track') => get('/meditation/favorite/check/status', { targetId, targetType })

// 收藏 - 详情接口（包含完整信息）
export const listFavoritesDetail = (params) => get('/meditation/favorite/detail/list', params)

export default { 
  listFavorites,
  listFavoritesDetail,
  addFavorite, 
  removeFavorite,
  checkFavorite
}


