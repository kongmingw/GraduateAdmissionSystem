package com.example.admission.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AdmissionList {
    private Long id;
    private String examNumber;
    private String department;
    private Double initialTotalScore;
    private Double reexamTotalScore;
    private Double finalScore;
    private Integer isAdmitted;
    private LocalDateTime createTime;
}