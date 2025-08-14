import request from '@/utils/request';
import { AxiosPromise } from 'axios';
import { HotKeywordVO, HotKeywordForm, HotKeywordQuery } from '@/api/meditation/hotKeyword/types';

/**
 * 查询热搜关键词列表
 * @param query
 * @returns {*}
 */

export const listHotKeyword = (query?: HotKeywordQuery): AxiosPromise<HotKeywordVO[]> => {
  return request({
    url: '/meditation/hotKeyword/list',
    method: 'get',
    params: query
  });
};

/**
 * 查询热搜关键词详细
 * @param id
 */
export const getHotKeyword = (id: string | number): AxiosPromise<HotKeywordVO> => {
  return request({
    url: '/meditation/hotKeyword/' + id,
    method: 'get'
  });
};

/**
 * 新增热搜关键词
 * @param data
 */
export const addHotKeyword = (data: HotKeywordForm) => {
  return request({
    url: '/meditation/hotKeyword',
    method: 'post',
    data: data
  });
};

/**
 * 修改热搜关键词
 * @param data
 */
export const updateHotKeyword = (data: HotKeywordForm) => {
  return request({
    url: '/meditation/hotKeyword',
    method: 'put',
    data: data
  });
};

/**
 * 删除热搜关键词
 * @param id
 */
export const delHotKeyword = (id: string | number | Array<string | number>) => {
  return request({
    url: '/meditation/hotKeyword/' + id,
    method: 'delete'
  });
};
