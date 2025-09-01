import request from '@/utils/request';
import { AxiosPromise } from 'axios';
import { PlayHistoryVO, PlayHistoryForm, PlayHistoryQuery } from '@/api/meditation/playHistory/types';

/**
 * 查询音频播放记录列表
 * @param query
 * @returns {*}
 */

export const listPlayHistory = (query?: PlayHistoryQuery): AxiosPromise<PlayHistoryVO[]> => {
  return request({
    url: '/meditation/playHistory/list',
    method: 'get',
    params: query
  });
};

/**
 * 查询音频播放记录详细
 * @param id
 */
export const getPlayHistory = (id: string | number): AxiosPromise<PlayHistoryVO> => {
  return request({
    url: '/meditation/playHistory/' + id,
    method: 'get'
  });
};

/**
 * 新增音频播放记录
 * @param data
 */
export const addPlayHistory = (data: PlayHistoryForm) => {
  return request({
    url: '/meditation/playHistory',
    method: 'post',
    data: data
  });
};

/**
 * 修改音频播放记录
 * @param data
 */
export const updatePlayHistory = (data: PlayHistoryForm) => {
  return request({
    url: '/meditation/playHistory',
    method: 'put',
    data: data
  });
};

/**
 * 新增或更新音频播放记录
 * 如果userId+trackId已存在，则更新记录（包括最后播放时间）
 * 如果不存在，则新增记录
 * @param data
 */
export const upsertPlayHistory = (data: PlayHistoryForm) => {
  return request({
    url: '/meditation/playHistory/upsert',
    method: 'post',
    data: data
  });
};

/**
 * 删除音频播放记录
 * @param id
 */
export const delPlayHistory = (id: string | number | Array<string | number>) => {
  return request({
    url: '/meditation/playHistory/' + id,
    method: 'delete'
  });
};
