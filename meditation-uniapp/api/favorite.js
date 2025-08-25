import { get, post, del } from '@/utils/request'

// 收藏
export const listFavorites = (params) => get('/meditation/favorite/list', params)
export const addFavorite = (trackId, type = 'track') => post('/meditation/favorite', { trackId, type })
export const removeFavorite = (trackId, type = 'track') => del(`/meditation/favorite/${trackId}`)
export const checkFavorite = (trackId, type = 'track') => get('/meditation/favorite/check', { trackId, type })

export default { 
  listFavorites, 
  addFavorite, 
  removeFavorite,
  checkFavorite
}


