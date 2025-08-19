package org.dromara.meditation.domain.vo;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.dromara.meditation.domain.Article;
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
 * 冥想知识文章视图对象 mg_article
 *
 * @author kdc
 * @date 2025-08-14
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = Article.class)
public class ArticleVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ExcelProperty(value = "主键")
    private Long id;

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
     * 摘要
     */
    @ExcelProperty(value = "摘要")
    private String summary;

    /**
     * 内容（HTML/Markdown）
     */
    @ExcelProperty(value = "内容", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "H=TML/Markdown")
    private String content;

    /**
     * 作者用户id
     */
    @ExcelProperty(value = "作者用户id")
    private Long authorId;

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
     * 显示顺序
     */
    @ExcelProperty(value = "显示顺序")
    private Integer orderNum;

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
