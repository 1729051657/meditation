package org.dromara.meditation.domain.vo;

import org.dromara.meditation.domain.Tag;
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
 * 内容标签视图对象 mg_tag
 *
 * @author kdc
 * @date 2025-08-14
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = Tag.class)
public class TagVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ExcelProperty(value = "主键")
    private Long id;

    /**
     * 标签名称
     */
    @ExcelProperty(value = "标签名称")
    private String name;

    /**
     * 标签类型
     */
    @ExcelProperty(value = "标签类型")
    private String type;

    /**
     * 状态（0正常 1停用）
     */
    @ExcelProperty(value = "状态", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "0=正常,1=停用")
    private String status;

    /**
     * 排序
     */
    @ExcelProperty(value = "排序")
    private Integer orderNum;

    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
    private String remark;


}
