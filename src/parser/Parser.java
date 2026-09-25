package parser;

import ast.*;
import lexer.Token;
import lexer.TokenType;

import java.util.ArrayList;
import java.util.List;

public class Parser {

    private final List<Token> tokens;
    private final List<ParserError> errors = new ArrayList<>();
    private int current = 0;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    public List<Statement> parse() {

        List<Statement> statements = new ArrayList<>();

        while (!isAtEnd()) {
            Statement statement = parseStatementSafely();

            if (statement != null) {
                statements.add(statement);
            }
        }

        return statements;
    }

    public List<ParserError> getErrors() {
        return errors;
    }

    public boolean hasErrors() {
        return !errors.isEmpty();
    }

    private Statement parseStatementSafely() {

        try {
            return statement();
        } catch (ParseException e) {
            synchronize();
            return null;
        }
    }

    private Statement statement() {

        if (match(TokenType.INT, TokenType.STRING_TYPE)) {
            return variableDeclaration(previous());
        }

        if (match(TokenType.IF)) {
            return ifStatement();
        }

        if (match(TokenType.WHILE)) {
            return whileStatement();
        }

        if (check(TokenType.IDENTIFIER)
                && checkNext(TokenType.ASSIGN)) {

            return assignment();
        }

        throw error(
                peek(),
                "Expected a statement."
        );
    }

    private Statement variableDeclaration(Token typeToken) {

        Token name = consume(
                TokenType.IDENTIFIER,
                "Expected variable name."
        );

        consume(
                TokenType.ASSIGN,
                "Expected '=' after variable name."
        );

        Expression initializer = expression();

        consume(
                TokenType.SEMICOLON,
                "Expected ';' after variable declaration."
        );

        return new VariableDeclaration(
                typeToken.getType(),
                name.getLexeme(),
                initializer
        );
    }

    private Statement assignment() {

        Token name = consume(
                TokenType.IDENTIFIER,
                "Expected variable name."
        );

        consume(
                TokenType.ASSIGN,
                "Expected '=' after variable name."
        );

        Expression value = expression();

        consume(
                TokenType.SEMICOLON,
                "Expected ';' after assignment."
        );

        return new AssignmentStatement(
                name.getLexeme(),
                value
        );
    }

    private Statement ifStatement() {

        consume(
                TokenType.LEFT_PAREN,
                "Expected '(' after যদি."
        );

        Expression condition = expression();

        consume(
                TokenType.RIGHT_PAREN,
                "Expected ')' after condition."
        );

        BlockStatement thenBranch = block();
        BlockStatement elseBranch = null;

        if (match(TokenType.ELSE)) {
            elseBranch = block();
        }

        return new IfStatement(
                condition,
                thenBranch,
                elseBranch
        );
    }

    private Statement whileStatement() {

        consume(
                TokenType.LEFT_PAREN,
                "Expected '(' after যতক্ষণ."
        );

        Expression condition = expression();

        consume(
                TokenType.RIGHT_PAREN,
                "Expected ')' after condition."
        );

        return new WhileStatement(
                condition,
                block()
        );
    }

    private BlockStatement block() {

        consume(
                TokenType.LEFT_BRACE,
                "Expected '{' before block."
        );

        List<Statement> statements = new ArrayList<>();

        while (!check(TokenType.RIGHT_BRACE)
                && !isAtEnd()) {

            Statement statement =
                    parseStatementSafely();

            if (statement != null) {
                statements.add(statement);
            }
        }

        consume(
                TokenType.RIGHT_BRACE,
                "Expected '}' after block."
        );

        return new BlockStatement(statements);
    }

    private Expression expression() {
        return comparison();
    }

    private Expression comparison() {

        Expression expr = addition();

        while (match(
                TokenType.EQUAL_EQUAL,
                TokenType.NOT_EQUAL,
                TokenType.LESS,
                TokenType.LESS_EQUAL,
                TokenType.GREATER,
                TokenType.GREATER_EQUAL)) {

            Token operator = previous();
            Expression right = addition();

            expr = new BinaryExpression(
                    expr,
                    operator,
                    right
            );
        }

        return expr;
    }

    private Expression addition() {

        Expression expr = multiplication();

        while (match(
                TokenType.PLUS,
                TokenType.MINUS)) {

            Token operator = previous();
            Expression right = multiplication();

            expr = new BinaryExpression(
                    expr,
                    operator,
                    right
            );
        }

        return expr;
    }

    private Expression multiplication() {

        Expression expr = primary();

        while (match(
                TokenType.STAR,
                TokenType.SLASH)) {

            Token operator = previous();
            Expression right = primary();

            expr = new BinaryExpression(
                    expr,
                    operator,
                    right
            );
        }

        return expr;
    }

    private Expression primary() {

        if (match(TokenType.NUMBER)) {

            return new LiteralExpression(
                    Integer.parseInt(
                            previous().getLexeme()
                    )
            );
        }

        if (match(TokenType.STRING)) {

            String text =
                    previous().getLexeme();

            return new LiteralExpression(
                    text.substring(
                            1,
                            text.length() - 1
                    )
            );
        }

        if (match(TokenType.IDENTIFIER)) {

            return new VariableExpression(
                    previous().getLexeme()
            );
        }

        if (match(TokenType.LEFT_PAREN)) {

            Expression expr = expression();

            consume(
                    TokenType.RIGHT_PAREN,
                    "Expected ')' after expression."
            );

            return expr;
        }

        throw error(
                peek(),
                "Expected expression."
        );
    }

    private boolean match(TokenType... types) {

        for (TokenType type : types) {

            if (check(type)) {
                advance();
                return true;
            }
        }

        return false;
    }

    private Token consume(
            TokenType type,
            String message) {

        if (check(type)) {
            return advance();
        }

        throw error(peek(), message);
    }

    private ParseException error(
            Token token,
            String message) {

        errors.add(
                new ParserError(
                        message,
                        token.getLine(),
                        token.getColumn()
                )
        );

        return new ParseException();
    }

    private void synchronize() {

        if (!isAtEnd()) {
            advance();
        }

        while (!isAtEnd()) {

            if (previous().getType()
                    == TokenType.SEMICOLON) {

                return;
            }

            switch (peek().getType()) {

                case INT,
                     STRING_TYPE,
                     IF,
                     WHILE,
                     IDENTIFIER,
                     RIGHT_BRACE -> {
                    return;
                }

                default -> advance();
            }
        }
    }

    private boolean check(TokenType type) {

        return !isAtEnd()
                && peek().getType() == type;
    }

    private boolean checkNext(TokenType type) {

        return current + 1 < tokens.size()
                && tokens.get(current + 1).getType()
                == type;
    }

    private Token advance() {

        if (!isAtEnd()) {
            current++;
        }

        return previous();
    }

    private boolean isAtEnd() {
        return peek().getType() == TokenType.EOF;
    }

    private Token peek() {
        return tokens.get(current);
    }

    private Token previous() {
        return tokens.get(current - 1);
    }

    private static class ParseException
            extends RuntimeException {
    }
}
