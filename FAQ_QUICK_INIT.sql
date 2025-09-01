-- =====================================================
-- FAQ快速初始化SQL（简化版）
-- 使用前请确认ID不冲突
-- =====================================================

-- 1. 创建FAQ字典类型
INSERT INTO `sys_dict_type` (`dict_id`, `tenant_id`, `dict_name`, `dict_type`, `create_by`, `create_time`) 
VALUES (2000000001, '000000', '小程序FAQ问题', 'miniapp_faq_questions', 1, NOW());

-- 2. 创建FAQ问题数据
INSERT INTO `sys_dict_data` (`dict_code`, `tenant_id`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `create_by`, `create_time`, `remark`) VALUES 
(3000000001, '000000', 1, '如何开始冥想？', '1', 'miniapp_faq_questions', 1, NOW(), '选择一个安静的环境，采用舒适的坐姿，闭上眼睛，专注于呼吸。初学者可以从5-10分钟开始，逐渐增加时长。'),
(3000000002, '000000', 2, '冥想的最佳时间是什么时候？', '2', 'miniapp_faq_questions', 1, NOW(), '早晨起床后和晚上睡前是最佳的冥想时间。早晨冥想可以帮助您开启美好的一天，晚上冥想有助于放松身心，改善睡眠质量。'),
(3000000003, '000000', 3, '冥想时总是走神怎么办？', '3', 'miniapp_faq_questions', 1, NOW(), '走神是正常现象，不必自责。当您意识到走神时，温柔地将注意力带回到呼吸上。随着练习的增加，专注力会逐渐提升。'),
(3000000004, '000000', 4, '需要特殊的装备吗？', '4', 'miniapp_faq_questions', 1, NOW(), '冥想不需要特殊装备。您只需要一个安静的空间和舒适的坐垫或椅子即可。如果愿意，可以使用冥想垫或瑜伽垫增加舒适度。'),
(3000000005, '000000', 5, '冥想有什么好处？', '5', 'miniapp_faq_questions', 1, NOW(), '冥想可以减轻压力和焦虑，提高专注力，改善睡眠质量，增强自我意识，促进情绪稳定，提升整体幸福感。'),
(3000000006, '000000', 6, '每天需要冥想多长时间？', '6', 'miniapp_faq_questions', 1, NOW(), '初学者可以从每天5-10分钟开始。随着练习的深入，可以逐渐增加到20-30分钟。重要的是保持规律性，而不是时长。'),
(3000000007, '000000', 7, '如何知道自己冥想是否正确？', '7', 'miniapp_faq_questions', 1, NOW(), '没有"正确"或"错误"的冥想方式。如果您感到更加平静、专注和放松，那就是有效的冥想。每个人的体验都是独特的。'),
(3000000008, '000000', 8, '可以躺着冥想吗？', '8', 'miniapp_faq_questions', 1, NOW(), '可以，但坐姿通常更推荐，因为躺着容易睡着。如果选择躺姿，建议保持清醒的意识，避免进入睡眠状态。');

-- 3. 创建联系方式配置
INSERT INTO `sys_config` (`config_id`, `tenant_id`, `config_name`, `config_key`, `config_value`, `config_type`, `create_by`, `create_time`) VALUES 
(4000000001, '000000', '小程序客服邮箱', 'miniapp.contact.email', 'support@meditation.com', 'N', 1, NOW()),
(4000000002, '000000', '小程序客服电话', 'miniapp.contact.phone', '400-123-4567', 'N', 1, NOW()),
(4000000003, '000000', '小程序客服微信', 'miniapp.contact.wechat', 'meditation_service', 'N', 1, NOW()),
(4000000004, '000000', '小程序客服QQ', 'miniapp.contact.qq', '88888888', 'N', 1, NOW()),
(4000000005, '000000', '小程序联系地址', 'miniapp.contact.address', '北京市朝阳区xxx大厦8层', 'N', 1, NOW());