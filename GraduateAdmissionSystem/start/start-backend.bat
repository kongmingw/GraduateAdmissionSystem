@echo off
title Backend - Graduate Admission

echo === Stopping old process on port 8080 ===
for /f "tokens=5" %%a in ('netstat -ano ^| findstr ":8080 " ^| findstr "LISTENING"') do (
    taskkill /PID %%a /F >nul 2>&1
)

echo === Starting Backend (port 8080) ===
cd /d "%~dp0..\backend"
mvn spring-boot:run
pause
