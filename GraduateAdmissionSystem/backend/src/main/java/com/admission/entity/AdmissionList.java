package com.admission.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 录取名单实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdmissionList {
    /** 考号 */
    private String examId;
    /** 录取专业代码 */
    private String admittedMajor;
    /** 最终初试成绩 */
    private Double finalFirstScore;
    /** 最终复试成绩 */
    private Double finalSecondScore;
    /** 综合总分 */
    private Double totalScore;
}