import request from '@/utils/request';
import { AxiosPromise } from 'axios';
import { RecommendSlotVO, RecommendSlotForm, RecommendSlotQuery } from '@/api/meditation/recommendSlot/types';

/**
 * 查询推荐位列表
 * @param query
 * @returns {*}
 */

export const listRecommendSlot = (query?: RecommendSlotQuery): AxiosPromise<RecommendSlotVO[]> => {
  return request({
    url: '/meditation/recommendSlot/list',
    method: 'get',
    params: query
  });
};

/**
 * 查询推荐位详细
 * @param id
 */
export const getRecommendSlot = (id: string | number): AxiosPromise<RecommendSlotVO> => {
  return request({
    url: '/meditation/recommendSlot/' + id,
    method: 'get'
  });
};

/**
 * 新增推荐位
 * @param data
 */
export const addRecommendSlot = (data: RecommendSlotForm) => {
  return request({
    url: '/meditation/recommendSlot',
    method: 'post',
    data: data
  });
};

/**
 * 修改推荐位
 * @param data
 */
export const updateRecommendSlot = (data: RecommendSlotForm) => {
  return request({
    url: '/meditation/recommendSlot',
    method: 'put',
    data: data
  });
};

/**
 * 删除推荐位
 * @param id
 */
export const delRecommendSlot = (id: string | number | Array<string | number>) => {
  return request({
    url: '/meditation/recommendSlot/' + id,
    method: 'delete'
  });
};
