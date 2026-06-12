package com.admission.controller;

import com.admission.entity.*;
import com.admission.service.AdmissionListService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 录取名单控制器
 */
@RestController
@RequestMapping("/api/admission")
@RequiredArgsConstructor
public class AdmissionListController {

    private final AdmissionListService admissionService;

    /** 查询所有录取考生 */
    @GetMapping("/list")
    public Result<List<AdmissionList>> list() {
        List<AdmissionList> list = admissionService.findAll();
        return Result.success(list);
    }

    /** 根据考号查询录取状态 */
    @GetMapping("/{examId}")
    public Result<AdmissionList> getByExamId(@PathVariable String examId) {
        AdmissionList admission = admissionService.findByExamId(examId);
        if (admission != null) {
            return Result.success(admission);
        }
        return Result.error("该考生未被录取");
    }

    /** 录取考生（自动计算综合总分、检查分数线、名额限制） */
    @PostMapping("/admit")
    public Result<String> admit(@RequestBody AdmissionList request) {
        try {
            String msg = admissionService.admit(request);
            return Result.success(msg);
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }

    /** 获取待录取候选列表（有初试+有复试+未被录取，按录取线分组） */
    @GetMapping("/candidates")
    public Result<Map<String, Object>> candidates() {
        return Result.success(admissionService.getCandidates());
    }

    /** 取消录取 */
    @DeleteMapping("/cancel/{examId}")
    public Result<String> cancel(@PathVariable String examId) {
        try {
            admissionService.cancel(examId);
            return Result.success("取消录取成功");
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }
}
