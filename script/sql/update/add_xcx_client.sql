-- 添加小程序客户端配置
-- 注意：请先检查是否已存在相同的client_id，避免重复插入

-- 删除已存在的小程序客户端配置（如果有）
DELETE FROM sys_client WHERE client_id = 'wx03df4b0ec47009cd';

-- 插入小程序客户端配置
INSERT INTO sys_client (
    id, 
    client_id, 
    client_key, 
    client_secret, 
    grant_type, 
    device_type, 
    active_timeout, 
    timeout, 
    status, 
    del_flag, 
    create_dept, 
    create_by, 
    create_time, 
    update_by, 
    update_time
) VALUES (
    3, 
    'wx03df4b0ec47009cd', 
    'user_xcx', 
    'c504f2d547bb0d5fa743ce6a3b4dbbe2', 
    'xcx', 
    'xcx', 
    1800, 
    604800, 
    '0', 
    '0', 
    103, 
    1, 
    NOW(), 
    1, 
    NOW()
);

-- 提示信息
SELECT '小程序客户端配置已添加' AS message;