export interface TagVO {
  /**
   * 主键
   */
  id: string | number;

  /**
   * 标签名称
   */
  name: string;

  /**
   * 标签类型
   */
  type: string;

  /**
   * 状态（0正常 1停用）
   */
  status: string;

  /**
   * 排序
   */
  orderNum: number;

  /**
   * 备注
   */
  remark: string;

}

export interface TagForm extends BaseEntity {
  /**
   * 主键
   */
  id?: string | number;

  /**
   * 标签名称
   */
  name?: string;

  /**
   * 标签类型
   */
  type?: string;

  /**
   * 状态（0正常 1停用）
   */
  status?: string;

  /**
   * 排序
   */
  orderNum?: number;

  /**
   * 备注
   */
  remark?: string;

}

export interface TagQuery extends PageQuery {

  /**
   * 标签名称
   */
  name?: string;

  /**
   * 标签类型
   */
  type?: string;

  /**
   * 状态（0正常 1停用）
   */
  status?: string;

  /**
   * 排序
   */
  orderNum?: number;

    /**
     * 日期范围参数
     */
    params?: any;
}



