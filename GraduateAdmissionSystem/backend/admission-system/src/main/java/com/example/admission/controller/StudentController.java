package com.example.admission.controller;

import com.example.admission.common.Result;
import com.example.admission.entity.StudentProfile;
import com.example.admission.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentController {
    
    @Autowired
    private StudentService studentService;
    
    // 获取所有考生
    @GetMapping
    public Result<List<StudentProfile>> list() {
        return Result.success(studentService.getAllStudents());
    }
    
    // 根据考号获取考生
    @GetMapping("/{examNumber}")
    public Result<StudentProfile> get(@PathVariable String examNumber) {
        return Result.success(studentService.getStudentByExamNumber(examNumber));
    }
    
    // 新增考生
    @PostMapping
    public Result<String> add(@RequestBody StudentProfile student) {
        studentService.addStudent(student);
        return Result.success("添加成功");
    }
    
    // 更新考生信息
    @PutMapping
    public Result<String> update(@RequestBody StudentProfile student) {
        studentService.updateStudent(student);
        return Result.success("更新成功");
    }
    
    // 删除考生
    @DeleteMapping("/{examNumber}")
    public Result<String> delete(@PathVariable String examNumber) {
        studentService.deleteStudent(examNumber);
        return Result.success("删除成功");
    }
}