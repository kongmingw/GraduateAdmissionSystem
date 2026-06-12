package com.admission.service;

import com.admission.entity.ScoreLine;
import com.admission.mapper.ScoreLineMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 分数线业务
 */
@Service
@RequiredArgsConstructor
public class ScoreLineService {

    private final ScoreLineMapper scoreLineMapper;

    /** 查询所有分数线 */
    public List<ScoreLine> findAll() {
        return scoreLineMapper.findAll();
    }

    /** 根据ID查询 */
    public ScoreLine findById(Integer id) {
        return scoreLineMapper.findById(id);
    }

    /** 根据年份查询 */
    public ScoreLine findByYear(String year) {
        return scoreLineMapper.findByYear(year);
    }

    /** 设定分数线 */
    @Transactional
    public int add(ScoreLine scoreLine) {
        return scoreLineMapper.insert(scoreLine);
    }

    /** 更新分数线 */
    @Transactional
    public int update(ScoreLine scoreLine) {
        return scoreLineMapper.update(scoreLine);
    }

    /** 删除分数线 */
    @Transactional
    public int delete(Integer id) {
        return scoreLineMapper.deleteById(id);
    }
}
