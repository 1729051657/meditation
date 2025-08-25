# 安全配置指南

## 重要提醒 ⚠️

**永远不要将真实的AccessKey、SecretKey、数据库密码等敏感信息提交到代码仓库！**

## 当前问题

GitHub检测到代码中包含阿里云的AccessKey信息，导致推送被拒绝。

## 解决方案

### 1. 已修复的问题
- ✅ 移除了 `doc/meditation_bak.sql` 中的真实AccessKey
- ✅ 替换为安全的占位符 `YOUR_ACCESS_KEY_ID` 和 `YOUR_ACCESS_KEY_SECRET`
- ✅ 更新了 `.gitignore` 文件，忽略敏感配置文件
- ✅ 创建了安全的配置模板 `doc/meditation_bak_template.sql`

### 2. 安全配置最佳实践

#### 使用环境变量
```bash
# 在系统环境变量中设置
export ALIYUN_ACCESS_KEY_ID=your_real_access_key
export ALIYUN_ACCESS_KEY_SECRET=your_real_secret_key
```

#### 使用配置文件（不提交到仓库）
```yaml
# config/application-prod.yml (已加入.gitignore)
aliyun:
  oss:
    access-key-id: ${ALIYUN_ACCESS_KEY_ID}
    access-key-secret: ${ALIYUN_ACCESS_KEY_SECRET}
    bucket-name: your-bucket-name
    endpoint: oss-cn-shanghai.aliyuncs.com
```

#### 使用配置中心
- 阿里云ACM配置中心
- Nacos配置中心
- Spring Cloud Config

### 3. 敏感文件管理

#### 必须忽略的文件
```
*.env
*.env.local
*.env.production
*.env.development
config/application-*.yml
config/application-*.properties
**/meditation_bak.sql
**/meditation_bak_*.sql
**/*backup*.sql
**/*bak*.sql
```

#### 安全的文件
```
doc/meditation_bak_template.sql  # 配置模板
config/application.yml            # 基础配置（无敏感信息）
```

### 4. 重新提交代码

现在可以安全地重新提交代码：

```bash
# 添加修改的文件
git add .

# 提交更改
git commit -m "fix: 移除敏感信息，添加安全配置指南"

# 推送到远程仓库
git push origin 5.X
```

### 5. 验证修复

推送成功后，GitHub应该不会再检测到敏感信息。

## 预防措施

1. **代码审查**：在提交前检查是否包含敏感信息
2. **自动化检测**：使用Git hooks或CI/CD检查敏感信息
3. **定期审计**：定期检查代码仓库中的敏感信息
4. **团队培训**：确保所有开发者了解安全配置的重要性

## 紧急情况

如果发现敏感信息已泄露：

1. **立即撤销**：立即撤销包含敏感信息的提交
2. **轮换密钥**：立即轮换泄露的AccessKey
3. **通知团队**：通知相关团队成员
4. **安全审计**：进行全面的安全审计

---

**记住：安全第一，永远不要为了便利而牺牲安全性！**
