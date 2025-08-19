import { TagVO } from '@/api/meditation/tag/types';

export interface SeriesVO {
  /**
   * 主键
   */
  id: string | number;

  /**
   * 分类id（mg_category.id）
   */
  categoryId: string | number;

  /**
   * 标题
   */
  title: string;

  /**
   * 副标题
   */
  subtitle: string;

  /**
   * 封面文件id（sys_oss.oss_id）
   */
  cover: string;

  /**
   * 横幅图文件id（sys_oss.oss_id）
   */
  banner: string;

  /**
   * 简介
   */
  intro: string;

  /**
   * 小节数
   */
  episodeCount: number;

  /**
   * 建议时长（秒）
   */
  recommendDuration: number;

  /**
   * 是否免费（Y是 N否）
   */
  isFree: string;

  /**
   * 显示顺序
   */
  orderNum: number;

  /**
   * 状态（0正常 1停用）
   */
  status: string;

  /**
   * 发布时间
   */
  publishTime: string;

  /**
   * 备注
   */
  remark: string;

  /**
   * 标签列表
   */
  tags?: TagVO[];

  /**
   * 标签ID列表
   */
  tagIds?: number[];
}

export interface SeriesForm extends BaseEntity {
  /**
   * 主键
   */
  id?: string | number;

  /**
   * 分类id（mg_category.id）
   */
  categoryId?: string | number;

  /**
   * 标题
   */
  title?: string;

  /**
   * 副标题
   */
  subtitle?: string;

  /**
   * 封面文件id（sys_oss.oss_id）
   */
  cover?: string;

  /**
   * 横幅图文件id（sys_oss.oss_id）
   */
  banner?: string;

  /**
   * 简介
   */
  intro?: string;

  /**
   * 小节数
   */
  episodeCount?: number;

  /**
   * 建议时长（秒）
   */
  recommendDuration?: number;

  /**
   * 是否免费（Y是 N否）
   */
  isFree?: string;

  /**
   * 显示顺序
   */
  orderNum?: number;

  /**
   * 状态（0正常 1停用）
   */
  status?: string;

  /**
   * 发布时间
   */
  publishTime?: string;

  /**
   * 备注
   */
  remark?: string;

  /**
   * 标签ID列表
   */
  tagIds?: number[];
}

export interface SeriesQuery extends PageQuery {

  /**
   * 分类id（mg_category.id）
   */
  categoryId?: string | number;

  /**
   * 标题
   */
  title?: string;

  /**
   * 副标题
   */
  subtitle?: string;

  /**
   * 封面文件id（sys_oss.oss_id）
   */
  cover?: string;

  /**
   * 横幅图文件id（sys_oss.oss_id）
   */
  banner?: string;

  /**
   * 简介
   */
  intro?: string;

  /**
   * 小节数
   */
  episodeCount?: number;

  /**
   * 建议时长（秒）
   */
  recommendDuration?: number;

  /**
   * 是否免费（Y是 N否）
   */
  isFree?: string;

  /**
   * 显示顺序
   */
  orderNum?: number;

  /**
   * 状态（0正常 1停用）
   */
  status?: string;

  /**
   * 发布时间
   */
  publishTime?: string;

    /**
     * 日期范围参数
     */
    params?: any;
}



