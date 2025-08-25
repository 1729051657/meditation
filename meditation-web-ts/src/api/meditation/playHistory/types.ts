import { TrackVO } from '../track/types';

export interface PlayHistoryVO {
  /**
   * 主键
   */
  id: string | number;

  /**
   * 用户ID
   */
  userId: string | number;

  /**
   * 单集ID（mg_track.id）
   */
  trackId: string | number;

  /**
   * 已播放时长（秒）
   */
  progressSec: number;

  /**
   * 最后播放时间
   */
  lastPlayTime: string;

  /**
   * 播放次数
   */
  playCount: number;

  /**
   * 是否已听完（Y是 N否）
   */
  isCompleted: string;

  /**
   * 关联的单集信息
   */
  track?: TrackVO;

}

export interface PlayHistoryForm extends BaseEntity {
  /**
   * 主键
   */
  id?: string | number;

  /**
   * 用户ID
   */
  userId?: string | number;

  /**
   * 单集ID（mg_track.id）
   */
  trackId?: string | number;

  /**
   * 已播放时长（秒）
   */
  progressSec?: number;

  /**
   * 最后播放时间
   */
  lastPlayTime?: string;

  /**
   * 播放次数
   */
  playCount?: number;

  /**
   * 是否已听完（Y是 N否）
   */
  isCompleted?: string;

}

export interface PlayHistoryQuery extends PageQuery {

  /**
   * 用户ID
   */
  userId?: string | number;

  /**
   * 单集ID（mg_track.id）
   */
  trackId?: string | number;

  /**
   * 已播放时长（秒）
   */
  progressSec?: number;

  /**
   * 最后播放时间
   */
  lastPlayTime?: string;

  /**
   * 播放次数
   */
  playCount?: number;

  /**
   * 是否已听完（Y是 N否）
   */
  isCompleted?: string;

    /**
     * 日期范围参数
     */
    params?: any;
}



