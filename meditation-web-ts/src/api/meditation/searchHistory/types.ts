export interface SearchHistoryVO {
  /**
   * 主键
   */
  id: string | number;

  /**
   * 用户ID
   */
  userId: string | number;

  /**
   * 搜索关键字
   */
  keyword: string;

  /**
   * 搜索次数
   */
  times: number;

  /**
   * 最后搜索时间
   */
  lastTime: string;

}

export interface SearchHistoryForm extends BaseEntity {
  /**
   * 主键
   */
  id?: string | number;

  /**
   * 用户ID
   */
  userId?: string | number;

  /**
   * 搜索关键字
   */
  keyword?: string;

  /**
   * 搜索次数
   */
  times?: number;

  /**
   * 最后搜索时间
   */
  lastTime?: string;

}

export interface SearchHistoryQuery extends PageQuery {

  /**
   * 用户ID
   */
  userId?: string | number;

  /**
   * 搜索关键字
   */
  keyword?: string;

  /**
   * 搜索次数
   */
  times?: number;

  /**
   * 最后搜索时间
   */
  lastTime?: string;

    /**
     * 日期范围参数
     */
    params?: any;
}



