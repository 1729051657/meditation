import { get, post, put } from '@/utils/request'

// 播放记录
export const listPlayHistory = (params) => get('/meditation/playHistory/list', params)
export const addPlayHistory = (data) => post('/meditation/playHistory', data)
export const updatePlayHistory = (data) => put('/meditation/playHistory', data)

// 系列进度
export const listSeriesProgress = (params) => get('/meditation/seriesProgress/list', params)
export const updateSeriesProgress = (data) => put('/meditation/seriesProgress', data)

export default {
  listPlayHistory,
  addPlayHistory,
  updatePlayHistory,
  listSeriesProgress,
  updateSeriesProgress
}


