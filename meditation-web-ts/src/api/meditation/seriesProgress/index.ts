import request from '@/utils/request';
import { AxiosPromise } from 'axios';
import { SeriesProgressVO, SeriesProgressForm, SeriesProgressQuery } from '@/api/meditation/seriesProgress/types';

/**
 * 查询系列学习进度列表
 * @param query
 * @returns {*}
 */

export const listSeriesProgress = (query?: SeriesProgressQuery): AxiosPromise<SeriesProgressVO[]> => {
  return request({
    url: '/meditation/seriesProgress/list',
    method: 'get',
    params: query
  });
};

/**
 * 查询系列学习进度详细
 * @param id
 */
export const getSeriesProgress = (id: string | number): AxiosPromise<SeriesProgressVO> => {
  return request({
    url: '/meditation/seriesProgress/' + id,
    method: 'get'
  });
};

/**
 * 新增系列学习进度
 * @param data
 */
export const addSeriesProgress = (data: SeriesProgressForm) => {
  return request({
    url: '/meditation/seriesProgress',
    method: 'post',
    data: data
  });
};

/**
 * 修改系列学习进度
 * @param data
 */
export const updateSeriesProgress = (data: SeriesProgressForm) => {
  return request({
    url: '/meditation/seriesProgress',
    method: 'put',
    data: data
  });
};

/**
 * 删除系列学习进度
 * @param id
 */
export const delSeriesProgress = (id: string | number | Array<string | number>) => {
  return request({
    url: '/meditation/seriesProgress/' + id,
    method: 'delete'
  });
};
