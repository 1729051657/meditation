package org.dromara.meditation.domain.vo;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.dromara.meditation.domain.Series;
import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import org.dromara.common.excel.annotation.ExcelDictFormat;
import org.dromara.common.excel.convert.ExcelDictConvert;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import com.baomidou.mybatisplus.annotation.TableField;



/**
 * 冥想系列视图对象 mg_series
 *
 * @author kdc
 * @date 2025-08-14
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = Series.class)
public class SeriesVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ExcelProperty(value = "主键")
    private Long id;

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
     * 副标题
     */
    @ExcelProperty(value = "副标题")
    private String subtitle;

    /**
     * 封面文件id（sys_oss.oss_id）
     */
    @ExcelProperty(value = "封面文件id", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "s=ys_oss.oss_id")
    private Long cover;

    /**
     * 横幅图文件id（sys_oss.oss_id）
     */
    @ExcelProperty(value = "横幅图文件id", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "s=ys_oss.oss_id")
    private Long banner;

    /**
     * 简介
     */
    @ExcelProperty(value = "简介")
    private String intro;

    /**
     * 小节数
     */
    @ExcelProperty(value = "小节数")
    private Integer episodeCount;

    /**
     * 建议时长（秒）
     */
    @ExcelProperty(value = "建议时长", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "秒=")
    private Integer recommendDuration;



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
     * 发布时间
     */
    @ExcelProperty(value = "发布时间")
    private Date publishTime;

    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
    private String remark;

    /**
     * 标签列表
     */
    @TableField(exist = false)
    private List<TagVo> tags;

    /**
     * 标签ID列表
     */
    @TableField(exist = false)
    private List<Long> tagIds;

}
