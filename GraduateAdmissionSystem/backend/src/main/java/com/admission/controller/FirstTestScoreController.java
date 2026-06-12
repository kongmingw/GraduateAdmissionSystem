package com.admission.controller;

import com.admission.entity.FirstTestScore;
import com.admission.entity.Result;
import com.admission.service.FirstTestScoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 初试成绩管理控制器
 */
@RestController
@RequestMapping("/api/first-test")
@RequiredArgsConstructor
public class FirstTestScoreController {

    private final FirstTestScoreService firstTestScoreService;

    /** 查询所有初试成绩 */
    @GetMapping("/list")
    public Result<List<FirstTestScore>> list() {
        return Result.success(firstTestScoreService.findAll());
    }

    /** 根据考号查询初试成绩 */
    @GetMapping("/{examId}")
    public Result<FirstTestScore> getByExamId(@PathVariable String examId) {
        FirstTestScore score = firstTestScoreService.findByExamId(examId);
        if (score != null) {
            return Result.success(score);
        }
        return Result.error("考生成绩不存在");
    }

    /** 录入初试成绩 */
    @PostMapping("/add")
    public Result<String> add(@RequestBody FirstTestScore score) {
        int count = firstTestScoreService.add(score);
        if (count > 0) {
            return Result.success("录入初试成绩成功");
        }
        return Result.error("录入失败");
    }

    /** 更新初试成绩 */
    @PutMapping("/update")
    public Result<String> update(@RequestBody FirstTestScore score) {
        int count = firstTestScoreService.update(score);
        if (count > 0) {
            return Result.success("更新初试成绩成功");
        }
        return Result.error("更新失败");
    }

    /** 删除初试成绩 */
    @DeleteMapping("/delete/{examId}")
    public Result<String> delete(@PathVariable String examId) {
        int count = firstTestScoreService.delete(examId);
        if (count > 0) {
            return Result.success("删除成功");
        }
        return Result.error("删除失败");
    }
}