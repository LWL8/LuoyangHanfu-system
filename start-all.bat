@echo off
TITLE Luoyang Hanfu System - One-Click Launcher
setlocal enabledelayedexpansion

:: Generate ESC character for ANSI colors
for /F "tokens=1,2 delims=#" %%a in ('"prompt #$H#$E# & echo on & for %%b in (1) do rem"') do set "ESC=%%b"

set "GREEN=!ESC![92m"
set "BLUE=!ESC![94m"
set "YELLOW=!ESC![93m"
set "CYAN=!ESC![96m"
set "RESET=!ESC![0m"

echo.
echo !BLUE!================================================================!RESET!
echo !BLUE!          LUOYANG HANFU SYSTEM - ONE-CLICK LAUNCHER           !RESET!
echo !BLUE!================================================================!RESET!
echo.

:: Check for MySQL (Optional reminder)
echo !YELLOW![INFO] Checking dependencies...!RESET!
echo !YELLOW![INFO] Ensure MySQL is running on port 3306 (database: luoyang_hanfu).!RESET!
echo.

:: Start Backend
echo !BLUE![BACKEND]!RESET! Starting Spring Boot Backend...
start "Hanfu-Backend" cmd /k "echo !BLUE![BACKEND]!RESET! Starting Maven... && cd /d backend && mvn spring-boot:run"

:: Start Frontend
echo !GREEN![FRONTEND]!RESET! Starting Vite Development Server...
start "Hanfu-Frontend" cmd /k "echo !GREEN![FRONTEND]!RESET! Starting NPM... && cd /d frontend && npm run dev"

echo.
echo !CYAN!----------------------------------------------------------------!RESET!
echo !CYAN![SUCCESS] Services are launching in separate windows.!RESET!
echo !CYAN![SUCCESS] Backend: http://localhost:8080!RESET!
echo !CYAN![SUCCESS] Frontend: (Usually http://localhost:5173)!RESET!
echo !CYAN!----------------------------------------------------------------!RESET!
echo.
echo !YELLOW!Press any key to close this launcher window (services will stay running).!RESET!
pause >nul
