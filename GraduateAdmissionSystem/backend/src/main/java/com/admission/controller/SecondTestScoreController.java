package com.admission.controller;

import com.admission.entity.Result;
import com.admission.entity.SecondTestScore;
import com.admission.service.SecondTestScoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 复试成绩管理控制器
 */
@RestController
@RequestMapping("/api/second-test")
@RequiredArgsConstructor
public class SecondTestScoreController {

    private final SecondTestScoreService secondTestScoreService;

    /** 查询所有复试成绩 */
    @GetMapping("/list")
    public Result<List<SecondTestScore>> list() {
        return Result.success(secondTestScoreService.findAll());
    }

    /** 根据考号查询复试成绩 */
    @GetMapping("/{examId}")
    public Result<SecondTestScore> getByExamId(@PathVariable String examId) {
        SecondTestScore score = secondTestScoreService.findByExamId(examId);
        if (score != null) {
            return Result.success(score);
        }
        return Result.error("考生复试成绩不存在");
    }

    /** 录入复试成绩（初试过线校验在Service层） */
    @PostMapping("/add")
    public Result<String> add(@RequestBody SecondTestScore score) {
        try {
            int count = secondTestScoreService.add(score);
            if (count > 0) {
                return Result.success("录入复试成绩成功");
            }
            return Result.error("录入失败");
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }

    /** 更新复试成绩 */
    @PutMapping("/update")
    public Result<String> update(@RequestBody SecondTestScore score) {
        int count = secondTestScoreService.update(score);
        if (count > 0) {
            return Result.success("更新复试成绩成功");
        }
        return Result.error("更新失败");
    }

    /** 删除复试成绩 */
    @DeleteMapping("/delete/{examId}")
    public Result<String> delete(@PathVariable String examId) {
        int count = secondTestScoreService.delete(examId);
        if (count > 0) {
            return Result.success("删除成功");
        }
        return Result.error("删除失败");
    }
}