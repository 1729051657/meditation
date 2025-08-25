-- 冥想应用测试数据生成脚本
-- 生成分类、系列、音频、文章等测试数据

-- 清理现有数据（可选）
-- DELETE FROM meditation_track WHERE 1=1;
-- DELETE FROM meditation_series WHERE 1=1;
-- DELETE FROM meditation_article WHERE 1=1;
-- DELETE FROM meditation_category WHERE 1=1;

-- 1. 插入冥想分类数据
INSERT INTO meditation_category (id, name, code, icon, description, sort, status, create_by, create_time, update_by, update_time) VALUES
(1, '情绪调节', 'emotion', '/static/home/emotion-regulation@2x.png', '帮助您管理情绪，保持内心平静', 1, 0, 'admin', NOW(), 'admin', NOW()),
(2, '提升专注', 'focus', '/static/home/improve-focus@2x.png', '提高注意力和专注度，增强工作效率', 2, 0, 'admin', NOW(), 'admin', NOW()),
(3, '改善睡眠', 'sleep', '/static/home/improve-sleep@2x.png', '改善睡眠质量，帮助您更好地休息', 3, 0, 'admin', NOW(), 'admin', NOW()),
(4, '放松减压', 'relax', '/static/home/relax-stress@2x.png', '释放压力，放松身心，恢复活力', 4, 0, 'admin', NOW(), 'admin', NOW());

-- 2. 插入冥想系列数据
INSERT INTO meditation_series (id, title, subtitle, description, cover, category_id, episode_count, total_duration, recommend_duration, difficulty, status, sort, create_by, create_time, update_by, update_time) VALUES
-- 情绪调节系列
(1, '情绪平衡训练', '7天情绪管理课程', '通过冥想练习，学会观察和接纳情绪，培养内心的平静与智慧', '/static/images/series/emotion-1.jpg', 1, 7, 3150, 450, 1, 0, 1, 'admin', NOW(), 'admin', NOW()),
(2, '焦虑缓解冥想', '告别焦虑困扰', '专为缓解焦虑设计的冥想练习，帮助您找回内心的宁静', '/static/images/series/emotion-2.jpg', 1, 5, 2250, 450, 2, 0, 2, 'admin', NOW(), 'admin', NOW()),
(3, '愤怒管理练习', '控制情绪的艺术', '学习如何识别和转化愤怒，培养耐心与慈悲', '/static/images/series/emotion-3.jpg', 1, 6, 2700, 450, 2, 0, 3, 'admin', NOW(), 'admin', NOW()),

-- 提升专注系列
(4, '专注力训练营', '21天专注力提升', '系统性的专注力训练，帮助您在工作和学习中保持高效', '/static/images/series/focus-1.jpg', 2, 21, 9450, 450, 1, 0, 1, 'admin', NOW(), 'admin', NOW()),
(5, '正念工作法', '职场高效冥想', '将正念融入工作，提高效率，减少职业倦怠', '/static/images/series/focus-2.jpg', 2, 10, 4500, 450, 2, 0, 2, 'admin', NOW(), 'admin', NOW()),
(6, '深度专注冥想', '进入心流状态', '帮助您进入深度专注状态，体验心流的美妙', '/static/images/series/focus-3.jpg', 2, 8, 3600, 450, 3, 0, 3, 'admin', NOW(), 'admin', NOW()),

-- 改善睡眠系列
(7, '深度睡眠引导', '7夜好眠计划', '专业的睡眠引导，帮助您快速入睡，提高睡眠质量', '/static/images/series/sleep-1.jpg', 3, 7, 4200, 600, 1, 0, 1, 'admin', NOW(), 'admin', NOW()),
(8, '失眠调理冥想', '告别失眠困扰', '针对失眠问题的专业冥想练习，重建健康睡眠模式', '/static/images/series/sleep-2.jpg', 3, 14, 8400, 600, 2, 0, 2, 'admin', NOW(), 'admin', NOW()),
(9, '午休充电冥想', '高效午休技巧', '短时间内恢复精力，提高下午工作效率', '/static/images/series/sleep-3.jpg', 3, 5, 1500, 300, 1, 0, 3, 'admin', NOW(), 'admin', NOW()),

-- 放松减压系列
(10, '压力释放练习', '14天减压计划', '系统性的压力管理方案，帮助您有效应对生活压力', '/static/images/series/relax-1.jpg', 4, 14, 6300, 450, 1, 0, 1, 'admin', NOW(), 'admin', NOW()),
(11, '身体扫描冥想', '深度放松技巧', '通过身体扫描，释放紧张，达到深度放松状态', '/static/images/series/relax-2.jpg', 4, 8, 3600, 450, 2, 0, 2, 'admin', NOW(), 'admin', NOW()),
(12, '呼吸放松法', '呼吸的力量', '掌握各种呼吸技巧，随时随地放松身心', '/static/images/series/relax-3.jpg', 4, 6, 1800, 300, 1, 0, 3, 'admin', NOW(), 'admin', NOW());

-- 3. 插入冥想音频数据
INSERT INTO meditation_track (id, title, description, audio_url, cover, duration_sec, category_id, series_id, episode_number, difficulty, play_count, status, create_by, create_time, update_by, update_time) VALUES
-- 情绪调节音频
(1, '观察情绪的流动', '学习以旁观者的角度观察情绪', '/audio/emotion-1.mp3', '/static/images/track/emotion-1.jpg', 600, 1, 1, 1, 1, 1234, 0, 'admin', NOW(), 'admin', NOW()),
(2, '接纳当下的感受', '练习无条件接纳自己的情绪', '/audio/emotion-2.mp3', '/static/images/track/emotion-2.jpg', 720, 1, 1, 2, 1, 987, 0, 'admin', NOW(), 'admin', NOW()),
(3, '情绪标签练习', '为情绪命名，增强情绪觉察', '/audio/emotion-3.mp3', '/static/images/track/emotion-3.jpg', 540, 1, 1, 3, 1, 876, 0, 'admin', NOW(), 'admin', NOW()),
(4, '慈悲心练习', '培养对自己和他人的慈悲', '/audio/emotion-4.mp3', '/static/images/track/emotion-4.jpg', 900, 1, 1, 4, 2, 765, 0, 'admin', NOW(), 'admin', NOW()),
(5, '情绪转化冥想', '将负面情绪转化为正能量', '/audio/emotion-5.mp3', '/static/images/track/emotion-5.jpg', 780, 1, 1, 5, 2, 654, 0, 'admin', NOW(), 'admin', NOW()),

-- 提升专注音频
(6, '数息观专注练习', '通过数息培养专注力', '/audio/focus-1.mp3', '/static/images/track/focus-1.jpg', 600, 2, 4, 1, 1, 2345, 0, 'admin', NOW(), 'admin', NOW()),
(7, '单点专注冥想', '将注意力集中在一点', '/audio/focus-2.mp3', '/static/images/track/focus-2.jpg', 720, 2, 4, 2, 2, 1987, 0, 'admin', NOW(), 'admin', NOW()),
(8, '开放觉察练习', '培养开放而专注的觉察力', '/audio/focus-3.mp3', '/static/images/track/focus-3.jpg', 840, 2, 4, 3, 2, 1654, 0, 'admin', NOW(), 'admin', NOW()),
(9, '工作间隙冥想', '5分钟快速恢复专注', '/audio/focus-4.mp3', '/static/images/track/focus-4.jpg', 300, 2, 5, 1, 1, 3456, 0, 'admin', NOW(), 'admin', NOW()),
(10, '会前准备冥想', '提升会议效率的冥想', '/audio/focus-5.mp3', '/static/images/track/focus-5.jpg', 420, 2, 5, 2, 1, 2876, 0, 'admin', NOW(), 'admin', NOW()),

-- 改善睡眠音频
(11, '渐进式肌肉放松', '帮助身体完全放松', '/audio/sleep-1.mp3', '/static/images/track/sleep-1.jpg', 900, 3, 7, 1, 1, 4567, 0, 'admin', NOW(), 'admin', NOW()),
(12, '睡前呼吸练习', '调整呼吸，准备入睡', '/audio/sleep-2.mp3', '/static/images/track/sleep-2.jpg', 600, 3, 7, 2, 1, 3987, 0, 'admin', NOW(), 'admin', NOW()),
(13, '身体扫描入睡法', '逐步放松全身进入睡眠', '/audio/sleep-3.mp3', '/static/images/track/sleep-3.jpg', 1200, 3, 7, 3, 1, 3654, 0, 'admin', NOW(), 'admin', NOW()),
(14, '梦境引导冥想', '引导进入美好梦境', '/audio/sleep-4.mp3', '/static/images/track/sleep-4.jpg', 1500, 3, 7, 4, 2, 2987, 0, 'admin', NOW(), 'admin', NOW()),
(15, '深度睡眠音乐', '助眠背景音乐', '/audio/sleep-5.mp3', '/static/images/track/sleep-5.jpg', 3600, 3, 7, 5, 1, 5432, 0, 'admin', NOW(), 'admin', NOW()),

-- 放松减压音频
(16, '压力释放呼吸法', '通过呼吸释放压力', '/audio/relax-1.mp3', '/static/images/track/relax-1.jpg', 480, 4, 10, 1, 1, 3456, 0, 'admin', NOW(), 'admin', NOW()),
(17, '肩颈放松练习', '缓解肩颈紧张', '/audio/relax-2.mp3', '/static/images/track/relax-2.jpg', 600, 4, 10, 2, 1, 2987, 0, 'admin', NOW(), 'admin', NOW()),
(18, '全身放松扫描', '系统放松全身肌肉', '/audio/relax-3.mp3', '/static/images/track/relax-3.jpg', 900, 4, 11, 1, 2, 2654, 0, 'admin', NOW(), 'admin', NOW()),
(19, '自然冥想放松', '想象自然场景放松身心', '/audio/relax-4.mp3', '/static/images/track/relax-4.jpg', 720, 4, 11, 2, 1, 2345, 0, 'admin', NOW(), 'admin', NOW()),
(20, '快速减压技巧', '3分钟快速减压', '/audio/relax-5.mp3', '/static/images/track/relax-5.jpg', 180, 4, 12, 1, 1, 4567, 0, 'admin', NOW(), 'admin', NOW());

-- 4. 插入冥想文章数据
INSERT INTO meditation_article (id, title, summary, content, cover, author, category_id, view_count, like_count, status, create_by, create_time, update_by, update_time) VALUES
(1, '冥想入门：从零开始的正念之旅', '了解冥想的基本概念和入门技巧', '<h2>什么是冥想？</h2><p>冥想是一种训练注意力和觉察力的练习...</p><h2>冥想的好处</h2><p>科学研究表明，规律的冥想练习可以...</p><h2>如何开始冥想</h2><p>1. 选择安静的环境<br>2. 采用舒适的坐姿<br>3. 专注于呼吸...</p>', '/static/images/article/article-1.jpg', '冥想导师', 1, 5678, 234, 0, 'admin', NOW(), 'admin', NOW()),

(2, '科学证实：冥想对大脑的积极影响', '探索冥想如何改变大脑结构和功能', '<h2>神经可塑性与冥想</h2><p>最新的神经科学研究发现...</p><h2>冥想对大脑的具体影响</h2><p>1. 增加灰质密度<br>2. 强化前额叶皮层<br>3. 减少杏仁核活动...</p>', '/static/images/article/article-2.jpg', '神经科学家', 2, 4567, 189, 0, 'admin', NOW(), 'admin', NOW()),

(3, '睡眠质量差？试试这些冥想技巧', '改善睡眠的实用冥想方法', '<h2>睡眠与冥想的关系</h2><p>良好的睡眠对健康至关重要...</p><h2>睡前冥想技巧</h2><p>1. 身体扫描法<br>2. 4-7-8呼吸法<br>3. 渐进式肌肉放松...</p>', '/static/images/article/article-3.jpg', '睡眠专家', 3, 6789, 312, 0, 'admin', NOW(), 'admin', NOW()),

(4, '职场压力管理：正念冥想的应用', '如何在工作中运用冥想技巧', '<h2>职场压力的来源</h2><p>现代职场充满各种压力...</p><h2>工作中的冥想练习</h2><p>1. 会议前的准备冥想<br>2. 午休时的恢复冥想<br>3. 下班后的释压冥想...</p>', '/static/images/article/article-4.jpg', '职场教练', 4, 3456, 145, 0, 'admin', NOW(), 'admin', NOW()),

(5, '情绪管理的艺术：通过冥想培养情商', '学习如何通过冥想提升情绪智力', '<h2>认识你的情绪</h2><p>情绪是人类体验的重要部分...</p><h2>冥想与情绪调节</h2><p>通过正念觉察，我们可以...</p>', '/static/images/article/article-5.jpg', '心理咨询师', 1, 4321, 198, 0, 'admin', NOW(), 'admin', NOW()),

(6, '儿童冥想：培养孩子的专注力', '适合儿童的冥想练习方法', '<h2>为什么儿童需要冥想</h2><p>在数字时代，孩子们面临着...</p><h2>儿童冥想技巧</h2><p>1. 呼吸小熊练习<br>2. 彩虹想象冥想<br>3. 感恩日记...</p>', '/static/images/article/article-6.jpg', '儿童教育专家', 2, 2987, 167, 0, 'admin', NOW(), 'admin', NOW()),

(7, '运动与冥想：动态冥想的魅力', '探索行走冥想和瑜伽冥想', '<h2>什么是动态冥想</h2><p>不同于静坐冥想，动态冥想...</p><h2>动态冥想的形式</h2><p>1. 行走冥想<br>2. 太极冥想<br>3. 瑜伽冥想...</p>', '/static/images/article/article-7.jpg', '瑜伽导师', 4, 3654, 156, 0, 'admin', NOW(), 'admin', NOW()),

(8, '冥想与创造力：激发灵感的秘密', '如何通过冥想提升创造力', '<h2>创造力的本质</h2><p>创造力来自于开放的心态...</p><h2>冥想激发创造力</h2><p>研究表明，冥想可以...</p>', '/static/images/article/article-8.jpg', '创意总监', 2, 2876, 134, 0, 'admin', NOW(), 'admin', NOW()),

(9, '慈悲冥想：培养爱与善意', '学习慈悲冥想的练习方法', '<h2>什么是慈悲冥想</h2><p>慈悲冥想是一种培养爱心的练习...</p><h2>慈悲冥想的步骤</h2><p>1. 对自己的慈悲<br>2. 对亲人的慈悲<br>3. 对陌生人的慈悲...</p>', '/static/images/article/article-9.jpg', '佛学老师', 1, 3987, 234, 0, 'admin', NOW(), 'admin', NOW()),

(10, '冥想误区：避免这些常见错误', '初学者容易犯的冥想错误', '<h2>常见的冥想误区</h2><p>1. 认为必须清空思绪<br>2. 期待立即见效<br>3. 姿势过于僵硬...</p><h2>正确的冥想态度</h2><p>保持开放和接纳的心态...</p>', '/static/images/article/article-10.jpg', '冥想教练', 1, 4567, 189, 0, 'admin', NOW(), 'admin', NOW());

-- 5. 插入热门搜索关键词
INSERT INTO meditation_hot_keyword (id, keyword, search_count, status, create_time) VALUES
(1, '睡眠', 9876, 0, NOW()),
(2, '焦虑', 8765, 0, NOW()),
(3, '专注', 7654, 0, NOW()),
(4, '放松', 6543, 0, NOW()),
(5, '压力', 5432, 0, NOW()),
(6, '呼吸', 4321, 0, NOW()),
(7, '正念', 3210, 0, NOW()),
(8, '情绪', 2109, 0, NOW()),
(9, '冥想入门', 1098, 0, NOW()),
(10, '失眠', 987, 0, NOW());

-- 6. 插入轮播图数据
INSERT INTO meditation_banner (id, title, image_url, link_url, link_type, target_id, sort, status, create_time) VALUES
(1, '21天冥想挑战', '/static/images/banner/banner-1.jpg', NULL, 'series', 4, 1, 0, NOW()),
(2, '深度睡眠计划', '/static/images/banner/banner-2.jpg', NULL, 'series', 7, 2, 0, NOW()),
(3, '缓解焦虑指南', '/static/images/banner/banner-3.jpg', NULL, 'article', 1, 3, 0, NOW()),
(4, '新手入门课程', '/static/images/banner/banner-4.jpg', NULL, 'series', 1, 4, 0, NOW()),
(5, '职场减压秘籍', '/static/images/banner/banner-5.jpg', NULL, 'article', 4, 5, 0, NOW());

-- 7. 插入常见问题数据
INSERT INTO meditation_faq (id, question, answer, category, sort, status, create_time) VALUES
(1, '如何开始冥想？', '选择一个安静的环境，采用舒适的坐姿，闭上眼睛，专注于呼吸。初学者可以从5-10分钟开始，逐渐增加时长。', '入门', 1, 0, NOW()),
(2, '冥想的最佳时间是什么时候？', '早晨起床后和晚上睡前是最佳的冥想时间。早晨冥想可以帮助您开启美好的一天，晚上冥想有助于放松身心，改善睡眠质量。', '入门', 2, 0, NOW()),
(3, '冥想时总是走神怎么办？', '走神是正常现象，不必自责。当您意识到走神时，温柔地将注意力带回到呼吸上。随着练习的增加，专注力会逐渐提升。', '技巧', 3, 0, NOW()),
(4, '需要特殊的装备吗？', '冥想不需要特殊装备。您只需要一个安静的空间和舒适的坐垫或椅子即可。如果愿意，可以使用冥想垫或瑜伽垫增加舒适度。', '入门', 4, 0, NOW()),
(5, '冥想有什么好处？', '冥想可以减轻压力和焦虑，提高专注力，改善睡眠质量，增强自我意识，促进情绪稳定，提升整体幸福感。', '效果', 5, 0, NOW()),
(6, '每天需要冥想多长时间？', '初学者可以从每天5-10分钟开始。随着练习的深入，可以逐渐增加到20-30分钟。重要的是保持规律性，而不是时长。', '技巧', 6, 0, NOW()),
(7, '如何知道自己冥想是否正确？', '没有"正确"或"错误"的冥想方式。如果您感到更加平静、专注和放松，那就是有效的冥想。每个人的体验都是独特的。', '技巧', 7, 0, NOW()),
(8, '可以躺着冥想吗？', '可以，但坐姿通常更推荐，因为躺着容易睡着。如果选择躺姿，建议保持清醒的意识，避免进入睡眠状态。', '技巧', 8, 0, NOW()),
(9, '冥想和睡觉有什么区别？', '冥想是保持清醒觉察的状态，而睡眠是意识降低的状态。冥想时要保持警觉但放松，观察思维和感受而不被其带走。', '概念', 9, 0, NOW()),
(10, '冥想会有副作用吗？', '正确的冥想练习通常是安全的。但如果有严重的心理问题，建议在专业指导下进行。初期可能会感到不适应，这是正常的。', '安全', 10, 0, NOW());

-- 添加一些测试用户数据（如果需要）
-- INSERT INTO sys_user ... 

-- 添加一些测试的收藏记录（需要先有用户）
-- INSERT INTO meditation_favorite ...

-- 添加一些测试的播放记录（需要先有用户）
-- INSERT INTO meditation_play_history ...

COMMIT;