#!/bin/bash

# 测试播放历史记录功能
echo "========================================="
echo "测试播放历史记录功能"
echo "========================================="

# 设置基础URL
BASE_URL="http://localhost:8080"
API_PREFIX="/meditation"

# 测试用的token（需要先登录获取）
TOKEN=""

echo "1. 测试获取所有冥想单集（不分页）"
echo "-----------------------------------------"
curl -X GET "${BASE_URL}${API_PREFIX}/track/all?status=0" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer ${TOKEN}"

echo -e "\n\n2. 测试创建播放历史记录"
echo "-----------------------------------------"
# 创建播放历史（trackId需要根据实际数据调整）
curl -X POST "${BASE_URL}${API_PREFIX}/playHistory" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer ${TOKEN}" \
  -d '{
    "trackId": 1,
    "progressSec": 0,
    "isCompleted": "0"
  }'

echo -e "\n\n3. 测试更新播放进度"
echo "-----------------------------------------"
# 更新播放进度（id需要根据创建返回的ID调整）
curl -X PUT "${BASE_URL}${API_PREFIX}/playHistory" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer ${TOKEN}" \
  -d '{
    "id": 1,
    "trackId": 1,
    "progressSec": 120,
    "isCompleted": "0"
  }'

echo -e "\n\n4. 测试标记播放完成"
echo "-----------------------------------------"
# 标记为已完成
curl -X PUT "${BASE_URL}${API_PREFIX}/playHistory" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer ${TOKEN}" \
  -d '{
    "id": 1,
    "trackId": 1,
    "progressSec": 600,
    "isCompleted": "1"
  }'

echo -e "\n\n5. 测试获取播放历史列表"
echo "-----------------------------------------"
curl -X GET "${BASE_URL}${API_PREFIX}/playHistory/list?pageNum=1&pageSize=10" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer ${TOKEN}"

echo -e "\n\n测试完成！"
echo "========================================="