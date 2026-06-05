package com.admission.controller;

import com.admission.entity.Result;
import com.admission.entity.ScoreLine;
import com.admission.service.ScoreLineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 分数线管理控制器
 */
@RestController
@RequestMapping("/api/score-line")
public class ScoreLineController {

    @Autowired
    private ScoreLineService scoreLineService;

    /** 查询所有分数线 */
    @GetMapping("/list")
    public Result<List<ScoreLine>> list() {
        return Result.success(scoreLineService.findAll());
    }

    /** 根据ID查询 */
    @GetMapping("/{id}")
    public Result<ScoreLine> getById(@PathVariable Integer id) {
        ScoreLine line = scoreLineService.findById(id);
        if (line != null) {
            return Result.success(line);
        }
        return Result.error("分数线不存在");
    }

    /** 根据年份查询 */
    @GetMapping("/year/{year}")
    public Result<ScoreLine> getByYear(@PathVariable String year) {
        ScoreLine line = scoreLineService.findByYear(year);
        if (line != null) {
            return Result.success(line);
        }
        return Result.error("该年份未设定分数线");
    }

    /** 新增分数线 */
    @PostMapping("/add")
    public Result<String> add(@RequestBody ScoreLine scoreLine) {
        int count = scoreLineService.add(scoreLine);
        if (count > 0) {
            return Result.success("分数线设定成功");
        }
        return Result.error("设定失败");
    }

    /** 更新分数线 */
    @PutMapping("/update")
    public Result<String> update(@RequestBody ScoreLine scoreLine) {
        int count = scoreLineService.update(scoreLine);
        if (count > 0) {
            return Result.success("分数线更新成功");
        }
        return Result.error("更新失败");
    }

    /** 删除分数线 */
    @DeleteMapping("/delete/{id}")
    public Result<String> delete(@PathVariable Integer id) {
        int count = scoreLineService.delete(id);
        if (count > 0) {
            return Result.success("删除成功");
        }
        return Result.error("删除失败");
    }
}
