export interface HotKeywordVO {
  /**
   * 主键
   */
  id: string | number;

  /**
   * 热搜词
   */
  keyword: string;

  /**
   * 排序
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

export interface HotKeywordForm extends BaseEntity {
  /**
   * 主键
   */
  id?: string | number;

  /**
   * 热搜词
   */
  keyword?: string;

  /**
   * 排序
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

export interface HotKeywordQuery extends PageQuery {

  /**
   * 热搜词
   */
  keyword?: string;

  /**
   * 排序
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



