export interface TrackVO {
  /**
   * 主键
   */
  id: string | number;

  /**
   * 所属系列id（mg_series.id）
   */
  seriesId: string | number;

  /**
   * 分类id（mg_category.id）
   */
  categoryId: string | number;

  /**
   * 标题
   */
  title: string;

  /**
   * 封面文件id（sys_oss.oss_id）
   */
  cover: string;

  /**
   * 音频文件id（sys_oss.oss_id）
   */
  audio: string;

  /**
   * 时长（秒）
   */
  durationSec: number;

  /**
   * 简介
   */
  intro: string;

  /**
   * 在系列内排序（从小到大）
   */
  orderIndex: number;

  /**
   * 是否免费（Y是 N否）
   */
  isFree: string;

  /**
   * 状态（0正常 1停用）
   */
  status: string;

  /**
   * 备注
   */
  remark: string;

}

export interface TrackForm extends BaseEntity {
  /**
   * 主键
   */
  id?: string | number;

  /**
   * 所属系列id（mg_series.id）
   */
  seriesId?: string | number;

  /**
   * 分类id（mg_category.id）
   */
  categoryId?: string | number;

  /**
   * 标题
   */
  title?: string;

  /**
   * 封面文件id（sys_oss.oss_id）
   */
  cover?: string;

  /**
   * 音频文件id（sys_oss.oss_id）
   */
  audio?: string;

  /**
   * 时长（秒）
   */
  durationSec?: number;

  /**
   * 简介
   */
  intro?: string;

  /**
   * 在系列内排序（从小到大）
   */
  orderIndex?: number;

  /**
   * 是否免费（Y是 N否）
   */
  isFree?: string;

  /**
   * 状态（0正常 1停用）
   */
  status?: string;

  /**
   * 备注
   */
  remark?: string;

}

export interface TrackQuery extends PageQuery {

  /**
   * 所属系列id（mg_series.id）
   */
  seriesId?: string | number;

  /**
   * 分类id（mg_category.id）
   */
  categoryId?: string | number;

  /**
   * 标题
   */
  title?: string;

  /**
   * 封面文件id（sys_oss.oss_id）
   */
  cover?: string;

  /**
   * 音频文件id（sys_oss.oss_id）
   */
  audio?: string;

  /**
   * 时长（秒）
   */
  durationSec?: number;

  /**
   * 简介
   */
  intro?: string;

  /**
   * 在系列内排序（从小到大）
   */
  orderIndex?: number;

  /**
   * 是否免费（Y是 N否）
   */
  isFree?: string;

  /**
   * 状态（0正常 1停用）
   */
  status?: string;

    /**
     * 日期范围参数
     */
    params?: any;
}



