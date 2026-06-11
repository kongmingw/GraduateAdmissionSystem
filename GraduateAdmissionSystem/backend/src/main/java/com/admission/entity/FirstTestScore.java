package com.admission.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 初试成绩实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FirstTestScore {
    /** 考号 */
    private String examId;
    /** 政治成绩 */
    private Double politics;
    /** 外语成绩 */
    private Double foreignLang;
    /** 专业基础科目成绩 */
    private Double majorBasis;
    /** 初试总分（数据库虚拟列，查询时返回，新增时不需要传） */
    private Double totalFirst;
}