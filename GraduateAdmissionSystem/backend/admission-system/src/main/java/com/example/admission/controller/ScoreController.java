package com.example.admission.controller;

import com.example.admission.common.Result;
import com.example.admission.entity.InitialExamScore;
import com.example.admission.entity.ReexamScore;
import com.example.admission.service.ScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/scores")
public class ScoreController {
    
    @Autowired
    private ScoreService scoreService;
    
    // 获取初试成绩
    @GetMapping("/initial/{examNumber}")
    public Result<InitialExamScore> getInitialScore(@PathVariable String examNumber) {
        return Result.success(scoreService.getInitialScore(examNumber));
    }
    
    // 保存初试成绩
    @PostMapping("/initial")
    public Result<String> saveInitialScore(@RequestBody InitialExamScore score) {
        scoreService.saveInitialScore(score);
        return Result.success("保存成功");
    }
    
    // 获取复试成绩
    @GetMapping("/reexam/{examNumber}")
    public Result<ReexamScore> getReexamScore(@PathVariable String examNumber) {
        return Result.success(scoreService.getReexamScore(examNumber));
    }
    
    // 保存复试成绩
    @PostMapping("/reexam")
    public Result<String> saveReexamScore(@RequestBody ReexamScore score) {
        scoreService.saveReexamScore(score);
        return Result.success("保存成功");
    }
}