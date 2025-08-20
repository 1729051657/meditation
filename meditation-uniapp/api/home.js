import { get } from '@/utils/request'

// 获取首页数据
export const getHomeData = () => get('/meditation/home/data')

export default { getHomeData }
