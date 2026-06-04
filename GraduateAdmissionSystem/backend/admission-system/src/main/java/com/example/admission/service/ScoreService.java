package com.example.admission.service;

import com.example.admission.entity.InitialExamScore;
import com.example.admission.entity.ReexamScore;

public interface ScoreService {
    InitialExamScore getInitialScore(String examNumber);
    int saveInitialScore(InitialExamScore score);
    ReexamScore getReexamScore(String examNumber);
    int saveReexamScore(ReexamScore score);
}