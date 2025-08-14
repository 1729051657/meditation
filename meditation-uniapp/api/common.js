import { get } from '@/utils/request'

/**
 * 获取门店(部门)列表
 * 如果传入 tenantId，则根据租户过滤
 */
export function getDeptList(tenantId) {
  const params = {}
  if (tenantId) {
    params.tenantId = tenantId
  }
  return get('/auth/dept/list', params, false, false)
} 


// OSS相关接口
export function getOssUrls(ossIds) {
  return get(`/resource/oss/listByIds/${ossIds}`, {}, false)
}