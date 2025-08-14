import request from '@/utils/request';
import { AxiosPromise } from 'axios';
import { RecommendItemVO, RecommendItemForm, RecommendItemQuery } from '@/api/meditation/recommendItem/types';

/**
 * 查询推荐位内容列表
 * @param query
 * @returns {*}
 */

export const listRecommendItem = (query?: RecommendItemQuery): AxiosPromise<RecommendItemVO[]> => {
  return request({
    url: '/meditation/recommendItem/list',
    method: 'get',
    params: query
  });
};

/**
 * 查询推荐位内容详细
 * @param id
 */
export const getRecommendItem = (id: string | number): AxiosPromise<RecommendItemVO> => {
  return request({
    url: '/meditation/recommendItem/' + id,
    method: 'get'
  });
};

/**
 * 新增推荐位内容
 * @param data
 */
export const addRecommendItem = (data: RecommendItemForm) => {
  return request({
    url: '/meditation/recommendItem',
    method: 'post',
    data: data
  });
};

/**
 * 修改推荐位内容
 * @param data
 */
export const updateRecommendItem = (data: RecommendItemForm) => {
  return request({
    url: '/meditation/recommendItem',
    method: 'put',
    data: data
  });
};

/**
 * 删除推荐位内容
 * @param id
 */
export const delRecommendItem = (id: string | number | Array<string | number>) => {
  return request({
    url: '/meditation/recommendItem/' + id,
    method: 'delete'
  });
};
