package com.admission.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 复试成绩实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SecondTestScore {
    /** 考号 */
    private String examId;
    /** 复试专业科目成绩 */
    private Double majorTest;
    /** 面试成绩 */
    private Double interview;
    /** 上机成绩 */
    private Double computerTest;
    /** 复试总分（数据库虚拟列，查询时返回，新增时不需要传） */
    private Double totalSecond;
}