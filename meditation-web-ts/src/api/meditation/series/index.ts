import request from '@/utils/request';
import { AxiosPromise } from 'axios';
import { SeriesVO, SeriesForm, SeriesQuery } from '@/api/meditation/series/types';

/**
 * 查询冥想系列列表
 * @param query
 * @returns {*}
 */

export const listSeries = (query?: SeriesQuery): AxiosPromise<SeriesVO[]> => {
  return request({
    url: '/meditation/series/list',
    method: 'get',
    params: query
  });
};

/**
 * 查询冥想系列详细
 * @param id
 */
export const getSeries = (id: string | number): AxiosPromise<SeriesVO> => {
  return request({
    url: '/meditation/series/' + id,
    method: 'get'
  });
};

/**
 * 新增冥想系列
 * @param data
 */
export const addSeries = (data: SeriesForm) => {
  return request({
    url: '/meditation/series',
    method: 'post',
    data: data
  });
};

/**
 * 修改冥想系列
 * @param data
 */
export const updateSeries = (data: SeriesForm) => {
  return request({
    url: '/meditation/series',
    method: 'put',
    data: data
  });
};

/**
 * 删除冥想系列
 * @param id
 */
export const delSeries = (id: string | number | Array<string | number>) => {
  return request({
    url: '/meditation/series/' + id,
    method: 'delete'
  });
};
