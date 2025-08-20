package org.dromara.meditation.domain.vo;

import org.dromara.meditation.domain.RecommendSlot;
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
 * 推荐位视图对象 mg_recommend_slot
 *
 * @author kdc
 * @date 2025-08-14
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = RecommendSlot.class)
public class RecommendSlotVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ExcelProperty(value = "主键")
    private Long id;

    /**
     * 推荐位编码
     */
    @ExcelProperty(value = "推荐位编码")
    private String code;

    /**
     * 推荐位名称
     */
    @ExcelProperty(value = "推荐位名称")
    private String name;

    /**
     * 所属页面（home/category/search等）
     */
    @ExcelProperty(value = "所属页面", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "h=ome/category/search等")
    private String page;

    /**
     * 状态（0正常 1停用）
     */
    @ExcelProperty(value = "状态", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "0=正常,1=停用")
    private String status;

    /**
     * 显示顺序
     */
    @ExcelProperty(value = "显示顺序")
    private Integer orderNum;

    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
    private String remark;

}
