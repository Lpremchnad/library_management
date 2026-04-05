@echo off
echo Compiling Library Management System...
if not exist "target\classes" mkdir target\classes
javac -d target/classes -cp ".;lib\mysql-connector-j-8.0.33.jar;lib\jxdatepicker-support-1.0.jar;lib\swingx-1.6.1.jar" -sourcepath src/main/java src/main/java/*.java
if %errorlevel% neq 0 (
    echo Compilation Failed!
    exit /b %errorlevel%
) else (
    echo Compilation Successful!
)
