package com.example.admission.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class StudentProfile {
    private Long id;
    private String examNumber;
    private String name;
    private Integer gender;
    private Integer age;
    private String politicalStatus;
    private Integer isFreshGraduate;
    private String education;
    private String source;
    private String majorCode;
    private String category;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}