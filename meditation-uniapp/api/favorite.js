import { get, post, del } from '@/utils/request'

// 收藏
export const listFavorites = (params) => get('/meditation/favorite/list', params)
export const addFavorite = (data) => post('/meditation/favorite', data)
export const removeFavorite = (ids) => del(`/meditation/favorite/${ids}`)

export default { listFavorites, addFavorite, removeFavorite }


