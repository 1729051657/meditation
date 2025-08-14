export interface ArticleVO {
  /**
   * 主键
   */
  id: string | number;

  /**
   * 标题
   */
  title: string;

  /**
   * 封面文件id（sys_oss.oss_id）
   */
  cover: number;

  /**
   * 摘要
   */
  summary: string;

  /**
   * 内容（HTML/Markdown）
   */
  content: string;

  /**
   * 作者用户id
   */
  authorId: string | number;

  /**
   * 状态（0正常 1停用）
   */
  status: string;

  /**
   * 发布时间
   */
  publishTime: string;

  /**
   * 显示顺序
   */
  orderNum: number;

  /**
   * 备注
   */
  remark: string;

}

export interface ArticleForm extends BaseEntity {
  /**
   * 主键
   */
  id?: string | number;

  /**
   * 标题
   */
  title?: string;

  /**
   * 封面文件id（sys_oss.oss_id）
   */
  cover?: number;

  /**
   * 摘要
   */
  summary?: string;

  /**
   * 内容（HTML/Markdown）
   */
  content?: string;

  /**
   * 作者用户id
   */
  authorId?: string | number;

  /**
   * 状态（0正常 1停用）
   */
  status?: string;

  /**
   * 发布时间
   */
  publishTime?: string;

  /**
   * 显示顺序
   */
  orderNum?: number;

  /**
   * 备注
   */
  remark?: string;

}

export interface ArticleQuery extends PageQuery {

  /**
   * 标题
   */
  title?: string;

  /**
   * 封面文件id（sys_oss.oss_id）
   */
  cover?: number;

  /**
   * 摘要
   */
  summary?: string;

  /**
   * 内容（HTML/Markdown）
   */
  content?: string;

  /**
   * 作者用户id
   */
  authorId?: string | number;

  /**
   * 状态（0正常 1停用）
   */
  status?: string;

  /**
   * 发布时间
   */
  publishTime?: string;

  /**
   * 显示顺序
   */
  orderNum?: number;

    /**
     * 日期范围参数
     */
    params?: any;
}



