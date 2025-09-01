import { get, post, put } from '@/utils/request'

// 播放记录 - 基础接口
export const listPlayHistory = (params) => get('/meditation/playHistory/list', params)
export const addPlayHistory = (data) => post('/meditation/playHistory', data)
export const updatePlayHistory = (data) => put('/meditation/playHistory', data)
// 新增或更新播放记录（如果userId+trackId已存在则更新，否则新增）
export const upsertPlayHistory = (data) => post('/meditation/playHistory/upsert', data, false)

// 播放记录 - 详情接口（包含单集完整信息）
export const listPlayHistoryDetail = (params) => get('/meditation/playHistory/detail/list', params)

// 系列进度
export const listSeriesProgress = (params) => get('/meditation/seriesProgress/list', params)
export const updateSeriesProgress = (data) => put('/meditation/seriesProgress', data)

export default {
  listPlayHistory,
  listPlayHistoryDetail,
  addPlayHistory,
  updatePlayHistory,
  upsertPlayHistory,
  listSeriesProgress,
  updateSeriesProgress
}


