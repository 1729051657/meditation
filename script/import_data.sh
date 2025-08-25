#!/bin/bash

# 冥想应用测试数据导入脚本
# 使用前请修改数据库连接信息

# 数据库配置
DB_HOST="localhost"
DB_PORT="3306"
DB_NAME="ry-vue"
DB_USER="root"
DB_PASSWORD="password"

# 颜色输出
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

echo -e "${GREEN}========================================${NC}"
echo -e "${GREEN}冥想应用测试数据导入工具${NC}"
echo -e "${GREEN}========================================${NC}"

# 检查MySQL客户端是否安装
if ! command -v mysql &> /dev/null; then
    echo -e "${RED}错误：MySQL客户端未安装${NC}"
    echo "请先安装MySQL客户端：sudo apt-get install mysql-client"
    exit 1
fi

# 显示当前配置
echo -e "${YELLOW}当前数据库配置：${NC}"
echo "主机: $DB_HOST:$DB_PORT"
echo "数据库: $DB_NAME"
echo "用户: $DB_USER"
echo ""

# 询问是否继续
read -p "是否继续导入数据？(y/n): " -n 1 -r
echo
if [[ ! $REPLY =~ ^[Yy]$ ]]; then
    echo "操作已取消"
    exit 0
fi

# 备份提醒
echo -e "${YELLOW}提示：建议在导入前备份现有数据${NC}"
read -p "是否已备份数据？(y/n): " -n 1 -r
echo
if [[ ! $REPLY =~ ^[Yy]$ ]]; then
    echo "请先备份数据后再执行"
    exit 0
fi

# 检查SQL文件是否存在
SQL_FILE="generate_meditation_data.sql"
if [ ! -f "$SQL_FILE" ]; then
    echo -e "${RED}错误：SQL文件 $SQL_FILE 不存在${NC}"
    exit 1
fi

# 执行导入
echo -e "${GREEN}开始导入数据...${NC}"
mysql -h $DB_HOST -P $DB_PORT -u $DB_USER -p$DB_PASSWORD $DB_NAME < $SQL_FILE

if [ $? -eq 0 ]; then
    echo -e "${GREEN}========================================${NC}"
    echo -e "${GREEN}数据导入成功！${NC}"
    echo -e "${GREEN}========================================${NC}"
    echo ""
    echo "已导入的数据包括："
    echo "  - 冥想分类 (meditation_category)"
    echo "  - 冥想系列 (meditation_series)"
    echo "  - 冥想音频 (meditation_track)"
    echo "  - 冥想文章 (meditation_article)"
    echo "  - 热门关键词 (meditation_hot_keyword)"
    echo "  - 轮播图 (meditation_banner)"
    echo "  - 常见问题 (meditation_faq)"
    echo ""
    echo -e "${YELLOW}注意事项：${NC}"
    echo "1. 请检查图片和音频文件路径是否正确"
    echo "2. 如需要真实的图片和音频文件，请上传到对应目录"
    echo "3. 可以通过后台管理系统进一步编辑数据"
else
    echo -e "${RED}========================================${NC}"
    echo -e "${RED}数据导入失败！${NC}"
    echo -e "${RED}========================================${NC}"
    echo "请检查："
    echo "1. 数据库连接信息是否正确"
    echo "2. 数据库表结构是否已创建"
    echo "3. 是否有重复的主键冲突"
fi