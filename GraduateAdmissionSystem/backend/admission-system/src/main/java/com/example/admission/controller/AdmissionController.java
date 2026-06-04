package com.example.admission.controller;

import com.example.admission.common.Result;
import com.example.admission.entity.AdmissionList;
import com.example.admission.service.AdmissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admission")
public class AdmissionController {
    
    @Autowired
    private AdmissionService admissionService;
    
    // 获取所有录取名单
    @GetMapping
    public Result<List<AdmissionList>> list() {
        return Result.success(admissionService.getAllAdmissions());
    }
    
    // 录取考生
    @PostMapping("/{examNumber}")
    public Result<String> admit(@PathVariable String examNumber, @RequestParam String department) {
        admissionService.admit(examNumber, department);
        return Result.success("录取成功");
    }
    
    // 更新录取信息
    @PutMapping
    public Result<String> update(@RequestBody AdmissionList admission) {
        admissionService.updateAdmission(admission);
        return Result.success("更新成功");
    }
    
    // 取消录取
    @DeleteMapping("/{examNumber}")
    public Result<String> delete(@PathVariable String examNumber) {
        admissionService.deleteAdmission(examNumber);
        return Result.success("取消录取成功");
    }
}