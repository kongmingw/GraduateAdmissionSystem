package com.admission.service;

import com.admission.entity.FirstTestScore;
import java.util.List;

public interface FirstTestScoreService {

    List<FirstTestScore> findAll();

    FirstTestScore findByExamId(String examId);

    int add(FirstTestScore score);

    int update(FirstTestScore score);

    int delete(String examId);
}