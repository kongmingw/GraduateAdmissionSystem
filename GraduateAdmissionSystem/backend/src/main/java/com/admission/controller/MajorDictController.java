package com.admission.controller;

import com.admission.entity.MajorDict;
import com.admission.entity.Result;
import com.admission.service.MajorDictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 专业字典控制器
 */
@RestController
@RequestMapping("/api/major")
public class MajorDictController {

    @Autowired
    private MajorDictService majorDictService;

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