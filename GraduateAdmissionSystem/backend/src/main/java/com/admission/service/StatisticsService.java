package com.admission.service;

import com.admission.entity.FirstTestScore;
import com.admission.entity.ScoreLine;
import com.admission.entity.SecondTestScore;
import com.admission.mapper.AdmissionListMapper;
import com.admission.mapper.CandidateProfileMapper;
import com.admission.mapper.FirstTestScoreMapper;
import com.admission.mapper.ScoreLineMapper;
import com.admission.mapper.ScoreStatisticsMapper;
import com.admission.mapper.SecondTestScoreMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Year;
import java.util.*;

/**
 * 统计分析业务
 */
@Service
@RequiredArgsConstructor
public class StatisticsService {

    private final AdmissionListMapper admissionListMapper;
    private final ScoreStatisticsMapper scoreStatisticsMapper;
    private final ScoreLineMapper scoreLineMapper;
    private final FirstTestScoreMapper firstTestScoreMapper;
    private final SecondTestScoreMapper secondTestScoreMapper;
    private final CandidateProfileMapper candidateProfileMapper;

    /** 获取录取统计数据 */
    public Map<String, Object> getAdmissionStatistics() {
        Map<String, Object> result = new HashMap<>();
        result.put("countByMajor", admissionListMapper.countByMajor());
        result.put("countByOrigin", admissionListMapper.countByOrigin());
        result.put("countByEducation", admissionListMapper.countByEducation());
        result.put("planVsActual", admissionListMapper.planVsActual());
        return result;
    }

    /** 获取成绩分析统计数据 */
    public Map<String, Object> getScoreStatistics() {
        Map<String, Object> result = new HashMap<>();
        result.put("scoreOverview", scoreStatisticsMapper.scoreOverview());
        result.put("passRate", scoreStatisticsMapper.passRate());
        result.put("firstScoreDistribution", scoreStatisticsMapper.firstScoreDistribution());
        result.put("secondScoreDistribution", scoreStatisticsMapper.secondScoreDistribution());
        return result;
    }

    /** 获取所有统计数据 */
    public Map<String, Object> getAllStatistics() {
        Map<String, Object> result = new HashMap<>();
        result.putAll(getAdmissionStatistics());
        result.putAll(getScoreStatistics());
        return result;
    }

    /** 初试筛选：根据分数线自动判定复试资格 */
    public Map<String, Object> screening(String year) {
        if (year == null || year.isEmpty()) {
            year = String.valueOf(Year.now().getValue());
        }

        ScoreLine line = scoreLineMapper.findByYear(year);
        if (line == null) {
            throw new RuntimeException("该年份未设定分数线");
        }

        List<FirstTestScore> scores = firstTestScoreMapper.findAll();
        List<Map<String, Object>> qualified = new ArrayList<>();
        List<Map<String, Object>> disqualified = new ArrayList<>();

        for (FirstTestScore s : scores) {
            List<String> reasons = new ArrayList<>();
            if (s.getPolitics() < line.getPoliticsLine())
                reasons.add("政治" + s.getPolitics() + "<" + line.getPoliticsLine());
            if (s.getForeignLang() < line.getForeignLangLine())
                reasons.add("外语" + s.getForeignLang() + "<" + line.getForeignLangLine());
            if (s.getMajorBasis() < line.getMajorBasisLine())
                reasons.add("专业基础" + s.getMajorBasis() + "<" + line.getMajorBasisLine());
            if (s.getTotalFirst() < line.getTotalFirstLine())
                reasons.add("初试总分" + s.getTotalFirst() + "<" + line.getTotalFirstLine());

            Map<String, Object> entry = new HashMap<>();
            entry.put("examId", s.getExamId());
            entry.put("politics", s.getPolitics());
            entry.put("foreignLang", s.getForeignLang());
            entry.put("majorBasis", s.getMajorBasis());
            entry.put("totalFirst", s.getTotalFirst());

            if (reasons.isEmpty()) {
                entry.put("status", "合格");
                qualified.add(entry);
            } else {
                entry.put("status", "不合格");
                entry.put("reasons", reasons);
                disqualified.add(entry);
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("year", year);
        result.put("scoreLine", line);
        result.put("qualifiedCount", qualified.size());
        result.put("disqualifiedCount", disqualified.size());
        result.put("qualified", qualified);
        result.put("disqualified", disqualified);
        return result;
    }

    /** 复试后综合筛选：初试过线者 + 复试成绩 → 综合总分 vs 录取线 */
    public Map<String, Object> admissionScreening(String year) {
        // 先做初试筛选
        Map<String, Object> firstResult = screening(year);
        ScoreLine line = (ScoreLine) firstResult.get("scoreLine");
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> qualified = (List<Map<String, Object>>) firstResult.get("qualified");

        // 查所有复试成绩和考生信息
        List<SecondTestScore> secondScores = secondTestScoreMapper.findAll();
        var secondMap = new java.util.HashMap<String, Double>();
        secondScores.forEach(s -> secondMap.put(s.getExamId(), s.getTotalSecond()));

        var nameMap = new java.util.HashMap<String, String>();
        candidateProfileMapper.findAll().forEach(c -> nameMap.put(c.getExamId(), c.getName()));

        List<Map<String, Object>> admitted = new ArrayList<>();
        List<Map<String, Object>> failed = new ArrayList<>();

        for (Map<String, Object> q : qualified) {
            String examId = (String) q.get("examId");
            double totalFirst = ((Number) q.get("totalFirst")).doubleValue();
            Double totalSecond = secondMap.get(examId);

            Map<String, Object> entry = new HashMap<>();
            entry.put("examId", examId);
            entry.put("name", nameMap.getOrDefault(examId, ""));
            entry.put("totalFirst", totalFirst);

            if (totalSecond == null) {
                entry.put("totalSecond", "--");
                entry.put("totalScore", "--");
                entry.put("status", "未复试");
                failed.add(entry);
            } else {
                double total = totalFirst + totalSecond;
                entry.put("totalSecond", totalSecond);
                entry.put("totalScore", total);
                if (line.getAdmissionTotalLine() != null && total >= line.getAdmissionTotalLine()) {
                    entry.put("status", "可录取");
                    admitted.add(entry);
                } else {
                    entry.put("status", "不达标");
                    if (line.getAdmissionTotalLine() != null) {
                        entry.put("gap", String.format("%.1f", line.getAdmissionTotalLine() - total));
                    }
                    failed.add(entry);
                }
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("line", line.getAdmissionTotalLine());
        result.put("admitted", admitted);
        result.put("failed", failed);
        return result;
    }
}
