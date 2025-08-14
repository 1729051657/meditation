export interface FavoriteVO {
  /**
   * 主键
   */
  id: string | number;

  /**
   * 用户ID
   */
  userId: string | number;

  /**
   * 目标类型（series/track/article）
   */
  targetType: string;

  /**
   * 目标ID
   */
  targetId: string | number;

}

export interface FavoriteForm extends BaseEntity {
  /**
   * 主键
   */
  id?: string | number;

  /**
   * 用户ID
   */
  userId?: string | number;

  /**
   * 目标类型（series/track/article）
   */
  targetType?: string;

  /**
   * 目标ID
   */
  targetId?: string | number;

}

export interface FavoriteQuery extends PageQuery {

  /**
   * 用户ID
   */
  userId?: string | number;

  /**
   * 目标类型（series/track/article）
   */
  targetType?: string;

  /**
   * 目标ID
   */
  targetId?: string | number;

    /**
     * 日期范围参数
     */
    params?: any;
}



