@echo off
javac --release 17 -encoding UTF-8 -d out src\lexer\TokenType.java src\lexer\Token.java src\lexer\LexerError.java src\lexer\Lexer.java src\ast\*.java src\parser\*.java tests\ParserTest.java
if errorlevel 1 (
    echo.
    echo Compilation failed.
    pause
    exit /b 1
)
echo.
echo Compilation successful.
echo.
java -Dfile.encoding=UTF-8 -cp out ParserTest
pause
