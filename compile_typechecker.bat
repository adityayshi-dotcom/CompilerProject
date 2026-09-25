@echo off
javac --release 17 -encoding UTF-8 -d out src\lexer\*.java src\ast\*.java src\parser\*.java src\semantic\*.java tests\TypeCheckerTest.java
if errorlevel 1 (
    echo.
    echo Compilation failed.
    pause
    exit /b 1
)

echo.
echo Compilation successful.
echo.

java -Dfile.encoding=UTF-8 -cp out TypeCheckerTest
pause
