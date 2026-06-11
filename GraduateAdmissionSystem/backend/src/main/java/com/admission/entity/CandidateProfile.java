package com.admission.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 考生档案实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CandidateProfile {
    /** 考号 */
    private String examId;
    /** 姓名 */
    private String name;
    /** 性别 */
    private String gender;
    /** 年龄 */
    private Integer age;
    /** 政治面貌 */
    private String politicalStatus;
    /** 是否应届 */
    private Boolean isFreshGraduate;
    /** 学历 */
    private String education;
    /** 来源地 */
    private String origin;
    /** 报考专业代码 */
    private String targetMajor;
    /** 报考类别 */
    private String category;
}