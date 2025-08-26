import { get, post } from '@/utils/request'
import { getMockTracks } from '@/utils/mockData'
import { USE_MOCK_DATA, mockConfig } from '@/common/config.mock'

// 冥想单集
export const listTracks = async (params) => {
  if (USE_MOCK_DATA) {
    if (mockConfig.showLogs) {
      console.log('[Mock] listTracks called with:', params)
    }
    // 模拟网络延迟
    if (mockConfig.delay > 0) {
      await new Promise(resolve => setTimeout(resolve, mockConfig.delay))
    }
    const result = getMockTracks(params.categoryId, params.pageNum, params.pageSize)
    if (mockConfig.showLogs) {
      console.log('[Mock] listTracks response:', result)
    }
    return result
  }
  return get('/meditation/track/list', params)
}
export const getTrack = (id) => get(`/meditation/track/${id}`)
export const getTrackDetail = (id) => get(`/meditation/track/${id}`)

// 记录播放
export const recordPlay = (trackId) => post('/meditation/playHistory', { trackId })

export default { 
  listTracks, 
  getTrack,
  getTrackDetail,
  recordPlay
}


