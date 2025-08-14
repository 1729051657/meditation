export interface CategoryVO {
  /**
   * 主键
   */
  id: string | number;

  /**
   * 父分类id
   */
  parentId: string | number;

  /**
   * 分类名称
   */
  name: string;

  /**
   * 分类编码（英文唯一标识）
   */
  code: string;

  /**
   * 图标文件id（sys_oss.oss_id）
   */
  icon: number;

  /**
   * 简介
   */
  description: string;

  /**
   * 显示顺序
   */
  orderNum: number;

  /**
   * 状态（0正常 1停用）
   */
  status: string;

  /**
   * 备注
   */
  remark: string;

    /**
     * 子对象
     */
    children: CategoryVO[];
}

export interface CategoryForm extends BaseEntity {
  /**
   * 主键
   */
  id?: string | number;

  /**
   * 父分类id
   */
  parentId?: string | number;

  /**
   * 分类名称
   */
  name?: string;

  /**
   * 分类编码（英文唯一标识）
   */
  code?: string;

  /**
   * 图标文件id（sys_oss.oss_id）
   */
  icon?: number;

  /**
   * 简介
   */
  description?: string;

  /**
   * 显示顺序
   */
  orderNum?: number;

  /**
   * 状态（0正常 1停用）
   */
  status?: string;

  /**
   * 备注
   */
  remark?: string;

}

export interface CategoryQuery {

  /**
   * 父分类id
   */
  parentId?: string | number;

  /**
   * 分类名称
   */
  name?: string;

  /**
   * 分类编码（英文唯一标识）
   */
  code?: string;

  /**
   * 图标文件id（sys_oss.oss_id）
   */
  icon?: number;

  /**
   * 简介
   */
  description?: string;

  /**
   * 显示顺序
   */
  orderNum?: number;

  /**
   * 状态（0正常 1停用）
   */
  status?: string;

    /**
     * 日期范围参数
     */
    params?: any;
}



