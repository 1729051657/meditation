package org.dromara.meditation.controller;

import cn.dev33.satoken.annotation.SaIgnore;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.domain.R;
import org.dromara.common.web.core.BaseController;
import org.dromara.system.domain.vo.SysDictDataVo;
import org.dromara.system.service.ISysConfigService;
import org.dromara.system.service.ISysDictDataService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * FAQ常见问题控制器
 * 提供小程序FAQ页面所需的数据接口
 *
 * @author system
 * @date 2024-01-10
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/meditation/faq")
public class FaqController extends BaseController {

    private final ISysConfigService configService;
    private final ISysDictDataService dictDataService;

    /**
     * 获取FAQ页面数据
     * 包含：系统参数配置（小程序联系方式）和字典数据（问题列表）
     */
    @SaIgnore
    @GetMapping("/data")
    public R<Map<String, Object>> getFaqData() {
        Map<String, Object> result = new HashMap<>();
        
        // 获取小程序联系方式配置
        // 假设配置键名为：miniapp.contact.email 和 miniapp.contact.phone
        String contactEmail = configService.selectConfigByKey("miniapp.contact.email");
        String contactPhone = configService.selectConfigByKey("miniapp.contact.phone");
        String contactWechat = configService.selectConfigByKey("miniapp.contact.wechat");
        
        Map<String, String> contactInfo = new HashMap<>();
        contactInfo.put("email", contactEmail != null ? contactEmail : "");
        contactInfo.put("phone", contactPhone != null ? contactPhone : "");
        contactInfo.put("wechat", contactWechat != null ? contactWechat : "");
        result.put("contactInfo", contactInfo);
        
        // 获取FAQ问题字典数据
        // 假设字典类型为：miniapp_faq_questions
        List<SysDictDataVo> faqList = dictDataService.selectDictDataByType("miniapp_faq_questions");
        
        // 将字典数据转换为FAQ格式
        List<Map<String, Object>> questions = faqList.stream().map(dict -> {
            Map<String, Object> faq = new HashMap<>();
            faq.put("question", dict.getDictLabel());  // 字典标签作为问题
            faq.put("answer", dict.getRemark());        // 备注作为答案
            faq.put("sort", dict.getDictSort());        // 排序
            faq.put("expanded", false);                 // 默认折叠状态
            return faq;
        }).toList();
        
        result.put("questions", questions);
        
        return R.ok(result);
    }
    
    /**
     * 获取联系信息
     * 单独获取联系方式的接口
     */
    @SaIgnore
    @GetMapping("/contact")
    public R<Map<String, String>> getContactInfo() {
        Map<String, String> contactInfo = new HashMap<>();
        
        // 获取小程序联系方式配置
        String contactEmail = configService.selectConfigByKey("miniapp.contact.email");
        String contactPhone = configService.selectConfigByKey("miniapp.contact.phone");
        String contactWechat = configService.selectConfigByKey("miniapp.contact.wechat");
        String contactQQ = configService.selectConfigByKey("miniapp.contact.qq");
        String contactAddress = configService.selectConfigByKey("miniapp.contact.address");
        
        contactInfo.put("email", contactEmail != null ? contactEmail : "support@meditation.com");
        contactInfo.put("phone", contactPhone != null ? contactPhone : "400-123-4567");
        contactInfo.put("wechat", contactWechat != null ? contactWechat : "");
        contactInfo.put("qq", contactQQ != null ? contactQQ : "");
        contactInfo.put("address", contactAddress != null ? contactAddress : "");
        
        return R.ok(contactInfo);
    }
    
    /**
     * 获取FAQ问题列表
     * 单独获取问题列表的接口
     */
    @SaIgnore
    @GetMapping("/questions")
    public R<List<Map<String, Object>>> getFaqQuestions() {
        // 获取FAQ问题字典数据
        List<SysDictDataVo> faqList = dictDataService.selectDictDataByType("miniapp_faq_questions");
        
        // 将字典数据转换为FAQ格式
        List<Map<String, Object>> questions = faqList.stream()
            .sorted((a, b) -> Integer.compare(a.getDictSort(), b.getDictSort())) // 按排序字段排序
            .map(dict -> {
                Map<String, Object> faq = new HashMap<>();
                faq.put("id", dict.getDictCode());          // 字典值作为ID
                faq.put("question", dict.getDictLabel());   // 字典标签作为问题
                faq.put("answer", dict.getRemark());         // 备注作为答案
                faq.put("sort", dict.getDictSort());        // 排序
                faq.put("expanded", false);                 // 默认折叠状态
                return faq;
            }).toList();
        
        return R.ok(questions);
    }
}