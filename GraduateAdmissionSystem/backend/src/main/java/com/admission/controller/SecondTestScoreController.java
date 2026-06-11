package com.admission.controller;

import com.admission.entity.FirstTestScore;
import com.admission.entity.Result;
import com.admission.entity.ScoreLine;
import com.admission.entity.SecondTestScore;
import com.admission.mapper.FirstTestScoreMapper;
import com.admission.mapper.ScoreLineMapper;
import com.admission.service.SecondTestScoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/second-test")
@RequiredArgsConstructor
public class SecondTestScoreController {

    private final SecondTestScoreService secondTestScoreService;
    private final ScoreLineMapper scoreLineMapper;
    private final FirstTestScoreMapper firstTestScoreMapper;

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
        String examId = score.getExamId();
        // 检查是否有初试成绩
        FirstTestScore first = firstTestScoreMapper.findByExamId(examId);
        if (first == null) {
            return Result.error("该考生无初试成绩，不能录入复试成绩");
        }
        // 检查初试是否过线（使用当前年份）
        String currentYear = String.valueOf(java.time.Year.now().getValue());
        ScoreLine line = scoreLineMapper.findByYear(currentYear);
        if (line != null) {
            boolean pass = first.getPolitics() >= line.getPoliticsLine()
                    && first.getForeignLang() >= line.getForeignLangLine()
                    && first.getMajorBasis() >= line.getMajorBasisLine()
                    && first.getTotalFirst() >= line.getTotalFirstLine();
            if (!pass) {
                return Result.error("该考生初试未过线，不能参加复试");
            }
        }
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