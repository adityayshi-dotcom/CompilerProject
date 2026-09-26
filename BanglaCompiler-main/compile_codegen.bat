@echo off
javac --release 17 -encoding UTF-8 -d out src\lexer\*.java src\ast\*.java src\parser\*.java src\semantic\*.java src\codegen\*.java tests\JavaGeneratorTest.java
if errorlevel 1 (
    echo.
    echo Project compilation failed.
    pause
    exit /b 1
)

echo.
echo Project compilation successful.
echo.

java -Dfile.encoding=UTF-8 -cp out JavaGeneratorTest
if errorlevel 1 (
    echo.
    echo Generator tests failed.
    pause
    exit /b 1
)

echo.
echo Compiling generated Java program...
javac --release 17 -encoding UTF-8 -d out out\generated\GeneratedProgram.java
if errorlevel 1 (
    echo.
    echo Generated Java compilation failed.
    pause
    exit /b 1
)

echo Generated Java compiled successfully.
echo.

java -Dfile.encoding=UTF-8 -cp out GeneratedProgram
if errorlevel 1 (
    echo Generated Java program failed to run.
    pause
    exit /b 1
)

echo Generated Java ran successfully.
echo.
echo Code generation stage passed completely.
pause
