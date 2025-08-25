-- 冥想应用测试数据生成脚本
-- 注意：分类数据已存在，不再插入

-- 清理现有数据（可选，分类数据除外）
-- DELETE FROM meditation_track WHERE 1=1;
-- DELETE FROM meditation_series WHERE 1=1;
-- DELETE FROM meditation_article WHERE 1=1;
-- DELETE FROM meditation_hot_keyword WHERE 1=1;
-- DELETE FROM meditation_banner WHERE 1=1;
-- DELETE FROM meditation_faq WHERE 1=1;

-- 1. 分类数据已存在，不需要插入
-- 假设现有分类ID为：1-情绪调节, 2-提升专注, 3-改善睡眠, 4-放松减压

-- 2. 插入冥想系列数据
INSERT INTO meditation_series (title, subtitle, description, cover, category_id, episode_count, total_duration, recommend_duration, difficulty, status, sort, create_by, create_time, update_by, update_time) VALUES
-- 情绪调节系列 (category_id = 1)
('情绪平衡训练', '7天情绪管理课程', '通过冥想练习，学会观察和接纳情绪，培养内心的平静与智慧', '/profile/upload/2025/01/series_emotion_1.jpg', 1, 7, 3150, 450, 1, 0, 1, 'admin', NOW(), 'admin', NOW()),
('焦虑缓解冥想', '告别焦虑困扰', '专为缓解焦虑设计的冥想练习，帮助您找回内心的宁静', '/profile/upload/2025/01/series_emotion_2.jpg', 1, 5, 2250, 450, 2, 0, 2, 'admin', NOW(), 'admin', NOW()),
('愤怒管理练习', '控制情绪的艺术', '学习如何识别和转化愤怒，培养耐心与慈悲', '/profile/upload/2025/01/series_emotion_3.jpg', 1, 6, 2700, 450, 2, 0, 3, 'admin', NOW(), 'admin', NOW()),
('情绪觉察入门', '认识你的情绪', '帮助初学者建立情绪觉察能力，提升情商', '/profile/upload/2025/01/series_emotion_4.jpg', 1, 8, 3600, 450, 1, 0, 4, 'admin', NOW(), 'admin', NOW()),

-- 提升专注系列 (category_id = 2)
('专注力训练营', '21天专注力提升', '系统性的专注力训练，帮助您在工作和学习中保持高效', '/profile/upload/2025/01/series_focus_1.jpg', 2, 21, 9450, 450, 1, 0, 1, 'admin', NOW(), 'admin', NOW()),
('正念工作法', '职场高效冥想', '将正念融入工作，提高效率，减少职业倦怠', '/profile/upload/2025/01/series_focus_2.jpg', 2, 10, 4500, 450, 2, 0, 2, 'admin', NOW(), 'admin', NOW()),
('深度专注冥想', '进入心流状态', '帮助您进入深度专注状态，体验心流的美妙', '/profile/upload/2025/01/series_focus_3.jpg', 2, 8, 3600, 450, 3, 0, 3, 'admin', NOW(), 'admin', NOW()),
('学习力提升计划', '高效学习冥想', '专为学生和终身学习者设计的专注力训练', '/profile/upload/2025/01/series_focus_4.jpg', 2, 12, 5400, 450, 2, 0, 4, 'admin', NOW(), 'admin', NOW()),

-- 改善睡眠系列 (category_id = 3)
('深度睡眠引导', '7夜好眠计划', '专业的睡眠引导，帮助您快速入睡，提高睡眠质量', '/profile/upload/2025/01/series_sleep_1.jpg', 3, 7, 4200, 600, 1, 0, 1, 'admin', NOW(), 'admin', NOW()),
('失眠调理冥想', '告别失眠困扰', '针对失眠问题的专业冥想练习，重建健康睡眠模式', '/profile/upload/2025/01/series_sleep_2.jpg', 3, 14, 8400, 600, 2, 0, 2, 'admin', NOW(), 'admin', NOW()),
('午休充电冥想', '高效午休技巧', '短时间内恢复精力，提高下午工作效率', '/profile/upload/2025/01/series_sleep_3.jpg', 3, 5, 1500, 300, 1, 0, 3, 'admin', NOW(), 'admin', NOW()),
('睡前放松仪式', '建立睡眠习惯', '通过固定的睡前冥想，培养良好的睡眠习惯', '/profile/upload/2025/01/series_sleep_4.jpg', 3, 10, 6000, 600, 1, 0, 4, 'admin', NOW(), 'admin', NOW()),

-- 放松减压系列 (category_id = 4)
('压力释放练习', '14天减压计划', '系统性的压力管理方案，帮助您有效应对生活压力', '/profile/upload/2025/01/series_relax_1.jpg', 4, 14, 6300, 450, 1, 0, 1, 'admin', NOW(), 'admin', NOW()),
('身体扫描冥想', '深度放松技巧', '通过身体扫描，释放紧张，达到深度放松状态', '/profile/upload/2025/01/series_relax_2.jpg', 4, 8, 3600, 450, 2, 0, 2, 'admin', NOW(), 'admin', NOW()),
('呼吸放松法', '呼吸的力量', '掌握各种呼吸技巧，随时随地放松身心', '/profile/upload/2025/01/series_relax_3.jpg', 4, 6, 1800, 300, 1, 0, 3, 'admin', NOW(), 'admin', NOW()),
('自然冥想之旅', '大自然的治愈', '通过想象自然场景，获得深度放松和内心平静', '/profile/upload/2025/01/series_relax_4.jpg', 4, 9, 4050, 450, 1, 0, 4, 'admin', NOW(), 'admin', NOW());

-- 3. 插入冥想音频数据
INSERT INTO meditation_track (title, description, audio_url, cover, duration_sec, category_id, series_id, episode_number, difficulty, play_count, status, create_by, create_time, update_by, update_time) VALUES
-- 情绪调节音频 (category_id = 1)
('观察情绪的流动', '学习以旁观者的角度观察情绪，不被情绪控制', '/profile/upload/audio/emotion_1.mp3', '/profile/upload/2025/01/track_emotion_1.jpg', 600, 1, 1, 1, 1, 1234, 0, 'admin', NOW(), 'admin', NOW()),
('接纳当下的感受', '练习无条件接纳自己的情绪，与情绪和平共处', '/profile/upload/audio/emotion_2.mp3', '/profile/upload/2025/01/track_emotion_2.jpg', 720, 1, 1, 2, 1, 987, 0, 'admin', NOW(), 'admin', NOW()),
('情绪标签练习', '为情绪命名，增强情绪觉察和管理能力', '/profile/upload/audio/emotion_3.mp3', '/profile/upload/2025/01/track_emotion_3.jpg', 540, 1, 1, 3, 1, 876, 0, 'admin', NOW(), 'admin', NOW()),
('慈悲心练习', '培养对自己和他人的慈悲，化解负面情绪', '/profile/upload/audio/emotion_4.mp3', '/profile/upload/2025/01/track_emotion_4.jpg', 900, 1, 1, 4, 2, 765, 0, 'admin', NOW(), 'admin', NOW()),
('情绪转化冥想', '将负面情绪转化为正能量的实用技巧', '/profile/upload/audio/emotion_5.mp3', '/profile/upload/2025/01/track_emotion_5.jpg', 780, 1, 1, 5, 2, 654, 0, 'admin', NOW(), 'admin', NOW()),
('内在小孩疗愈', '疗愈童年创伤，释放情绪包袱', '/profile/upload/audio/emotion_6.mp3', '/profile/upload/2025/01/track_emotion_6.jpg', 900, 1, 2, 1, 2, 543, 0, 'admin', NOW(), 'admin', NOW()),
('焦虑呼吸法', '通过特殊呼吸技巧快速缓解焦虑', '/profile/upload/audio/emotion_7.mp3', '/profile/upload/2025/01/track_emotion_7.jpg', 480, 1, 2, 2, 1, 432, 0, 'admin', NOW(), 'admin', NOW()),

-- 提升专注音频 (category_id = 2)
('数息观专注练习', '通过数息培养专注力的基础练习', '/profile/upload/audio/focus_1.mp3', '/profile/upload/2025/01/track_focus_1.jpg', 600, 2, 5, 1, 1, 2345, 0, 'admin', NOW(), 'admin', NOW()),
('单点专注冥想', '将注意力集中在一点，训练深度专注', '/profile/upload/audio/focus_2.mp3', '/profile/upload/2025/01/track_focus_2.jpg', 720, 2, 5, 2, 2, 1987, 0, 'admin', NOW(), 'admin', NOW()),
('开放觉察练习', '培养开放而专注的觉察力，提升整体注意力', '/profile/upload/audio/focus_3.mp3', '/profile/upload/2025/01/track_focus_3.jpg', 840, 2, 5, 3, 2, 1654, 0, 'admin', NOW(), 'admin', NOW()),
('工作间隙冥想', '5分钟快速恢复专注，提高工作效率', '/profile/upload/audio/focus_4.mp3', '/profile/upload/2025/01/track_focus_4.jpg', 300, 2, 6, 1, 1, 3456, 0, 'admin', NOW(), 'admin', NOW()),
('会前准备冥想', '提升会议效率的快速冥想练习', '/profile/upload/audio/focus_5.mp3', '/profile/upload/2025/01/track_focus_5.jpg', 420, 2, 6, 2, 1, 2876, 0, 'admin', NOW(), 'admin', NOW()),
('创意激发冥想', '打开思维，激发创造力和灵感', '/profile/upload/audio/focus_6.mp3', '/profile/upload/2025/01/track_focus_6.jpg', 600, 2, 7, 1, 2, 2234, 0, 'admin', NOW(), 'admin', NOW()),
('深度工作准备', '进入深度工作状态的引导冥想', '/profile/upload/audio/focus_7.mp3', '/profile/upload/2025/01/track_focus_7.jpg', 540, 2, 7, 2, 2, 1998, 0, 'admin', NOW(), 'admin', NOW()),

-- 改善睡眠音频 (category_id = 3)
('渐进式肌肉放松', '帮助身体完全放松，准备进入深度睡眠', '/profile/upload/audio/sleep_1.mp3', '/profile/upload/2025/01/track_sleep_1.jpg', 900, 3, 9, 1, 1, 4567, 0, 'admin', NOW(), 'admin', NOW()),
('睡前呼吸练习', '调整呼吸节奏，引导身心进入睡眠状态', '/profile/upload/audio/sleep_2.mp3', '/profile/upload/2025/01/track_sleep_2.jpg', 600, 3, 9, 2, 1, 3987, 0, 'admin', NOW(), 'admin', NOW()),
('身体扫描入睡法', '逐步放松全身每个部位，自然进入睡眠', '/profile/upload/audio/sleep_3.mp3', '/profile/upload/2025/01/track_sleep_3.jpg', 1200, 3, 9, 3, 1, 3654, 0, 'admin', NOW(), 'admin', NOW()),
('梦境引导冥想', '引导进入美好梦境，提升睡眠质量', '/profile/upload/audio/sleep_4.mp3', '/profile/upload/2025/01/track_sleep_4.jpg', 1500, 3, 9, 4, 2, 2987, 0, 'admin', NOW(), 'admin', NOW()),
('深度睡眠音乐', '特殊频率的助眠背景音乐', '/profile/upload/audio/sleep_5.mp3', '/profile/upload/2025/01/track_sleep_5.jpg', 3600, 3, 9, 5, 1, 5432, 0, 'admin', NOW(), 'admin', NOW()),
('午休快速入睡', '15分钟高效午休引导', '/profile/upload/audio/sleep_6.mp3', '/profile/upload/2025/01/track_sleep_6.jpg', 900, 3, 11, 1, 1, 3210, 0, 'admin', NOW(), 'admin', NOW()),
('失眠急救冥想', '失眠时的紧急助眠练习', '/profile/upload/audio/sleep_7.mp3', '/profile/upload/2025/01/track_sleep_7.jpg', 1800, 3, 10, 1, 2, 2876, 0, 'admin', NOW(), 'admin', NOW()),

-- 放松减压音频 (category_id = 4)
('压力释放呼吸法', '通过特殊呼吸技巧快速释放压力', '/profile/upload/audio/relax_1.mp3', '/profile/upload/2025/01/track_relax_1.jpg', 480, 4, 13, 1, 1, 3456, 0, 'admin', NOW(), 'admin', NOW()),
('肩颈放松练习', '缓解肩颈紧张，释放工作压力', '/profile/upload/audio/relax_2.mp3', '/profile/upload/2025/01/track_relax_2.jpg', 600, 4, 13, 2, 1, 2987, 0, 'admin', NOW(), 'admin', NOW()),
('全身放松扫描', '系统放松全身肌肉，达到深度放松', '/profile/upload/audio/relax_3.mp3', '/profile/upload/2025/01/track_relax_3.jpg', 900, 4, 14, 1, 2, 2654, 0, 'admin', NOW(), 'admin', NOW()),
('自然冥想放松', '想象自然场景，获得身心放松', '/profile/upload/audio/relax_4.mp3', '/profile/upload/2025/01/track_relax_4.jpg', 720, 4, 14, 2, 1, 2345, 0, 'admin', NOW(), 'admin', NOW()),
('快速减压技巧', '3分钟快速减压，随时可用', '/profile/upload/audio/relax_5.mp3', '/profile/upload/2025/01/track_relax_5.jpg', 180, 4, 15, 1, 1, 4567, 0, 'admin', NOW(), 'admin', NOW()),
('海浪冥想', '聆听海浪声，深度放松身心', '/profile/upload/audio/relax_6.mp3', '/profile/upload/2025/01/track_relax_6.jpg', 1200, 4, 16, 1, 1, 3890, 0, 'admin', NOW(), 'admin', NOW()),
('森林漫步冥想', '想象森林漫步，回归内心宁静', '/profile/upload/audio/relax_7.mp3', '/profile/upload/2025/01/track_relax_7.jpg', 900, 4, 16, 2, 1, 3234, 0, 'admin', NOW(), 'admin', NOW());

-- 4. 插入冥想文章数据
INSERT INTO meditation_article (title, summary, content, cover, author, category_id, view_count, like_count, status, create_by, create_time, update_by, update_time) VALUES
('冥想入门：从零开始的正念之旅', '了解冥想的基本概念和入门技巧，开启您的冥想之旅', '<h2>什么是冥想？</h2><p>冥想是一种训练注意力和觉察力的练习，通过特定的技巧帮助我们达到心理清晰和情绪平静的状态。</p><h2>冥想的好处</h2><p>科学研究表明，规律的冥想练习可以：<br>• 减轻压力和焦虑<br>• 提高专注力<br>• 改善睡眠质量<br>• 增强情绪管理能力<br>• 提升整体幸福感</p><h2>如何开始冥想</h2><p>1. 选择安静的环境<br>2. 采用舒适的坐姿<br>3. 专注于呼吸<br>4. 观察但不评判念头<br>5. 从5分钟开始，逐渐增加时长</p><h2>初学者常见问题</h2><p>• 总是走神怎么办？这很正常，温柔地将注意力带回即可<br>• 需要特殊装备吗？不需要，舒适即可<br>• 什么时间最好？早晨和睡前都很适合</p>', '/profile/upload/2025/01/article_1.jpg', '资深冥想导师', 1, 5678, 234, 0, 'admin', NOW(), 'admin', NOW()),

('科学证实：冥想对大脑的积极影响', '探索冥想如何改变大脑结构和功能，了解其背后的科学原理', '<h2>神经可塑性与冥想</h2><p>最新的神经科学研究发现，规律的冥想练习可以改变大脑的结构和功能。这种现象被称为神经可塑性。</p><h2>冥想对大脑的具体影响</h2><p>1. <strong>增加灰质密度</strong>：海马体和前额叶皮层的灰质增加<br>2. <strong>强化前额叶皮层</strong>：提升决策和情绪调节能力<br>3. <strong>减少杏仁核活动</strong>：降低压力反应<br>4. <strong>增强默认模式网络</strong>：提高自我觉察</p><h2>研究数据</h2><p>哈佛大学的研究显示，8周的冥想练习就能观察到大脑结构的变化。参与者的海马体灰质密度增加了5%。</p>', '/profile/upload/2025/01/article_2.jpg', '神经科学研究员', 2, 4567, 189, 0, 'admin', NOW(), 'admin', NOW()),

('睡眠质量差？试试这些冥想技巧', '改善睡眠的实用冥想方法，帮助您获得更好的休息', '<h2>睡眠与冥想的关系</h2><p>良好的睡眠对健康至关重要，而冥想是改善睡眠质量的有效方法。冥想可以帮助放松身心，减少入睡前的焦虑。</p><h2>睡前冥想技巧</h2><p>1. <strong>身体扫描法</strong>：从头到脚逐步放松每个部位<br>2. <strong>4-7-8呼吸法</strong>：吸气4秒，屏息7秒，呼气8秒<br>3. <strong>渐进式肌肉放松</strong>：先紧张后放松各肌肉群<br>4. <strong>想象练习</strong>：想象宁静的场景</p><h2>建立睡眠仪式</h2><p>• 固定时间进行睡前冥想<br>• 创造舒适的睡眠环境<br>• 避免睡前使用电子设备<br>• 保持规律的作息时间</p>', '/profile/upload/2025/01/article_3.jpg', '睡眠专家', 3, 6789, 312, 0, 'admin', NOW(), 'admin', NOW()),

('职场压力管理：正念冥想的应用', '如何在工作中运用冥想技巧，提升效率减少压力', '<h2>职场压力的来源</h2><p>现代职场充满各种压力：紧张的截止日期、复杂的人际关系、高强度的工作负荷等。正念冥想可以帮助我们更好地应对这些挑战。</p><h2>工作中的冥想练习</h2><p>1. <strong>会议前的准备冥想</strong>：3分钟呼吸练习，提升专注<br>2. <strong>午休时的恢复冥想</strong>：15分钟引导冥想，快速充电<br>3. <strong>下班后的释压冥想</strong>：释放工作压力，切换状态</p><h2>提升工作效率</h2><p>研究表明，每天10分钟的冥想可以：<br>• 提高专注力25%<br>• 减少工作失误<br>• 增强创造力<br>• 改善团队协作</p>', '/profile/upload/2025/01/article_4.jpg', '职场心理教练', 4, 3456, 145, 0, 'admin', NOW(), 'admin', NOW()),

('情绪管理的艺术：通过冥想培养情商', '学习如何通过冥想提升情绪智力，建立更好的人际关系', '<h2>认识你的情绪</h2><p>情绪是人类体验的重要部分，但很多人不知道如何有效管理情绪。冥想可以帮助我们更好地认识和管理情绪。</p><h2>冥想与情绪调节</h2><p>通过正念觉察，我们可以：<br>• 识别情绪的早期信号<br>• 观察情绪而不被其控制<br>• 选择更合适的反应方式<br>• 培养情绪韧性</p><h2>实践练习</h2><p>1. 情绪标签练习：为情绪命名<br>2. RAIN技巧：识别、接纳、探究、非认同<br>3. 慈悲冥想：培养对自己和他人的慈悲</p>', '/profile/upload/2025/01/article_5.jpg', '心理咨询师', 1, 4321, 198, 0, 'admin', NOW(), 'admin', NOW()),

('儿童冥想：培养孩子的专注力和情绪管理能力', '适合儿童的冥想练习方法，帮助孩子健康成长', '<h2>为什么儿童需要冥想</h2><p>在数字时代，孩子们面临着信息过载、注意力分散等挑战。冥想可以帮助他们培养专注力、情绪管理能力和内在平静。</p><h2>儿童冥想技巧</h2><p>1. <strong>呼吸小熊练习</strong>：把玩具熊放在肚子上，观察呼吸<br>2. <strong>彩虹想象冥想</strong>：想象彩虹的颜色<br>3. <strong>感恩日记</strong>：每天记录三件感恩的事<br>4. <strong>正念行走</strong>：慢慢走，感受每一步</p><h2>年龄适配建议</h2><p>• 3-5岁：2-3分钟的简单练习<br>• 6-8岁：5-8分钟的引导冥想<br>• 9-12岁：10-15分钟的多样化练习</p>', '/profile/upload/2025/01/article_6.jpg', '儿童教育专家', 2, 2987, 167, 0, 'admin', NOW(), 'admin', NOW()),

('运动与冥想：动态冥想的魅力', '探索行走冥想、瑜伽冥想等动态冥想形式', '<h2>什么是动态冥想</h2><p>不同于静坐冥想，动态冥想是在运动中保持正念觉察。这种练习方式特别适合难以静坐的人。</p><h2>动态冥想的形式</h2><p>1. <strong>行走冥想</strong>：专注于每一步的感受<br>2. <strong>太极冥想</strong>：缓慢流畅的动作配合呼吸<br>3. <strong>瑜伽冥想</strong>：体式练习中的觉察<br>4. <strong>跑步冥想</strong>：节奏性运动中的正念</p><h2>练习要点</h2><p>• 保持对身体感受的觉察<br>• 协调呼吸与动作<br>• 不追求完美，重在体验<br>• 从简单动作开始</p>', '/profile/upload/2025/01/article_7.jpg', '瑜伽导师', 4, 3654, 156, 0, 'admin', NOW(), 'admin', NOW()),

('冥想与创造力：激发灵感的秘密', '如何通过冥想提升创造力，找到创新的源泉', '<h2>创造力的本质</h2><p>创造力来自于开放的心态和灵活的思维。冥想可以帮助我们打破固有思维模式，激发创新灵感。</p><h2>冥想激发创造力</h2><p>研究表明，冥想可以：<br>• 增强发散性思维<br>• 提高问题解决能力<br>• 促进灵感涌现<br>• 减少创作焦虑</p><h2>创意冥想练习</h2><p>1. 开放监控冥想：不聚焦特定对象<br>2. 视觉化练习：想象创意场景<br>3. 自由书写冥想：冥想后自由书写<br>4. 音乐冥想：聆听音乐激发灵感</p>', '/profile/upload/2025/01/article_8.jpg', '创意总监', 2, 2876, 134, 0, 'admin', NOW(), 'admin', NOW()),

('慈悲冥想：培养爱与善意', '学习慈悲冥想的练习方法，提升人际关系质量', '<h2>什么是慈悲冥想</h2><p>慈悲冥想是一种培养爱心和善意的练习，源自佛教传统，现已被科学证实对心理健康有益。</p><h2>慈悲冥想的步骤</h2><p>1. <strong>对自己的慈悲</strong>：愿我快乐、健康、平安<br>2. <strong>对亲人的慈悲</strong>：将祝福延伸到家人朋友<br>3. <strong>对陌生人的慈悲</strong>：祝福不认识的人<br>4. <strong>对困难关系的慈悲</strong>：包容有矛盾的人<br>5. <strong>对所有生命的慈悲</strong>：祝福一切众生</p><h2>科学研究发现</h2><p>慈悲冥想可以增加大脑中与同理心相关的区域活动，提升人际关系满意度。</p>', '/profile/upload/2025/01/article_9.jpg', '正念导师', 1, 3987, 234, 0, 'admin', NOW(), 'admin', NOW()),

('冥想误区：避免这些常见错误', '初学者容易犯的冥想错误及纠正方法', '<h2>常见的冥想误区</h2><p>1. <strong>认为必须清空思绪</strong>：冥想不是停止思考，而是观察思维<br>2. <strong>期待立即见效</strong>：冥想需要持续练习才能看到效果<br>3. <strong>姿势过于僵硬</strong>：舒适比形式更重要<br>4. <strong>练习时间过长</strong>：初学者应从短时间开始<br>5. <strong>评判自己的表现</strong>：没有对错，只有体验</p><h2>正确的冥想态度</h2><p>• 保持开放和接纳的心态<br>• 对自己有耐心<br>• 规律练习比时长重要<br>• 找到适合自己的方式</p>', '/profile/upload/2025/01/article_10.jpg', '冥想教练', 1, 4567, 189, 0, 'admin', NOW(), 'admin', NOW());

-- 5. 插入热门搜索关键词
INSERT INTO meditation_hot_keyword (keyword, search_count, status, create_time) VALUES
('睡眠', 9876, 0, NOW()),
('焦虑', 8765, 0, NOW()),
('专注', 7654, 0, NOW()),
('放松', 6543, 0, NOW()),
('压力', 5432, 0, NOW()),
('呼吸', 4321, 0, NOW()),
('正念', 3210, 0, NOW()),
('情绪', 2109, 0, NOW()),
('冥想入门', 1098, 0, NOW()),
('失眠', 987, 0, NOW()),
('深度睡眠', 876, 0, NOW()),
('工作压力', 765, 0, NOW()),
('内心平静', 654, 0, NOW()),
('身心健康', 543, 0, NOW()),
('快速入睡', 432, 0, NOW());

-- 6. 插入轮播图数据
INSERT INTO meditation_banner (title, image_url, link_url, link_type, target_id, sort, status, create_time) VALUES
('21天专注力训练营', '/profile/upload/2025/01/banner_1.jpg', NULL, 'series', 5, 1, 0, NOW()),
('深度睡眠计划', '/profile/upload/2025/01/banner_2.jpg', NULL, 'series', 9, 2, 0, NOW()),
('缓解焦虑指南', '/profile/upload/2025/01/banner_3.jpg', NULL, 'article', 1, 3, 0, NOW()),
('新手入门课程', '/profile/upload/2025/01/banner_4.jpg', NULL, 'series', 1, 4, 0, NOW()),
('职场减压秘籍', '/profile/upload/2025/01/banner_5.jpg', NULL, 'article', 4, 5, 0, NOW());

-- 7. 插入常见问题数据
INSERT INTO meditation_faq (question, answer, category, sort, status, create_time) VALUES
('如何开始冥想？', '选择一个安静的环境，采用舒适的坐姿，闭上眼睛，专注于呼吸。初学者可以从5-10分钟开始，逐渐增加时长。', '入门指导', 1, 0, NOW()),
('冥想的最佳时间是什么时候？', '早晨起床后和晚上睡前是最佳的冥想时间。早晨冥想可以帮助您开启美好的一天，晚上冥想有助于放松身心，改善睡眠质量。', '入门指导', 2, 0, NOW()),
('冥想时总是走神怎么办？', '走神是正常现象，不必自责。当您意识到走神时，温柔地将注意力带回到呼吸上。随着练习的增加，专注力会逐渐提升。', '常见问题', 3, 0, NOW()),
('需要特殊的装备吗？', '冥想不需要特殊装备。您只需要一个安静的空间和舒适的坐垫或椅子即可。如果愿意，可以使用冥想垫或瑜伽垫增加舒适度。', '入门指导', 4, 0, NOW()),
('冥想有什么好处？', '冥想可以减轻压力和焦虑，提高专注力，改善睡眠质量，增强自我意识，促进情绪稳定，提升整体幸福感。', '效果说明', 5, 0, NOW()),
('每天需要冥想多长时间？', '初学者可以从每天5-10分钟开始。随着练习的深入，可以逐渐增加到20-30分钟。重要的是保持规律性，而不是时长。', '练习建议', 6, 0, NOW()),
('如何知道自己冥想是否正确？', '没有"正确"或"错误"的冥想方式。如果您感到更加平静、专注和放松，那就是有效的冥想。每个人的体验都是独特的。', '练习建议', 7, 0, NOW()),
('可以躺着冥想吗？', '可以，但坐姿通常更推荐，因为躺着容易睡着。如果选择躺姿，建议保持清醒的意识，避免进入睡眠状态。', '练习建议', 8, 0, NOW()),
('冥想和睡觉有什么区别？', '冥想是保持清醒觉察的状态，而睡眠是意识降低的状态。冥想时要保持警觉但放松，观察思维和感受而不被其带走。', '基础知识', 9, 0, NOW()),
('冥想会有副作用吗？', '正确的冥想练习通常是安全的。但如果有严重的心理问题，建议在专业指导下进行。初期可能会感到不适应，这是正常的。', '注意事项', 10, 0, NOW()),
('儿童可以练习冥想吗？', '可以。儿童冥想有助于提高专注力、情绪管理能力和学习效果。建议使用适合儿童年龄的引导方式，时间不宜过长。', '特殊人群', 11, 0, NOW()),
('冥想能治疗疾病吗？', '冥想可以作为辅助手段改善某些症状，如焦虑、失眠等，但不能替代医疗治疗。如有疾病，请先咨询医生。', '注意事项', 12, 0, NOW()),
('不同冥想方式有什么区别？', '常见的有正念冥想（觉察当下）、专注冥想（集中注意力）、慈悲冥想（培养善意）等，各有特点，可根据需求选择。', '冥想类型', 13, 0, NOW()),
('如何建立冥想习惯？', '选择固定的时间和地点，从短时间开始，使用提醒工具，记录进展，加入冥想社群获得支持，保持耐心和坚持。', '习惯养成', 14, 0, NOW()),
('冥想时应该想什么？', '冥想不是要想什么特定的内容，而是观察当下的体验。可以专注于呼吸、身体感受、声音等，关键是保持觉察。', '练习建议', 15, 0, NOW());

-- 提交事务
COMMIT;

-- 输出统计信息
SELECT '数据导入完成！' AS '状态';
SELECT '系列数据' AS '类型', COUNT(*) AS '数量' FROM meditation_series;
SELECT '音频数据' AS '类型', COUNT(*) AS '数量' FROM meditation_track;
SELECT '文章数据' AS '类型', COUNT(*) AS '数量' FROM meditation_article;
SELECT '热门关键词' AS '类型', COUNT(*) AS '数量' FROM meditation_hot_keyword;
SELECT '轮播图' AS '类型', COUNT(*) AS '数量' FROM meditation_banner;
SELECT '常见问题' AS '类型', COUNT(*) AS '数量' FROM meditation_faq;