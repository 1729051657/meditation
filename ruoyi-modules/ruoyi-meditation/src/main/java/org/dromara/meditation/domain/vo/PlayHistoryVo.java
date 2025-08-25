package org.dromara.meditation.domain.vo;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.dromara.meditation.domain.PlayHistory;
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
 * 音频播放记录视图对象 mg_play_history
 *
 * @author kdc
 * @date 2025-08-14
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = PlayHistory.class)
public class PlayHistoryVo implements Serializable {

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
     * 单集ID（mg_track.id）
     */
    @ExcelProperty(value = "单集ID", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "m=g_track.id")
    private Long trackId;

    /**
     * 已播放时长（秒）
     */
    @ExcelProperty(value = "已播放时长", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "秒=")
    private Integer progressSec;

    /**
     * 最后播放时间
     */
    @ExcelProperty(value = "最后播放时间")
    private Date lastPlayTime;

    /**
     * 播放次数
     */
    @ExcelProperty(value = "播放次数")
    private Integer playCount;

    /**
     * 是否已听完（Y是 N否）
     */
    @ExcelProperty(value = "是否已听完", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "Y=是,N=否")
    private String isCompleted;

    /**
     * 关联的单集信息
     */
    private TrackVo track;

}
