export interface RecommendSlotVO {
  /**
   * 主键
   */
  id: string | number;

  /**
   * 推荐位编码
   */
  code: string;

  /**
   * 推荐位名称
   */
  name: string;

  /**
   * 所属页面（home/category/search等）
   */
  page: string;

  /**
   * 状态（0正常 1停用）
   */
  status: string;

  /**
   * 显示顺序
   */
  orderNum: number;

  /**
   * 备注
   */
  remark: string;

}

export interface RecommendSlotForm extends BaseEntity {
  /**
   * 主键
   */
  id?: string | number;

  /**
   * 推荐位编码
   */
  code?: string;

  /**
   * 推荐位名称
   */
  name?: string;

  /**
   * 所属页面（home/category/search等）
   */
  page?: string;

  /**
   * 状态（0正常 1停用）
   */
  status?: string;

  /**
   * 显示顺序
   */
  orderNum?: number;

  /**
   * 备注
   */
  remark?: string;

}

export interface RecommendSlotQuery extends PageQuery {

  /**
   * 推荐位编码
   */
  code?: string;

  /**
   * 推荐位名称
   */
  name?: string;

  /**
   * 所属页面（home/category/search等）
   */
  page?: string;

  /**
   * 状态（0正常 1停用）
   */
  status?: string;

  /**
   * 显示顺序
   */
  orderNum?: number;

    /**
     * 日期范围参数
     */
    params?: any;
}



