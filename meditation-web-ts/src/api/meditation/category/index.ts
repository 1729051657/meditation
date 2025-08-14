import request from '@/utils/request';
import { AxiosPromise } from 'axios';
import { CategoryVO, CategoryForm, CategoryQuery } from '@/api/meditation/category/types';

/**
 * 查询冥想分类列表
 * @param query
 * @returns {*}
 */

export const listCategory = (query?: CategoryQuery): AxiosPromise<CategoryVO[]> => {
  return request({
    url: '/meditation/category/list',
    method: 'get',
    params: query
  });
};

/**
 * 查询冥想分类详细
 * @param id
 */
export const getCategory = (id: string | number): AxiosPromise<CategoryVO> => {
  return request({
    url: '/meditation/category/' + id,
    method: 'get'
  });
};

/**
 * 新增冥想分类
 * @param data
 */
export const addCategory = (data: CategoryForm) => {
  return request({
    url: '/meditation/category',
    method: 'post',
    data: data
  });
};

/**
 * 修改冥想分类
 * @param data
 */
export const updateCategory = (data: CategoryForm) => {
  return request({
    url: '/meditation/category',
    method: 'put',
    data: data
  });
};

/**
 * 删除冥想分类
 * @param id
 */
export const delCategory = (id: string | number | Array<string | number>) => {
  return request({
    url: '/meditation/category/' + id,
    method: 'delete'
  });
};
