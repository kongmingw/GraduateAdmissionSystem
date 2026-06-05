package com.admission.controller;

import com.admission.entity.AdmissionList;
import com.admission.entity.FirstTestScore;
import com.admission.entity.Result;
import com.admission.entity.ScoreLine;
import com.admission.entity.SecondTestScore;
import com.admission.mapper.AdmissionListMapper;
import com.admission.mapper.FirstTestScoreMapper;
import com.admission.mapper.ScoreLineMapper;
import com.admission.mapper.SecondTestScoreMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 录取名单控制器
 */
@RestController
@RequestMapping("/api/admission")
public class AdmissionListController {

    @Autowired
    private AdmissionListMapper admissionListMapper;

    @Autowired
    private FirstTestScoreMapper firstTestScoreMapper;

    @Autowired
    private SecondTestScoreMapper secondTestScoreMapper;

    @Autowired
    private ScoreLineMapper scoreLineMapper;

    /** 查询所有录取考生 */
    @GetMapping("/list")
    public Result<List<AdmissionList>> list() {
        List<AdmissionList> list = admissionListMapper.findAll();
        return Result.success(list);
    }

    /** 根据考号查询录取状态 */
    @GetMapping("/{examId}")
    public Result<AdmissionList> getByExamId(@PathVariable String examId) {
        AdmissionList admission = admissionListMapper.findByExamId(examId);
        if (admission != null) {
            return Result.success(admission);
        }
        return Result.error("该考生未被录取");
    }

    /** 录取考生（自动计算综合总分） */
    @PostMapping("/admit")
    @Transactional
    public Result<String> admit(@RequestBody AdmissionList request) {
        String examId = request.getExamId();
        // 检查考生是否已被录取
        AdmissionList existing = admissionListMapper.findByExamId(examId);
        if (existing != null) {
            return Result.error("该考生已在录取名单中");
        }
        // 获取初试成绩
        FirstTestScore firstScore = firstTestScoreMapper.findByExamId(examId);
        if (firstScore == null) {
            return Result.error("该考生无初试成绩，无法录取");
        }
        // 获取复试成绩
        SecondTestScore secondScore = secondTestScoreMapper.findByExamId(examId);
        if (secondScore == null) {
            return Result.error("该考生无复试成绩，无法录取");
        }
        // 计算综合总分
        double totalScore = firstScore.getTotalFirst() + secondScore.getTotalSecond();
        // 检查是否达到录取总分线
        ScoreLine line = scoreLineMapper.findByYear("2026");
        if (line != null && line.getAdmissionTotalLine() != null) {
            if (totalScore < line.getAdmissionTotalLine()) {
                return Result.error("综合总分(" + totalScore + ")未达到录取总分线(" + line.getAdmissionTotalLine() + ")，无法录取");
            }
        }
        request.setFinalFirstScore(firstScore.getTotalFirst());
        request.setFinalSecondScore(secondScore.getTotalSecond());
        request.setTotalScore(totalScore);
        // 插入录取名单
        int count = admissionListMapper.insert(request);
        if (count > 0) {
            String lineInfo = (line != null && line.getAdmissionTotalLine() != null)
                    ? "（录取线：" + line.getAdmissionTotalLine() + "）" : "";
            return Result.success("录取成功，综合总分：" + totalScore + lineInfo);
        }
        return Result.error("录取失败");
    }

    /** 取消录取 */
    @DeleteMapping("/cancel/{examId}")
    @Transactional
    public Result<String> cancel(@PathVariable String examId) {
        AdmissionList existing = admissionListMapper.findByExamId(examId);
        if (existing == null) {
            return Result.error("该考生不在录取名单中");
        }
        int count = admissionListMapper.deleteByExamId(examId);
        if (count > 0) {
            return Result.success("取消录取成功");
        }
        return Result.error("取消录取失败");
    }
}
