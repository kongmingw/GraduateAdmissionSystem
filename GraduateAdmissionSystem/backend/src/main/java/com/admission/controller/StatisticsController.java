package com.admission.controller;

import com.admission.entity.Result;
import com.admission.mapper.AdmissionListMapper;
import com.admission.mapper.ScoreStatisticsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/statistics")
public class StatisticsController {

    @Autowired
    private AdmissionListMapper admissionListMapper;

    @Autowired
    private ScoreStatisticsMapper scoreStatisticsMapper;

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
}