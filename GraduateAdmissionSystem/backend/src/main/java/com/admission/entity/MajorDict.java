package com.admission.entity;

/**
 * 专业字典实体类
 */
public class MajorDict {
    /** 专业代码 */
    private String majorCode;
    /** 专业名称 */
    private String majorName;
    /** 计划内招生数 */
    private Integer plannedInside;
    /** 计划外招生数 */
    private Integer plannedOutside;

    // 无参构造
    public MajorDict() {}

    // 全参构造
    public MajorDict(String majorCode, String majorName, Integer plannedInside, Integer plannedOutside) {
        this.majorCode = majorCode;
        this.majorName = majorName;
        this.plannedInside = plannedInside;
        this.plannedOutside = plannedOutside;
    }

    // Getter 和 Setter
    public String getMajorCode() { return majorCode; }
    public void setMajorCode(String majorCode) { this.majorCode = majorCode; }
    public String getMajorName() { return majorName; }
    public void setMajorName(String majorName) { this.majorName = majorName; }
    public Integer getPlannedInside() { return plannedInside; }
    public void setPlannedInside(Integer plannedInside) { this.plannedInside = plannedInside; }
    public Integer getPlannedOutside() { return plannedOutside; }
    public void setPlannedOutside(Integer plannedOutside) { this.plannedOutside = plannedOutside; }
}