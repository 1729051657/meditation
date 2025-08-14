import request from '@/utils/request';
import { AxiosPromise } from 'axios';
import { FavoriteVO, FavoriteForm, FavoriteQuery } from '@/api/meditation/favorite/types';

/**
 * 查询用户收藏列表
 * @param query
 * @returns {*}
 */

export const listFavorite = (query?: FavoriteQuery): AxiosPromise<FavoriteVO[]> => {
  return request({
    url: '/meditation/favorite/list',
    method: 'get',
    params: query
  });
};

/**
 * 查询用户收藏详细
 * @param id
 */
export const getFavorite = (id: string | number): AxiosPromise<FavoriteVO> => {
  return request({
    url: '/meditation/favorite/' + id,
    method: 'get'
  });
};

/**
 * 新增用户收藏
 * @param data
 */
export const addFavorite = (data: FavoriteForm) => {
  return request({
    url: '/meditation/favorite',
    method: 'post',
    data: data
  });
};

/**
 * 修改用户收藏
 * @param data
 */
export const updateFavorite = (data: FavoriteForm) => {
  return request({
    url: '/meditation/favorite',
    method: 'put',
    data: data
  });
};

/**
 * 删除用户收藏
 * @param id
 */
export const delFavorite = (id: string | number | Array<string | number>) => {
  return request({
    url: '/meditation/favorite/' + id,
    method: 'delete'
  });
};
