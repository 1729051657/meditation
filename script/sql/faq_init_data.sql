-- FAQ相关的系统配置和字典数据初始化脚本

-- ===================================
-- 1. 系统配置 - 小程序联系方式
-- ===================================

-- 插入小程序联系方式配置（如果不存在）
INSERT INTO sys_config (config_id, tenant_id, config_name, config_key, config_value, config_type, create_by, create_time, update_by, update_time, remark)
SELECT 
    (SELECT COALESCE(MAX(config_id), 0) + 1 FROM sys_config),
    '000000',
    '小程序客服邮箱',
    'miniapp.contact.email',
    'support@meditation.com',
    'Y',
    1,
    NOW(),
    1,
    NOW(),
    '小程序FAQ页面显示的客服邮箱'
WHERE NOT EXISTS (SELECT 1 FROM sys_config WHERE config_key = 'miniapp.contact.email');

INSERT INTO sys_config (config_id, tenant_id, config_name, config_key, config_value, config_type, create_by, create_time, update_by, update_time, remark)
SELECT 
    (SELECT COALESCE(MAX(config_id), 0) + 1 FROM sys_config),
    '000000',
    '小程序客服电话',
    'miniapp.contact.phone',
    '400-123-4567',
    'Y',
    1,
    NOW(),
    1,
    NOW(),
    '小程序FAQ页面显示的客服电话'
WHERE NOT EXISTS (SELECT 1 FROM sys_config WHERE config_key = 'miniapp.contact.phone');

INSERT INTO sys_config (config_id, tenant_id, config_name, config_key, config_value, config_type, create_by, create_time, update_by, update_time, remark)
SELECT 
    (SELECT COALESCE(MAX(config_id), 0) + 1 FROM sys_config),
    '000000',
    '小程序客服微信',
    'miniapp.contact.wechat',
    'meditation_service',
    'Y',
    1,
    NOW(),
    1,
    NOW(),
    '小程序FAQ页面显示的客服微信号'
WHERE NOT EXISTS (SELECT 1 FROM sys_config WHERE config_key = 'miniapp.contact.wechat');

INSERT INTO sys_config (config_id, tenant_id, config_name, config_key, config_value, config_type, create_by, create_time, update_by, update_time, remark)
SELECT 
    (SELECT COALESCE(MAX(config_id), 0) + 1 FROM sys_config),
    '000000',
    '小程序客服QQ',
    'miniapp.contact.qq',
    '88888888',
    'Y',
    1,
    NOW(),
    1,
    NOW(),
    '小程序FAQ页面显示的客服QQ号'
WHERE NOT EXISTS (SELECT 1 FROM sys_config WHERE config_key = 'miniapp.contact.qq');

INSERT INTO sys_config (config_id, tenant_id, config_name, config_key, config_value, config_type, create_by, create_time, update_by, update_time, remark)
SELECT 
    (SELECT COALESCE(MAX(config_id), 0) + 1 FROM sys_config),
    '000000',
    '小程序客服地址',
    'miniapp.contact.address',
    '北京市朝阳区xxx大厦10层',
    'Y',
    1,
    NOW(),
    1,
    NOW(),
    '小程序FAQ页面显示的客服地址'
WHERE NOT EXISTS (SELECT 1 FROM sys_config WHERE config_key = 'miniapp.contact.address');

-- ===================================
-- 2. 字典类型 - FAQ问题类型
-- ===================================

-- 插入字典类型（如果不存在）
INSERT INTO sys_dict_type (dict_id, tenant_id, dict_name, dict_type, create_by, create_time, update_by, update_time, remark)
SELECT 
    (SELECT COALESCE(MAX(dict_id), 0) + 1 FROM sys_dict_type),
    '000000',
    '小程序FAQ问题',
    'miniapp_faq_questions',
    1,
    NOW(),
    1,
    NOW(),
    '小程序常见问题FAQ列表'
WHERE NOT EXISTS (SELECT 1 FROM sys_dict_type WHERE dict_type = 'miniapp_faq_questions');

-- ===================================
-- 3. 字典数据 - FAQ问题列表
-- ===================================

-- 清理旧数据（可选）
-- DELETE FROM sys_dict_data WHERE dict_type = 'miniapp_faq_questions';

-- 插入FAQ问题数据
INSERT INTO sys_dict_data (dict_code, tenant_id, dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, create_by, create_time, update_by, update_time, remark)
VALUES
    ((SELECT COALESCE(MAX(dict_code), 0) + 1 FROM sys_dict_data), '000000', 1, '如何开始冥想？', 'faq_1', 'miniapp_faq_questions', '', 'default', 'N', 1, NOW(), 1, NOW(), 
     '选择一个安静的环境，采用舒适的坐姿，闭上眼睛，专注于呼吸。初学者可以从5-10分钟开始，逐渐增加时长。'),
    
    ((SELECT COALESCE(MAX(dict_code), 0) + 2 FROM sys_dict_data), '000000', 2, '冥想的最佳时间是什么时候？', 'faq_2', 'miniapp_faq_questions', '', 'default', 'N', 1, NOW(), 1, NOW(),
     '早晨起床后和晚上睡前是最佳的冥想时间。早晨冥想可以帮助您开启美好的一天，晚上冥想有助于放松身心，改善睡眠质量。'),
    
    ((SELECT COALESCE(MAX(dict_code), 0) + 3 FROM sys_dict_data), '000000', 3, '冥想时总是走神怎么办？', 'faq_3', 'miniapp_faq_questions', '', 'default', 'N', 1, NOW(), 1, NOW(),
     '走神是正常现象，不必自责。当您意识到走神时，温柔地将注意力带回到呼吸上。随着练习的增加，专注力会逐渐提升。'),
    
    ((SELECT COALESCE(MAX(dict_code), 0) + 4 FROM sys_dict_data), '000000', 4, '需要特殊的装备吗？', 'faq_4', 'miniapp_faq_questions', '', 'default', 'N', 1, NOW(), 1, NOW(),
     '冥想不需要特殊装备。您只需要一个安静的空间和舒适的坐垫或椅子即可。如果愿意，可以使用冥想垫或瑜伽垫增加舒适度。'),
    
    ((SELECT COALESCE(MAX(dict_code), 0) + 5 FROM sys_dict_data), '000000', 5, '冥想有什么好处？', 'faq_5', 'miniapp_faq_questions', '', 'default', 'N', 1, NOW(), 1, NOW(),
     '冥想可以减轻压力和焦虑，提高专注力，改善睡眠质量，增强自我意识，促进情绪稳定，提升整体幸福感。'),
    
    ((SELECT COALESCE(MAX(dict_code), 0) + 6 FROM sys_dict_data), '000000', 6, '每天需要冥想多长时间？', 'faq_6', 'miniapp_faq_questions', '', 'default', 'N', 1, NOW(), 1, NOW(),
     '初学者可以从每天5-10分钟开始。随着练习的深入，可以逐渐增加到20-30分钟。重要的是保持规律性，而不是时长。'),
    
    ((SELECT COALESCE(MAX(dict_code), 0) + 7 FROM sys_dict_data), '000000', 7, '如何知道自己冥想是否正确？', 'faq_7', 'miniapp_faq_questions', '', 'default', 'N', 1, NOW(), 1, NOW(),
     '没有"正确"或"错误"的冥想方式。如果您感到更加平静、专注和放松，那就是有效的冥想。每个人的体验都是独特的。'),
    
    ((SELECT COALESCE(MAX(dict_code), 0) + 8 FROM sys_dict_data), '000000', 8, '可以躺着冥想吗？', 'faq_8', 'miniapp_faq_questions', '', 'default', 'N', 1, NOW(), 1, NOW(),
     '可以，但坐姿通常更推荐，因为躺着容易睡着。如果选择躺姿，建议保持清醒的意识，避免进入睡眠状态。'),
    
    ((SELECT COALESCE(MAX(dict_code), 0) + 9 FROM sys_dict_data), '000000', 9, '冥想和睡觉有什么区别？', 'faq_9', 'miniapp_faq_questions', '', 'default', 'N', 1, NOW(), 1, NOW(),
     '冥想是保持清醒意识的放松状态，而睡眠是无意识的休息状态。冥想时大脑处于专注但放松的状态，有助于提高觉察力。'),
    
    ((SELECT COALESCE(MAX(dict_code), 0) + 10 FROM sys_dict_data), '000000', 10, '冥想适合所有人吗？', 'faq_10', 'miniapp_faq_questions', '', 'default', 'N', 1, NOW(), 1, NOW(),
     '大多数人都可以从冥想中受益。但如果您有严重的心理健康问题，建议在专业人士指导下进行。孕妇和儿童也可以适度练习。');

-- ===================================
-- 4. 提示信息
-- ===================================
SELECT '===========================================';
SELECT 'FAQ初始化数据已导入完成！';
SELECT '===========================================';
SELECT '系统配置项：';
SELECT '- miniapp.contact.email: 客服邮箱';
SELECT '- miniapp.contact.phone: 客服电话';
SELECT '- miniapp.contact.wechat: 客服微信';
SELECT '- miniapp.contact.qq: 客服QQ';
SELECT '- miniapp.contact.address: 客服地址';
SELECT '';
SELECT '字典类型：';
SELECT '- miniapp_faq_questions: 小程序FAQ问题列表';
SELECT '';
SELECT '请在后台管理系统中修改这些配置和字典数据。';
SELECT '===========================================';