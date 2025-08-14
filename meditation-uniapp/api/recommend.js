import { get } from '@/utils/request'

// 推荐位与内容
export const listSlots = (params) => get('/meditation/recommendSlot/list', params)
export const listSlotItems = (params) => get('/meditation/recommendItem/list', params)

export default { listSlots, listSlotItems }


