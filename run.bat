@echo off
echo Compiling...
javac -cp ".;lib/ojdbc17.jar" src/RunGUI.java src/View/DatabaseManagementGUI.java src/ConnectDB/ConnectionUtils.java src/ConnectDB/ConnectionOracle.java
if errorlevel 1 (
    echo Compilation failed
    pause
    exit /b 1
)
echo Running...
java -cp ".;lib/ojdbc17.jar;src" RunGUI
pause 