# FAQ功能实现文档

## 功能概述
为小程序FAQ（常见问题）页面实现了后端数据管理功能，支持通过系统配置和字典管理FAQ内容。

## 实现内容

### 1. 后端实现

#### 1.1 控制器
- **文件**: `/ruoyi-modules/ruoyi-meditation/src/main/java/org/dromara/meditation/controller/FaqController.java`
- **接口**:
  - `GET /meditation/faq/data` - 获取所有FAQ数据（联系信息+问题列表）
  - `GET /meditation/faq/contact` - 仅获取联系信息
  - `GET /meditation/faq/questions` - 仅获取问题列表

#### 1.2 依赖配置
- 在 `ruoyi-modules/ruoyi-meditation/pom.xml` 中添加了 `ruoyi-system` 模块依赖
- 用于访问系统配置服务和字典服务

### 2. 前端实现

#### 2.1 API接口
- **文件**: `/meditation-uniapp/api/faq.js`
- 封装了FAQ相关的API调用方法

#### 2.2 页面更新
- **文件**: `/meditation-uniapp/pages/faq/index.vue`
- 实现了从后端动态加载FAQ数据
- 保留了默认数据作为后备方案
- 动态显示联系信息

### 3. 数据配置

#### 3.1 系统配置项
配置键 | 说明 | 默认值
---|---|---
miniapp.contact.email | 客服邮箱 | support@meditation.com
miniapp.contact.phone | 客服电话 | 400-123-4567
miniapp.contact.wechat | 客服微信 | meditation_service
miniapp.contact.qq | 客服QQ | 88888888
miniapp.contact.address | 客服地址 | 北京市朝阳区xxx大厦10层

#### 3.2 字典配置
- **字典类型**: `miniapp_faq_questions`
- **字典数据结构**:
  - `dict_label`: 问题标题
  - `dict_value`: 问题ID
  - `remark`: 问题答案
  - `dict_sort`: 显示顺序

### 4. 数据初始化

执行SQL脚本初始化数据：
```bash
mysql -u root -p your_database < /workspace/script/sql/faq_init_data.sql
```

### 5. 使用说明

#### 5.1 管理员操作
1. **修改联系方式**:
   - 登录后台管理系统
   - 进入 系统管理 > 参数设置
   - 搜索 `miniapp.contact` 相关配置
   - 修改对应的值

2. **管理FAQ问题**:
   - 登录后台管理系统
   - 进入 系统管理 > 字典管理
   - 找到字典类型 `miniapp_faq_questions`
   - 点击"数据"按钮管理问题列表
   - 可以新增、修改、删除问题

#### 5.2 前端展示
- 小程序FAQ页面会自动从后端加载最新的配置和问题
- 如果后端服务不可用，会使用默认数据保证页面正常显示

### 6. 特性

1. **动态管理**: 通过后台管理系统即可修改FAQ内容，无需修改代码
2. **容错处理**: 前端保留默认数据，确保服务异常时页面仍可正常展示
3. **灵活配置**: 联系方式和问题列表分离管理，可独立更新
4. **排序支持**: 问题可通过 `dict_sort` 字段控制显示顺序
5. **权限控制**: 接口使用 `@SaIgnore` 注解，无需登录即可访问

### 7. 注意事项

1. 初次部署需要执行SQL脚本初始化数据
2. 修改字典数据时，将问题内容放在 `dict_label` 字段，答案放在 `remark` 字段
3. 系统配置的值如果为空，前端会使用默认值
4. FAQ接口是公开的，不需要用户登录

### 8. 扩展建议

1. **缓存优化**: 可以对FAQ数据进行缓存，减少数据库查询
2. **多语言支持**: 可以通过不同的字典类型支持多语言FAQ
3. **分类管理**: 可以增加问题分类功能，更好地组织问题
4. **搜索功能**: 可以添加问题搜索功能，帮助用户快速找到答案
5. **统计分析**: 可以记录用户查看的问题，分析热门问题

## 测试验证

1. 启动后端服务
2. 执行SQL初始化脚本
3. 访问接口测试：
   ```bash
   curl http://localhost:8080/meditation/faq/data
   ```
4. 启动小程序，进入FAQ页面查看效果