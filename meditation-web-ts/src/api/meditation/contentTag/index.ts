import request from '@/utils/request';
import { AxiosPromise } from 'axios';
import { ContentTagVO, ContentTagForm, ContentTagQuery } from '@/api/meditation/contentTag/types';

/**
 * 查询内容-标签关联列表
 * @param query
 * @returns {*}
 */

export const listContentTag = (query?: ContentTagQuery): AxiosPromise<ContentTagVO[]> => {
  return request({
    url: '/meditation/contentTag/list',
    method: 'get',
    params: query
  });
};

/**
 * 查询内容-标签关联详细
 * @param id
 */
export const getContentTag = (id: string | number): AxiosPromise<ContentTagVO> => {
  return request({
    url: '/meditation/contentTag/' + id,
    method: 'get'
  });
};

/**
 * 新增内容-标签关联
 * @param data
 */
export const addContentTag = (data: ContentTagForm) => {
  return request({
    url: '/meditation/contentTag',
    method: 'post',
    data: data
  });
};

/**
 * 修改内容-标签关联
 * @param data
 */
export const updateContentTag = (data: ContentTagForm) => {
  return request({
    url: '/meditation/contentTag',
    method: 'put',
    data: data
  });
};

/**
 * 删除内容-标签关联
 * @param id
 */
export const delContentTag = (id: string | number | Array<string | number>) => {
  return request({
    url: '/meditation/contentTag/' + id,
    method: 'delete'
  });
};
