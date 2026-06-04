package com.admission.controller;

import com.admission.entity.CandidateProfile;
import com.admission.entity.Result;
import com.admission.service.CandidateProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 考生档案控制器
 */
@RestController
@RequestMapping("/api/candidate")
public class CandidateProfileController {

    @Autowired
    private CandidateProfileService candidateService;

    /** 查询所有考生 */
    @GetMapping("/list")
    public Result<List<CandidateProfile>> list() {
        List<CandidateProfile> list = candidateService.findAll();
        return Result.success(list);
    }

    /** 根据考号查询考生 */
    @GetMapping("/{examId}")
    public Result<CandidateProfile> getByExamId(@PathVariable String examId) {
        CandidateProfile candidate = candidateService.findByExamId(examId);
        if (candidate != null) {
            return Result.success(candidate);
        }
        return Result.error("考生不存在");
    }

    /** 根据报考专业查询考生 */
    @GetMapping("/major/{majorCode}")
    public Result<List<CandidateProfile>> getByMajor(@PathVariable String majorCode) {
        List<CandidateProfile> list = candidateService.findByMajor(majorCode);
        return Result.success(list);
    }

    /** 考生报名（新增考生） */
    @PostMapping("/register")
    public Result<String> register(@RequestBody CandidateProfile candidate) {
        int count = candidateService.register(candidate);
        if (count > 0) {
            return Result.success("报名成功");
        }
        return Result.error("报名失败");
    }

    /** 更新考生信息 */
    @PutMapping("/update")
    public Result<String> update(@RequestBody CandidateProfile candidate) {
        int count = candidateService.update(candidate);
        if (count > 0) {
            return Result.success("更新考生信息成功");
        }
        return Result.error("更新考生信息失败");
    }

    /** 删除考生 */
    @DeleteMapping("/delete/{examId}")
    public Result<String> delete(@PathVariable String examId) {
        int count = candidateService.delete(examId);
        if (count > 0) {
            return Result.success("删除考生成功");
        }
        return Result.error("删除考生失败");
    }
}