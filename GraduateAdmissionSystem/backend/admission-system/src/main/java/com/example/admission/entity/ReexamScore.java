package com.example.admission.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ReexamScore {
    private Long id;
    private String examNumber;
    private Double professionalSubjectScore;
    private Double interviewScore;
    private Double computerTestScore;
    private Double totalScore;
    private LocalDateTime createTime;
}