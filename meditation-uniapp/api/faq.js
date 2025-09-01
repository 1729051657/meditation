import { get } from '@/utils/request'

/**
 * 获取FAQ页面所有数据
 * 包含联系信息和问题列表
 */
export const getFaqData = () => get('/meditation/faq/data')

/**
 * 获取联系信息
 */
export const getContactInfo = () => get('/meditation/faq/contact')

/**
 * 获取FAQ问题列表
 */
export const getFaqQuestions = () => get('/meditation/faq/questions')

export default {
  getFaqData,
  getContactInfo,
  getFaqQuestions
}