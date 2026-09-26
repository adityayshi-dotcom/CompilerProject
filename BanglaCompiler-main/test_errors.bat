@echo off
chcp 65001 >nul

javac --release 17 -encoding UTF-8 -d out src\lexer\*.java src\ast\*.java src\parser\*.java src\semantic\*.java src\codegen\*.java src\BanglaCompiler.java

if errorlevel 1 (
    echo Compiler build failed.
    pause
    exit /b 1
)

echo.
echo ===== LEXICAL ERROR TEST =====
java -Dfile.encoding=UTF-8 -cp out BanglaCompiler examples\lexical_error.bng

echo.
echo ===== SYNTAX ERROR TEST =====
java -Dfile.encoding=UTF-8 -cp out BanglaCompiler examples\syntax_error.bng

echo.
echo ===== TYPE ERROR TEST =====
java -Dfile.encoding=UTF-8 -cp out BanglaCompiler examples\type_error.bng

echo.
pause
