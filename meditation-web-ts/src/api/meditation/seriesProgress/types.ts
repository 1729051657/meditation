export interface SeriesProgressVO {
  /**
   * 主键
   */
  id: string | number;

  /**
   * 用户ID
   */
  userId: string | number;

  /**
   * 系列ID（mg_series.id）
   */
  seriesId: string | number;

  /**
   * 最近播放的单集ID（mg_track.id）
   */
  lastTrackId: string | number;

  /**
   * 已完成小节数
   */
  completedCount: number;

}

export interface SeriesProgressForm extends BaseEntity {
  /**
   * 主键
   */
  id?: string | number;

  /**
   * 用户ID
   */
  userId?: string | number;

  /**
   * 系列ID（mg_series.id）
   */
  seriesId?: string | number;

  /**
   * 最近播放的单集ID（mg_track.id）
   */
  lastTrackId?: string | number;

  /**
   * 已完成小节数
   */
  completedCount?: number;

}

export interface SeriesProgressQuery extends PageQuery {

  /**
   * 用户ID
   */
  userId?: string | number;

  /**
   * 系列ID（mg_series.id）
   */
  seriesId?: string | number;

  /**
   * 最近播放的单集ID（mg_track.id）
   */
  lastTrackId?: string | number;

  /**
   * 已完成小节数
   */
  completedCount?: number;

    /**
     * 日期范围参数
     */
    params?: any;
}



