package org.dromara.meditation.domain.vo;

import org.dromara.meditation.domain.Track;
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
 * 冥想单集视图对象 mg_track
 *
 * @author kdc
 * @date 2025-08-14
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = Track.class)
public class TrackVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ExcelProperty(value = "主键")
    private Long id;

    /**
     * 所属系列id（mg_series.id）
     */
    @ExcelProperty(value = "所属系列id", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "m=g_series.id")
    private Long seriesId;

    /**
     * 分类id（mg_category.id）
     */
    @ExcelProperty(value = "分类id", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "m=g_category.id")
    private Long categoryId;

    /**
     * 标题
     */
    @ExcelProperty(value = "标题")
    private String title;

    /**
     * 封面文件id（sys_oss.oss_id）
     */
    @ExcelProperty(value = "封面文件id", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "s=ys_oss.oss_id")
    private Long cover;

    /**
     * 音频文件id（sys_oss.oss_id）
     */
    @ExcelProperty(value = "音频文件id", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "s=ys_oss.oss_id")
    private Long audio;

    /**
     * 时长（秒）
     */
    @ExcelProperty(value = "时长", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "秒=")
    private Integer durationSec;

    /**
     * 简介
     */
    @ExcelProperty(value = "简介")
    private String intro;

    /**
     * 在系列内排序（从小到大）
     */
    @ExcelProperty(value = "在系列内排序", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "从=小到大")
    private Integer orderIndex;

    /**
     * 是否免费（Y是 N否）
     */
    @ExcelProperty(value = "是否免费", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "Y=是,N=否")
    private String isFree;

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
