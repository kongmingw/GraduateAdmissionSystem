package com.example.admission.controller;

import com.example.admission.entity.StudentProfile;
import com.example.admission.mapper.StudentProfileMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentController {
    
    @Autowired
    private StudentProfileMapper studentProfileMapper;
    
    @GetMapping
    public List<StudentProfile> list() {
        return studentProfileMapper.findAll();
    }
    
    @GetMapping("/{examNumber}")
    public StudentProfile get(@PathVariable String examNumber) {
        return studentProfileMapper.findByExamNumber(examNumber);
    }
}