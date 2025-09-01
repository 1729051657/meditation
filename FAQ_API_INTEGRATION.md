# FAQ接口对接文档

## 一、后端接口更新说明

### 1. 修复的问题
- 修复了 `FaqController.java` 中的编译错误
- 将 `ISysDictDataService` 替换为 `ISysDictTypeService`
- 将返回类型从 `Map` 改为强类型的 VO 对象

### 2. 新增的VO类
- `ContactInfoVo` - 联系信息视图对象
- `FaqQuestionVo` - FAQ问题视图对象  
- `FaqDataVo` - FAQ完整数据视图对象

## 二、接口说明

### 1. 获取FAQ完整数据
**接口地址**: `GET /meditation/faq/data`

**返回格式**:
```json
{
  "code": 200,
  "msg": "操作成功",
  "data": {
    "contactInfo": {
      "email": "support@meditation.com",
      "phone": "400-123-4567",
      "wechat": "",
      "qq": "",
      "address": ""
    },
    "questions": [
      {
        "id": "1",
        "question": "如何开始冥想？",
        "answer": "选择一个安静的环境...",
        "sort": 1,
        "expanded": false
      }
    ]
  }
}
```

### 2. 获取联系信息
**接口地址**: `GET /meditation/faq/contact`

**返回格式**:
```json
{
  "code": 200,
  "msg": "操作成功",
  "data": {
    "email": "support@meditation.com",
    "phone": "400-123-4567",
    "wechat": "",
    "qq": "",
    "address": ""
  }
}
```

### 3. 获取FAQ问题列表
**接口地址**: `GET /meditation/faq/questions`

**返回格式**:
```json
{
  "code": 200,
  "msg": "操作成功",
  "data": [
    {
      "id": "1",
      "question": "如何开始冥想？",
      "answer": "选择一个安静的环境...",
      "sort": 1,
      "expanded": false
    }
  ]
}
```

## 三、小程序端对接

### 1. 文件结构
```
meditation-uniapp/
├── api/
│   └── faq.js              # FAQ接口定义（已完成）
├── pages/
│   └── faq/
│       └── index.vue       # FAQ页面（已完成）
├── common/
│   └── config.js          # 配置文件
└── test/
    └── test-faq-api.html  # 接口测试页面
```

### 2. 小程序端已完成的功能
- ✅ FAQ页面UI实现
- ✅ API接口定义
- ✅ 数据加载和展示逻辑
- ✅ 默认数据兜底机制
- ✅ 联系客服功能

## 四、数据配置说明

### 1. 系统参数配置（sys_config表）
需要在后台管理系统中配置以下参数：
- `miniapp.contact.email` - 客服邮箱
- `miniapp.contact.phone` - 客服电话
- `miniapp.contact.wechat` - 微信号
- `miniapp.contact.qq` - QQ号
- `miniapp.contact.address` - 联系地址

### 2. 字典数据配置（sys_dict_type和sys_dict_data表）
1. 首先创建字典类型：
   - 字典名称：小程序FAQ问题
   - 字典类型：`miniapp_faq_questions`

2. 然后添加字典数据：
   - 字典标签（dictLabel）：问题内容
   - 字典键值（dictValue）：问题ID
   - 字典排序（dictSort）：显示顺序
   - 备注（remark）：答案内容

## 五、测试步骤

### 1. 启动后端服务
```bash
cd /workspace
# 使用Maven启动（需要先安装Maven）
mvn spring-boot:run -pl ruoyi-admin
```

### 2. 配置数据
1. 登录后台管理系统
2. 进入"系统管理" -> "参数设置"，添加联系方式配置
3. 进入"系统管理" -> "字典管理"，添加FAQ问题

### 3. 测试接口
1. 使用浏览器打开测试页面：
   ```
   /workspace/meditation-uniapp/test/test-faq-api.html
   ```
2. 输入正确的API地址（默认: http://localhost:8080）
3. 点击各个测试按钮验证接口

### 4. 运行小程序
```bash
cd /workspace/meditation-uniapp
# 使用HBuilderX或命令行运行
npm run dev:mp-weixin
```

## 六、注意事项

1. **跨域配置**：开发环境需要后端配置CORS允许跨域访问
2. **数据初始化**：如果字典数据为空，小程序会使用默认的FAQ数据
3. **权限配置**：FAQ接口使用了 `@SaIgnore` 注解，无需登录即可访问
4. **环境配置**：修改 `meditation-uniapp/common/config.js` 中的 `baseUrl` 配置不同环境的API地址

## 七、常见问题

### Q1: 接口返回404
**解决方案**：
- 检查后端服务是否启动
- 确认接口路径是否正确
- 检查Controller是否被正确注册

### Q2: 数据为空
**解决方案**：
- 检查字典类型是否为 `miniapp_faq_questions`
- 确认字典数据是否已添加
- 查看后端日志是否有错误

### Q3: 小程序无法访问接口
**解决方案**：
- 检查 `config.js` 中的 `baseUrl` 配置
- 确认网络是否连通
- 查看小程序控制台的错误信息

## 八、后续优化建议

1. **缓存优化**：FAQ数据变化不频繁，可以添加缓存机制
2. **搜索功能**：添加问题搜索功能
3. **分类管理**：支持问题分类
4. **统计分析**：记录用户查看的问题，分析热门问题
5. **富文本支持**：答案支持富文本格式