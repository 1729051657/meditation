package org.dromara.meditation.domain.vo;

import org.dromara.common.translation.annotation.Translation;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.dromara.common.translation.constant.TransConstant;
import org.dromara.meditation.domain.Banner;
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
 * 横幅配置视图对象 mg_banner
 *
 * @author kdc
 * @date 2025-08-14
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = Banner.class)
public class BannerVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ExcelProperty(value = "主键")
    private Long id;

    /**
     * 所属页面（home等）
     */
    @ExcelProperty(value = "所属页面", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "h=ome等")
    private String page;

    /**
     * 图片文件id（sys_oss.oss_id）
     */
    @ExcelProperty(value = "图片文件id", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "s=ys_oss.oss_id")
    private Long image;

    /**
     * 图片文件id（sys_oss.oss_id）Url
     */
    @Translation(type = TransConstant.OSS_ID_TO_URL, mapper = "image")
    private String imageUrl;
    /**
     * 链接类型（0无 1外链URL 2内容跳转）
     */
    @ExcelProperty(value = "链接类型", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "0=无,1=外链URL,2=内容跳转")
    private String linkType;

    /**
     * 链接目标（URL或内容定位）
     */
    @ExcelProperty(value = "链接目标", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "U=RL或内容定位")
    private String linkTarget;

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
     * 生效时间
     */
    @ExcelProperty(value = "生效时间")
    private Date startTime;

    /**
     * 失效时间
     */
    @ExcelProperty(value = "失效时间")
    private Date endTime;


}
