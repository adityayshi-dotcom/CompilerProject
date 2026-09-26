package lexer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Lexer {

    private final String source;

    private final List<Token> tokens = new ArrayList<>();
    private final List<LexerError> errors = new ArrayList<>();

    private int start = 0;
    private int current = 0;

    private int line = 1;
    private int column = 1;

    private int tokenLine = 1;
    private int tokenColumn = 1;

    private static final Map<String, TokenType> KEYWORDS = new HashMap<>();

    static {
        KEYWORDS.put("সংখ্যা", TokenType.INT);
        KEYWORDS.put("লেখা", TokenType.STRING_TYPE);
        KEYWORDS.put("যদি", TokenType.IF);
        KEYWORDS.put("নাহলে", TokenType.ELSE);
        KEYWORDS.put("যতক্ষণ", TokenType.WHILE);
    }

    public Lexer(String source) {
        this.source = source;
    }

    public List<Token> tokenize() {

        while (!isAtEnd()) {

            start = current;
            tokenLine = line;
            tokenColumn = column;

            scanToken();
        }

        tokens.add(
                new Token(
                        TokenType.EOF,
                        "",
                        line,
                        column
                )
        );

        return tokens;
    }

    public List<LexerError> getErrors() {
        return errors;
    }

    public boolean hasErrors() {
        return !errors.isEmpty();
    }

    private void scanToken() {

        char c = advance();

        switch (c) {

            case '(':
                addToken(TokenType.LEFT_PAREN);
                break;

            case ')':
                addToken(TokenType.RIGHT_PAREN);
                break;

            case '{':
                addToken(TokenType.LEFT_BRACE);
                break;

            case '}':
                addToken(TokenType.RIGHT_BRACE);
                break;

            case ';':
                addToken(TokenType.SEMICOLON);
                break;

            case '+':
                addToken(TokenType.PLUS);
                break;

            case '-':
                addToken(TokenType.MINUS);
                break;

            case '*':
                addToken(TokenType.STAR);
                break;

            case '/':
                addToken(TokenType.SLASH);
                break;

            case '=':
                if (match('=')) {
                    addToken(TokenType.EQUAL_EQUAL);
                } else {
                    addToken(TokenType.ASSIGN);
                }
                break;

            case '!':
                if (match('=')) {
                    addToken(TokenType.NOT_EQUAL);
                } else {
                    reportError("Unexpected character '!'. Did you mean '!='?");
                    addToken(TokenType.ERROR);
                }
                break;

            case '<':
                if (match('=')) {
                    addToken(TokenType.LESS_EQUAL);
                } else {
                    addToken(TokenType.LESS);
                }
                break;

            case '>':
                if (match('=')) {
                    addToken(TokenType.GREATER_EQUAL);
                } else {
                    addToken(TokenType.GREATER);
                }
                break;

            case '"':
                scanString();
                break;

            case ' ':
            case '\r':
            case '\t':
            case '\n':
                break;

            default:

                if (Character.isDigit(c)) {
                    scanNumber();
                }

                else if (isIdentifierStart(c)) {
                    scanIdentifier();
                }

                else {
                    reportError(
                            "Unexpected character '" + c + "'"
                    );

                    addToken(TokenType.ERROR);
                }

                break;
        }
    }

    private void scanNumber() {

        while (!isAtEnd()
                && Character.isDigit(peek())) {

            advance();
        }

        addToken(TokenType.NUMBER);
    }

    private void scanIdentifier() {

        while (!isAtEnd()
                && isIdentifierPart(peek())) {

            advance();
        }

        String text =
                source.substring(start, current);

        TokenType type =
                KEYWORDS.get(text);

        if (type == null) {
            type = TokenType.IDENTIFIER;
        }

        addToken(type);
    }

    private void scanString() {

        while (!isAtEnd()
                && peek() != '"') {

            advance();
        }

        if (isAtEnd()) {

            reportError(
                    "Unterminated string literal"
            );

            addToken(TokenType.ERROR);

            return;
        }

        advance();

        addToken(TokenType.STRING);
    }

    private boolean isIdentifierStart(char c) {

        return Character.isLetter(c)
                || c == '_';
    }

    private boolean isIdentifierPart(char c) {

        int type =
                Character.getType(c);

        return Character.isLetterOrDigit(c)
                || c == '_'
                || type == Character.NON_SPACING_MARK
                || type == Character.COMBINING_SPACING_MARK
                || type == Character.ENCLOSING_MARK;
    }

    private char advance() {

        char c =
                source.charAt(current);

        current++;

        if (c == '\n') {

            line++;
            column = 1;

        } else {

            column++;
        }

        return c;
    }

    private boolean match(char expected) {

        if (isAtEnd()) {
            return false;
        }

        if (source.charAt(current)
                != expected) {

            return false;
        }

        advance();

        return true;
    }

    private char peek() {

        if (isAtEnd()) {
            return '\0';
        }

        return source.charAt(current);
    }

    private void addToken(TokenType type) {

        String lexeme =
                source.substring(
                        start,
                        current
                );

        tokens.add(
                new Token(
                        type,
                        lexeme,
                        tokenLine,
                        tokenColumn
                )
        );
    }

    private void reportError(String message) {

        errors.add(
                new LexerError(
                        message,
                        tokenLine,
                        tokenColumn
                )
        );
    }

    private boolean isAtEnd() {
        return current >= source.length();
    }
}