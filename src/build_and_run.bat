@echo off
echo ========================================
echo   Library Pro - Build and Run
echo ========================================
echo.

cd /d "%~dp0"

echo [1/3] Cleaning old .class files...
del /s /q *.class >nul 2>&1

echo [2/3] Compiling Java sources...
javac -d . main\java\console_lms\*.java main\java\console_lms\models\*.java main\java\console_lms\gui\*.java main\java\console_lms\service\*.java main\java\console_lms\ui\*.java

if %ERRORLEVEL% neq 0 (
    echo.
    echo [ERROR] Compilation failed!
    pause
    exit /b 1
)

echo [3/3] Launching Library Pro...
echo.
java console_lms.Main
pause
