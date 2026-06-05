package com.admission.controller;

import com.admission.entity.SecondTestScore;
import com.admission.entity.Result;
import com.admission.service.SecondTestScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/second-test")
public class SecondTestScoreController {

    @Autowired
    private SecondTestScoreService secondTestScoreService;

    @GetMapping("/list")
    public Result<List<SecondTestScore>> list() {
        return Result.success(secondTestScoreService.findAll());
    }

    @GetMapping("/{examId}")
    public Result<SecondTestScore> getByExamId(@PathVariable String examId) {
        SecondTestScore score = secondTestScoreService.findByExamId(examId);
        if (score != null) {
            return Result.success(score);
        }
        return Result.error("考生复试成绩不存在");
    }

    @PostMapping("/add")
    public Result<String> add(@RequestBody SecondTestScore score) {
        int count = secondTestScoreService.add(score);
        if (count > 0) {
            return Result.success("录入复试成绩成功");
        }
        return Result.error("录入失败");
    }

    @PutMapping("/update")
    public Result<String> update(@RequestBody SecondTestScore score) {
        int count = secondTestScoreService.update(score);
        if (count > 0) {
            return Result.success("更新复试成绩成功");
        }
        return Result.error("更新失败");
    }

    @DeleteMapping("/delete/{examId}")
    public Result<String> delete(@PathVariable String examId) {
        int count = secondTestScoreService.delete(examId);
        if (count > 0) {
            return Result.success("删除成功");
        }
        return Result.error("删除失败");
    }
}