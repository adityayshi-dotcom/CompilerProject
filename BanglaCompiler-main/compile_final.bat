@echo off
chcp 65001 >nul

echo Compiling complete Bangla compiler...
javac --release 17 -encoding UTF-8 -d out src\lexer\*.java src\ast\*.java src\parser\*.java src\semantic\*.java src\codegen\*.java src\BanglaCompiler.java

if errorlevel 1 (
    echo.
    echo Compiler build failed.
    pause
    exit /b 1
)

echo.
echo Compiler build successful.
echo.
echo Running compiler on examples\full_program.bng...
java -Dfile.encoding=UTF-8 -cp out BanglaCompiler examples\full_program.bng

if errorlevel 1 (
    echo.
    echo Bangla compiler failed.
    pause
    exit /b 1
)

if not exist generated\GeneratedProgram.java (
    echo.
    echo GeneratedProgram.java was not created.
    pause
    exit /b 1
)

echo.
echo Generated Java source:
echo ----------------------------------------
type generated\GeneratedProgram.java
echo ----------------------------------------

echo.
echo Compiling generated Java...
javac --release 17 -encoding UTF-8 -d out generated\GeneratedProgram.java

if errorlevel 1 (
    echo.
    echo Generated Java compilation failed.
    pause
    exit /b 1
)

echo.
echo Running generated Java...
java -Dfile.encoding=UTF-8 -cp out GeneratedProgram

if errorlevel 1 (
    echo.
    echo Generated Java program failed.
    pause
    exit /b 1
)

echo.
echo ========================================
echo COMPLETE COMPILER TEST PASSED
echo ========================================
pause
