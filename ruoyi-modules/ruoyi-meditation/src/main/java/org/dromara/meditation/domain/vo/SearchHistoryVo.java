package org.dromara.meditation.domain.vo;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.dromara.meditation.domain.SearchHistory;
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
 * 搜索历史视图对象 mg_search_history
 *
 * @author kdc
 * @date 2025-08-14
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = SearchHistory.class)
public class SearchHistoryVo implements Serializable {

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
     * 搜索关键字
     */
    @ExcelProperty(value = "搜索关键字")
    private String keyword;

    /**
     * 搜索次数
     */
    @ExcelProperty(value = "搜索次数")
    private Integer times;

    /**
     * 最后搜索时间
     */
    @ExcelProperty(value = "最后搜索时间")
    private Date lastTime;


}
