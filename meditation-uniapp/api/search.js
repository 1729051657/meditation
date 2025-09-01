import { get, post, del } from '@/utils/request'
import { listTracks } from './track'
import { listSeries } from './series'
import { listArticles } from './article'

// 搜索相关
export const listHistory = (params) => get('/meditation/searchHistory/list', params)
export const addHistory = (data) => post('/meditation/searchHistory', data, false)
export const removeHistory = (ids) => del(`/meditation/searchHistory/${ids}`)
export const clearMyHistory = () => del('/meditation/searchHistory/clear')
export const listHot = (params) => get('/meditation/hotKeyword/list', params)

export const searchTracks = (kw, extra = {}) => listTracks({ title: kw, status: 0, ...extra })
export const searchSeries = (kw, extra = {}) => listSeries({ title: kw, status: 0, ...extra })
export const searchArticles = (kw, extra = {}) => listArticles({ title: kw, status: 0, ...extra })

export default {
  listHistory,
  addHistory,
  removeHistory,
  clearMyHistory,
  listHot,
  searchTracks,
  searchSeries,
  searchArticles
}


