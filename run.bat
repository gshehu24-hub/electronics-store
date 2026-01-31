@echo off
setlocal

set PROJECT_DIR=%~dp0
set JAVAFX_LIB=%PROJECT_DIR%javafx\lib
set OUT_DIR=%PROJECT_DIR%out

if not exist "%OUT_DIR%" (
    echo Project not compiled. Running compile.bat first...
    call "%PROJECT_DIR%compile.bat"
)

java --module-path "%JAVAFX_LIB%" --add-modules javafx.controls,javafx.fxml,javafx.graphics -cp "%OUT_DIR%" electronicstore.Main
