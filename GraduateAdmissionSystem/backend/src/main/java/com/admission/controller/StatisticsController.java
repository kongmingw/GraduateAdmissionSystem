package com.admission.controller;

import com.admission.entity.FirstTestScore;
import com.admission.entity.Result;
import com.admission.entity.ScoreLine;
import com.admission.mapper.AdmissionListMapper;
import com.admission.mapper.FirstTestScoreMapper;
import com.admission.mapper.ScoreLineMapper;
import com.admission.mapper.ScoreStatisticsMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 统计分析控制器
 */
@RestController
@RequestMapping("/api/statistics")
@RequiredArgsConstructor
public class StatisticsController {

    private final AdmissionListMapper admissionListMapper;
    private final ScoreStatisticsMapper scoreStatisticsMapper;
    private final ScoreLineMapper scoreLineMapper;
    private final FirstTestScoreMapper firstTestScoreMapper;

    /** 获取录取统计数据 */
    @GetMapping("/admission")
    public Result<Map<String, Object>> getAdmissionStatistics() {
        Map<String, Object> result = new HashMap<>();
        result.put("countByMajor", admissionListMapper.countByMajor());
        result.put("countByOrigin", admissionListMapper.countByOrigin());
        result.put("countByEducation", admissionListMapper.countByEducation());
        result.put("planVsActual", admissionListMapper.planVsActual());
        return Result.success(result);
    }

    /** 获取成绩分析统计数据 */
    @GetMapping("/scores")
    public Result<Map<String, Object>> getScoreStatistics() {
        Map<String, Object> result = new HashMap<>();
        result.put("scoreOverview", scoreStatisticsMapper.scoreOverview());
        result.put("passRate", scoreStatisticsMapper.passRate());
        result.put("firstScoreDistribution", scoreStatisticsMapper.firstScoreDistribution());
        result.put("secondScoreDistribution", scoreStatisticsMapper.secondScoreDistribution());
        return Result.success(result);
    }

    /** 获取所有统计数据 */
    @GetMapping("/all")
    public Result<Map<String, Object>> getAllStatistics() {
        Map<String, Object> result = new HashMap<>();
        result.put("countByMajor", admissionListMapper.countByMajor());
        result.put("countByOrigin", admissionListMapper.countByOrigin());
        result.put("countByEducation", admissionListMapper.countByEducation());
        result.put("planVsActual", admissionListMapper.planVsActual());
        result.put("scoreOverview", scoreStatisticsMapper.scoreOverview());
        result.put("passRate", scoreStatisticsMapper.passRate());
        result.put("firstScoreDistribution", scoreStatisticsMapper.firstScoreDistribution());
        result.put("secondScoreDistribution", scoreStatisticsMapper.secondScoreDistribution());
        return Result.success(result);
    }

    /** 初试筛选：根据分数线自动判定复试资格 */
    @GetMapping("/screening")
    public Result<Map<String, Object>> screening(@RequestParam(required = false) String year) {
        // 未传年份则使用当前年份
        if (year == null || year.isEmpty()) {
            year = String.valueOf(java.time.Year.now().getValue());
        }
        // 查分数线
        ScoreLine line = scoreLineMapper.findByYear(year);
        if (line == null) {
            return Result.error("该年份未设定分数线");
        }
        // 查所有初试成绩
        List<FirstTestScore> scores = firstTestScoreMapper.findAll();
        List<Map<String, Object>> qualified = new ArrayList<>();    // 合格进复试
        List<Map<String, Object>> disqualified = new ArrayList<>(); // 不合格

        for (FirstTestScore s : scores) {
            List<String> reasons = new ArrayList<>();
            if (s.getPolitics() < line.getPoliticsLine()) reasons.add("政治" + s.getPolitics() + "<" + line.getPoliticsLine());
            if (s.getForeignLang() < line.getForeignLangLine()) reasons.add("外语" + s.getForeignLang() + "<" + line.getForeignLangLine());
            if (s.getMajorBasis() < line.getMajorBasisLine()) reasons.add("专业基础" + s.getMajorBasis() + "<" + line.getMajorBasisLine());
            if (s.getTotalFirst() < line.getTotalFirstLine()) reasons.add("初试总分" + s.getTotalFirst() + "<" + line.getTotalFirstLine());

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
        return Result.success(result);
    }
}