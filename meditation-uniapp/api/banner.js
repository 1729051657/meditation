import { get } from '@/utils/request'

// 横幅
export const listBanners = (params) => get('/meditation/banner/list', params)

export default { listBanners }


