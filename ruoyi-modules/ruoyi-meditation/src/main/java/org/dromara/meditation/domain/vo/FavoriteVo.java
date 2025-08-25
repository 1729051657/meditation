package org.dromara.meditation.domain.vo;

import org.dromara.meditation.domain.Favorite;
import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import org.dromara.common.excel.annotation.ExcelDictFormat;
import org.dromara.common.excel.convert.ExcelDictConvert;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;


/**
 * 用户收藏视图对象 mg_favorite
 *
 * @author kdc
 * @date 2025-08-14
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = Favorite.class)
public class FavoriteVo implements Serializable {

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
     * 目标类型（series/track/article）
     */
    @ExcelProperty(value = "目标类型", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "s=eries/track/article")
    private String targetType;

    /**
     * 目标ID
     */
    @ExcelProperty(value = "目标ID")
    private Long targetId;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(exist = false)
    private Date createTime;

    /**
     * 关联的音频对象（当targetType为track时）
     */
    @TableField(exist = false)
    private TrackVo track;

    /**
     * 关联的系列对象（当targetType为series时）
     */
    @TableField(exist = false)
    private SeriesVo series;

    /**
     * 关联的文章对象（当targetType为article时）
     */
    @TableField(exist = false)
    private ArticleVo article;

    /**
     * 分类信息
     */
    @TableField(exist = false)
    private CategoryVo category;

}
