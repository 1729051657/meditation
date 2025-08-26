import { get } from '@/utils/request'
import { getMockCategories } from '@/utils/mockData'
import { USE_MOCK_DATA, mockConfig } from '@/common/config.mock'

// 冥想分类
export const listCategories = async (params) => {
  if (USE_MOCK_DATA) {
    if (mockConfig.showLogs) {
      console.log('[Mock] listCategories called with:', params)
    }
    // 模拟网络延迟
    if (mockConfig.delay > 0) {
      await new Promise(resolve => setTimeout(resolve, mockConfig.delay))
    }
    const result = getMockCategories()
    if (mockConfig.showLogs) {
      console.log('[Mock] listCategories response:', result)
    }
    return result
  }
  return get('/meditation/category/list', params)
}

export const getCategory = (id) => get(`/meditation/category/${id}`)

export default { listCategories, getCategory }


