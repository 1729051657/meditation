package org.dromara.meditation.domain.vo;

import org.dromara.meditation.domain.Category;
import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import org.dromara.common.excel.annotation.ExcelDictFormat;
import org.dromara.common.excel.convert.ExcelDictConvert;
import org.dromara.common.translation.annotation.Translation;
import org.dromara.common.translation.constant.TransConstant;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;



/**
 * 冥想分类视图对象 mg_category
 *
 * @author kdc
 * @date 2025-08-14
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = Category.class)
public class CategoryVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ExcelProperty(value = "主键")
    private Long id;

    /**
     * 父分类id
     */
    @ExcelProperty(value = "父分类id")
    private Long parentId;

    /**
     * 分类名称
     */
    @ExcelProperty(value = "分类名称")
    private String name;

    /**
     * 分类编码（英文唯一标识）
     */
    @ExcelProperty(value = "分类编码", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "英=文唯一标识")
    private String code;

    /**
     * 图标文件id（sys_oss.oss_id）
     */
    @ExcelProperty(value = "图标文件id", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "s=ys_oss.oss_id")
    private Long icon;

    /**
     * 图标文件URL
     */
    @Translation(type = TransConstant.OSS_ID_TO_URL, mapper = "icon")
    private String iconUrl;

    /**
     * 简介
     */
    @ExcelProperty(value = "简介")
    private String description;

    /**
     * 显示顺序
     */
    @ExcelProperty(value = "显示顺序")
    private Integer orderNum;

    /**
     * 状态（0正常 1停用）
     */
    @ExcelProperty(value = "状态", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "0=正常,1=停用")
    private String status;

    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
    private String remark;


}
