package com.example.admission.controller;

import com.example.admission.common.Result;
import com.example.admission.mapper.AdmissionListMapper;
import com.example.admission.mapper.InitialExamScoreMapper;
import com.example.admission.mapper.StudentProfileMapper;
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
    private StudentProfileMapper studentProfileMapper;
    
    @Autowired
    private InitialExamScoreMapper initialExamScoreMapper;
    
    @Autowired
    private AdmissionListMapper admissionListMapper;
    
    @GetMapping
    public Result<Map<String, Object>> getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        // 考生总人数
        stats.put("totalStudents", studentProfileMapper.findAll().size());
        
        // 录取人数
        stats.put("admittedCount", admissionListMapper.countAdmitted());
        
        // 录取平均分
        Double avgScore = admissionListMapper.avgFinalScore();
        stats.put("avgFinalScore", avgScore != null ? Math.round(avgScore * 10.0) / 10.0 : 0);
        
        // 各分数段人数（用初试总分统计）
        int below270 = 0, between270_300 = 0, above300 = 0;
        var allScores = initialExamScoreMapper.findAll();
        for (var score : allScores) {
            double total = score.getTotalScore() != null ? score.getTotalScore() : 0;
            if (total < 270) below270++;
            else if (total <= 300) between270_300++;
            else above300++;
        }
        
        Map<String, Integer> scoreDistribution = new HashMap<>();
        scoreDistribution.put("below270", below270);
        scoreDistribution.put("between270_300", between270_300);
        scoreDistribution.put("above300", above300);
        stats.put("scoreDistribution", scoreDistribution);
        
        return Result.success(stats);
    }
}