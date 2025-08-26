import { get } from '@/utils/request'

// 冥想系列
export const listSeries = (params) => get('/meditation/series/list', params)

export const getSeries = (id) => get(`/meditation/series/${id}`)

export default { listSeries, getSeries }


