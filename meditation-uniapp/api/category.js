import { get } from '@/utils/request'

// 冥想分类
export const listCategories = (params) => get('/meditation/category/list', params)

export const getCategory = (id) => get(`/meditation/category/${id}`)

export default { listCategories, getCategory }


