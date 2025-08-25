import { get } from '@/utils/request'

// 常见问题
export const listFaq = (params) => get('/meditation/faq/list', params)
export const getFaq = (id) => get(`/meditation/faq/${id}`)

export default { 
  listFaq, 
  getFaq
}