@echo off
title Graduate Admission System

echo === Starting Backend (port 8080) ===
start "Backend" cmd /c "cd /d %~dp0..\backend && mvn spring-boot:run"

echo Waiting 5 seconds for backend...
timeout /t 5 /nobreak

echo === Starting Frontend (port 5173) ===
start "Frontend" cmd /c "cd /d %~dp0..\frontend && npm run dev"

echo.
echo Backend:  http://localhost:8080
echo Frontend: http://localhost:5173
echo.
echo Waiting for frontend to be ready...
timeout /t 5 /nobreak
start http://localhost:5173

pause
