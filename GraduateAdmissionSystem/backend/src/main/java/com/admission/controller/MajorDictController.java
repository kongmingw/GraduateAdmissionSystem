package com.admission.controller;

import com.admission.entity.*;
import com.admission.mapper.AdmissionListMapper;
import com.admission.mapper.CandidateProfileMapper;
import com.admission.service.MajorDictService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 专业字典控制器
 */
@RestController
@RequestMapping("/api/major")
@RequiredArgsConstructor
public class MajorDictController {

    private final MajorDictService majorDictService;
    private final AdmissionListMapper admissionListMapper;
    private final CandidateProfileMapper candidateProfileMapper;

    /** 查询所有专业 */
    @GetMapping("/list")
    public Result<List<MajorDict>> list() {
        List<MajorDict> list = majorDictService.findAll();
        return Result.success(list);
    }

    /** 根据专业代码查询 */
    @GetMapping("/{majorCode}")
    public Result<MajorDict> getByCode(@PathVariable String majorCode) {
        MajorDict major = majorDictService.findByCode(majorCode);
        if (major != null) {
            return Result.success(major);
        }
        return Result.error("专业不存在");
    }

    /** 新增专业 */
    @PostMapping("/add")
    public Result<String> add(@RequestBody MajorDict majorDict) {
        int count = majorDictService.add(majorDict);
        if (count > 0) {
            return Result.success("新增专业成功");
        }
        return Result.error("新增专业失败");
    }

    /** 更新专业 */
    @PutMapping("/update")
    public Result<String> update(@RequestBody MajorDict majorDict) {
        // 检查已录取人数是否超过新的招生数
        String majorCode = majorDict.getMajorCode();
        int planNei = 0, planWai = 0;
        for (AdmissionList a : admissionListMapper.findAll()) {
            if (majorCode.equals(a.getAdmittedMajor())) {
                CandidateProfile cp = candidateProfileMapper.findByExamId(a.getExamId());
                if (cp != null) {
                    if ("计划内".equals(cp.getCategory())) planNei++;
                    else planWai++;
                }
            }
        }
        if (planNei > majorDict.getPlannedInside()) {
            return Result.error("计划内已录取" + planNei + "人，不能将招生数改为" + majorDict.getPlannedInside());
        }
        if (planWai > majorDict.getPlannedOutside()) {
            return Result.error("计划外已录取" + planWai + "人，不能将招生数改为" + majorDict.getPlannedOutside());
        }
        int count = majorDictService.update(majorDict);
        if (count > 0) {
            return Result.success("更新专业成功");
        }
        return Result.error("更新专业失败");
    }

    /** 删除专业 */
    @DeleteMapping("/delete/{majorCode}")
    public Result<String> delete(@PathVariable String majorCode) {
        int count = majorDictService.delete(majorCode);
        if (count > 0) {
            return Result.success("删除专业成功");
        }
        return Result.error("删除专业失败");
    }
}