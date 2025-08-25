import { get, post, del } from '@/utils/request'

// 收藏 - 基础接口
export const listFavorites = (params) => get('/meditation/favorite/list', params)
export const addFavorite = (trackId, type = 'track') => post('/meditation/favorite', { trackId, type })
export const removeFavorite = (trackId, type = 'track') => del(`/meditation/favorite/${trackId}`)
export const checkFavorite = (trackId, type = 'track') => get(`/meditation/favorite/check?trackId=${trackId}&type=${type}`)

// 收藏 - 详情接口（包含完整信息）
export const listFavoritesDetail = (params) => get('/meditation/favorite/detail/list', params)

export default { 
  listFavorites,
  listFavoritesDetail,
  addFavorite, 
  removeFavorite,
  checkFavorite
}


