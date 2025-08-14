package org.dromara.meditation.domain.vo;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.dromara.meditation.domain.RecommendItem;
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
 * 推荐位内容视图对象 mg_recommend_item
 *
 * @author kdc
 * @date 2025-08-14
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = RecommendItem.class)
public class RecommendItemVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ExcelProperty(value = "主键")
    private Long id;

    /**
     * 推荐位ID
     */
    @ExcelProperty(value = "推荐位ID")
    private Long slotId;

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
     * 显示顺序
     */
    @ExcelProperty(value = "显示顺序")
    private Integer orderNum;

    /**
     * 生效时间
     */
    @ExcelProperty(value = "生效时间")
    private Date startTime;

    /**
     * 失效时间
     */
    @ExcelProperty(value = "失效时间")
    private Date endTime;

    /**
     * 状态（0正常 1停用）
     */
    @ExcelProperty(value = "状态", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "0=正常,1=停用")
    private String status;


}
