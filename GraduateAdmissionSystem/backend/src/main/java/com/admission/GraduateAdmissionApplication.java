package com.admission;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GraduateAdmissionApplication {
    public static void main(String[] args) {
        SpringApplication.run(GraduateAdmissionApplication.class, args);
        System.out.println("===========================================");
        System.out.println("  研究生招生信息管理系统启动成功！");
        System.out.println("===========================================");
    }
}