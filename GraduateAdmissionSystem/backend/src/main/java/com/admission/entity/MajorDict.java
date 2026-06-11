package com.admission.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 专业字典实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MajorDict {
    /** 专业代码 */
    private String majorCode;
    /** 专业名称 */
    private String majorName;
    /** 计划内招生数 */
    private Integer plannedInside;
    /** 计划外招生数 */
    private Integer plannedOutside;
}