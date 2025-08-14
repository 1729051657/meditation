package org.dromara.meditation.domain.vo;

import org.dromara.meditation.domain.SeriesProgress;
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
 * 系列学习进度视图对象 mg_series_progress
 *
 * @author kdc
 * @date 2025-08-14
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = SeriesProgress.class)
public class SeriesProgressVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ExcelProperty(value = "主键")
    private Long id;

    /**
     * 用户ID
     */
    @ExcelProperty(value = "用户ID")
    private Long userId;

    /**
     * 系列ID（mg_series.id）
     */
    @ExcelProperty(value = "系列ID", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "m=g_series.id")
    private Long seriesId;

    /**
     * 最近播放的单集ID（mg_track.id）
     */
    @ExcelProperty(value = "最近播放的单集ID", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "m=g_track.id")
    private Long lastTrackId;

    /**
     * 已完成小节数
     */
    @ExcelProperty(value = "已完成小节数")
    private Integer completedCount;


}
