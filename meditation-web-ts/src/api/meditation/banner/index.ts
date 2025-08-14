import request from '@/utils/request';
import { AxiosPromise } from 'axios';
import { BannerVO, BannerForm, BannerQuery } from '@/api/meditation/banner/types';

/**
 * 查询横幅配置列表
 * @param query
 * @returns {*}
 */

export const listBanner = (query?: BannerQuery): AxiosPromise<BannerVO[]> => {
  return request({
    url: '/meditation/banner/list',
    method: 'get',
    params: query
  });
};

/**
 * 查询横幅配置详细
 * @param id
 */
export const getBanner = (id: string | number): AxiosPromise<BannerVO> => {
  return request({
    url: '/meditation/banner/' + id,
    method: 'get'
  });
};

/**
 * 新增横幅配置
 * @param data
 */
export const addBanner = (data: BannerForm) => {
  return request({
    url: '/meditation/banner',
    method: 'post',
    data: data
  });
};

/**
 * 修改横幅配置
 * @param data
 */
export const updateBanner = (data: BannerForm) => {
  return request({
    url: '/meditation/banner',
    method: 'put',
    data: data
  });
};

/**
 * 删除横幅配置
 * @param id
 */
export const delBanner = (id: string | number | Array<string | number>) => {
  return request({
    url: '/meditation/banner/' + id,
    method: 'delete'
  });
};
