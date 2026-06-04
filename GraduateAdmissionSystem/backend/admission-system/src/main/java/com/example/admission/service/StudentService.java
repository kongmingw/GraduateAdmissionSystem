package com.example.admission.service;

import com.example.admission.entity.StudentProfile;
import java.util.List;

public interface StudentService {
    List<StudentProfile> getAllStudents();
    StudentProfile getStudentByExamNumber(String examNumber);
    int addStudent(StudentProfile student);
    int updateStudent(StudentProfile student);
    int deleteStudent(String examNumber);
}