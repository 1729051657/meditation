import { get } from '@/utils/request'
import { getMockSeries } from '@/utils/mockData'
import { USE_MOCK_DATA, mockConfig } from '@/common/config.mock'

// 冥想系列
export const listSeries = async (params) => {
  if (USE_MOCK_DATA) {
    if (mockConfig.showLogs) {
      console.log('[Mock] listSeries called with:', params)
    }
    // 模拟网络延迟
    if (mockConfig.delay > 0) {
      await new Promise(resolve => setTimeout(resolve, mockConfig.delay))
    }
    const result = getMockSeries(params.categoryId, params.pageNum, params.pageSize)
    if (mockConfig.showLogs) {
      console.log('[Mock] listSeries response:', result)
    }
    return result
  }
  return get('/meditation/series/list', params)
}

export const getSeries = (id) => get(`/meditation/series/${id}`)

export default { listSeries, getSeries }


