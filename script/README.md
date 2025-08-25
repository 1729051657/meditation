# 冥想应用测试数据生成工具

本目录包含用于生成冥想应用测试数据的脚本和工具。

## 文件说明

1. **generate_meditation_data.sql** - SQL脚本，包含所有测试数据的INSERT语句
2. **DataGenerator.java** - Java程序，可以动态生成更多测试数据
3. **import_data.sh** - Shell脚本，用于导入SQL数据到数据库

## 使用方法

### 方法一：使用SQL脚本导入

1. 修改数据库连接信息（如果需要）
2. 执行SQL脚本：

```bash
# 直接使用mysql命令
mysql -u root -p ry-vue < generate_meditation_data.sql

# 或使用导入脚本
chmod +x import_data.sh
./import_data.sh
```

### 方法二：使用Java程序生成

1. 修改DataGenerator.java中的数据库连接信息：
```java
private static final String DB_URL = "jdbc:mysql://localhost:3306/ry-vue";
private static final String DB_USER = "root";
private static final String DB_PASSWORD = "your_password";
```

2. 编译并运行：
```bash
javac DataGenerator.java
java DataGenerator
```

## 生成的测试数据

### 数据统计
- **分类数据**: 4-6条
- **系列数据**: 12-30条
- **音频数据**: 20-100条
- **文章数据**: 10-50条
- **热门关键词**: 10-20条
- **轮播图**: 5-8条
- **常见问题**: 10-15条

### 数据内容
所有数据都是模拟的冥想相关内容，包括：
- 情绪调节类冥想
- 专注力提升训练
- 睡眠改善音频
- 压力释放练习
- 相关知识文章

## 注意事项

1. **备份数据**: 导入前请先备份现有数据库
2. **清理旧数据**: 如需清理旧数据，请取消SQL脚本中的DELETE语句注释
3. **文件路径**: 生成的图片和音频路径为示例路径，需要上传实际文件
4. **主键冲突**: 如果出现主键冲突，可以：
   - 清空相关表后再导入
   - 修改SQL中的ID值
   - 使用AUTO_INCREMENT

## 自定义数据

如需自定义数据内容，可以：

1. 修改SQL脚本中的INSERT语句
2. 修改Java程序中的数组内容
3. 调整生成数据的数量

## 故障排除

### 常见问题

1. **连接失败**: 检查数据库服务是否启动，连接信息是否正确
2. **权限不足**: 确保数据库用户有INSERT权限
3. **表不存在**: 确保已执行数据库初始化脚本创建表结构
4. **字符集问题**: 确保数据库和表使用UTF-8字符集

### 联系支持

如遇到问题，请检查：
- 数据库日志
- 应用日志
- 网络连接状态

## 更新日志

- 2025-01-01: 初始版本，包含基础测试数据
- 支持6个分类的冥想数据
- 包含系列、音频、文章等完整数据结构