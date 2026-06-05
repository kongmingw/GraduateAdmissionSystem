package com.admission.service;

import com.admission.entity.SecondTestScore;
import java.util.List;

public interface SecondTestScoreService {

    List<SecondTestScore> findAll();

    SecondTestScore findByExamId(String examId);

    int add(SecondTestScore score);

    int update(SecondTestScore score);

    int delete(String examId);
}