-- ----------------------------
-- 冥想业务 - 分类表
-- ----------------------------
create table mg_category
(
    id           bigint(20)      not null                comment '主键',
    tenant_id    varchar(20)     default '000000'        comment '租户编号',
    parent_id    bigint(20)      default 0               comment '父分类id',
    name         varchar(50)     not null                comment '分类名称',
    code         varchar(50)     default ''              comment '分类编码（英文唯一标识）',
    icon         bigint(20)      default null            comment '图标文件id（sys_oss.oss_id）',
    description  varchar(200)    default null            comment '简介',
    order_num    int(4)          default 0               comment '显示顺序',
    status       char(1)         default '0'             comment '状态（0正常 1停用）',
    del_flag     char(1)         default '0'             comment '删除标志（0代表存在 1代表删除）',
    create_dept  bigint(20)      default null            comment '创建部门',
    create_by    bigint(20)      default null            comment '创建者',
    create_time  datetime                                  comment '创建时间',
    update_by    bigint(20)      default null            comment '更新者',
    update_time  datetime                                  comment '更新时间',
    remark       varchar(255)    default null            comment '备注',
    primary key (id),
    unique key uk_tenant_code (tenant_id, code),
    key idx_parent (parent_id),
    key idx_status (status)
) engine=innodb comment = '冥想分类表';


-- ----------------------------
-- 冥想业务 - 系列表
-- ----------------------------
create table mg_series
(
    id                   bigint(20)      not null              comment '主键',
    tenant_id            varchar(20)     default '000000'      comment '租户编号',
    category_id          bigint(20)      default null          comment '分类id（mg_category.id）',
    title                varchar(100)    not null              comment '标题',
    subtitle             varchar(100)    default ''            comment '副标题',
    cover                bigint(20)      default null          comment '封面文件id（sys_oss.oss_id）',
    banner               bigint(20)      default null          comment '横幅图文件id（sys_oss.oss_id）',
    intro                varchar(1000)   default null          comment '简介',
    episode_count        int(11)         default 0             comment '小节数',
    recommend_duration   int(11)         default 0             comment '建议时长（秒）',
    order_num            int(4)          default 0             comment '显示顺序',
    status               char(1)         default '0'           comment '状态（0正常 1停用）',
    publish_time         datetime         default null          comment '发布时间',
    del_flag             char(1)         default '0'           comment '删除标志（0代表存在 1代表删除）',
    create_dept          bigint(20)      default null          comment '创建部门',
    create_by            bigint(20)      default null          comment '创建者',
    create_time          datetime                                comment '创建时间',
    update_by            bigint(20)      default null          comment '更新者',
    update_time          datetime                                comment '更新时间',
    remark               varchar(255)    default null          comment '备注',
    primary key (id),
    key idx_category (category_id),
    key idx_status (status)
) engine=innodb comment = '冥想系列表';


-- ----------------------------
-- 冥想业务 - 单集音频表
-- ----------------------------
create table mg_track
(
    id             bigint(20)      not null                comment '主键',
    tenant_id      varchar(20)     default '000000'        comment '租户编号',
    series_id      bigint(20)      default null            comment '所属系列id（mg_series.id）',
    category_id    bigint(20)      default null            comment '分类id（mg_category.id）',
    title          varchar(100)    not null                comment '标题',
    cover          bigint(20)      default null            comment '封面文件id（sys_oss.oss_id）',
    audio          bigint(20)      not null                comment '音频文件id（sys_oss.oss_id）',
    duration_sec   int(11)         default 0               comment '时长（秒）',
    intro          varchar(1000)   default null            comment '简介',
    order_index    int(11)         default 0               comment '在系列内排序（从小到大）',
    status         char(1)         default '0'             comment '状态（0正常 1停用）',
    del_flag       char(1)         default '0'             comment '删除标志（0代表存在 1代表删除）',
    create_dept    bigint(20)      default null            comment '创建部门',
    create_by      bigint(20)      default null            comment '创建者',
    create_time    datetime                                  comment '创建时间',
    update_by      bigint(20)      default null            comment '更新者',
    update_time    datetime                                  comment '更新时间',
    remark         varchar(255)    default null            comment '备注',
    primary key (id),
    key idx_series (series_id),
    key idx_category (category_id),
    key idx_status (status)
) engine=innodb comment = '冥想单集表';


-- ----------------------------
-- 冥想业务 - 文章/知识表
-- ----------------------------
create table mg_article
(
    id            bigint(20)      not null                comment '主键',
    tenant_id     varchar(20)     default '000000'        comment '租户编号',
    title         varchar(100)    not null                comment '标题',
    cover         bigint(20)      default null            comment '封面文件id（sys_oss.oss_id）',
    summary       varchar(500)    default null            comment '摘要',
    content       longtext        default null            comment '内容（HTML/Markdown）',
    author_id     bigint(20)      default null            comment '作者用户id',
    status        char(1)         default '0'             comment '状态（0正常 1停用）',
    publish_time  datetime        default null            comment '发布时间',
    order_num     int(4)          default 0               comment '显示顺序',
    del_flag      char(1)         default '0'             comment '删除标志（0代表存在 1代表删除）',
    create_dept   bigint(20)      default null            comment '创建部门',
    create_by     bigint(20)      default null            comment '创建者',
    create_time   datetime                                  comment '创建时间',
    update_by     bigint(20)      default null            comment '更新者',
    update_time   datetime                                  comment '更新时间',
    remark        varchar(255)    default null            comment '备注',
    primary key (id),
    key idx_status (status),
    key idx_publish_time (publish_time)
) engine=innodb comment = '冥想知识文章表';


-- ----------------------------
-- 冥想业务 - 标签表
-- ----------------------------
create table mg_tag
(
    id           bigint(20)     not null                 comment '主键',
    tenant_id    varchar(20)    default '000000'         comment '租户编号',
    name         varchar(50)    not null                 comment '标签名称',
    type         varchar(50)    default ''               comment '标签类型',
    status       char(1)        default '0'              comment '状态（0正常 1停用）',
    order_num    int(4)         default 0                comment '排序',
    create_dept  bigint(20)     default null             comment '创建部门',
    create_by    bigint(20)     default null             comment '创建者',
    create_time  datetime                                 comment '创建时间',
    update_by    bigint(20)     default null             comment '更新者',
    update_time  datetime                                 comment '更新时间',
    del_flag     char(1)        default '0'              comment '删除标志（0代表存在 1代表删除）',
    remark       varchar(255)   default null             comment '备注',
    primary key (id),
    unique key uk_tenant_name (tenant_id, name),
    key idx_status (status)
) engine=innodb comment = '内容标签表';


-- ----------------------------
-- 冥想业务 - 内容与标签关联表
-- ----------------------------
create table mg_content_tag
(
    id            bigint(20)     not null                 comment '主键',
    tenant_id     varchar(20)    default '000000'         comment '租户编号',
    content_type  varchar(20)    not null                 comment '内容类型（series/track/article）',
    content_id    bigint(20)     not null                 comment '内容ID',
    tag_id        bigint(20)     not null                 comment '标签ID',
    create_by     bigint(20)     default null             comment '创建者',
    create_time   datetime                                 comment '创建时间',
    del_flag      char(1)        default '0'              comment '删除标志（0代表存在 1代表删除）',
    primary key (id),
    unique key uk_content_tag (tenant_id, content_type, content_id, tag_id),
    key idx_tag (tag_id)
) engine=innodb comment = '内容-标签关联表';


-- ----------------------------
-- 冥想业务 - 推荐位（频道/区域）
-- ----------------------------
create table mg_recommend_slot
(
    id           bigint(20)     not null                 comment '主键',
    tenant_id    varchar(20)    default '000000'         comment '租户编号',
    code         varchar(50)    not null                 comment '推荐位编码',
    name         varchar(50)    not null                 comment '推荐位名称',
    page         varchar(20)    default ''               comment '所属页面（home/category/search等）',
    status       char(1)        default '0'              comment '状态（0正常 1停用）',
    order_num    int(4)         default 0                comment '显示顺序',
    remark       varchar(200)   default null             comment '备注',
    create_dept  bigint(20)     default null             comment '创建部门',
    create_by    bigint(20)     default null             comment '创建者',
    create_time  datetime                                 comment '创建时间',
    update_by    bigint(20)     default null             comment '更新者',
    update_time  datetime                                 comment '更新时间',
    del_flag     char(1)        default '0'              comment '删除标志（0代表存在 1代表删除）',
    primary key (id),
    unique key uk_tenant_code (tenant_id, code),
    key idx_page (page)
) engine=innodb comment = '推荐位表';


-- ----------------------------
-- 冥想业务 - 推荐位内容
-- ----------------------------
create table mg_recommend_item
(
    id            bigint(20)     not null                 comment '主键',
    tenant_id     varchar(20)    default '000000'         comment '租户编号',
    slot_id       bigint(20)     not null                 comment '推荐位ID',
    content_type  varchar(20)    not null                 comment '内容类型（series/track/article）',
    content_id    bigint(20)     not null                 comment '内容ID',
    order_num     int(4)         default 0                comment '显示顺序',
    start_time    datetime       default null             comment '生效时间',
    end_time      datetime       default null             comment '失效时间',
    status        char(1)        default '0'              comment '状态（0正常 1停用）',
    create_by     bigint(20)     default null             comment '创建者',
    create_time   datetime                                 comment '创建时间',
    update_by     bigint(20)     default null             comment '更新者',
    update_time   datetime                                 comment '更新时间',
    del_flag      char(1)        default '0'              comment '删除标志（0代表存在 1代表删除）',
    primary key (id),
    key idx_slot (slot_id),
    key idx_effective (start_time, end_time)
) engine=innodb comment = '推荐位内容表';


-- ----------------------------
-- 冥想业务 - 横幅配置
-- ----------------------------
create table mg_banner
(
    id           bigint(20)     not null                 comment '主键',
    tenant_id    varchar(20)    default '000000'         comment '租户编号',
    page         varchar(20)    default ''               comment '所属页面（home等）',
    image        bigint(20)     not null                 comment '图片文件id（sys_oss.oss_id）',
    link_type    char(1)        default '0'              comment '链接类型（0无 1外链URL 2内容跳转）',
    link_target  varchar(255)   default ''               comment '链接目标（URL或内容定位）',
    order_num    int(4)         default 0                comment '显示顺序',
    status       char(1)        default '0'              comment '状态（0正常 1停用）',
    start_time   datetime       default null             comment '生效时间',
    end_time     datetime       default null             comment '失效时间',
    create_dept  bigint(20)     default null             comment '创建部门',
    create_by    bigint(20)     default null             comment '创建者',
    create_time  datetime                                 comment '创建时间',
    update_by    bigint(20)     default null             comment '更新者',
    update_time  datetime                                 comment '更新时间',
    del_flag     char(1)        default '0'              comment '删除标志（0代表存在 1代表删除）',
    primary key (id),
    key idx_page (page),
    key idx_effective (start_time, end_time)
) engine=innodb comment = '横幅配置表';


-- ----------------------------
-- 冥想业务 - 搜索历史
-- ----------------------------
create table mg_search_history
(
    id           bigint(20)     not null                 comment '主键',
    tenant_id    varchar(20)    default '000000'         comment '租户编号',
    user_id      bigint(20)     not null                 comment '用户ID',
    keyword      varchar(100)   not null                 comment '搜索关键字',
    times        int(11)        default 1                comment '搜索次数',
    last_time    datetime                                 comment '最后搜索时间',
    create_dept  bigint(20)     default null             comment '创建部门',
    create_by    bigint(20)     default null             comment '创建者',
    create_time  datetime                                 comment '创建时间',
    update_by    bigint(20)     default null             comment '更新者',
    update_time  datetime                                 comment '更新时间',
    del_flag     char(1)        default '0'              comment '删除标志（0代表存在 1代表删除）',
    primary key (id),
    unique key uk_user_keyword (tenant_id, user_id, keyword)
) engine=innodb comment = '搜索历史表';


-- ----------------------------
-- 冥想业务 - 热搜词
-- ----------------------------
create table mg_hot_keyword
(
    id           bigint(20)     not null                 comment '主键',
    tenant_id    varchar(20)    default '000000'         comment '租户编号',
    keyword      varchar(100)   not null                 comment '热搜词',
    order_num    int(4)         default 0                comment '排序',
    start_time   datetime       default null             comment '生效时间',
    end_time     datetime       default null             comment '失效时间',
    status       char(1)        default '0'              comment '状态（0正常 1停用）',
    create_dept  bigint(20)     default null             comment '创建部门',
    create_by    bigint(20)     default null             comment '创建者',
    create_time  datetime                                 comment '创建时间',
    update_by    bigint(20)     default null             comment '更新者',
    update_time  datetime                                 comment '更新时间',
    del_flag     char(1)        default '0'              comment '删除标志（0代表存在 1代表删除）',
    primary key (id)
) engine=innodb comment = '热搜关键词表';


-- ----------------------------
-- 冥想业务 - 用户收藏
-- ----------------------------
create table mg_favorite
(
    id           bigint(20)     not null                 comment '主键',
    tenant_id    varchar(20)    default '000000'         comment '租户编号',
    user_id      bigint(20)     not null                 comment '用户ID',
    target_type  varchar(20)    not null                 comment '目标类型（series/track/article）',
    target_id    bigint(20)     not null                 comment '目标ID',
    create_time  datetime                                 comment '创建时间',
    del_flag     char(1)        default '0'              comment '删除标志（0代表存在 1代表删除）',
    primary key (id),
    unique key uk_user_target (tenant_id, user_id, target_type, target_id)
) engine=innodb comment = '用户收藏表';


-- ----------------------------
-- 冥想业务 - 播放记录
-- ----------------------------
create table mg_play_history
(
    id              bigint(20)     not null                 comment '主键',
    tenant_id       varchar(20)    default '000000'         comment '租户编号',
    user_id         bigint(20)     not null                 comment '用户ID',
    track_id        bigint(20)     not null                 comment '单集ID（mg_track.id）',
    progress_sec    int(11)        default 0                comment '已播放时长（秒）',
    last_play_time  datetime                                 comment '最后播放时间',
    play_count      int(11)        default 1                comment '播放次数',
    is_completed    char(1)        default 'N'              comment '是否已听完（Y是 N否）',
    create_time     datetime                                 comment '创建时间',
    update_time     datetime                                 comment '更新时间',
    del_flag        char(1)        default '0'              comment '删除标志（0代表存在 1代表删除）',
    primary key (id),
    unique key uk_user_track (tenant_id, user_id, track_id),
    key idx_last_play_time (last_play_time)
) engine=innodb comment = '音频播放记录表';


-- ----------------------------
-- 冥想业务 - 系列进度
-- ----------------------------
create table mg_series_progress
(
    id                bigint(20)     not null                 comment '主键',
    tenant_id         varchar(20)    default '000000'         comment '租户编号',
    user_id           bigint(20)     not null                 comment '用户ID',
    series_id         bigint(20)     not null                 comment '系列ID（mg_series.id）',
    last_track_id     bigint(20)     default null             comment '最近播放的单集ID（mg_track.id）',
    completed_count   int(11)        default 0                comment '已完成小节数',
    update_time       datetime                                 comment '更新时间',
    del_flag          char(1)        default '0'              comment '删除标志（0代表存在 1代表删除）',
    primary key (id),
    unique key uk_user_series (tenant_id, user_id, series_id)
) engine=innodb comment = '系列学习进度表';


