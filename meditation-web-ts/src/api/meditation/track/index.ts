import request from '@/utils/request';
import { AxiosPromise } from 'axios';
import { TrackVO, TrackForm, TrackQuery } from '@/api/meditation/track/types';

/**
 * 查询冥想单集列表
 * @param query
 * @returns {*}
 */

export const listTrack = (query?: TrackQuery): AxiosPromise<TrackVO[]> => {
  return request({
    url: '/meditation/track/list',
    method: 'get',
    params: query
  });
};

/**
 * 查询冥想单集详细
 * @param id
 */
export const getTrack = (id: string | number): AxiosPromise<TrackVO> => {
  return request({
    url: '/meditation/track/' + id,
    method: 'get'
  });
};

/**
 * 新增冥想单集
 * @param data
 */
export const addTrack = (data: TrackForm) => {
  return request({
    url: '/meditation/track',
    method: 'post',
    data: data
  });
};

/**
 * 修改冥想单集
 * @param data
 */
export const updateTrack = (data: TrackForm) => {
  return request({
    url: '/meditation/track',
    method: 'put',
    data: data
  });
};

/**
 * 删除冥想单集
 * @param id
 */
export const delTrack = (id: string | number | Array<string | number>) => {
  return request({
    url: '/meditation/track/' + id,
    method: 'delete'
  });
};
