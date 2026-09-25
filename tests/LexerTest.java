import lexer.Lexer;
import lexer.LexerError;
import lexer.Token;
import lexer.TokenType;

import java.util.List;

public class LexerTest {

    public static void main(String[] args) {

        testKeywords();
        testOperators();
        testInvalidCharacter();
        testUnterminatedString();
        testNumbers();
        testStrings();
        testIdentifiers();
        testPositions();

        System.out.println();
        System.out.println("All lexer tests passed.");
    }


    private static void testKeywords() {

        String source = "সংখ্যা লেখা যদি নাহলে যতক্ষণ বয়স";

        Lexer lexer = new Lexer(source);
        List<Token> tokens = lexer.tokenize();

        TokenType[] expected = {
                TokenType.INT,
                TokenType.STRING_TYPE,
                TokenType.IF,
                TokenType.ELSE,
                TokenType.WHILE,
                TokenType.IDENTIFIER,
                TokenType.EOF
        };

        check(tokens, expected, "Keyword test");

        if (lexer.hasErrors()) {
            throw new AssertionError(
                    "Keyword test unexpectedly produced lexical errors."
            );
        }
    }


    private static void testOperators() {

        String source =
                "+ - * / = == != < <= > >= ( ) { } ;";

        Lexer lexer = new Lexer(source);
        List<Token> tokens = lexer.tokenize();

        TokenType[] expected = {
                TokenType.PLUS,
                TokenType.MINUS,
                TokenType.STAR,
                TokenType.SLASH,

                TokenType.ASSIGN,
                TokenType.EQUAL_EQUAL,
                TokenType.NOT_EQUAL,

                TokenType.LESS,
                TokenType.LESS_EQUAL,
                TokenType.GREATER,
                TokenType.GREATER_EQUAL,

                TokenType.LEFT_PAREN,
                TokenType.RIGHT_PAREN,
                TokenType.LEFT_BRACE,
                TokenType.RIGHT_BRACE,
                TokenType.SEMICOLON,

                TokenType.EOF
        };

        check(tokens, expected, "Operator test");

        if (lexer.hasErrors()) {
            throw new AssertionError(
                    "Operator test unexpectedly produced lexical errors."
            );
        }
    }


    private static void testInvalidCharacter() {

        String source = "সংখ্যা বয়স = 20 @ 5;";

        Lexer lexer = new Lexer(source);
        lexer.tokenize();

        if (!lexer.hasErrors()) {
            throw new AssertionError(
                    "Invalid character test failed: expected a lexical error."
            );
        }

        List<LexerError> errors = lexer.getErrors();

        if (errors.size() != 1) {
            throw new AssertionError(
                    "Invalid character test failed: expected 1 error but got "
                            + errors.size()
            );
        }

        System.out.println("Invalid character test passed.");
        System.out.println(errors.get(0));
    }


    private static void testUnterminatedString() {

        String source = "লেখা নাম = \"রহিম";

        Lexer lexer = new Lexer(source);
        lexer.tokenize();

        if (!lexer.hasErrors()) {
            throw new AssertionError(
                    "Unterminated string test failed: expected an error."
            );
        }

        System.out.println("Unterminated string test passed.");
        System.out.println(lexer.getErrors().get(0));
    }


    private static void testNumbers() {

        String source = "10 25 100 0";

        Lexer lexer = new Lexer(source);
        List<Token> tokens = lexer.tokenize();

        TokenType[] expected = {
                TokenType.NUMBER,
                TokenType.NUMBER,
                TokenType.NUMBER,
                TokenType.NUMBER,
                TokenType.EOF
        };

        check(tokens, expected, "Number test");

        if (lexer.hasErrors()) {
            throw new AssertionError(
                    "Number test unexpectedly produced lexical errors."
            );
        }
    }


    private static void testStrings() {

        String source = "লেখা নাম = \"রহিম\";";

        Lexer lexer = new Lexer(source);
        List<Token> tokens = lexer.tokenize();

        TokenType[] expected = {
                TokenType.STRING_TYPE,
                TokenType.IDENTIFIER,
                TokenType.ASSIGN,
                TokenType.STRING,
                TokenType.SEMICOLON,
                TokenType.EOF
        };

        check(tokens, expected, "String test");

        if (lexer.hasErrors()) {
            throw new AssertionError(
                    "String test unexpectedly produced lexical errors."
            );
        }
    }


    private static void testIdentifiers() {

        String source =
                "বয়স total_count student1";

        Lexer lexer = new Lexer(source);
        List<Token> tokens = lexer.tokenize();

        TokenType[] expected = {
                TokenType.IDENTIFIER,
                TokenType.IDENTIFIER,
                TokenType.IDENTIFIER,
                TokenType.EOF
        };

        check(tokens, expected, "Identifier test");

        if (lexer.hasErrors()) {
            throw new AssertionError(
                    "Identifier test unexpectedly produced lexical errors."
            );
        }
    }


    private static void testPositions() {

        String source =
                "abc = 12;\n"
                        + "xyz = 3;";

        Lexer lexer = new Lexer(source);
        List<Token> tokens = lexer.tokenize();

        Token firstIdentifier = tokens.get(0);

        if (firstIdentifier.getLine() != 1
                || firstIdentifier.getColumn() != 1) {

            throw new AssertionError(
                    "Position test failed for first identifier."
            );
        }


        Token firstNumber = tokens.get(2);

        if (firstNumber.getLine() != 1
                || firstNumber.getColumn() != 7) {

            throw new AssertionError(
                    "Position test failed for first number."
            );
        }


        Token secondIdentifier = tokens.get(4);

        if (secondIdentifier.getLine() != 2
                || secondIdentifier.getColumn() != 1) {

            throw new AssertionError(
                    "Position test failed for second line."
            );
        }

        System.out.println("Position test passed.");
    }


    private static void check(
            List<Token> tokens,
            TokenType[] expected,
            String testName) {

        if (tokens.size() != expected.length) {

            throw new AssertionError(
                    testName
                            + " failed: expected "
                            + expected.length
                            + " tokens but received "
                            + tokens.size()
            );
        }


        for (int i = 0; i < expected.length; i++) {

            TokenType actual =
                    tokens.get(i).getType();

            if (actual != expected[i]) {

                throw new AssertionError(
                        testName
                                + " failed at token "
                                + i
                                + ": expected "
                                + expected[i]
                                + " but received "
                                + actual
                );
            }
        }

        System.out.println(testName + " passed.");
    }
}