package com.example.admission.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ScoreLine {
    private Long id;
    private String majorCode;
    private Double politicsLine;
    private Double foreignLanguageLine;
    private Double professionalBasicLine;
    private Double totalScoreLine;
    private Integer year;
    private LocalDateTime createTime;
}