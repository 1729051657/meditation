import { get } from '@/utils/request'

// 冥想知识文章
export const listArticles = (params) => get('/meditation/article/list', params)
export const getArticle = (id) => get(`/meditation/article/${id}`)

export default { listArticles, getArticle }


