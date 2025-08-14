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



