package com.admission.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 分数线实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScoreLine {
    /** 主键ID */
    private Integer id;
    /** 招生年份 */
    private String year;
    /** 政治单科线 */
    private Double politicsLine;
    /** 外语单科线 */
    private Double foreignLangLine;
    /** 专业基础单科线 */
    private Double majorBasisLine;
    /** 初试总分线 */
    private Double totalFirstLine;
    /** 录取总分线 */
    private Double admissionTotalLine;
}
