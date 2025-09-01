# FAQ字段对接说明

## 一、后端VO字段定义

### 1. FaqQuestionVo（问题对象）
| 字段名 | 类型 | 说明 | 对应字典字段 |
|--------|------|------|-------------|
| id | String | 问题ID | dictValue（字典键值） |
| question | String | 问题内容 | dictLabel（字典标签） |
| answer | String | 答案内容 | remark（备注） |
| sort | Integer | 排序号 | dictSort（字典排序） |
| expanded | Boolean | 展开状态 | 前端控制，默认false |

### 2. ContactInfoVo（联系信息对象）
| 字段名 | 类型 | 说明 | 对应系统参数 |
|--------|------|------|-------------|
| email | String | 邮箱地址 | miniapp.contact.email |
| phone | String | 联系电话 | miniapp.contact.phone |
| wechat | String | 微信号 | miniapp.contact.wechat |
| qq | String | QQ号 | miniapp.contact.qq |
| address | String | 联系地址 | miniapp.contact.address |

### 3. FaqDataVo（完整数据对象）
| 字段名 | 类型 | 说明 |
|--------|------|------|
| contactInfo | ContactInfoVo | 联系信息 |
| questions | List<FaqQuestionVo> | 问题列表 |

## 二、小程序端字段使用

### 1. 页面数据结构
```javascript
data() {
  return {
    // FAQ问题列表
    faqList: [
      {
        id: '1',           // 问题ID
        question: '...',   // 问题内容
        answer: '...',     // 答案内容
        sort: 1,           // 排序号
        expanded: false    // 展开状态
      }
    ],
    // 联系信息
    contactInfo: {
      email: '',     // 邮箱
      phone: '',     // 电话
      wechat: '',    // 微信
      qq: '',        // QQ
      address: ''    // 地址
    }
  }
}
```

### 2. 模板中的字段绑定
```html
<!-- 问题列表 -->
<view v-for="(item, index) in faqList" :key="item.id || index">
  <!-- 使用item.question显示问题 -->
  <text>{{ item.question }}</text>
  <!-- 使用item.answer显示答案 -->
  <text>{{ item.answer }}</text>
  <!-- 使用item.expanded控制展开状态 -->
  <view v-if="item.expanded">...</view>
</view>

<!-- 联系信息 -->
<text>{{ contactInfo.email }}</text>
<text>{{ contactInfo.phone }}</text>
<text>{{ contactInfo.wechat }}</text>
<text>{{ contactInfo.qq }}</text>
<text>{{ contactInfo.address }}</text>
```

## 三、数据流程

### 1. 数据加载流程
1. 页面加载时调用 `loadFaqData()` 方法
2. 调用API接口 `getFaqData()` 获取数据
3. 接收后端返回的数据结构：
   ```json
   {
     "code": 200,
     "data": {
       "contactInfo": {...},
       "questions": [...]
     }
   }
   ```
4. 将数据赋值给页面变量
5. 按sort字段对问题进行排序
6. 如果获取失败，使用默认数据兜底

### 2. 字段映射关系
- 后端 `FaqQuestionVo.id` → 前端 `item.id`
- 后端 `FaqQuestionVo.question` → 前端 `item.question`
- 后端 `FaqQuestionVo.answer` → 前端 `item.answer`
- 后端 `FaqQuestionVo.sort` → 前端 `item.sort`
- 后端 `FaqQuestionVo.expanded` → 前端 `item.expanded`

## 四、已完成的对接工作

✅ 后端VO类创建完成
✅ 后端接口返回强类型数据
✅ 小程序API接口定义完成
✅ 小程序页面数据结构匹配
✅ 默认数据包含所有字段
✅ 添加了sort字段排序功能
✅ 使用id作为v-for的key
✅ 联系信息字段完整对接

## 五、注意事项

1. **字段类型**：确保前后端字段类型一致
   - id: String类型
   - sort: Integer类型
   - expanded: Boolean类型

2. **排序逻辑**：问题列表按sort字段升序排列

3. **默认值处理**：
   - 联系信息为空时使用空字符串
   - expanded默认为false
   - sort为null时当作0处理

4. **兼容性**：使用 `item.id || index` 作为key，确保即使id为空也能正常渲染