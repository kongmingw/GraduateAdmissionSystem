package com.example.admission.service.impl;

import com.example.admission.entity.StudentProfile;
import com.example.admission.mapper.StudentProfileMapper;
import com.example.admission.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {
    
    @Autowired
    private StudentProfileMapper studentProfileMapper;
    
    @Override
    public List<StudentProfile> getAllStudents() {
        return studentProfileMapper.findAll();
    }
    
    @Override
    public StudentProfile getStudentByExamNumber(String examNumber) {
        return studentProfileMapper.findByExamNumber(examNumber);
    }
    
    @Override
    @Transactional
    public int addStudent(StudentProfile student) {
        return studentProfileMapper.insert(student);
    }
    
    @Override
    @Transactional
    public int updateStudent(StudentProfile student) {
        return studentProfileMapper.update(student);
    }
    
    @Override
    @Transactional
    public int deleteStudent(String examNumber) {
        return studentProfileMapper.delete(examNumber);
    }
}