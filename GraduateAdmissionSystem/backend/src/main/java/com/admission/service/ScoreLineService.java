package com.admission.service;

import com.admission.entity.ScoreLine;

import java.util.List;

public interface ScoreLineService {
    List<ScoreLine> findAll();
    ScoreLine findById(Integer id);
    ScoreLine findByYear(String year);
    int add(ScoreLine scoreLine);
    int update(ScoreLine scoreLine);
    int delete(Integer id);
}
