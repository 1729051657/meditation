package org.dromara.meditation.controller;

import cn.dev33.satoken.annotation.SaIgnore;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.domain.R;
import org.dromara.common.web.core.BaseController;
import org.dromara.meditation.domain.vo.ContactInfoVo;
import org.dromara.meditation.domain.vo.FaqDataVo;
import org.dromara.meditation.domain.vo.FaqQuestionVo;
import org.dromara.system.domain.vo.SysDictDataVo;
import org.dromara.system.service.ISysConfigService;
import org.dromara.system.service.ISysDictTypeService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

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
    private final ISysDictTypeService dictTypeService;

    /**
     * 获取FAQ页面数据
     * 包含：系统参数配置（小程序联系方式）和字典数据（问题列表）
     */
    @SaIgnore
    @GetMapping("/data")
    public R<FaqDataVo> getFaqData() {
        FaqDataVo result = new FaqDataVo();
        
        // 获取小程序联系方式配置
        ContactInfoVo contactInfo = new ContactInfoVo();
        String contactEmail = configService.selectConfigByKey("miniapp.contact.email");
        String contactPhone = configService.selectConfigByKey("miniapp.contact.phone");
        String contactWechat = configService.selectConfigByKey("miniapp.contact.wechat");
        String contactQQ = configService.selectConfigByKey("miniapp.contact.qq");
        String contactAddress = configService.selectConfigByKey("miniapp.contact.address");
        
        // 只设置有值的联系方式，空值不设置
        if (contactEmail != null && !contactEmail.trim().isEmpty()) {
            contactInfo.setEmail(contactEmail.trim());
        }
        if (contactPhone != null && !contactPhone.trim().isEmpty()) {
            contactInfo.setPhone(contactPhone.trim());
        }
        if (contactWechat != null && !contactWechat.trim().isEmpty()) {
            contactInfo.setWechat(contactWechat.trim());
        }
        if (contactQQ != null && !contactQQ.trim().isEmpty()) {
            contactInfo.setQq(contactQQ.trim());
        }
        if (contactAddress != null && !contactAddress.trim().isEmpty()) {
            contactInfo.setAddress(contactAddress.trim());
        }
        result.setContactInfo(contactInfo);
        
        // 获取FAQ问题字典数据
        // 假设字典类型为：miniapp_faq_questions
        List<SysDictDataVo> faqList = dictTypeService.selectDictDataByType("miniapp_faq_questions");
        
        // 将字典数据转换为FAQ格式
        List<FaqQuestionVo> questions = faqList.stream().map(dict -> {
            FaqQuestionVo faq = new FaqQuestionVo();
            faq.setId(dict.getDictValue());           // 字典值作为ID
            faq.setQuestion(dict.getDictLabel());     // 字典标签作为问题
            faq.setAnswer(dict.getRemark());          // 备注作为答案
            faq.setSort(dict.getDictSort());          // 排序
            faq.setExpanded(false);                   // 默认折叠状态
            return faq;
        }).collect(Collectors.toList());
        
        result.setQuestions(questions);
        
        return R.ok(result);
    }
    
    /**
     * 获取联系信息
     * 单独获取联系方式的接口
     */
    @SaIgnore
    @GetMapping("/contact")
    public R<ContactInfoVo> getContactInfo() {
        ContactInfoVo contactInfo = new ContactInfoVo();
        
        // 获取小程序联系方式配置
        String contactEmail = configService.selectConfigByKey("miniapp.contact.email");
        String contactPhone = configService.selectConfigByKey("miniapp.contact.phone");
        String contactWechat = configService.selectConfigByKey("miniapp.contact.wechat");
        String contactQQ = configService.selectConfigByKey("miniapp.contact.qq");
        String contactAddress = configService.selectConfigByKey("miniapp.contact.address");
        
        // 只设置有值的联系方式，空值不设置
        if (contactEmail != null && !contactEmail.trim().isEmpty()) {
            contactInfo.setEmail(contactEmail.trim());
        }
        if (contactPhone != null && !contactPhone.trim().isEmpty()) {
            contactInfo.setPhone(contactPhone.trim());
        }
        if (contactWechat != null && !contactWechat.trim().isEmpty()) {
            contactInfo.setWechat(contactWechat.trim());
        }
        if (contactQQ != null && !contactQQ.trim().isEmpty()) {
            contactInfo.setQq(contactQQ.trim());
        }
        if (contactAddress != null && !contactAddress.trim().isEmpty()) {
            contactInfo.setAddress(contactAddress.trim());
        }
        
        return R.ok(contactInfo);
    }
    
    /**
     * 获取FAQ问题列表
     * 单独获取问题列表的接口
     */
    @SaIgnore
    @GetMapping("/questions")
    public R<List<FaqQuestionVo>> getFaqQuestions() {
        // 获取FAQ问题字典数据
        List<SysDictDataVo> faqList = dictTypeService.selectDictDataByType("miniapp_faq_questions");
        
        // 将字典数据转换为FAQ格式
        List<FaqQuestionVo> questions = faqList.stream()
            .sorted((a, b) -> Integer.compare(a.getDictSort(), b.getDictSort())) // 按排序字段排序
            .map(dict -> {
                FaqQuestionVo faq = new FaqQuestionVo();
                faq.setId(dict.getDictValue());          // 字典值作为ID
                faq.setQuestion(dict.getDictLabel());    // 字典标签作为问题
                faq.setAnswer(dict.getRemark());         // 备注作为答案
                faq.setSort(dict.getDictSort());         // 排序
                faq.setExpanded(false);                  // 默认折叠状态
                return faq;
            }).collect(Collectors.toList());
        
        return R.ok(questions);
    }
}