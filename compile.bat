@echo off
setlocal

set PROJECT_DIR=%~dp0
set JAVAFX_LIB=%PROJECT_DIR%javafx\lib
set SRC_DIR=%PROJECT_DIR%src\main\java
set RESOURCES_DIR=%PROJECT_DIR%src\main\resources
set OUT_DIR=%PROJECT_DIR%out

if exist "%OUT_DIR%" rmdir /s /q "%OUT_DIR%"
mkdir "%OUT_DIR%"

echo Compiling Java files...
dir /s /b "%SRC_DIR%\*.java" > "%PROJECT_DIR%sources.txt"

javac --module-path "%JAVAFX_LIB%" --add-modules javafx.controls,javafx.fxml,javafx.graphics -d "%OUT_DIR%" @"%PROJECT_DIR%sources.txt"

if %ERRORLEVEL% == 0 (
    echo Compilation successful!
    copy "%RESOURCES_DIR%\styles.css" "%OUT_DIR%\"
    del "%PROJECT_DIR%sources.txt"
) else (
    echo Compilation failed!
    del "%PROJECT_DIR%sources.txt"
    exit /b 1
)
