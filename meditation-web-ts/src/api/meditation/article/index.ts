import request from '@/utils/request';
import { AxiosPromise } from 'axios';
import { ArticleVO, ArticleForm, ArticleQuery } from '@/api/meditation/article/types';

/**
 * 查询冥想知识文章列表
 * @param query
 * @returns {*}
 */

export const listArticle = (query?: ArticleQuery): AxiosPromise<ArticleVO[]> => {
  return request({
    url: '/meditation/article/list',
    method: 'get',
    params: query
  });
};

/**
 * 查询冥想知识文章详细
 * @param id
 */
export const getArticle = (id: string | number): AxiosPromise<ArticleVO> => {
  return request({
    url: '/meditation/article/' + id,
    method: 'get'
  });
};

/**
 * 新增冥想知识文章
 * @param data
 */
export const addArticle = (data: ArticleForm) => {
  return request({
    url: '/meditation/article',
    method: 'post',
    data: data
  });
};

/**
 * 修改冥想知识文章
 * @param data
 */
export const updateArticle = (data: ArticleForm) => {
  return request({
    url: '/meditation/article',
    method: 'put',
    data: data
  });
};

/**
 * 删除冥想知识文章
 * @param id
 */
export const delArticle = (id: string | number | Array<string | number>) => {
  return request({
    url: '/meditation/article/' + id,
    method: 'delete'
  });
};
