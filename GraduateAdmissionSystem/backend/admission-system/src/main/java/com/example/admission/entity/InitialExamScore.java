package com.example.admission.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class InitialExamScore {
    private Long id;
    private String examNumber;
    private Double politicsScore;
    private Double foreignLanguageScore;
    private Double professionalBasicScore;
    private Double totalScore;
    private Double averageScore;
    private LocalDateTime createTime;
}