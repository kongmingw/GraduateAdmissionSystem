package com.admission.service.impl;

import com.admission.entity.ScoreLine;
import com.admission.mapper.ScoreLineMapper;
import com.admission.service.ScoreLineService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ScoreLineServiceImpl implements ScoreLineService {

    private final ScoreLineMapper scoreLineMapper;

    @Override
    public List<ScoreLine> findAll() {
        return scoreLineMapper.findAll();
    }

    @Override
    public ScoreLine findById(Integer id) {
        return scoreLineMapper.findById(id);
    }

    @Override
    public ScoreLine findByYear(String year) {
        return scoreLineMapper.findByYear(year);
    }

    @Override
    @Transactional
    public int add(ScoreLine scoreLine) {
        return scoreLineMapper.insert(scoreLine);
    }

    @Override
    @Transactional
    public int update(ScoreLine scoreLine) {
        return scoreLineMapper.update(scoreLine);
    }

    @Override
    @Transactional
    public int delete(Integer id) {
        return scoreLineMapper.deleteById(id);
    }
}
