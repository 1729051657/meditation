import { get } from '@/utils/request'

// 冥想单集
export const listTracks = (params) => get('/meditation/track/list', params)
export const getTrack = (id) => get(`/meditation/track/${id}`)

export default { listTracks, getTrack }


