export interface ContentTagVO {
  /**
   * 主键
   */
  id: string | number;

  /**
   * 内容类型（series/track/article）
   */
  contentType: string;

  /**
   * 内容ID
   */
  contentId: string | number;

  /**
   * 标签ID
   */
  tagId: string | number;

}

export interface ContentTagForm extends BaseEntity {
  /**
   * 主键
   */
  id?: string | number;

  /**
   * 内容类型（series/track/article）
   */
  contentType?: string;

  /**
   * 内容ID
   */
  contentId?: string | number;

  /**
   * 标签ID
   */
  tagId?: string | number;

}

export interface ContentTagQuery extends PageQuery {

  /**
   * 内容类型（series/track/article）
   */
  contentType?: string;

  /**
   * 内容ID
   */
  contentId?: string | number;

  /**
   * 标签ID
   */
  tagId?: string | number;

    /**
     * 日期范围参数
     */
    params?: any;
}



