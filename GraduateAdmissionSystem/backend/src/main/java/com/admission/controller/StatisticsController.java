package com.admission.controller;

import com.admission.entity.Result;
import com.admission.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 统计分析控制器
 */
@RestController
@RequestMapping("/api/statistics")
@RequiredArgsConstructor
public class StatisticsController {

    private final StatisticsService statisticsService;

    /** 获取录取统计数据 */
    @GetMapping("/admission")
    public Result<Map<String, Object>> getAdmissionStatistics() {
        return Result.success(statisticsService.getAdmissionStatistics());
    }

    /** 获取成绩分析统计数据 */
    @GetMapping("/scores")
    public Result<Map<String, Object>> getScoreStatistics() {
        return Result.success(statisticsService.getScoreStatistics());
    }

    /** 获取所有统计数据 */
    @GetMapping("/all")
    public Result<Map<String, Object>> getAllStatistics() {
        return Result.success(statisticsService.getAllStatistics());
    }

    /** 初试筛选：根据分数线自动判定复试资格 */
    @GetMapping("/screening")
    public Result<Map<String, Object>> screening(@RequestParam(required = false) String year) {
        try {
            return Result.success(statisticsService.screening(year));
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }
}
