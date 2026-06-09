@echo off
title Frontend - Graduate Admission

echo === Starting Frontend (port 5173) ===
cd /d "%~dp0..\frontend"
npm run dev
pause
