package org.dromara.meditation.domain.vo;

import org.dromara.meditation.domain.ContentTag;
import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import org.dromara.common.excel.annotation.ExcelDictFormat;
import org.dromara.common.excel.convert.ExcelDictConvert;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;



/**
 * 内容-标签关联视图对象 mg_content_tag
 *
 * @author kdc
 * @date 2025-08-14
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = ContentTag.class)
public class ContentTagVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ExcelProperty(value = "主键")
    private Long id;

    /**
     * 内容类型（series/track/article）
     */
    @ExcelProperty(value = "内容类型", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "s=eries/track/article")
    private String contentType;

    /**
     * 内容ID
     */
    @ExcelProperty(value = "内容ID")
    private Long contentId;

    /**
     * 标签ID
     */
    @ExcelProperty(value = "标签ID")
    private Long tagId;


}
