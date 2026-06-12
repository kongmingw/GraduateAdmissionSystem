package com.admission.service;

import com.admission.entity.ScoreLine;
import com.admission.mapper.ScoreLineMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ScoreLineService {

    private final ScoreLineMapper scoreLineMapper;

    public List<ScoreLine> findAll() {
        return scoreLineMapper.findAll();
    }

    public ScoreLine findById(Integer id) {
        return scoreLineMapper.findById(id);
    }

    public ScoreLine findByYear(String year) {
        return scoreLineMapper.findByYear(year);
    }

    @Transactional
    public int add(ScoreLine scoreLine) {
        return scoreLineMapper.insert(scoreLine);
    }

    @Transactional
    public int update(ScoreLine scoreLine) {
        return scoreLineMapper.update(scoreLine);
    }

    @Transactional
    public int delete(Integer id) {
        return scoreLineMapper.deleteById(id);
    }
}
