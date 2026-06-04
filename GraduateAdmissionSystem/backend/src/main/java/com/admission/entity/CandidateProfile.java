package com.admission.entity;

/**
 * 考生档案实体类
 */
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

    // 无参构造
    public CandidateProfile() {}

    // Getter 和 Setter
    public String getExamId() { return examId; }
    public void setExamId(String examId) { this.examId = examId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }
    public String getPoliticalStatus() { return politicalStatus; }
    public void setPoliticalStatus(String politicalStatus) { this.politicalStatus = politicalStatus; }
    public Boolean getIsFreshGraduate() { return isFreshGraduate; }
    public void setIsFreshGraduate(Boolean isFreshGraduate) { this.isFreshGraduate = isFreshGraduate; }
    public String getEducation() { return education; }
    public void setEducation(String education) { this.education = education; }
    public String getOrigin() { return origin; }
    public void setOrigin(String origin) { this.origin = origin; }
    public String getTargetMajor() { return targetMajor; }
    public void setTargetMajor(String targetMajor) { this.targetMajor = targetMajor; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
}