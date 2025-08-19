import request from '@/utils/request';
import { AxiosPromise } from 'axios';
import { TagVO, TagForm, TagQuery } from '@/api/meditation/tag/types';

/**
 * 查询内容标签列表
 * @param query
 * @returns {*}
 */

export const listTag = (query?: TagQuery): AxiosPromise<TagVO[]> => {
  return request({
    url: '/meditation/tag/list',
    method: 'get',
    params: query
  });
};

/**
 * 查询内容标签详细
 * @param id
 */
export const getTag = (id: string | number): AxiosPromise<TagVO> => {
  return request({
    url: '/meditation/tag/' + id,
    method: 'get'
  });
};

/**
 * 新增内容标签
 * @param data
 */
export const addTag = (data: TagForm) => {
  return request({
    url: '/meditation/tag',
    method: 'post',
    data: data
  });
};

/**
 * 修改内容标签
 * @param data
 */
export const updateTag = (data: TagForm) => {
  return request({
    url: '/meditation/tag',
    method: 'put',
    data: data
  });
};

/**
 * 删除内容标签
 * @param id
 */
export const delTag = (id: string | number | Array<string | number>) => {
  return request({
    url: '/meditation/tag/' + id,
    method: 'delete'
  });
};

/**
 * 获取所有可用标签
 * @returns {*}
 */
export const getAllAvailableTags = (): AxiosPromise<TagVO[]> => {
  return request({
    url: '/meditation/tag/all',
    method: 'get'
  });
};
