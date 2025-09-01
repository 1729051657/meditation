// 引入其他内容类型
import { SeriesVO } from '../series/types';
import { ArticleVO } from '../article/types';
import { TrackVO } from '../track/types';

export interface RecommendItemVO {
  /**
   * 主键
   */
  id: string | number;

  /**
   * 推荐位ID
   */
  slotId: string | number;

  /**
   * 内容类型（series/track/article）
   */
  contentType: string;

  /**
   * 内容ID
   */
  contentId: string | number;

  /**
   * 显示顺序
   */
  orderNum: number;

  /**
   * 生效时间
   */
  startTime: string;

  /**
   * 失效时间
   */
  endTime: string;

  /**
   * 状态（0正常 1停用）
   */
  status: string;

  /**
   * 系列内容详情（当contentType为series时）
   */
  seriesContent?: SeriesVO;

  /**
   * 文章内容详情（当contentType为article时）
   */
  articleContent?: ArticleVO;

  /**
   * 音频内容详情（当contentType为track时）
   */
  trackContent?: TrackVO;
}

export interface RecommendItemForm extends BaseEntity {
  /**
   * 主键
   */
  id?: string | number;

  /**
   * 推荐位ID
   */
  slotId?: string | number;

  /**
   * 内容类型（series/track/article）
   */
  contentType?: string;

  /**
   * 内容ID
   */
  contentId?: string | number;

  /**
   * 显示顺序
   */
  orderNum?: number;

  /**
   * 生效时间
   */
  startTime?: string;

  /**
   * 失效时间
   */
  endTime?: string;

  /**
   * 状态（0正常 1停用）
   */
  status?: string;

}

export interface RecommendItemQuery extends PageQuery {

  /**
   * 推荐位ID
   */
  slotId?: string | number;

  /**
   * 内容类型（series/track/article）
   */
  contentType?: string;

  /**
   * 内容ID
   */
  contentId?: string | number;

  /**
   * 显示顺序
   */
  orderNum?: number;

  /**
   * 生效时间
   */
  startTime?: string;

  /**
   * 失效时间
   */
  endTime?: string;

  /**
   * 状态（0正常 1停用）
   */
  status?: string;

    /**
     * 日期范围参数
     */
    params?: any;
}



