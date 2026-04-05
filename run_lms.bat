@echo off
setlocal
echo ==================================================
echo   Library Pro - Modern Violet Management System
echo ==================================================
echo.

set JAVA_SRC=src\main\java
set LIB_DIR=lib
set BIN_DIR=target\classes

if not exist %BIN_DIR% mkdir %BIN_DIR%

echo [1/2] Compiling project...
javac -d %BIN_DIR% -cp "%JAVA_SRC%;%LIB_DIR%\*" %JAVA_SRC%\console_lms\Main.java
if %errorlevel% neq 0 (
    echo.
    echo ERROR: Compilation failed. Please check for syntax errors.
    pause
    exit /b %errorlevel%
)

echo [2/2] Launching application...
start /b javaw -cp "%BIN_DIR%;%LIB_DIR%\*" console_lms.Main

echo.
echo Application started. You can now login with:
echo   - Admin: admin / admin123
echo   - User:  user  / user123
echo.
echo Check the Library Management window.
pause
