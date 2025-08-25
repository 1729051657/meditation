package script;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.Random;
import java.util.UUID;

/**
 * 冥想应用测试数据生成器
 * 使用方法：
 * 1. 修改数据库连接信息
 * 2. 运行 main 方法
 */
public class DataGenerator {
    
    // 数据库连接配置
    private static final String DB_URL = "jdbc:mysql://localhost:3306/ry-vue?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "password";
    
    private static Connection conn;
    private static Random random = new Random();
    
    // 图片URL前缀
    private static final String IMAGE_BASE_URL = "/profile/upload/2025/01/";
    
    // 音频URL前缀
    private static final String AUDIO_BASE_URL = "/profile/upload/audio/2025/01/";
    
    public static void main(String[] args) {
        try {
            // 建立数据库连接
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            conn.setAutoCommit(false);
            
            System.out.println("开始生成测试数据...");
            
            // 生成数据
            generateCategories();
            generateSeries();
            generateTracks();
            generateArticles();
            generateHotKeywords();
            generateBanners();
            generateFAQs();
            
            // 提交事务
            conn.commit();
            System.out.println("测试数据生成完成！");
            
        } catch (Exception e) {
            e.printStackTrace();
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * 生成分类数据
     */
    private static void generateCategories() throws Exception {
        String sql = "INSERT INTO meditation_category (name, code, icon, description, sort, status, create_by, create_time, update_by, update_time) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = conn.prepareStatement(sql);
        
        String[][] categories = {
            {"情绪调节", "emotion", "emotion-regulation@2x_" + UUID.randomUUID().toString().substring(0, 8) + ".png", "帮助您管理情绪，保持内心平静"},
            {"提升专注", "focus", "improve-focus@2x_" + UUID.randomUUID().toString().substring(0, 8) + ".png", "提高注意力和专注度，增强工作效率"},
            {"改善睡眠", "sleep", "improve-sleep@2x_" + UUID.randomUUID().toString().substring(0, 8) + ".png", "改善睡眠质量，帮助您更好地休息"},
            {"放松减压", "relax", "relax-stress@2x_" + UUID.randomUUID().toString().substring(0, 8) + ".png", "释放压力，放松身心，恢复活力"},
            {"身心健康", "health", "health@2x_" + UUID.randomUUID().toString().substring(0, 8) + ".png", "促进身心健康，提升生活质量"},
            {"个人成长", "growth", "growth@2x_" + UUID.randomUUID().toString().substring(0, 8) + ".png", "助力个人成长，实现自我提升"}
        };
        
        for (int i = 0; i < categories.length; i++) {
            ps.setString(1, categories[i][0]);
            ps.setString(2, categories[i][1]);
            ps.setString(3, IMAGE_BASE_URL + categories[i][2]);
            ps.setString(4, categories[i][3]);
            ps.setInt(5, i + 1);
            ps.setInt(6, 0);
            ps.setString(7, "admin");
            ps.setTimestamp(8, new Timestamp(System.currentTimeMillis()));
            ps.setString(9, "admin");
            ps.setTimestamp(10, new Timestamp(System.currentTimeMillis()));
            ps.executeUpdate();
        }
        
        ps.close();
        System.out.println("分类数据生成完成：" + categories.length + " 条");
    }
    
    /**
     * 生成系列数据
     */
    private static void generateSeries() throws Exception {
        String sql = "INSERT INTO meditation_series (title, subtitle, description, cover, category_id, episode_count, total_duration, recommend_duration, difficulty, status, sort, create_by, create_time, update_by, update_time) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = conn.prepareStatement(sql);
        
        String[] seriesTitles = {
            "初学者入门", "7天基础训练", "21天进阶计划", "30天深度练习",
            "晨间唤醒", "午休充电", "睡前放松", "周末调整",
            "情绪管理课程", "压力释放训练", "专注力提升", "创造力激发",
            "身体扫描", "呼吸练习", "正念行走", "慈悲冥想"
        };
        
        String[] descriptions = {
            "适合零基础学员的入门课程",
            "循序渐进的基础训练计划",
            "系统性的进阶练习方案",
            "深入探索冥想的精髓",
            "开启美好一天的晨间练习",
            "快速恢复精力的午休方案",
            "改善睡眠质量的夜间练习",
            "周末深度放松与调整"
        };
        
        for (int i = 0; i < 30; i++) {
            ps.setString(1, seriesTitles[i % seriesTitles.length] + (i >= seriesTitles.length ? " " + (i / seriesTitles.length + 1) : ""));
            ps.setString(2, "第" + (i + 1) + "期 · 精选课程");
            ps.setString(3, descriptions[i % descriptions.length]);
            ps.setString(4, IMAGE_BASE_URL + "series_" + (i + 1) + "_" + UUID.randomUUID().toString().substring(0, 8) + ".jpg");
            ps.setInt(5, (i % 6) + 1); // 分类ID 1-6
            ps.setInt(6, random.nextInt(10) + 5); // 5-14集
            ps.setInt(7, (random.nextInt(10) + 5) * 60 * 15); // 总时长
            ps.setInt(8, random.nextInt(5) * 60 + 300); // 推荐时长 5-25分钟
            ps.setInt(9, (i % 3) + 1); // 难度 1-3
            ps.setInt(10, 0); // 状态
            ps.setInt(11, i + 1); // 排序
            ps.setString(12, "admin");
            ps.setTimestamp(13, new Timestamp(System.currentTimeMillis()));
            ps.setString(14, "admin");
            ps.setTimestamp(15, new Timestamp(System.currentTimeMillis()));
            ps.executeUpdate();
        }
        
        ps.close();
        System.out.println("系列数据生成完成：30 条");
    }
    
    /**
     * 生成音频数据
     */
    private static void generateTracks() throws Exception {
        String sql = "INSERT INTO meditation_track (title, description, audio_url, cover, duration_sec, category_id, series_id, episode_number, difficulty, play_count, status, create_by, create_time, update_by, update_time) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = conn.prepareStatement(sql);
        
        String[] trackTitles = {
            "呼吸觉察", "身体扫描", "声音冥想", "视觉冥想", "念头观察",
            "情绪释放", "能量平衡", "脉轮清理", "内在对话", "感恩练习",
            "慈悲心培养", "专注力训练", "创意激发", "直觉开发", "潜意识探索"
        };
        
        String[] descriptions = {
            "通过呼吸练习达到内心平静",
            "逐步放松身体各个部位",
            "聆听自然之声，放松身心",
            "通过视觉想象进入冥想状态",
            "观察念头的生灭，不做评判"
        };
        
        for (int i = 0; i < 100; i++) {
            ps.setString(1, trackTitles[i % trackTitles.length] + " - 第" + (i + 1) + "课");
            ps.setString(2, descriptions[i % descriptions.length]);
            ps.setString(3, AUDIO_BASE_URL + "track_" + (i + 1) + "_" + UUID.randomUUID().toString().substring(0, 8) + ".mp3");
            ps.setString(4, IMAGE_BASE_URL + "track_" + (i + 1) + "_" + UUID.randomUUID().toString().substring(0, 8) + ".jpg");
            ps.setInt(5, (random.nextInt(20) + 5) * 60); // 5-25分钟
            ps.setInt(6, (i % 6) + 1); // 分类ID
            ps.setInt(7, (i % 30) + 1); // 系列ID
            ps.setInt(8, (i % 10) + 1); // 集数
            ps.setInt(9, (i % 3) + 1); // 难度
            ps.setInt(10, random.nextInt(10000)); // 播放次数
            ps.setInt(11, 0); // 状态
            ps.setString(12, "admin");
            ps.setTimestamp(13, new Timestamp(System.currentTimeMillis()));
            ps.setString(14, "admin");
            ps.setTimestamp(15, new Timestamp(System.currentTimeMillis()));
            ps.executeUpdate();
        }
        
        ps.close();
        System.out.println("音频数据生成完成：100 条");
    }
    
    /**
     * 生成文章数据
     */
    private static void generateArticles() throws Exception {
        String sql = "INSERT INTO meditation_article (title, summary, content, cover, author, category_id, view_count, like_count, status, create_by, create_time, update_by, update_time) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = conn.prepareStatement(sql);
        
        String[] titles = {
            "冥想的科学原理", "如何建立冥想习惯", "冥想与大脑健康", "冥想的误区与真相",
            "办公室冥想技巧", "儿童冥想指南", "老年人冥想益处", "运动员的冥想训练",
            "冥想与情绪管理", "冥想改善人际关系", "冥想提升创造力", "冥想与时间管理",
            "不同文化中的冥想", "现代冥想技术", "冥想App使用指南", "冥想社群的力量"
        };
        
        String contentTemplate = "<h2>引言</h2><p>这是一篇关于冥想的深度文章...</p>" +
                                "<h2>核心概念</h2><p>让我们深入了解这个主题...</p>" +
                                "<h2>实践方法</h2><p>以下是具体的练习步骤：</p>" +
                                "<ol><li>第一步...</li><li>第二步...</li><li>第三步...</li></ol>" +
                                "<h2>注意事项</h2><p>在练习过程中需要注意...</p>" +
                                "<h2>总结</h2><p>通过本文的学习，您应该已经掌握了...</p>";
        
        String[] authors = {"冥想导师", "心理学家", "神经科学家", "瑜伽教练", "健康专家", "生活教练"};
        
        for (int i = 0; i < 50; i++) {
            ps.setString(1, titles[i % titles.length] + (i >= titles.length ? " (第" + (i / titles.length + 1) + "篇)" : ""));
            ps.setString(2, "深入探讨" + titles[i % titles.length] + "的要点和实践方法");
            ps.setString(3, contentTemplate);
            ps.setString(4, IMAGE_BASE_URL + "article_" + (i + 1) + "_" + UUID.randomUUID().toString().substring(0, 8) + ".jpg");
            ps.setString(5, authors[i % authors.length]);
            ps.setInt(6, (i % 6) + 1); // 分类ID
            ps.setInt(7, random.nextInt(5000) + 100); // 浏览次数
            ps.setInt(8, random.nextInt(500)); // 点赞次数
            ps.setInt(9, 0); // 状态
            ps.setString(10, "admin");
            ps.setTimestamp(11, new Timestamp(System.currentTimeMillis()));
            ps.setString(12, "admin");
            ps.setTimestamp(13, new Timestamp(System.currentTimeMillis()));
            ps.executeUpdate();
        }
        
        ps.close();
        System.out.println("文章数据生成完成：50 条");
    }
    
    /**
     * 生成热门关键词
     */
    private static void generateHotKeywords() throws Exception {
        String sql = "INSERT INTO meditation_hot_keyword (keyword, search_count, status, create_time) VALUES (?, ?, ?, ?)";
        PreparedStatement ps = conn.prepareStatement(sql);
        
        String[] keywords = {
            "睡眠", "失眠", "焦虑", "压力", "放松",
            "专注", "正念", "呼吸", "情绪", "冥想入门",
            "深度睡眠", "快速入睡", "缓解焦虑", "提高效率", "内心平静",
            "身心健康", "自我提升", "心灵成长", "减压技巧", "专注力训练"
        };
        
        for (int i = 0; i < keywords.length; i++) {
            ps.setString(1, keywords[i]);
            ps.setInt(2, random.nextInt(9000) + 1000); // 搜索次数 1000-10000
            ps.setInt(3, 0);
            ps.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
            ps.executeUpdate();
        }
        
        ps.close();
        System.out.println("热门关键词生成完成：" + keywords.length + " 条");
    }
    
    /**
     * 生成轮播图
     */
    private static void generateBanners() throws Exception {
        String sql = "INSERT INTO meditation_banner (title, image_url, link_url, link_type, target_id, sort, status, create_time) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = conn.prepareStatement(sql);
        
        String[][] banners = {
            {"新手入门特辑", "series", "1"},
            {"21天冥想挑战", "series", "3"},
            {"深度睡眠计划", "series", "7"},
            {"职场减压方案", "article", "5"},
            {"周末放松时光", "series", "8"},
            {"情绪管理课程", "series", "9"},
            {"专注力训练营", "series", "11"},
            {"冥想科学解读", "article", "1"}
        };
        
        for (int i = 0; i < banners.length; i++) {
            ps.setString(1, banners[i][0]);
            ps.setString(2, IMAGE_BASE_URL + "banner_" + (i + 1) + "_" + UUID.randomUUID().toString().substring(0, 8) + ".jpg");
            ps.setString(3, null); // link_url
            ps.setString(4, banners[i][1]); // link_type
            ps.setLong(5, Long.parseLong(banners[i][2])); // target_id
            ps.setInt(6, i + 1); // sort
            ps.setInt(7, 0); // status
            ps.setTimestamp(8, new Timestamp(System.currentTimeMillis()));
            ps.executeUpdate();
        }
        
        ps.close();
        System.out.println("轮播图数据生成完成：" + banners.length + " 条");
    }
    
    /**
     * 生成常见问题
     */
    private static void generateFAQs() throws Exception {
        String sql = "INSERT INTO meditation_faq (question, answer, category, sort, status, create_time) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = conn.prepareStatement(sql);
        
        String[][] faqs = {
            {"什么是冥想？", "冥想是一种训练注意力和觉察力的心理练习，通过特定的技巧帮助人们达到心理清晰和情绪平静的状态。", "基础知识"},
            {"冥想需要多长时间？", "初学者可以从每天5-10分钟开始，随着练习的深入，可以逐渐增加到20-30分钟。关键是保持规律性。", "练习指导"},
            {"冥想的最佳时间？", "早晨起床后和晚上睡前是理想的冥想时间，但任何让您感到舒适的时间都可以。", "练习指导"},
            {"冥想时总是分心怎么办？", "分心是正常的，关键是温柔地将注意力带回到冥想对象上，不要自我批评。", "常见问题"},
            {"需要特殊装备吗？", "不需要。一个安静的空间和舒适的坐姿就足够了。如果愿意，可以使用冥想垫增加舒适度。", "准备工作"},
            {"冥想和睡觉的区别？", "冥想是保持清醒觉察的状态，而睡眠是意识降低的状态。冥想时要保持警觉但放松。", "基础知识"},
            {"可以躺着冥想吗？", "可以，但坐姿通常更推荐，因为躺着容易睡着。如果选择躺姿，要努力保持清醒。", "练习指导"},
            {"冥想有副作用吗？", "正确的冥想练习通常是安全的。但如果有严重心理问题，建议在专业指导下进行。", "安全须知"},
            {"如何知道冥想是否有效？", "如果您感到更加平静、专注和放松，睡眠质量改善，压力减少，这些都是冥想有效的标志。", "效果评估"},
            {"儿童可以冥想吗？", "可以。儿童冥想有助于提高专注力、情绪管理能力和学习效果。建议使用适合儿童的引导方式。", "特殊人群"},
            {"冥想能治疗疾病吗？", "冥想可以作为辅助手段改善某些症状，但不能替代医疗治疗。如有疾病，请咨询医生。", "安全须知"},
            {"不同冥想方式的区别？", "常见的有正念冥想、专注冥想、慈悲冥想等，各有特点，可以根据个人需求选择。", "冥想类型"},
            {"冥想时的正确坐姿？", "保持脊柱挺直但不僵硬，肩膀放松，双手自然放置。可以盘腿坐或坐在椅子上。", "练习指导"},
            {"冥想时呼吸应该怎样？", "自然呼吸即可，不需要刻意控制。可以将注意力放在呼吸上作为冥想的锚点。", "练习指导"},
            {"如何建立冥想习惯？", "选择固定的时间和地点，从短时间开始，使用提醒工具，记录进展，加入冥想社群获得支持。", "习惯养成"}
        };
        
        for (int i = 0; i < faqs.length; i++) {
            ps.setString(1, faqs[i][0]);
            ps.setString(2, faqs[i][1]);
            ps.setString(3, faqs[i][2]);
            ps.setInt(4, i + 1);
            ps.setInt(5, 0);
            ps.setTimestamp(6, new Timestamp(System.currentTimeMillis()));
            ps.executeUpdate();
        }
        
        ps.close();
        System.out.println("常见问题生成完成：" + faqs.length + " 条");
    }
}