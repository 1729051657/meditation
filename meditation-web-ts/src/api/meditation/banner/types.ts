export interface BannerVO {
  /**
   * 主键
   */
  id: string | number;

  /**
   * 所属页面（home等）
   */
  page: string;

  /**
   * 图片文件id（sys_oss.oss_id）
   */
  image: number;

  /**
   * 图片文件id（sys_oss.oss_id）Url
   */
  imageUrl: string;
  /**
   * 链接类型（0无 1外链URL 2内容跳转）
   */
  linkType: string;

  /**
   * 链接目标（URL或内容定位）
   */
  linkTarget: string;

  /**
   * 显示顺序
   */
  orderNum: number;

  /**
   * 状态（0正常 1停用）
   */
  status: string;

  /**
   * 生效时间
   */
  startTime: string;

  /**
   * 失效时间
   */
  endTime: string;

}

export interface BannerForm extends BaseEntity {
  /**
   * 主键
   */
  id?: string | number;

  /**
   * 所属页面（home等）
   */
  page?: string;

  /**
   * 图片文件id（sys_oss.oss_id）
   */
  image?: number;

  /**
   * 链接类型（0无 1外链URL 2内容跳转）
   */
  linkType?: string;

  /**
   * 链接目标（URL或内容定位）
   */
  linkTarget?: string;

  /**
   * 显示顺序
   */
  orderNum?: number;

  /**
   * 状态（0正常 1停用）
   */
  status?: string;

  /**
   * 生效时间
   */
  startTime?: string;

  /**
   * 失效时间
   */
  endTime?: string;

}

export interface BannerQuery extends PageQuery {

  /**
   * 所属页面（home等）
   */
  page?: string;

  /**
   * 图片文件id（sys_oss.oss_id）
   */
  image?: number;

  /**
   * 链接类型（0无 1外链URL 2内容跳转）
   */
  linkType?: string;

  /**
   * 链接目标（URL或内容定位）
   */
  linkTarget?: string;

  /**
   * 显示顺序
   */
  orderNum?: number;

  /**
   * 状态（0正常 1停用）
   */
  status?: string;

  /**
   * 生效时间
   */
  startTime?: string;

  /**
   * 失效时间
   */
  endTime?: string;

    /**
     * 日期范围参数
     */
    params?: any;
}



