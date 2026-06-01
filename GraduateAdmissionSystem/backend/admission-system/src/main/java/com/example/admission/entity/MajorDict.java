package com.example.admission.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class MajorDict {
    private Long id;
    private String majorCode;
    private String majorName;
    private Integer plannedEnrollment;
    private Integer extraEnrollment;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}