# 冥想小程序登录对接说明

## 一、功能概述

冥想小程序已成功对接微信登录功能，实现了以下特性：

1. **自动登录**：首页打开时自动进行微信登录
2. **用户自动创建**：首次登录的用户会自动创建账号
3. **无需手机号授权**：简化登录流程，不需要获取用户手机号
4. **状态管理**：使用Vuex统一管理用户登录状态

## 二、数据库配置

### 1. sys_client表配置

系统授权表中已配置小程序客户端信息：

```sql
-- 小程序客户端配置
INSERT INTO `sys_client` (
    `id`, 
    `client_id`,           -- app.VOE (小程序AppID)
    `client_key`,          -- user_xcx
    `client_secret`,       -- c504f2d547bb0d5fa743ce6a3b4dbbe2 (小程序AppSecret)
    `grant_type`,          -- xcx (授权类型)
    `device_type`,         -- xcx (设备类型)
    `active_timeout`,      -- 1800 (30分钟活跃超时)
    `timeout`,             -- 604800 (7天固定超时)
    `status`,              -- 0 (正常)
    `del_flag`             -- 0 (未删除)
) VALUES (
    3, 
    'wx1ef177b90f6fb543', 
    'user_xcx', 
    'c504f2d547bb0d5fa743ce6a3b4dbbe2', 
    'xcx', 
    'xcx', 
    1800, 
    604800, 
    '0', 
    '0'
);
```

**注意**：请将`client_secret`替换为您的小程序实际AppSecret。

### 2. 执行SQL脚本

```bash
# 进入数据库
mysql -u root -p meditation

# 执行脚本
source /workspace/script/sql/update/add_xcx_client.sql
```

## 三、前端配置

### 1. 配置文件修改

文件：`/meditation-uniapp/common/config.js`

```javascript
export const appConfig = {
  // 微信小程序AppID
  wechatAppId: 'wx1ef177b90f6fb543',
  
  // 客户端配置
  clientId: 'wx1ef177b90f6fb543',
  clientKey: 'user_xcx',
  
  // 默认租户ID
  defaultTenantId: '000000',
  
  // ... 其他配置
}
```

### 2. Store配置

用户状态管理：`/meditation-uniapp/store/modules/user.js`

主要方法：
- `wxLogin()` - 小程序登录
- `logout()` - 退出登录
- `getUserInfo()` - 获取用户信息

## 四、核心功能实现

### 1. 首页自动登录

文件：`/meditation-uniapp/pages/home/index.vue`

```javascript
onLoad() {
  // 页面加载时自动登录
  this.autoLogin()
}

async autoLogin() {
  try {
    // 检查是否已登录
    if (this.token) {
      this.loadData()
      return
    }
    
    // 调用store中的小程序登录方法
    await this.wxLogin({
      tenantId: '000000'
    })
    
    // 登录成功，加载数据
    this.loadData()
  } catch (error) {
    console.error('自动登录失败:', error)
  }
}
```

### 2. 个人中心登出

文件：`/meditation-uniapp/pages/me/index.vue`

```javascript
handleLogout() {
  uni.showModal({
    title: '提示',
    content: '确定要退出登录吗？',
    success: async (res) => {
      if (res.confirm) {
        // 调用store中的登出方法
        await this.logout()
        uni.showToast({
          title: '已退出登录',
          icon: 'success'
        })
      }
    }
  })
}
```

## 五、后端接口

### 1. 登录接口

- **URL**: `/xcx/auth/login`
- **Method**: POST
- **请求参数**:
  ```json
  {
    "xcxCode": "微信登录code",
    "appid": "小程序AppID",
    "clientId": "客户端ID",
    "grantType": "xcx",
    "tenantId": "租户ID"
  }
  ```
- **返回数据**:
  ```json
  {
    "code": 200,
    "data": {
      "access_token": "token值",
      "expire_in": 604800,
      "client_id": "wx1ef177b90f6fb543"
    }
  }
  ```

### 2. 登出接口

- **URL**: `/xcx/auth/logout`
- **Method**: POST
- **Headers**: `Authorization: Bearer {token}`

## 六、注意事项

1. **AppSecret安全**：小程序AppSecret必须保存在后端，前端不应包含此信息
2. **Token存储**：使用统一的key存储token，确保各模块一致
3. **用户类型**：小程序用户类型为`xcx_user`，与普通用户区分
4. **自动创建用户**：首次登录会自动创建用户，用户名格式为"冥想用户+openid前8位"
5. **角色分配**：新用户默认分配角色ID为5的"冥想用户"角色

## 七、测试步骤

1. **配置数据库**：确保sys_client表中有小程序客户端配置
2. **启动后端服务**：运行Spring Boot应用
3. **启动小程序**：使用微信开发者工具打开项目
4. **测试登录**：
   - 打开首页，应自动进行登录
   - 查看控制台日志，确认登录成功
   - 进入个人中心，查看用户信息
5. **测试登出**：在个人中心点击"退出登录"

## 八、常见问题

### 1. 登录失败

- 检查AppID和AppSecret是否正确
- 确认sys_client表配置正确
- 查看后端日志排查错误

### 2. Token失效

- Token有效期为7天
- 活跃超时时间为30分钟
- 可在sys_client表中调整超时时间

### 3. 用户信息不显示

- 检查store中的状态是否正确更新
- 确认getUserInfo接口返回正常

## 九、开发环境说明

在H5开发环境下，由于不在微信环境中，自动登录会失败。系统会自动跳过登录流程，直接加载数据。这是正常现象，不影响小程序实际使用。

## 十、后续优化建议

1. **添加登录loading动画**：优化用户体验
2. **实现token自动刷新**：避免用户频繁重新登录
3. **添加游客模式**：允许未登录用户浏览部分内容
4. **完善错误处理**：提供更友好的错误提示
5. **添加手机号绑定**：可选功能，用于后续营销推送