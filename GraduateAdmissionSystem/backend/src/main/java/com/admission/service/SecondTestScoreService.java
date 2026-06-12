package com.admission.service;

import com.admission.entity.FirstTestScore;
import com.admission.entity.ScoreLine;
import com.admission.entity.SecondTestScore;
import com.admission.mapper.FirstTestScoreMapper;
import com.admission.mapper.ScoreLineMapper;
import com.admission.mapper.SecondTestScoreMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Year;
import java.util.List;

/**
 * 复试成绩业务
 */
@Service
@RequiredArgsConstructor
public class SecondTestScoreService {

    private final SecondTestScoreMapper secondTestScoreMapper;
    private final FirstTestScoreMapper firstTestScoreMapper;
    private final ScoreLineMapper scoreLineMapper;

    /** 查询所有复试成绩 */
    public List<SecondTestScore> findAll() {
        return secondTestScoreMapper.findAll();
    }

    /** 根据考号查询 */
    public SecondTestScore findByExamId(String examId) {
        return secondTestScoreMapper.findByExamId(examId);
    }

    /** 录入复试成绩（校验：必须有初试成绩且过线） */
    @Transactional
    public int add(SecondTestScore score) {
        String examId = score.getExamId();
        FirstTestScore first = firstTestScoreMapper.findByExamId(examId);
        if (first == null) {
            throw new RuntimeException("该考生无初试成绩，不能录入复试成绩");
        }
        ScoreLine line = scoreLineMapper.findByYear(String.valueOf(Year.now().getValue()));
        if (line != null) {
            boolean pass = first.getPolitics() >= line.getPoliticsLine()
                    && first.getForeignLang() >= line.getForeignLangLine()
                    && first.getMajorBasis() >= line.getMajorBasisLine()
                    && first.getTotalFirst() >= line.getTotalFirstLine();
            if (!pass) {
                throw new RuntimeException("该考生初试未过线，不能参加复试");
            }
        }
        return secondTestScoreMapper.insert(score);
    }

    /** 更新复试成绩 */
    @Transactional
    public int update(SecondTestScore score) {
        return secondTestScoreMapper.update(score);
    }

    /** 删除复试成绩 */
    @Transactional
    public int delete(String examId) {
        return secondTestScoreMapper.deleteByExamId(examId);
    }
}
