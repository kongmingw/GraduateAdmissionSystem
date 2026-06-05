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

    // Getter 和 Setter
    public String getExamId() { return examId; }
    public void setExamId(String examId) { this.examId = examId; }

    public String getAdmittedMajor() { return admittedMajor; }
    public void setAdmittedMajor(String admittedMajor) { this.admittedMajor = admittedMajor; }

    public Double getFinalFirstScore() { return finalFirstScore; }
    public void setFinalFirstScore(Double finalFirstScore) { this.finalFirstScore = finalFirstScore; }

    public Double getFinalSecondScore() { return finalSecondScore; }
    public void setFinalSecondScore(Double finalSecondScore) { this.finalSecondScore = finalSecondScore; }

    public Double getTotalScore() { return totalScore; }
    public void setTotalScore(Double totalScore) { this.totalScore = totalScore; }
}