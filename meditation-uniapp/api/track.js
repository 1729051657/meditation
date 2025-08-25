import { get, post } from '@/utils/request'

// 冥想单集
export const listTracks = (params) => get('/meditation/track/list', params)
export const getAllTracks = (params) => get('/meditation/track/all', params)
export const getTrack = (id) => get(`/meditation/track/${id}`)
export const getTrackDetail = (id) => get(`/meditation/track/${id}`)

// 记录播放
export const recordPlay = (trackId) => post('/meditation/playHistory', { trackId })

export default { 
  listTracks,
  getAllTracks, 
  getTrack,
  getTrackDetail,
  recordPlay
}


