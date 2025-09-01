-- =====================================================
-- FAQ相关初始化数据SQL脚本
-- =====================================================

-- 一、创建FAQ字典类型
-- =====================================================
INSERT INTO `sys_dict_type` (`dict_id`, `tenant_id`, `dict_name`, `dict_type`, `create_dept`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) 
VALUES 
(2000000001, '000000', '小程序FAQ问题', 'miniapp_faq_questions', 103, 1, NOW(), 1, NOW(), '小程序常见问题配置');


-- 二、创建FAQ字典数据（问题和答案）
-- =====================================================
INSERT INTO `sys_dict_data` (`dict_code`, `tenant_id`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `create_dept`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) 
VALUES 
-- 问题1：如何开始冥想？
(3000000001, '000000', 1, '如何开始冥想？', '1', 'miniapp_faq_questions', NULL, 'default', 'N', 103, 1, NOW(), 1, NOW(), 
'选择一个安静的环境，采用舒适的坐姿，闭上眼睛，专注于呼吸。初学者可以从5-10分钟开始，逐渐增加时长。'),

-- 问题2：冥想的最佳时间是什么时候？
(3000000002, '000000', 2, '冥想的最佳时间是什么时候？', '2', 'miniapp_faq_questions', NULL, 'default', 'N', 103, 1, NOW(), 1, NOW(), 
'早晨起床后和晚上睡前是最佳的冥想时间。早晨冥想可以帮助您开启美好的一天，晚上冥想有助于放松身心，改善睡眠质量。'),

-- 问题3：冥想时总是走神怎么办？
(3000000003, '000000', 3, '冥想时总是走神怎么办？', '3', 'miniapp_faq_questions', NULL, 'default', 'N', 103, 1, NOW(), 1, NOW(), 
'走神是正常现象，不必自责。当您意识到走神时，温柔地将注意力带回到呼吸上。随着练习的增加，专注力会逐渐提升。'),

-- 问题4：需要特殊的装备吗？
(3000000004, '000000', 4, '需要特殊的装备吗？', '4', 'miniapp_faq_questions', NULL, 'default', 'N', 103, 1, NOW(), 1, NOW(), 
'冥想不需要特殊装备。您只需要一个安静的空间和舒适的坐垫或椅子即可。如果愿意，可以使用冥想垫或瑜伽垫增加舒适度。'),

-- 问题5：冥想有什么好处？
(3000000005, '000000', 5, '冥想有什么好处？', '5', 'miniapp_faq_questions', NULL, 'default', 'N', 103, 1, NOW(), 1, NOW(), 
'冥想可以减轻压力和焦虑，提高专注力，改善睡眠质量，增强自我意识，促进情绪稳定，提升整体幸福感。'),

-- 问题6：每天需要冥想多长时间？
(3000000006, '000000', 6, '每天需要冥想多长时间？', '6', 'miniapp_faq_questions', NULL, 'default', 'N', 103, 1, NOW(), 1, NOW(), 
'初学者可以从每天5-10分钟开始。随着练习的深入，可以逐渐增加到20-30分钟。重要的是保持规律性，而不是时长。'),

-- 问题7：如何知道自己冥想是否正确？
(3000000007, '000000', 7, '如何知道自己冥想是否正确？', '7', 'miniapp_faq_questions', NULL, 'default', 'N', 103, 1, NOW(), 1, NOW(), 
'没有"正确"或"错误"的冥想方式。如果您感到更加平静、专注和放松，那就是有效的冥想。每个人的体验都是独特的。'),

-- 问题8：可以躺着冥想吗？
(3000000008, '000000', 8, '可以躺着冥想吗？', '8', 'miniapp_faq_questions', NULL, 'default', 'N', 103, 1, NOW(), 1, NOW(), 
'可以，但坐姿通常更推荐，因为躺着容易睡着。如果选择躺姿，建议保持清醒的意识，避免进入睡眠状态。'),

-- 问题9：冥想和睡眠有什么区别？
(3000000009, '000000', 9, '冥想和睡眠有什么区别？', '9', 'miniapp_faq_questions', NULL, 'default', 'N', 103, 1, NOW(), 1, NOW(), 
'冥想是保持清醒意识的放松状态，而睡眠是无意识的休息状态。冥想时大脑保持觉察，能够观察自己的思维和感受。'),

-- 问题10：冥想时可以听音乐吗？
(3000000010, '000000', 10, '冥想时可以听音乐吗？', '10', 'miniapp_faq_questions', NULL, 'default', 'N', 103, 1, NOW(), 1, NOW(), 
'可以。轻柔的背景音乐、自然声音或引导冥想音频都有助于放松。但随着练习深入，建议尝试静默冥想以提高专注力。'),

-- 问题11：什么是正念冥想？
(3000000011, '000000', 11, '什么是正念冥想？', '11', 'miniapp_faq_questions', NULL, 'default', 'N', 103, 1, NOW(), 1, NOW(), 
'正念冥想是专注于当下的练习，不评判地观察自己的思维、情绪和身体感受。它帮助我们更好地了解自己，提高生活质量。'),

-- 问题12：冥想适合所有人吗？
(3000000012, '000000', 12, '冥想适合所有人吗？', '12', 'miniapp_faq_questions', NULL, 'default', 'N', 103, 1, NOW(), 1, NOW(), 
'大多数人都能从冥想中受益。但如果您有严重的心理健康问题，建议先咨询专业医生或心理治疗师的意见。');


-- 三、创建联系方式系统参数配置
-- =====================================================
INSERT INTO `sys_config` (`config_id`, `tenant_id`, `config_name`, `config_key`, `config_value`, `config_type`, `create_dept`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) 
VALUES 
-- 客服邮箱
(4000000001, '000000', '小程序客服邮箱', 'miniapp.contact.email', 'support@meditation.com', 'N', 103, 1, NOW(), 1, NOW(), 
'小程序FAQ页面显示的客服邮箱地址'),

-- 客服电话
(4000000002, '000000', '小程序客服电话', 'miniapp.contact.phone', '400-123-4567', 'N', 103, 1, NOW(), 1, NOW(), 
'小程序FAQ页面显示的客服电话'),

-- 微信号
(4000000003, '000000', '小程序客服微信', 'miniapp.contact.wechat', 'meditation_service', 'N', 103, 1, NOW(), 1, NOW(), 
'小程序FAQ页面显示的客服微信号'),

-- QQ号
(4000000004, '000000', '小程序客服QQ', 'miniapp.contact.qq', '88888888', 'N', 103, 1, NOW(), 1, NOW(), 
'小程序FAQ页面显示的客服QQ号'),

-- 联系地址
(4000000005, '000000', '小程序联系地址', 'miniapp.contact.address', '北京市朝阳区xxx大厦8层', 'N', 103, 1, NOW(), 1, NOW(), 
'小程序FAQ页面显示的联系地址');


-- 四、查询验证数据
-- =====================================================
-- 查询FAQ字典类型
SELECT * FROM sys_dict_type WHERE dict_type = 'miniapp_faq_questions';

-- 查询FAQ问题列表
SELECT dict_code, dict_sort, dict_label as question, dict_value as id, remark as answer 
FROM sys_dict_data 
WHERE dict_type = 'miniapp_faq_questions' 
ORDER BY dict_sort;

-- 查询联系方式配置
SELECT config_key, config_value, config_name, remark 
FROM sys_config 
WHERE config_key LIKE 'miniapp.contact.%';


-- 五、更新或删除数据（如需要）
-- =====================================================
-- 更新某个问题的答案
-- UPDATE sys_dict_data 
-- SET remark = '新的答案内容', update_time = NOW() 
-- WHERE dict_code = 3000000001;

-- 删除所有FAQ数据（谨慎使用）
-- DELETE FROM sys_dict_data WHERE dict_type = 'miniapp_faq_questions';
-- DELETE FROM sys_dict_type WHERE dict_type = 'miniapp_faq_questions';
-- DELETE FROM sys_config WHERE config_key LIKE 'miniapp.contact.%';


-- =====================================================
-- 注意事项：
-- 1. dict_code 和 config_id 需要确保唯一，如果已存在相同ID，请修改
-- 2. tenant_id 默认使用 '000000'，如需其他租户请修改
-- 3. create_by 和 update_by 默认使用 1（admin用户），请根据实际情况修改
-- 4. create_dept 默认使用 103，请根据实际部门ID修改
-- 5. 执行前请先备份数据库
-- =====================================================